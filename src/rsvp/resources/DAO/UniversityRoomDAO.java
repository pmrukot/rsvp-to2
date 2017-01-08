package rsvp.resources.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.common.persistence.HibernateUtils;
import rsvp.resources.model.UniversityRoom;

import java.util.List;

public class UniversityRoomDAO {

    public void create(UniversityRoom universityRoom) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(universityRoom);
        transaction.commit();
        session.close();
    }

    public void delete(UniversityRoom universityRoom) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(universityRoom);
        transaction.commit();
        session.close();
    }

    public void update(UniversityRoom universityRoom, String newNumber, Integer newCapacity) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        UniversityRoom updatedUniversityRoom = session.get(UniversityRoom.class, universityRoom.getId());
        updatedUniversityRoom.setNumber(newNumber);
        updatedUniversityRoom.setCapacity(newCapacity);
        transaction.commit();
        session.close();
    }

    public List<UniversityRoom> getAll() {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        List<UniversityRoom> result = session.createQuery("from UniversityRoom u", UniversityRoom.class).getResultList();
        transaction.commit();
        session.close();
        return result;
    }
}
