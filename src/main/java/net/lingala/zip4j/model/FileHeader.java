/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.Zip64ExtendedInfo;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.unzip.Unzip;
import net.lingala.zip4j.util.Zip4jUtil;

public class FileHeader {
    private int signature;
    private int versionMadeBy;
    private int versionNeededToExtract;
    private byte[] generalPurposeFlag;
    private int compressionMethod;
    private int lastModFileTime;
    private long crc32 = 0;
    private byte[] crcBuff;
    private long compressedSize;
    private long uncompressedSize = 0;
    private int fileNameLength;
    private int extraFieldLength;
    private int diskNumberStart;
    private byte[] internalFileAttr;
    private byte[] externalFileAttr;
    private long offsetLocalHeader;
    private String fileName;
    private String fileComment;
    private boolean isDirectory;
    private boolean isEncrypted;
    private int encryptionMethod = -1;
    private char[] password;
    private boolean dataDescriptorExists;
    private Zip64ExtendedInfo zip64ExtendedInfo;
    private AESExtraDataRecord aesExtraDataRecord;
    private ArrayList extraDataRecords;
    private boolean fileNameUTF8Encoded;

    public void setSignature(int n2) {
        this.signature = n2;
    }

    public void setVersionMadeBy(int n2) {
        this.versionMadeBy = n2;
    }

    public void setVersionNeededToExtract(int n2) {
        this.versionNeededToExtract = n2;
    }

    public void setGeneralPurposeFlag(byte[] arrby) {
        this.generalPurposeFlag = arrby;
    }

    public int getCompressionMethod() {
        return this.compressionMethod;
    }

    public void setCompressionMethod(int n2) {
        this.compressionMethod = n2;
    }

    public int getLastModFileTime() {
        return this.lastModFileTime;
    }

    public void setLastModFileTime(int n2) {
        this.lastModFileTime = n2;
    }

    public long getCrc32() {
        return this.crc32 & 0xFFFFFFFFL;
    }

    public void setCrc32(long l2) {
        this.crc32 = l2;
    }

    public long getCompressedSize() {
        return this.compressedSize;
    }

    public void setCompressedSize(long l2) {
        this.compressedSize = l2;
    }

    public long getUncompressedSize() {
        return this.uncompressedSize;
    }

    public void setUncompressedSize(long l2) {
        this.uncompressedSize = l2;
    }

    public void setFileNameLength(int n2) {
        this.fileNameLength = n2;
    }

    public int getExtraFieldLength() {
        return this.extraFieldLength;
    }

    public void setExtraFieldLength(int n2) {
        this.extraFieldLength = n2;
    }

    public int getDiskNumberStart() {
        return this.diskNumberStart;
    }

    public void setDiskNumberStart(int n2) {
        this.diskNumberStart = n2;
    }

    public void setInternalFileAttr(byte[] arrby) {
        this.internalFileAttr = arrby;
    }

    public byte[] getExternalFileAttr() {
        return this.externalFileAttr;
    }

    public void setExternalFileAttr(byte[] arrby) {
        this.externalFileAttr = arrby;
    }

    public long getOffsetLocalHeader() {
        return this.offsetLocalHeader;
    }

    public void setOffsetLocalHeader(long l2) {
        this.offsetLocalHeader = l2;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String string) {
        this.fileName = string;
    }

    public void setFileComment(String string) {
        this.fileComment = string;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public void setDirectory(boolean bl) {
        this.isDirectory = bl;
    }

    public void extractFile(ZipModel zipModel, String string, UnzipParameters unzipParameters, String string2, ProgressMonitor progressMonitor, boolean bl) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("input zipModel is null");
        }
        if (!Zip4jUtil.checkOutputFolder(string)) {
            throw new ZipException("Invalid output path");
        }
        if (this == null) {
            throw new ZipException("invalid file header");
        }
        Unzip unzip = new Unzip(zipModel);
        unzip.extractFile(this, string, unzipParameters, string2, progressMonitor, bl);
    }

    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    public void setEncrypted(boolean bl) {
        this.isEncrypted = bl;
    }

    public int getEncryptionMethod() {
        return this.encryptionMethod;
    }

    public void setEncryptionMethod(int n2) {
        this.encryptionMethod = n2;
    }

    public char[] getPassword() {
        return this.password;
    }

    public byte[] getCrcBuff() {
        return this.crcBuff;
    }

    public void setCrcBuff(byte[] arrby) {
        this.crcBuff = arrby;
    }

    public ArrayList getExtraDataRecords() {
        return this.extraDataRecords;
    }

    public void setExtraDataRecords(ArrayList arrayList) {
        this.extraDataRecords = arrayList;
    }

    public void setDataDescriptorExists(boolean bl) {
        this.dataDescriptorExists = bl;
    }

    public Zip64ExtendedInfo getZip64ExtendedInfo() {
        return this.zip64ExtendedInfo;
    }

    public void setZip64ExtendedInfo(Zip64ExtendedInfo zip64ExtendedInfo) {
        this.zip64ExtendedInfo = zip64ExtendedInfo;
    }

    public AESExtraDataRecord getAesExtraDataRecord() {
        return this.aesExtraDataRecord;
    }

    public void setAesExtraDataRecord(AESExtraDataRecord aESExtraDataRecord) {
        this.aesExtraDataRecord = aESExtraDataRecord;
    }

    public boolean isFileNameUTF8Encoded() {
        return this.fileNameUTF8Encoded;
    }

    public void setFileNameUTF8Encoded(boolean bl) {
        this.fileNameUTF8Encoded = bl;
    }
}

