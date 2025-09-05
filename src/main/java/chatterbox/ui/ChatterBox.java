package chatterbox.ui;

import java.util.Scanner;

import chatterbox.command.CommandProcessor;
import chatterbox.memory.MemoryStorage;
import chatterbox.memory.Storage;
import chatterbox.task.Task;

/**
 * Main entry point for the ChatterBox application.
 */
public class ChatterBox {

    private Storage<Task> storage = new Storage<>();

    /**
     * Process a single line of user input.
     *
     * <p>The method * The method validates whether the input corresponds to a supported command.
     * If valid, the command is executed and its result returned.
     * Otherwise, an error message is returned.
     *
     * @param input the raw input string from the user
     * @return the response message after executing the command,
     *         or an error message if the input is invalid
     */
    public String run(String input) {
        Scanner scanner = new Scanner(input);

        if (!scanner.hasNext()) {
            return "You did not input any command! Try again!";
        }

        String userInput = scanner.next();

        if (!CommandProcessor.isCommand(userInput)) {
            return "Invalid command! Try Again!";
        }

        String result = CommandProcessor.processCommand(storage, scanner, userInput);
        return result;
    }

    /**
     * Initializes {@code Task} objects from memory to virtual memory.
     */
    public void initialize() {
        assert storage != null : "Storage should not be null";

        MemoryStorage.loadTasks(storage);
    }

    /**
     * Starts the ChatterBox application.
     *
     * Currently not in use due to switching to GUI.
     *
     * This method initializes stored data, if any, displays a greeting message,
     * enters the main input loop, and ends with a farewell.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        /*
        initialize();
        ChatterBoxUI.greet();
        run();
        ChatterBoxUI.farewell();
        */
    }
}
