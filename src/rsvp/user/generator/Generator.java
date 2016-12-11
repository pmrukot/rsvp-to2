package rsvp.user.generator;


import org.hibernate.Session;
import org.hibernate.query.Query;
import rsvp.common.persistence.HibernateUtils;
import rsvp.user.model.User;

public class Generator {

    public static String generateLogin(String firstName, String lastName) {
        String login = firstName + lastName;
        Session session = HibernateUtils.getSession();
        String countSql = "select count(*) " +
                "from User u " +
                "where u.firstName = :firstName and u.lastName = :lastName";
        Query countQuery = session.createQuery(countSql);
        countQuery.setParameter("firstName", firstName);
        countQuery.setParameter("lastName", lastName);
        long count = (long) countQuery.uniqueResult();
        // todo which is better: load list of users once and iterate over it or select 1 user from db (potentially) multiple times?
        User u;
        do {
            count++;
            String userSql = "select u " +
                    "from User u " +
                    "where login = :login";
            Query userQuery = session.createQuery(userSql);
            userQuery.setParameter("login", login + count);
            u = (User) userQuery.uniqueResult();
        } while(u != null);
        login += count;
        session.close();
        return login;
    }
    static void generatePassword() {

    }
}
