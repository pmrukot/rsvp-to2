package rsvp.user.API;

import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import rsvp.user.model.User;
import java.util.List;

public class UserProvider {
    public List<User> getAllUsers() {
        UserDAO userDAO = new DBUserDAO();
        return userDAO.findUsersByName("");
    }
    public List<User> getUsersByName(String name) {
        UserDAO userDAO = new DBUserDAO();
        return userDAO.findUsersByName(name);
    }
}
