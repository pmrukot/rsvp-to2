package rsvp.booking.DAO;

import rsvp.booking.model.Booking;
import rsvp.resources.model.UniversityRoom;

import java.sql.Date;
import java.util.List;

public interface BookingDAO {
    void deleteBooking(Booking booking);
    List<Booking> deleteCyclicBookings(long rootId);
    void createBooking(Booking booking);
    void updateBooking(Booking booking);
    List<Booking> getCyclicBookings(long rootId);
    List<Booking> getAllBookings();
    List<Booking> getAllBookingsForCurrentUser();
    List<Booking> getAllBookingsForUniversityRoom(UniversityRoom universityRoom);
    List<Booking> getAllBookingsForGivenPeriodAndUniversityRoom(
            Date startingDate, Date endingDate, UniversityRoom universityRoom);
}
