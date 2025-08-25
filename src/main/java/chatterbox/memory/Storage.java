package chatterbox.memory;

import chatterbox.task.Task;
import java.util.ArrayList;

public class Storage<T extends Task> {

    private ArrayList<T> storage;

    public Storage() {
        this.storage = new ArrayList<>();
    }

    public void displayItems() {
        for (int index = 1; index <= storage.size(); ++index) {
            System.out.println(index + "." + this.storage.get(index - 1));
        }
        System.out.println();
    }

    public ArrayList<Task> searchTasksByDescription(String desc) {
        ArrayList<Task> result = new ArrayList<>();
        
        for (Task task : storage) {
            String description = task.getTaskDescription();
            System.out.println(description);

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
