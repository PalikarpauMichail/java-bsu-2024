package by.bsu.dependency.context;

import by.bsu.dependency.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.exception.NoSuchBeanDefinitionException;
import by.bsu.dependency.beans.testBeans.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleApplicationContextTest {

    public ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new SimpleApplicationContext(
                FirstBean.class,
                OtherBean.class,
                PrototypeBean.class,
                PrototypeHolderBean.class,
                ImplicitBean.class
        );
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
        assertThat(applicationContext.containsBean("implicitBean")).isTrue();
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
        assertThat(applicationContext.getBean("implicitBean"))
                .isNotNull().isInstanceOf(ImplicitBean.class);
    }

    @Test
    void testIsSingletonReturns() {
        assertThat(applicationContext.isSingleton("firstBean")).isTrue();
        assertThat(applicationContext.isSingleton("otherBean")).isTrue();
        assertThat(applicationContext.isSingleton("prototypeBean")).isFalse();
        assertThat(applicationContext.isSingleton("prototypeHolderBean")).isFalse();
        assertThat(applicationContext.isSingleton("implicitBean")).isTrue();
    }



    @Test
    void testIsPrototypeReturns() {
        assertThat(applicationContext.isPrototype("firstBean")).isFalse();
        assertThat(applicationContext.isPrototype("otherBean")).isFalse();
        assertThat(applicationContext.isPrototype("prototypeBean")).isTrue();
        assertThat(applicationContext.isPrototype("prototypeHolderBean")).isTrue();
        assertThat(applicationContext.isPrototype("implicitBean")).isFalse();
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
        Assertions.assertEquals(
                applicationContext.getBean("implicitBean"),
                applicationContext.getBean("implicitBean"));
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
    void postConstructCorrectness() {
        applicationContext.start();

        Assertions.assertEquals(
                ((PrototypeHolderBean) applicationContext.getBean("prototypeHolderBean")).getString(),
                "Hello");

    }
}
