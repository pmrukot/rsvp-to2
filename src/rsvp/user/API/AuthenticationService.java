package rsvp.user.API;

import org.hibernate.Session;
import org.hibernate.query.Query;
import rsvp.common.persistence.HibernateUtils;
import rsvp.user.model.User;

public abstract class AuthenticationService {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    private static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static boolean authenticateUser(String login, String password) {
        boolean success = false;
        if(getCurrentUser() == null) {
            String sql = "select u " +
                    "from User u " +
                    "where u.login = :login";
            Session session = HibernateUtils.getSession();
            Query query = session.createQuery(sql, User.class);
            query.setParameter("login", login);
            User user = (User) query.uniqueResult();
            if(user != null) {
                if(user.getPassword().equals(password)) {
                    setCurrentUser(user);
                    success = true;
                }
            }
            session.close();
        }
        return success;
    }

    public static boolean logout() {
        boolean success = false;
        if(getCurrentUser() != null) {
            setCurrentUser(null);
            success = true;
        }
        return success;
    }
}
