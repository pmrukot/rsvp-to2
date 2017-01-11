package rsvp.resources;

import org.junit.Before;
import org.junit.Test;
import rsvp.resources.DAO.TimeSlotDAO;
import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.TimeSlotManager;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class TimeSlotManagerTest {
    private TimeSlotManager timeSlotManager;
    private static final String COLLISION_ALERT = "You have to provide time slot not colliding with existing ones";

    @Before
    public void setUp() {
        TimeSlotDAO timeSlotDAO = mock(TimeSlotDAO.class);
        timeSlotManager = new TimeSlotManager(timeSlotDAO);
    }

    @Test
    public void testCreateNewTimeSlot() {
        assertEquals(Optional.empty(),
                timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00"))));

        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("07:30"), LocalTime.parse("08:30"))));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("08:30"))));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:15"), LocalTime.parse("08:45"))));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:30"), LocalTime.parse("09:00"))));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00"))));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:30"), LocalTime.parse("09:30"))));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("10:00"))));

        List<TimeSlot> someTimeSlots = new ArrayList<>();
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testGetTimeSlots() {
        timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        List<TimeSlot> someTimeSlots = new ArrayList<>();
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testDeleteTimeSlot() {
        timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        timeSlotManager.createNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        List<TimeSlot> someTimeSlots = new ArrayList<>();
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());


        timeSlotManager.deleteTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));
        someTimeSlots.remove(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testUpdateTimeSlot() {
        TimeSlot timeSlot1 = TimeSlot.createTimeSlot(LocalTime.parse("06:00"), LocalTime.parse("07:00"));
        TimeSlot timeSlot2 = TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00"));

        assertEquals(Optional.empty(), timeSlotManager.createNewTimeSlot(timeSlot1));
        assertEquals(Optional.empty(), timeSlotManager.createNewTimeSlot(timeSlot2));

        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("07:30"), LocalTime.parse("08:30")));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("08:00"), LocalTime.parse("08:30")));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("08:15"), LocalTime.parse("08:45")));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("08:30"), LocalTime.parse("09:00")));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("08:30"), LocalTime.parse("09:30")));
        assertEquals(Optional.of(COLLISION_ALERT),
                timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("07:00"), LocalTime.parse("10:00")));


        List<TimeSlot> someTimeSlots = new ArrayList<>();
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("06:00"), LocalTime.parse("07:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());

//        TODO: fix this
//        assertEquals(Optional.empty(), timeSlotManager.updateTimeSlot(timeSlot2, LocalTime.parse("10:00"), LocalTime.parse("11:00")));
//
//        someTimeSlots.remove(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
//        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
//
//        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }
}
