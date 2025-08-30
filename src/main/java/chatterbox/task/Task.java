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

    public Task(String description, char symbol) {
        this.description = description;
        this.isCompleted = false;
        this.symbol = symbol;
    }

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
    public String toString() {
        String status = "[" + getTaskSymbol() + "] [" + getStatusIcon() + "]";
        return status + " " + this.description;
    }
}
