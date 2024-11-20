package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class AutoScanApplicationContext extends SimpleApplicationContext {

    public AutoScanApplicationContext(String packageName) {
        super(collectAllClassesUsingReflectionLibrary(packageName));
    }

    private static Class<?>[] collectAllClassesUsingReflectionLibrary(String packageName) {
        Reflections reflections = new Reflections(packageName,
            Scanners.SubTypes.filterResultsBy(s -> true));

        return reflections.getSubTypesOf(Object.class).stream()
                .filter(clazz -> clazz.isAnnotationPresent(Bean.class))
                .toArray(Class[]::new);
    }
}
