package by.bsu.dependency.myExample;

import by.bsu.dependency.beans.exampleBeans.*;
import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.AutoScanApplicationContext;

public class Main {
    public static void main(String[] args) {
       ApplicationContext applicationContext= new AutoScanApplicationContext(
               "by.bsu.dependency.beans.exampleBeans");
       applicationContext.start();
       Phone phone0 = applicationContext.getBean(Phone.class);
       Phone phone1 = applicationContext.getBean(Phone.class);
       System.out.println(phone0.getPhoneInfo());
       System.out.println(phone1.getPhoneInfo());
    }
}
