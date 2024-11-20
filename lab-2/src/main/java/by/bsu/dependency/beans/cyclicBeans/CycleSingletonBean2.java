package by.bsu.dependency.beans.cyclicBeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "cycleBean2", scope = BeanScope.SINGLETON)
public class CycleSingletonBean2 {
    @Inject
    CycleSingletonBean1 cycleSingletonBean1;
}