package rsvp.resources.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.CalendarTableItem;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;
import rsvp.resources.view.CalendarDayColumn;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.Date;
import java.util.List;

public class CalendarController {

    @FXML
    TableView<CalendarTableItem>calendarTable;
    @FXML
    TableColumn<CalendarTableItem, String> slotsColumn;
    @FXML
    CalendarDayColumn mondayColumn;
    @FXML
    CalendarDayColumn tuesdayColumn;
    @FXML
    CalendarDayColumn wednesdayColumn;
    @FXML
    CalendarDayColumn thursdayColumn;
    @FXML
    CalendarDayColumn fridayColumn;
    @FXML
    CalendarDayColumn saturdayColumn;
    @FXML
    CalendarDayColumn sundayColumn;

    @FXML
    Button previousButton;
    @FXML
    Button nextButton;
    @FXML
    Label dateRangeLabel;

    private List<Booking> bookings;
    private Date currentStartDate;
    private Date currentEndDate;

    @FXML
    private void initialize() {

        slotsColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTimeSlotRepresentation()));
        mondayColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(1), p.getValue().getColor(1))));
        tuesdayColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(2), p.getValue().getColor(2))));
        wednesdayColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(3), p.getValue().getColor(3))));
        thursdayColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(4), p.getValue().getColor(4))));
        fridayColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(5), p.getValue().getColor(5))));
        saturdayColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(6), p.getValue().getColor(6))));
        sundayColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(
                new Pair<>(p.getValue().getBookingDescriptionPerDay(7), p.getValue().getColor(7))));
    }

    private void initializeCalendarTableContentForDates(Date start, Date end){
        ObservableList<CalendarTableItem> calendarTableItems = FXCollections.observableArrayList();
        calendarTableItems.addAll(provideCalendarTableItems(start, end));
        calendarTable.setItems(calendarTableItems);
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

    private boolean isBetweenTimeSlots(TimeSlot start, TimeSlot end, TimeSlot timeSlot){
        return (timeSlot.getEndTime().isBefore(end.getStartTime()) &&
                timeSlot.getStartTime().isAfter(start.getEndTime())) || start.equals(timeSlot) || end.equals(timeSlot);
    }

    private boolean isInThePeriod(Date start, Date end, Date date){
        return !date.before(start) && !date.after(end);
    }

    private Color getRandomColor(){
        Random rand = new Random();
        double r = rand.nextFloat() / 2f + 0.5;
        double g = rand.nextFloat() / 2f + 0.5;
        double b = rand.nextFloat() / 2f + 0.5;
        return new Color(r, g, b, 1.0);
    }

    private List<CalendarTableItem> provideCalendarTableItems(Date start, Date end){
        List<CalendarTableItem> result = new ArrayList<>();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        List<TimeSlot> timeSlots = timeSlotDAO.getAll();
        Collections.sort(timeSlots);
        Map<TimeSlot, CalendarTableItem> bookingItemsMap = new HashMap<>();

        for (TimeSlot timeSlot : timeSlots){
            CalendarTableItem item = new CalendarTableItem(timeSlot);
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
                        CalendarTableItem item = bookingItemsMap.get(timeSlot);
                        item.addColor(dayOfWeek, color);
                        item.addBookingsMap(dayOfWeek, booking);
                    });
                });

        return result;
    }

    @FXML
    private void handleShowNextWeek(ActionEvent event) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentEndDate);
        cal.add(Calendar.DATE, 1);
        currentStartDate = cal.getTime();
        cal.add(Calendar.DATE, 6);
        currentEndDate = cal.getTime();
        initializeCalendarTableContentForDates(currentStartDate, currentEndDate);
        setDateRangeLabel();
    }

    @FXML
    private void handleShowPreviousWeek(ActionEvent event) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentStartDate);
        cal.add(Calendar.DATE, -1);
        currentEndDate = cal.getTime();
        cal.add(Calendar.DATE, -6);
        currentStartDate = cal.getTime();
        initializeCalendarTableContentForDates(currentStartDate, currentEndDate);
        setDateRangeLabel();
    }

    public void setContentForUniversityRoom(UniversityRoom universityRoom){
        DBBookingDAO bookingDAO = new DBBookingDAO();
        bookings = bookingDAO.getAllBookingsForUniversityRoom(universityRoom);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 6);
        currentStartDate = new Date();
        currentEndDate = cal.getTime();

        initializeCalendarTableContentForDates(currentStartDate, currentEndDate);
        setIcons();
        setDateRangeLabel();
    }

}
