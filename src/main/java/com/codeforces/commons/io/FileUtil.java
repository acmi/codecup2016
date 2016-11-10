/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io;

import com.codeforces.commons.io.internal.UnsafeFileUtil;
import com.codeforces.commons.process.ThreadUtil;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Locale;
import java.util.regex.Pattern;

public class FileUtil {
    private static final Pattern SIZE_PATTERN = Pattern.compile("(0|[1-9][01-9]{0,5})(\\.[01-9]{1,5})? ?[KMGTP]?B?");

    private FileUtil() {
        throw new UnsupportedOperationException();
    }

    public static <T> T executeIoOperation(ThreadUtil.Operation<T> operation) throws IOException {
        return FileUtil.executeIoOperation(operation, 9);
    }

    public static <T> T executeIoOperation(ThreadUtil.Operation<T> operation, int n2) throws IOException {
        return FileUtil.executeIoOperation(operation, n2, 50, ThreadUtil.ExecutionStrategy.Type.SQUARE);
    }

    public static <T> T executeIoOperation(ThreadUtil.Operation<T> operation, int n2, long l2, ThreadUtil.ExecutionStrategy.Type type) throws IOException {
        try {
            return ThreadUtil.execute(operation, n2, new ThreadUtil.ExecutionStrategy(l2, type));
        }
        catch (Error | RuntimeException throwable) {
            throw throwable;
        }
        catch (Throwable throwable) {
            throw new IOException(throwable);
        }
    }

    public static File ensureDirectoryExists(File file) throws IOException {
        return Preconditions.checkNotNull(FileUtil.executeIoOperation(() -> UnsafeFileUtil.ensureDirectoryExists(file)));
    }

    public static File ensureParentDirectoryExists(File file) throws IOException {
        File file2 = file.getParentFile();
        if (file2 == null) {
            return null;
        }
        return FileUtil.executeIoOperation(() -> UnsafeFileUtil.ensureDirectoryExists(file2));
    }

    public static void deleteTotally(File file) throws IOException {
        FileUtil.executeIoOperation(() -> {
            UnsafeFileUtil.deleteTotally(file);
            return null;
        }
        );
    }

    public static void writeFile(File file, byte[] arrby) throws IOException {
        FileUtil.executeIoOperation(() -> {
            UnsafeFileUtil.writeFile(file, arrby);
            return null;
        }
        );
    }

    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

    public static boolean isDirectory(File file) {
        return file != null && file.isDirectory();
    }

    public static void createSymbolicLinkOrCopy(File file, File file2) throws IOException {
        if (!file.exists()) {
            throw new IOException("Source '" + file + "' doesn't exist.");
        }
        FileUtil.deleteTotally(file2);
        FileUtil.ensureParentDirectoryExists(file2);
        try {
            Files.createSymbolicLink(Paths.get(file2.toURI()), Paths.get(file.toURI()), new FileAttribute[0]);
        }
        catch (IOException | InternalError | UnsupportedOperationException throwable) {
            if (FileUtil.isFile(file)) {
                UnsafeFileUtil.copyFile(file, file2);
            }
            if (FileUtil.isDirectory(file)) {
                UnsafeFileUtil.copyDirectory(file, file2);
            }
            throw new IOException("Unexpected source '" + file + "'.");
        }
    }

    public static String formatSize(long l2) {
        if (l2 < 0) {
            throw new IllegalArgumentException("Argument 'size' must be a positive integer or zero.");
        }
        if (l2 >= 0x4000000000000L) {
            return FileUtil.formatSize(l2, 0x4000000000000L, "PB");
        }
        if (l2 >= 0x10000000000L) {
            return FileUtil.formatSize(l2, 0x10000000000L, "TB");
        }
        if (l2 >= 0x40000000) {
            return FileUtil.formatSize(l2, 0x40000000, "GB");
        }
        if (l2 >= 0x100000) {
            return FileUtil.formatSize(l2, 0x100000, "MB");
        }
        if (l2 >= 1024) {
            return FileUtil.formatSize(l2, 1024, "kB");
        }
        return "" + l2 + " B";
    }

    private static String formatSize(long l2, long l3, String string) {
        if (l2 % l3 == 0) {
            return "" + l2 / l3 + " " + string;
        }
        return String.format(Locale.US, "%.1f %s", (double)l2 / (double)l3, string);
    }
}

