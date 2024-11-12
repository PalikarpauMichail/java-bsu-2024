package by.bsu.dependency.context;

import by.bsu.dependency.example.*;
import by.bsu.dependency.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.exception.NoSuchBeanDefinitionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AutoScanApplicationContextTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new AutoScanApplicationContext();
    }

    @Test
    void testIsRunning() {
        assertThat(applicationContext.isRunning()).isFalse();
        applicationContext.start();
        assertThat(applicationContext.isRunning()).isTrue();
    }

    @Test
    void testContextContainsBeans() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("firstBean")).isTrue();
        assertThat(applicationContext.containsBean("otherBean")).isTrue();
        assertThat(applicationContext.containsBean("prototypeBean")).isTrue();
        assertThat(applicationContext.containsBean("prototypeHolderBean")).isTrue();

        assertThat(applicationContext.containsBean("implicitBean")).isFalse();
        assertThat(applicationContext.containsBean("randomBean")).isFalse();
    }


    @Test
    void testContextContainsNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.containsBean("firstBean")
        );
    }

    @Test
    void testContextGetBeanNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.getBean("firstBean")
        );
    }

    @Test
    void testGetBeanThrows() {
        applicationContext.start();

        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.getBean("randomName")
        );
    }

    @Test
    void testIsSingletonThrows() {
        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.isSingleton("randomName")
        );
    }

    @Test
    void testIsPrototypeThrows() {
        assertThrows(
                RuntimeException.class,
                () -> applicationContext.isPrototype("randomName")
        );
    }

    @Test
    void testGetBeanReturns() {
        applicationContext.start();

        assertThat(applicationContext.getBean("firstBean"))
                .isNotNull().isInstanceOf(FirstBean.class);
        assertThat(applicationContext.getBean("otherBean"))
                .isNotNull().isInstanceOf(OtherBean.class);
        assertThat(applicationContext.getBean("prototypeBean"))
                .isNotNull().isInstanceOf(PrototypeBean.class);
        assertThat(applicationContext.getBean("prototypeHolderBean"))
                .isNotNull().isInstanceOf(PrototypeHolderBean.class);
    }

    @Test
    void testIsSingletonReturns() {
        assertThat(applicationContext.isSingleton("firstBean")).isTrue();
        assertThat(applicationContext.isSingleton("otherBean")).isTrue();

        assertThat(applicationContext.isSingleton("prototypeBean")).isFalse();
        assertThat(applicationContext.isSingleton("prototypeHolderBean")).isFalse();
    }



    @Test
    void testIsPrototypeReturns() {
        assertThat(applicationContext.isPrototype("firstBean")).isFalse();
        assertThat(applicationContext.isPrototype("otherBean")).isFalse();
        assertThat(applicationContext.isPrototype("prototypeBean")).isTrue();
        assertThat(applicationContext.isPrototype("prototypeHolderBean")).isTrue();
    }

    @Test
    void testSingletonCorrectness() {
        applicationContext.start();


        Assertions.assertEquals(
                applicationContext.getBean("firstBean"),
                applicationContext.getBean("firstBean"));
        Assertions.assertEquals(
                applicationContext.getBean("otherBean"),
                applicationContext.getBean("otherBean"));
    }

    @Test
    void testPrototypeCorrectness() {
        applicationContext.start();

        Assertions.assertNotEquals(
                applicationContext.getBean("prototypeBean"),
                applicationContext.getBean("prototypeBean"));
        Assertions.assertNotEquals(
                applicationContext.getBean("prototypeHolderBean"),
                applicationContext.getBean("prototypeHolderBean"));

        Assertions.assertNotEquals(
                ((PrototypeHolderBean) applicationContext.getBean("prototypeHolderBean")).getPrototypeBean(),
                ((PrototypeHolderBean) applicationContext.getBean("prototypeHolderBean")).getPrototypeBean());

    }

    @Test
    void injectionCorrectness() {
        applicationContext.start();

        Assertions.assertNull(
                ((PrototypeHolderBean) applicationContext.getBean("prototypeHolderBean")).getNullString());
        Assertions.assertNotNull(
                ((PrototypeHolderBean) applicationContext.getBean("prototypeHolderBean")).getEmptyString());
    }
}
