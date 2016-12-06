package rsvp.resources.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import rsvp.resources.model.UniversityRoom;
import rsvp.resources.view.CalendarCell;

public class UniversityRoomController {
    
    @FXML
    TableView<UniversityRoom> universityRoomListTableView;

    @FXML
    TableColumn<UniversityRoom, String> numberColumn;

    @FXML
    TableColumn<UniversityRoom, Integer> capacityColumn;

    @FXML
    TableColumn<UniversityRoom, Boolean> calendarColumn;

    @FXML
    private TextField numberFieldCreate;

    @FXML
    private TextField capacityFieldCreate;

    @FXML
    Button createButton;

    @FXML
    Button deleteButton;

    ObservableList<UniversityRoom> items = FXCollections.observableArrayList(
            new UniversityRoom("1.38", 300),
            new UniversityRoom("2.41", 150),
            new UniversityRoom("1.31", 50),
            new UniversityRoom("3.23", 40),
            new UniversityRoom("4.21", 35));

    @FXML
    private void initialize() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        numberColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<UniversityRoom, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<UniversityRoom, String> t) {
                        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
                        chosenUniversityRoom.setNumber(t.getNewValue());
                        ((UniversityRoom) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNumber(t.getNewValue());
                    }
                }
        );

        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        capacityColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<UniversityRoom, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<UniversityRoom, Integer> t) {
                        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
                        chosenUniversityRoom.setCapacity(t.getNewValue());
                        ((UniversityRoom) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCapacity(t.getNewValue());
                    }
                }
        );

        calendarColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<UniversityRoom, Boolean>,
                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<UniversityRoom, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        calendarColumn.setCellFactory(
                new Callback<TableColumn<UniversityRoom, Boolean>, TableCell<UniversityRoom, Boolean>>() {
                    @Override
                    public TableCell<UniversityRoom, Boolean> call(TableColumn<UniversityRoom, Boolean> p) {
                        return new CalendarCell();
                    }
                });

        universityRoomListTableView.setItems(items);
    }

    @FXML
    private void handleCreateButtonAction(ActionEvent event) {
        String number = numberFieldCreate.getText();
        Integer capacity = Integer.parseInt(capacityFieldCreate.getText());

        if (capacity > 0) {
            items.add(new UniversityRoom(number, capacity));
            numberFieldCreate.clear();
            capacityFieldCreate.clear();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
        items.remove(chosenUniversityRoom);
    }
}