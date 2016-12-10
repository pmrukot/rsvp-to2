package rsvp.user.API;

import org.hibernate.Session;
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
            Session session = HibernateUtils.getSession();
            User user = session.get(User.class, login);
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
