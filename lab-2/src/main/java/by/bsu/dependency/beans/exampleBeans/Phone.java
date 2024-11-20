package by.bsu.dependency.beans.exampleBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "modernPhone", scope = BeanScope.PROTOTYPE)
public class Phone {
    static private int totalManufactured = 0;

    @Inject
    private Processor processor;
    @Inject
    private PhoneManufacturer manufacturer;
    private int phoneID = 0;

    public Phone() {
        phoneID = totalManufactured;
        totalManufactured++;
    }

    private String getInfo() {
        String info =
                """
                PhoneID : %d
                """;
        return String.format(info, phoneID);
    }

    public String getPhoneInfo() {
        return this.manufacturer.getInfo() + this.processor.getInfo() + this.getInfo();
    }

    @PostConstruct
    public void upgradeProcessor() {
        this.processor.upgrade();
    }
}
