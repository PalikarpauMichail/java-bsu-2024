package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.exception.CyclicDependencyException;
import by.bsu.dependency.exception.IncompleteContextException;
import by.bsu.dependency.graph.DirectedGraph;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public abstract class AbstractApplicationContext implements ApplicationContext {

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    protected ContextStatus status = ContextStatus.NOT_STARTED;
    protected Map<String, Class<?>> beanDefinitions = new HashMap<>();

    protected String getBeanName(Class<?> clazz) {
        if (clazz.getAnnotation(Bean.class) == null) {
            return clazz.getSimpleName().substring(0, 1).toLowerCase()
                    + clazz.getSimpleName().substring(1);
        }
        return clazz.getAnnotation(Bean.class).name();
    }

    protected BeanScope getBeanScope(Class<?> clazz) {
        if (clazz.getAnnotation(Bean.class) == null) {
            return BeanScope.SINGLETON;
        }
        return clazz.getAnnotation(Bean.class).scope();
    }

    protected <T> T instantiateBean(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean containsBean(String name) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        return beanDefinitions.containsKey(name);
    }

    @Override
    public boolean isRunning() {
        return status == ContextStatus.STARTED;
    }

    @Override
    public void start() {
        status = ContextStatus.STARTED;
        var injectionGraph = buildInjectionGraph();
        if (injectionGraph.hasCycle()) {
            throw new CyclicDependencyException();
        }
    }

    /**
     * Строит граф зависимостей.
     * <br/>
     * @throws IncompleteContextException если контекст не содержит бинов, необходимых, для внедрения зависимостей
     * @return построенный {@code DirectedGraph}
     */
    private DirectedGraph buildInjectionGraph() {
        Map<String, Integer> classesEnumeration = new HashMap<>();
        DirectedGraph injectionGraph = new DirectedGraph(beanDefinitions.size());

        int currentNumber = 0;
        for (var entry : beanDefinitions.entrySet()) {
            classesEnumeration.put(entry.getKey(), currentNumber);
            currentNumber++;
        }

        for (var entry : beanDefinitions.entrySet()) {
            String name = entry.getKey();
            Class<?> clazz = entry.getValue();
            int u = classesEnumeration.get(name);

            if (getBeanScope(clazz) == BeanScope.SINGLETON) {
                continue;
            }

            List<Integer> adjacentVertices = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> getBeanScope(field.getType()) == BeanScope.PROTOTYPE)
                    .map(field -> {
                        String beanName = getBeanName(field.getType());
                        if (!containsBean(beanName)) {
                            throw new IncompleteContextException();
                        }
                        return classesEnumeration.get(getBeanName(field.getType()));
                    })
                    .toList();

            for (int v : adjacentVertices) {
                injectionGraph.addEdge(u, v);
            }
        }

        return injectionGraph;
    }

}
