package chatterbox.command;

import java.util.Scanner;

import chatterbox.memory.Storage;
import chatterbox.task.Task;

/**
 * Represents a command that can be executed within the Chatterbox application.
 *
 * <p>A {@code Runnable} defines a single operation that processes user input
 * from a {@link Scanner} and may manipulate a {@link Storage} of {@link Task}
 * objects. Implementing classes encapsulate specific commands recognized by
 * {@link chatterbox.command.CommandProcessor}.
 *
 * <p>A {@code Runnable} will return a String as a response.
 */
public interface Runnable {
    String run(Storage<Task> s, Scanner sc);
}
