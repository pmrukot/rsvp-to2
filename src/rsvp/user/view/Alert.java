package rsvp.user.view;


import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alert {
    String content;
    javafx.scene.control.Alert alert;

    public Alert(String content, AlertType alertType) {
        this.content = content;
        alert = new javafx.scene.control.Alert(alertType);
        setAlertTitle(alertType);
        alert.setHeaderText(content);
    }

    private void setAlertTitle(AlertType alertType) {
        switch (alertType) {
            case NONE:
                break;
            case INFORMATION:
                alert.setTitle("Information Dialog");
                break;
            case WARNING:
                break;
            case CONFIRMATION:
                alert.setTitle("Confirmation Dialog");
                break;
            case ERROR:
                alert.setTitle("Error Dialog");
                break;
        }
    }

    public Optional<ButtonType> showAndWait() {
        return alert.showAndWait();
    }
}
