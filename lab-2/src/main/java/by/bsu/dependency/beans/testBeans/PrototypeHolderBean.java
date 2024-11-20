package by.bsu.dependency.beans.testBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "prototypeHolderBean", scope = BeanScope.PROTOTYPE)
public class PrototypeHolderBean {
    @Inject
    private PrototypeBean prototypeBean;

    String someString;

    @PostConstruct
    private void instantiateString() {
        someString = "Hello";
    }
    @PostConstruct
    private void checkInjection() {
        if (prototypeBean == null) {
            throw new RuntimeException("Bean is not injected");
        }
    }
    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }

    public String getString() {
        return someString;
    }
}