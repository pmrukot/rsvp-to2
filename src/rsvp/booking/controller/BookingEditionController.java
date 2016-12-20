package rsvp.booking.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;

import java.sql.Date;


public class BookingEditionController {
    private DBBookingDAO dbBookingDao = new DBBookingDAO();

    private BookingController bookingController;
    private Stage dialogStage;
    private Booking booking;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private ComboBox<UniversityRoom> universityRoom;

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
        } catch (NullPointerException ignored) {}
        this.universityRoom.getItems().addAll(bookingController.getUniversityRooms());
        this.firstTimeSlot.getItems().addAll(bookingController.getTimeSlots());
        this.lastTimeSlot.getItems().addAll(bookingController.getTimeSlots());
        this.universityRoom.setValue(booking.getUniversityRoom());
        this.firstTimeSlot.setValue(booking.getFirstSlot());
        this.lastTimeSlot.setValue(booking.getLastSlot());
    }

    @FXML
    private void updateBooking() {
        try {
            Date date = Date.valueOf(reservationDatePicker.getValue());
            TimeSlot firstSlot = firstTimeSlot.getValue();
            TimeSlot lastSlot = lastTimeSlot.getValue();
            UniversityRoom pickedUniversityRoom = universityRoom.getValue();
            booking.setFirstSlot(firstSlot);
            booking.setLastSlot(lastSlot);
            booking.setReservationDate(date);
            booking.setUniversityRoom(pickedUniversityRoom);
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
