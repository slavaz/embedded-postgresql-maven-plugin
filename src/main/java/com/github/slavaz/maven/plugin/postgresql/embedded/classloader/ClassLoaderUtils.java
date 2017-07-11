package com.github.slavaz.maven.plugin.postgresql.embedded.classloader;

import org.apache.commons.lang3.Validate;
import org.apache.maven.artifact.Artifact;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ClassLoaderUtils {

    private ClassLoaderUtils() {
    }

    public static ClassLoader buildClassLoader(List<Artifact> artifacts) {
        Validate.notEmpty(artifacts);
        return new URLClassLoader(artifacts.stream()
                .map(Artifact::getFile)
                .map(File::toURI)
                .map(ClassLoaderUtils::uriToURL)
                .toArray(URL[]::new));
    }

    private static URL uriToURL(URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + uri, e);
        }
    }
}
