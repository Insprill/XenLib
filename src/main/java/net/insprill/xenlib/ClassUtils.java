package net.insprill.xenlib;

import com.google.common.reflect.ClassPath;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ClassUtils {

    /**
     * Gets all classes in the package that implement the given interface. Doesn't include sub-packages.
     *
     * @param packageName     Package to search. CaSe-SeNsItIvE.
     * @param targetInterface Interface to search for.
     * @param <T>             Type of the interface.
     * @return Set of classes that implement the interface.
     */
    public <T> Set<Class<T>> getImplementingClasses(String packageName, Class<T> targetInterface) {
        return getImplementingClasses(packageName, targetInterface, false);
    }

    /**
     * Gets all classes in the package that implement the given interface.
     *
     * @param packageName     Package to search. CaSe-SeNsItIvE.
     * @param targetInterface Interface to search for.
     * @param deep            Whether to search sub-packages.
     * @param <T>             Type of the interface.
     * @return Set of classes that implement the interface.
     */
    @SneakyThrows
    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    public <T> Set<Class<T>> getImplementingClasses(String packageName, Class<T> targetInterface, boolean deep) {
        return ClassPath.from(XenLib.getPlugin().getClass().getClassLoader())
                .getAllClasses()
                .parallelStream()
                .filter(info -> (deep && info.getPackageName().startsWith(packageName)) || (!deep && info.getPackageName().equals(packageName)))
                .map(ClassPath.ClassInfo::load)
                .filter(targetInterface::isAssignableFrom)
                .filter(clazz -> clazz != targetInterface || clazz.isInterface())
                .map(clazz -> (Class<T>) clazz)
                .collect(Collectors.toSet());
    }

}
