package rsvp.booking.DAO;

import rsvp.booking.model.Booking;
import rsvp.resources.model.UniversityRoom;

import java.util.List;

public interface BookingDAO {
    void deleteBooking(Booking booking);
    void createBooking(Booking booking);
    void updateBooking(Booking booking);
    List<Booking> getAllBookings();
    List<Booking> getAllBookingsForCurrentUser();
    List<Booking> getAllBookingsForUniversityRoom(UniversityRoom universityRoom);
}
