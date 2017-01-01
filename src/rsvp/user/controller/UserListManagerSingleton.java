package rsvp.user.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.model.User;
import java.util.List;

public class UserListManagerSingleton {
    private static ObservableList<User> users;
    private static UserListManagerSingleton instance;

    private UserListManagerSingleton() {
        users = FXCollections.observableArrayList();
        users.addAll(new DBUserDAO().findUsersByName(""));
    }

    public static UserListManagerSingleton getInstance() {
        if(instance == null) {
            synchronized (UserListManagerSingleton.class) {
                if(instance == null) {
                    instance = new UserListManagerSingleton();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public void addListener(ListChangeListener listener) {
        users.addListener(listener);
    }

    ObservableList<User> getUsers() {
        return FXCollections.unmodifiableObservableList(users);
    }

    public void addUser(User u) {
        users.add(u);
    }

    public void addAllUsers(List<User> userList) {
        users.addAll(userList);
    }

    public void removeUser(User u) {
        users.remove(u);
    }

    public void updateUser(int index, User u) {
        users.set(index, u);
    }

    public int indexOf(User u) {
        int index = users.indexOf(u);
        if(index == -1) {
            for(int i = 0; i < users.size(); i++) {
                if(users.get(i).getId() == u.getId()) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }
}
