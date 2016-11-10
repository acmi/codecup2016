/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class FileUtils {
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024);
    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
    public static final BigInteger ONE_ZB = BigInteger.valueOf(1024).multiply(BigInteger.valueOf(0x1000000000000000L));
    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
    public static final File[] EMPTY_FILE_ARRAY = new File[0];

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return FileUtils.openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean bl) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File file2 = file.getParentFile();
            if (file2 != null && !file2.mkdirs() && !file2.isDirectory()) {
                throw new IOException("Directory '" + file2 + "' could not be created");
            }
        }
        return new FileOutputStream(file, bl);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try {
            FileUtils.copyToFile(inputStream, file);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copyToFile(InputStream inputStream, File file) throws IOException {
        FileOutputStream fileOutputStream = FileUtils.openOutputStream(file);
        try {
            IOUtils.copy(inputStream, fileOutputStream);
            fileOutputStream.close();
        }
        finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List<String> readLines(File file, Charset charset) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = FileUtils.openInputStream(file);
            List<String> list = IOUtils.readLines(fileInputStream, Charsets.toCharset(charset));
            return list;
        }
        finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public static void writeByteArrayToFile(File file, byte[] arrby) throws IOException {
        FileUtils.writeByteArrayToFile(file, arrby, false);
    }

    public static void writeByteArrayToFile(File file, byte[] arrby, boolean bl) throws IOException {
        FileUtils.writeByteArrayToFile(file, arrby, 0, arrby.length, bl);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void writeByteArrayToFile(File file, byte[] arrby, int n2, int n3, boolean bl) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = FileUtils.openOutputStream(file, bl);
            fileOutputStream.write(arrby, n2, n3);
            fileOutputStream.close();
        }
        finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }
}

