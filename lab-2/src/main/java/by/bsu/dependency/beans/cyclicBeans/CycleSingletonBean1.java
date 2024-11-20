package by.bsu.dependency.beans.cyclicBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "cycleBean1", scope = BeanScope.SINGLETON)
public class CycleSingletonBean1 {
    @Inject
    CycleSingletonBean2 cycleSingletonBean2;
}