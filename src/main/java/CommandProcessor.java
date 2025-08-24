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
    }

    public static void processCommand(Storage<Task> storage, Scanner scanner, String command) {
        if (isCommand(command)) {
            Runnable cmd = commands.get(command);
            cmd.run(storage, scanner);
        } else {
            reply("Invalid Command!");
        }
    }

    public static boolean isCommand(String command) {
        return commands.containsKey(command);
    }

    private static void list(Storage<Task> storage, Scanner scanner) {
        reply("Here are the tasks in your list:");
        storage.displayItems();
        scanner.nextLine();
    }

    private static void mark(Storage<Task> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            Task item = storage.getItem(index - 1);

            item.setCompleted();
            MemoryStorage.updateTaskCompletion(index - 1, true);

            reply("Nice! I've marked this task as done:");
            System.out.println(item);
        } catch (InputMismatchException e) {
            reply("Invalid Input! Try: mark <index>");
        } catch (IndexOutOfBoundsException e) {
            reply("Invalid index! You can only mark tasks between 1 and " + storage.size() + ".");
        } finally {
            scanner.nextLine();
        }
    }

    private static void unmark(Storage<Task> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            Task item = storage.getItem(index - 1);

            item.setIncomplete();
            MemoryStorage.updateTaskCompletion(index - 1, false);

            reply("OK, I've marked this task as not done yet:");
            System.out.println(item);
        } catch (InputMismatchException e) {
            reply("Invalid Input! Try: unmark <index>");
        } catch (IndexOutOfBoundsException e) {
            reply("Invalid Index! You can only unmark tasks between 1 and " + storage.size() + ".");
        } finally {
            scanner.nextLine();
        }
    }

    private static void addTodo(Storage<Task> storage, Scanner scanner) {
        try {
            String token = scanner.nextLine().trim();

            if (token.isEmpty()) {
                throw new ChatterBoxException("Uh oh! You forgot to include a description for your todo task! Try again!");
            }

            Task newTask = new TodoTask(token);
            addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            reply(e.getMessage());
        }
    }

    private static void addDeadline(Storage<Task> storage, Scanner scanner) {
        try {
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                throw new ChatterBoxException("Uh oh! You forgot to include a description for your deadline task! Try again!");
            }
            
            String[] tokens = parseInput(input, " /by ");
            
            if (tokens.length != 2) {
                throw new ChatterBoxException("Uh oh! You did not input your deadline task correctly! Try: deadline <description> /by <deadline>");
            }
    
            Task newTask = new DeadlineTask(tokens[0], tokens[1]);
            addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            reply(e.getMessage());
        }
    }
    
    private static void addEvent(Storage<Task> storage, Scanner scanner) {
        try {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                throw new ChatterBoxException("Uh oh! You forgot to include a description for your event task! Try again!");
            }

            String[] tokens = parseInput(input, " /from ", " /to ");

            if (tokens.length != 3) {
                throw new ChatterBoxException("Uh oh! You did not input your event task correctly! Try: event <description> /from <time> /to <time>");
            }

            Task newTask = new EventTask(tokens[0], tokens[1], tokens[2]);
            addTask(storage, newTask);
        } catch (ChatterBoxException e) {
            reply(e.getMessage());
        }
    }

    private static void delete(Storage<Task> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            Task deleted = storage.removeItem(index - 1);
            MemoryStorage.deleteTask(index - 1);

            reply("Noted. I've removed this task:");
            System.out.println(deleted);
            reply("Now you have " + storage.size() + " tasks in the list.");
        } catch (InputMismatchException e) {
            reply("Invalid Input! Try: delete <index>");
        } catch (IndexOutOfBoundsException e) {
            reply("Invalid index! You can only delete tasks between 1 and " + storage.size() + ".");
        } finally {
            scanner.nextLine();
        }
    }

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

    private static void addTask(Storage<Task> storage, Task task) {
        storage.addItem(task);
        MemoryStorage.saveTask(task);

        reply("Got it. I've added this task:");
        System.out.println(task);
        reply("You now have " + storage.size() + " tasks in the list.");
    }

    private static void reply(String message) {
        System.out.println("\nChatterBox: " + message);
    }
}
