package rsvp.user.API;

import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import rsvp.user.model.User;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class UserProviderSingleton extends Observable {
    private static UserProviderSingleton instance;
    private static UserDAO userDAO;
    private static List<User> users;

    private UserProviderSingleton() {
        userDAO = new DBUserDAO();
        users = userDAO.findUsersByName("");
    }

    public static UserProviderSingleton getInstance() {
        if(instance == null) {
            synchronized (UserProviderSingleton.class) {
                if(instance == null) {
                    instance = new UserProviderSingleton();
                }
            }
        }
        return instance;
    }

    public void update() {
        users = userDAO.findUsersByName("");
        setChanged();
        notifyObservers();
    }

    public List<User> getUsers() {
        return users;
    }

    public List<User> getUsersByLogin(String login) {
        return users.stream().filter(u -> u.getLogin().toLowerCase().contains(login.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<User> getUsersByFirstName(String firstName) {
        return users.stream().filter(u -> u.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<User> getUsersByLastName(String lastName) {
        return users.stream().filter(u -> u.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<User> getUsersByFirstAndLastName(String firstAndLastName) {
        return users.stream().filter(u -> {
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
        }).collect(Collectors.toList());
    }
}
