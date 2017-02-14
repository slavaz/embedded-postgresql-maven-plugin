package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import de.flapdoodle.embed.process.distribution.IVersion;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.io.IOException;

import static de.flapdoodle.embed.process.runtime.Network.getLocalHost;
import static java.util.Arrays.asList;

/**
 * Created by slavaz on 13/02/17.
 */
public class PgInstanceManager {

    public void start() throws IOException {

        final PgInstanceProcess pgInstanceProcess = PgInstanceProcess.getInstance();

        final PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getDefaultInstance();

        final PostgresConfig config = getPostgresConfig(pgInstanceProcess);

        PostgresExecutable exec = runtime.prepare(config);

        pgInstanceProcess.setProcess(exec.start());
    }

    public void stop() {

        final PgInstanceProcess pgInstanceProcess = PgInstanceProcess.getInstance();
        final PostgresProcess process = pgInstanceProcess.getProcess();

        process.stop();
    }

    private PostgresConfig getPostgresConfig(final PgInstanceProcess pgInstanceProcess)
            /* String dbName, String userName, String password) */
            throws IOException {

        final AbstractPostgresConfig.Storage storage =
                new AbstractPostgresConfig.Storage(pgInstanceProcess.getDbName(), pgInstanceProcess.getDatabaseDir());

        final AbstractPostgresConfig.Credentials creds = new AbstractPostgresConfig.Credentials(
                pgInstanceProcess.getUserName(), pgInstanceProcess.getPassword());

        final IVersion version = getVersion(pgInstanceProcess);

        final PostgresConfig config = new PostgresConfig(version, getNet(pgInstanceProcess), storage,
                new AbstractPostgresConfig.Timeout(), creds);

        config.getAdditionalInitDbParams()
                .addAll(asList("-E", "UTF-8", "--locale=en_US.UTF-8", "--lc-collate=en_US.UTF-8",
                        "--lc-ctype=en_US.UTF-8"));

        return config;
    }

    private AbstractPostgresConfig.Net getNet(PgInstanceProcess pgInstanceProcess) throws IOException {
        int port = Integer.parseInt(pgInstanceProcess.getPgPort());
        return new AbstractPostgresConfig.Net(getLocalHost().getHostAddress(), port);
    }

    private IVersion getVersion(final PgInstanceProcess pgInstanceProcess) {
        return PgVersion.get(pgInstanceProcess.getPgServerVersion());
    }

}
