package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import de.flapdoodle.embed.process.distribution.IVersion;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import java.util.stream.Stream;

/**
 * Created by slavaz on 13/02/17.
 */
public enum PgVersion {
    V9_3(new String[] { "9.3", "9.3.15" }, Version.V9_3_15),
    V9_4(new String[] { "9.4", "9.4.10" }, Version.V9_4_10),
    V9_5(new String[] { "9.5", "9.5.5" }, Version.V9_5_5),
    V9_6(new String[] { "9.6", "9.6.1" }, Version.V9_6_1),

    DEFAULT(V9_6),

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
