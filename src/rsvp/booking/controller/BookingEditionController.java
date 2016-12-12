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


public class BookingEditionController {
    private BookingController bookingController;
    private Stage dialogStage;
    private Booking booking;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private TextField roomId;

    @FXML
    private Button updateButton;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Booking booking) {
        this.booking = booking;
        try {
            this.reservationDatePicker.setValue(booking.getReservationDate().toLocalDate());
            this.roomId.setText(String.valueOf(booking.getRoomId()));
        } catch (NullPointerException ignored) {}
    }

    @FXML
    private void updateBooking() {
        try {
            Date date = Date.valueOf(reservationDatePicker.getValue());
            Long room = Long.parseLong(roomId.getText());
            booking.setReservationDate(date);
            booking.setRoomId(room);
            if(booking.isNewRecord()) {
                createBookingToDatabase(booking);
            } else {
                updateBookingToDatabase(booking);
            }
            dialogStage.close();
        } catch (NullPointerException ignored) {}
    }


    private void createBookingToDatabase(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(booking);

        transaction.commit();
        session.close();

        bookingController.addBooking(booking);
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
