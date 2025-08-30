package chatterbox.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class TaskTest {
    @Test
    public void defaultCompletionStatus_newTask_isFalse() {
        Task task = new TodoTask("default task");
        assertFalse(task.isCompleted());
    }

    @Test
    public void toString_defaultTask_returnsCorrectFormat() {
        Task task = new TodoTask("default task");
        assertEquals("[T] [ ] default task", task.toString());
    }
}
