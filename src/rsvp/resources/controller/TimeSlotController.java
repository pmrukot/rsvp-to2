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
    private static final String NO_TIMESLOT_SELECTED_ALERT = "You have to select some time slot in order to do modification";
    private static final String IMPROPER_HOUR_FORMAT = "You have to provide valid hour format (hh:mm)";

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
    private  TextField startTimeFieldUpdate;

    @FXML
    private  TextField endTimeFieldUpdate;

    ObservableList<TimeSlot> items;
    TimeSlotDAO timeSlotDAO;

    Alert errorAlert;

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
        LocalTime startTime, endTime;
        try {
            startTime = LocalTime.parse(startTimeFieldCreate.getText());
            endTime = LocalTime.parse(endTimeFieldCreate.getText());
        } catch(DateTimeParseException e){
            errorAlert.setContentText(IMPROPER_HOUR_FORMAT);
            errorAlert.showAndWait();
            return;
        }

        TimeSlot createdTimeSlot = new TimeSlot(startTime, endTime);
        items.add(createdTimeSlot);
        timeSlotDAO.create(createdTimeSlot);
        startTimeFieldCreate.clear();
        endTimeFieldCreate.clear();
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
        if (chosenTimeSlot == null){
            errorAlert.setContentText(NO_TIMESLOT_SELECTED_ALERT);
            errorAlert.showAndWait();
            return;
        }
        timeSlotDAO.delete(chosenTimeSlot);
        items.remove(chosenTimeSlot);
    }
    
    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        LocalTime newStartTime = LocalTime.parse(startTimeFieldUpdate.getText());
        LocalTime newEndTime = LocalTime.parse(endTimeFieldUpdate.getText());
        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();

        if (chosenTimeSlot == null){
            errorAlert.setContentText(NO_TIMESLOT_SELECTED_ALERT);
            errorAlert.showAndWait();
            return;
        }

        if(!chosenTimeSlot.getStartTime().equals(newStartTime) || !chosenTimeSlot.getEndTime().equals(newEndTime)) {
            timeSlotDAO.update(chosenTimeSlot, newStartTime, newEndTime);
            startTimeFieldUpdate.clear();
            endTimeFieldUpdate.clear();
            items.clear();
            items.addAll(timeSlotDAO.getAll());
            timeSlotListTableView.setItems(items);
        }
    }
}
