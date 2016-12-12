package rsvp.resources.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalTimeStringConverter;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TimeSlotController {
    private static final String NO_ITEM_SELECTED_ALERT = "You have to select some time slot in order to do modification";
    private static final String IMPROPER_HOUR_FORMAT_ALERT = "You have to provide valid hour format (hh:mm)";
    private static final String NO_MODYFICATION_ALERT = "You have to provide different values than before";
    private static final String NOT_ENOUGH_ARGUMENTS_ALERT = "You have to provide all arguments";
    private static final String NON_CHRONOLOGICAL_ORDER_ALERT = "Start time must be before end time";

    @FXML
    TableView<TimeSlot> timeSlotListTableView;

    @FXML
    TableColumn<TimeSlot, LocalTime> startTimeColumn;

    @FXML
    TableColumn<TimeSlot, LocalTime> endTimeColumn;

    @FXML
    private TextField startTimeFieldCreate;

    @FXML
    private TextField endTimeFieldCreate;

    @FXML
    private TextField startTimeFieldUpdate;

    @FXML
    private TextField endTimeFieldUpdate;

    ObservableList<TimeSlot> items;
    TimeSlotDAO timeSlotDAO;

    Alert errorAlert;

    private void handleErrorAlert(TextField firstTextField, TextField secondTextField, String alertMessage) {
        if (alertMessage != null) {
            errorAlert.setContentText(alertMessage);
            errorAlert.showAndWait();
        }
        if (firstTextField != null)
            firstTextField.clear();
        if (secondTextField != null)
            secondTextField.clear();
    }

    @FXML
    private void initialize() {
        errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error!");
        errorAlert.setHeaderText("Error while modyfying data");

        items = FXCollections.observableArrayList();
        timeSlotDAO = new TimeSlotDAO();

        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));

        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));

        items.addAll(timeSlotDAO.getAll());
        timeSlotListTableView.setItems(items);
    }

    @FXML
    private void handleCreateButtonAction(ActionEvent event) {
        if (startTimeFieldCreate.getText().isEmpty() || endTimeFieldCreate.getText().isEmpty()) {
            handleErrorAlert(startTimeFieldCreate, endTimeFieldCreate, NOT_ENOUGH_ARGUMENTS_ALERT);
            return;
        }

        LocalTime startTime, endTime;
        try {
            startTime = LocalTime.parse(startTimeFieldCreate.getText());
            endTime = LocalTime.parse(endTimeFieldCreate.getText());
        } catch (DateTimeParseException e) {
            handleErrorAlert(startTimeFieldCreate, endTimeFieldCreate, IMPROPER_HOUR_FORMAT_ALERT);
            return;
        }

        if (!startTime.isBefore(endTime)) {
            handleErrorAlert(startTimeFieldCreate, endTimeFieldCreate, NON_CHRONOLOGICAL_ORDER_ALERT);
            return;
        }

        TimeSlot createdTimeSlot = new TimeSlot(startTime, endTime);
        items.add(createdTimeSlot);
        timeSlotDAO.create(createdTimeSlot);
        handleErrorAlert(startTimeFieldCreate, endTimeFieldCreate, null);
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
        if (chosenTimeSlot == null) {
            handleErrorAlert(null, null, NO_ITEM_SELECTED_ALERT);
            return;
        }

        timeSlotDAO.delete(chosenTimeSlot);
        items.remove(chosenTimeSlot);
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        if (startTimeFieldUpdate.getText().isEmpty() || endTimeFieldUpdate.getText().isEmpty()) {
            handleErrorAlert(startTimeFieldUpdate, endTimeFieldUpdate, NOT_ENOUGH_ARGUMENTS_ALERT);
            return;
        }

        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
        if (chosenTimeSlot == null) {
            handleErrorAlert(startTimeFieldUpdate, endTimeFieldUpdate, NO_ITEM_SELECTED_ALERT);
            return;
        }

        LocalTime newStartTime, newEndTime;
        try {
            newStartTime = LocalTime.parse(startTimeFieldUpdate.getText());
            newEndTime = LocalTime.parse(endTimeFieldUpdate.getText());
        } catch (DateTimeParseException e) {
            handleErrorAlert(startTimeFieldUpdate, endTimeFieldUpdate, IMPROPER_HOUR_FORMAT_ALERT);
            return;
        }

        if (!newStartTime.isBefore(newEndTime)) {
            handleErrorAlert(startTimeFieldUpdate, endTimeFieldUpdate, NON_CHRONOLOGICAL_ORDER_ALERT);
            return;
        }

        if (chosenTimeSlot.getStartTime().equals(newStartTime) && chosenTimeSlot.getEndTime().equals(newEndTime)) {
            handleErrorAlert(startTimeFieldUpdate, endTimeFieldUpdate, NO_MODYFICATION_ALERT);
            return;
        }

        timeSlotDAO.update(chosenTimeSlot, newStartTime, newEndTime);
        handleErrorAlert(startTimeFieldUpdate, endTimeFieldUpdate, null);
        items.clear();
        items.addAll(timeSlotDAO.getAll());
        timeSlotListTableView.setItems(items);
    }
}
