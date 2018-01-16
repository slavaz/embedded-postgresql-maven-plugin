package com.github.slavaz.maven.plugin.postgresql.embedded.psql.util;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.IPgInstanceProcessData;
import com.github.slavaz.maven.plugin.postgresql.embedded.psql.data.PgInstanceProcessData;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PostgresConfigUtilTest {

    IPgInstanceProcessData pgInstanceProcessData;
    PostgresConfig postgresConfig;
    PgParameters pgParameters;
    static Path tempDir;

    @BeforeClass
    protected void setUpTempDir() throws IOException {
        tempDir = Files.createTempDirectory("PostgresConfigUtilTest");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileUtils.deleteDirectory(tempDir.toFile());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
    }

    @DataProvider(name = "pgConfigs")
    protected static Object[][] pgConfigs() {
        return new Object[][]{
                new Object[]{new Object[]{
                        "latest", "hostname", 123456, "db", "user", "pass",
                        "locale", "charset"
                }},
                new Object[]{new Object[]{
                        "latest", null, 123456, "db", "user", "pass",
                        "locale", "charset"
                }},
                new Object[]{new Object[]{
                        null, null, 0, null, null, null,
                        null, null
                }},
        };
    }


    @Test(dataProvider = "pgConfigs")
    public void happyPath(final Object[] parameters) throws IOException {
        given_aPgParameters(parameters);
        given_aPgInstanceProcessData();

        when_getConfigCalled();

        then_configShouldBeFilled();
    }

    private void given_aPgParameters(Object[] parameters) {
        pgParameters = new PgParameters(parameters);
    }

    private void given_aPgInstanceProcessData() {
        pgInstanceProcessData = new PgInstanceProcessData(pgParameters.getPgVersion(), pgParameters.getPgHost(),
                pgParameters.getPgPort(), pgParameters.getDbName(), pgParameters.getUserName(), pgParameters
                .getPassword(), tempDir.toString(), pgParameters.getLocale(), pgParameters.getCharset());
    }

    private void then_configShouldBeFilled() {
        then_configShouldContainsPathToDb(tempDir.toFile());
        then_configShouldContainsDbName(pgParameters.getDbName());
        then_configShouldContainsHost(pgParameters.getPgHost());
        then_configShouldContainsPort(pgParameters.getPgPort());
        then_configShouldContainsCredentialsUser(pgParameters.getUserName());
        then_configShouldContainsCredentialsPassword(pgParameters.getPassword());
    }

    private void then_configShouldContainsCredentialsPassword(String password) {
        Assert.assertEquals(postgresConfig.credentials().password(), password);
    }

    private void then_configShouldContainsCredentialsUser(String userName) {
        Assert.assertEquals(postgresConfig.credentials().username(), userName);
    }

    private void then_configShouldContainsPort(int port) {
        Assert.assertEquals(postgresConfig.net().port(), port);
    }

    private void then_configShouldContainsHost(final String hostname) {
        Assert.assertEquals(postgresConfig.net().host(), hostname);
    }

    private void then_configShouldContainsDbName(String dbName) {
        Assert.assertEquals(postgresConfig.storage().dbName(), dbName);
    }

    private void then_configShouldContainsPathToDb(File pathToDb) {
        Assert.assertEquals(postgresConfig.storage().dbDir(), pathToDb);
    }

    private void when_getConfigCalled() throws IOException {
        postgresConfig = PostgresConfigUtil.get(pgInstanceProcessData);
    }

    class PgParameters {
        private final String pgVersion;
        private final String pgHost;
        private final int pgPort;
        private final String dbName;
        private final String userName;
        private final String password;
        private final String locale;
        private final String charset;

        PgParameters(Object[] parameters) {
            pgVersion = (String) parameters[0];
            pgHost = (String) parameters[1];
            pgPort = (int) parameters[2];
            dbName = (String) parameters[3];
            userName = (String) parameters[4];
            password = (String) parameters[5];
            locale = (String) parameters[6];
            charset = (String) parameters[7];
        }

        String getPgVersion() {
            return pgVersion;
        }

        String getPgHost() {
            return pgHost;
        }

        int getPgPort() {
            return pgPort;
        }

        String getDbName() {
            return dbName;
        }

        String getUserName() {
            return userName;
        }

        String getPassword() {
            return password;
        }

        String getLocale() {
            return locale;
        }

        String getCharset() {
            return charset;
        }
    }
}
