package com.github.slavaz.maven.plugin.postgresql.embedded.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.UnknownHostException;
import java.util.Collections;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Settings;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProxyUtilsTest {

    @Mock
    private static Log logger;

    private Settings settings;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");
        System.clearProperty("http.nonProxyHosts");
        Authenticator.setDefault(null);

        settings = new Settings();
    }

    @Test
    public void happyPath_withoutAuth() throws UnknownHostException {

        given_aProxy();

        when_handleProxyConfigurjationCalled();

        then_systemPropertyProxyHostnameIsInstalled();
        then_systemPropertyProxyPortIsInstalled();
        then_systemPropertyProxyExclusionsIsInstalled();
    }

    @Test
    public void happyPath_withAuthInSettings() throws UnknownHostException {

        given_aProxy();
        given_theProxyWithAuthInfo();

        when_handleProxyConfigurjationCalled();

        then_systemPropertyProxyHostnameIsInstalled();
        then_systemPropertyProxyPortIsInstalled();
        then_systemPropertyProxyExclusionsIsInstalled();
        then_authenticatorIsInstalled();
    }

    @Test
    public void happyPath_withAuthInSystemProperties() throws UnknownHostException {

        given_aProxy();
        given_systemPropertiesWithAuthInfo();

        when_handleProxyConfigurjationCalled();

        then_systemPropertyProxyHostnameIsInstalled();
        then_systemPropertyProxyPortIsInstalled();
        then_systemPropertyProxyExclusionsIsNotInstalled();
        then_authenticatorIsNotInstalled();
    }

    @Test
    public void unhappyPath_settingsIsNull() throws UnknownHostException {

        given_noSettings();

        when_handleProxyConfigurjationCalled();

        then_noSystemPropertiesTouched();
        then_authenticatorIsNotInstalled();
    }

    @Test
    public void unhappyPath_noProxiesSpecified() throws UnknownHostException {

        given_noProxy();

        when_handleProxyConfigurjationCalled();

        then_noSystemPropertiesTouched();
        then_authenticatorIsNotInstalled();
    }

    @Test
    public void unhappyPath_nonActiveProxy() throws UnknownHostException {

        given_aProxy();
        given_theProxyIsNotActive();

        when_handleProxyConfigurjationCalled();

        then_noSystemPropertiesTouched();
        then_authenticatorIsNotInstalled();
    }

    @Test
    public void unhappyPath_ftpProxy() throws UnknownHostException {

        given_aProxy();
        given_theProxyIsFtp();

        when_handleProxyConfigurjationCalled();

        then_noSystemPropertiesTouched();
        then_authenticatorIsNotInstalled();
    }

    private void then_systemPropertyProxyExclusionsIsNotInstalled() {
        assertNull(System.getProperty("http.nonProxyHosts"));
    }

    private void given_systemPropertiesWithAuthInfo() {
        System.setProperty("http.proxyHost", "theproxy");
        System.setProperty("http.proxyPort", "9090");
    }

    private void then_noSystemPropertiesTouched() {
        assertNull(System.getProperty("http.proxyHost"));
        assertNull(System.getProperty("http.proxyPort"));
        assertNull(System.getProperty("http.nonProxyHosts"));
    }

    private void then_authenticatorIsNotInstalled() throws UnknownHostException {
        assertNull(Authenticator.requestPasswordAuthentication(InetAddress.getByName("localhost"), 8080, "https",
                "the prompt", "the scheme"));
    }

    private void then_authenticatorIsInstalled() throws UnknownHostException {
        final PasswordAuthentication passwordAuthentication = Authenticator.requestPasswordAuthentication(
                InetAddress.getByName("localhost"), 8080, "https", "the prompt", "the scheme");

        assertNotNull(passwordAuthentication);
        assertEquals("theuser", passwordAuthentication.getUserName());
        assertNotNull(passwordAuthentication.getPassword());
    }

    private void when_handleProxyConfigurjationCalled() {
        ProxyUtils.handleProxyConfigurjation(settings, logger);
    }

    private void given_noSettings() {
        settings = null;
    }

    private void given_theProxyIsFtp() {
        getProxyFromSettings().setProtocol("ftp");
    }

    private void given_theProxyIsNotActive() {
        getProxyFromSettings().setActive(false);
    }

    private void given_aProxy() {
        final Proxy proxy = new Proxy();

        proxy.setHost("theproxy");
        proxy.setPort(9090);
        proxy.setActive(true);
        proxy.setProtocol("http");
        proxy.setNonProxyHosts("localhost");

        settings.setProxies(Collections.singletonList(proxy));
    }

    private void given_noProxy() {
        settings.setProxies(Collections.emptyList());
    }

    private Proxy getProxyFromSettings() {
        return settings.getProxies()
                .get(0);
    }

    private void then_systemPropertyProxyExclusionsIsInstalled() {
        assertEquals("localhost", System.getProperty("http.nonProxyHosts"));
    }

    private void then_systemPropertyProxyPortIsInstalled() {
        assertEquals(9090, Integer.parseInt(System.getProperty("http.proxyPort")));
    }

    private void then_systemPropertyProxyHostnameIsInstalled() {
        assertEquals("theproxy", System.getProperty("http.proxyHost"));
    }

    private void given_theProxyWithAuthInfo() {
        getProxyFromSettings().setUsername("theuser");
        getProxyFromSettings().setPassword("thepassword");
    }
}
