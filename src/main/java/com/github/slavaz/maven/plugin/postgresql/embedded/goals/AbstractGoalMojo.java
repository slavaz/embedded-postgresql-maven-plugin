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

/**
 * Created by slavaz on 13/02/17.
 */
public abstract class AbstractGoalMojo extends AbstractMojo {

	protected static final String SP_HTTP_PROXY_HOST = "http.proxyHost";

	protected static final String SP_HTTP_PROXY_PORT = "http.proxyPort";

	protected static final String SP_HTTP_NO_PROXY = "http.nonProxyHosts";

	protected static final String PROTOCOL_HTTP = "http";

	@Parameter(defaultValue = "false")
	private boolean skipGoal;

	@Parameter(defaultValue = "${settings}", readonly = true)
	protected Settings settings;

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (skipGoal) {
			getLog().debug("Skipped.");
		} else {
			doExecute();
		}
	}

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		handleProxyConfigurjation();
	}

	/**
	 * Thie method sets the proper system properties to force http connections to use proxy when downloading Postgres binaries
	 * If system property http.proxyHost is already set, the proxies defined on settings are not taken in consideration
	 * Authentication is taken in consideration using {@link Authenticator#setDefault(Authenticator)}
	 */
	protected void handleProxyConfigurjation() {
		if (settings != null && StringUtils.isBlank(System.getProperty(SP_HTTP_PROXY_HOST))
				&& CollectionUtils.isNotEmpty(settings.getProxies())) {

			Proxy proxy = IterableUtils.find(settings.getProxies(), (Predicate<? super Proxy>) (Proxy item) -> {
				return item.isActive() && (PROTOCOL_HTTP.equalsIgnoreCase(item.getProtocol())
						|| StringUtils.isBlank(item.getProtocol()));
			});
			

			if (proxy != null && StringUtils.isNotBlank(proxy.getHost()) && proxy.getPort() > 0){
				System.setProperty(SP_HTTP_PROXY_HOST, proxy.getHost());
				System.setProperty(SP_HTTP_PROXY_PORT, String.valueOf(proxy.getPort()));

				getLog().info(String.format("Proxy settings detected:  %s:%s", proxy.getHost(), proxy.getPort()));

				if(StringUtils.isNotBlank(proxy.getNonProxyHosts())) {
					System.setProperty(SP_HTTP_NO_PROXY, proxy.getNonProxyHosts());
					getLog().info(String.format("No proxy:  %s", proxy.getNonProxyHosts()));
				}

				if (StringUtils.isNotBlank(proxy.getUsername())) {
					Authenticator.setDefault(new Authenticator() {
						@Override
						public PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(proxy.getUsername(), proxy.getPassword().toCharArray());
						}
					});

					getLog().info(
							String.format("Proxy settings authentication detected. %s:*****", proxy.getUsername()));

				}
			}

		}

	}
}
