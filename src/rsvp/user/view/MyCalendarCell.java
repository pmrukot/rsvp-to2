package rsvp.user.view;


import javafx.scene.paint.Color;
import rsvp.booking.model.Booking;
import rsvp.resources.model.TimeSlot;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCalendarCell {

    private TimeSlot timeSlot;
    private List<TimeSlot> timeSlots;

    private Map<DayOfWeek, Booking> bookingsMap;
    private Map<DayOfWeek, Color> colors;

    public MyCalendarCell(TimeSlot timeSlot, List<TimeSlot> timeSlots) {
        this.timeSlot = timeSlot;
        this.timeSlots = timeSlots;
        bookingsMap = new HashMap<>();
        colors = new HashMap<>();
    }

    public String getTimeSlotRepresentation() {
        return timeSlot.getStartTime().toString() + " - " + timeSlot.getEndTime().toString();
    }

    public String getBookingDescriptionPerDay(int dayNumber) {
        DayOfWeek day = DayOfWeek.of(dayNumber);
        if(bookingsMap.containsKey(day)){
            Booking booking = bookingsMap.get(day);
            if(booking.getFirstSlot() == null){
                if(timeSlot.equals(timeSlots.get(0))){
                    return booking.getUniversityRoom().getNumber();
                }
                else return "";
            }
            if(booking.getFirstSlot().getId() == timeSlot.getId()) {
                return booking.getUniversityRoom().getNumber();
            }
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
}
