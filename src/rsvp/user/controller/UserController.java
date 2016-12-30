package rsvp.user.controller;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rsvp.booking.DAO.BookingDAO;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.user.model.ReservationsPerWeek;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class UserController implements ListChangeListener{


    public Button previousButton;
    public Label dateRangeLabel;
    public Button nextButton;
    private Stage primaryStage;
    private ObservableList<ReservationsPerWeek> reservations = FXCollections.observableArrayList();
    @FXML
    private TableView<ReservationsPerWeek> myCalendarTable;

    @FXML
    public TableColumn<ReservationsPerWeek, String> slots;
    @FXML
    private TableColumn monday;
    @FXML
    private TableColumn<ReservationsPerWeek, String> tuesday;
    @FXML
    private TableColumn<ReservationsPerWeek, String> wednesday;
    @FXML
    private TableColumn<ReservationsPerWeek, String> thursday;
    @FXML
    private TableColumn<ReservationsPerWeek, String> friday;
    @FXML
    private TableColumn<ReservationsPerWeek, String> saturday;
    @FXML
    private TableColumn<ReservationsPerWeek, String> sunday;

    private Date currentStartDate;
    private Date currentEndDate;


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
        setData(LocalDate.now());
        currentStartDate = getDate(DayOfWeek.MONDAY, LocalDate.now());
        currentEndDate = getDate(DayOfWeek.SUNDAY, LocalDate.now());
        setDateRangeLabel();
        setIcons();
        setColors();
    }

    private void getReservations(LocalDate currentDate) {
        reservations.clear();
        BookingDAO bookingDAO = new DBBookingDAO();
        List<Booking> allReservations = bookingDAO.getAllBookingsForCurrentUser();
        List<TimeSlot> timeSlots = getTimeSlots();
        for(TimeSlot timeSlot : timeSlots) {
            List<Booking> reservationForTimeSlot = allReservations.stream().filter(res -> containsTimeSlot(res, timeSlot)).collect(Collectors.toList());
            Optional<Booking> mondayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.MONDAY, currentDate))).findFirst();
            Optional<Booking> tuesdayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.TUESDAY, currentDate))).findFirst();
            Optional<Booking> wednesdayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.WEDNESDAY, currentDate))).findFirst();
            Optional<Booking> thursdayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.THURSDAY, currentDate))).findFirst();
            Optional<Booking> fridayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.FRIDAY, currentDate))).findFirst();
            Optional<Booking> saturdayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.SATURDAY, currentDate))).findFirst();
            Optional<Booking> sundayReservation = reservationForTimeSlot.stream().filter(res -> res.getReservationDate().equals(getDate(DayOfWeek.SUNDAY, currentDate))).findFirst();

            reservations.add(new ReservationsPerWeek(timeSlot.toString(),
                    mondayReservation.isPresent() ? deleteRepetitions(mondayReservation.get(), DayOfWeek.MONDAY): "",
                    tuesdayReservation.isPresent() ? deleteRepetitions(tuesdayReservation.get(), DayOfWeek.TUESDAY) : "",
                    wednesdayReservation.isPresent() ? deleteRepetitions(wednesdayReservation.get(), DayOfWeek.WEDNESDAY) : "",
                    thursdayReservation.isPresent() ? deleteRepetitions(thursdayReservation.get(), DayOfWeek.THURSDAY) : "",
                    fridayReservation.isPresent() ? deleteRepetitions(fridayReservation.get(), DayOfWeek.FRIDAY) : "",
                    saturdayReservation.isPresent() ? deleteRepetitions(saturdayReservation.get(), DayOfWeek.SATURDAY) : "",
                    sundayReservation.isPresent() ? deleteRepetitions(sundayReservation.get(), DayOfWeek.SUNDAY) : ""));
        }
    }
    private  List<TimeSlot> getTimeSlots() {
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        return timeSlotDAO.getAll();
    }

    private String deleteRepetitions(Booking reservation, DayOfWeek dayOfWeek) {
        int index = reservations.size() - 1;
        if(index < 0) {
            return reservation.getUniversityRoom().getNumber();
        }
        ReservationsPerWeek res = reservations.get(index);
        switch (dayOfWeek) {
            case MONDAY:
                return res.getMonday().equals(reservation.getUniversityRoom().getNumber()) ? "." : reservation.getUniversityRoom().getNumber();
            case TUESDAY:
                return res.getTuesday().equals(reservation.getUniversityRoom().getNumber()) ? "." : reservation.getUniversityRoom().getNumber();
            case WEDNESDAY:
                return res.getWednesday().equals(reservation.getUniversityRoom().getNumber()) ? "." : reservation.getUniversityRoom().getNumber();
            case THURSDAY:
                return res.getThursday().equals(reservation.getUniversityRoom().getNumber()) ? "." : reservation.getUniversityRoom().getNumber();
            case FRIDAY:
                return res.getFriday().equals(reservation.getUniversityRoom().getNumber()) ? "." : reservation.getUniversityRoom().getNumber();
            case SATURDAY:
                return res.getSaturday().equals(reservation.getUniversityRoom().getNumber()) ? "." : reservation.getUniversityRoom().getNumber();
            case SUNDAY:
                return res.getSunday().equals(reservation.getUniversityRoom().getNumber()) ? "." : reservation.getUniversityRoom().getNumber();
        }
        return null;
    }

    private boolean containsTimeSlot(Booking reservation, TimeSlot timeSlot) {
        if(reservation.getFirstSlot().getId() == timeSlot.getId() || reservation.getLastSlot().getId() == timeSlot.getId()) {
            return true;
        } else if (reservation.getFirstSlot().compareTo(timeSlot) == -1 && reservation.getLastSlot().compareTo(timeSlot) == 1) {
            return true;
        }
        return false;
    }

    private Date getDate(DayOfWeek dayOfWeek, LocalDate currentDate) {
        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        if(dayOfWeek.equals(currentDayOfWeek)){
            return Date.valueOf(currentDate);
        } else if(dayOfWeek.getValue() < currentDayOfWeek.getValue()) {
            return Date.valueOf(currentDate.minusDays(currentDayOfWeek.getValue() - dayOfWeek.getValue()));
        }
        return Date.valueOf(currentDate.plusDays(dayOfWeek.getValue() - currentDayOfWeek.getValue()));
    }

    private void setData(LocalDate date) {
        getReservations(date);
        myCalendarTable.setItems(reservations);
        myCalendarTable.getColumns().clear();
        myCalendarTable.getColumns().addAll(slots, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @Override
    public void onChanged(Change c) {
        setData(LocalDate.now());
    }

    public void handleShowPreviousWeek(ActionEvent actionEvent) {
        currentEndDate = Date.valueOf(currentStartDate.toLocalDate().minusDays(1));
        currentStartDate = Date.valueOf(currentStartDate.toLocalDate().minusDays(7));
        setData(currentStartDate.toLocalDate());
        setDateRangeLabel();
        setColors();
    }

    public void handleShowNextWeek(ActionEvent actionEvent) {
        currentStartDate = Date.valueOf(currentEndDate.toLocalDate().plusDays(1));
        currentEndDate = Date.valueOf(currentStartDate.toLocalDate().plusDays(6));
        setData(currentStartDate.toLocalDate());
        setDateRangeLabel();
        setColors();
    }

    private void setIcons() {
        ImageView previousView = new ImageView(new Image(getClass().getResourceAsStream("../view/images/previous.png")));
        previousView.setFitWidth(20);
        previousView.setFitHeight(20);
        previousButton.setGraphic(previousView);
        ImageView nextView = new ImageView(new Image(getClass().getResourceAsStream("../view/images/next.png")));
        nextView.setFitWidth(20);
        nextView.setFitHeight(20);
        nextButton.setGraphic(nextView);
    }

    private void setDateRangeLabel(){
        String formattedStartDate = new SimpleDateFormat("dd/MM").format(this.currentStartDate);
        String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(this.currentEndDate);
        dateRangeLabel.setText(formattedStartDate + " - "+ formattedEndDate);
    }

    private String getRandomColor(){
        Random ra = new Random();
        int r, g, b;
        r=ra.nextInt(255);
        g=ra.nextInt(255);
        b=ra.nextInt(255);
        Color color = new Color(r,g,b);
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            hex = "0" + hex;
        }
        return  "#" + hex;
    }

    private void setColors() {
        for (TableColumn col : myCalendarTable.getColumns()) {
            if(col.getId().equals("slots")) {
                continue;
            }
            col.setCellFactory(column -> {
                return new TableCell<TableColumn, String>() {
                    String old;
                    String color;
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {

                            if (item.isEmpty()) {
                                setStyle("");
                            } else if(item.equals(".")){
                                color = getRandomColor();
                                setStyle("-fx-background-color: " + color);
                            } else {
                                setText(item);
                                color = getRandomColor();
                                setStyle("-fx-background-color: " + color);
                            }
                        }
                    }
                };
            });
        }
    }
}
