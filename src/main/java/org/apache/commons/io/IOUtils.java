/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.output.StringBuilderWriter;

public class IOUtils {
    public static final char DIR_SEPARATOR = File.separatorChar;
    public static final String LINE_SEPARATOR;
    private static byte[] SKIP_BYTE_BUFFER;

    public static void closeQuietly(InputStream inputStream) {
        IOUtils.closeQuietly((Closeable)inputStream);
    }

    public static void closeQuietly(OutputStream outputStream) {
        IOUtils.closeQuietly((Closeable)outputStream);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
    }

    public static byte[] toByteArray(InputStream inputStream, int n2) throws IOException {
        int n3;
        int n4;
        if (n2 < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + n2);
        }
        if (n2 == 0) {
            return new byte[0];
        }
        byte[] arrby = new byte[n2];
        for (n4 = 0; n4 < n2 && (n3 = inputStream.read(arrby, n4, n2 - n4)) != -1; n4 += n3) {
        }
        if (n4 != n2) {
            throw new IOException("Unexpected readed size. current: " + n4 + ", excepted: " + n2);
        }
        return arrby;
    }

    public static List<String> readLines(InputStream inputStream, Charset charset) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.toCharset(charset));
        return IOUtils.readLines(inputStreamReader);
    }

    public static List<String> readLines(Reader reader) throws IOException {
        BufferedReader bufferedReader = IOUtils.toBufferedReader(reader);
        ArrayList<String> arrayList = new ArrayList<String>();
        String string = bufferedReader.readLine();
        while (string != null) {
            arrayList.add(string);
            string = bufferedReader.readLine();
        }
        return arrayList;
    }

    public static int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        long l2 = IOUtils.copyLarge(inputStream, outputStream);
        if (l2 > Integer.MAX_VALUE) {
            return -1;
        }
        return (int)l2;
    }

    public static long copy(InputStream inputStream, OutputStream outputStream, int n2) throws IOException {
        return IOUtils.copyLarge(inputStream, outputStream, new byte[n2]);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream) throws IOException {
        return IOUtils.copy(inputStream, outputStream, 4096);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream, byte[] arrby) throws IOException {
        int n2;
        long l2 = 0;
        while (-1 != (n2 = inputStream.read(arrby))) {
            outputStream.write(arrby, 0, n2);
            l2 += (long)n2;
        }
        return l2;
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream, long l2, long l3, byte[] arrby) throws IOException {
        int n2;
        int n3;
        if (l2 > 0) {
            IOUtils.skipFully(inputStream, l2);
        }
        if (l3 == 0) {
            return 0;
        }
        int n4 = n3 = arrby.length;
        if (l3 > 0 && l3 < (long)n3) {
            n4 = (int)l3;
        }
        long l4 = 0;
        while (n4 > 0 && -1 != (n2 = inputStream.read(arrby, 0, n4))) {
            outputStream.write(arrby, 0, n2);
            l4 += (long)n2;
            if (l3 <= 0) continue;
            n4 = (int)Math.min(l3 - l4, (long)n3);
        }
        return l4;
    }

    public static boolean contentEquals(InputStream inputStream, InputStream inputStream2) throws IOException {
        int n2;
        if (inputStream == inputStream2) {
            return true;
        }
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream);
        }
        if (!(inputStream2 instanceof BufferedInputStream)) {
            inputStream2 = new BufferedInputStream(inputStream2);
        }
        int n3 = inputStream.read();
        while (-1 != n3) {
            n2 = inputStream2.read();
            if (n3 != n2) {
                return false;
            }
            n3 = inputStream.read();
        }
        n2 = inputStream2.read();
        return n2 == -1;
    }

    public static long skip(InputStream inputStream, long l2) throws IOException {
        long l3;
        long l4;
        if (l2 < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + l2);
        }
        if (SKIP_BYTE_BUFFER == null) {
            SKIP_BYTE_BUFFER = new byte[2048];
        }
        for (l3 = l2; l3 > 0 && (l4 = (long)inputStream.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(l3, 2048))) >= 0; l3 -= l4) {
        }
        return l2 - l3;
    }

    public static void skipFully(InputStream inputStream, long l2) throws IOException {
        if (l2 < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + l2);
        }
        long l3 = IOUtils.skip(inputStream, l2);
        if (l3 != l2) {
            throw new EOFException("Bytes to skip: " + l2 + " actual: " + l3);
        }
    }

    public static int read(InputStream inputStream, byte[] arrby, int n2, int n3) throws IOException {
        int n4;
        int n5;
        int n6;
        if (n3 < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + n3);
        }
        for (n5 = n3; n5 > 0 && -1 != (n4 = inputStream.read(arrby, n2 + (n6 = n3 - n5), n5)); n5 -= n4) {
        }
        return n3 - n5;
    }

    public static int read(InputStream inputStream, byte[] arrby) throws IOException {
        return IOUtils.read(inputStream, arrby, 0, arrby.length);
    }

    static {
        StringBuilderWriter stringBuilderWriter = new StringBuilderWriter(4);
        PrintWriter printWriter = new PrintWriter(stringBuilderWriter);
        printWriter.println();
        LINE_SEPARATOR = stringBuilderWriter.toString();
        printWriter.close();
    }
}

