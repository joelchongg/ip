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

    /**
     * Creates a new {@code DeadlineTask} with the specified description
     * and deadline.
     * The task is initially incomplete.
     *
     * @param description the description of the task
     * @param deadline the deadline in {@code "dd-MM-yyyy HH:mm"} format
     * @throws DateTimeException if the deadline cannot be parsed
     */
    public DeadlineTask(String description, String deadline) throws DateTimeException {
        super(description, symbol);
        this.deadline = LocalDateTime.parse(deadline, formatter);
    }

    /**
     * Creates a new {@code DeadlineTask} with the specified description,
     * deadline and completion status.
     *
     * @param description the description of the task
     * @param deadline the deadline in {@code "dd-MM-yyyy HH:mm"} format
     * @param isCompleted whether the task is initially completed
     * @throws DateTimeException if the deadline cannot be parsed
     */
    public DeadlineTask(String description, String deadline, boolean isCompleted) throws DateTimeException {
        super(description, symbol, isCompleted);
        this.deadline = LocalDateTime.parse(deadline, formatter);
    }

    /**
     * Serializes the {@code DeadlineTask} for storage in persistent memory.
     * @return
     */
    public String serializeDeadline() {
        return this.deadline.format(formatter);
    }

    /**
     * Returns {@code DeadlineTask} in a specific format.
     * The format returned is of "MMM d yyyy HH:mm"/
     *
     * @return formatted String of {@code DeadlineTask}
     */
    public String getFormattedDeadline() {
        return this.deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), getFormattedDeadline());
    }
}
