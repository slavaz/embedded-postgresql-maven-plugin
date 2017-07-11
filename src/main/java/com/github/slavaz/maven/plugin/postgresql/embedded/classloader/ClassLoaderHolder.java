package com.github.slavaz.maven.plugin.postgresql.embedded.classloader;

import org.apache.commons.lang3.Validate;

import java.util.Optional;

public class ClassLoaderHolder {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<ClassLoader> instance = Optional.empty();

    private ClassLoaderHolder() {
    }

    public static void setClassLoader(final ClassLoader classLoader) {
        Validate.notNull(classLoader);
        if (instance.isPresent()) {
            throw new IllegalStateException("ClassLoader instance already set");
        }
        instance = Optional.of(classLoader);
    }

    public static Optional<ClassLoader> getClassLoader() {
        return instance;
    }
}
