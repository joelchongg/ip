import java.util.Scanner;

public class ChatterBox {

    private static void greetUser() {
        String startPhrase = "\nChatterBox: Hello! I'm ChatterBox\nChatterBox: What can I do for you?";
        System.out.println(startPhrase);
    }

    private static void exit() {
        String endPhrase = "\nChatterBox: Bye. Hope to see you again soon!";
        System.out.println(endPhrase);
    }

    private static void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                break;
            }

            System.out.println("\nChatterBox: " + command);
        }

        scanner.close();
    }
    
    public static void main(String[] args) {
        greetUser();

        run();

        exit();
    }
}
