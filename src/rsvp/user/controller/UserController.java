package rsvp.user.controller;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import rsvp.booking.DAO.BookingDAO;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.user.model.ReservationsPerWeek;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserController implements ListChangeListener{


    private Stage primaryStage;
    private ObservableList<ReservationsPerWeek> reservations = FXCollections.observableArrayList();
    @FXML
    private TableView<ReservationsPerWeek> myCalendarTable;

    @FXML
    public TableColumn slots;
    @FXML
    private TableColumn monday;
    @FXML
    private TableColumn tuesday;
    @FXML
    private TableColumn wednesday;
    @FXML
    private TableColumn thursday;
    @FXML
    private TableColumn friday;
    @FXML
    private TableColumn saturday;
    @FXML
    private TableColumn sunday;


    @FXML
    private void initialize() {
        myCalendarTable.setEditable(false);
        slots.setCellValueFactory(new PropertyValueFactory<ReservationsPerWeek, String>("slots"));
        monday.setCellValueFactory(new PropertyValueFactory<ReservationsPerWeek, String>("monday"));
        tuesday.setCellValueFactory(new PropertyValueFactory<ReservationsPerWeek, String>("tuesday"));
        wednesday.setCellValueFactory(new PropertyValueFactory<ReservationsPerWeek, String>("wednesday"));
        thursday.setCellValueFactory(new PropertyValueFactory<ReservationsPerWeek, String>("thursday"));
        friday.setCellValueFactory(new PropertyValueFactory<ReservationsPerWeek, String>("friday"));
        saturday.setCellValueFactory(new PropertyValueFactory<ReservationsPerWeek, String>("saturday"));
        sunday.setCellValueFactory(new PropertyValueFactory<ReservationsPerWeek, String>("sunday"));
        setData();
    }

    private void getReservations() {
        BookingDAO bookingDAO = new DBBookingDAO();
        List<Booking> allReservations = bookingDAO.getAllBookingsForCurrentUser();
        List<TimeSlot> timeSlots = getTimeSlots();
        for(TimeSlot timeSlot : timeSlots) {
            List<Booking> reservationForTimeSlot = allReservations.stream().filter(res -> containsTimeSlot(res, timeSlot)).collect(Collectors.toList());
            Optional<Booking> mondayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.MONDAY))).findFirst();
            Optional<Booking> tuesdayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.TUESDAY))).findFirst();
            Optional<Booking> wednesdayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.WEDNESDAY))).findFirst();
            Optional<Booking> thursdayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.THURSDAY))).findFirst();
            Optional<Booking> fridayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.FRIDAY))).findFirst();
            Optional<Booking> saturdayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.SATURDAY))).findFirst();
            Optional<Booking> sundayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.SUNDAY))).findFirst();

            reservations.add(new ReservationsPerWeek(timeSlot.toString(),
                    mondayReservation.isPresent() ? mondayReservation.get().getUniversityRoom().getNumber() : "",
                    tuesdayReservation.isPresent() ? tuesdayReservation.get().getUniversityRoom().getNumber() : "",
                    wednesdayReservation.isPresent() ? wednesdayReservation.get().getUniversityRoom().getNumber() : "",
                    thursdayReservation.isPresent() ? thursdayReservation.get().getUniversityRoom().getNumber() : "",
                    fridayReservation.isPresent() ? fridayReservation.get().getUniversityRoom().getNumber() : "",
                    saturdayReservation.isPresent() ? saturdayReservation.get().getUniversityRoom().getNumber() : "",
                    sundayReservation.isPresent() ? sundayReservation.get().getUniversityRoom().getNumber() : ""));
        }
    }
    private  List<TimeSlot> getTimeSlots() {
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        return timeSlotDAO.getAll();
    }

    private boolean containsTimeSlot(Booking reservation, TimeSlot timeSlot) {
        if(reservation.getFirstSlot().getId() == timeSlot.getId() || reservation.getLastSlot().getId() == timeSlot.getId()) {
            return true;
        } else if (reservation.getFirstSlot().compareTo(timeSlot) == -1 && reservation.getLastSlot().compareTo(timeSlot) == 1) {
            return true;
        }
        return false;
    }

    private Date getDate(DayOfWeek dayOfWeek) {
        LocalDate currentDate = LocalDate.now();
        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        if(dayOfWeek.equals(currentDayOfWeek)){
            return Date.valueOf(currentDate);
        } else if(dayOfWeek.getValue() < currentDayOfWeek.getValue()) {
            return Date.valueOf(currentDate.minusDays(currentDayOfWeek.getValue() - dayOfWeek.getValue()));
        }
        return Date.valueOf(currentDate.plusDays(dayOfWeek.getValue() - currentDayOfWeek.getValue()));
    }

    private void setData() {
        getReservations();
        myCalendarTable.setItems(reservations);
        myCalendarTable.getColumns().addAll(slots, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @Override
    public void onChanged(Change c) {
        setData();
    }
}
