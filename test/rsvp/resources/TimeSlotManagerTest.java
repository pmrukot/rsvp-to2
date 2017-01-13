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
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewTimeSlotsCollideCase1() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("07:30"), LocalTime.parse("08:30"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewTimeSlotsCollideCase2() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("08:30"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewTimeSlotsCollideCase3() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("08:15"), LocalTime.parse("08:45"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewTimeSlotsCollideCase4() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("08:30"), LocalTime.parse("09:00"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewTimeSlotsCollideCase5() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewTimeSlotsCollideCase6() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("08:30"), LocalTime.parse("09:30"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewTimeSlotsCollideCase7() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("10:00"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsOptionalEmptyWhenGivenCorrectHours() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.empty(), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("19:00"), LocalTime.parse("20:00"))));
    }

    @Test
    public void testAddNewTimeSlotsAddsNewTimeSlotWhenGivenCorrectHours() {
        List<TimeSlot> someTimeSlots = new ArrayList<>();
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testGetTimeSlotsReturnsCorrectTimeSlotsEmptyList() {
        List<TimeSlot> someTimeSlots = new ArrayList<>();

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testGetTimeSlotsReturnsCorrectTimeSlots() {
        List<TimeSlot> someTimeSlots = new ArrayList<>();
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testDeleteTimeSlot() {
        List<TimeSlot> someTimeSlots = new ArrayList<>();
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));

        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));
        timeSlotManager.deleteTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testUpdateTimeSlotReturnsOptionalEmptyWhenGivenCorrectHours() {
        TimeSlot timeSlot = TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("08:00"));

        assertEquals(Optional.empty(), timeSlotManager.updateTimeSlot(
                timeSlot, LocalTime.parse("09:00"), LocalTime.parse("10:00")));
    }

    @Test
    public void testUpdateTimeSlotDoesNotRemoveOriginalTimeSlotWhenGivenWrongHours() {
        List<TimeSlot> someTimeSlots = new ArrayList<>();
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("08:00")));
        someTimeSlots.add(TimeSlot.createTimeSlot(LocalTime.parse("09:00"), LocalTime.parse("10:00")));

        TimeSlot timeSlot1 = TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("08:00"));
        TimeSlot timeSlot2 = TimeSlot.createTimeSlot(LocalTime.parse("09:00"), LocalTime.parse("10:00"));
        timeSlotManager.addNewTimeSlot(timeSlot1);
        timeSlotManager.addNewTimeSlot(timeSlot2);
        timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("08:30"), LocalTime.parse("09:30"));

        assertEquals(someTimeSlots, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testUpdateTimeSlotUpdatesTimeSlotWhenGivenCorrectHours() {
        List<TimeSlot> expectedTimeSlotList = new ArrayList<>();
        expectedTimeSlotList.add(TimeSlot.createTimeSlot(LocalTime.parse("06:00"), LocalTime.parse("07:00")));
        expectedTimeSlotList.add(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));

        TimeSlot timeSlot1 = TimeSlot.createTimeSlot(LocalTime.parse("06:00"), LocalTime.parse("07:00"));
        TimeSlot timeSlot2 = TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00"));
        timeSlotManager.addNewTimeSlot(timeSlot1);
        timeSlotManager.addNewTimeSlot(timeSlot2);
        timeSlotManager.updateTimeSlot(timeSlot2, LocalTime.parse("10:00"), LocalTime.parse("11:00"));
        List<TimeSlot> actualTimeSlots = timeSlotManager.getTimeSlots();

        assertEquals(expectedTimeSlotList, actualTimeSlots);
    }
}
