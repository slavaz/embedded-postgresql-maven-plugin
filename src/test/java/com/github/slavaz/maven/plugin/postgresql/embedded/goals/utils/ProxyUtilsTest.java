package com.github.slavaz.maven.plugin.postgresql.embedded.goals.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Settings;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class ProxyUtilsTest {
	
	protected static Log logger;
	
	@BeforeClass
	public static void create() {
		logger = Mockito.mock(Log.class);
		
	}

	@Before
	public void init() {
		System.clearProperty("http.proxyHost");
		System.clearProperty("http.proxyPort");
		System.clearProperty("http.nonProxyHosts");
		Authenticator.setDefault(null);
	}

	@Test
	public void testHandleProxyConfigurjation() throws UnknownHostException {

		Settings settings = null;

		// No settings
		ProxyUtils.handleProxyConfigurjation(settings, logger);
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// No proxyies
		settings = new Settings();
		ProxyUtils.handleProxyConfigurjation(settings, logger);
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// Defaul proxy: port=8080, active=true, host=null
		settings.setProxies(Arrays.asList(new Proxy()));
		ProxyUtils.handleProxyConfigurjation(settings, logger);
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// Proxy not active
		settings.getProxies().get(0).setHost("theproxy");
		settings.getProxies().get(0).setPort(9090);
		settings.getProxies().get(0).setActive(false);
		settings.getProxies().get(0).setNonProxyHosts("localhost");
		ProxyUtils.handleProxyConfigurjation(settings, logger);
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// Proxy active but protocol != http
		settings.getProxies().get(0).setProtocol("ftp");
		settings.getProxies().get(0).setActive(true);
		ProxyUtils.handleProxyConfigurjation(settings, logger);
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// Proxy active with no autentication
		settings.getProxies().get(0).setProtocol("http");
		ProxyUtils.handleProxyConfigurjation(settings, logger);
		assertSystemProperties("theproxy", 9090, "localhost");
		assertNullAuthenticator();
	}

	@Test
	public void testHandleProxyConfigurjationAuthentication() throws UnknownHostException {
		Settings settings = new Settings();
		Proxy proxy = new Proxy();
		proxy.setHost("thehostbysettings");
		proxy.setPort(9090);
		proxy.setProtocol("http");
		proxy.setUsername("theuser");
		proxy.setPassword("thepassword");
		proxy.setActive(true);
		settings.addProxy(proxy);		
		
		ProxyUtils.handleProxyConfigurjation(settings, logger);
		assertSystemProperties("thehostbysettings", 9090, null);
		assertAuthenticator();
	}

		
	@Test
	public void testHandleProxyConfigurjationSystemPropertyExists() throws UnknownHostException {
		System.setProperty("http.proxyHost", "thehostbyproperty");
		System.setProperty("http.proxyPort", "9191");
		
		Settings settings = new Settings();
		Proxy proxy = new Proxy();
		proxy.setHost("thehostbysettings");
		proxy.setPort(9090);
		proxy.setProtocol("http");
		proxy.setUsername("theuser");
		proxy.setPassword("thepassword");
		proxy.setActive(true);
		settings.addProxy(proxy);
		
		settings.setProxies(Arrays.asList(new Proxy()));
		assertSystemProperties("thehostbyproperty", 9191, null);
		assertNullAuthenticator();
	}

	
	protected void assertNullSystemProperties() {
		assertNull(System.getProperty("http.proxyHost"));
		assertNull(System.getProperty("http.proxyPort"));
		assertNull(System.getProperty("http.nonProxyHosts"));
	}
	
	protected void assertSystemProperties(String host, int port, String noProxy) {
		assertEquals(host, System.getProperty("http.proxyHost"));
		assertEquals(port, Integer.parseInt(System.getProperty("http.proxyPort")));
		assertEquals(noProxy, System.getProperty("http.nonProxyHosts"));
		
	}
	
	protected void assertNullAuthenticator() throws UnknownHostException {
		assertNull(Authenticator.requestPasswordAuthentication(InetAddress.getByName("localhost"), 8080, "https", "the prompt", "the scheme"));
	}
	
	protected void assertAuthenticator() throws UnknownHostException {
		assertNotNull(Authenticator.requestPasswordAuthentication(InetAddress.getByName("localhost"), 8080, "https", "the prompt", "the scheme"));
	}


}
