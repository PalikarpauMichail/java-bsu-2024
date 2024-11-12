package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "prototypeHolderBean", scope = BeanScope.PROTOTYPE)
public class PrototypeHolderBean {
    @Inject
    private PrototypeBean prototypeBean;

    @Inject
    String emptyString;
    String nullString;


    void printSomething() {
        System.out.println("Hello, I'm prototype user bean");
    }

    void doSomethingWithPrototypeBean() {
        System.out.println("Trying to shake prototype bean");
        prototypeBean.doSomething();
    }

    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }

    public String getEmptyString() {
        return emptyString;
    }

    public String getNullString() {
        return nullString;
    }
}