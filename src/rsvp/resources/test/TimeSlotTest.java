package rsvp.resources.test;

import org.junit.Before;
import org.junit.Test;
import rsvp.resources.model.TimeSlot;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class TimeSlotTest {
    private TimeSlot timeSlot;

    @Before
    public void setUp() {
        timeSlot = new TimeSlot();
    }

    @Test
    public void TestCreateTimeSlot() {
        assertEquals(null, TimeSlot.createTimeSlot(null, null));
        assertEquals(null, TimeSlot.createTimeSlot(LocalTime.parse("13:00"), null));
        assertEquals(null, TimeSlot.createTimeSlot(null, LocalTime.parse("14:00")));
        assertEquals(null, TimeSlot.createTimeSlot(LocalTime.parse("13:00"), LocalTime.parse("13:00")));
        assertEquals(null, TimeSlot.createTimeSlot(LocalTime.parse("14:00"), LocalTime.parse("13:00")));

        assertTrue(TimeSlot.createTimeSlot(LocalTime.parse("13:00"), LocalTime.parse("14:00")) instanceof TimeSlot);
        TimeSlot createdTimeSlot = TimeSlot.createTimeSlot(LocalTime.parse("13:00"), LocalTime.parse("14:00"));
        assertNotNull(createdTimeSlot);
        assertEquals(LocalTime.parse("13:00"), createdTimeSlot.getStartTime());
        assertEquals(LocalTime.parse("14:00"), createdTimeSlot.getEndTime());
    }

    @Test
    public void testSetStartAndEndTime() {
        assertFalse(timeSlot.setStartAndEndTime(null, null));
        assertFalse(timeSlot.setStartAndEndTime(null, LocalTime.parse("14:00")));
        assertFalse(timeSlot.setStartAndEndTime(LocalTime.parse("13:00"), null));
        assertFalse(timeSlot.setStartAndEndTime(LocalTime.parse("13:00"), LocalTime.parse("13:00")));
        assertFalse(timeSlot.setStartAndEndTime(LocalTime.parse("14:00"), LocalTime.parse("13:00")));

        assertTrue(timeSlot.setStartAndEndTime(LocalTime.parse("13:00"), LocalTime.parse("14:00")));
        assertEquals(LocalTime.parse("13:00"), timeSlot.getStartTime());
        assertEquals(LocalTime.parse("14:00"), timeSlot.getEndTime());
    }

    @Test
    public void testToString() {
        timeSlot.setStartAndEndTime(LocalTime.parse("13:00"), LocalTime.parse("14:00"));
        assertEquals("13:00 - 14:00", timeSlot.toString());
    }

}