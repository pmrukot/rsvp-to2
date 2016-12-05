package rsvp.resources.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import rsvp.resources.model.UniversityRoom;
import rsvp.resources.view.CalendarCell;

public class ClassroomListController {
    @FXML
    TableView<UniversityRoom> classListTableView;

    @FXML
    TableColumn<UniversityRoom, String> numberColumn;

    @FXML
    TableColumn<UniversityRoom, Integer> capacityColumn;

    @FXML
    TableColumn<UniversityRoom, Boolean> calendarColumn;

    @FXML
    TableColumn<UniversityRoom, Boolean> deleteColumn;

    @FXML
    private TextField numberFieldCreate;

    @FXML
    private TextField capacityFieldCreate;

    @FXML
    Button createButton;

    @FXML
    private TextField numberFieldUpdate;

    @FXML
    private TextField capacityFieldUpdate;

    @FXML
    Button updateButton;

    @FXML
    Button deleteButton;

    // TODO: make this list being read from database
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
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

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

        classListTableView.setItems(items);
    }

    @FXML
    private void handleCreateButtonAction(ActionEvent enet) {
        System.out.println("Create button clicked");

        String number = numberFieldCreate.getText();
        Integer capacity = Integer.parseInt(capacityFieldCreate.getText());

        if (capacity > 0) {
            items.add(new UniversityRoom(number, capacity));
            System.out.println("Created " + number + " " + capacity);
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        System.out.println("Update button clicked");

        UniversityRoom chosenUniversityRoom = classListTableView.getSelectionModel().getSelectedItem();

        String previousNumber = chosenUniversityRoom.getNumber();
        Integer previousCapacity = chosenUniversityRoom.getCapacity();

        String newNumber = numberFieldUpdate.getText();
        Integer newCapacity = Integer.parseInt(capacityFieldUpdate.getText());

//        System.out.println("Updated from " + previousNumber + " " + previousCapacity + " to " + newNumber + " " + newCapacity);
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        System.out.println("Delete button clicked");
        UniversityRoom chosenUniversityRoom = classListTableView.getSelectionModel().getSelectedItem();
        String previousNumber = chosenUniversityRoom.getNumber();
        Integer previousCapacity = chosenUniversityRoom.getCapacity();
        items.remove(chosenUniversityRoom);
        System.out.println("Deleted " + previousNumber + " " + previousCapacity);
    }
}
