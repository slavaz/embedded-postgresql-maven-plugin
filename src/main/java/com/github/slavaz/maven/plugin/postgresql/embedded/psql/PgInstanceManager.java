package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import de.flapdoodle.embed.process.distribution.IVersion;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Locale;

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
            throws IOException {

        final AbstractPostgresConfig.Storage storage =
                new AbstractPostgresConfig.Storage(pgInstanceProcess.getDbName(), pgInstanceProcess.getDatabaseDir());

        final AbstractPostgresConfig.Credentials creds = new AbstractPostgresConfig.Credentials(
                pgInstanceProcess.getUserName(), pgInstanceProcess.getPassword());

        final IVersion version = getVersion(pgInstanceProcess);

        final PostgresConfig config = new PostgresConfig(version, getNet(pgInstanceProcess), storage,
                new AbstractPostgresConfig.Timeout(), creds);

        config.getAdditionalInitDbParams()
                .addAll(new CharsetParametersList().get());

        return config;
    }

    private AbstractPostgresConfig.Net getNet(PgInstanceProcess pgInstanceProcess) throws IOException {

        return new AbstractPostgresConfig.Net(getLocalHost().getHostAddress(), pgInstanceProcess.getPgPort());
    }

    private IVersion getVersion(final PgInstanceProcess pgInstanceProcess) {
        return PgVersion.get(pgInstanceProcess.getPgServerVersion());
    }

    static class CharsetParametersList {
        final private Charset defaultCharset;
        final private String localeName;

        CharsetParametersList() {
            defaultCharset = Charset.defaultCharset();
            localeName = Locale.getDefault()
                    .toString() + "." + defaultCharset.name();
        }

        Collection<String> get() {
            return asList("-E", defaultCharset.name(), "--locale=" + localeName, "--lc-collate=" + localeName,
                    "--lc-ctype=" + localeName);
        }
    }
}
