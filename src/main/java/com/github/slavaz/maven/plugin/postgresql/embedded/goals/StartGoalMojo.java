package com.github.slavaz.maven.plugin.postgresql.embedded.goals;

import com.github.slavaz.maven.plugin.postgresql.embedded.classloader.ClassLoaderHolder;
import com.github.slavaz.maven.plugin.postgresql.embedded.classloader.ClassLoaderUtils;
import com.github.slavaz.maven.plugin.postgresql.embedded.psql.IPgInstanceProcessData;
import com.github.slavaz.maven.plugin.postgresql.embedded.psql.IsolatedPgInstanceManager;
import com.github.slavaz.maven.plugin.postgresql.embedded.psql.data.PgInstanceProcessData;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by slavaz on 13/02/17.
 */
@Mojo(name = "start")
public class StartGoalMojo extends AbstractGoalMojo {

    @Parameter(defaultValue = "${project.build.directory}")
    private String projectBuildDir;

    @Parameter(defaultValue = "latest", property = "pgVersion", required = true)
    private String pgServerVersion;

    @Parameter(property = "pgDatabasedir")
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

    @Parameter(property = "pgHost", defaultValue = "localhost")
    private String pgHost;

    @Parameter(defaultValue = "5432", property = "pgPort", required = true)
    private int pgServerPort;

    @Parameter(property = "pgDumpFile")
    private String pgDumpFile;

    @Parameter(readonly = true, defaultValue = "${plugin.artifacts}")
    private List<Artifact> pluginDependencies;

    protected void doExecute() throws MojoExecutionException, MojoFailureException {

        try {
            getLog().info("Starting PostgreSQL...");
            calculateDatabaseDir();

            ClassLoader classLoader = ClassLoaderUtils.buildClassLoader(pluginDependencies);
            ClassLoaderHolder.setClassLoader(classLoader);
            new IsolatedPgInstanceManager(classLoader).start(buildInstanceProcessData());

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

    private IPgInstanceProcessData buildInstanceProcessData() {
        return new PgInstanceProcessData(pgServerVersion, pgHost, pgServerPort,
                dbName, userName, password, pgDatabaseDir, pgLocale, pgCharset, pgDumpFile);
    }
}
