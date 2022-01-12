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
	public Set<Class<?>> getClasses(String packageName, Class<?> targetClazz) {
		return ClassPath.from(XenLib.getPlugin().getClass().getClassLoader())
				.getAllClasses()
				.parallelStream()
				.filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
				.map(ClassPath.ClassInfo::load)
				.filter(targetClazz::isAssignableFrom)
				.collect(Collectors.toSet());
	}

}
