package rsvp.resources.validation;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import rsvp.resources.model.TimeSlot;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TimeSlotValidation extends Validation {
    private static final String NO_ITEM_SELECTED_ALERT = "You have to select some time slot in order to do modification";
    private static final String IMPROPER_HOUR_FORMAT_ALERT = "You have to provide valid hour format (hh:mm)";
    private static final String NO_MODYFICATION_ALERT = "You have to provide different values than before";
    private static final String NOT_ENOUGH_ARGUMENTS_ALERT = "You have to provide all arguments";
    private static final String NON_CHRONOLOGICAL_ORDER_ALERT = "You have to provide start time earlier than end time";
    private static final String COLLISION_ALERT = "You have to provide time slot not colliding with existing ones";

    private static boolean isColliding(ObservableList<TimeSlot> items, LocalTime insertedStartTime, LocalTime insertedEndTime) {
        for(TimeSlot item : items) {
            LocalTime currentStartTime = item.getStartTime();
            LocalTime currentEndTime = item.getEndTime();
            if(insertedStartTime.isBefore(currentStartTime) && insertedEndTime.isAfter(currentStartTime)) return true;
            if(insertedStartTime.equals(currentStartTime) || insertedEndTime.equals(currentEndTime)) return true;
            if(insertedStartTime.isAfter(currentStartTime) && insertedEndTime.isBefore(currentEndTime)) return true;
            if(insertedStartTime.isBefore(currentEndTime) && insertedEndTime.isAfter(currentEndTime)) return true;
        }
        return false;
    }

    private static boolean notChronological(LocalTime startTime, LocalTime endTime) {
        return !startTime.isBefore(endTime);
    }

    private static boolean noItemSelected(TimeSlot timeSlot) {
        return timeSlot == null;
    }

    public static boolean createValidationPassed(Alert errorAlert, ObservableList<TimeSlot> items, TextField startTimeFieldCreate, TextField endTimeFieldCreate) {
        if (notEnoughArguments(startTimeFieldCreate, endTimeFieldCreate)) {
            handleErrorAlert(errorAlert, startTimeFieldCreate, endTimeFieldCreate, NOT_ENOUGH_ARGUMENTS_ALERT);
            return false;
        }

        LocalTime startTime, endTime;
        try {
            startTime = LocalTime.parse(startTimeFieldCreate.getText());
            endTime = LocalTime.parse(endTimeFieldCreate.getText());
        } catch (DateTimeParseException e) {
            handleErrorAlert(errorAlert, startTimeFieldCreate, endTimeFieldCreate, IMPROPER_HOUR_FORMAT_ALERT);
            return false;
        }

        if (notChronological(startTime, endTime)) {
            handleErrorAlert(errorAlert, startTimeFieldCreate, endTimeFieldCreate, NON_CHRONOLOGICAL_ORDER_ALERT);
            return false;
        }

        if (isColliding(items, startTime, endTime)) {
            handleErrorAlert(errorAlert, startTimeFieldCreate, endTimeFieldCreate, COLLISION_ALERT);
            return false;
        }
        return true;
    }

    public static boolean deleteValidationPassed(Alert errorAlert, TimeSlot chosenTimeSlot) {
        if (noItemSelected(chosenTimeSlot)) {
            showError(errorAlert, NO_ITEM_SELECTED_ALERT);
            return false;
        }
        return true;
    }

    public static boolean updateValidationPassed(Alert errorAlert, ObservableList<TimeSlot> items, TextField startTimeFieldUpdate, TextField endTimeFieldUpdate, TimeSlot chosenTimeSlot) {
        if (noItemSelected(chosenTimeSlot)) {
            handleErrorAlert(errorAlert, startTimeFieldUpdate, endTimeFieldUpdate, NO_ITEM_SELECTED_ALERT);
            return false;
        }

        if (notEnoughArguments(startTimeFieldUpdate, endTimeFieldUpdate)) {
            handleErrorAlert(errorAlert, startTimeFieldUpdate, endTimeFieldUpdate, NOT_ENOUGH_ARGUMENTS_ALERT);
            return false;
        }

        LocalTime newStartTime, newEndTime;
        try {
            newStartTime = LocalTime.parse(startTimeFieldUpdate.getText());
            newEndTime = LocalTime.parse(endTimeFieldUpdate.getText());
        } catch (DateTimeParseException e) {
            handleErrorAlert(errorAlert, startTimeFieldUpdate, endTimeFieldUpdate, IMPROPER_HOUR_FORMAT_ALERT);
            return false;
        }

        if (notChronological(newStartTime, newEndTime)) {
            handleErrorAlert(errorAlert, startTimeFieldUpdate, endTimeFieldUpdate, NON_CHRONOLOGICAL_ORDER_ALERT);
            return false;
        }

        if (chosenTimeSlot.getStartTime().equals(newStartTime) && chosenTimeSlot.getEndTime().equals(newEndTime)) {
            handleErrorAlert(errorAlert, startTimeFieldUpdate, endTimeFieldUpdate, NO_MODYFICATION_ALERT);
            return false;
        }

        if (isColliding(items, newStartTime, newEndTime)) {
            handleErrorAlert(errorAlert, startTimeFieldUpdate, endTimeFieldUpdate, COLLISION_ALERT);
            return false;
        }
        return true;
    }

    public static boolean isBetweenTimeSlots(TimeSlot start, TimeSlot end, TimeSlot timeSlot){
        return (timeSlot.getEndTime().isBefore(end.getStartTime()) &&
                timeSlot.getStartTime().isAfter(start.getEndTime())) || start.equals(timeSlot) || end.equals(timeSlot);
    }
}
