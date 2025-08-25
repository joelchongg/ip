package chatterbox.command;

import chatterbox.memory.Storage;
import chatterbox.task.Task;
import chatterbox.task.TodoTask;
import chatterbox.testutils.OutputCaptor;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandProcessorTest {
    @Test
    public void isCommand_todo_returnTrue() {
        assertTrue(CommandProcessor.isCommand("todo"));
    }

    @Test 
    public void isNotCommand_foobar_returnFalse() {
        assertFalse(CommandProcessor.isCommand("foobar"));
    }

    @Test
    public void processCommand_addTodo_addsTaskToStorage() {
        Storage<Task> storage = new Storage<>();
        String userInput = "Read book\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

        OutputCaptor captor = new OutputCaptor();
        String output = captor.capture(() -> 
                CommandProcessor.processCommand(storage, scanner, "todo"));


        assertEquals(1, storage.size());
        Task task = storage.getItem(0);
        assertTrue(task instanceof TodoTask);
        assertEquals("Read book", task.getTaskDescription());
        assertTrue(output.contains("Got it. I've added this task:"));
        assertTrue(output.contains("Read book"));
    }

    @Test
    public void processCommand_emptyTodoDescription_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner(new ByteArrayInputStream("\n".getBytes()));
        OutputCaptor captor = new OutputCaptor();

        String output = captor.capture(() ->
                CommandProcessor.processCommand(storage, scanner, "todo"));
        
        assertEquals(0, storage.size());
        assertEquals("ChatterBox: Uh oh! You forgot to include a description for your todo task! Try again!", 
                      output.trim());
    }

    @Test
    public void processCommand_invalidCommand_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner(new ByteArrayInputStream("\n".getBytes()));

        OutputCaptor captor = new OutputCaptor();
        String output = captor.capture(() ->
                CommandProcessor.processCommand(storage, scanner, "foobar"));
        
        assertEquals("ChatterBox: Invalid Command!", output.trim());
    }
}
