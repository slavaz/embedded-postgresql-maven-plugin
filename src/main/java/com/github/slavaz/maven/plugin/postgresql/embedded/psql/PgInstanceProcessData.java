package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import ru.yandex.qatools.embed.postgresql.PostgresProcess;

/**
 * Created by slavaz on 13/02/17.
 */
public class PgInstanceProcessData implements IPgInstanceProcessData {

    private static PgInstanceProcessData instance;

    private PostgresProcess process;

    private String pgServerVersion;

    private int pgPort;

    private String dbName;

    private String userName;

    private String password;

    private String pgDatabaseDir;

    private String pgLocale;

    private String pgCharset;

    private PgInstanceProcessData() {
    }

    public static PgInstanceProcessData getInstance() {
        if (instance == null) {
            synchronized (PgInstanceProcessData.class) {
                if (instance == null) {
                    instance = new PgInstanceProcessData();
                }
            }
        }
        return instance;
    }

    public PostgresProcess getProcess() {
        return process;
    }

    public void setProcess(PostgresProcess process) {
        this.process = process;
    }

    public String getPgServerVersion() {
        return pgServerVersion;
    }

    public void setPgServerVersion(String pgServerVersion) {
        this.pgServerVersion = pgServerVersion;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPgDatabaseDir() {
        return pgDatabaseDir;
    }

    public void setPgDatabaseDir(String pgDatabaseDir) {
        this.pgDatabaseDir = pgDatabaseDir;
    }

    public int getPgPort() {
        return pgPort;
    }

    public void setPgPort(int pgPort) {
        this.pgPort = pgPort;
    }

    public String getPgLocale() {
        return pgLocale;
    }

    public void setPgLocale(String pgLocale) {
        this.pgLocale = pgLocale;
    }

    public String getPgCharset() {
        return pgCharset;
    }

    public void setPgCharset(String pgCharset) {
        this.pgCharset = pgCharset;
    }
}
