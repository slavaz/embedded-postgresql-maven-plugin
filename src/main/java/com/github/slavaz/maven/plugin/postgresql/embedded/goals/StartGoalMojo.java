package com.github.slavaz.maven.plugin.postgresql.embedded.goals;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.PgInstanceProcess;
import com.github.slavaz.maven.plugin.postgresql.embedded.psql.PgInstanceManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

/**
 * Created by slavaz on 13/02/17.
 */
@Mojo(name = "start")
public class StartGoalMojo extends AbstractGoalMojo {


    @Parameter(defaultValue = "latest", property = "pgVersion", required = true)
    private String pgServerVersion;

    @Parameter(defaultValue = "${project.build.directory}/pgdata", property = "databasedir", required = true)
    private String databaseDir;

    @Parameter(property = "dbname", required = true)
    private String dbName;

    @Parameter(defaultValue = "postgres", property = "username", required = true)
    private String userName;

    @Parameter(defaultValue = "postgres", required = true)
    private String password;

    protected void doExecute() throws MojoExecutionException, MojoFailureException {

        try {
            getLog().info("Starting PostgreSQL...");
            initPgInstanceProcess();
            new PgInstanceManager().start();
            getLog().debug("PostgreSQL started.");
        } catch (IOException e) {
            getLog().error("Failed to start PostgreSQL", e);
        }
    }

    private void initPgInstanceProcess() {
        final PgInstanceProcess pgInstanceProcess = PgInstanceProcess.getInstance();

        pgInstanceProcess.setPgServerVersion(pgServerVersion);
        pgInstanceProcess.setDatabaseDir(databaseDir);
        pgInstanceProcess.setDbName(dbName);
        pgInstanceProcess.setUserName(userName);
        pgInstanceProcess.setPassword(password);
    }
}
