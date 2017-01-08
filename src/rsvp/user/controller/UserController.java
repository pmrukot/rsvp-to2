package rsvp.user.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;
import rsvp.user.model.Reservation;
import rsvp.user.model.User;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class UserController {

    private static final List<Reservation> data = Arrays.asList(
            new Reservation(new User("Jan", "Kowalski", "abc", false),
                    TimeSlot.createTimeSlot(LocalTime.of(13, 30), LocalTime.of(14, 30)),
                    new UniversityRoom("13", 30)));

    private Stage primaryStage;
    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    @FXML
    private TableView<Reservation> myCalendarTable;

    @FXML
    private TableColumn<Reservation, String> monday;

    @FXML
    private void initialize() {
        monday.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRoom().getNumber()));
        setData();
    }

    private List<Reservation> getReservations() {
        return data;
    }

    private void setData() {
        reservations.addAll(getReservations());
        myCalendarTable.setItems(reservations);
    }
}
