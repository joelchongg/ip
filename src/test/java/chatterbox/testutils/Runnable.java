package chatterbox.testutils;

/**
 * Functional interface representing a block of code that can be executed.
 *
 * <p>This is similar to {@link java.lang.Runnable} but defined for testing utilities
 * in the ChatterBox project. It allows passing code blocks to methods such as
 * {@link OutputCaptor#capture(Runnable)} to capture output or perform test actions.
 */
public interface Runnable {
    void run();
}
