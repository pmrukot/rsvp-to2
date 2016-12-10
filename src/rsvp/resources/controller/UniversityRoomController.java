package rsvp.resources.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import rsvp.resources.DAO.UniversityRoomDAO;
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
    private TextField numberFieldUpdate;
    @FXML
    private TextField capacityFieldUpdate;

    ObservableList<UniversityRoom> items;

    @FXML
    private void initialize() {
        items = FXCollections.observableArrayList();

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        calendarColumn.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        calendarColumn.setCellFactory(p -> new CalendarCell());

        items.addAll(UniversityRoomDAO.getAll());
        universityRoomListTableView.setItems(items);
    }

    @FXML
    private void handleCreateButtonAction(ActionEvent event) {
        String number = numberFieldCreate.getText();
        Integer capacity = Integer.parseInt(capacityFieldCreate.getText());

        if (capacity > 0) {
            UniversityRoom createdUniversityRoom = new UniversityRoom(number, capacity);
            items.add(createdUniversityRoom);
            UniversityRoomDAO.create(createdUniversityRoom);
            numberFieldCreate.clear();
            capacityFieldCreate.clear();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
        UniversityRoomDAO.delete(chosenUniversityRoom);
        items.remove(chosenUniversityRoom);
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        String newNumber = numberFieldUpdate.getText();
        Integer newCapacity = Integer.parseInt(capacityFieldUpdate.getText());
        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();

        if(!chosenUniversityRoom.getNumber().equals(newNumber) || !chosenUniversityRoom.getCapacity().equals(newCapacity)) {
            UniversityRoomDAO.update(chosenUniversityRoom, newNumber, newCapacity);
            numberFieldUpdate.clear();
            capacityFieldUpdate.clear();
            items.clear();
            items.addAll(UniversityRoomDAO.getAll());
            universityRoomListTableView.setItems(items);
        }
    }
}
