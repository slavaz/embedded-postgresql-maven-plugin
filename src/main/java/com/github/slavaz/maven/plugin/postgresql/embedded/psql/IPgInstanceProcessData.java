package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

public interface IPgInstanceProcessData {
    String getPgServerVersion();

    void setPgServerVersion(String pgServerVersion);

    String getDbName();

    void setDbName(String dbName);

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);

    String getPgDatabaseDir();

    void setPgDatabaseDir(String pgDatabaseDir);

    int getPgPort();

    void setPgPort(int pgPort);

    String getPgLocale();

    void setPgLocale(String pgLocale);

    String getPgCharset();

    void setPgCharset(String pgCharset);
}
