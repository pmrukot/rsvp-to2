package rsvp.user.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import rsvp.common.persistence.HibernateUtils;

import java.io.File;
import java.util.List;

public abstract class UserUtils {
    static String generateLogin(String firstName, String lastName) {
        String login = firstName + lastName;
        Session session = HibernateUtils.getSession();
        String sql = "select count(*) " +
                "from User u " +
                "where u.firstName = :firstName and u.lastName = :lastName";
        Query query = session.createQuery(sql);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        long count = (long) query.uniqueResult();
        // todo which is better: load list of users once and iterate over it or select 1 user from db (potentially) multiple times?
        do {
            count++;
        } while(session.get(User.class, login + count) != null);
        login += count;
        session.close();
        return login;
    }

    public static boolean createUser(String firstName, String lastName, String password, boolean isAdmin) {
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

    public static boolean updateUser(String login, String firstName, String lastName, String password, boolean isAdmin) {
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

    public static boolean deleteUser(String login) {
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

    public static List<User> getUsersByName(String fullName) {
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

    public static boolean createUsersFromCsv(File file) {
        // todo implement
        return false;
    }
}
