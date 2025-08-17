import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CommandProcessor {
    private static final HashMap<String, Runnable> commands = new HashMap<>();
    private static String name = "\nChatterBox: ";

    static {
        commands.put("list", CommandProcessor::list);
        commands.put("mark", CommandProcessor::mark);
        commands.put("unmark", CommandProcessor::unmark);
        commands.put("todo", CommandProcessor::addTodo);
        commands.put("deadline", CommandProcessor::addDeadline);
        commands.put("event", CommandProcessor::addEvent);
    }

    public static void processCommand(Storage<Task> storage, Scanner scanner, String command) {
        if (isCommand(command)) {
            Runnable cmd = commands.get(command);
            cmd.run(storage, scanner);
        } else {
            System.out.println(name + "Invalid Command!");
        }
    }

    public static boolean isCommand(String command) {
        return commands.containsKey(command);
    }

    private static void list(Storage<Task> storage, Scanner scanner) {
        System.out.println(name + "Here are the tasks in your list:");
        storage.displayItems();
    }

    private static void mark(Storage<Task> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            Task item = storage.getItem(index - 1);
            item.setCompleted();
            System.out.println(name + "Nice! I've marked this task as done:");
            System.out.println(item);
        } catch (InputMismatchException e) {
            System.out.println(name + "Invalid input for mark!");
        }
    }

    private static void unmark(Storage<Task> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            Task item = storage.getItem(index - 1);
            item.setIncomplete();
            System.out.println(name + "OK, I've marked this task as not done yet:");
            System.out.println(item);
        } catch (InputMismatchException e) {
            System.out.println(name + "Invalid input for unmark!");
        }
    }

    private static void addTodo(Storage<Task> storage, Scanner scanner) {
        String token = scanner.nextLine().trim();
        Task newTask = new TodoTask(token);
        addTask(storage, newTask);
    }

    private static void addDeadline(Storage<Task> storage, Scanner scanner) {
        String input = scanner.nextLine().trim();
        String[] tokens = parseInput(input, " /by ");

        Task newTask = new DeadlineTask(tokens[0], tokens[1]);
        addTask(storage, newTask);
    }
    
    private static void addEvent(Storage<Task> storage, Scanner scanner) {
        String input = scanner.nextLine().trim();
        String[] tokens = parseInput(input, " /from ", " /to ");

        Task newTask = new EventTask(tokens[0], tokens[0], tokens[1]);
        addTask(storage, newTask);
    }

    private static String[] parseInput(String input, String... delimiters) {
        String[] parts = new String[delimiters.length + 1];
        int lastIndex = 0;
        for (int i = 0; i < delimiters.length; ++i) {
            int idx = input.indexOf(delimiters[i], lastIndex);
            if (idx == -1) {
                throw new IllegalArgumentException("Missing Delimiter: " + delimiters[i]);
            }

            parts[i] = input.substring(lastIndex, idx).trim();
            lastIndex = idx + delimiters[i].length();
        }
        parts[delimiters.length] = input.substring(lastIndex).trim();

        return parts;
    }

    private static void addTask(Storage<Task> storage, Task task) {
        storage.addItem(task);
        System.out.println(name + "Got it. I've added this task:");
        System.out.println(task);
        System.out.println(name + "You now have " + storage.size() + " tasks in the list.");
    }
}
