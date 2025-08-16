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

    public static void main(String[] args) {
        greetUser();

        exit();
    }
}
