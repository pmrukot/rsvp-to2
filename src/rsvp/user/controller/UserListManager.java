package rsvp.user.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rsvp.user.API.UserProviderSingleton;
import rsvp.user.model.User;
import java.util.List;

public class UserListManager {
    private  static ObservableList<User> users;

    UserListManager(List<User> userList, AdminController adminController) {
        if(users == null) {
            users = FXCollections.observableList(userList);
        }
        if(adminController != null) {
            users.addListener(adminController);
        }
        users.addListener(UserProviderSingleton.getInstance());
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
        return users.indexOf(u);
    }
}
