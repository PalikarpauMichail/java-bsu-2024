package by.bsu.dependency.beans.exampleBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;

@Bean(name = "phoneManufacturer", scope = BeanScope.SINGLETON)
public class PhoneManufacturer {
    String name = "Apple";
    int foundationYear = 1976;

    String getInfo() {
        String info =
                """
                Manufacturer name: %s
                Foundation year: %d;
                """;
        return String.format(info, name, foundationYear);
    }
}
