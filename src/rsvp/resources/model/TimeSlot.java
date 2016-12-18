package rsvp.resources.model;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "TimeSlot")
public class TimeSlot implements Comparable<TimeSlot>{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    private  LocalTime endTime;

    public TimeSlot() {}

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public int compareTo(TimeSlot anotherTimeSlot) {
        return this.startTime.compareTo(anotherTimeSlot.getStartTime());
    }

    public String toString(){
        return startTime.toString() + " - " + endTime.toString();
    }
}
