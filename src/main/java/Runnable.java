import java.util.Scanner;

public interface Runnable {
    <T extends Task> void run(Storage<T> s, Scanner sc);
}
