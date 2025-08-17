public class Task {
    private String description;
    private boolean completed;

    public Task(String description) {
        this.description = description;
        this.completed = false;
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

    @Override
    public String toString() {
        String completionStatus = "[" + getStatusIcon() + "]"; 
        return completionStatus + " " + this.description;
    }
}
