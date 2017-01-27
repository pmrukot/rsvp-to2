package rsvp.user.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandManagerTest {

    @Mock
    private Command command;

    @InjectMocks
    private CommandManager commandManager;

    @Test
    public void testExecuteCommand() {
        when(command.execute()).thenReturn(true);
        boolean result = commandManager.executeCommand(command);
        assertTrue(result);
        assertThat(commandManager.undoImpossible.get(), is(false));
        assertThat(commandManager.redoImpossible.get(), is(true));
        assertThat(commandManager.getExecutedCommandsStackSize(), is(1));
        assertThat(commandManager.getExecutedCommands().firstElement(), is(command));
        assertThat(commandManager.getRedoableCommandsStackSize(), is(0));
    }

    @Test
    public void testExecuteCommandThanRevert() {
        when(command.execute()).thenReturn(true);
        when(command.undo()).thenReturn(true);

        commandManager.executeCommand(command);
        commandManager.undo();

        assertThat(commandManager.undoImpossible.get(), is(true));
        assertThat(commandManager.redoImpossible.get(), is(false));
        assertThat(commandManager.getExecutedCommandsStackSize(), is(0));
        assertThat(commandManager.getRedoableCommands().firstElement(), is(command));
        assertThat(commandManager.getRedoableCommandsStackSize(), is(1));
    }

    @Test
    public void testRedoCommand() {
        when(command.execute()).thenReturn(true);
        when(command.undo()).thenReturn(true);
        when(command.redo()).thenReturn(true);

        boolean result = commandManager.executeCommand(command);
        commandManager.undo();
        commandManager.redo();

        assertTrue(result);
        assertThat(commandManager.undoImpossible.get(), is(false));
        assertThat(commandManager.redoImpossible.get(), is(true));
        assertThat(commandManager.getExecutedCommandsStackSize(), is(1));
        assertThat(commandManager.getExecutedCommands().firstElement(), is(command));
        assertThat(commandManager.getRedoableCommandsStackSize(), is(0));
    }
}
