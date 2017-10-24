package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import de.flapdoodle.embed.process.distribution.IVersion;

/**
 * Add support for more Postgresql Versions which are not supported by {@code postgres-embedded}.
 *
 * @see ru.yandex.qatools.embed.postgresql.distribution.Version
 * @see IVersion
 */
public enum VersionEx implements IVersion {
    V9_4_14("9.4.14-1");

    private final String specificVersion;

    VersionEx(String vName) {
        this.specificVersion = vName;
    }

    public String asInDownloadPath() {
        return this.specificVersion;
    }

    public String toString() {
        return "Version{" + this.specificVersion + '}';
    }
}

