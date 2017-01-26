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
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewEndTimeIsDuringExistingTimeSlot() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("07:30"), LocalTime.parse("08:30"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewStartTimeIsDuringExistingTimeSlot() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("08:30"), LocalTime.parse("09:30"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewStartTimeIsTheSameAsExistingStartTime() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:30"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewEndTimeIsTheSameAsExistingEndTime() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("07:30"), LocalTime.parse("09:00"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsCollisionAlertWhenNewTimeSlotContainsExistingTimeSlot() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.of(COLLISION_ALERT), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("10:00"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsOptionalEmptyWhenNewStartTimeIsAfterExistingEndTime() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.empty(), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("19:00"), LocalTime.parse("20:00"))));
    }

    @Test
    public void testAddNewTimeSlotReturnsOptionalEmptyWhenNewEndTimeIsBeforeExistingStartTime() {
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(Optional.empty(), timeSlotManager.addNewTimeSlot(
                TimeSlot.createTimeSlot(LocalTime.parse("06:00"), LocalTime.parse("07:30"))));
    }

    @Test
    public void testAddNewTimeSlotsAddsNewTimeSlotWhenGivenCorrectHours() {
        List<TimeSlot> expectedTimeSlotList = new ArrayList<>();
        expectedTimeSlotList.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));

        assertEquals(expectedTimeSlotList, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testAddNewTimeSlotsAddsNewTimeSlotJustAfterAnotherWhenGivenCorrectHours() {
        List<TimeSlot> expectedTimeSlotList = new ArrayList<>();
        expectedTimeSlotList.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        expectedTimeSlotList.add(TimeSlot.createTimeSlot(LocalTime.parse("09:00"), LocalTime.parse("10:00")));

        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("09:00"), LocalTime.parse("10:00")));

        assertEquals(expectedTimeSlotList, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testGetTimeSlotsReturnsCorrectTimeSlotsEmptyList() {
        List<TimeSlot> expectedTimeSlotsList = new ArrayList<>();

        assertEquals(expectedTimeSlotsList, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testGetTimeSlotsReturnsCorrectListOfAllCorectlyAddedTimeSlots() {
        List<TimeSlot> expectedTimeSlotsList = new ArrayList<>();
        expectedTimeSlotsList.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        expectedTimeSlotsList.add(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        expectedTimeSlotsList.add(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        assertEquals(expectedTimeSlotsList, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testDeleteTimeSlotProperlyDeletesRequiredTimeSlot() {
        List<TimeSlot> expectedTimeSlotsList = new ArrayList<>();
        expectedTimeSlotsList.add(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        expectedTimeSlotsList.add(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));

        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        timeSlotManager.addNewTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));
        timeSlotManager.deleteTimeSlot(TimeSlot.createTimeSlot(LocalTime.parse("12:00"), LocalTime.parse("13:00")));

        assertEquals(expectedTimeSlotsList, timeSlotManager.getTimeSlots());
    }

    @Test
    public void testUpdateTimeSlotReturnsOptionalEmptyWhenGivenNotCollidingHours() {
        TimeSlot timeSlot = TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("08:00"));

        assertEquals(Optional.empty(), timeSlotManager.updateTimeSlot(
                timeSlot, LocalTime.parse("09:00"), LocalTime.parse("10:00")));
    }

    @Test
    public void testUpdateTimeSlotDoesNotChangeOriginalTimeSlotWhenGivenWrongHours() {
        List<TimeSlot> expectedTimeSlotsList = new ArrayList<>();
        expectedTimeSlotsList.add(TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("08:00")));
        expectedTimeSlotsList.add(TimeSlot.createTimeSlot(LocalTime.parse("09:00"), LocalTime.parse("10:00")));

        TimeSlot timeSlot1 = TimeSlot.createTimeSlot(LocalTime.parse("07:00"), LocalTime.parse("08:00"));
        TimeSlot timeSlot2 = TimeSlot.createTimeSlot(LocalTime.parse("09:00"), LocalTime.parse("10:00"));
        timeSlotManager.addNewTimeSlot(timeSlot1);
        timeSlotManager.addNewTimeSlot(timeSlot2);
        timeSlotManager.updateTimeSlot(timeSlot1, LocalTime.parse("08:30"), LocalTime.parse("09:30"));

        assertEquals(expectedTimeSlotsList, timeSlotManager.getTimeSlots());
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

        assertEquals(expectedTimeSlotList, timeSlotManager.getTimeSlots());
    }
}
