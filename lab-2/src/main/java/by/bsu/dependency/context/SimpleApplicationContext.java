package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.exception.NoSuchBeanDefinitionException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleApplicationContext extends AbstractApplicationContext {

    private final Map<String, Class<?>> beanDefinitions;
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
                        (mapEntry) -> instantiateBean(mapEntry.getValue())
                ));

    }

    @Override
    public boolean containsBean(String name) throws ApplicationContextNotStartedException {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException("");
        }
        return beanDefinitions.containsKey(name);
    }

    @Override
    public Object getBean(String name) {
        if (!containsBean(name)) {
            throw new NoSuchBeanDefinitionException("");
        }
        return getBean(beanDefinitions.get(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> clazz) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException("");
        }
        if (singletonBeans.containsKey(this.getBeanName(clazz))) {
            if (singletonBeans.get(this.getBeanName(clazz)) != null) {
                return (T) singletonBeans.get(this.getBeanName(clazz));
            } else {
                throw new RuntimeException();
            }
        }
        T bean = super.instantiateBean(clazz);

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
        return bean;
    }

    @Override
    public boolean isPrototype(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException("");
        }
        return this.getBeanScope(beanDefinitions.get(name)) == BeanScope.PROTOTYPE;
    }

    @Override
    public boolean isSingleton(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException("");
        }
        return this.getBeanScope(beanDefinitions.get(name)) == BeanScope.SINGLETON;
    }

    private String getBeanName(Class<?> clazz) {
        if (clazz.getAnnotation(Bean.class) == null) {
            return clazz.getSimpleName().substring(0, 1).toLowerCase()
                    + clazz.getSimpleName().substring(1);
        }
        return clazz.getAnnotation(Bean.class).name();
    }

    private BeanScope getBeanScope(Class<?> clazz) {
        if (clazz.getAnnotation(Bean.class) == null) {
            return BeanScope.SINGLETON;
        }
        return clazz.getAnnotation(Bean.class).scope();
    }

}
