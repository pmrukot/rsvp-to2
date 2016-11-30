package model;

import java.util.Optional;

public abstract class AuthenticationService {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    private static void setCurrentUser(User user) {
        currentUser = user;
    }

    static boolean AuthenticateUser(String username, String password) {
        boolean success = false;
        Optional<User> user = Optional.empty(); // todo get user from db
        if(user.isPresent()) {
            setCurrentUser(user.get());
            success = true;
        }
        return success;
    }

    static void LogOut() {
        setCurrentUser(null);
    }
}
