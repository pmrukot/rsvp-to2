package rsvp.resources.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UniversityRoom {

    private final StringProperty number;
    private final IntegerProperty capacity;

    public UniversityRoom(String number, Integer capacity) {
        this.number = new SimpleStringProperty(number);
        this.capacity = new SimpleIntegerProperty(capacity);
    }

    public final String getNumber() {
        return number.get();
    }

    public final void setNumber(String number) {
        this.number.set(number);
    }

    public final StringProperty getNumberProperty() {
        return number;
    }

    public final Integer getCapacity () {
        return capacity.get();
    }

    public final void setCapacity (Integer capacity) {
        this.capacity.set(capacity);
    }

    public final IntegerProperty getCapacityProperty() {
        return capacity;
    }
}
