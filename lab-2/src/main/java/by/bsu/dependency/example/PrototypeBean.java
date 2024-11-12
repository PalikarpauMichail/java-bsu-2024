package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;

@Bean(name = "prototypeBean", scope = BeanScope.PROTOTYPE)
public class PrototypeBean {
    void printSomething() {
        System.out.println("Hello, I'm simple prototype bean");
    }

    void doSomething() {
        System.out.println("Simple prototype bean is working on a project...");
    }
}
