package rsvp.resources.model;

import javafx.scene.paint.Color;
import rsvp.booking.model.Booking;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarTableItem {

    private TimeSlot timeSlot;
    private List<TimeSlot> timeSlots;

    private Map<DayOfWeek, Booking> bookingsMap;
    private Map<DayOfWeek, Color> colors;

    public CalendarTableItem(TimeSlot timeSlot, List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
        this.timeSlot = timeSlot;
        bookingsMap = new HashMap<>();
        colors = new HashMap<>();
    }

    public String getTimeSlotRepresentation() {
        return timeSlot.getStartTime().toString() + " - " + timeSlot.getEndTime().toString();
    }

    public String getBookingDescriptionPerDay(int dayNumber) {
        DayOfWeek day = DayOfWeek.of(dayNumber);
        if (bookingsMap.containsKey(day)) {
            Booking booking = bookingsMap.get(day);
            if (booking.getFirstSlot() == null) {
                if (timeSlot.equals(timeSlots.get(0))) {
                    return booking.getOwner().getFirstName() + " " + booking.getOwner().getLastName();
                } else return "";
            }
            if (booking.getFirstSlot().equals(timeSlot))
                return booking.getOwner().getFirstName() + " " + booking.getOwner().getLastName();
        }
        return "";
    }

    public Color getColor(int dayNumber) {
        DayOfWeek day = DayOfWeek.of(dayNumber);
        if (colors.containsKey(day)) {
            return colors.get(day);
        }
        return null;
    }

    public void addBookingsMap(DayOfWeek day, Booking booking) {
        bookingsMap.put(day, booking);
    }

    public void addColor(DayOfWeek day, Color color) {
        colors.put(day, color);
    }

    public Booking getBooking(int dayNumber) {
        DayOfWeek day = DayOfWeek.of(dayNumber);
        if (bookingsMap.containsKey(day)) {
            return bookingsMap.get(day);
        }
        return null;
    }

}