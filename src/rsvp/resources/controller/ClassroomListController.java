package rsvp.resources.controller;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rsvp.resources.model.UniversityRoom;

public class ClassroomListController {
    @FXML
    TableView<UniversityRoom> classListTableView;

    @FXML
    TableColumn<UniversityRoom, StringProperty> numberColumn;

    @FXML
    TableColumn<UniversityRoom, StringProperty> capacityColumn;

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
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        classListTableView.setItems(items);
    }

}
