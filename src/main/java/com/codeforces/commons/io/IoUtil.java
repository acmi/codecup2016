/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io;

import com.codeforces.commons.io.LimitedByteArrayOutputStream;
import com.codeforces.commons.math.NumberUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class IoUtil {
    public static final int BUFFER_SIZE = NumberUtil.toInt(0x100000);

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        return IoUtil.toByteArray(inputStream, Integer.MAX_VALUE);
    }

    public static byte[] toByteArray(InputStream inputStream, int n2) throws IOException {
        return IoUtil.toByteArray(inputStream, n2, true);
    }

    public static byte[] toByteArray(InputStream inputStream, int n2, boolean bl) throws IOException {
        LimitedByteArrayOutputStream limitedByteArrayOutputStream = new LimitedByteArrayOutputStream(n2, bl);
        IoUtil.copy(inputStream, limitedByteArrayOutputStream, true, true);
        return limitedByteArrayOutputStream.toByteArray();
    }

    public static long copy(InputStream inputStream, OutputStream outputStream, boolean bl, boolean bl2, int n2) throws IOException {
        try {
            long l2 = IOUtils.copyLarge(inputStream, outputStream, 0, n2, new byte[BUFFER_SIZE]);
            if (bl) {
                inputStream.close();
            }
            if (bl2) {
                outputStream.close();
            }
            return l2;
        }
        catch (IOException iOException) {
            if (bl) {
                IoUtil.closeQuietly((AutoCloseable)inputStream);
            }
            if (bl2) {
                IoUtil.closeQuietly((AutoCloseable)outputStream);
            }
            throw iOException;
        }
    }

    public static long copy(InputStream inputStream, OutputStream outputStream, boolean bl, boolean bl2) throws IOException {
        return IoUtil.copy(inputStream, outputStream, bl, bl2, Integer.MAX_VALUE);
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        return IoUtil.copy(inputStream, outputStream, true, false);
    }

    public static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static /* varargs */ void closeQuietly(AutoCloseable ... arrautoCloseable) {
        for (AutoCloseable autoCloseable : arrautoCloseable) {
            IoUtil.closeQuietly(autoCloseable);
        }
    }
}

