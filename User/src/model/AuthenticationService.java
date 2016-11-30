package model;

import org.hibernate.Session;

public abstract class AuthenticationService {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    private static void setCurrentUser(User user) {
        currentUser = user;
    }

    static boolean authenticateUser(String login, String password) {
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

    static boolean logOut() {
        boolean success = false;
        if(getCurrentUser() != null) {
            setCurrentUser(null);
            success = true;
        }
        return success;
    }
}
