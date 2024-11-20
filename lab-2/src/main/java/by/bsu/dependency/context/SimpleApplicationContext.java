package by.bsu.dependency.context;

import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.exception.NoSuchBeanDefinitionException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleApplicationContext extends AbstractApplicationContext {

    private Map<String, Object> singletonBeans = new HashMap<>();

    /**
     * Создает контекст, содержащий классы, переданные в параметре.
     * <br/>
     * Если на классе нет аннотации {@code @Bean}, имя бина получается из названия класса, скоуп бина по дефолту
     * считается {@code Singleton}.
     * <br/>
     * Подразумевается, что у всех классов, переданных в списке, есть конструктор без аргументов.
     *
     * @param beanClasses классы, из которых требуется создать бины
     */
    public SimpleApplicationContext(Class<?>... beanClasses) {
        beanDefinitions = Arrays.stream(beanClasses).collect(
                Collectors.toMap(
                        this::getBeanName,
                        Function.identity()
                ));
    }
    /**
     * Помимо прочего, метод должен заниматься внедрением зависимостей в создаваемые объекты
     */
    @Override
    public void start() {
        super.start();
        singletonBeans = beanDefinitions.entrySet().stream()
                .filter((mapEntry) -> getBeanScope(mapEntry.getValue()) == BeanScope.SINGLETON)
                .collect(Collectors.toMap(
                        Map.Entry<String, Class<?>>::getKey,
                        (mapEntry) -> getBean(mapEntry.getValue())
                ));

    }

    @Override
    public Object getBean(String name) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        if (!containsBean(name)) {
            throw new NoSuchBeanDefinitionException();
        }
        return getBean(beanDefinitions.get(name));
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        if (!containsBean(getBeanName(clazz))) {
            throw new NoSuchBeanDefinitionException();
        }
        if (singletonBeans.containsKey(this.getBeanName(clazz))) {
            return clazz.cast(singletonBeans.get(this.getBeanName(clazz)));
        }
        return instantiateBean(clazz);
    }

    @Override
    public boolean isPrototype(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException();
        }
        return this.getBeanScope(beanDefinitions.get(name)) == BeanScope.PROTOTYPE;
    }

    @Override
    public boolean isSingleton(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException();
        }
        return this.getBeanScope(beanDefinitions.get(name)) == BeanScope.SINGLETON;
    }

    @Override
    protected <T> T instantiateBean(Class<T> clazz) {
        T bean = super.instantiateBean(clazz);
        if (getBeanScope(clazz) == BeanScope.SINGLETON) {
            singletonBeans.put(getBeanName(clazz), bean);
        }
        injectDependencies(bean);
        runPostConstructMethods(bean);
        return bean;
    }

    private void runPostConstructMethods(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> {
                    try {
                        method.setAccessible(true);
                        method.invoke(bean);
                    } catch(IllegalAccessException | IllegalArgumentException |
                            java. lang. reflect. InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void injectDependencies(Object bean) {
        Class<?> clazz = bean.getClass();
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(bean, getBean(field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
