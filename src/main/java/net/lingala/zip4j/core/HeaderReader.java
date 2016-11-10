/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.DigitalSignature;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip64EndCentralDirLocator;
import net.lingala.zip4j.model.Zip64EndCentralDirRecord;
import net.lingala.zip4j.model.Zip64ExtendedInfo;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderReader {
    private RandomAccessFile zip4jRaf = null;
    private ZipModel zipModel;

    public HeaderReader(RandomAccessFile randomAccessFile) {
        this.zip4jRaf = randomAccessFile;
    }

    public ZipModel readAllHeaders(String string) throws ZipException {
        this.zipModel = new ZipModel();
        this.zipModel.setFileNameCharset(string);
        this.zipModel.setEndCentralDirRecord(this.readEndOfCentralDirectoryRecord());
        this.zipModel.setZip64EndCentralDirLocator(this.readZip64EndCentralDirLocator());
        if (this.zipModel.isZip64Format()) {
            this.zipModel.setZip64EndCentralDirRecord(this.readZip64EndCentralDirRec());
            if (this.zipModel.getZip64EndCentralDirRecord() != null && this.zipModel.getZip64EndCentralDirRecord().getNoOfThisDisk() > 0) {
                this.zipModel.setSplitArchive(true);
            } else {
                this.zipModel.setSplitArchive(false);
            }
        }
        this.zipModel.setCentralDirectory(this.readCentralDirectory());
        return this.zipModel;
    }

    private EndCentralDirRecord readEndOfCentralDirectoryRecord() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("random access file was null", 3);
        }
        try {
            byte[] arrby = new byte[4];
            long l2 = this.zip4jRaf.length() - 22;
            EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
            int n2 = 0;
            do {
                this.zip4jRaf.seek(l2--);
            } while ((long)Raw.readLeInt(this.zip4jRaf, arrby) != 101010256 && ++n2 <= 3000);
            if ((long)Raw.readIntLittleEndian(arrby, 0) != 101010256) {
                throw new ZipException("zip headers not found. probably not a zip file");
            }
            byte[] arrby2 = new byte[4];
            byte[] arrby3 = new byte[2];
            endCentralDirRecord.setSignature(101010256);
            this.readIntoBuff(this.zip4jRaf, arrby3);
            endCentralDirRecord.setNoOfThisDisk(Raw.readShortLittleEndian(arrby3, 0));
            this.readIntoBuff(this.zip4jRaf, arrby3);
            endCentralDirRecord.setNoOfThisDiskStartOfCentralDir(Raw.readShortLittleEndian(arrby3, 0));
            this.readIntoBuff(this.zip4jRaf, arrby3);
            endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readShortLittleEndian(arrby3, 0));
            this.readIntoBuff(this.zip4jRaf, arrby3);
            endCentralDirRecord.setTotNoOfEntriesInCentralDir(Raw.readShortLittleEndian(arrby3, 0));
            this.readIntoBuff(this.zip4jRaf, arrby2);
            endCentralDirRecord.setSizeOfCentralDir(Raw.readIntLittleEndian(arrby2, 0));
            this.readIntoBuff(this.zip4jRaf, arrby2);
            byte[] arrby4 = this.getLongByteFromIntByte(arrby2);
            endCentralDirRecord.setOffsetOfStartOfCentralDir(Raw.readLongLittleEndian(arrby4, 0));
            this.readIntoBuff(this.zip4jRaf, arrby3);
            int n3 = Raw.readShortLittleEndian(arrby3, 0);
            endCentralDirRecord.setCommentLength(n3);
            if (n3 > 0) {
                byte[] arrby5 = new byte[n3];
                this.readIntoBuff(this.zip4jRaf, arrby5);
                endCentralDirRecord.setComment(new String(arrby5));
                endCentralDirRecord.setCommentBytes(arrby5);
            } else {
                endCentralDirRecord.setComment(null);
            }
            int n4 = endCentralDirRecord.getNoOfThisDisk();
            if (n4 > 0) {
                this.zipModel.setSplitArchive(true);
            } else {
                this.zipModel.setSplitArchive(false);
            }
            return endCentralDirRecord;
        }
        catch (IOException iOException) {
            throw new ZipException("Probably not a zip file or a corrupted zip file", iOException, 4);
        }
    }

    private CentralDirectory readCentralDirectory() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("random access file was null", 3);
        }
        if (this.zipModel.getEndCentralDirRecord() == null) {
            throw new ZipException("EndCentralRecord was null, maybe a corrupt zip file");
        }
        try {
            int n2;
            CentralDirectory centralDirectory = new CentralDirectory();
            ArrayList<FileHeader> arrayList = new ArrayList<FileHeader>();
            EndCentralDirRecord endCentralDirRecord = this.zipModel.getEndCentralDirRecord();
            long l2 = endCentralDirRecord.getOffsetOfStartOfCentralDir();
            int n3 = endCentralDirRecord.getTotNoOfEntriesInCentralDir();
            if (this.zipModel.isZip64Format()) {
                l2 = this.zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo();
                n3 = (int)this.zipModel.getZip64EndCentralDirRecord().getTotNoOfEntriesInCentralDir();
            }
            this.zip4jRaf.seek(l2);
            byte[] arrby = new byte[4];
            byte[] arrby2 = new byte[2];
            byte[] arrby3 = new byte[8];
            for (int i2 = 0; i2 < n3; ++i2) {
                byte[] arrby4;
                FileHeader fileHeader = new FileHeader();
                this.readIntoBuff(this.zip4jRaf, arrby);
                n2 = Raw.readIntLittleEndian(arrby, 0);
                if ((long)n2 != 33639248) {
                    throw new ZipException("Expected central directory entry not found (#" + (i2 + 1) + ")");
                }
                fileHeader.setSignature(n2);
                this.readIntoBuff(this.zip4jRaf, arrby2);
                fileHeader.setVersionMadeBy(Raw.readShortLittleEndian(arrby2, 0));
                this.readIntoBuff(this.zip4jRaf, arrby2);
                fileHeader.setVersionNeededToExtract(Raw.readShortLittleEndian(arrby2, 0));
                this.readIntoBuff(this.zip4jRaf, arrby2);
                fileHeader.setFileNameUTF8Encoded((Raw.readShortLittleEndian(arrby2, 0) & 2048) != 0);
                byte by = arrby2[0];
                int n4 = by & 1;
                if (n4 != 0) {
                    fileHeader.setEncrypted(true);
                }
                fileHeader.setGeneralPurposeFlag((byte[])arrby2.clone());
                fileHeader.setDataDescriptorExists(by >> 3 == 1);
                this.readIntoBuff(this.zip4jRaf, arrby2);
                fileHeader.setCompressionMethod(Raw.readShortLittleEndian(arrby2, 0));
                this.readIntoBuff(this.zip4jRaf, arrby);
                fileHeader.setLastModFileTime(Raw.readIntLittleEndian(arrby, 0));
                this.readIntoBuff(this.zip4jRaf, arrby);
                fileHeader.setCrc32(Raw.readIntLittleEndian(arrby, 0));
                fileHeader.setCrcBuff((byte[])arrby.clone());
                this.readIntoBuff(this.zip4jRaf, arrby);
                arrby3 = this.getLongByteFromIntByte(arrby);
                fileHeader.setCompressedSize(Raw.readLongLittleEndian(arrby3, 0));
                this.readIntoBuff(this.zip4jRaf, arrby);
                arrby3 = this.getLongByteFromIntByte(arrby);
                fileHeader.setUncompressedSize(Raw.readLongLittleEndian(arrby3, 0));
                this.readIntoBuff(this.zip4jRaf, arrby2);
                int n5 = Raw.readShortLittleEndian(arrby2, 0);
                fileHeader.setFileNameLength(n5);
                this.readIntoBuff(this.zip4jRaf, arrby2);
                int n6 = Raw.readShortLittleEndian(arrby2, 0);
                fileHeader.setExtraFieldLength(n6);
                this.readIntoBuff(this.zip4jRaf, arrby2);
                int n7 = Raw.readShortLittleEndian(arrby2, 0);
                fileHeader.setFileComment(new String(arrby2));
                this.readIntoBuff(this.zip4jRaf, arrby2);
                fileHeader.setDiskNumberStart(Raw.readShortLittleEndian(arrby2, 0));
                this.readIntoBuff(this.zip4jRaf, arrby2);
                fileHeader.setInternalFileAttr((byte[])arrby2.clone());
                this.readIntoBuff(this.zip4jRaf, arrby);
                fileHeader.setExternalFileAttr((byte[])arrby.clone());
                this.readIntoBuff(this.zip4jRaf, arrby);
                arrby3 = this.getLongByteFromIntByte(arrby);
                fileHeader.setOffsetLocalHeader(Raw.readLongLittleEndian(arrby3, 0) & 0xFFFFFFFFL);
                if (n5 > 0) {
                    arrby4 = new byte[n5];
                    this.readIntoBuff(this.zip4jRaf, arrby4);
                    String string = null;
                    string = Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset()) ? new String(arrby4, this.zipModel.getFileNameCharset()) : Zip4jUtil.decodeFileName(arrby4, fileHeader.isFileNameUTF8Encoded());
                    if (string == null) {
                        throw new ZipException("fileName is null when reading central directory");
                    }
                    if (string.indexOf(":" + System.getProperty("file.separator")) >= 0) {
                        string = string.substring(string.indexOf(":" + System.getProperty("file.separator")) + 2);
                    }
                    fileHeader.setFileName(string);
                    fileHeader.setDirectory(string.endsWith("/") || string.endsWith("\\"));
                } else {
                    fileHeader.setFileName(null);
                }
                this.readAndSaveExtraDataRecord(fileHeader);
                this.readAndSaveZip64ExtendedInfo(fileHeader);
                this.readAndSaveAESExtraDataRecord(fileHeader);
                if (n7 > 0) {
                    arrby4 = new byte[n7];
                    this.readIntoBuff(this.zip4jRaf, arrby4);
                    fileHeader.setFileComment(new String(arrby4));
                }
                arrayList.add(fileHeader);
            }
            centralDirectory.setFileHeaders(arrayList);
            DigitalSignature digitalSignature = new DigitalSignature();
            this.readIntoBuff(this.zip4jRaf, arrby);
            int n8 = Raw.readIntLittleEndian(arrby, 0);
            if ((long)n8 != 84233040) {
                return centralDirectory;
            }
            digitalSignature.setHeaderSignature(n8);
            this.readIntoBuff(this.zip4jRaf, arrby2);
            n2 = Raw.readShortLittleEndian(arrby2, 0);
            digitalSignature.setSizeOfData(n2);
            if (n2 > 0) {
                byte[] arrby5 = new byte[n2];
                this.readIntoBuff(this.zip4jRaf, arrby5);
                digitalSignature.setSignatureData(new String(arrby5));
            }
            return centralDirectory;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private void readAndSaveExtraDataRecord(FileHeader fileHeader) throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read extra data record");
        }
        if (fileHeader == null) {
            throw new ZipException("file header is null");
        }
        int n2 = fileHeader.getExtraFieldLength();
        if (n2 <= 0) {
            return;
        }
        fileHeader.setExtraDataRecords(this.readExtraDataRecords(n2));
    }

    private void readAndSaveExtraDataRecord(LocalFileHeader localFileHeader) throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read extra data record");
        }
        if (localFileHeader == null) {
            throw new ZipException("file header is null");
        }
        int n2 = localFileHeader.getExtraFieldLength();
        if (n2 <= 0) {
            return;
        }
        localFileHeader.setExtraDataRecords(this.readExtraDataRecords(n2));
    }

    private ArrayList readExtraDataRecords(int n2) throws ZipException {
        if (n2 <= 0) {
            return null;
        }
        try {
            int n3;
            byte[] arrby = new byte[n2];
            this.zip4jRaf.read(arrby);
            ArrayList<ExtraDataRecord> arrayList = new ArrayList<ExtraDataRecord>();
            for (int i2 = 0; i2 < n2; i2 += n3) {
                ExtraDataRecord extraDataRecord = new ExtraDataRecord();
                int n4 = Raw.readShortLittleEndian(arrby, i2);
                extraDataRecord.setHeader(n4);
                n3 = Raw.readShortLittleEndian(arrby, i2 += 2);
                if (2 + n3 > n2 && 2 + (n3 = (int)Raw.readShortBigEndian(arrby, i2)) > n2) break;
                extraDataRecord.setSizeOfData(n3);
                i2 += 2;
                if (n3 > 0) {
                    byte[] arrby2 = new byte[n3];
                    System.arraycopy(arrby, i2, arrby2, 0, n3);
                    extraDataRecord.setData(arrby2);
                }
                arrayList.add(extraDataRecord);
            }
            if (arrayList.size() > 0) {
                return arrayList;
            }
            return null;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private Zip64EndCentralDirLocator readZip64EndCentralDirLocator() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read Zip64EndCentralDirLocator");
        }
        try {
            Zip64EndCentralDirLocator zip64EndCentralDirLocator = new Zip64EndCentralDirLocator();
            this.setFilePointerToReadZip64EndCentralDirLoc();
            byte[] arrby = new byte[4];
            byte[] arrby2 = new byte[8];
            this.readIntoBuff(this.zip4jRaf, arrby);
            int n2 = Raw.readIntLittleEndian(arrby, 0);
            if ((long)n2 != 117853008) {
                this.zipModel.setZip64Format(false);
                return null;
            }
            this.zipModel.setZip64Format(true);
            zip64EndCentralDirLocator.setSignature(n2);
            this.readIntoBuff(this.zip4jRaf, arrby);
            zip64EndCentralDirLocator.setNoOfDiskStartOfZip64EndOfCentralDirRec(Raw.readIntLittleEndian(arrby, 0));
            this.readIntoBuff(this.zip4jRaf, arrby2);
            zip64EndCentralDirLocator.setOffsetZip64EndOfCentralDirRec(Raw.readLongLittleEndian(arrby2, 0));
            this.readIntoBuff(this.zip4jRaf, arrby);
            zip64EndCentralDirLocator.setTotNumberOfDiscs(Raw.readIntLittleEndian(arrby, 0));
            return zip64EndCentralDirLocator;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

    private Zip64EndCentralDirRecord readZip64EndCentralDirRec() throws ZipException {
        if (this.zipModel.getZip64EndCentralDirLocator() == null) {
            throw new ZipException("invalid zip64 end of central directory locator");
        }
        long l2 = this.zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec();
        if (l2 < 0) {
            throw new ZipException("invalid offset for start of end of central directory record");
        }
        try {
            this.zip4jRaf.seek(l2);
            Zip64EndCentralDirRecord zip64EndCentralDirRecord = new Zip64EndCentralDirRecord();
            byte[] arrby = new byte[2];
            byte[] arrby2 = new byte[4];
            byte[] arrby3 = new byte[8];
            this.readIntoBuff(this.zip4jRaf, arrby2);
            int n2 = Raw.readIntLittleEndian(arrby2, 0);
            if ((long)n2 != 101075792) {
                throw new ZipException("invalid signature for zip64 end of central directory record");
            }
            zip64EndCentralDirRecord.setSignature(n2);
            this.readIntoBuff(this.zip4jRaf, arrby3);
            zip64EndCentralDirRecord.setSizeOfZip64EndCentralDirRec(Raw.readLongLittleEndian(arrby3, 0));
            this.readIntoBuff(this.zip4jRaf, arrby);
            zip64EndCentralDirRecord.setVersionMadeBy(Raw.readShortLittleEndian(arrby, 0));
            this.readIntoBuff(this.zip4jRaf, arrby);
            zip64EndCentralDirRecord.setVersionNeededToExtract(Raw.readShortLittleEndian(arrby, 0));
            this.readIntoBuff(this.zip4jRaf, arrby2);
            zip64EndCentralDirRecord.setNoOfThisDisk(Raw.readIntLittleEndian(arrby2, 0));
            this.readIntoBuff(this.zip4jRaf, arrby2);
            zip64EndCentralDirRecord.setNoOfThisDiskStartOfCentralDir(Raw.readIntLittleEndian(arrby2, 0));
            this.readIntoBuff(this.zip4jRaf, arrby3);
            zip64EndCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readLongLittleEndian(arrby3, 0));
            this.readIntoBuff(this.zip4jRaf, arrby3);
            zip64EndCentralDirRecord.setTotNoOfEntriesInCentralDir(Raw.readLongLittleEndian(arrby3, 0));
            this.readIntoBuff(this.zip4jRaf, arrby3);
            zip64EndCentralDirRecord.setSizeOfCentralDir(Raw.readLongLittleEndian(arrby3, 0));
            this.readIntoBuff(this.zip4jRaf, arrby3);
            zip64EndCentralDirRecord.setOffsetStartCenDirWRTStartDiskNo(Raw.readLongLittleEndian(arrby3, 0));
            long l3 = zip64EndCentralDirRecord.getSizeOfZip64EndCentralDirRec() - 44;
            if (l3 > 0) {
                byte[] arrby4 = new byte[(int)l3];
                this.readIntoBuff(this.zip4jRaf, arrby4);
                zip64EndCentralDirRecord.setExtensibleDataSector(arrby4);
            }
            return zip64EndCentralDirRecord;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private void readAndSaveZip64ExtendedInfo(FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (fileHeader.getExtraDataRecords() == null || fileHeader.getExtraDataRecords().size() <= 0) {
            return;
        }
        Zip64ExtendedInfo zip64ExtendedInfo = this.readZip64ExtendedInfo(fileHeader.getExtraDataRecords(), fileHeader.getUncompressedSize(), fileHeader.getCompressedSize(), fileHeader.getOffsetLocalHeader(), fileHeader.getDiskNumberStart());
        if (zip64ExtendedInfo != null) {
            fileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
            if (zip64ExtendedInfo.getUnCompressedSize() != -1) {
                fileHeader.setUncompressedSize(zip64ExtendedInfo.getUnCompressedSize());
            }
            if (zip64ExtendedInfo.getCompressedSize() != -1) {
                fileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
            }
            if (zip64ExtendedInfo.getOffsetLocalHeader() != -1) {
                fileHeader.setOffsetLocalHeader(zip64ExtendedInfo.getOffsetLocalHeader());
            }
            if (zip64ExtendedInfo.getDiskNumberStart() != -1) {
                fileHeader.setDiskNumberStart(zip64ExtendedInfo.getDiskNumberStart());
            }
        }
    }

    private void readAndSaveZip64ExtendedInfo(LocalFileHeader localFileHeader) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (localFileHeader.getExtraDataRecords() == null || localFileHeader.getExtraDataRecords().size() <= 0) {
            return;
        }
        Zip64ExtendedInfo zip64ExtendedInfo = this.readZip64ExtendedInfo(localFileHeader.getExtraDataRecords(), localFileHeader.getUncompressedSize(), localFileHeader.getCompressedSize(), -1, -1);
        if (zip64ExtendedInfo != null) {
            localFileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
            if (zip64ExtendedInfo.getUnCompressedSize() != -1) {
                localFileHeader.setUncompressedSize(zip64ExtendedInfo.getUnCompressedSize());
            }
            if (zip64ExtendedInfo.getCompressedSize() != -1) {
                localFileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
            }
        }
    }

    private Zip64ExtendedInfo readZip64ExtendedInfo(ArrayList arrayList, long l2, long l3, long l4, int n2) throws ZipException {
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            long l5;
            ExtraDataRecord extraDataRecord = (ExtraDataRecord)arrayList.get(i2);
            if (extraDataRecord == null || extraDataRecord.getHeader() != 1) continue;
            Zip64ExtendedInfo zip64ExtendedInfo = new Zip64ExtendedInfo();
            byte[] arrby = extraDataRecord.getData();
            if (extraDataRecord.getSizeOfData() <= 0) break;
            byte[] arrby2 = new byte[8];
            byte[] arrby3 = new byte[4];
            int n3 = 0;
            boolean bl = false;
            if ((l2 & 65535) == 65535 && n3 < extraDataRecord.getSizeOfData()) {
                System.arraycopy(arrby, n3, arrby2, 0, 8);
                l5 = Raw.readLongLittleEndian(arrby2, 0);
                zip64ExtendedInfo.setUnCompressedSize(l5);
                n3 += 8;
                bl = true;
            }
            if ((l3 & 65535) == 65535 && n3 < extraDataRecord.getSizeOfData()) {
                System.arraycopy(arrby, n3, arrby2, 0, 8);
                l5 = Raw.readLongLittleEndian(arrby2, 0);
                zip64ExtendedInfo.setCompressedSize(l5);
                n3 += 8;
                bl = true;
            }
            if ((l4 & 65535) == 65535 && n3 < extraDataRecord.getSizeOfData()) {
                System.arraycopy(arrby, n3, arrby2, 0, 8);
                l5 = Raw.readLongLittleEndian(arrby2, 0);
                zip64ExtendedInfo.setOffsetLocalHeader(l5);
                n3 += 8;
                bl = true;
            }
            if ((n2 & 65535) == 65535 && n3 < extraDataRecord.getSizeOfData()) {
                System.arraycopy(arrby, n3, arrby3, 0, 4);
                int n4 = Raw.readIntLittleEndian(arrby3, 0);
                zip64ExtendedInfo.setDiskNumberStart(n4);
                n3 += 8;
                bl = true;
            }
            if (!bl) break;
            return zip64ExtendedInfo;
        }
        return null;
    }

    private void setFilePointerToReadZip64EndCentralDirLoc() throws ZipException {
        try {
            byte[] arrby = new byte[4];
            long l2 = this.zip4jRaf.length() - 22;
            do {
                this.zip4jRaf.seek(l2--);
            } while ((long)Raw.readLeInt(this.zip4jRaf, arrby) != 101010256);
            this.zip4jRaf.seek(this.zip4jRaf.getFilePointer() - 4 - 4 - 8 - 4 - 4);
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    public LocalFileHeader readLocalFileHeader(FileHeader fileHeader) throws ZipException {
        Zip64ExtendedInfo zip64ExtendedInfo;
        if (fileHeader == null || this.zip4jRaf == null) {
            throw new ZipException("invalid read parameters for local header");
        }
        long l2 = fileHeader.getOffsetLocalHeader();
        if (fileHeader.getZip64ExtendedInfo() != null && (zip64ExtendedInfo = fileHeader.getZip64ExtendedInfo()).getOffsetLocalHeader() > 0) {
            l2 = fileHeader.getOffsetLocalHeader();
        }
        if (l2 < 0) {
            throw new ZipException("invalid local header offset");
        }
        try {
            this.zip4jRaf.seek(l2);
            int n2 = 0;
            LocalFileHeader localFileHeader = new LocalFileHeader();
            byte[] arrby = new byte[2];
            byte[] arrby2 = new byte[4];
            byte[] arrby3 = new byte[8];
            this.readIntoBuff(this.zip4jRaf, arrby2);
            int n3 = Raw.readIntLittleEndian(arrby2, 0);
            if ((long)n3 != 67324752) {
                throw new ZipException("invalid local header signature for file: " + fileHeader.getFileName());
            }
            localFileHeader.setSignature(n3);
            n2 += 4;
            this.readIntoBuff(this.zip4jRaf, arrby);
            localFileHeader.setVersionNeededToExtract(Raw.readShortLittleEndian(arrby, 0));
            n2 += 2;
            this.readIntoBuff(this.zip4jRaf, arrby);
            localFileHeader.setFileNameUTF8Encoded((Raw.readShortLittleEndian(arrby, 0) & 2048) != 0);
            byte by = arrby[0];
            int n4 = by & 1;
            if (n4 != 0) {
                localFileHeader.setEncrypted(true);
            }
            localFileHeader.setGeneralPurposeFlag(arrby);
            n2 += 2;
            String string = Integer.toBinaryString(by);
            if (string.length() >= 4) {
                localFileHeader.setDataDescriptorExists(string.charAt(3) == '1');
            }
            this.readIntoBuff(this.zip4jRaf, arrby);
            localFileHeader.setCompressionMethod(Raw.readShortLittleEndian(arrby, 0));
            n2 += 2;
            this.readIntoBuff(this.zip4jRaf, arrby2);
            localFileHeader.setLastModFileTime(Raw.readIntLittleEndian(arrby2, 0));
            n2 += 4;
            this.readIntoBuff(this.zip4jRaf, arrby2);
            localFileHeader.setCrc32(Raw.readIntLittleEndian(arrby2, 0));
            localFileHeader.setCrcBuff((byte[])arrby2.clone());
            n2 += 4;
            this.readIntoBuff(this.zip4jRaf, arrby2);
            arrby3 = this.getLongByteFromIntByte(arrby2);
            localFileHeader.setCompressedSize(Raw.readLongLittleEndian(arrby3, 0));
            n2 += 4;
            this.readIntoBuff(this.zip4jRaf, arrby2);
            arrby3 = this.getLongByteFromIntByte(arrby2);
            localFileHeader.setUncompressedSize(Raw.readLongLittleEndian(arrby3, 0));
            n2 += 4;
            this.readIntoBuff(this.zip4jRaf, arrby);
            int n5 = Raw.readShortLittleEndian(arrby, 0);
            localFileHeader.setFileNameLength(n5);
            n2 += 2;
            this.readIntoBuff(this.zip4jRaf, arrby);
            int n6 = Raw.readShortLittleEndian(arrby, 0);
            localFileHeader.setExtraFieldLength(n6);
            n2 += 2;
            if (n5 > 0) {
                byte[] arrby4 = new byte[n5];
                this.readIntoBuff(this.zip4jRaf, arrby4);
                String string2 = Zip4jUtil.decodeFileName(arrby4, localFileHeader.isFileNameUTF8Encoded());
                if (string2 == null) {
                    throw new ZipException("file name is null, cannot assign file name to local file header");
                }
                if (string2.indexOf(":" + System.getProperty("file.separator")) >= 0) {
                    string2 = string2.substring(string2.indexOf(":" + System.getProperty("file.separator")) + 2);
                }
                localFileHeader.setFileName(string2);
                n2 += n5;
            } else {
                localFileHeader.setFileName(null);
            }
            this.readAndSaveExtraDataRecord(localFileHeader);
            localFileHeader.setOffsetStartOfData(l2 + (long)(n2 += n6));
            localFileHeader.setPassword(fileHeader.getPassword());
            this.readAndSaveZip64ExtendedInfo(localFileHeader);
            this.readAndSaveAESExtraDataRecord(localFileHeader);
            if (localFileHeader.isEncrypted() && localFileHeader.getEncryptionMethod() != 99) {
                if ((by & 64) == 64) {
                    localFileHeader.setEncryptionMethod(1);
                } else {
                    localFileHeader.setEncryptionMethod(0);
                }
            }
            if (localFileHeader.getCrc32() <= 0) {
                localFileHeader.setCrc32(fileHeader.getCrc32());
                localFileHeader.setCrcBuff(fileHeader.getCrcBuff());
            }
            if (localFileHeader.getCompressedSize() <= 0) {
                localFileHeader.setCompressedSize(fileHeader.getCompressedSize());
            }
            if (localFileHeader.getUncompressedSize() <= 0) {
                localFileHeader.setUncompressedSize(fileHeader.getUncompressedSize());
            }
            return localFileHeader;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private void readAndSaveAESExtraDataRecord(FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (fileHeader.getExtraDataRecords() == null || fileHeader.getExtraDataRecords().size() <= 0) {
            return;
        }
        AESExtraDataRecord aESExtraDataRecord = this.readAESExtraDataRecord(fileHeader.getExtraDataRecords());
        if (aESExtraDataRecord != null) {
            fileHeader.setAesExtraDataRecord(aESExtraDataRecord);
            fileHeader.setEncryptionMethod(99);
        }
    }

    private void readAndSaveAESExtraDataRecord(LocalFileHeader localFileHeader) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (localFileHeader.getExtraDataRecords() == null || localFileHeader.getExtraDataRecords().size() <= 0) {
            return;
        }
        AESExtraDataRecord aESExtraDataRecord = this.readAESExtraDataRecord(localFileHeader.getExtraDataRecords());
        if (aESExtraDataRecord != null) {
            localFileHeader.setAesExtraDataRecord(aESExtraDataRecord);
            localFileHeader.setEncryptionMethod(99);
        }
    }

    private AESExtraDataRecord readAESExtraDataRecord(ArrayList arrayList) throws ZipException {
        if (arrayList == null) {
            return null;
        }
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            ExtraDataRecord extraDataRecord = (ExtraDataRecord)arrayList.get(i2);
            if (extraDataRecord == null || extraDataRecord.getHeader() != 39169) continue;
            if (extraDataRecord.getData() == null) {
                throw new ZipException("corrput AES extra data records");
            }
            AESExtraDataRecord aESExtraDataRecord = new AESExtraDataRecord();
            aESExtraDataRecord.setSignature(39169);
            aESExtraDataRecord.setDataSize(extraDataRecord.getSizeOfData());
            byte[] arrby = extraDataRecord.getData();
            aESExtraDataRecord.setVersionNumber(Raw.readShortLittleEndian(arrby, 0));
            byte[] arrby2 = new byte[2];
            System.arraycopy(arrby, 2, arrby2, 0, 2);
            aESExtraDataRecord.setVendorID(new String(arrby2));
            aESExtraDataRecord.setAesStrength(arrby[4] & 255);
            aESExtraDataRecord.setCompressionMethod(Raw.readShortLittleEndian(arrby, 5));
            return aESExtraDataRecord;
        }
        return null;
    }

    private byte[] readIntoBuff(RandomAccessFile randomAccessFile, byte[] arrby) throws ZipException {
        try {
            if (randomAccessFile.read(arrby, 0, arrby.length) != -1) {
                return arrby;
            }
            throw new ZipException("unexpected end of file when reading short buff");
        }
        catch (IOException iOException) {
            throw new ZipException("IOException when reading short buff", iOException);
        }
    }

    private byte[] getLongByteFromIntByte(byte[] arrby) throws ZipException {
        if (arrby == null) {
            throw new ZipException("input parameter is null, cannot expand to 8 bytes");
        }
        if (arrby.length != 4) {
            throw new ZipException("invalid byte length, cannot expand to 8 bytes");
        }
        byte[] arrby2 = new byte[]{arrby[0], arrby[1], arrby[2], arrby[3], 0, 0, 0, 0};
        return arrby2;
    }
}

