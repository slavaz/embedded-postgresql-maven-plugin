package com.github.slavaz.maven.plugin.postgresql.embedded.classloader;

import org.apache.maven.artifact.Artifact;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

public class ClassLoaderUtilsTest {

    private final String EXPECTED_EXCEPTION_MESSAGE = "The validated collection is empty";

    private List<Artifact> artifacts;

    private ClassLoader actualResult;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        actualResult = null;
    }

    @Test
    public void happyPath_oneArtifact() throws URISyntaxException {

        given_artifactList();
        given_anArtifactIntoList("file:///");

        when_buildClassLoaderCalled();

        then_resultShouldNotNull();

        then_ClassPathCountShouldBe(1);
        then_ClassPathShouldContain("file:/");
    }

    @Test
    public void happyPath_fewArtifacts() throws URISyntaxException {

        given_artifactList();
        given_anArtifactIntoList("file:///");
        given_anArtifactIntoList("http://127.0.0.1");

        when_buildClassLoaderCalled();

        then_resultShouldNotNull();

        then_ClassPathCountShouldBe(2);
        then_ClassPathShouldContain("file:/");
        then_ClassPathShouldContain("http://127.0.0.1");
    }

    @Test(expectedExceptions = {
            NullPointerException.class }, expectedExceptionsMessageRegExp = EXPECTED_EXCEPTION_MESSAGE)
    public void unhappyPath_artifactListIsNull() {

        given_artifactListIsNull();

        when_buildClassLoaderCalled();

        then_anExceptionShouldBeRaised();
    }

    @Test(expectedExceptions = {
            IllegalArgumentException.class }, expectedExceptionsMessageRegExp = EXPECTED_EXCEPTION_MESSAGE)
    public void unhappyPath_emptyArtifactList() {

        given_artifactList();

        when_buildClassLoaderCalled();

        then_anExceptionShouldBeRaised();
    }

    @Test(expectedExceptions = {RuntimeException.class}, expectedExceptionsMessageRegExp = "Malformed URL: .*")
    public void unhappyPath_malformedURL() throws URISyntaxException {

        given_artifactList();
        given_anArtifactIntoList("unknown:///");

        when_buildClassLoaderCalled();

        then_anExceptionShouldBeRaised();
    }

    private void given_artifactList() {
        artifacts = new ArrayList<>();
    }

    private void then_anExceptionShouldBeRaised() {
        fail("An exception should be raised");
    }

    private void when_buildClassLoaderCalled() {
        actualResult = ClassLoaderUtils.buildClassLoader(artifacts);
    }

    private void given_artifactListIsNull() {
        artifacts = null;
    }

    private void then_ClassPathCountShouldBe(final long expectedCount) {
        long actualCount = Arrays.stream(((URLClassLoader) actualResult).getURLs())
                .count();
        assertEquals(expectedCount, actualCount);
    }

    private void then_ClassPathShouldContain(final String expectedPath) {
        final String actualPath = Arrays.stream(((URLClassLoader) actualResult).getURLs())
                .map(URL::toString)
                .filter(e -> e.equals(expectedPath))
                .findFirst()
                .orElse(null);

        assertNotNull(actualPath, "Class path should contain " + expectedPath);
    }

    private void then_resultShouldNotNull() {
        assertNotNull(actualResult);
    }

    private void given_anArtifactIntoList(final String path) throws URISyntaxException {
        Artifact artifact = mock(Artifact.class, RETURNS_DEEP_STUBS);
        when(artifact.getFile().toURI()).thenReturn(new URI(path));
        artifacts.add(artifact);
    }
}
