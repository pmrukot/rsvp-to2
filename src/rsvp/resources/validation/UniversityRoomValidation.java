package rsvp.resources.validation;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import rsvp.resources.model.UniversityRoom;

public class UniversityRoomValidation {
    private static final String CAPACITY_ALERT = "You have to provide capacity greater than 0";
    private static final String NO_ITEM_SELECTED_ALERT = "You have to select some room in order to do modification";
    private static final String IMPROPER_NUMBER_FORMAT_ALERT = "You have to provide valid number format";
    private static final String NO_MODYFICATION_ALERT = "You have to provide different values than before";
    private static final String NOT_ENOUGH_ARGUMENTS_ALERT = "You have to provide all arguments";

    private static void handleErrorAlert(Alert errorAlert, TextField firstTextField, TextField secondTextField, String alertMessage) {
        showError(errorAlert, alertMessage);
        clearFields(firstTextField, secondTextField);
    }

    private static void showError(Alert errorAlert, String alertMessage) {
        errorAlert.setContentText(alertMessage);
        errorAlert.showAndWait();
    }

    private static void clearFields(TextField firstTextField, TextField secondTextField) {
        firstTextField.clear();
        secondTextField.clear();
    }

    private static boolean notEnoughArguments(TextField firstTextField, TextField secondTextField) {
        return firstTextField.getText().isEmpty() || secondTextField.getText().isEmpty();
    }

    private static boolean noItemSelected(UniversityRoom universityRoom) {
        return universityRoom == null;
    }
    
    private static boolean wrongCapacity(Integer capacity) {
        return capacity < 1;
    }

    public static boolean createValidationPassed(Alert errorAlert, TextField numberFieldCreate, TextField capacityFieldCreate) {
        if (notEnoughArguments(numberFieldCreate, capacityFieldCreate)) {
            handleErrorAlert(errorAlert, numberFieldCreate, capacityFieldCreate, NOT_ENOUGH_ARGUMENTS_ALERT);
            return false;
        }

        Integer capacity;
        try {
            capacity = Integer.parseInt(capacityFieldCreate.getText());
        } catch (NumberFormatException e) {
            handleErrorAlert(errorAlert, numberFieldCreate, capacityFieldCreate, IMPROPER_NUMBER_FORMAT_ALERT);
            return false;
        }

        if (wrongCapacity(capacity)) {
            handleErrorAlert(errorAlert, numberFieldCreate, capacityFieldCreate, CAPACITY_ALERT);
            return false;
        }
        return true;
    }

    public static boolean deleteValidationPassed(Alert errorAlert, UniversityRoom chosenUniversityRoom) {
        if (noItemSelected(chosenUniversityRoom)) {
            showError(errorAlert, NO_ITEM_SELECTED_ALERT);
            return false;
        }
        return true;
    }

    public static boolean updateValidationPassed(Alert errorAlert, TextField numberFieldUpdate, TextField capacityFieldUpdate, UniversityRoom chosenUniversityRoom) {
        if (noItemSelected(chosenUniversityRoom)) {
            handleErrorAlert(errorAlert, numberFieldUpdate, capacityFieldUpdate, NO_ITEM_SELECTED_ALERT);
            return false;
        }

        if (notEnoughArguments(numberFieldUpdate, capacityFieldUpdate)) {
            handleErrorAlert(errorAlert, numberFieldUpdate, capacityFieldUpdate, NOT_ENOUGH_ARGUMENTS_ALERT);
            return false;
        }

        String newNumber = numberFieldUpdate.getText();
        Integer newCapacity;
        try {
            newCapacity = Integer.parseInt(capacityFieldUpdate.getText());
        } catch (NumberFormatException e) {
            handleErrorAlert(errorAlert, numberFieldUpdate, capacityFieldUpdate, IMPROPER_NUMBER_FORMAT_ALERT);
            return false;
        }

        if (chosenUniversityRoom.getNumber().equals(newNumber) && chosenUniversityRoom.getCapacity().equals(newCapacity)) {
            handleErrorAlert(errorAlert, numberFieldUpdate, capacityFieldUpdate, NO_MODYFICATION_ALERT);
            return false;
        }

        if (wrongCapacity(newCapacity)) {
            handleErrorAlert(errorAlert, numberFieldUpdate, capacityFieldUpdate, CAPACITY_ALERT);
            return false;
        }
        return true;
    }
}
