package rsvp.booking.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import rsvp.booking.Main;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class BookingController {

    private Stage primaryStage;

    public BookingController() {}

    public BookingController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button participantsButton;

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Date> reservationDate;

    @FXML
    private TableColumn<Booking, Long> userId;

    @FXML
    private TableColumn<Booking, Long> roomId;

    @FXML
    private void initialize() {
        reservationDate.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getReservationDate()));
        userId.setCellValueFactory(cellData -> new SimpleObjectProperty<Long>(cellData.getValue().getUserId()));
        roomId.setCellValueFactory(cellData -> new SimpleObjectProperty<Long>(cellData.getValue().getRoomId()));
        setData();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BookingOverviewPane.fxml"));
            VBox rootLayout = (VBox) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void deleteBooking() {
        try{
            Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
            deleteBookingFromDatabase(selectedBooking);
            bookingsTable.getItems().remove(selectedBooking);
        } catch (NullPointerException ignored) {}

    }

    private void deleteBookingFromDatabase(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.delete(booking);

        transaction.commit();
        session.close();
    }

    @FXML
    public void handleCreateAction() {
        Booking booking = new Booking();
        booking.markAsNewRecord(true);
        editBooking(booking);
        booking.markAsNewRecord(false);
    }

    @FXML
    public void handleEditAction() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        editBooking(selectedBooking);
    }

    private void editBooking(Booking booking) {
        try {
            final Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BookingEditionPane.fxml"));
            VBox bookingEditionLayout = (VBox) loader.load();
            Scene scene = new Scene(bookingEditionLayout, 300, 200);

            BookingEditionController bookingEditionController = loader.getController();
            bookingEditionController.setDialogStage(dialogStage);
            bookingEditionController.setBookingController(this);
            bookingEditionController.setData(booking);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {}

        bookingsTable.refresh();
    }

    @FXML
    public void handleEditParticipantsAction() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        editParticipants(selectedBooking);
    }

    private void editParticipants(Booking booking) {
        try {
            final Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BookingParticipantsEditionPane.fxml"));
            VBox bookingParticipantsEditionLayout = (VBox) loader.load();
            Scene scene = new Scene(bookingParticipantsEditionLayout, 300, 200);

            BookingParticipantsEditionController bookingParticipantsEditionController = loader.getController();
            bookingParticipantsEditionController.setDialogStage(dialogStage);
            bookingParticipantsEditionController.setBookingController(this);
            bookingParticipantsEditionController.setData(booking);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {}

        bookingsTable.refresh();
    }

    private List<Booking> listBooking() {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        List<Booking> result = session.createQuery("from Booking b", Booking.class).getResultList();

        transaction.commit();
        session.close();
        return result;
    }

    public void addBooking(Booking booking){
        bookings.add(booking);
    }

    public void setData() {
        bookings.addAll(listBooking());
        bookingsTable.setItems(bookings);
    }
}
