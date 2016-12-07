package rsvp.resources.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.common.persistence.HibernateUtils;
import rsvp.resources.model.TimeSlot;
import java.time.LocalTime;
import java.util.List;

public class TimeSlotDAO {

    public static void create(TimeSlot timeSlot) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(timeSlot);
        transaction.commit();
        session.close();
    }

    public static  void delete(TimeSlot timeSlot) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(timeSlot);
        transaction.commit();
        session.close();
    }

    public static void update(TimeSlot timeSlot, LocalTime newStartTime, LocalTime newEndTime) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        TimeSlot updatedTimeSlot = session.get(TimeSlot.class, timeSlot.getId());
        updatedTimeSlot.setStartTime(newStartTime);
        updatedTimeSlot.setEndTime(newEndTime);
        transaction.commit();
        session.close();
    }

    public static List<TimeSlot> getAll() {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        List<TimeSlot> result = session.createQuery("from TimeSlot t", TimeSlot.class).getResultList();
        transaction.commit();
        session.close();
        return result;
    }
}
