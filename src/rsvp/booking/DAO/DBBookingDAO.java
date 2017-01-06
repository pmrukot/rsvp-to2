package rsvp.booking.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import rsvp.booking.model.Booking;
import rsvp.common.persistence.HibernateUtils;
import rsvp.resources.model.UniversityRoom;
import rsvp.user.API.AuthenticationService;
import rsvp.user.model.User;

import java.sql.Date;
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
    public List<Booking> deleteCyclicBookings(long rootId) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        List<Booking> result = session.createQuery("from Booking b where b.rootId = :rootId", Booking.class)
                                        .setParameter("rootId", rootId)
                                        .getResultList();
        Query query = session.createQuery("delete Booking  b where b.rootId = :rootId")
                                .setParameter("rootId", rootId);
        query.executeUpdate();
        transaction.commit();
        session.close();

        return result;
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
    public List<Booking> getCyclicBookings(long rootId) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        List<Booking> result = session.createQuery("from Booking b where b.rootId = :rootId", Booking.class)
                                    .setParameter("rootId", rootId)
                                    .getResultList();
        transaction.commit();
        session.close();
        return result;
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
        User currentUser = AuthenticationService.getCurrentUser();
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        List<Booking> result = session.createQuery("from Booking b where b.owner = :user", Booking.class)
                                      .setParameter("user", currentUser)
                                      .getResultList();
        transaction.commit();
        session.close();
        return result;
    }

    @Override
    public List<Booking> getAllBookingsForUniversityRoom(UniversityRoom universityRoom) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        List<Booking> result = session.createQuery("from Booking b where b.roomId = :room", Booking.class)
                .setParameter("room", universityRoom)
                .getResultList();
        transaction.commit();
        session.close();
        return result;
    }

    @Override
    public List<Booking> getAllBookingsForGivenPeriod(Date startingDate, Date endingDate) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        List<Booking> result = session.createQuery("from Booking b where b.reservationDate >= :startingDate and b.reservationDate <= :endingDate", Booking.class)
                .setParameter("startingDate", startingDate)
                .setParameter("endingDate", endingDate)
                .getResultList();
        transaction.commit();
        session.close();
        return result;
    }
}
