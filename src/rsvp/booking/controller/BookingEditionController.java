package rsvp.booking.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;

import java.sql.Date;


public class BookingEditionController {
    private DBBookingDAO dbBookingDao = new DBBookingDAO();

    private BookingController bookingController;
    private Stage dialogStage;
    private Booking booking;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private TextField roomId;

    @FXML
    private ComboBox<TimeSlot> firstTimeSlot;

    @FXML
    private ComboBox<TimeSlot> lastTimeSlot;

    @FXML
    private Button updateButton;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Booking booking) {
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        this.booking = booking;
        try {
            this.reservationDatePicker.setValue(booking.getReservationDate().toLocalDate());
            this.roomId.setText(String.valueOf(booking.getRoomId()));
        } catch (NullPointerException ignored) {}
        this.firstTimeSlot.getItems().addAll(bookingController.getTimeSlots());
        this.lastTimeSlot.getItems().addAll(bookingController.getTimeSlots());
    }

    @FXML
    private void updateBooking() {
        try {
            Date date = Date.valueOf(reservationDatePicker.getValue());
            Long room = Long.parseLong(roomId.getText());
            TimeSlot firstSlot = firstTimeSlot.getValue();
            TimeSlot lastSlot = lastTimeSlot.getValue();
            booking.setFirstSlot(firstSlot);
            booking.setLastSlot(lastSlot);
            booking.setReservationDate(date);
            booking.setRoomId(room);
            if(booking.isNewRecord()) {
                dbBookingDao.createBooking(booking);
                bookingController.addBooking(booking);
            } else {
                dbBookingDao.updateBooking(booking);
            }
            dialogStage.close();
        } catch (NullPointerException ignored) {}
    }


    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }
}
