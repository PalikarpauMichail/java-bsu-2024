package by.bsu.dependency.beans.cyclicBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "cycleBean3", scope = BeanScope.PROTOTYPE)
public class CycleBean3 {
    @Inject
    CycleBean4 cycleBean4;
}
