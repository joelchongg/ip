public abstract class Task {
    private String description;
    private boolean completed;
    private char symbol;

    public Task(String description, char symbol) {
        this.description = description;
        this.completed = false;
        this.symbol = symbol;
    }

    public boolean isCompleted() {
        return this.completed;
    }
    
    public void setCompleted() {
        this.completed = true;
    }

    public void setIncomplete() {
        this.completed = false;
    }

    public String getTaskDescription() {
        return this.description;
    }

    public String getStatusIcon() {
        return (this.completed ? "X" : " ");
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
