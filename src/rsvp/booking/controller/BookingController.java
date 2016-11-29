package booking.controller;

import booking.Main;
import booking.model.Booking;
import booking.persistence.HibernateUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.sql.Date;

public class BookingController {

    private Stage primaryStage;

    public BookingController() {}

    public BookingController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private DatePicker reservationDate;

    @FXML
    private Button createButton;


    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BookingOverviewPane.fxml"));
            BorderPane rootLayout = (BorderPane) loader.load();

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
        Date date = Date.valueOf(reservationDate.getValue());

        booking.setReservationDate(date);
        saveBookingToDatabase(booking);

        reservationDate.getEditor().setText(null);
        reservationDate.setValue(null);
    }

    private void saveBookingToDatabase(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(booking);

        transaction.commit();
        session.close();
    }
}
