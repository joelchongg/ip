package chatterbox.exception;

/**
 * Custom unchecked exception for the ChatterBox application.
 *
 * <p>This exception is  thrown to indicated errors specific
 * to the application's logic or runtime conditions.
 */
public class ChatterBoxException extends RuntimeException {
    /**
     * Constructs a new ChatterBoxException with the specified error message.
     *
     * @param errorMessage the detail message explaining the cause of the exception
     */
    public ChatterBoxException(String errorMessage) {
        super(errorMessage);
    }
}
