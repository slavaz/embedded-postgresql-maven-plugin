package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import com.github.slavaz.utils.ChangeCharset;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Locale;

import static java.util.Arrays.asList;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by slavaz on 28/02/17.
 */
public class PgInstanceManagerTest {

    @Test
    public void CharsetParametersListTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Locale.setDefault(Locale.US);
        ChangeCharset.to("windows-1252");

        final PgInstanceManager.CharsetParametersList charsetParametersList =
                new PgInstanceManager.CharsetParametersList();

        // when
        final Collection actualResult = charsetParametersList.get();

        // then
        final Collection<String> expectedResult = asList("-E", "windows-1252", "--locale=en_US.windows-1252",
                "--lc-collate=en_US.windows-1252", "--lc-ctype=en_US.windows-1252");

        assertEquals(expectedResult, actualResult);
    }
}
