package rsvp.user.model;

import rsvp.common.persistence.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.io.File;

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

    public static boolean createUsersFromCsv(File file) {
        // todo implement me
        return false;
    }
}
