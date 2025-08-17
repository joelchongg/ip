public class DeadlineTask extends Task {
    
    private static char symbol = 'D';
    private String deadline;

    public DeadlineTask(String description, String deadline) {
        super(description, symbol);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), deadline);
    }
}
