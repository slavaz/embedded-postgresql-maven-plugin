package com.github.slavaz.maven.plugin.postgresql.embedded.classloader;

import java.util.Optional;

public class ClassLoaderHolder {
    private static Optional<ClassLoader> instance = Optional.empty();

    public static void setClassLoader(ClassLoader classLoader) {
        if (instance.isPresent()) {
            throw new IllegalStateException("ClassLoader instance already set");
        }
        instance = Optional.of(classLoader);
    }

    public static Optional<ClassLoader> getClassLoader() {
        return instance;
    }
}
