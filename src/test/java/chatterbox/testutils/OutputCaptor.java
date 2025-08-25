package chatterbox.testutils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OutputCaptor {
    private final PrintStream originalOut;
    private final ByteArrayOutputStream outputStream;

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

    public String capture(Runnable runnable) {
        start();
        try {
            runnable.run();
        } finally {
            stop();
        }
        return outputStream.toString();
    }

    public String getOutput() {
        return outputStream.toString();
    }

    public void reset() {
        outputStream.reset();
    }
}
