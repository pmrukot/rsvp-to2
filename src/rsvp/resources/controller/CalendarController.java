package rsvp.resources.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.BookingMock;
import rsvp.resources.model.CalendarTableItem;
import rsvp.resources.model.TimeSlot;

import java.time.DayOfWeek;
import java.util.*;

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

    //TODO: get rid of those mocks
    private List<BookingMock> bookings = Arrays.asList(
            new BookingMock(9, "sieci", DayOfWeek.MONDAY),
            new BookingMock(17, "kompilatory", DayOfWeek.FRIDAY),
            new BookingMock(14, "asd", DayOfWeek.TUESDAY)
    );

    @FXML
    private void initialize() {
        ObservableList<CalendarTableItem> calendarTableItems = FXCollections.observableArrayList();
        calendarTableItems.addAll(provideCalendarTableItems());

        slotsColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTimeSlotRepresentation()));

        mondayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(1)));
        tuesdayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(2)));
        wednesdayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(3)));
        thursdayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(4)));
        fridayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(5)));
        saturdayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(6)));
        sundayColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBookingDescriptionPerDay(7)));

        calendarTable.setItems(calendarTableItems);
    }

    private List<CalendarTableItem> provideCalendarTableItems(){
        List<CalendarTableItem> result = new ArrayList<>();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        List<TimeSlot> timeSlots = timeSlotDAO.getAll();
        Collections.sort(timeSlots);

        for (TimeSlot timeSlot: timeSlots){
            Map<DayOfWeek, String> bookingsMap = new HashMap<>();

            for (BookingMock booking : bookings){
                if(booking.getTimeSlot() == timeSlot.getId()){
                    bookingsMap.put(booking.getDayOfWeek(), booking.getValue());
                }
            }

            CalendarTableItem item = new CalendarTableItem(bookingsMap, (int)timeSlot.getId());
            result.add(item);
        }
        return result;
    }


}
