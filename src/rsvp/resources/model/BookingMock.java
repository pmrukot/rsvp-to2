package rsvp.resources.model;

import java.time.DayOfWeek;

public class BookingMock {

    private int timeSlotID;
    private String value;
    private DayOfWeek dayOfWeek;

    public BookingMock(int timeSlotID, String value, DayOfWeek dayOfWeek){
        this.timeSlotID = timeSlotID;
        this.value = value;
        this.dayOfWeek = dayOfWeek;
    }

    public int getTimeSlot(){
        return timeSlotID;
    }

    public String getValue(){
        return value;
    }

    public DayOfWeek getDayOfWeek(){
        return dayOfWeek;
    }

}
