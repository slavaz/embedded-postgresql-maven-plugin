package com.github.slavaz.utils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

/**
 * Created by slavaz on 28/02/17.
 */
public class ChangeCharset {

    static public void to(final String charsetName) throws NoSuchFieldException, IllegalAccessException {
        checkIsSupported(charsetName);
        System.setProperty("file.encoding", charsetName);

        doSomeTrickyHackForApplyNewCharsetInRuntime();
    }

    private static void doSomeTrickyHackForApplyNewCharsetInRuntime()
            throws NoSuchFieldException, IllegalAccessException {

        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null, null);
    }

    private static void checkIsSupported(final String charsetName) {
        if (!Charset.isSupported(charsetName)) {
            throw new IllegalCharsetNameException(
                    "The charset \n" + charsetName + " is not available in the current Java virtual machine");
        }
    }
}
