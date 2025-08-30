package chatterbox.task;

/**
 * Represents an event task in the ChatterBox application.
 *
 * <p>An {@code EventTask} is a specific type of {@link Task} that
 * has a start time and an end time. It uses the symbol 'E' to
 * denote its type and inherits common task behavior such as
 * completion status and description.
 */
public class EventTask extends Task {

    private static char symbol = 'E';
    private String startTime;
    private String endTime;

    /**
     * Creates a new {@code EventTask} with the specified description,
     * start time and end time.
     *
     * The task is initially incomplete.
     *
     * @param description the description of the task
     * @param startTime the start time of the event
     * @param endTime the end time of the event
     */
    public EventTask(String description, String startTime, String endTime) {
        super(description, symbol);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Creates a new {@code EventTask} with the specified description,
     * start time, end time and completion status.
     *
     * The task is initially incomplete
     *
     * @param description the description of the task
     * @param startTime the start time of the event
     * @param endTime the end time of the event
     * @param isCompleted whether the task is initially completed
     */
    public EventTask(String description, String startTime, String endTime, boolean isCompleted) {
        super(description, symbol, isCompleted);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)",
        super.toString(), startTime, endTime);
    }
}
