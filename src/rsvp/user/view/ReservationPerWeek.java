package rsvp.user.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import rsvp.booking.model.Booking;
import rsvp.resources.model.TimeSlot;

import java.time.DayOfWeek;
import java.util.stream.Collectors;


public class ReservationPerWeek {
    private SimpleObjectProperty<CalendarCell> slots;
    private SimpleObjectProperty<CalendarCell> monday;
    private SimpleObjectProperty<CalendarCell> tuesday;
    private SimpleObjectProperty<CalendarCell> wednesday;
    private SimpleObjectProperty<CalendarCell> thursday;
    private SimpleObjectProperty<CalendarCell> friday;
    private SimpleObjectProperty<CalendarCell> saturday;
    private SimpleObjectProperty<CalendarCell> sunday;

    public ReservationPerWeek(TimeSlot timeSlot) {
        this.slots = new SimpleObjectProperty<>(new CalendarCell(timeSlot, timeSlot.toString()));
        this.monday = new SimpleObjectProperty<>(new CalendarCell(timeSlot));
        this.tuesday = new SimpleObjectProperty<>(new CalendarCell(timeSlot));
        this.wednesday = new SimpleObjectProperty<>(new CalendarCell(timeSlot));
        this.thursday = new SimpleObjectProperty<>(new CalendarCell(timeSlot));
        this.friday = new SimpleObjectProperty<>(new CalendarCell(timeSlot));
        this.saturday = new SimpleObjectProperty<>(new CalendarCell(timeSlot));
        this.sunday = new SimpleObjectProperty<>(new CalendarCell(timeSlot));
    }

    public void addColor(DayOfWeek day, Color color) {
        CalendarCell cell = getColumn(day);
        if(cell != null) {
            cell.setColor(color);
        }

    }

    public void addBooking(DayOfWeek day, Booking booking) {
        CalendarCell cell = getColumn(day);
        if(cell != null) {
            cell.setBooking(booking);
            cell.setCellContent(booking.getUniversityRoom().getNumber());
            cell.setTooltip("Capacity: " + booking.getUniversityRoom().getCapacity() + "\nParticipants: "
                    + booking.getParticipants().stream().map(p -> p.getFirstName() + " " + p.getLastName())
                    .collect(Collectors.joining(", ")));
        }
    }

    public void addNumber(DayOfWeek day, int number) {
        CalendarCell cell = getColumn(day);
        if(cell != null) {
            cell.setNumber(number);
        }
    }

    private CalendarCell getColumn(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return monday.get();
            case TUESDAY:
                return tuesday.get();
            case WEDNESDAY:
                return wednesday.get();
            case THURSDAY:
                return thursday.get();
            case FRIDAY:
                return friday.get();
            case SATURDAY:
                return saturday.get();
            case SUNDAY:
                return sunday.get();
            default:
            return null;
        }
    }

    public CalendarCell getSlots() {
        return slots.get();
    }

    public SimpleObjectProperty<CalendarCell> slotsProperty() {
        return slots;
    }

    public void setSlots(CalendarCell slots) {
        this.slots.set(slots);
    }

    public CalendarCell getMonday() {
        return monday.get();
    }

    public SimpleObjectProperty<CalendarCell> mondayProperty() {
        return monday;
    }

    public void setMonday(CalendarCell monday) {
        this.monday.set(monday);
    }

    public CalendarCell getTuesday() {
        return tuesday.get();
    }

    public SimpleObjectProperty<CalendarCell> tuesdayProperty() {
        return tuesday;
    }

    public void setTuesday(CalendarCell tuesday) {
        this.tuesday.set(tuesday);
    }

    public CalendarCell getWednesday() {
        return wednesday.get();
    }

    public SimpleObjectProperty<CalendarCell> wednesdayProperty() {
        return wednesday;
    }

    public void setWednesday(CalendarCell wednesday) {
        this.wednesday.set(wednesday);
    }

    public CalendarCell getThursday() {
        return thursday.get();
    }

    public SimpleObjectProperty<CalendarCell> thursdayProperty() {
        return thursday;
    }

    public void setThursday(CalendarCell thursday) {
        this.thursday.set(thursday);
    }

    public CalendarCell getFriday() {
        return friday.get();
    }

    public SimpleObjectProperty<CalendarCell> fridayProperty() {
        return friday;
    }

    public void setFriday(CalendarCell friday) {
        this.friday.set(friday);
    }

    public CalendarCell getSaturday() {
        return saturday.get();
    }

    public SimpleObjectProperty<CalendarCell> saturdayProperty() {
        return saturday;
    }

    public void setSaturday(CalendarCell saturday) {
        this.saturday.set(saturday);
    }

    public CalendarCell getSunday() {
        return sunday.get();
    }

    public SimpleObjectProperty<CalendarCell> sundayProperty() {
        return sunday;
    }

    public void setSunday(CalendarCell sunday) {
        this.sunday.set(sunday);
    }
}