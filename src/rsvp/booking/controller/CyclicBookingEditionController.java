package rsvp.booking.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.alerts.FxAlerts;
import rsvp.booking.model.Booking;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;

import java.util.List;

public class CyclicBookingEditionController {
    private FxAlerts fxAlerts = new FxAlerts();
    private BookingController bookingController;
    private Stage dialogStage;
    private Booking booking;
    private List<Booking> cyclicBookings;
    private DBBookingDAO dbBookingDAO = new DBBookingDAO();

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
        this.booking = booking;
        this.cyclicBookings = dbBookingDAO.getCyclicBookings(booking.getRootId());
        this.universityRoom.getItems().addAll(bookingController.getUniversityRooms());
        this.firstTimeSlot.getItems().addAll(bookingController.getTimeSlots());
        this.lastTimeSlot.getItems().addAll(bookingController.getTimeSlots());
        this.universityRoom.setValue(booking.getUniversityRoom());
        this.firstTimeSlot.setValue(booking.getFirstSlot());
        this.lastTimeSlot.setValue(booking.getLastSlot());
    }

    @FXML
    private void processBooking() {
        for(Booking booking : this.cyclicBookings) {
            updateBooking(booking);
            persistBooking(booking);
        }
        dialogStage.close();
    }

    private void updateBooking(Booking booking) {
        booking.setFirstSlot(firstTimeSlot.getValue());
        booking.setLastSlot(lastTimeSlot.getValue());
        booking.setUniversityRoom(universityRoom.getValue());
    }

    private void persistBooking(Booking booking) {
        if(booking.isValid() && !isOverlappingOtherBookings()) {
            dbBookingDAO.updateBooking(booking);
        } else {
            fxAlerts.showOverlappingAlert();
        }
    }

    private boolean isOverlappingOtherBookings() {
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


    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }
}
