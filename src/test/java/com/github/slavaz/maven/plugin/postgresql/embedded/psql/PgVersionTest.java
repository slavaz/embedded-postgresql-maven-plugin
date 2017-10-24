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
                { null, Version.V10_0 },
                {"latest", Version.V10_0 },
                {"default", Version.V10_0 },
                {"9.5", Version.V9_5_7},
                {"9.5.7", Version.V9_5_7},
                {"9.6", Version.V9_6_5},
                {"9.6.5", Version.V9_6_5},
                {"bla-bla-bla", Version.V10_0 },

        };
        // @formatter:on
    }

    @Test(dataProvider = "get_DataProvider")
    public void getTest(final String inputAlias, final IVersion expectedResult) {

        final IVersion actualResult = PgVersion.get(inputAlias);

        assertEquals(expectedResult, actualResult);
    }
}
