package rsvp.user.controller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rsvp.booking.DAO.BookingDAO;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.DAO.UniversityRoomDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;
import rsvp.user.view.Alert;
import java.sql.Date;
import java.util.List;

public class EditBookingController {
    private Booking booking;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private ComboBox<UniversityRoom> universityRoom;

    @FXML
    private ComboBox<TimeSlot> firstTimeSlot;

    @FXML
    private ComboBox<TimeSlot> lastTimeSlot;


    private UniversityRoomDAO universityRoomDAO = new UniversityRoomDAO();
    private TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
    private BookingDAO bookingDAO = new DBBookingDAO();
    private Stage dialogStage;


    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    @FXML
    void setData(Booking booking) {
        this.booking = booking;
        try {
            this.reservationDatePicker.setValue(booking.getReservationDate().toLocalDate());
        } catch (NullPointerException ignored) {}
        this.universityRoom.getItems().addAll(universityRoomDAO.getAll());
        this.firstTimeSlot.getItems().addAll(timeSlotDAO.getAll());
        this.lastTimeSlot.getItems().addAll(timeSlotDAO.getAll());
        this.universityRoom.setValue(booking.getUniversityRoom());
        this.firstTimeSlot.setValue(booking.getFirstSlot());
        this.lastTimeSlot.setValue(booking.getLastSlot());
}

    public void processBooking(ActionEvent actionEvent) {
        booking.setFirstSlot(firstTimeSlot.getValue());
        booking.setLastSlot(lastTimeSlot.getValue());
        booking.setReservationDate(Date.valueOf(reservationDatePicker.getValue()));
        booking.setUniversityRoom(universityRoom.getValue());
        persistBooking();
        dialogStage.close();

    }

    private void persistBooking() {
        if(booking.isValid() && !isOverlappingOtherBookings()) {
            bookingDAO.updateBooking(booking);
        } else {
            rsvp.user.view.Alert alert = new Alert("Make sure you populated fields with appropriate values",
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.showAndWait();
        }
    }

    private boolean isOverlappingOtherBookings() {
        List<Booking> bookings = bookingDAO.getAllBookingsForCurrentUser();
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
}
