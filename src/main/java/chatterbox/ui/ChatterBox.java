package chatterbox.ui;

import chatterbox.command.CommandProcessor;
import chatterbox.memory.MemoryStorage;
import chatterbox.memory.Storage;
import chatterbox.task.Task;
import java.util.Scanner;

public class ChatterBox {

    private static Storage<Task> storage = new Storage<>();

    private static void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter Input: ");
            
            if (!scanner.hasNext()) {
                break;    
            }

            String userInput = scanner.next();

            if (userInput.equals("bye")) {
                break;
            }

            if (!CommandProcessor.isCommand(userInput)) {
                ChatterBoxUI.reply("Invalid command! Try Again!");
                scanner.nextLine();     // Clear input buffer
                continue;
            }

            CommandProcessor.processCommand(storage, scanner, userInput);
        }

        scanner.close();
    }

    public static void initialize() {
        MemoryStorage.loadTasks(storage);
    }

    public static void main(String[] args) {
        initialize();
        ChatterBoxUI.greet();
        run();
        ChatterBoxUI.farewell();
    }
}
