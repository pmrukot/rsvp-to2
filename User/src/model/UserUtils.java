package model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
        while(session.get(User.class, login + count) != null) {
            count++;
        }
        login += count;
        session.close();
        return login;
    }

    public static boolean createUser(String firstName, String lastName, String password, boolean isAdmin) {
        boolean success = true;
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            User u = new User(firstName, lastName, password, isAdmin);
            session.persist(u);
            transaction.commit();
            session.close();
        } catch (Exception e) { // todo what kind of exceptions do we expect here?
            e.printStackTrace();
            success = false;
        } finally {
            return success;
        }
    }

    public static boolean editUser(String login) {
        // todo implement me remembering that i should generate new login if first/last name is edited
        boolean success = true;
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, login);
            // edit user here
            session.update(user);
            transaction.commit();
            session.close();
        } catch (Exception e) { // todo what kind of exceptions do we expect here?
            e.printStackTrace();
            success = false;
        } finally {
            return success;
        }
    }

    public static boolean deleteUser(String login) {
        boolean success = true;
        try {
            Session session = HibernateUtils.getSession();
            Transaction transaction = session.beginTransaction();
            User u = session.get(User.class, login);
            session.delete(u);
            transaction.commit();
            session.close();
        } catch (Exception e) { // todo what kind of exceptions do we expect here?
            e.printStackTrace();
            success = false;
        } finally {
            return success;
        }
    }

    public static List<User> getUsersByName(String name) {
        // todo solve corner cases like: someone typed only firstName and hasn't typed lastName yet
        // (for example "imie" instead of "imie nazwisko" [operator like might be useful)
        String[] names = name.split(" ");
        Session session = HibernateUtils.getSession();
        String sql = "select u " +
                "from User u " +
                "where u.firstName = :firstName and u.lastName = :lastName";
        Query query = session.createQuery(sql, User.class);
        query.setParameter("firstName", names[0]);
        query.setParameter("lastName", names[1]);
        List<User> users = query.getResultList();
        session.close();
        return users;
    }

    public static boolean createUsersFromCsv() {
        // todo implement me
        boolean success = false;
        return success;
    }
}
