package rsvp.resources.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Optional;

@Entity
@Table(name = "TimeSlot")
public class TimeSlot implements Comparable<TimeSlot> {
    private static final String NOT_ENOUGH_ARGUMENTS_ALERT = "You have to provide all arguments";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    /**
     * public no argument constructor is a requirement for Hibernate
     */
    public TimeSlot() {
    }

    public static TimeSlot createTimeSlot(LocalTime startTime, LocalTime endTime) {
        if (startTime != null && endTime != null && startTime.isBefore(endTime)) {
            return new TimeSlot(startTime, endTime);
        }
        return null;
    }

    private TimeSlot(LocalTime startTime, LocalTime endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Optional<String> setStartTime(LocalTime startTime) {
        if (startTime == null) {
            return Optional.of(NOT_ENOUGH_ARGUMENTS_ALERT);
        }
        this.startTime = startTime;
        return Optional.empty();
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Optional<String> setEndTime(LocalTime endTime) {
        if (endTime == null) {
            return Optional.of(NOT_ENOUGH_ARGUMENTS_ALERT);
        }
        this.endTime = endTime;
        return Optional.empty();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof TimeSlot && ((TimeSlot) object).getStartTime().equals(this.startTime) &&
                ((TimeSlot) object).getEndTime().equals(this.endTime);
    }

    @Override
    public int compareTo(TimeSlot anotherTimeSlot) {
        return this.startTime.compareTo(anotherTimeSlot.getStartTime());
    }

    public String toString() {
        return startTime.toString() + " - " + endTime.toString();
    }
}
