package by.bsu.dependency.beans.cyclicBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "cycleBean1", scope = BeanScope.PROTOTYPE)
public class CycleBean1 {
    @Inject
    CycleBean2 cycleBean2;
    @Inject
    CycleBean3 cycleBean3;
}
