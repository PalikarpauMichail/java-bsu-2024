package by.bsu.dependency.beans.cyclicBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "cycleBean4", scope = BeanScope.PROTOTYPE)
public class CycleBean4 {
    @Inject
    CycleBean2 cycleBean2;
}