/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.resource;

import com.codeforces.commons.io.FileUtil;
import com.codeforces.commons.io.IoUtil;
import com.google.common.io.Resources;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ResourceUtil {
    private static final Logger logger = Logger.getLogger(ResourceUtil.class);
    private static final ConcurrentMap<File, ReadWriteLock> cacheLockByDirectory = new ConcurrentHashMap<File, ReadWriteLock>();
    private static final ConcurrentMap<CacheKey, Boolean> validationResultByCacheKey = new ConcurrentHashMap<CacheKey, Boolean>();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copyResourceToDir(File file, File file2, String string, byte[] arrby, Class class_, boolean bl) throws IOException {
        File file3 = new File(file, new File(string).getName());
        if (file2 == null) {
            ResourceUtil.saveResourceToFile(file3, string, arrby, class_);
        } else {
            File file4;
            boolean bl2;
            file4 = new File(file2, ResourceUtil.toRelativePath(string));
            ReadWriteLock readWriteLock = cacheLockByDirectory.get(file2);
            if (readWriteLock == null) {
                cacheLockByDirectory.putIfAbsent(file2, new ReentrantReadWriteLock());
                readWriteLock = cacheLockByDirectory.get(file2);
            }
            Lock lock = readWriteLock.readLock();
            lock.lock();
            try {
                bl2 = ResourceUtil.isCacheEntryValid(file4, string, arrby, class_, bl);
            }
            finally {
                lock.unlock();
            }
            if (!bl2) {
                Lock lock2 = readWriteLock.writeLock();
                lock2.lock();
                try {
                    if (!ResourceUtil.isCacheEntryValid(file4, string, arrby, class_, bl)) {
                        ResourceUtil.writeCacheEntry(file4, string, arrby, class_);
                    }
                }
                finally {
                    lock2.unlock();
                }
            }
            try {
                FileUtil.createSymbolicLinkOrCopy(file4, file3);
            }
            catch (IOException iOException) {
                throw new IOException(String.format("Can't create symbolic link or copy resource '%s' into the directory '%s'.", string, file), iOException);
            }
        }
    }

    private static boolean isCacheEntryValid(File file, String string, byte[] arrby, Class class_, boolean bl) throws IOException {
        Object object;
        if (!file.isFile()) {
            return false;
        }
        Class class_2 = class_ == null ? ResourceUtil.class : class_;
        CacheKey cacheKey = new CacheKey(string, arrby, class_);
        if (bl && (object = validationResultByCacheKey.get(cacheKey)) != null && object.booleanValue()) {
            long l2 = Resources.asByteSource(class_2.getResource(string)).size();
            if (file.length() == l2) {
                return true;
            }
        }
        object = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            object = arrby == null ? new BufferedInputStream(class_2.getResourceAsStream(string), IoUtil.BUFFER_SIZE) : new ByteArrayInputStream(arrby);
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file), IoUtil.BUFFER_SIZE);
            boolean bl2 = IOUtils.contentEquals((InputStream)object, bufferedInputStream);
            if (bl2) {
                validationResultByCacheKey.putIfAbsent(cacheKey, true);
            }
            object.close();
            bufferedInputStream.close();
            return bl2;
        }
        catch (IOException iOException) {
            IoUtil.closeQuietly(new AutoCloseable[]{object, bufferedInputStream});
            throw new IOException(String.format("Can't compare resource '%s' and cache file '%s'.", string, file), iOException);
        }
    }

    private static void writeCacheEntry(File file, String string, byte[] arrby, Class class_) throws IOException {
        logger.info(String.format("Saving resource '%s' to the cache file '%s'.", string, file));
        try {
            FileUtil.deleteTotally(file);
        }
        catch (IOException iOException) {
            throw new IOException(String.format("Can't delete invalid cache file '%s'.", file), iOException);
        }
        try {
            FileUtil.ensureParentDirectoryExists(file);
        }
        catch (IOException iOException) {
            throw new IOException(String.format("Can't create cache directory '%s'.", file.getParentFile()), iOException);
        }
        ResourceUtil.saveResourceToFile(file, string, arrby, class_);
    }

    public static void saveResourceToFile(File file, String string, byte[] arrby, Class class_) throws IOException {
        block8 : {
            InputStream inputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                if (arrby == null) {
                    inputStream = (class_ == null ? FileUtil.class : class_).getResourceAsStream(string);
                    if (inputStream == null) {
                        throw new IOException("Can't find resource '" + string + "'.");
                    }
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    IoUtil.copy(inputStream, bufferedOutputStream);
                    inputStream.close();
                    bufferedOutputStream.close();
                    break block8;
                }
                FileUtil.writeFile(file, arrby);
            }
            catch (IOException iOException) {
                IoUtil.closeQuietly(inputStream, bufferedOutputStream);
                throw new IOException(String.format("Can't save resource '%s' to the file '%s'.", string, file), iOException);
            }
            finally {
                if (string != null) {
                    validationResultByCacheKey.putIfAbsent(new CacheKey(string, arrby, class_), true);
                }
            }
        }
    }

    private static String toRelativePath(String string) {
        while (string.startsWith(File.separator)) {
            string = string.substring(File.separator.length());
        }
        while (SEPARATOR != null && string.startsWith(SEPARATOR)) {
            string = string.substring(SEPARATOR.length());
        }
        while (string.startsWith("/")) {
            string = string.substring("/".length());
        }
        while (string.startsWith("\\")) {
            string = string.substring("\\".length());
        }
        return string;
    }

    private static final class SeparatorHolder {
        private static final String SEPARATOR;

        static {
            String string;
            try {
                FileSystem fileSystem = FileSystems.getDefault();
                string = fileSystem == null ? null : fileSystem.getSeparator();
            }
            catch (RuntimeException runtimeException) {
                logger.fatal("Can't get path separator.", runtimeException);
                string = null;
            }
            SEPARATOR = string;
        }
    }

    private static final class CacheKey {
        private final String sha1;

        private CacheKey(String string, byte[] arrby, Class class_) {
            String string2 = arrby == null ? "" : DigestUtils.sha1Hex(arrby);
            String string3 = class_ == null ? "" : class_.getCanonicalName();
            this.sha1 = DigestUtils.sha1Hex(string + '\u0001' + string2 + '\u0002' + string3);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            CacheKey cacheKey = (CacheKey)object;
            return this.sha1.equals(cacheKey.sha1);
        }

        public int hashCode() {
            return this.sha1.hashCode();
        }
    }

}

