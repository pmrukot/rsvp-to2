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
    private static final String CAPACITY_ALERT = "Capacity of the room should be greater than 0 and less than 200";
    private static final String NO_ROOM_SELECTED_ALERT = "You have to select some room in order to do modification";
    private static final String IMPROPER_NUMBER_FORMAT = "You have to provide valid number format";
    private static final String NO_MODYFICATION = "You have to provide different values than before";
    private static final String NOT_ENOUGH_ARGUMENTS = "You have to provide all arguments";

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
    UniversityRoomDAO universityRoomDAO;

    Alert errorAlert;

    @FXML
    private void initialize() {
        errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error!");
        errorAlert.setHeaderText("Error while modyfying data");

        items = FXCollections.observableArrayList();
        universityRoomDAO = new UniversityRoomDAO();

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        calendarColumn.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        calendarColumn.setCellFactory(p -> new CalendarCell());

        items.addAll(universityRoomDAO.getAll());
        universityRoomListTableView.setItems(items);
    }

    @FXML
    private void handleCreateButtonAction(ActionEvent event) {

        if(numberFieldCreate.getText().isEmpty() || capacityFieldCreate.getText().isEmpty()) {
            errorAlert.setContentText(NOT_ENOUGH_ARGUMENTS);
            errorAlert.showAndWait();
            numberFieldCreate.clear();
            capacityFieldCreate.clear();
            return;
        }

        String number = numberFieldCreate.getText();

        Integer capacity;
        try {
            capacity = Integer.parseInt(capacityFieldCreate.getText());
        } catch (NumberFormatException e){
            errorAlert.setContentText(IMPROPER_NUMBER_FORMAT);
            errorAlert.showAndWait();
            numberFieldCreate.clear();
            capacityFieldCreate.clear();
            return;
        }

        if(capacity < 1 || capacity > 200){
            errorAlert.setContentText(CAPACITY_ALERT);
            errorAlert.showAndWait();
            numberFieldCreate.clear();
            capacityFieldCreate.clear();
            return;
        }

        UniversityRoom createdUniversityRoom = new UniversityRoom(number, capacity);
        items.add(createdUniversityRoom);
        universityRoomDAO.create(createdUniversityRoom);
        numberFieldCreate.clear();
        capacityFieldCreate.clear();
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
        if (chosenUniversityRoom == null){
            errorAlert.setContentText(NO_ROOM_SELECTED_ALERT);
            errorAlert.showAndWait();
            return;
        }
        universityRoomDAO.delete(chosenUniversityRoom);
        items.remove(chosenUniversityRoom);
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {

        if(numberFieldUpdate.getText().isEmpty() && capacityFieldUpdate.getText().isEmpty()) {
            errorAlert.setContentText(NOT_ENOUGH_ARGUMENTS);
            errorAlert.showAndWait();
            numberFieldUpdate.clear();
            capacityFieldUpdate.clear();
            return;
        }

        String newNumber = numberFieldUpdate.getText();
        Integer newCapacity;
        try {
             newCapacity = Integer.parseInt(capacityFieldUpdate.getText());
        } catch (NumberFormatException e){
            errorAlert.setContentText(IMPROPER_NUMBER_FORMAT);
            errorAlert.showAndWait();
            numberFieldUpdate.clear();
            capacityFieldUpdate.clear();
            return;
        }
        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();

        if (chosenUniversityRoom == null){
            errorAlert.setContentText(NO_ROOM_SELECTED_ALERT);
            errorAlert.showAndWait();
            numberFieldUpdate.clear();
            capacityFieldUpdate.clear();
            return;
        }

        if(chosenUniversityRoom.getNumber().equals(newNumber) && chosenUniversityRoom.getCapacity().equals(newCapacity)) {
            errorAlert.setContentText(NO_MODYFICATION);
            errorAlert.showAndWait();
            numberFieldUpdate.clear();
            capacityFieldUpdate.clear();
            return;
        }

        if(newCapacity < 1 || newCapacity > 200){
            errorAlert.setContentText(CAPACITY_ALERT);
            errorAlert.showAndWait();
            numberFieldUpdate.clear();
            capacityFieldUpdate.clear();
            return;
        }

        universityRoomDAO.update(chosenUniversityRoom, newNumber, newCapacity);
        numberFieldUpdate.clear();
        capacityFieldUpdate.clear();
        items.clear();
        items.addAll(universityRoomDAO.getAll());
        universityRoomListTableView.setItems(items);
    }
}
