package rsvp.resources.validation;

import javafx.scene.control.TextField;
import rsvp.resources.model.UniversityRoom;

public class UniversityRoomManager {
    private static final String CAPACITY_ALERT = "You have to provide capacity greater than 0 and lesser than 200";
    private static final String NO_ITEM_SELECTED_ALERT = "You have to select some room in order to do modification";
    private static final String IMPROPER_NUMBER_FORMAT_ALERT = "You have to provide valid number format";
    private static final String NO_MODYFICATION_ALERT = "You have to provide different values than before";
    private static final String NOT_ENOUGH_ARGUMENTS_ALERT = "You have to provide all arguments";

    private static boolean notEnoughArguments(TextField firstTextField, TextField secondTextField) {
        return firstTextField.getText().isEmpty() || secondTextField.getText().isEmpty();
    }

    public static String createValidation(TextField numberFieldCreate, TextField capacityFieldCreate) {
        if (notEnoughArguments(numberFieldCreate, capacityFieldCreate)) {
            return NOT_ENOUGH_ARGUMENTS_ALERT;
        }

        Integer capacity;
        try {
            capacity = Integer.parseInt(capacityFieldCreate.getText());
        } catch (NumberFormatException e) {
            return IMPROPER_NUMBER_FORMAT_ALERT;
        }

        if (capacity < 1) {
            return CAPACITY_ALERT;
        }
        return null;
    }

    public static String deleteValidation(UniversityRoom chosenUniversityRoom) {
        if (chosenUniversityRoom == null) {
            return NO_ITEM_SELECTED_ALERT;
        }
        return null;
    }

    public static String updateValidation(TextField numberFieldUpdate, TextField capacityFieldUpdate, UniversityRoom chosenUniversityRoom) {
        if (notEnoughArguments(numberFieldUpdate, capacityFieldUpdate)) {
            return NOT_ENOUGH_ARGUMENTS_ALERT;
        }

        String newNumber = numberFieldUpdate.getText();
        Integer newCapacity;
        try {
            newCapacity = Integer.parseInt(capacityFieldUpdate.getText());
        } catch (NumberFormatException e) {
            return IMPROPER_NUMBER_FORMAT_ALERT;
        }

        if (chosenUniversityRoom == null) {
            return NO_ITEM_SELECTED_ALERT;
        }

        if (chosenUniversityRoom.getNumber().equals(newNumber) && chosenUniversityRoom.getCapacity().equals(newCapacity)) {
            return NO_MODYFICATION_ALERT;
        }

        if (newCapacity < 1) {
            return CAPACITY_ALERT;
        }
        return null;
    }
}
