package rsvp.resources.api;

import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.DAO.UniversityRoomDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;

import java.util.Collections;
import java.util.List;

public class ResourcesProvider {

    public List<TimeSlot> getAllTimeSlots(){
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        List<TimeSlot> result = timeSlotDAO.getAll();
        Collections.sort(result);
        return result;
    }

    public List<UniversityRoom> getAllUniversityRooms(){
        UniversityRoomDAO universityRoomDAO = new UniversityRoomDAO();
        return universityRoomDAO.getAll();
    }

}
