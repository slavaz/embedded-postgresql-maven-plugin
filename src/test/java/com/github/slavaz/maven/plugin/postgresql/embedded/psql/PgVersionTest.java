package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import de.flapdoodle.embed.process.distribution.IVersion;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import static org.testng.AssertJUnit.assertEquals;


/**
 * Created by slavaz on 07/07/17.
 */
public class PgVersionTest {

    @DataProvider(name = "get_DataProvider")
    protected Object[][] LocaleCharsetDataSource() {
        // @formatter:off
        return new Object[][] {
                { null, Version.V9_6_1 },
                {"latest", Version.V9_6_1 },
                {"default", Version.V9_6_1 },
                {"9.3", Version.V9_3_15},
                {"9.3.15", Version.V9_3_15},
                {"9.4", Version.V9_4_10},
                {"9.4.10", Version.V9_4_10},
                {"9.5", Version.V9_5_5},
                {"9.5.5", Version.V9_5_5},
                {"9.6", Version.V9_6_1},
                {"9.6.1", Version.V9_6_1},
                {"bla-bla-bla", Version.V9_6_1 },

        };
        // @formatter:on
    }

    @Test(dataProvider = "get_DataProvider")
    public void getTest(final String inputAlias, final IVersion expectedResult) {

        final IVersion actualResult = PgVersion.get(inputAlias);

        assertEquals(expectedResult, actualResult);
    }
}
