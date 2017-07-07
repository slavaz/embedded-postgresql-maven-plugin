package com.github.slavaz.maven.plugin.postgresql.embedded.goals;

import com.github.slavaz.maven.plugin.postgresql.embedded.classloader.ClassLoaderHolder;
import com.github.slavaz.maven.plugin.postgresql.embedded.psql.IsolatedPgInstanceManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.Optional;

/**
 * Created by slavaz on 13/02/17.
 */
@Mojo(name = "stop")
public class StopGoalMojo extends AbstractGoalMojo {
    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        Optional<ClassLoader> classLoader = ClassLoaderHolder.getClassLoader();
        if (classLoader.isPresent()) {
            try {
                getLog().info("Stopping PostgreSQL...");
                new IsolatedPgInstanceManager(classLoader.get()).stop();
                getLog().debug("PostgreSQL stopped.");
            } catch (Exception e) {
                getLog().error("Failed to stop PostgreSQL", e);
            }
        } else {
            getLog().warn("PostgreSQL was not started");
        }
    }
}
