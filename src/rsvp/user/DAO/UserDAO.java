package rsvp.user.DAO;

import rsvp.user.model.User;
import java.util.List;

public interface UserDAO {
    boolean createUser(String firstName, String lastName, String password, boolean isAdmin);
    List<User> findUsersByName(String fullName);
    boolean updateUser(String login, String firstName, String lastName, String password, boolean isAdmin);
    boolean deleteUser(String login);
    boolean updatePassword(String login, String password);
}
