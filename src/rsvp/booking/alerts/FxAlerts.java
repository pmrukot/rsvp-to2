package rsvp.booking.alerts;

import javafx.scene.control.Alert;

public class FxAlerts {

    public void showOverlappingAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Invalid Booking Data");
        alert.setContentText("Make sure you populated fields with appropriate values");

        alert.showAndWait();
    }

}
