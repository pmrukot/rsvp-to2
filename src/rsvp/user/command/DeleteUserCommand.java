package rsvp.user.command;

import org.hibernate.Session;
import rsvp.common.persistence.HibernateUtils;
import rsvp.user.DAO.UserDAO;
import rsvp.user.controller.UserListManagerSingleton;
import rsvp.user.model.User;

public class DeleteUserCommand implements Command {
    private UserDAO userDAO;
    private User user;

    public DeleteUserCommand(UserDAO userDAO, User user) {
        this.userDAO = userDAO;
        this.user = user;
    }

    @Override
    public boolean execute() {
        if(userDAO.deleteUser(user)) {
            UserListManagerSingleton.getInstance().removeUser(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        // todo at this point user object is detached...
        // http://stackoverflow.com/questions/912659/what-is-the-proper-way-to-re-attach-detached-objects-in-hibernate
        if(userDAO.mergeUser(user)) { // todo temporary workaround
            UserListManagerSingleton.getInstance().addUser(user);
            return true;
        }
        return false;
    }
}
