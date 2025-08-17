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
        String description = scanner.nextLine().trim();
        Task newTask = new TodoTask(description);

        storage.addItem(newTask);
        System.out.println(name + "Got it. I've added this task:");
        System.out.println(newTask);
        System.out.println(name + "You now have " + storage.size() + " tasks in the list.");
    }

    private static void addDeadline(Storage<Task> storage, Scanner scanner) {
        String input = scanner.nextLine().trim();       //TODO
        String[] description = input.split(" /by ");    //TODO find a better way
        System.out.println(input);
        Task newTask = new DeadlineTask(description[0], description[1]);

        storage.addItem(newTask);
        System.out.println(name + "Got it. I've added this task:");
        System.out.println(newTask);
        System.out.println(name + "You now have " + storage.size() + " tasks in the list.");
    }
    
    private static void addEvent(Storage<Task> storage, Scanner scanner) {
        String input = scanner.nextLine().trim();       //TODO find a better way
        String[] description = input.split(" /from ");       //TODO
        String fromAndTo = description[1];
        String[] time = fromAndTo.split(" /to ");


        Task newTask = new EventTask(description[0], time[0], time[1]);

        storage.addItem(newTask);
        System.out.println(name + "Got it. I've added this task:");
        System.out.println(newTask);
        System.out.println(name + "You now have " + storage.size() + " tasks in the list.");
    }
}
