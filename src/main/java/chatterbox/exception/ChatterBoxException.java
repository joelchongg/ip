package chatterbox.exception;

public class ChatterBoxException extends RuntimeException {
    public ChatterBoxException(String errorMessage) {
        super(errorMessage);
    }
}