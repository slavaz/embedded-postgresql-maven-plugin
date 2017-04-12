package com.github.slavaz.maven.plugin.postgresql.embedded.goals;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Settings;

import com.github.slavaz.maven.plugin.postgresql.embedded.goals.utils.ProxyUtils;

/**
 * Created by slavaz on 13/02/17.
 */
public abstract class AbstractGoalMojo extends AbstractMojo {

	@Parameter(defaultValue = "false")
	private boolean skipGoal;

	@Parameter(defaultValue = "${settings}", readonly = true)
	protected Settings settings;

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (skipGoal) {
			getLog().debug("Skipped.");
		} else {
			ProxyUtils.handleProxyConfigurjation(this.settings, getLog());
			doExecute();
		}
	}

	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

}
