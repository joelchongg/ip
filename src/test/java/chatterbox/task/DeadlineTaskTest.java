package chatterbox.task;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;

import org.junit.jupiter.api.Test;

public class DeadlineTaskTest {
    @Test
    public void constructor_invalidDateTimeArgument_throwsException() {
        String invalidDateTimeString = "18 Aug 2025 6pm";
        assertThrows(DateTimeException.class, () -> new DeadlineTask("deadline task", invalidDateTimeString));
    }

    @Test
    public void constructor_validDateTimeArgument_noExceptionThrown() {
        String validDateTimeString = "18-12-2025 18:00";
        assertDoesNotThrow(() -> new DeadlineTask("deadline task", validDateTimeString));
    }

    @Test
    public void serializeDeadline_validDeadline_returnsSameFormatAsInput() {
        DeadlineTask task = new DeadlineTask("deadline task", "25-08-2025 16:30");
        assertEquals("25-08-2025 16:30", task.serializeDeadline());
    }

    @Test
    public void getFormattedDeadline_validDeadline_returnsReadableFormat() {
        DeadlineTask task = new DeadlineTask("deadline task", "25-08-2025 16:30");
        assertEquals("Aug 25 2025 16:30", task.getFormattedDeadline());
    }

    @Test
    void toString_incompleteTask_returnsCorrectString() {
        DeadlineTask task = new DeadlineTask("Do project", "25-08-2025 16:30");
        String expected = "[D] [ ] Do project (by: Aug 25 2025 16:30)";
        assertEquals(expected, task.toString());
    }

    @Test
    void toString_completedTask_returnsCorrectString() {
        DeadlineTask task = new DeadlineTask("Do project", "25-08-2025 16:30", true);
        String expected = "[D] [X] Do project (by: Aug 25 2025 16:30)";
        assertEquals(expected, task.toString());
    }
}
