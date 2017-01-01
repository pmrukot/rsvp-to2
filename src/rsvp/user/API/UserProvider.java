package rsvp.user.API;

import javafx.collections.ListChangeListener;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import rsvp.user.controller.UserListManagerSingleton;
import rsvp.user.model.User;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class UserProvider extends Observable implements ListChangeListener {
    private static UserDAO userDAO;
    private static List<User> users;

    private UserProvider() {
        userDAO = new DBUserDAO();
        users = userDAO.findUsersByName("");
        UserListManagerSingleton.getInstance().addListener(this);
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<User> getUsersByLogin(String login) {
        return Collections.unmodifiableList(
                users.stream().filter(u -> u.getLogin().toLowerCase().contains(login.toLowerCase()))
                .collect(Collectors.toList())
        );
    }

    public List<User> getUsersByFirstName(String firstName) {
        return Collections.unmodifiableList(
                users.stream().filter(u -> u.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .collect(Collectors.toList())
        );
    }

    public List<User> getUsersByLastName(String lastName) {
        return Collections.unmodifiableList(
                users.stream().filter(u -> u.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList())
        );
    }

    public List<User> getUsersByFirstAndLastName(String firstAndLastName) {
        return Collections.unmodifiableList(
                users.stream().filter(u -> {
                String[] names = firstAndLastName.split("\\s+");
                if(names.length > 0) {
                    if(names.length == 1) {
                        return u.getFirstName().toLowerCase().contains(names[0].toLowerCase());
                    } else {
                        return u.getFirstName().toLowerCase().equals(names[0].toLowerCase()) &&
                                u.getLastName().toLowerCase().contains(names[1].toLowerCase());
                    }
                } else {
                    return true;
                }
            }).collect(Collectors.toList())
        );
    }

    @Override
    public void onChanged(Change c) {
        users = userDAO.findUsersByName("");
        setChanged();
        notifyObservers();
    }
}
