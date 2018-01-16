package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.util.PostgresConfigUtil;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.io.IOException;

/**
 * Created by slavaz on 13/02/17.
 */
public class PgInstanceManager {

    private static PostgresProcess process = null;

    public static void start(final IPgInstanceProcessData pgInstanceProcessData) throws IOException {
        if (process != null) {
            throw new IllegalStateException("Postgres already started");
        }

        final PostgresStarter<PostgresExecutable, PostgresProcess> postgresStarter =
                PostgresStarter.getDefaultInstance();

        final PostgresConfig postgresConfig = PostgresConfigUtil.get(pgInstanceProcessData);

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
