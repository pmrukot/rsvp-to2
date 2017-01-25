package rsvp.user.command;

public interface Command {
    boolean execute();
    boolean undo();
    boolean redo();
}
