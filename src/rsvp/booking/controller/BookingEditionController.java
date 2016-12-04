package rsvp.booking.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import rsvp.booking.model.Booking;


public class BookingEditionController {
    private Stage dialogStage;
    private Booking booking;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private Button updateButton;

    @FXML
    private Button closeButton;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Booking booking) {
        this.booking = booking;
        this.reservationDatePicker.setValue(booking.getReservationDate().toLocalDate());
    }

    @FXML
    private void updateBooking() {

    }

    @FXML
    private void closeDialog() {

    }
}
