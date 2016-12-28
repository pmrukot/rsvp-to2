package rsvp.user.model;

import javafx.beans.property.SimpleStringProperty;

public class ReservationsPerWeek {

    private SimpleStringProperty slots;
    private SimpleStringProperty monday;
    private SimpleStringProperty tuesday;
    private SimpleStringProperty wednesday;
    private SimpleStringProperty thursday;
    private SimpleStringProperty friday;
    private SimpleStringProperty saturday;
    private SimpleStringProperty sunday;

    public ReservationsPerWeek(String slot, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.slots = new SimpleStringProperty(slot);
        this.monday = new SimpleStringProperty(monday);
        this.tuesday = new SimpleStringProperty(tuesday);
        this.wednesday = new SimpleStringProperty(wednesday);
        this.thursday = new SimpleStringProperty(thursday);
        this.friday = new SimpleStringProperty(friday);
        this.saturday = new SimpleStringProperty(saturday);
        this.sunday = new SimpleStringProperty(sunday);
    }

    public String getSlots() {
        return slots.get();
    }

    public SimpleStringProperty slotsProperty() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots.set(slots);
    }

    public String getMonday() {
        return monday.get();
    }

    public SimpleStringProperty mondayProperty() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday.set(monday);
    }

    public String getTuesday() {
        return tuesday.get();
    }

    public SimpleStringProperty tuesdayProperty() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday.set(tuesday);
    }

    public String getWednesday() {
        return wednesday.get();
    }

    public SimpleStringProperty wednesdayProperty() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday.set(wednesday);
    }

    public String getThursday() {
        return thursday.get();
    }

    public SimpleStringProperty thursdayProperty() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday.set(thursday);
    }

    public String getFriday() {
        return friday.get();
    }

    public SimpleStringProperty fridayProperty() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday.set(friday);
    }

    public String getSaturday() {
        return saturday.get();
    }

    public SimpleStringProperty saturdayProperty() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday.set(saturday);
    }

    public String getSunday() {
        return sunday.get();
    }

    public SimpleStringProperty sundayProperty() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday.set(sunday);
    }
}
