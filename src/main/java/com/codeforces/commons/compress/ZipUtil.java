/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.compress;

import com.codeforces.commons.io.FileUtil;
import com.codeforces.commons.io.IoUtil;
import com.codeforces.commons.math.Math;
import com.google.common.primitives.Ints;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import de.schlichtherle.truezip.file.TVFS;
import de.schlichtherle.truezip.fs.FsSyncException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.Deflater;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;

public final class ZipUtil {
    private static final int DEFAULT_BUFFER_SIZE = Ints.checkedCast(0x100000);

    public static byte[] compress(byte[] arrby, int n2) {
        Deflater deflater = new Deflater();
        deflater.setLevel(n2);
        deflater.setInput(arrby);
        deflater.finish();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(arrby.length);
        byte[] arrby2 = new byte[DEFAULT_BUFFER_SIZE];
        while (!deflater.finished()) {
            byteArrayOutputStream.write(arrby2, 0, deflater.deflate(arrby2));
        }
        IoUtil.closeQuietly((AutoCloseable)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void unzip(File file, File file2) throws IOException {
        ZipUtil.unzip(file, file2, null);
    }

    public static void unzip(File file, File file2, FileFilter fileFilter) throws IOException {
        try {
            FileUtil.ensureDirectoryExists(file2);
            ZipFile zipFile = new ZipFile(file);
            int n2 = 0;
            for (Object e2 : zipFile.getFileHeaders()) {
                if ((long)n2 < 50000) {
                    FileHeader fileHeader = (FileHeader)e2;
                    File file3 = new File(file2, fileHeader.getFileName());
                    if (fileFilter != null && fileFilter.accept(file3)) continue;
                    if (fileHeader.isDirectory()) {
                        FileUtil.ensureDirectoryExists(file3);
                    } else if (fileHeader.getUncompressedSize() <= 0x20000000 && fileHeader.getCompressedSize() <= 0x20000000) {
                        FileUtil.ensureDirectoryExists(file3.getParentFile());
                        zipFile.extractFile(fileHeader, file2.getAbsolutePath());
                    } else {
                        long l2 = Math.max(fileHeader.getUncompressedSize(), fileHeader.getCompressedSize());
                        throw new IOException("Entry '" + fileHeader.getFileName() + "' is larger than " + l2 + " B.");
                    }
                    ++n2;
                    continue;
                }
                break;
            }
        }
        catch (ZipException zipException) {
            throw new IOException("Can't extract ZIP-file to directory.", zipException);
        }
    }

    public static byte[] getZipEntryBytes(File file, String string) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipUtil.writeZipEntryBytes(file, string, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void writeZipEntryBytes(File file, String string, OutputStream outputStream) throws IOException {
        TFile tFile = new TFile(new File(file, string));
        try {
            try {
                InputStream inputStream2;
                InputStream inputStream2;
                if (tFile.isArchive()) {
                    ZipUtil.synchronizeQuietly(tFile);
                    ZipFile zipFile = new ZipFile(file);
                    inputStream2 = zipFile.getInputStream(zipFile.getFileHeader(string));
                } else {
                    inputStream2 = new TFileInputStream(tFile);
                }
                IoUtil.copy(inputStream2, outputStream);
            }
            catch (ZipException zipException) {
                throw new IOException("Can't write ZIP-entry bytes.", zipException);
            }
        }
        finally {
            IoUtil.closeQuietly((AutoCloseable)outputStream);
            ZipUtil.synchronizeQuietly(tFile);
        }
    }

    public static void synchronizeQuietly(TFile tFile) {
        if (tFile != null) {
            TFile tFile2 = tFile.getTopLevelArchive();
            try {
                if (tFile2 == null) {
                    TVFS.umount(tFile);
                } else {
                    TVFS.umount(tFile2);
                }
            }
            catch (FsSyncException fsSyncException) {
                // empty catch block
            }
        }
    }
}

