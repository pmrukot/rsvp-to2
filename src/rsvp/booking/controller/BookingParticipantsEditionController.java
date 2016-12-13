package rsvp.booking.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;

import java.sql.Date;
import java.util.List;


public class BookingParticipantsEditionController {
    private BookingController bookingController;
    private Stage dialogStage;
    private Booking booking;

    @FXML
    private Button saveButton;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Booking booking) {
        this.booking = booking;
    }

    @FXML
    private void updateBooking() {

    }

    private void updateBookingToDatabase(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.update(booking);

        transaction.commit();
        session.close();
    }

    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }
}
