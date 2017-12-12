package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.io.IOException;

import static com.github.slavaz.maven.plugin.postgresql.embedded.psql.util.PostgresConfigUtil.getPostgresConfig;

/**
 * Created by slavaz on 13/02/17.
 */
public class PgInstanceManager {

    private static PostgresProcess process = null;

    public static void start(IPgInstanceProcessData pgInstanceProcessData) throws IOException {
        if (process != null) {
            throw new IllegalStateException("Postgres already started");
        }

        final PostgresStarter<PostgresExecutable, PostgresProcess> postgresStarter =
                PostgresStarter.getDefaultInstance();

        final PostgresConfig postgresConfig = getPostgresConfig(pgInstanceProcessData);

        PostgresExecutable postgresExecutable = postgresStarter.prepare(postgresConfig);

        process = postgresExecutable.start();
    }

    public static void stop() throws InterruptedException {
        if (process != null) {
            PostgresProcess p = process;
            process = null;
            p.stop();
            p.waitFor();
        }
    }

}
