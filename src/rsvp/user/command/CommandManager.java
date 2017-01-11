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

    public void executeCommand(Command c) {
        if(c.execute()) {
            executedCommands.push(c);
            undoPossible.setValue(executedCommands.empty());
        }
    }

    public void undo() {
        Command c = executedCommands.pop();
        if(c.undo()) {
            waitingCommands.push(c);
            redoPossible.setValue(waitingCommands.empty());
        } else {
            executedCommands.push(c);
        }
    }

    public void redo() {
        Command c = waitingCommands.pop();
        if(c.execute()) {
            executedCommands.push(c);
            undoPossible.setValue(executedCommands.empty());
        } else {
            waitingCommands.push(c);
        }
    }
}
