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
import rsvp.resources.validation.UniversityRoomValidation;
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
    UniversityRoomDAO universityRoomDAO;

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
        String validationOutput = UniversityRoomValidation.createValidation(numberFieldCreate, capacityFieldCreate);
        if (validationOutput != null) {
            handleErrorAlert(numberFieldCreate, capacityFieldCreate, validationOutput);
            return;
        }

        String number = numberFieldCreate.getText();
        Integer capacity = Integer.parseInt(capacityFieldCreate.getText());

        UniversityRoom createdUniversityRoom = new UniversityRoom(number, capacity);
        items.add(createdUniversityRoom);
        universityRoomDAO.create(createdUniversityRoom);
        clearFields(numberFieldCreate, capacityFieldCreate);
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
        String validationOutput = UniversityRoomValidation.deleteValidation(chosenUniversityRoom);
        if (validationOutput != null) {
            showError(validationOutput);
            return;
        }

        universityRoomDAO.delete(chosenUniversityRoom);
        items.remove(chosenUniversityRoom);
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        UniversityRoom chosenUniversityRoom = universityRoomListTableView.getSelectionModel().getSelectedItem();
        String validationOutput = UniversityRoomValidation.updateValidation(numberFieldUpdate, capacityFieldUpdate, chosenUniversityRoom);
        if (validationOutput != null) {
            handleErrorAlert(numberFieldUpdate, capacityFieldUpdate, validationOutput);
        }

        String newNumber = numberFieldUpdate.getText();
        Integer newCapacity = Integer.parseInt(capacityFieldUpdate.getText());

        universityRoomDAO.update(chosenUniversityRoom, newNumber, newCapacity);
        clearFields(numberFieldUpdate, capacityFieldUpdate);
        items.clear();
        items.addAll(universityRoomDAO.getAll());
        universityRoomListTableView.setItems(items);
    }
}
