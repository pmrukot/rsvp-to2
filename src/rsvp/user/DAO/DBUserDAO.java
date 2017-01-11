package rsvp.user.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import rsvp.common.persistence.HibernateUtils;
import rsvp.user.model.User;

import java.util.List;

public class DBUserDAO implements UserDAO {
    @Override
    public boolean createUser(User u) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(u);
            //session.saveOrUpdate(u); - redoing will throw the same as redoing delete user
            return true;
        } catch (Exception e) {
            try {
                session.merge(u);
                return true;
            } catch (Exception e1) {
                e.printStackTrace();
                return false;
            }
        } finally {
            transaction.commit();
            session.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUsersByName(String fullName) {
        String[] names = fullName.split("\\s+");
        String firstName = names[0];
        String sql = "select u " +
                "from User u ";
        Session session = HibernateUtils.getSession();
        Query query;
        if(names.length == 1) {
            sql += "where u.firstName like :firstName";
            query = session.createQuery(sql, User.class);
            query.setParameter("firstName", firstName + "%");
        } else {
            String lastName = names[1];
            sql += "where u.firstName = :firstName and u.lastName like :lastName";
            query = session.createQuery(sql, User.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName + "%");
        }
        List<User> users = query.getResultList();
        session.close();
        return users;
    }

    @Override
    public boolean updateUser(User u) {
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            session.update(u);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(User u) {
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            session.delete(u);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean mergeUser(User u) {
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            session.merge(u);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
