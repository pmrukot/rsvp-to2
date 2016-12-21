package rsvp.resources.validation;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class Validation {
    protected static void handleErrorAlert(Alert errorAlert, TextField firstTextField, TextField secondTextField, String alertMessage) {
        showError(errorAlert, alertMessage);
        clearFields(firstTextField, secondTextField);
    }

    protected static void showError(Alert errorAlert, String alertMessage) {
        errorAlert.setContentText(alertMessage);
        errorAlert.showAndWait();
    }

    protected static void clearFields(TextField firstTextField, TextField secondTextField) {
        firstTextField.clear();
        secondTextField.clear();
    }

    protected static boolean notEnoughArguments(TextField firstTextField, TextField secondTextField) {
        return firstTextField.getText().isEmpty() || secondTextField.getText().isEmpty();
    }
}
