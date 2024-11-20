package by.bsu.dependency.exception;

public class ApplicationContextNotStartedException extends RuntimeException {
    public ApplicationContextNotStartedException() {
        super("Контекст не запущен");
    }
    public ApplicationContextNotStartedException(String message) {
        super(message);
    }
}
