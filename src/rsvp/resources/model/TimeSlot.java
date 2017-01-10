package rsvp.resources.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

@Entity
@Table(name = "TimeSlot")
public class TimeSlot implements Comparable<TimeSlot> {

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

    public boolean setStartAndEndTime(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null || !startTime.isBefore(endTime)) {
            return false;
        }
        setStartTime(startTime);
        setEndTime(endTime);
        return true;
    }

    private void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    private void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof TimeSlot && ((TimeSlot) object).getStartTime().equals(this.startTime) &&
                ((TimeSlot) object).getEndTime().equals(this.endTime);
    }

    @Override public int hashCode() {
        return Arrays.hashCode(new Object[]{startTime, endTime});
    }

    @Override
    public int compareTo(TimeSlot anotherTimeSlot) {
        return this.startTime.compareTo(anotherTimeSlot.getStartTime());
    }

    public String toString() {
        return startTime.toString() + " - " + endTime.toString();
    }
}
