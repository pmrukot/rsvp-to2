package rsvp.user.DAO;

import rsvp.user.model.User;
import java.util.List;

public interface UserDAO {
    boolean createUser(User u);
    List<User> findUsersByName(String fullName);
    boolean updateUser(User u);
    boolean deleteUser(User u);
}
