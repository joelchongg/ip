public class DeadlineTask extends Task {
    
    private static char symbol = 'D';

    public DeadlineTask(String description) {
        super(description, symbol);
    }
}
