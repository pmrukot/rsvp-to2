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
import rsvp.resources.validation.TimeSlotValidation;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TimeSlotController {

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
        showError(alertMessage);
        clearFields(firstTextField, secondTextField);
    }

    private void showError(String alertMessage) {
        errorAlert.setContentText(alertMessage);
        errorAlert.showAndWait();
    }

    private void clearFields(TextField firstTextField, TextField secondTextField) {
        firstTextField.clear();
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
        String validationOutput = TimeSlotValidation.createValidation(items, startTimeFieldCreate, endTimeFieldCreate);
        if (validationOutput != null) {
            handleErrorAlert(startTimeFieldCreate, endTimeFieldCreate, validationOutput);
            return;
        }

        LocalTime startTime = LocalTime.parse(startTimeFieldCreate.getText());
        LocalTime endTime = LocalTime.parse(endTimeFieldCreate.getText());
        TimeSlot createdTimeSlot = new TimeSlot(startTime, endTime);
        items.add(createdTimeSlot);
        timeSlotDAO.create(createdTimeSlot);
        clearFields(startTimeFieldCreate, endTimeFieldCreate);
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
        String validationOutput = TimeSlotValidation.deleteValidation(chosenTimeSlot);
        if (validationOutput != null) {
            showError(validationOutput);
            return;
        }

        timeSlotDAO.delete(chosenTimeSlot);
        items.remove(chosenTimeSlot);
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
        String validationOutput = TimeSlotValidation.updateValidation(items, startTimeFieldUpdate, endTimeFieldUpdate, chosenTimeSlot);
        if (validationOutput != null) {
            handleErrorAlert(startTimeFieldUpdate, endTimeFieldUpdate, validationOutput);
            return;
        }

        LocalTime newStartTime = LocalTime.parse(startTimeFieldUpdate.getText());
        LocalTime newEndTime = LocalTime.parse(endTimeFieldUpdate.getText());
        timeSlotDAO.update(chosenTimeSlot, newStartTime, newEndTime);
        clearFields(startTimeFieldUpdate, endTimeFieldUpdate);
        items.clear();
        items.addAll(timeSlotDAO.getAll());
        timeSlotListTableView.setItems(items);
    }
}
