package rsvp.booking.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;

import java.sql.Date;


public class BookingEditionController {
    private Stage dialogStage;
    private Booking booking;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private Button updateButton;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Booking booking) {
        this.booking = booking;
        this.reservationDatePicker.setValue(booking.getReservationDate().toLocalDate());
    }

    @FXML
    private void updateBooking() {
        try {
            Date date = Date.valueOf(reservationDatePicker.getValue());
            booking.setReservationDate(date);
            updateBookingToDatabase(booking);
            dialogStage.close();
        } catch (NullPointerException ignored) {}
    }

    private void updateBookingToDatabase(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.update(booking);

        transaction.commit();
        session.close();
    }
}
