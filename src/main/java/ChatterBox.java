import java.util.Scanner;

public class ChatterBox {

    private static String name = "\nChatterBox: ";
    private static Storage<String> storage = new Storage<>();

    private static void greetUser() {
        String startPhrase = name + "Hello! I'm ChatterBox\nChatterBox: What can I do for you?";
        System.out.println(startPhrase);
    }

    private static void exit() {
        String endPhrase = name + "Bye. Hope to see you again soon!";
        System.out.println(endPhrase);
    }

    private static void processUserInput(String userInput) {
        System.out.print(name);
        if (userInput.equals("list")) {
            System.out.println();
            storage.displayItems();
        } else {
            storage.addItem(userInput);
            System.out.println("added: " + userInput);
        }
    }

    private static void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                break;
            }

            processUserInput(userInput);
        }

        scanner.close();
    }

    public static void main(String[] args) {
        greetUser();

        run();

        exit();
    }
}
