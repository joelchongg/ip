package chatterbox.ui;

/**
 * Provides console-based interaction for the ChatterBox application.
 */
public class ChatterBoxUI {
    private static String botName = "\nChatterBox: ";

    /**
     * Prints a greeting message to the user.
     */
    public static void greet() {
        System.out.println(botName + "Hello! I'm ChatterBox.");
        System.out.println(botName + "What can I do for you?");
    }

    /**
     * Prints a farewell message to the user.
     */
    public static void farewell() {
        System.out.println(botName + "Bye. Hope to see you again soon!");
    }

    /**
     * Prints a reply message to the user.
     * @param message The string to be displayed after the bot name
     */
    public static void reply(String message) {
        System.out.println(botName + message);
    }
}
