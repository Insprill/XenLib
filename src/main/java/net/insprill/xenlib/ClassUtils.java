package net.insprill.xenlib;

import com.google.common.reflect.ClassPath;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ClassUtils {

	@SneakyThrows
	@SuppressWarnings("UnstableApiUsage")
	public Set<Class<?>> getImplementingClasses(String packageName, Class<?> targetInterface) {
		return ClassPath.from(XenLib.getPlugin().getClass().getClassLoader())
				.getAllClasses()
				.parallelStream()
				.filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
				.map(ClassPath.ClassInfo::load)
				.filter(targetInterface::isAssignableFrom)
				.filter(clazz -> clazz != targetInterface)
				.collect(Collectors.toSet());
	}

}
