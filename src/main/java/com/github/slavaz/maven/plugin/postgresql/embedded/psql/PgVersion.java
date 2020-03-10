package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import de.flapdoodle.embed.process.distribution.IVersion;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import java.util.stream.Stream;

/**
 * Created by slavaz on 13/02/17.
 */
public enum PgVersion {
    V9_4(new String[] { "9.4", "9.4.14"}, VersionEx.V9_4_14),
    V9_5(new String[] { "9.5", "9.5.15" }, Version.V9_5_15),
    V9_6(new String[] { "9.6", "9.6.11" }, Version.V9_6_11),
    V10(new String[] { "10", "10.6" }, Version.V10_6),

    DEFAULT(V10),

    LATEST(new String[] { "default", "latest" }, DEFAULT);

    final private String[] aliases;
    final private IVersion version;

    PgVersion(final PgVersion pgVersion) {
        this.aliases = pgVersion.aliases;
        this.version = pgVersion.version;
    }

    PgVersion(final String[] aliases, final IVersion version) {
        this.aliases = aliases;
        this.version = version;
    }

    PgVersion(final String[] aliases, final PgVersion pgVersion) {
        this.aliases = aliases;
        this.version = pgVersion.version;
    }

    static public IVersion get(final String alias) {
        return Stream.of(PgVersion.values())
                .filter(pgVersion -> Stream.of(pgVersion.aliases)
                        .filter(pgAlias -> pgAlias.equals(alias))
                        .findFirst()
                        .isPresent()
                )
                .findFirst()
                .orElse(DEFAULT)
                .version;
    }
}
