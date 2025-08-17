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
    }

    public static <T extends Task> void processCommand(Storage<T> storage, Scanner scanner, String command) {
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

    private static <T extends Task> void list(Storage<T> storage, Scanner scanner) {
        System.out.println(name + "Here are the tasks in your list:");
        storage.displayItems();
    }

    private static <T extends Task> void mark(Storage<T> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            T item = storage.getItem(index - 1);
            item.setCompleted();
            System.out.println(name + "Nice! I've marked this task as done:");
            System.out.println(item);
        } catch (InputMismatchException e) {
            System.out.println(name + "Invalid input for mark!");
        }
    }

    private static <T extends Task> void unmark(Storage<T> storage, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            T item = storage.getItem(index - 1);
            item.setIncomplete();
            System.out.println(name + "OK, I've marked this task as not done yet:");
            System.out.println(item);
        } catch (InputMismatchException e) {
            System.out.println(name + "Invalid input for unmark!");
        }
    }
}
