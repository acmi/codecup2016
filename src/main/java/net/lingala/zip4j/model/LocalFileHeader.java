/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

import java.util.ArrayList;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.Zip64ExtendedInfo;

public class LocalFileHeader {
    private int signature;
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
    private String fileName;
    private long offsetStartOfData;
    private boolean isEncrypted;
    private int encryptionMethod = -1;
    private char[] password;
    private ArrayList extraDataRecords;
    private Zip64ExtendedInfo zip64ExtendedInfo;
    private AESExtraDataRecord aesExtraDataRecord;
    private boolean dataDescriptorExists;
    private boolean writeComprSizeInZip64ExtraRecord = false;
    private boolean fileNameUTF8Encoded;

    public void setSignature(int n2) {
        this.signature = n2;
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

    public void setLastModFileTime(int n2) {
        this.lastModFileTime = n2;
    }

    public long getCrc32() {
        return this.crc32;
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

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String string) {
        this.fileName = string;
    }

    public long getOffsetStartOfData() {
        return this.offsetStartOfData;
    }

    public void setOffsetStartOfData(long l2) {
        this.offsetStartOfData = l2;
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

    public void setCrcBuff(byte[] arrby) {
        this.crcBuff = arrby;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] arrc) {
        this.password = arrc;
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

