/*
package rsvp.user.command;

import java.util.ArrayList;
import java.util.List;

public class CommandInvoker {
    private List<Command> commandList;
    private List<Command> executedCommands;

    public CommandInvoker() {
        commandList = new ArrayList<>();
        executedCommands = new ArrayList<>();
    }

    public void addCommand(Command c) {
        commandList.add(c);
    }

    public boolean executeCommand(Command c) {
        if(commandList.contains(c)) {
            commandList.remove(c);
        }
        executedCommands.add(c);
        return c.execute();
    }

    public boolean undoCommand(Command c) {
        if(executedCommands.contains(c)) {
            executedCommands.remove(c);
        }
        commandList.add(c);
        return c.undo();
    }

    public void executeCommands() {
        for(Command c : commandList) {
            if(c.execute()) {
                commandList.remove(c);
                executedCommands.add(c);
            }
        }
    }

    public void undoCommands() {
        for(Command c : executedCommands) {
            if(c.undo()) {
                executedCommands.remove(c);
                commandList.add(c);
            }
        }
    }
}
*/
