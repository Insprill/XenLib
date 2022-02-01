package net.insprill.xenlib;

import com.google.common.reflect.ClassPath;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ClassUtils {

    @SneakyThrows
    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    public <T> Set<Class<T>> getImplementingClasses(String packageName, Class<T> targetInterface) {
        return ClassPath.from(XenLib.getPlugin().getClass().getClassLoader())
                .getAllClasses()
                .parallelStream()
                .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
                .map(ClassPath.ClassInfo::load)
                .filter(targetInterface::isAssignableFrom)
                .filter(clazz -> clazz != targetInterface)
                .map(clazz -> (Class<T>) clazz)
                .collect(Collectors.toSet());
    }

}
