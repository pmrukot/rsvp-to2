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
import rsvp.booking.DAO.DBBookingDAO;
import rsvp.booking.model.Booking;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.DAO.UniversityRoomDAO;
import rsvp.resources.model.CalendarTableItem;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;

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
    TableColumn<CalendarTableItem, String> mondayColumn;
    @FXML
    TableColumn<CalendarTableItem, String> tuesdayColumn;
    @FXML
    TableColumn<CalendarTableItem, String> wednesdayColumn;
    @FXML
    TableColumn<CalendarTableItem, String> thursdayColumn;
    @FXML
    TableColumn<CalendarTableItem, String> fridayColumn;
    @FXML
    TableColumn<CalendarTableItem, String> saturdayColumn;
    @FXML
    TableColumn<CalendarTableItem, String> sundayColumn;

    @FXML
    Button previousButton;
    @FXML
    Button nextButton;
    @FXML
    Label dateRangeLabel;

    private List<Booking> bookings;
    private Date currentStartDate;
    private Date currentEndDate;

    //TODO: pass UniversityRoom to the controller
    UniversityRoomDAO dao = new UniversityRoomDAO();
    private UniversityRoom universityRoom= dao.getAll().get(0);

    @FXML
    private void initialize() {
        DBBookingDAO bookingDAO = new DBBookingDAO();
        bookings = bookingDAO.getAllBookingsForUniversityRoom(universityRoom);

        slotsColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTimeSlotRepresentation()));

        mondayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(1)));
        tuesdayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(2)));
        wednesdayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(3)));
        thursdayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(4)));
        fridayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(5)));
        saturdayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(6)));
        sundayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(7)));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 6);
        currentStartDate = new Date();
        currentEndDate = cal.getTime();

        initializeCalendarTableContentForDates(currentStartDate, currentEndDate);
        setIcons();
        setDateRangeLabel();
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

    private List<CalendarTableItem> provideCalendarTableItems(Date start, Date end){
        List<CalendarTableItem> result = new ArrayList<>();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        List<TimeSlot> timeSlots = timeSlotDAO.getAll();
        Collections.sort(timeSlots);

        for (TimeSlot timeSlot: timeSlots){
            Map<DayOfWeek, String> bookingsMap = new HashMap<>();
            bookings.stream().filter(booking ->
                    isInThePeriod(start, end, booking.getReservationDate())).
                    filter(booking -> isBetweenTimeSlots(booking.getFirstSlot(), booking.getLastSlot(), timeSlot)).
                    forEach(booking -> bookingsMap.put(booking.getReservationDate().toLocalDate().getDayOfWeek(),
                            "booked by: " + booking.getOwner().getFirstName() + " " + booking.getOwner().getLastName()));

            CalendarTableItem item = new CalendarTableItem(bookingsMap, (int)timeSlot.getId());
            result.add(item);
        }
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

}
