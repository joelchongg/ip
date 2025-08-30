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

    /**
     * Creates a new {@code TodoTask} with the specifed description.
     * The task is initially incomplete.
     *
     * @param description the description of the task
     */
    public TodoTask(String description) {
        super(description, symbol);
    }

    /**
     * Creates a new {@code TodoTask} with the specified description
     * and completion status.
     *
     * @param description the description of the task
     * @param isCompleted whether the task is initially completed
     */
    public TodoTask(String description, boolean isCompleted) {
        super(description, symbol, isCompleted);
    }
}
