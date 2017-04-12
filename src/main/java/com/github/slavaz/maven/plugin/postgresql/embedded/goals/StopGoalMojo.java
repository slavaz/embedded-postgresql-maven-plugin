package com.github.slavaz.maven.plugin.postgresql.embedded.goals;

import com.github.slavaz.maven.plugin.postgresql.embedded.psql.PgInstanceManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Created by slavaz on 13/02/17.
 */
@Mojo(name = "stop")
public class StopGoalMojo extends AbstractGoalMojo {
    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        try {
            getLog().info("Stopping PostgreSQL...");
            new PgInstanceManager().stop();
            getLog().debug("PostgreSQL stopped.");
        } catch (Exception e) {
            getLog().error("Failed to stop PostgreSQL", e);
        }
    }
    
    
}
