package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.data.PgInstanceProcessData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Starts a PostgreSQL instance in a separate thread using a separate classloader. This is necessary, because the
 * Embedded PostgreSQL registers a shutdown hook which is run when the JVM shuts down, to shut down the process. When a
 * build fails, this plugin's "stop" goal is never run, so we rely on the shutdown hook to shut down PostgreSQL and
 * clean up resources. However, Maven plugins run in separate class loaders, which means the required classes to shut
 * down PostgreSQL are not available any more in the shutdown hook. By starting a thread with our own class loader,
 * we ensure that the classes are available during the shutdown hook.
 */
public class IsolatedPgInstanceManager {
    private final ClassLoader classLoader;

    public IsolatedPgInstanceManager(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void start(IPgInstanceProcessData data) throws IOException {
        Thread postgresThread = new Thread(() -> {
            Method startPostgres = getMethod("startPostgres", String.class, String.class, int.class, String.class,
                    String.class, String.class, String.class, String.class, String.class, String.class);

            invokeStaticMethod(startPostgres, data.getPgServerVersion(), data.getPgHost(), data.getPgPort(), data
                    .getDbName(), data.getUserName(), data.getPassword(), data.getPgDatabaseDir(), data
                    .getPgLocale(), data.getPgCharset(), data.getPgDumpFile());

        }, "postgres-embedded");
        postgresThread.setContextClassLoader(classLoader);
        postgresThread.start();

        try {
            postgresThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Embedded Postgres thread was interrupted", e);
        }
    }

    public void stop() {
        invokeStaticMethod(getMethod("stopPostgres"));
    }

    @SuppressWarnings("unused")
    public static void startPostgres(final String pgServerVersion, final String pgHost, final int pgPort, final
    String dbName, final String userName, final String password, final String pgDatabaseDir, final String pgLocale,
                                     final String pgCharset, final String pgDumpFile) throws IOException {
        PgInstanceManager.start(new PgInstanceProcessData(pgServerVersion, pgHost, pgPort, dbName, userName,
                password, pgDatabaseDir, pgLocale, pgCharset, pgDumpFile));
    }

    @SuppressWarnings("unused")
    public static void stopPostgres() throws InterruptedException {
        PgInstanceManager.stop();
    }

    private Method getMethod(String methodName, Class<?>... parameterTypes) {
        Class<?> managerClass = loadClass(IsolatedPgInstanceManager.class.getName());
        try {
            return managerClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No method " + methodName + " on " + managerClass, e);
        }
    }

    private Class loadClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + className, e);
        }
    }

    private static void invokeStaticMethod(Method m, Object... arguments) {
        try {
            m.invoke(null, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Method " + m.getName() + " not accessible", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Invocation of method " + m.getName() + " threw exception", e);
        }
    }

}
