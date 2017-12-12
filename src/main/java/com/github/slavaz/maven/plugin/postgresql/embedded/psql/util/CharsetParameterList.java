package com.github.slavaz.maven.plugin.postgresql.embedded.psql.util;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.IPgInstanceProcessData;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

class CharsetParameterList {
    private final static String NO_PARAMETERS = "no";

    private final String charsetName;
    private final String localeName;

    CharsetParameterList(final IPgInstanceProcessData pgInstanceProcess) {
        charsetName = calculateCharset(pgInstanceProcess);
        localeName = calculateLocale(pgInstanceProcess);
    }

    Collection<String> get() {
        if (NO_PARAMETERS.equals(localeName) || NO_PARAMETERS.equals(charsetName)) {
            return emptyList();
        }

        final String lc = localeName + "." + charsetName;
        return asList("-E", charsetName, "--locale=" + lc, "--lc-collate=" + lc, "--lc-ctype=" + lc);
    }

    private String calculateCharset(final IPgInstanceProcessData iPgInstanceProcessData) {
        if (StringUtils.isEmpty(iPgInstanceProcessData.getPgCharset())) {
            return Charset.defaultCharset()
                    .name();
        }
        return iPgInstanceProcessData.getPgCharset();
    }

    private String calculateLocale(final IPgInstanceProcessData iPgInstanceProcessData) {
        if (StringUtils.isEmpty(iPgInstanceProcessData.getPgLocale())) {
            return Locale.getDefault()
                    .toString();
        }
        return iPgInstanceProcessData.getPgLocale();
    }

}
