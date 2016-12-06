package rsvp.user.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rsvp.resources.model.UniversityRoom;
import rsvp.user.model.Reservation;
import rsvp.user.model.TimeSlot;
import rsvp.user.model.User;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class UserController {

    private static final List<Reservation> data = Arrays.asList(
            new Reservation(new User("Jan", "Kowalski", "abc", false),
                    new TimeSlot(LocalDateTime.of(2016, 12, 3, 12, 0), LocalDateTime.of(2016, 12, 3, 13, 30)),
                    new UniversityRoom("13", 30)));

    private Stage primaryStage;
    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    @FXML
    private TableView<Reservation> myCalendarTable;

    @FXML
    private TableColumn<Reservation, String> monday;


    @FXML
    private void initialize() {
        monday.setCellValueFactory(cellData -> new SimpleObjectProperty<String>(cellData.getValue().getRoom().getNumber()));
        setData();
    }

    private List<Reservation> getReservations() {

        List<Reservation> result = data;
        return result;
    }

    public void setData() {
        reservations.addAll(getReservations());
        myCalendarTable.setItems(reservations);
    }
}
