package chatterbox.command;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import chatterbox.exception.ChatterBoxException;
import chatterbox.memory.MemoryStorage;
import chatterbox.memory.Storage;
import chatterbox.task.DeadlineTask;
import chatterbox.task.EventTask;
import chatterbox.task.Task;
import chatterbox.task.TodoTask;

/**
 * Processes and executes user commands in the ChatterBox application.
 *
 * <p>The {@code CommandProcessor} class maps user-entered command strings
 * to specific actions, such as adding, marking, unmarking, deleting, and
 * searching tasks. Commands are validated and executed using the appropriate
 * {@link Runnable} implementation. All task modifications are reflected in
 * both memory and persistent storage.
 *
 * <p>Common commands include {@code list}, {@code mark}, {@code unmark},
 * {@code todo}, {@code deadline}, {@code event}, {@code delete}, and {@code find}.
 */
public class CommandProcessor {
    private static final HashMap<String, Runnable> commands = new HashMap<>();

    static {
        commands.put("list", CommandProcessor::list);
        commands.put("mark", CommandProcessor::mark);
        commands.put("unmark", CommandProcessor::unmark);
        commands.put("todo", CommandProcessor::addTodo);
        commands.put("deadline", CommandProcessor::addDeadline);
        commands.put("event", CommandProcessor::addEvent);
        commands.put("delete", CommandProcessor::delete);
        commands.put("find", CommandProcessor::find);
    }

    /**
     * Processes the command given in the parameter.
     * If the command is invalid, no changes are made and "Invalid Command" is seen in output.
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     * @param command Command to be processed.
     * @return Returns a string to be shown in response to user input
     */
    public static String processCommand(Storage<Task> storage, Scanner scanner, String command) {
        if (!isCommand(command)) {
            return "Invalid Command!";
        }

        Runnable cmd = commands.get(command);
        String response = cmd.run(storage, scanner);

        return response;
    }

    /**
     * Returns a boolean depending if command is valid.
     *
     * @param command String for a command.
     * @return boolean indicating whether the given input is a command
     */
    public static boolean isCommand(String command) {
        return commands.containsKey(command);
    }

    /**
     * Outputs to command line interface tasks in the storage in a numbered list.
     * If storage is empty, no output is produced.
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     * @return Returns a string to be shown in response to user input
     */
    private static String list(Storage<Task> storage, Scanner scanner) {
        String response = "Here are the tasks in your list:\n";
        response += storage.displayItems();

        return response;
    }

    /**
     * Takes in input from command line interface and
     * marks the task at the corresponding index from input.
     * Subsequent input should be an integer denoting the index of the task
     * in the storage object.
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     * @return Returns a string to be shown in response to user input
     */
    private static String mark(Storage<Task> storage, Scanner scanner) {
        String response = "";

        try {
            int index = scanner.nextInt();
            Task item = storage.getItem(index - 1);

            item.setCompleted();
            MemoryStorage.updateTaskCompletion(index - 1, true);

            response += "Nice! I've marked this task as done:\n";
            response += item;
        } catch (InputMismatchException e) {
            response = "Invalid Input! Try: mark <index>\n";
        } catch (IndexOutOfBoundsException e) {
            response = "Invalid index! You can only mark tasks between 1 and " + storage.size() + ".\n";
        } catch (NoSuchElementException e) {
            response = "Uh oh! You forgot to input an index! Try: mark <index>\n";
        }

        return response;
    }

    /**
     * Takes in input from command line interface and
     * marks the task at the corresponding index from input.
     * Subsequent input should be an integer denoting the index of the task
     * in the storage object.
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     * @return Returns a string to be shown in response to user input
     */
    private static String unmark(Storage<Task> storage, Scanner scanner) {
        String response = "";

        try {
            int index = scanner.nextInt();
            Task item = storage.getItem(index - 1);

            item.setIncomplete();
            MemoryStorage.updateTaskCompletion(index - 1, false);

            response += "OK, I've marked this task as not done yet:\n";
            response += item;
        } catch (InputMismatchException e) {
            response = "Invalid Input! Try: unmark <index>\n";
        } catch (IndexOutOfBoundsException e) {
            response = "Invalid Index! You can only unmark tasks between 1 and " + storage.size() + ".\n";
        } catch (NoSuchElementException e) {
            response = "Uh oh! You forgot to input an index! Try: unmark <index>\n";
        }

        return response;
    }

    /**
     * Creates and adds a todo Task object into the storage.
     * Description for the task object should be inputted after the 'todo' command.
     * <p>Input Format: {@code todo <description>}
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     * @return Returns a string to be shown in response to user input
     */
    private static String addTodo(Storage<Task> storage, Scanner scanner) {
        String response = "";

        try {
            String token = scanner.nextLine().trim();

            if (token.isEmpty()) {
                throw new ChatterBoxException(
                    "Uh oh! You forgot to include a description for your todo task! Try again!"
                );
            }

            Task newTask = new TodoTask(token);
            response = addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            response = e.getMessage();
        }

        return response;
    }

