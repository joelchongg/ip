package chatterbox.memory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import chatterbox.exception.ChatterBoxException;
import chatterbox.task.DeadlineTask;
import chatterbox.task.EventTask;
import chatterbox.task.Task;
import chatterbox.task.TodoTask;
import chatterbox.ui.ChatterBoxUI;

/**
 * Handles persistent storage of {@link Task} objects for the ChatterBox application.
 *
 * <p>The {@code MemoryStorage} class provides methods to load tasks from disk into
 * memory, save new tasks, update completion status, and delete tasks. It ensures data
 * integrity by detecting corrupted files and creating temporary memory files if needed.
 * All methods are static and operate on a shared file located at {@code ./data/tasks.txt}.
 *
 * <p>Tasks are serialized in a simple text format and reconstructed into their corresponding
 * {@link Task} subclasses when loaded.
 */
public class MemoryStorage {
    private static File taskFile = new File("./data/tasks.txt");

    static {
        try {
            taskFile.getParentFile().mkdirs();
            taskFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Unable to create data folder to save tasks!");
        }
    }

    /**
     * Creates and stores tasks stored in the hard drive into the storage object.
     * If memory file is corrupted, task is skipped and removed from memory.
     * Alerts user via the command line interface if data is corrupted.
     * If file is corrupted, a temporary memory file is generated for the user.
     *
     * @param storage Storage object in which Task objects are stored in
     */
    public static void loadTasks(Storage<Task> storage) {
        assert storage != null : "Storage must not be null";

        File tempFile = new File("./data/tasks_tmp.txt");
        boolean isCorruptedFile = false;

        try (Scanner scanner = new Scanner(taskFile);
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (!initializeTask(storage, line)) {
                    isCorruptedFile = true;
                    continue;
                }
                writer.println(line);
            }

            if (isCorruptedFile) {
                ChatterBoxUI.reply("Save file is corrupted! Corrupted tasks have been deleted.");

                if (!taskFile.delete() || !tempFile.renameTo(taskFile)) {
                    System.out.println("File update failed! Task data may be inconsistent.");
                }
            } else {
                tempFile.delete();
            }
        } catch (IOException e) {
            System.out.println("Unable to find task file from memory!");
        }
    }

    /**
     * Updates the task's completion status in memory.
     * Task is accessed based on its index in the virtual storage object.
     * If index is invalid, memory will not be updated.
     *
     * @param index Index in which the task is stored in memory
     * @param isCompleted Completion status to set the selected task to
     */
    public static void updateTaskCompletion(int index, boolean isCompleted) {
        File tempFile = new File("./data/tasks_tmp.txt");
        int currentLine = 0;

        try (Scanner scanner = new Scanner(taskFile);
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (currentLine == index) {
                    line = updateTask(line, isCompleted);
                }

                ++currentLine;
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Unable to find file!");
        }

        if (!taskFile.delete()) {
            System.out.println("Unable to delete original file to update task completion!");
            return;
        }

        if (!tempFile.renameTo(taskFile)) {
            System.out.println("Could not rename temp file to update task completion!");
        }
    }

    /**
     * Adds and stores a new task into memory.
     * Warns the user via the command line interface if task cannot be saved.
     *
     * @param task Task to be saved into memory
     */
    public static void saveTask(Task task) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(taskFile, true))) {
            String line = task.getTaskSymbol() + " | "
                    + (task.getStatusIcon().equals("X") ? "1" : "0") + " | "
                    + task.getTaskDescription();

            if (task instanceof DeadlineTask) {
                DeadlineTask tempTask = (DeadlineTask) task;
                line += " | " + tempTask.serializeDeadline();
            } else if (task instanceof EventTask) {
                EventTask tempTask = (EventTask) task;
                line += " | " + tempTask.getStartTime() + " | " + tempTask.getEndTime();
            }

            writer.println(line);
        } catch (IOException e) {
            System.out.println("Could not save new task to file!");
        }
    }

    /**
     * Deletes the task that is stored in memory.
     * Task to be deleted is accessed based on its index in the virtual storage object.
     * No task will be deleted if index is invalid.
     *
     * @param index Index in which the task is stored in the virtual storage object
     */
    public static void deleteTask(int index) {
        File tempFile = new File("./data/tasks_tmp.txt");
        int currentLine = 0;

        try (Scanner scanner = new Scanner(taskFile);
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (currentLine == index) {
                    ++currentLine;
                    continue;
                }

                ++currentLine;
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Unable to find file!");
        }

        if (!taskFile.delete()) {
            System.out.println("Unable to delete original file to update task deletion!");
            return;
        }

        if (!tempFile.renameTo(taskFile)) {
            System.out.println("Could not rename temp file to update task deletion!");
        }
    }

    /**
     * Creates a task object from memory and adds it into the storage object.
     * If memory is corrupted, the task object is not constructed.
     *
     * @param storage Storage object in which Task objects are stored in
     * @param input Data stored in the memory that corresponds to a task object
     */
    private static boolean initializeTask(Storage<Task> storage, String input) {
        try {
            String[] tokens = input.split(" \\| ");
            char taskType = tokens[0].charAt(0);
            String completed = tokens[1];
            String description = tokens[2];

            boolean isCompleted;
            if (completed.equals("1")) {
                isCompleted = true;
            } else if (completed.equals("0")) {
                isCompleted = false;
            } else {
                throw new ChatterBoxException("Corrupted save file. Completion status is invalid.");
            }

            switch (taskType) {
            case 'T':
                storage.addItem(new TodoTask(description, isCompleted));
                return true;
            case 'D':
                String deadline = tokens[3];
                storage.addItem(new DeadlineTask(description, deadline, isCompleted));
                return true;
            case 'E':
                String startTime = tokens[3];
                String endTime = tokens[4];
                storage.addItem(new EventTask(description, startTime, endTime, isCompleted));
                return true;

            default:
                return false;
            }
        } catch (IndexOutOfBoundsException | ChatterBoxException e) {
            return false;
        }
    }

    /**
     * Returns a String to be stored in memory for a task.
     * The String returned depends on the completion status given.
     *
     * @param task Task to be serialized and stored in the memory
     * @param isCompleted Completion status of the corresponding task
     * @return String that contains the serialized task information
     */
    private static String updateTask(String task, boolean isCompleted) {
        String[] tokens = task.split(" \\| ");
        tokens[1] = isCompleted ? "1" : "0";

        return String.join(" | ", tokens);
    }
}
