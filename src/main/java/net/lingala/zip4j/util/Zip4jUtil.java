/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;

public class Zip4jUtil {
    public static boolean isStringNotNullAndNotEmpty(String string) {
        if (string == null || string.trim().length() <= 0) {
            return false;
        }
        return true;
    }

    public static boolean checkOutputFolder(String string) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException(new NullPointerException("output path is null"));
        }
        File file = new File(string);
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new ZipException("output folder is not valid");
            }
            if (!file.canWrite()) {
                throw new ZipException("no write access to output folder");
            }
        } else {
            try {
                file.mkdirs();
                if (!file.isDirectory()) {
                    throw new ZipException("output folder is not valid");
                }
                if (!file.canWrite()) {
                    throw new ZipException("no write access to destination folder");
                }
            }
            catch (Exception exception) {
                throw new ZipException("Cannot create destination folder");
            }
        }
        return true;
    }

    public static boolean checkFileReadAccess(String string) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("path is null");
        }
        if (!Zip4jUtil.checkFileExists(string)) {
            throw new ZipException("file does not exist: " + string);
        }
        try {
            File file = new File(string);
            return file.canRead();
        }
        catch (Exception exception) {
            throw new ZipException("cannot read zip file");
        }
    }

    public static boolean checkFileExists(String string) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("path is null");
        }
        File file = new File(string);
        return Zip4jUtil.checkFileExists(file);
    }

    public static boolean checkFileExists(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("cannot check if file exists: input file is null");
        }
        return file.exists();
    }

    public static void setFileReadOnly(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set read only file attribute");
        }
        if (file.exists()) {
            file.setReadOnly();
        }
    }

    public static void setFileHidden(File file) throws ZipException {
    }

    public static void setFileArchive(File file) throws ZipException {
    }

    public static void setFileSystemMode(File file) throws ZipException {
    }

    public static long dosToJavaTme(int n2) {
        int n3 = 2 * (n2 & 31);
        int n4 = n2 >> 5 & 63;
        int n5 = n2 >> 11 & 31;
        int n6 = n2 >> 16 & 31;
        int n7 = (n2 >> 21 & 15) - 1;
        int n8 = (n2 >> 25 & 127) + 1980;
        Calendar calendar = Calendar.getInstance();
        calendar.set(n8, n7, n6, n5, n4, n3);
        calendar.set(14, 0);
        return calendar.getTime().getTime();
    }

    public static FileHeader getFileHeader(ZipModel zipModel, String string) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot determine file header for fileName: " + string);
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("file name is null, cannot determine file header for fileName: " + string);
        }
        FileHeader fileHeader = null;
        fileHeader = Zip4jUtil.getFileHeaderWithExactMatch(zipModel, string);
        if (fileHeader == null && (fileHeader = Zip4jUtil.getFileHeaderWithExactMatch(zipModel, string = string.replaceAll("\\\\", "/"))) == null) {
            string = string.replaceAll("/", "\\\\");
            fileHeader = Zip4jUtil.getFileHeaderWithExactMatch(zipModel, string);
        }
        return fileHeader;
    }

    public static FileHeader getFileHeaderWithExactMatch(ZipModel zipModel, String string) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot determine file header with exact match for fileName: " + string);
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("file name is null, cannot determine file header with exact match for fileName: " + string);
        }
        if (zipModel.getCentralDirectory() == null) {
            throw new ZipException("central directory is null, cannot determine file header with exact match for fileName: " + string);
        }
        if (zipModel.getCentralDirectory().getFileHeaders() == null) {
            throw new ZipException("file Headers are null, cannot determine file header with exact match for fileName: " + string);
        }
        if (zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return null;
        }
        ArrayList arrayList = zipModel.getCentralDirectory().getFileHeaders();
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            FileHeader fileHeader = (FileHeader)arrayList.get(i2);
            String string2 = fileHeader.getFileName();
            if (!Zip4jUtil.isStringNotNullAndNotEmpty(string2) || !string.equalsIgnoreCase(string2)) continue;
            return fileHeader;
        }
        return null;
    }

    public static String decodeFileName(byte[] arrby, boolean bl) {
        if (bl) {
            try {
                return new String(arrby, "UTF8");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                return new String(arrby);
            }
        }
        return Zip4jUtil.getCp850EncodedString(arrby);
    }

    public static String getCp850EncodedString(byte[] arrby) {
        try {
            String string = new String(arrby, "Cp850");
            return string;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return new String(arrby);
        }
    }
}

