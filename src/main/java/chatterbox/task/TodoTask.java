package chatterbox.task;

/**
 * Represents a to-do task in the ChatterBox application.
 * 
 * <p>A {@code TodoTask} is a simple task without any start/end time.
 * It uses the symbol 'T' to denote its type and inherits common 
 * behavior from {@link Task}, such as description and completion status.
 */
public class TodoTask extends Task {

    private static char symbol = 'T';

    public TodoTask(String description) {
        super(description, symbol);
    }

    public TodoTask(String description, boolean isCompleted) {
        super(description, symbol, isCompleted);
    }
}
