package rsvp.resources.controller;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import rsvp.resources.model.UniversityRoom;

public class ClassroomListController {
    @FXML
    TableView<UniversityRoom> classListTableView;

    @FXML
    TableColumn<UniversityRoom, String> numberColumn;

    @FXML
    TableColumn<UniversityRoom, Integer> capacityColumn;

    @FXML
    Button saveButton;

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
        classListTableView.setItems(items);
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        System.out.println("Save button clicked");
    }
}
