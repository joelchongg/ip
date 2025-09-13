package chatterbox.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import chatterbox.memory.Storage;
import chatterbox.task.Task;
import chatterbox.task.TodoTask;

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
        Scanner scanner = new Scanner(userInput);

        String response = CommandProcessor.processCommand(storage, scanner, "todo");


        assertEquals(1, storage.size());
        Task task = storage.getItem(0);
        assertTrue(task instanceof TodoTask);
        assertEquals("Read book", task.getTaskDescription());
        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("Read book"));
    }

    @Test
    public void processCommand_emptyTodoDescription_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("\n");

        String response = CommandProcessor.processCommand(storage, scanner, "todo");

        assertEquals(0, storage.size());
        assertEquals("Uh oh! You forgot to include a description for your todo task! Try again!",
                response.trim());
    }

    @Test
    public void processCommand_invalidCommand_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("\n");

        String response = CommandProcessor.processCommand(storage, scanner, "foobar");

        assertEquals("Invalid Command!", response.trim());
    }

    @Test
    public void processCommand_emptyMarkIndex_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("\n");

        String response = CommandProcessor.processCommand(storage, scanner, "mark");

        assertEquals("Uh oh! You forgot to input an index! Try: mark <index>",
                response.trim());
    }

    @Test
    public void processCommand_emptyUnmarkIndex_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("\n");

        String response = CommandProcessor.processCommand(storage, scanner, "unmark");

        assertEquals("Uh oh! You forgot to input an index! Try: unmark <index>",
                response.trim());
    }

    @Test
    public void processCommand_emptyDeadlineDescription_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("\n");

        String response = CommandProcessor.processCommand(storage, scanner, "deadline");

        assertEquals("Uh oh! You forgot to include a description for your deadline task! Try again!",
                response.trim());
    }

    @Test
    public void processCommand_emptyEventDescription_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("\n");

        String response = CommandProcessor.processCommand(storage, scanner, "event");

        assertEquals("Uh oh! You forgot to include a description for your event task! Try again!",
                response.trim());
    }

    @Test
    public void processCommand_emptyDeleteIndex_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("\n");

        String response = CommandProcessor.processCommand(storage, scanner, "delete");

        assertEquals("Uh oh! You forgot to input an index! Try: delete <index>",
                response.trim());
    }

    @Test
    public void processCommand_emptyFindDescription_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("\n");

        String response = CommandProcessor.processCommand(storage, scanner, "find");

        assertEquals("Uh oh! You forgot to include a description to search for! Try Again!",
                response.trim());
    }

    @Test
    public void processCommand_addDuplicateTask_displayErrorMessage() {
        Storage<Task> storage = new Storage<>();
        Scanner scanner = new Scanner("task\n");

        storage.addItem(new TodoTask("task"));

        String response = CommandProcessor.processCommand(storage, scanner, "todo");

        assertEquals("You have already added this task!",
                response.trim());
        assertTrue(storage.size() == 1);
    }
}
