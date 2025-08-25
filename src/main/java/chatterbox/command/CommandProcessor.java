package chatterbox.command;

import chatterbox.exception.ChatterBoxException;
import chatterbox.memory.Storage;
import chatterbox.memory.MemoryStorage;
import chatterbox.task.*;
import chatterbox.ui.ChatterBoxUI;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

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
     */
    public static void processCommand(Storage<Task> storage, Scanner scanner, String command) {
        if (isCommand(command)) {
            Runnable cmd = commands.get(command);
            cmd.run(storage, scanner);
        } else {
            ChatterBoxUI.reply("Invalid Command!");
        }
    }

    /**
     * Returns a boolean depending if command is valid.
     * 
     * @param command String for a command.
     * @return boolean
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
     */
    private static void list(Storage<Task> storage, Scanner scanner) {
        ChatterBoxUI.reply("Here are the tasks in your list:");
        storage.displayItems();
        scanner.nextLine();
    }

    /**
     * Takes in input from command line interface and 
     * marks the task at the corresponding index from input.
     * Subsequent input should be an integer denoting the index of the task
     * in the storage object.
     * 
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     */
    private static void mark(Storage<Task> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            Task item = storage.getItem(index - 1);

            item.setCompleted();
            MemoryStorage.updateTaskCompletion(index - 1, true);

            ChatterBoxUI.reply("Nice! I've marked this task as done:");
            System.out.println(item);
        } catch (InputMismatchException e) {
            ChatterBoxUI.reply("Invalid Input! Try: mark <index>");
        } catch (IndexOutOfBoundsException e) {
            ChatterBoxUI.reply("Invalid index! You can only mark tasks between 1 and " + storage.size() + ".");
        } finally {
            scanner.nextLine();
        }
    }

    /**
     * Takes in input from command line interface and
     * marks the task at the corresponding index from input.
     * Subsequent input should be an integer denoting the index of the task
     * in the storage object.
     * 
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     */
    private static void unmark(Storage<Task> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            Task item = storage.getItem(index - 1);

            item.setIncomplete();
            MemoryStorage.updateTaskCompletion(index - 1, false);

            ChatterBoxUI.reply("OK, I've marked this task as not done yet:");
            System.out.println(item);
        } catch (InputMismatchException e) {
            ChatterBoxUI.reply("Invalid Input! Try: unmark <index>");
        } catch (IndexOutOfBoundsException e) {
            ChatterBoxUI.reply("Invalid Index! You can only unmark tasks between 1 and " + storage.size() + ".");
        } finally {
            scanner.nextLine();
        }
    }

    /**
     * Creates and adds a todo Task object into the storage.
     * Description for the task object should be inputted after the 'todo' command.
     * Input Format: todo <description>
     * 
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     */
    private static void addTodo(Storage<Task> storage, Scanner scanner) {
        try {
            String token = scanner.nextLine().trim();

            if (token.isEmpty()) {
                throw new ChatterBoxException(
                    "Uh oh! You forgot to include a description for your todo task! Try again!"
                );
            }

            Task newTask = new TodoTask(token);
            addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            ChatterBoxUI.reply(e.getMessage());
        }
    }

    /**
     * Creates and adds a deadline Task object into the storage.
     * Description for the task object should be inputted after the 'deadline' command.
     * Input Format: deadline <description> /by <LocalDateTime>
     * LocalDateTime format should follow: dd-mm-yyyy HH:mm
     * 
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     */
    private static void addDeadline(Storage<Task> storage, Scanner scanner) {
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
            addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            ChatterBoxUI.reply(e.getMessage());
        } catch(DateTimeException e) {
            ChatterBoxUI.reply("Oops! Your deadline format is incorrect! It should be \"dd-mm-yyyy HH:mm\". Try Again!");
        }
    }
    
    /**
     * Creates and adds a event Task object into the storage.
     * Description for the task object should be inputted after the 'event' command.
     * Input Format: event <description> from: <time> to: <time>
     * 
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     */
    private static void addEvent(Storage<Task> storage, Scanner scanner) {
        try {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                throw new ChatterBoxException("Uh oh! You forgot to include a description for your event task! Try again!");
            }

            String[] tokens = parseInput(input, " /from ", " /to ");

            if (tokens.length != 3) {
                throw new ChatterBoxException(
                    "Uh oh! You did not input your event task correctly! Try: event <description> /from <time> /to <time>"
                );
            }

            Task newTask = new EventTask(tokens[0], tokens[1], tokens[2]);
            addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            ChatterBoxUI.reply(e.getMessage());
        }
    }

    /**
     * Deletes a task from the storage
     * The task deleted corresponds to the index inputted after the 'delete' command.
     * Input Format: delete <index>
     * 
     * @param storage Storage object in which Task objects are stored.
     * @param scanner Scanner used to read input from command line interface.
     */
    private static void delete(Storage<Task> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            Task deleted = storage.removeItem(index - 1);
            MemoryStorage.deleteTask(index - 1);

            ChatterBoxUI.reply("Noted. I've removed this task:");
            System.out.println(deleted);
            ChatterBoxUI.reply("Now you have " + storage.size() + " tasks in the list.");
        } catch (InputMismatchException e) {
            ChatterBoxUI.reply("Invalid Input! Try: delete <index>");
        } catch (IndexOutOfBoundsException e) {
            ChatterBoxUI.reply("Invalid index! You can only delete tasks between 1 and " + storage.size() + ".");
        } finally {
            scanner.nextLine();
        }
    }

    private static void find(Storage<Task> storage, Scanner scanner) {
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) {
            ChatterBoxUI.reply("Uh oh! You forgot to include a description to search for! Try Again!");
            return;
        }
        
        ArrayList<Task> tasks = storage.searchTasksByDescription(input);
        
        if (tasks.isEmpty()) {
            ChatterBoxUI.reply("There are no items in your list with that description.");
            return;
        }
        
        ChatterBoxUI.reply("Here are the matching tasks in your list:");
        
        for (int index = 1; index <= tasks.size(); ++index) {
            System.out.println(index + "." + tasks.get(index - 1));
        }
        System.out.println();
    }
    
    /**
     * Returns a String[] that contains the parsed input based on the delimiters given.
     * Multiple delimiters can be used to parse an input.
     * 
     * @param input String to be parsed.
     * @param delimiters One or more delimiters to be used to parse the input.
     * @return String[] containing the tokens of the input.
     * @throws ChatterBoxException If delimiter does not exist within the input string.
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
     */
    private static void addTask(Storage<Task> storage, Task task) {
        storage.addItem(task);
        MemoryStorage.saveTask(task);

        ChatterBoxUI.reply("Got it. I've added this task:");
        System.out.println(task);
        ChatterBoxUI.reply("You now have " + storage.size() + " tasks in the list.");
    }
}
