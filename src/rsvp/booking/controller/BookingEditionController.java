package rsvp.booking.controller;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
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
    private void processBooking() {
        try {
            updateBooking();
            persistBooking();
            dialogStage.close();
        } catch (NullPointerException ignored) {}
    }

    private void updateBooking() {
        booking.setFirstSlot(firstTimeSlot.getValue());
        booking.setLastSlot(lastTimeSlot.getValue());
        booking.setReservationDate(Date.valueOf(reservationDatePicker.getValue()));
        booking.setUniversityRoom(universityRoom.getValue());
    }

    private void persistBooking() {
        if(booking.isValid() && !isOverlappingOtherBookings()) {
            if(booking.isNewRecord()) {
                dbBookingDao.createBooking(booking);
                bookingController.addBooking(booking);
            } else {
                dbBookingDao.updateBooking(booking);
            }
        } else {
            showAlert();
        }
    }

    public boolean isOverlappingOtherBookings() {
        ObservableList<Booking> bookings = bookingController.getBookings();
        bookings.removeIf(booking1 -> booking1.getUniversityRoom().getId() != booking.getUniversityRoom().getId() ||
        booking1.getReservationDate().compareTo(booking.getReservationDate()) != 0);
        for(Booking iteratingBooking : bookings) {
            if(iteratingBooking.getId() != booking.getId()
                    && iteratingBooking.getStartTime().compareTo(booking.getEndTime()) <= 0
                    && iteratingBooking.getEndTime().compareTo(booking.getStartTime()) >= 0) {
                return true;
            }
        }
        return false;
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Invalid Booking Data");
        alert.setContentText("Make sure you populated fields with appropriate values");

        alert.showAndWait();
    }


    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }
}
