package com.dnd.spaced.global.log.proxy.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class DataSourceLogProxyUtils {

    public Class<?>[] findInterfaces(Object targetObject) {
        Set<Class<?>> interfaces = new HashSet<>();
        Class<?> current = targetObject.getClass();

        while (current != null) {
            interfaces.addAll(Arrays.asList(current.getInterfaces()));
            current = current.getSuperclass();
        }

        return interfaces.toArray(new Class<?>[0]);
    }
}
