package chatterbox.memory;

import chatterbox.task.Task;
import java.util.ArrayList;

/**
 * A generic storage container for {@link Task} objects in the ChatterBox application.
 * 
 * <p>The {@code Storage} class manages a collection of tasks, providing methods to
 * add, remove, retrieve and display tasks. It also supports searching tasks by 
 * their description. This class uses an {@link ArrayList} internally to store 
 * the tasks and is generic over any subclass of {@link Task}.
 */
public class Storage<T extends Task> {

    private ArrayList<T> storage;

    public Storage() {
        this.storage = new ArrayList<>();
    }

    /**
     * Outputs to the command line interface the items stored in a numbered list format.
     * Output is empty if storage is empty
     *
     * @return Returns a string representing {@code Task} objects in {@code Storage} 
     */
    public String displayItems() {
        String response = "";

        for (int index = 1; index <= storage.size(); ++index) {
            response += index + "." + this.storage.get(index - 1) + '\n';
        }

        return response;
    }

    /**
     * Returns an ArrayList of Tasks for which each Task contains the given description
     * 
     * @param desc String used to search the description for.
     * @return ArrayList<Task>
     */
    public ArrayList<Task> searchTasksByDescription(String desc) {
        ArrayList<Task> result = new ArrayList<>();
        
        for (Task task : storage) {
            String description = task.getTaskDescription();

            if (description.contains(desc)) {
                result.add(task);
            }
        }

        return result;
    }

    public boolean addItem(T item) {
        return this.storage.add(item);
    }

    public T removeItem(int index) throws IndexOutOfBoundsException {
        return this.storage.remove(index);
    }

    public T getItem(int index) throws IndexOutOfBoundsException {
        return this.storage.get(index);
    }

    public int size() {
        return this.storage.size();
    }
}
