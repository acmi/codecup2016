/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io.internal;

import com.codeforces.commons.compress.ZipUtil;
import com.codeforces.commons.io.IoUtil;
import com.google.common.primitives.Ints;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import de.schlichtherle.truezip.file.TVFS;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class UnsafeFileUtil {
    private static final Logger logger = Logger.getLogger(UnsafeFileUtil.class);

    private UnsafeFileUtil() {
        throw new UnsupportedOperationException();
    }

    public static void copyFile(File file, File file2) throws IOException {
        UnsafeFileUtil.internalCopyFile(file, file2, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void internalCopyFile(File file, File file2, boolean bl) throws IOException {
        if (file2 instanceof TFile) {
            throw new UnsupportedOperationException("Can't copy file into archive file.");
        }
        UnsafeFileUtil.deleteTotally(file2);
        File file3 = file2.getParentFile();
        if (file3 != null) {
            UnsafeFileUtil.ensureDirectoryExists(file3);
        }
        if (file instanceof TFile) {
            try {
                UnsafeFileUtil.writeFile(file2, UnsafeFileUtil.getBytes(file));
            }
            finally {
                if (bl) {
                    ZipUtil.synchronizeQuietly((TFile)file);
                }
            }
        }
        if (!file.isFile()) {
            throw new IOException("'" + file + "' is not a file.");
        }
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            fileOutputStream = new FileOutputStream(file2);
            FileChannel fileChannel = fileInputStream.getChannel();
            FileChannel fileChannel2 = fileOutputStream.getChannel();
            fileChannel.transferTo(0, fileChannel.size(), fileChannel2);
        }
        catch (Throwable throwable) {
            IoUtil.closeQuietly(fileInputStream, fileOutputStream);
            throw throwable;
        }
        IoUtil.closeQuietly(fileInputStream, fileOutputStream);
    }

    public static void copyDirectory(File file, File file2) throws IOException {
        UnsafeFileUtil.internalCopyDirectory(file, file2, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void internalCopyDirectory(File file, File file2, boolean bl) throws IOException {
        if (file2 instanceof TFile) {
            throw new UnsupportedOperationException("Can't copy directory into archive file.");
        }
        if (!file.isDirectory()) {
            throw new IOException("'" + file + "' is not a directory.");
        }
        if (file2.isFile()) {
            throw new IOException("'" + file2 + "' is a file.");
        }
        UnsafeFileUtil.ensureDirectoryExists(file2);
        try {
            for (String string : file.list()) {
                File file3 = file instanceof TFile ? new TFile(file, string) : new File(file, string);
                File file4 = new File(file2, string);
                if (file3.isDirectory()) {
                    TFile tFile;
                    TFile tFile2;
                    if (file3 instanceof TFile && (tFile2 = (TFile)file3).isArchive() && (tFile = tFile2.getEnclArchive()) != null && new File(tFile.getAbsolutePath()).isFile()) {
                        UnsafeFileUtil.deleteTotally(file4);
                        ZipUtil.synchronizeQuietly(tFile2);
                        ZipUtil.writeZipEntryBytes(tFile, tFile2.getEnclEntryName(), new FileOutputStream(file4));
                        continue;
                    }
                    UnsafeFileUtil.internalCopyDirectory(file3, file4, false);
                    continue;
                }
                UnsafeFileUtil.internalCopyFile(file3, file4, false);
            }
        }
        finally {
            if (bl && file instanceof TFile) {
                ZipUtil.synchronizeQuietly((TFile)file);
            }
        }
    }

    public static File ensureDirectoryExists(File file) throws IOException {
        if (file.isDirectory() || file.mkdirs() || file.isDirectory()) {
            return file;
        }
        throw new IOException("Can't create directory '" + file + "'.");
    }

    public static void deleteTotally(File file) throws IOException {
        if (file == null) {
            return;
        }
        Path path = Paths.get(file.toURI());
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isSymbolicLink(path)) {
                if (!file.delete() && Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                    throw new IOException("Can't delete symbolic link '" + file + "'.");
                }
            } else if (file.isFile()) {
                if (!file.delete() && file.exists()) {
                    throw new IOException("Can't delete file '" + file + "'.");
                }
            } else if (file.isDirectory()) {
                UnsafeFileUtil.cleanDirectory(file, null);
                if (!file.delete() && file.exists()) {
                    throw new IOException("Can't delete directory '" + file + "'.");
                }
            } else if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                throw new IllegalArgumentException("Unsupported file system item '" + file + "'.");
            }
        }
    }

    private static void ensureParentDirectoryExists(File file) throws IOException {
        File file2 = file.getParentFile();
        if (file2 != null) {
            UnsafeFileUtil.ensureDirectoryExists(file2);
        }
    }

    public static void cleanDirectory(File file, FileFilter fileFilter) throws IOException {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("'" + file + "' is not a directory.");
        }
        File[] arrfile = file.listFiles();
        if (arrfile == null) {
            throw new IOException("Failed to list files of '" + file + "'.");
        }
        for (File file2 : arrfile) {
            if (fileFilter == null || fileFilter.accept(file2)) {
                UnsafeFileUtil.deleteTotally(file2);
                continue;
            }
            if (!file2.isDirectory() || Files.isSymbolicLink(Paths.get(file2.toURI()))) continue;
            UnsafeFileUtil.cleanDirectory(file2, fileFilter);
        }
    }

    public static void writeFile(File file, byte[] arrby) throws IOException {
        UnsafeFileUtil.ensureParentDirectoryExists(file);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(arrby);
        }
        finally {
            IoUtil.closeQuietly((AutoCloseable)fileOutputStream);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] getBytes(File file) throws IOException {
        block11 : {
            if (file instanceof TFile) {
                TFile tFile = (TFile)file;
                try {
                    if (tFile.isFile()) {
                        long l2 = file.length();
                        TFileInputStream tFileInputStream = new TFileInputStream(file);
                        byte[] arrby = new byte[Ints.checkedCast(l2)];
                        IOUtils.read(tFileInputStream, arrby);
                        tFileInputStream.close();
                        byte[] arrby2 = arrby;
                        return arrby2;
                    }
                    if (tFile.isArchive()) {
                        TVFS.umount(tFile);
                        file = new File(file.getAbsolutePath());
                        if (file.isFile()) {
                            byte[] arrby = UnsafeFileUtil.forceGetBytesFromExistingRegularFile(file);
                            return arrby;
                        }
                        TFile tFile2 = tFile.getEnclArchive();
                        if (tFile2 != null && new File(tFile2.getAbsolutePath()).isFile()) {
                            byte[] arrby = ZipUtil.getZipEntryBytes(tFile2, tFile.getEnclEntryName());
                            return arrby;
                        }
                    }
                    break block11;
                }
                finally {
                    TVFS.umount(tFile);
                }
            }
            if (file.isFile()) {
                return UnsafeFileUtil.forceGetBytesFromExistingRegularFile(file);
            }
        }
        throw new FileNotFoundException("'" + file + "' is not file.");
    }

    private static byte[] forceGetBytesFromExistingRegularFile(File file) throws IOException {
        long l2 = file.length();
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(Ints.checkedCast(l2));
        fileChannel.read(byteBuffer);
        fileChannel.close();
        fileInputStream.close();
        return byteBuffer.array();
    }
}

