public class EventTask extends Task {
    
    private static char symbol = 'E';
    private String startTime;
    private String endTime;

    public EventTask(String description, String startTime, String endTime) {
        super(description, symbol);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)", 
        super.toString(), startTime, endTime);
    }
}
