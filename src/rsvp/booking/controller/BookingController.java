package rsvp.booking.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import rsvp.booking.Main;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Date> reservationDate;


    @FXML
    private void initialize() {
        reservationDate.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getReservationDate()));
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
    public void createBooking() {
        Booking booking = new Booking();
        Date date = Date.valueOf(reservationDatePicker.getValue());

        booking.setReservationDate(date);
        saveBookingToDatabase(booking);

        reservationDatePicker.getEditor().setText(null);
        reservationDatePicker.setValue(null);
    }

    private void saveBookingToDatabase(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(booking);

        transaction.commit();
        session.close();
        bookings.add(booking);
    }

    @FXML
    public void deleteBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        deleteBookingFromDatabase(selectedBooking);
        bookingsTable.getItems().remove(selectedBooking);
    }

    private void deleteBookingFromDatabase(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.delete(booking);

        transaction.commit();
        session.close();
    }

    private List<Booking> listBooking() {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        List<Booking> result = session.createQuery("from Booking b", Booking.class).getResultList();

        transaction.commit();
        session.close();
        return result;
    }

    public void setData() {
        bookings.addAll(listBooking());
        bookingsTable.setItems(bookings);
    }
}
