package rsvp.resources.controller;

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
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.CalendarTableItem;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;
import rsvp.resources.view.CalendarDayColumn;
import rsvp.resources.view.annotations.WeekDayColumn;
import rsvp.resources.view.annotations.processors.WeekDayColumnAnnotationProcessor;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.Date;
import java.util.List;

public class CalendarController {

    @FXML
    TableView<CalendarTableItem> calendarTable;
    @FXML
    TableColumn<CalendarTableItem, String> slotsColumn;
    @FXML @WeekDayColumn(dayNumber = 1)
    CalendarDayColumn mondayColumn;
    @FXML @WeekDayColumn(dayNumber = 2)
    CalendarDayColumn tuesdayColumn;
    @FXML @WeekDayColumn(dayNumber = 3)
    CalendarDayColumn wednesdayColumn;
    @FXML @WeekDayColumn(dayNumber = 4)
    CalendarDayColumn thursdayColumn;
    @FXML @WeekDayColumn(dayNumber = 5)
    CalendarDayColumn fridayColumn;
    @FXML @WeekDayColumn(dayNumber = 6)
    CalendarDayColumn saturdayColumn;
    @FXML @WeekDayColumn(dayNumber = 7)
    CalendarDayColumn sundayColumn;

    @FXML
    Button previousButton;
    @FXML
    Button nextButton;
    @FXML
    Label dateRangeLabel;

    private Date currentStartDate;
    private Date currentEndDate;

    private UniversityRoom universityRoom;
    private Map<TimeSlot, CalendarTableItem> bookingItemsMap;

    @FXML
    private void initialize() {
        slotsColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTimeSlotRepresentation()));
        new WeekDayColumnAnnotationProcessor().process(this);
    }

    public void initializeCalendarTableContent() {
        ObservableList<CalendarTableItem> calendarTableItems = FXCollections.observableArrayList();
        calendarTableItems.addAll(provideCalendarTableItems(currentStartDate, currentEndDate));
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

    private void setDateRangeLabel() {
        String formattedStartDate = new SimpleDateFormat("dd/MM").format(this.currentStartDate);
        String formattedEndDate = new SimpleDateFormat("dd/MM/yyyy").format(this.currentEndDate);
        dateRangeLabel.setText(formattedStartDate + " - " + formattedEndDate);
    }

    private boolean isBetweenTimeSlots(TimeSlot start, TimeSlot end, TimeSlot timeSlot) {
        if (start == null && end == null) return true;
        if (start == null) return (timeSlot.getEndTime().isBefore(end.getStartTime())) || end.equals(timeSlot);
        if (end == null) return (timeSlot.getStartTime().isAfter(start.getEndTime())) || start.equals(timeSlot);
        return (timeSlot.getEndTime().isBefore(end.getStartTime()) &&
                timeSlot.getStartTime().isAfter(start.getEndTime())) || start.equals(timeSlot) || end.equals(timeSlot);
    }

    private Color getRandomColor() {
        Random rand = new Random();
        double r = rand.nextFloat() / 2f + 0.5;
        double g = rand.nextFloat() / 2f + 0.5;
        double b = rand.nextFloat() / 2f + 0.5;
        return new Color(r, g, b, 1.0);
    }

    private List<CalendarTableItem> provideCalendarTableItems(Date start, Date end) {
        List<CalendarTableItem> result = new ArrayList<>();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        List<TimeSlot> timeSlots = timeSlotDAO.getAll();
        Collections.sort(timeSlots);
        bookingItemsMap = new HashMap<>();

        for (TimeSlot timeSlot : timeSlots) {
            CalendarTableItem item = new CalendarTableItem(timeSlot, timeSlots);
            result.add(item);
            bookingItemsMap.put(timeSlot, item);
        }

        DBBookingDAO dbBookingDAO = new DBBookingDAO();
        java.sql.Date sqlStartTime = new java.sql.Date(start.getTime());
        java.sql.Date sqlEndTime = new java.sql.Date(end.getTime());
        List<Booking> bookings = dbBookingDAO.getAllBookingsForGivenPeriodAndUniversityRoom(
                sqlStartTime, sqlEndTime, universityRoom);

        bookings.forEach(booking -> {
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
        initializeCalendarTableContent();
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
        initializeCalendarTableContent();
        setDateRangeLabel();
    }

    public void setContentForUniversityRoom(UniversityRoom universityRoom) {
        this.universityRoom = universityRoom;
        Calendar cal = Calendar.getInstance();
        while (cal.get(Calendar.DAY_OF_WEEK) > cal.getFirstDayOfWeek()) {
            cal.add(Calendar.DATE, -1);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        currentStartDate = cal.getTime();
        cal.add(Calendar.DATE, 6);
        currentEndDate = cal.getTime();

        initializeCalendarTableContent();
        setIcons();
        setDateRangeLabel();
    }

}