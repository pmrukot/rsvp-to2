package rsvp.resources.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalTimeStringConverter;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;
import java.time.LocalTime;

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
    private  TextField startTimeFieldUpdate;

    @FXML
    private  TextField endTimeFieldUpdate;

    ObservableList<TimeSlot> items;

    @FXML
    private void initialize() {
        items = FXCollections.observableArrayList();

        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));

        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));

        items.addAll(TimeSlotDAO.getAll());
        timeSlotListTableView.setItems(items);
    }

    @FXML
    private void handleCreateButtonAction(ActionEvent event) {
        LocalTime startTime = LocalTime.parse(startTimeFieldCreate.getText());
        LocalTime endTime = LocalTime.parse(endTimeFieldCreate.getText());

        TimeSlot createdTimeSlot = new TimeSlot(startTime, endTime);
        items.add(createdTimeSlot);
        TimeSlotDAO.create(createdTimeSlot);
        startTimeFieldCreate.clear();
        endTimeFieldCreate.clear();
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
        TimeSlotDAO.delete(chosenTimeSlot);
        items.remove(chosenTimeSlot);
    }
    
    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        LocalTime newStartTime = LocalTime.parse(startTimeFieldUpdate.getText());
        LocalTime newEndTime = LocalTime.parse(endTimeFieldUpdate.getText());
        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();

        if(!chosenTimeSlot.getStartTime().equals(newStartTime) || !chosenTimeSlot.getEndTime().equals(newEndTime)) {
            TimeSlotDAO.update(chosenTimeSlot, newStartTime, newEndTime);
            startTimeFieldUpdate.clear();
            endTimeFieldUpdate.clear();
            //TODO refresh timeSlotListTableView
        }
    }
}
