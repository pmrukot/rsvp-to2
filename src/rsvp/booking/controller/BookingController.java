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
    public void openEditWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/EditBookingPane.fxml"));
            VBox editPage = (VBox) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Booking");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editPage);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            EditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookingController(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
