package by.bsu.dependency.beans.cyclicBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "cycleBean2", scope = BeanScope.PROTOTYPE)
public class CycleBean2 {
    @Inject
    CycleBean3 cycleBean3;
}
