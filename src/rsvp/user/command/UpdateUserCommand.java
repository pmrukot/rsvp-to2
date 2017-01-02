package rsvp.user.command;

import rsvp.user.DAO.UserDAO;
import rsvp.user.controller.UserListManagerSingleton;
import rsvp.user.model.User;

public class UpdateUserCommand implements Command {
    private UserDAO userDAO;
    private User user;
    // old values:
    private String oldLogin;
    private String oldFirstName;
    private String oldLastName;
    private String oldPassword;
    private boolean oldAdmin;
    // new values:
    private String newLogin;
    private String newFirstName;
    private String newLastName;
    private String newPassword;
    private boolean newAdmin;

    public UpdateUserCommand(UserDAO userDAO, User user, String newLogin, String newFirstName,
                             String newLastName, String newPassword, boolean newAdmin) {
        this.userDAO = userDAO;
        this.user = user;
        oldLogin = user.getLogin();
        oldFirstName = user.getFirstName();
        oldLastName = user.getLastName();
        oldPassword = user.getPassword();
        oldAdmin = user.isAdmin();
        this.newLogin = newLogin;
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
        this.newPassword = newPassword;
        this.newAdmin = newAdmin;
    }

    @Override
    public boolean execute() {
        int index = UserListManagerSingleton.getInstance().indexOf(user);
        updateData();
        if(userDAO.updateUser(user)) {
            UserListManagerSingleton.getInstance().updateUser(index, user);
            return true;
        } else {
            restoreData();
            return false;
        }
    }

    @Override
    public boolean undo() {
        int index = UserListManagerSingleton.getInstance().indexOf(user);
        restoreData();
        if(userDAO.updateUser(user)) {
            UserListManagerSingleton.getInstance().updateUser(index, user);
            return true;
        } else {
            updateData();
            return false;
        }
    }

    private void updateData() {
        user.setLogin(newLogin);
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        user.setPassword(newPassword);
        user.setAdmin(newAdmin);
    }
    private void restoreData() {
        user.setLogin(oldLogin);
        user.setFirstName(oldFirstName);
        user.setLastName(oldLastName);
        user.setPassword(oldPassword);
        user.setAdmin(oldAdmin);
    }
}
