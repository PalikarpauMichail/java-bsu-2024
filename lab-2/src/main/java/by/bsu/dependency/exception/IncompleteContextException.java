package by.bsu.dependency.exception;

public class IncompleteContextException extends RuntimeException {
    public IncompleteContextException() {
        super("В созданном контексте, не объявлены бины, необходимые для внедрения зависимостей");
    }
    public IncompleteContextException(String message) {
        super(message);
    }
}
