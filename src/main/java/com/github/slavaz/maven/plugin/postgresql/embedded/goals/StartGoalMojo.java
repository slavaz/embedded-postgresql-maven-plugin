package com.github.slavaz.maven.plugin.postgresql.embedded.goals;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.IPgInstanceProcessData;
import com.github.slavaz.maven.plugin.postgresql.embedded.psql.PgInstanceProcessData;
import com.github.slavaz.maven.plugin.postgresql.embedded.psql.PgInstanceManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/**
 * Created by slavaz on 13/02/17.
 */
@Mojo(name = "start")
public class StartGoalMojo extends AbstractGoalMojo {

    @Parameter(defaultValue = "${project.build.directory}")
    private String projectBuildDir;

    @Parameter(defaultValue = "latest", property = "pgVersion", required = true)
    private String pgServerVersion;

    @Parameter(property = "pgDatabasedir", required = false)
    private String pgDatabaseDir;

    @Parameter(property = "dbname", required = true)
    private String dbName;

    @Parameter(defaultValue = "postgres", property = "username", required = true)
    private String userName;

    @Parameter(defaultValue = "postgres", required = true)
    private String password;

    @Parameter(property = "pgLocale")
    private String pgLocale;

    @Parameter(property = "pgCharset")
    private String pgCharset;

    @Parameter(defaultValue = "5432", property = "pgPort", required = true)
    private int pgServerPort;

    protected void doExecute() throws MojoExecutionException, MojoFailureException {

        try {
            getLog().info("Starting PostgreSQL...");
            calculateDatabaseDir();
            initPgInstanceProcess();
            new PgInstanceManager().start();
            getLog().debug("PostgreSQL started.");
        } catch (IOException e) {
            getLog().error("Failed to start PostgreSQL", e);
        }
    }

    private void calculateDatabaseDir() {
        if (StringUtils.isEmpty(pgDatabaseDir)) {
            pgDatabaseDir = projectBuildDir + File.separator + "pgdata";
        }
    }

    private void initPgInstanceProcess() {
        final IPgInstanceProcessData pgInstanceProcessData = PgInstanceProcessData.getInstance();

        pgInstanceProcessData.setPgServerVersion(pgServerVersion);
        pgInstanceProcessData.setPgPort(pgServerPort);
        pgInstanceProcessData.setPgDatabaseDir(pgDatabaseDir);
        pgInstanceProcessData.setDbName(dbName);
        pgInstanceProcessData.setUserName(userName);
        pgInstanceProcessData.setPassword(password);
        pgInstanceProcessData.setPgLocale(pgLocale);
        pgInstanceProcessData.setPgCharset(pgCharset);
    }
}
