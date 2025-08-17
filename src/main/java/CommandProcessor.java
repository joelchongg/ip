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
        //TODO
    }

    private static <T extends Task> void addDeadline(Storage<T> storage, Scanner scanner) {
        //TODO
    }
    
    private static <T extends Task> void addEvent(Storage<T> storage, Scanner scanner) {
        //TODO
    }
}
