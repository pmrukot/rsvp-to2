package rsvp.user.command;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Stack;

public class CommandManager {
    private Stack<Command> executedCommands;
    private Stack<Command> waitingCommands;
    public BooleanProperty undoPossible;
    public BooleanProperty redoPossible;

    public CommandManager() {
        executedCommands = new Stack<>();
        waitingCommands = new Stack<>();
        undoPossible = new SimpleBooleanProperty(executedCommands.empty());
        redoPossible = new SimpleBooleanProperty(waitingCommands.empty());
    }

    public boolean executeCommand(Command c) {
        boolean success = false;
        if(c.execute()) {
            executedCommands.push(c);
            success = true;
        }
        updateBindings();
        return success;
    }

    public void undo() {
        Command c = executedCommands.pop();
        if(c.undo()) {
            waitingCommands.push(c);
        } else {
            executedCommands.push(c);
        }
        updateBindings();
    }

    public void redo() {
        Command c = waitingCommands.pop();
        if(c.execute()) {
            executedCommands.push(c);
        } else {
            waitingCommands.push(c);
        }
        updateBindings();
    }

    private void updateBindings() {
        undoPossible.setValue(executedCommands.empty());
        redoPossible.setValue(waitingCommands.empty());
    }
}
