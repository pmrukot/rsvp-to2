package rsvp.user.DAO;

import rsvp.common.persistence.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import rsvp.user.model.User;
import java.util.List;

public class DBUserDAO implements UserDAO {
    @Override
    public boolean createUser(String firstName, String lastName, String password, boolean isAdmin) {
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            User u = new User(firstName, lastName, password, isAdmin);
            session.persist(u);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
    public boolean updateUser(String login, String firstName, String lastName, String password, boolean isAdmin) {
        try {
            // todo refactor
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, login);
            boolean updateLogin = false;
            if(!user.getFirstName().equals(firstName)) {
                user.setFirstName(firstName);
                updateLogin = true;
            }
            if(!user.getLastName().equals(lastName)) {
                user.setLastName(lastName);
                updateLogin = true;
            }
            if(updateLogin) {
                user.setLogin();
            }
            user.setPassword(password);
            user.setAdmin(isAdmin);
            session.update(user);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePassword(String login, String password) {
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, login);
            user.setPassword(password);
            session.update(user);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(String login) {
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            User u = session.get(User.class, login);
            session.delete(u);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
