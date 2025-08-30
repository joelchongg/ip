package chatterbox.task;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task in the ChatterBox application.
 * 
 * <p>A {@code DeadlineTask} is a specific type of {@link Task}
 * that has a deadline. It uses the symbol 'D' to denote its type
 * and inherits common task behavior such as completion status and
 * description. The deadline is formatted to show LocalDateTime when
 * printed.
 */
public class DeadlineTask extends Task {
    
    private static char symbol = 'D';
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private LocalDateTime deadline;

    public DeadlineTask(String description, String deadline) throws DateTimeException {
        super(description, symbol);
        this.deadline = LocalDateTime.parse(deadline, formatter);
    }

    public DeadlineTask(String description, String deadline, boolean isCompleted) throws DateTimeException {
        super(description, symbol, isCompleted);
        this.deadline = LocalDateTime.parse(deadline, formatter);
    }

    public String serializeDeadline() {
        return this.deadline.format(formatter);
    }

    public String getFormattedDeadline() {
        return this.deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), getFormattedDeadline());
    }
}
