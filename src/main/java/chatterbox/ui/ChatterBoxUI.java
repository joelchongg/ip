package chatterbox.ui;

public class ChatterBoxUI {
    private static String botName = "\nChatterBox: ";

    public static void greet() {
        System.out.println(botName + "Hello! I'm ChatterBox.");
        System.out.println(botName + "What can I do for you?");
    }

    public static void farewell() {
        System.out.println(botName + "Bye. Hope to see you again soon!");
    }

    public static void reply(String message) {
        System.out.println(botName + message);
    }
}
