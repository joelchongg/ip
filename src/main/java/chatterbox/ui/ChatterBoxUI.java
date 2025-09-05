package chatterbox.ui;

/**
 * Provides console-based interaction for the ChatterBox application.
 */
public class ChatterBoxUI {
    private static String BOT_NAME = "\nChatterBox: ";

    /**
     * Prints a greeting message to the user.
     */
    public static void greet() {
        reply("Hello! I'm ChatterBox.");
        reply("What can I do for you?");
    }

    /**
     * Prints a farewell message to the user.
     */
    public static void farewell() {
        reply("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a reply message to the user.
     * @param message The string to be displayed after the bot name
     */
    public static void reply(String message) {
        System.out.println(BOT_NAME + message);
    }
}
