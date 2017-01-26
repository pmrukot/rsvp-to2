package rsvp.user.command;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Stack;

public class CommandManager {
    private Stack<Command> executedCommands;
    private Stack<Command> redoableCommands;
    public BooleanProperty undoImpossible;
    public BooleanProperty redoImpossible;

    public CommandManager() {
        executedCommands = new Stack<>();
        redoableCommands = new Stack<>();
        undoImpossible = new SimpleBooleanProperty(executedCommands.empty());
        redoImpossible = new SimpleBooleanProperty(redoableCommands.empty());
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
            redoableCommands.push(c);
        } else {
            executedCommands.push(c);
        }
        updateBindings();
    }

    public void redo() {
        Command c = redoableCommands.pop();
        if(c.redo()) {
            executedCommands.push(c);
        } else {
            redoableCommands.push(c);
        }
        updateBindings();
    }

    private void updateBindings() {
        undoImpossible.setValue(executedCommands.empty());
        redoImpossible.setValue(redoableCommands.empty());
    }

    public int getExecutedCommandsStackSize() {
        return executedCommands.size();
    }

    public int getRedoableCommandsStackSize () {
        return redoableCommands.size();
    }

    public Stack<Command> getExecutedCommands() {
        return executedCommands;
    }

    public Stack<Command> getRedoableCommands() {
        return redoableCommands;
    }
}
