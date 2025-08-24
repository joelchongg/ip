package chatterbox.task;

public class TodoTask extends Task {

    private static char symbol = 'T';

    public TodoTask(String description) {
        super(description, symbol);
    }

    public TodoTask(String description, boolean isCompleted) {
        super(description, symbol, isCompleted);
    }
}
