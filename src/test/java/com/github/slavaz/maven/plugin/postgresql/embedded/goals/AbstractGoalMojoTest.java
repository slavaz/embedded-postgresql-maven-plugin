package com.github.slavaz.maven.plugin.postgresql.embedded.goals;

import static org.junit.Assert.*;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Settings;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractGoalMojoTest {
	
	public AbstractGoalMojo mojo;
	
	@Before
	public void init() {
		this.mojo = Mockito.mock(AbstractGoalMojo.class, Mockito.CALLS_REAL_METHODS);
		System.clearProperty("http.proxyHost");
		System.clearProperty("http.proxyPort");
		System.clearProperty("http.nonProxyHosts");
		Authenticator.setDefault(null);
	}

	@Test
	public void testHandleProxyConfigurjation() throws UnknownHostException {
		
		// No settings
		this.mojo.handleProxyConfigurjation();
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// No proxyies
		this.mojo.settings = new Settings();
		this.mojo.handleProxyConfigurjation();
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// Defaul proxy: port=8080, active=true, host=null
		this.mojo.settings.setProxies(Arrays.asList(new Proxy()));
		this.mojo.handleProxyConfigurjation();
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// Proxy not active
		this.mojo.settings.getProxies().get(0).setHost("theproxy");
		this.mojo.settings.getProxies().get(0).setPort(9090);
		this.mojo.settings.getProxies().get(0).setActive(false);
		this.mojo.settings.getProxies().get(0).setNonProxyHosts("localhost");
		this.mojo.handleProxyConfigurjation();
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// Proxy active but protocol != http
		this.mojo.settings.getProxies().get(0).setProtocol("ftp");
		this.mojo.settings.getProxies().get(0).setActive(true);
		this.mojo.handleProxyConfigurjation();
		assertNullSystemProperties();
		assertNullAuthenticator();
		
		// Proxy active with no autentication
		this.mojo.settings.getProxies().get(0).setProtocol("http");
		this.mojo.handleProxyConfigurjation();
		assertSystemProperties("theproxy", 9090, "localhost");
		assertNullAuthenticator();
	}

	@Test
	public void testHandleProxyConfigurjationAuthentication() throws UnknownHostException {
		this.mojo.settings = new Settings();
		Proxy proxy = new Proxy();
		proxy.setHost("thehostbysettings");
		proxy.setPort(9090);
		proxy.setProtocol("http");
		proxy.setUsername("theuser");
		proxy.setPassword("thepassword");
		proxy.setActive(true);
		this.mojo.settings.addProxy(proxy);		
		
		this.mojo.handleProxyConfigurjation();
		assertSystemProperties("thehostbysettings", 9090, null);
		assertAuthenticator();
	}

		
	@Test
	public void testHandleProxyConfigurjationSystemPropertyExists() throws UnknownHostException {
		System.setProperty("http.proxyHost", "thehostbyproperty");
		System.setProperty("http.proxyPort", "9191");
		
		this.mojo.settings = new Settings();
		Proxy proxy = new Proxy();
		proxy.setHost("thehostbysettings");
		proxy.setPort(9090);
		proxy.setProtocol("http");
		proxy.setUsername("theuser");
		proxy.setPassword("thepassword");
		proxy.setActive(true);
		this.mojo.settings.addProxy(proxy);
		
		this.mojo.settings.setProxies(Arrays.asList(new Proxy()));
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
