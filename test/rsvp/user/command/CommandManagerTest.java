package rsvp.user.command;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rsvp.user.DAO.DBUserDAO;
import rsvp.user.DAO.UserDAO;
import rsvp.user.model.User;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandManagerTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private CommandManager commandManager;


    @Test
    public void testExecuteCommand() {
        User u  = new User("Jan", "Nowak", "pass", true);
        Command c = new CreateUserCommand(userDAO, u);
        when(c.execute()).thenReturn(true);

        boolean result = commandManager.executeCommand(c);

        assertTrue(result);
        assertThat(commandManager.undoPossible.get(), is(false));
        assertThat(commandManager.redoPossible.get(), is(true));
        assertThat(commandManager.getExecutedCommandsStackSize(), is(1));
        assertThat(commandManager.getExecutedCommands().firstElement(), is(c));
        assertThat(commandManager.getRedoableCommandsStackSize(), is(0));
    }

    @Test
    public void testExecuteCommandThanRevert() {
        User u  = new User("Jan", "Nowak", "pass", true);
        Command c = new CreateUserCommand(userDAO, u);
        when(c.execute()).thenReturn(true);
        when(c.undo()).thenReturn(true);

        commandManager.executeCommand(c);
        commandManager.undo();

        assertThat(commandManager.undoPossible.get(), is(true));
        assertThat(commandManager.redoPossible.get(), is(false));
        assertThat(commandManager.getExecutedCommandsStackSize(), is(0));
        assertThat(commandManager.getRedoableCommands().firstElement(), is(c));
        assertThat(commandManager.getRedoableCommandsStackSize(), is(1));
    }

    @Test
    public void testRedoCommand() {
        User u  = new User("Jan", "Nowak", "pass", true);
        Command c = new CreateUserCommand(userDAO, u);
        when(c.execute()).thenReturn(true);
        when(c.undo()).thenReturn(true);
        when(c.redo()).thenReturn(true);

        boolean result = commandManager.executeCommand(c);
        commandManager.undo();
        commandManager.redo();

        assertTrue(result);
        assertThat(commandManager.undoPossible.get(), is(false));
        assertThat(commandManager.redoPossible.get(), is(true));
        assertThat(commandManager.getExecutedCommandsStackSize(), is(1));
        assertThat(commandManager.getExecutedCommands().firstElement(), is(c));
        assertThat(commandManager.getRedoableCommandsStackSize(), is(0));
    }
}
