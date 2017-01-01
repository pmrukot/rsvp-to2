package rsvp.user.controller;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.user.view.MyCalendarCell;
import rsvp.user.view.MyCalendarColumn;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class UserController implements ListChangeListener{


    public Button previousButton;
    public Label dateRangeLabel;
    public Button nextButton;
    @FXML
    private TableView<MyCalendarCell> myCalendarTable;

    @FXML
    public TableColumn<MyCalendarCell, String> slots;
    @FXML
    private MyCalendarColumn monday;
    @FXML
    private MyCalendarColumn tuesday;
    @FXML
    private MyCalendarColumn wednesday;
    @FXML
    private MyCalendarColumn thursday;
    @FXML
    private MyCalendarColumn friday;
    @FXML
    private MyCalendarColumn saturday;
    @FXML
    private MyCalendarColumn sunday;

    private Date currentStartDate;
    private Date currentEndDate;
    private List<Booking> bookings;


    @FXML
    private void initialize() {
        DBBookingDAO bookingDAO = new DBBookingDAO();
        bookings = bookingDAO.getAllBookingsForCurrentUser();
        myCalendarTable.setEditable(false);
        slots.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTimeSlotRepresentation()));
        monday.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(1), p.getValue().getColor(1))));
        tuesday.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(2), p.getValue().getColor(2))));
        wednesday.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(3), p.getValue().getColor(3))));
        thursday.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(4), p.getValue().getColor(4))));
        friday.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(5), p.getValue().getColor(5))));
        saturday.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(6), p.getValue().getColor(6))));
        sunday.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(7), p.getValue().getColor(7))));


        currentStartDate = getDate(DayOfWeek.MONDAY, LocalDate.now());
        currentEndDate = getDate(DayOfWeek.SUNDAY, LocalDate.now());
        setDateRangeLabel();
        setIcons();
        initializeMyCalendarContent(currentStartDate, currentEndDate);

    }

    private void initializeMyCalendarContent(Date start, Date end){
        ObservableList<MyCalendarCell> calendarTableItems = FXCollections.observableArrayList();
        calendarTableItems.addAll(fillTableCells(start, end));
        myCalendarTable.setItems(calendarTableItems);
    }

    private List<MyCalendarCell> fillTableCells(Date start, Date end){
        List<MyCalendarCell> result = new ArrayList<>();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        List<TimeSlot> timeSlots = timeSlotDAO.getAll();
        Collections.sort(timeSlots);
        Map<TimeSlot, MyCalendarCell> bookingItemsMap = new HashMap<>();

        for (TimeSlot timeSlot : timeSlots){
            MyCalendarCell item = new MyCalendarCell(timeSlot, timeSlots);
            result.add(item);
            bookingItemsMap.put(timeSlot, item);
        }

        bookings.stream().filter(booking ->
                isInThePeriod(start, end, booking.getReservationDate())).
                forEach(booking -> {
                    Color color = getRandomColor();
                    DayOfWeek dayOfWeek = booking.getReservationDate().toLocalDate().getDayOfWeek();
                    timeSlots.stream().filter(timeSlot -> isBetweenTimeSlots(booking.getFirstSlot(),
                            booking.getLastSlot(), timeSlot)).forEach(timeSlot -> {
                        MyCalendarCell item = bookingItemsMap.get(timeSlot);
                        item.addColor(dayOfWeek, color);
                        item.addBookingsMap(dayOfWeek, booking);
                    });
                });

        return result;
    }
    private boolean isInThePeriod(Date start, Date end, Date date){
        return !date.before(start) && !date.after(end);
    }

    private boolean isBetweenTimeSlots(TimeSlot start, TimeSlot end, TimeSlot timeSlot){
        if(start == null && end == null) return true;
        if(start == null) return (end.compareTo(timeSlot) == 1 || end.getId() == timeSlot.getId());
        if(end == null) return (start.compareTo(timeSlot) == -1 || start.getId() == timeSlot.getId());
        if(start.getId() == timeSlot.getId() || end.getId() == timeSlot.getId()) {
            return true;
        } else if (start.compareTo(timeSlot) == -1 && end.compareTo(timeSlot) == 1) {
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

    @Override
    public void onChanged(Change c) {
    }

    public void handleShowPreviousWeek(ActionEvent actionEvent) {
        currentEndDate = Date.valueOf(currentStartDate.toLocalDate().minusDays(1));
        currentStartDate = Date.valueOf(currentStartDate.toLocalDate().minusDays(7));
        initializeMyCalendarContent(currentStartDate, currentEndDate);
        setDateRangeLabel();
    }

    public void handleShowNextWeek(ActionEvent actionEvent) {
        currentStartDate = Date.valueOf(currentEndDate.toLocalDate().plusDays(1));
        currentEndDate = Date.valueOf(currentEndDate.toLocalDate().plusDays(7));
        initializeMyCalendarContent(currentStartDate, currentEndDate);
        setDateRangeLabel();
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

    private Color getRandomColor(){
        Random rand = new Random();
        double r = rand.nextFloat() / 2f + 0.5;
        double g = rand.nextFloat() / 2f + 0.5;
        double b = rand.nextFloat() / 2f + 0.5;
        return new Color(r, g, b, 1.0);
    }
}
