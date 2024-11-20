package by.bsu.dependency.exception;

public class NoSuchBeanDefinitionException extends RuntimeException {
    public NoSuchBeanDefinitionException() {
        super("Запрошенный бин не содержится в контексте");
    }
    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}
