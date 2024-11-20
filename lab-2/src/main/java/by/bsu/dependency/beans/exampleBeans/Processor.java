package by.bsu.dependency.beans.exampleBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;

@Bean(name = "processor", scope = BeanScope.PROTOTYPE)
public class Processor {
    static private int totalManufactured = 0;

    private String name = "Apple A10 Bionic";
    private int coresNumber = 4;
    private final int processorID;

    public Processor() {
        processorID = totalManufactured;
        totalManufactured++;
    }

    public void upgrade() {
        this.name = "Apple A18 Bionic";
        this.coresNumber = 6;
    }

    public String getInfo() {
        String info =
                """
                Processor name: %s
                Number of cores: %d
                ProcessorID: %d
                """;
        return String.format(info, name, this.coresNumber, processorID);
    }
}
