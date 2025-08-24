import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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

    public static void loadTasks(Storage<Task> storage) {
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

    private static String updateTask(String task, boolean isCompleted) {
        String[] tokens = task.split(" \\| ");
        tokens[1] = isCompleted ? "1" : "0";
        return String.join(" | ", tokens);
    }
}
