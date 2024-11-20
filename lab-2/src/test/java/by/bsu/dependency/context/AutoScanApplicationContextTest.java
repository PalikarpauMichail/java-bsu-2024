package by.bsu.dependency.context;

import by.bsu.dependency.beans.testBeans.FirstBean;
import by.bsu.dependency.beans.testBeans.OtherBean;
import by.bsu.dependency.beans.testBeans.PrototypeBean;
import by.bsu.dependency.beans.testBeans.PrototypeHolderBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoScanApplicationContextTest extends SimpleApplicationContextTest {
    @BeforeEach
    @Override
    void init() {
        this.applicationContext = new AutoScanApplicationContext("by.bsu.dependency.beans.testBeans");
    }

    @Test
    @Override
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
    @Override
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
    @Override
    void testIsSingletonReturns() {
        assertThat(applicationContext.isSingleton("firstBean")).isTrue();
        assertThat(applicationContext.isSingleton("otherBean")).isTrue();

        assertThat(applicationContext.isSingleton("prototypeBean")).isFalse();
        assertThat(applicationContext.isSingleton("prototypeHolderBean")).isFalse();
    }

    @Test
    @Override
    void testIsPrototypeReturns() {
        assertThat(applicationContext.isPrototype("firstBean")).isFalse();
        assertThat(applicationContext.isPrototype("otherBean")).isFalse();
        assertThat(applicationContext.isPrototype("prototypeBean")).isTrue();
        assertThat(applicationContext.isPrototype("prototypeHolderBean")).isTrue();
    }

    @Test
    @Override
    void testSingletonCorrectness() {
        applicationContext.start();

        Assertions.assertEquals(
                applicationContext.getBean("firstBean"),
                applicationContext.getBean("firstBean"));
        Assertions.assertEquals(
                applicationContext.getBean("otherBean"),
                applicationContext.getBean("otherBean"));
    }
}
