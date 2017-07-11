package com.github.slavaz.maven.plugin.postgresql.embedded.psql.data;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.IPgInstanceProcessData;

/**
 * Created by slavaz on 13/02/17.
 */
public class PgInstanceProcessData implements IPgInstanceProcessData {
    private String pgServerVersion;

    private int pgPort;

    private String dbName;

    private String userName;

    private String password;

    private String pgDatabaseDir;

    private String pgLocale;

    private String pgCharset;

    public PgInstanceProcessData(String pgServerVersion, int pgPort, String dbName, String userName, String password, String pgDatabaseDir, String pgLocale, String pgCharset) {
        this.pgServerVersion = pgServerVersion;
        this.pgPort = pgPort;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
        this.pgDatabaseDir = pgDatabaseDir;
        this.pgLocale = pgLocale;
        this.pgCharset = pgCharset;
    }

    public PgInstanceProcessData() {
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
