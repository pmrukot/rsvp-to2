package rsvp.user.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rsvp.user.DAO.UserDAO;
import rsvp.user.controller.UserListManagerSingleton;
import rsvp.user.model.User;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUserCommandTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private User user;

    @InjectMocks
    private UpdateUserCommand command;

    @Test
    public void execute() throws Exception {
        when(userDAO.updateUser(user)).thenReturn(true);
        boolean result = command.execute();
        assertTrue(result);
    }

    @Test
    public void undo() throws Exception {
        when(userDAO.updateUser(user)).thenReturn(true);
        boolean result = command.undo();
        assertTrue(result);
    }

    @Test
    public void redo() throws Exception {
        when(userDAO.updateUser(user)).thenReturn(true);
        boolean result = command.redo();
        assertTrue(result);
    }
}
