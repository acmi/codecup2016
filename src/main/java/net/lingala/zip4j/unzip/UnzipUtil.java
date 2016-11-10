/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.unzip;

import java.io.File;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.util.Zip4jUtil;

public class UnzipUtil {
    public static void applyFileAttributes(FileHeader fileHeader, File file, UnzipParameters unzipParameters) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("cannot set file properties: file header is null");
        }
        if (file == null) {
            throw new ZipException("cannot set file properties: output file is null");
        }
        if (!Zip4jUtil.checkFileExists(file)) {
            throw new ZipException("cannot set file properties: file doesnot exist");
        }
        if (unzipParameters == null || !unzipParameters.isIgnoreDateTimeAttributes()) {
            UnzipUtil.setFileLastModifiedTime(fileHeader, file);
        }
        if (unzipParameters == null) {
            UnzipUtil.setFileAttributes(fileHeader, file, true, true, true, true);
        } else if (unzipParameters.isIgnoreAllFileAttributes()) {
            UnzipUtil.setFileAttributes(fileHeader, file, false, false, false, false);
        } else {
            UnzipUtil.setFileAttributes(fileHeader, file, !unzipParameters.isIgnoreReadOnlyFileAttribute(), !unzipParameters.isIgnoreHiddenFileAttribute(), !unzipParameters.isIgnoreArchiveFileAttribute(), !unzipParameters.isIgnoreSystemFileAttribute());
        }
    }

    private static void setFileAttributes(FileHeader fileHeader, File file, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("invalid file header. cannot set file attributes");
        }
        byte[] arrby = fileHeader.getExternalFileAttr();
        if (arrby == null) {
            return;
        }
        byte by = arrby[0];
        switch (by) {
            case 1: {
                if (!bl) break;
                Zip4jUtil.setFileReadOnly(file);
                break;
            }
            case 2: 
            case 18: {
                if (!bl2) break;
                Zip4jUtil.setFileHidden(file);
                break;
            }
            case 32: 
            case 48: {
                if (!bl3) break;
                Zip4jUtil.setFileArchive(file);
                break;
            }
            case 3: {
                if (bl) {
                    Zip4jUtil.setFileReadOnly(file);
                }
                if (!bl2) break;
                Zip4jUtil.setFileHidden(file);
                break;
            }
            case 33: {
                if (bl3) {
                    Zip4jUtil.setFileArchive(file);
                }
                if (!bl) break;
                Zip4jUtil.setFileReadOnly(file);
                break;
            }
            case 34: 
            case 50: {
                if (bl3) {
                    Zip4jUtil.setFileArchive(file);
                }
                if (!bl2) break;
                Zip4jUtil.setFileHidden(file);
                break;
            }
            case 35: {
                if (bl3) {
                    Zip4jUtil.setFileArchive(file);
                }
                if (bl) {
                    Zip4jUtil.setFileReadOnly(file);
                }
                if (!bl2) break;
                Zip4jUtil.setFileHidden(file);
                break;
            }
            case 38: {
                if (bl) {
                    Zip4jUtil.setFileReadOnly(file);
                }
                if (bl2) {
                    Zip4jUtil.setFileHidden(file);
                }
                if (!bl4) break;
                Zip4jUtil.setFileSystemMode(file);
                break;
            }
        }
    }

    private static void setFileLastModifiedTime(FileHeader fileHeader, File file) throws ZipException {
        if (fileHeader.getLastModFileTime() <= 0) {
            return;
        }
        if (file.exists()) {
            file.setLastModified(Zip4jUtil.dosToJavaTme(fileHeader.getLastModFileTime()));
        }
    }
}

