import java.util.Scanner;

public class ChatterBox {

    private static String name = "\nChatterBox: ";
    private static Storage<Task> storage = new Storage<>();

    private static void greetUser() {
        String startPhrase = name + "Hello! I'm ChatterBox." + name + "What can I do for you?";
        System.out.println(startPhrase);
    }

    private static void exit() {
        String endPhrase = name + "Bye. Hope to see you again soon!";
        System.out.println(endPhrase);
    }

    private static void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter Input: ");
            String userInput = scanner.next();

            if (userInput.equals("bye")) {
                break;
            }

            // Current code for adding items to storage. Modify eventually when "add" command exists
            if (!CommandProcessor.isCommand(userInput)) {
                userInput += scanner.nextLine();        
                Task newTask = new Task(userInput);
                storage.addItem(newTask);
                System.out.println(name + "added: " + userInput);
                continue;
            }

            CommandProcessor.processCommand(storage, scanner, userInput);
        }

        scanner.close();
    }

    public static void main(String[] args) {
        greetUser();

        run();

        exit();
    }
}
