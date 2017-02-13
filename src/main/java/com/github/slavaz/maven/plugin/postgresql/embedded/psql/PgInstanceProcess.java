package com.github.slavaz.maven.plugin.postgresql.embedded.psql;

import ru.yandex.qatools.embed.postgresql.PostgresProcess;

/**
 * Created by slavaz on 13/02/17.
 */
public class PgInstanceProcess {

    private static PgInstanceProcess instance;

    private PostgresProcess process;

    private String pgServerVersion;

    private String dbName;

    private String userName;

    private String password;

    private String databaseDir;

    private PgInstanceProcess() {
    }

    public static PgInstanceProcess getInstance() {
        if (instance == null) {
            synchronized (PgInstanceProcess.class) {
                if (instance == null) {
                    instance = new PgInstanceProcess();
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

    public String getDatabaseDir() {
        return databaseDir;
    }

    public void setDatabaseDir(String databaseDir) {
        this.databaseDir = databaseDir;
    }
}
