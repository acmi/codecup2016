/*
 * Decompiled with CFR 0_119.
 */
package com.a.b;

import com.a.b.a;
import com.a.b.a.a.a.b;
import com.google.inject.Guice;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class c
implements Runnable {
    private static final Logger a = LoggerFactory.getLogger(c.class);
    private final String[] b;

    public c(String[] arrstring) {
        this.b = arrstring;
    }

    @Override
    public void run() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            a.error("Got unexpected exception in thread '" + thread + "'.", throwable);
            throwable.printStackTrace();
        }
        );
        b b2 = null;
        try {
            b2 = new b(this.b);
            b.a(b2);
            a a2 = Guice.createInjector(new com.a.b.a.a.a.a()).getInstance(a.class);
            try {
                a2.a(b2);
                a2.a();
            }
            finally {
                a2.b();
            }
        }
        catch (RuntimeException runtimeException) {
            a.error("Got unexpected game exception.", runtimeException);
            runtimeException.printStackTrace();
            if (b2 == null) {
                return;
            }
            File file = b2.h();
            if (file == null) {
                return;
            }
            try {
                String string = "FAILED\n" + ExceptionUtils.getStackTrace(runtimeException) + '\n';
                FileUtils.writeByteArrayToFile(file, string.getBytes(StandardCharsets.UTF_8));
            }
            catch (IOException iOException) {
                a.error(String.format("Can't write results to file '%s'.", file.getPath()), iOException);
            }
        }
    }
}

