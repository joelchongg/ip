package chatterbox.testutils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Utility class to capture output sent to {@link System#out}.
 *
 * <p>Useful for testing methods that print to the console. You
 * can run a piece of code and retrieve what it printed as a string.
 */
public class OutputCaptor {
    private final PrintStream originalOut;
    private final ByteArrayOutputStream outputStream;

    /**
     * Constructs a new {@code OutputCaptor} and initializes
     * the underlying output stream.
     */
    public OutputCaptor() {
        this.originalOut = System.out;
        this.outputStream = new ByteArrayOutputStream();
    }

    private void start() {
        System.setOut(new PrintStream(outputStream));
    }

    private void stop() {
        System.setOut(originalOut);
    }

    /**
     * Captures the output of the given {@link Runnable} and
     * returns it as a string.
     *
     * @param runnable the code to run and capture output from
     * @return the captured output as a string
     */
    public String capture(Runnable runnable) {
        start();
        try {
            runnable.run();
        } finally {
            stop();
        }
        return outputStream.toString();
    }

    /**
     * Returns the currently captured output as a string.
     *
     * @return the captured output
     */
    public String getOutput() {
        return outputStream.toString();
    }

    /**
     * Resets the captured output buffer, clearing any previously
     * captured output.
     */
    public void reset() {
        outputStream.reset();
    }
}
