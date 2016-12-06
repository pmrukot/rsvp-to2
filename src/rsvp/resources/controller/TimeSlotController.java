package rsvp.resources.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalTimeStringConverter;
import rsvp.resources.model.TimeSlot;

import java.sql.Time;
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
    Button createButton;
    
    @FXML
    Button deleteButton;

    ObservableList<TimeSlot> items = FXCollections.observableArrayList(
            new TimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:30")),
            new TimeSlot(LocalTime.parse("09:30"), LocalTime.parse("11:05")),
            new TimeSlot(LocalTime.parse("11:15"), LocalTime.parse("12:45")),
            new TimeSlot(LocalTime.parse("12:50"), LocalTime.parse("14:20")),
            new TimeSlot(LocalTime.parse("14:40"), LocalTime.parse("16:10")),
            new TimeSlot(LocalTime.parse("16:15"), LocalTime.parse("17:45")),
            new TimeSlot(LocalTime.parse("17:50"), LocalTime.parse("19:20")),
            new TimeSlot(LocalTime.parse("19:30"), LocalTime.parse("21:00"))
    );

    @FXML
    private void initialize() {
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
        startTimeColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TimeSlot, LocalTime>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TimeSlot, LocalTime> t) {
                        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
                        chosenTimeSlot.setStartTime(t.getNewValue());
                        ((TimeSlot) t.getTableView().getItems().get(t.getTablePosition().getRow())).setStartTime(t.getNewValue());
                    }
                }
        );

        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
        endTimeColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TimeSlot, LocalTime>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TimeSlot, LocalTime> t) {
                        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
                        chosenTimeSlot.setEndTime(t.getNewValue());
                        ((TimeSlot) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEndTime(t.getNewValue());
                    }
                }
        );

        timeSlotListTableView.setItems(items);
    }

    @FXML
    private void handleCreateButtonAction(javafx.event.ActionEvent event) {
        String startTime = startTimeFieldCreate.getText();
        String endTime = endTimeFieldCreate.getText();

        items.add(new TimeSlot(LocalTime.parse(startTime), LocalTime.parse(endTime)));
        startTimeFieldCreate.clear();
        endTimeFieldCreate.clear();
    }
    
    @FXML
    private void handleDeleteButtonAction(javafx.event.ActionEvent event) {
        TimeSlot chosenTimeSlot = timeSlotListTableView.getSelectionModel().getSelectedItem();
        items.remove(chosenTimeSlot);
    }
}
