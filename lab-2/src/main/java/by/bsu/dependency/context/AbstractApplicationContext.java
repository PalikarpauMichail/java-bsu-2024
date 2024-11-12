package by.bsu.dependency.context;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractApplicationContext implements ApplicationContext {
    protected ContextStatus status = ContextStatus.NOT_STARTED;

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    protected  <T> T instantiateBean(Class<T> beanClass) {
        try {
            return beanClass.getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isRunning() {
        return status == ContextStatus.STARTED;
    }

    @Override
    public void start() {
        status = ContextStatus.STARTED;
    }

}
