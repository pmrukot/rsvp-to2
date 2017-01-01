package rsvp.resources.model;

import rsvp.resources.DAO.TimeSlotDAO;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TimeSlotManager {
    private static final String COLLISION_ALERT = "You have to provide time slot not colliding with existing ones";

    private List<TimeSlot> timeSlots;
    private TimeSlotDAO timeSlotDAO;

    public TimeSlotManager(){
        timeSlotDAO = new TimeSlotDAO();
        timeSlots = new ArrayList<>();
        timeSlots.addAll(timeSlotDAO.getAll());
    }

    private boolean isColliding(LocalTime insertedStartTime, LocalTime insertedEndTime, TimeSlot excludedTimeSlot) {
        for(TimeSlot timeSlot : timeSlots) {
            if(excludedTimeSlot == null || timeSlot != excludedTimeSlot) {
                LocalTime currentStartTime = timeSlot.getStartTime();
                LocalTime currentEndTime = timeSlot.getEndTime();
                return (((insertedStartTime.isBefore(currentStartTime) && insertedEndTime.isAfter(currentStartTime))) ||
                        ((insertedStartTime.equals(currentStartTime) || insertedEndTime.equals(currentEndTime))) ||
                        ((insertedStartTime.isAfter(currentStartTime) && insertedEndTime.isBefore(currentEndTime))) ||
                        ((insertedStartTime.isBefore(currentEndTime) && insertedEndTime.isAfter(currentEndTime))));
            }
        }
        return false;
    }

    public Optional<String> createNewTimeSlot(TimeSlot timeSlot){
        if(isColliding(timeSlot.getStartTime(), timeSlot.getEndTime(), null)){
            return Optional.of(COLLISION_ALERT);
        }
        timeSlots.add(timeSlot);
        timeSlotDAO.create(timeSlot);
        return Optional.empty();
    }

    public List<TimeSlot> getTimeSlots(){
        return Collections.unmodifiableList(timeSlots);
    }

    public void deleteTimeSlot(TimeSlot timeSlot){
        timeSlotDAO.delete(timeSlot);
        timeSlots.remove(timeSlot);
    }

    public Optional<String> updateTimeSlot(TimeSlot timeSlot, LocalTime newStartTime, LocalTime newEndTime){
        if(isColliding(newStartTime, newEndTime, timeSlot)) {
            return Optional.of(COLLISION_ALERT);
        }
        timeSlotDAO.update(timeSlot, newStartTime, newEndTime);
        timeSlots.clear();
        timeSlots.addAll(timeSlotDAO.getAll());
        return Optional.empty();
    }

}
