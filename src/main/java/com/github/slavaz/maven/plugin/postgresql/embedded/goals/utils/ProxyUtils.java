package com.github.slavaz.maven.plugin.postgresql.embedded.goals.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Settings;

/**
 * Helper class to handler proxy configuration based on Maven settings
 * 
 * @author  <a href="mailto:gonzalo@arcadiaconsulting.es">Gonzalo Gómez García </a>
 *
 */
public class ProxyUtils {
	
	protected static final String SP_HTTP_PROXY_HOST = "http.proxyHost";

	protected static final String SP_HTTP_PROXY_PORT = "http.proxyPort";

	protected static final String SP_HTTP_NO_PROXY = "http.nonProxyHosts";

	protected static final String PROTOCOL_HTTP = "http";
	
	
	/**
	 * Thie method sets the proper system properties to force http connections to use proxy when downloading Postgres binaries
	 * If system property http.proxyHost is already set, the proxies defined on settings are not taken in consideration
	 * Authentication is taken in consideration using {@link Authenticator#setDefault(Authenticator)}
	 * @param settings Maven settings 
	 * @param logger Maven logger
	 */
	public static void handleProxyConfigurjation(Settings settings, Log logger) {
		if (settings != null && StringUtils.isBlank(System.getProperty(SP_HTTP_PROXY_HOST))
				&& CollectionUtils.isNotEmpty(settings.getProxies())) {

			Proxy proxy = IterableUtils.find(settings.getProxies(), (Predicate<? super Proxy>) (Proxy item) -> {
				return item.isActive() && (PROTOCOL_HTTP.equalsIgnoreCase(item.getProtocol())
						|| StringUtils.isBlank(item.getProtocol()));
			});
			

			if (proxy != null && StringUtils.isNotBlank(proxy.getHost()) && proxy.getPort() > 0){
				System.setProperty(SP_HTTP_PROXY_HOST, proxy.getHost());
				System.setProperty(SP_HTTP_PROXY_PORT, String.valueOf(proxy.getPort()));

				logger.info(String.format("Proxy settings detected:  %s:%s", proxy.getHost(), proxy.getPort()));

				if(StringUtils.isNotBlank(proxy.getNonProxyHosts())) {
					System.setProperty(SP_HTTP_NO_PROXY, proxy.getNonProxyHosts());
					logger.info(String.format("No proxy:  %s", proxy.getNonProxyHosts()));
				}

				if (StringUtils.isNotBlank(proxy.getUsername())) {
					Authenticator.setDefault(new Authenticator() {
						@Override
						public PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(proxy.getUsername(), proxy.getPassword().toCharArray());
						}
					});

					logger.info(
							String.format("Proxy settings authentication detected. %s:*****", proxy.getUsername()));

				}
			}

		} else {
			logger.info(String.format("Using system properties proxy configuration: %s:%s. no proxy: ", 
					System.getProperty(SP_HTTP_PROXY_HOST), System.getProperty(SP_HTTP_PROXY_PORT), System.getProperty(SP_HTTP_NO_PROXY)));
		}

	}
	

}
