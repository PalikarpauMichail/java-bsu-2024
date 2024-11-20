package by.bsu.dependency.context;

import by.bsu.dependency.beans.cyclicBeans.*;
import by.bsu.dependency.exception.CyclicDependencyException;
import by.bsu.dependency.exception.IncompleteContextException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CyclicDependenciesTest {

    public ApplicationContext cyclicDependencyApplicationContext;
    public ApplicationContext uncompleteApplicationContext;
    public ApplicationContext cyclicDependencySingletonApplicationContext;
    @BeforeEach
    void init() {
        cyclicDependencyApplicationContext = new SimpleApplicationContext(
                CycleBean1.class,
                CycleBean2.class,
                CycleBean3.class,
                CycleBean4.class
        );
        uncompleteApplicationContext = new SimpleApplicationContext(
                CycleBean1.class,
                CycleBean2.class
        );
        cyclicDependencySingletonApplicationContext = new SimpleApplicationContext(
                CycleSingletonBean1.class,
                CycleSingletonBean2.class
        );
    }

    @Test
    void testBadApplicationContextThrows() {

        assertThrows(CyclicDependencyException.class, () -> cyclicDependencyApplicationContext.start());
        assertThrows(IncompleteContextException.class, () -> uncompleteApplicationContext.start());
    }

    @Test
    void testGoodCyclicDependencyDoesNotThrow() {
        Assertions.assertDoesNotThrow(() -> cyclicDependencySingletonApplicationContext.start());
    }
}
