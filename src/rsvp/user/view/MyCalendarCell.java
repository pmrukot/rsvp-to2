package rsvp.user.view;


import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import rsvp.booking.model.Booking;
import rsvp.resources.model.TimeSlot;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyCalendarCell {

    private TimeSlot timeSlot;
    private List<TimeSlot> timeSlots;
    private Tooltip tooltip;

    private Map<DayOfWeek, Booking> bookingsMap;
    private Map<DayOfWeek, Color> colors;

    public MyCalendarCell(TimeSlot timeSlot, List<TimeSlot> timeSlots) {
        this.timeSlot = timeSlot;
        this.timeSlots = timeSlots;
        bookingsMap = new HashMap<>();
        colors = new HashMap<>();
        tooltip = new Tooltip();
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
                    tooltip.setText("Participants: " + booking.getParticipants().stream()
                            .map(user -> user.getFirstName() + " " + user.getLastName())
                            .collect(Collectors.joining(", ")));
                    return  booking.getUniversityRoom().getNumber();
                }
                else return "";
            }
            if(booking.getFirstSlot().getId() == timeSlot.getId()) {
                tooltip.setText("Participants: " + booking.getParticipants().stream()
                        .map(user -> user.getFirstName() + " " + user.getLastName())
                        .collect(Collectors.joining(", ")));
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

    public Tooltip getTooltip() {
        return tooltip;
    }
}
