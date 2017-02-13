package com.github.slavaz.maven.plugin.postgresql.embedded.goals;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Created by slavaz on 13/02/17.
 */
public abstract class AbstractGoalMojo extends AbstractMojo {

    @Parameter(defaultValue = "false")
    private boolean skipGoal;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipGoal) {
            getLog().debug("Skipped.");
        } else {
            doExecute();
        }
    }

    protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
}
