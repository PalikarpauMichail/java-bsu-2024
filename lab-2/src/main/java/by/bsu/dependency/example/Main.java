package by.bsu.dependency.example;

import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.HardCodedSingletonApplicationContext;
import by.bsu.dependency.context.SimpleApplicationContext;

public class Main {

    public static void main(String[] args) {
//        ApplicationContext applicationContext = new HardCodedSingletonApplicationContext(
//                FirstBean.class, OtherBean.class
//        );
//        applicationContext.start();

        ApplicationContext applicationContext = new SimpleApplicationContext(
                PrototypeHolderBean.class
        );
        applicationContext.start();
        PrototypeHolderBean bean = (PrototypeHolderBean) applicationContext.getBean("prototypeHolderBean");

//        firstBean.doSomething();
//        otherBean.doSomething();

        // Метод падает, так как в классе HardCodedSingletonApplicationContext не реализовано внедрение зависимостей
        // otherBean.doSomethingWithFirst();
    }
}
