package rsvp.booking.controller;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;

/**
 * Created by Piotr on 04.12.2016.
 */
public class EditController {

    private Stage dialogStage;

    private BookingController bookingController;


    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private TextField userId;

    @FXML
    private TextField roomId;

    @FXML
    private Button saveButton;


    @FXML
    public void save(){
        Booking booking = new Booking();
        Date date = Date.valueOf(reservationDatePicker.getValue());
        Long userId = Long.parseLong(this.userId.getText());
        Long roomId = Long.parseLong(this.roomId.getText());

        booking.setReservationDate(date);
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        saveBookingToDatabase(booking);

        reservationDatePicker.getEditor().setText(null);
        reservationDatePicker.setValue(null);
        this.dialogStage.close();
    }

    @FXML
    private void saveBookingToDatabase(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(booking);

        transaction.commit();
        session.close();
        bookingController.addBooking(booking);
    }

    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