    /**
     * Creates and adds a deadline Task object into the storage.
     * Description for the task object should be inputted after the 'deadline' command.
     * <p>Input Format: {@code deadline <description> /by <LocalDateTime>}
     * LocalDateTime format should follow: dd-mm-yyyy HH:mm
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     * @return Returns a string to be shown in response to user input
     */
    private static String addDeadline(Storage<Task> storage, Scanner scanner) {
        String response = "";

        try {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                throw new ChatterBoxException(
                    "Uh oh! You forgot to include a description for your deadline task! Try again!"
                );
            }

            String[] tokens = parseInput(input, " /by ");

            if (tokens.length != 2) {
                throw new ChatterBoxException(
                    "Uh oh! You did not input your deadline task correctly! Try: deadline <description> /by <deadline>"
                );
            }

            Task newTask = new DeadlineTask(tokens[0], tokens[1]);
            response = addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            response = e.getMessage() + '\n';
        } catch (DateTimeException e) {
            response = "Oops! Your deadline format is incorrect! It should be \"dd-mm-yyyy HH:mm\". Try Again!\n";
        }

        return response;
    }

    /**
     * Creates and adds a event Task object into the storage.
     * Description for the task object should be inputted after the 'event' command.
     * <p>Input Format: {@code event <description> from: <time> to: <time>}
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     * @return Returns a string to be shown in response to user input
     */
    private static String addEvent(Storage<Task> storage, Scanner scanner) {
        String response = "";

        try {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                throw new ChatterBoxException(
                    "Uh oh! You forgot to include a description for your event task! Try again!"
                );
            }

            String[] tokens = parseInput(input, " /from ", " /to ");

            if (tokens.length != 3) {
                throw new ChatterBoxException(
                    "Uh oh! You did not input your event task correctly!"
                    + "Try: event <description> /from <time> /to <time>"
                );
            }

            Task newTask = new EventTask(tokens[0], tokens[1], tokens[2]);
            response = addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            response = e.getMessage() + '\n';
        }

        return response;
    }

    /**
     * Deletes a task from the storage
     * The task deleted corresponds to the index inputted after the 'delete' command.
     * <p>Input Format: {@code delete <index>}
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     * @return Returns a string to be shown in response to user input
     */
    private static String delete(Storage<Task> storage, Scanner scanner) {
        String response = "";

        try {
            int index = scanner.nextInt();
            Task deleted = storage.removeItem(index - 1);
            MemoryStorage.deleteTask(index - 1);

            response += ("Noted. I've removed this task:\n");
            response += deleted + "\n";
            response += "Now you have " + storage.size() + " tasks in the list.\n";
        } catch (InputMismatchException e) {
            response = "Invalid Input! Try: delete <index>\n";
        } catch (IndexOutOfBoundsException e) {
            response = "Invalid index! You can only delete tasks between 1 and " + storage.size() + ".\n";
        } catch (NoSuchElementException e) {
            response = "Uh oh! You forgot to input an index! Try: delete <index>\n";
        }

        return response;
    }

    /**
     * Finds tasks that contain a specific description and outputs it into the command line interface.
     * Description to search for should be inputted after the 'find' command.
     * <p> Input Format: {@code find <description>}
     *
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from the command line interface.
     * @return Returns a string to be shown in response to user input
     */
    private static String find(Storage<Task> storage, Scanner scanner) {
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return "Uh oh! You forgot to include a description to search for! Try Again!\n";
        }

        ArrayList<Task> tasks = storage.searchTasksByDescription(input);

        if (tasks.isEmpty()) {
            return "There are no items in your list with that description.\n";
        }

        String response = "Here are the matching tasks in your list:\n";

        for (int index = 1; index <= tasks.size(); ++index) {
            response += index + "." + tasks.get(index - 1) + '\n';
        }
        response += '\n';

        return response;
    }

    /**
     * Returns a String[] that contains the parsed input based on the delimiters given.
     * Multiple delimiters can be used to parse an input.
     *
     * @param input String to be parsed
     * @param delimiters One or more delimiters to be used to parse the input
     * @return String[] containing the tokens of the input
     * @throws ChatterBoxException If delimiter does not exist within the input string
     */
    private static String[] parseInput(String input, String... delimiters) throws ChatterBoxException {
        String[] parts = new String[delimiters.length + 1];
        int lastIndex = 0;
        for (int i = 0; i < delimiters.length; ++i) {
            int idx = input.indexOf(delimiters[i], lastIndex);
            if (idx == -1) {
                throw new ChatterBoxException("Uh oh! You forgot to include the delimiter: " + delimiters[i]);
            }

            parts[i] = input.substring(lastIndex, idx).trim();
            lastIndex = idx + delimiters[i].length();
        }
        parts[delimiters.length] = input.substring(lastIndex).trim();

        return parts;
    }

    /**
     * Adds the given task into the storage object.
     *
     * @param storage Storage object in which Task objects are stored in.
     * @param task Task object that is to be stored in the storage object.
     * @return Returns a string to be shown in response to user input
     */
    private static String addTask(Storage<Task> storage, Task task) {
        String response = "";

        storage.addItem(task);
        MemoryStorage.saveTask(task);

        response += "Got it. I've added this task:\n";
        response += task + "\n";
        response += "You now have " + storage.size() + " tasks in the list.\n";

        return response;
    }
}
