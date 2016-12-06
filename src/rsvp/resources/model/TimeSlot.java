package rsvp.resources.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalTime;

public class TimeSlot {

    private final ObjectProperty<LocalTime> startTime;
    private final ObjectProperty<LocalTime> endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = new SimpleObjectProperty<LocalTime>(startTime);
        this.endTime = new SimpleObjectProperty<LocalTime>(endTime);
    }

    public final LocalTime getStartTime() {
        return startTime.get();
    }

    public final void setStartTime(LocalTime startTime) {
        this.startTime.set(startTime);
    }

    public final ObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }

    public final LocalTime getEndTime() {
        return endTime.get();
    }

    public final void setEndTime(LocalTime endTime) {
        this.endTime.set(endTime);
    }

    public final ObjectProperty<LocalTime> endTimeProperty() {
        return endTime;
    }
}
