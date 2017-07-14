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
    protected Object[][] getVersionsDataSource() {
        // @formatter:off
        return new Object[][] {
                { null, Version.V9_6_2 },
                {"latest", Version.V9_6_2 },
                {"default", Version.V9_6_2 },
                {"9.4", Version.V9_4_10},
                {"9.4.10", Version.V9_4_10},
                {"9.5", Version.V9_5_5},
                {"9.5.5", Version.V9_5_5},
                {"9.6", Version.V9_6_2},
                {"9.6.2", Version.V9_6_2},
                {"bla-bla-bla", Version.V9_6_2 },

        };
        // @formatter:on
    }

    @Test(dataProvider = "get_DataProvider")
    public void getTest(final String inputAlias, final IVersion expectedResult) {

        final IVersion actualResult = PgVersion.get(inputAlias);

        assertEquals(expectedResult, actualResult);
    }
}
