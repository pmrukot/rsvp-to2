package rsvp.resources.model;


import rsvp.resources.DAO.TimeSlotDAO;

import java.time.DayOfWeek;
import java.util.Map;

public class CalendarTableItem {

    private int timeSlotID;
    private Map<DayOfWeek, String> bookingsMap;

    public CalendarTableItem(Map<DayOfWeek, String> bookingsMap, int timeSlotID){
        this.timeSlotID = timeSlotID;
        this.bookingsMap = bookingsMap;
    }

    public String getTimeSlotRepresentation(){
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        TimeSlot timeSlot = timeSlotDAO.getTimeSlotByID(timeSlotID);
        return timeSlot.getStartTime().toString() +  " - " + timeSlot.getEndTime().toString();
    }

    public String getBookingDescriptionPerDay(int dayNumber){
        DayOfWeek day = DayOfWeek.of(dayNumber);
        if (bookingsMap.containsKey(day)){
            return bookingsMap.get(day);
        }
        return "";
    }
}
