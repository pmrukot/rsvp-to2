package rsvp.user.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rsvp.user.DAO.UserDAO;
import rsvp.user.model.User;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateUserCommandTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private User user;

    @InjectMocks
    private CreateUserCommand command;

    @Test
    public void execute() throws Exception {
        when(userDAO.createUser(user)).thenReturn(true);
        boolean result = command.execute();
        assertTrue(result);
        when(userDAO.createUser(user)).thenReturn(false);
        boolean result2 = command.execute();
        assertFalse(result2);
    }

    @Test
    public void undo() throws Exception {
        when(userDAO.deleteUser(user)).thenReturn(true);
        boolean result = command.undo();
        assertTrue(result);
        when(userDAO.deleteUser(user)).thenReturn(false);
        boolean result2 = command.undo();
        assertFalse(result2);
    }

    @Test
    public void redo() throws Exception {
        when(userDAO.createUser(any(User.class))).thenReturn(true);
        boolean result = command.redo();
        assertTrue(result);
        when(userDAO.createUser(any(User.class))).thenReturn(false);
        boolean result2 = command.redo();
        assertFalse(result2);
    }
}
