package rsvp.booking.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
import rsvp.resources.model.UniversityRoom;

import java.util.List;

public class DBBookingDAO implements BookingDAO {

    @Override
    public void deleteBooking(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(booking);
        transaction.commit();
        session.close();
    }

    @Override
    public void createBooking(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(booking);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateBooking(Booking booking) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(booking);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Booking> getAllBookings() {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        List<Booking> result = session.createQuery("from Booking b", Booking.class).getResultList();

        transaction.commit();
        session.close();
        return result;
    }

    @Override
    public List<Booking> getAllBookingsForCurrentUser() {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsForUniversityRoom(UniversityRoom universityRoom) {
        return null;
    }
}
