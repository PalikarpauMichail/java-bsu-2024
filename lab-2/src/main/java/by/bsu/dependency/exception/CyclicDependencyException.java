package by.bsu.dependency.exception;

public class CyclicDependencyException extends RuntimeException {
    public CyclicDependencyException() {
        super("Найдена неразрешимая циклическая зависимость бинов");
    }
    public CyclicDependencyException(String message) {
        super(message);
    }
}
