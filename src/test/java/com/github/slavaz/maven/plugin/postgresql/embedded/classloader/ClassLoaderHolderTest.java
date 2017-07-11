package com.github.slavaz.maven.plugin.postgresql.embedded.classloader;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

public class ClassLoaderHolderTest {

    private ClassLoader classLoader;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<ClassLoader> actualClassLoader;

    @BeforeMethod
    public void setUp() throws Exception {
        resetClassLoaderHelper();
    }

    @Test
    public void happyPath_setClassLoader() {
        given_aClassLoader();

        when_setClassLoaderCalled();

        then_theClassLoaderShouldBeSet();
    }

    @Test(expectedExceptions = {
            NullPointerException.class }, expectedExceptionsMessageRegExp = "The validated object is null")
    public void unhappyPath_classLoaderIsNull() {
        given_noClassLoader();

        when_setClassLoaderCalled();

        then_anExceptionShouldBeRaised();
    }

    @Test(expectedExceptions = {
            IllegalStateException.class }, expectedExceptionsMessageRegExp = "ClassLoader instance already set")
    public void unhappyPath_setClassLoaderCalledTwice() {

        given_aClassLoader();

        when_setClassLoaderCalledTwice();

        then_anExceptionShouldBeRaised();
    }

    private void when_setClassLoaderCalledTwice() {
        when_setClassLoaderCalled();
        when_setClassLoaderCalled();
    }

    @Test
    public void unhappyPath_noClassLoader() {

        when_getClassLoaderCalled();

        then_emptyClassLoaderShouldBeReturned();
    }

    private void then_emptyClassLoaderShouldBeReturned() {
        assertFalse(actualClassLoader.isPresent());
    }

    private void when_getClassLoaderCalled() {
        actualClassLoader = ClassLoaderHolder.getClassLoader();
    }

    private void then_anExceptionShouldBeRaised() {
        fail("An exception should be raised");
    }

    private void given_noClassLoader() {
        classLoader = null;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void then_theClassLoaderShouldBeSet() {
        assertSame(ClassLoaderHolder.getClassLoader()
                .get(), classLoader);
    }

    private void when_setClassLoaderCalled() {
        ClassLoaderHolder.setClassLoader(classLoader);
    }

    private void given_aClassLoader() {
        classLoader = mock(ClassLoader.class);
    }

    private void resetClassLoaderHelper() throws NoSuchFieldException, IllegalAccessException {
        final Field field = ClassLoaderHolder.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, Optional.empty());
    }
}
