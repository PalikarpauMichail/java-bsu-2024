package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class AutoScanApplicationContext extends SimpleApplicationContext {
    private static Class<?>[] collectAllClassesUsingReflectionLibrary() {
        Reflections reflections = new Reflections("by.bsu.dependency.example",
            Scanners.SubTypes.filterResultsBy(s -> true));
        return reflections.getSubTypesOf(Object.class).stream()
                .filter(clazz -> clazz.isAnnotationPresent(Bean.class))
                .toArray(Class[]::new);
    }

    AutoScanApplicationContext() {
        super(collectAllClassesUsingReflectionLibrary());
    }
}
