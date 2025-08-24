package chatterbox.command;

import chatterbox.task.Task;
import chatterbox.memory.Storage;
import java.util.Scanner;

public interface Runnable {
    void run(Storage<Task> s, Scanner sc);
}
