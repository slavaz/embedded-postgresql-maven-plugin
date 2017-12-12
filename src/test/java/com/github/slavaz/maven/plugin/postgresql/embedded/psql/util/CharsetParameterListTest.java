package com.github.slavaz.maven.plugin.postgresql.embedded.psql.util;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.IPgInstanceProcessData;
import com.github.slavaz.utils.ChangeCharset;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.doReturn;
import static org.testng.Assert.assertEquals;

/**
 * Created by slavaz on 28/02/17.
 */
public class CharsetParameterListTest {

    private static final List<String> SYSTEM_DEFAULT_LIST =
            asList("-E", "UTF-32", "--locale=ja_JP.UTF-32", "--lc-collate=ja_JP.UTF-32", "--lc-ctype=ja_JP.UTF-32");
    @Mock
    private IPgInstanceProcessData pgInstanceProcessData;

    private CharsetParameterList charsetParameterList;

    @BeforeClass
    public static void setUpSystemEnvironment() throws NoSuchFieldException, IllegalAccessException {
        Locale.setDefault(Locale.JAPAN);
        ChangeCharset.to("UTF-32");
    }

    @BeforeMethod
    protected void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void SystemLocaleCharsetTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        charsetParameterList = new CharsetParameterList(pgInstanceProcessData);

        Locale.setDefault(Locale.US);
        ChangeCharset.to("windows-1252");

        // when
        final Collection actualResult = charsetParameterList.get();

        // then
        assertEquals(SYSTEM_DEFAULT_LIST, actualResult);
    }

    @DataProvider(name = "LocaleCharsetDataProvider")
    protected Object[][] LocaleCharsetDataSource() {
        // @formatter:off
        return new Object[][] {
                { null, null, SYSTEM_DEFAULT_LIST },
                { "", "", SYSTEM_DEFAULT_LIST },
                { "en_US", "windows-1252",
                        asList("-E", "windows-1252", "--locale=en_US.windows-1252",
                        "--lc-collate=en_US.windows-1252", "--lc-ctype=en_US.windows-1252") },
                { "no", "no", emptyList() },
                { "no", "", emptyList() },
                { "", "no", emptyList() },

        };
        // @formatter:on
    }

    @Test(dataProvider = "LocaleCharsetDataProvider")
    public void LocaleCharsetTest(final String locale, final String charset, final Collection<String> expectedResult) {
        // given
        doReturn(locale).when(pgInstanceProcessData)
                .getPgLocale();
        doReturn(charset).when(pgInstanceProcessData)
                .getPgCharset();

        charsetParameterList = new CharsetParameterList(pgInstanceProcessData);

        // when
        final Collection actualResult = charsetParameterList.get();

        // then
        assertEquals(expectedResult, actualResult);
    }

}
