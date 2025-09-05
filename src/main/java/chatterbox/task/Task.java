package chatterbox.task;

/**
 * Represents an abstract task in the ChatterBox application.
 *
 * <p>A task has a description, a completion status, and a symbol
 * identifying its type. Subclasses define specific types of tasks
 * (e.g deadline, event, todo) by extending this class
 * and providing additional behaviour or attributes
 */
public abstract class Task {
    private String description;
    private boolean isCompleted;
    private char symbol;

    /**
     * Creates a new Task with the specified description and symbol.
     *
     * The task is initially incomplete.
     *
     * @param description the description of the task.
     * @param symbol the character representing the task type.
     */
    public Task(String description, char symbol) {
        assert symbol == 'T' || symbol == 'D' || symbol == 'E' : "Invalid task symbol";

        this.description = description;
        this.isCompleted = false;
        this.symbol = symbol;
    }

    /**
     * Creates a new Task with the specified description, symbol
     * and completion status.
     *
     * @param description the description of the task.
     * @param symbol the character representing the task type.
     * @param isCompleted whether the task is initially completed.
     */
    public Task(String description, char symbol, boolean isCompleted) {
        this(description, symbol);
        this.isCompleted = isCompleted;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public void setCompleted() {
        this.isCompleted = true;
    }

    public void setIncomplete() {
        this.isCompleted = false;
    }

    public String getTaskDescription() {
        return this.description;
    }

    public String getStatusIcon() {
        return (this.isCompleted ? "X" : " ");
    }

    public char getTaskSymbol() {
        return this.symbol;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        
        return this.description.equals(otherTask.description)
                && this.symbol == otherTask.symbol;
    }

    @Override
    public String toString() {
        String status = "[" + getTaskSymbol() + "] [" + getStatusIcon() + "]";
        return status + " " + this.description;
    }
}
