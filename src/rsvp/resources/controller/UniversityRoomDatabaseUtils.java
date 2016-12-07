package rsvp.resources.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import rsvp.common.persistence.HibernateUtils;
import rsvp.resources.model.UniversityRoom;

import java.util.List;

public class UniversityRoomDatabaseUtils {

    protected static void create(UniversityRoom universityRoom) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(universityRoom);
        transaction.commit();
        session.close();
    }

    protected static void delete(UniversityRoom universityRoom) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(universityRoom);
        transaction.commit();
        session.close();
    }

    protected static List<UniversityRoom> getAll(){
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            List<UniversityRoom> result =
                    session.createQuery("from UniversityRoom u", UniversityRoom.class).getResultList();
            transaction.commit();
            session.close();
            return result;
    }
}
