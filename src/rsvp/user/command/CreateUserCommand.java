package rsvp.user.command;

import rsvp.user.DAO.UserDAO;
import rsvp.user.controller.UserListManagerSingleton;
import rsvp.user.model.User;

public class CreateUserCommand implements Command {
    private UserDAO userDAO;
    private User user;

    public CreateUserCommand(UserDAO userDAO, User user) {
        this.userDAO = userDAO;
        this.user = user;
    }

    @Override
    public boolean execute() {
        if(userDAO.createUser(user)) {
            UserListManagerSingleton.getInstance().addUser(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if(userDAO.deleteUser(user)) {
            UserListManagerSingleton.getInstance().removeUser(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean redo() {
        User newUser = new User(user.getLogin(), user.getFirstName(), user.getLastName(), user.getPassword(), user.isAdmin());
        if(userDAO.createUser(newUser)) {
            UserListManagerSingleton.getInstance().addUser(newUser);
            this.user = newUser;
            return true;
        }
        return false;
    }
}
