/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.unzip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.zip.CRC32;
import net.lingala.zip4j.core.HeaderReader;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.crypto.StandardDecrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.BaseInputStream;
import net.lingala.zip4j.io.InflaterInputStream;
import net.lingala.zip4j.io.PartInputStream;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.unzip.UnzipUtil;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class UnzipEngine {
    private ZipModel zipModel;
    private FileHeader fileHeader;
    private int currSplitFileCounter = 0;
    private LocalFileHeader localFileHeader;
    private IDecrypter decrypter;
    private CRC32 crc;

    public UnzipEngine(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        if (zipModel == null || fileHeader == null) {
            throw new ZipException("Invalid parameters passed to StoreUnzip. One or more of the parameters were null");
        }
        this.zipModel = zipModel;
        this.fileHeader = fileHeader;
        this.crc = new CRC32();
    }

    public void unzipFile(ProgressMonitor progressMonitor, String string, String string2, UnzipParameters unzipParameters) throws ZipException {
        if (this.zipModel == null || this.fileHeader == null || !Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("Invalid parameters passed during unzipping file. One or more of the parameters were null");
        }
        ZipInputStream zipInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            byte[] arrby = new byte[4096];
            int n2 = -1;
            zipInputStream = this.getInputStream();
            fileOutputStream = this.getOutputStream(string, string2);
            while ((n2 = zipInputStream.read(arrby)) != -1) {
                fileOutputStream.write(arrby, 0, n2);
                progressMonitor.updateWorkCompleted(n2);
                if (!progressMonitor.isCancelAllTasks()) continue;
                progressMonitor.setResult(3);
                progressMonitor.setState(0);
                return;
            }
            this.closeStreams(zipInputStream, fileOutputStream);
            UnzipUtil.applyFileAttributes(this.fileHeader, new File(this.getOutputFileNameWithPath(string, string2)), unzipParameters);
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
        finally {
            this.closeStreams(zipInputStream, fileOutputStream);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ZipInputStream getInputStream() throws ZipException {
        if (this.fileHeader == null) {
            throw new ZipException("file header is null, cannot get inputstream");
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = this.createFileHandler("r");
            String string = "local header and file header do not match";
            if (!this.checkLocalHeader()) {
                throw new ZipException(string);
            }
            this.init(randomAccessFile);
            long l2 = this.localFileHeader.getCompressedSize();
            long l3 = this.localFileHeader.getOffsetStartOfData();
            if (this.localFileHeader.isEncrypted()) {
                if (this.localFileHeader.getEncryptionMethod() == 99) {
                    if (!(this.decrypter instanceof AESDecrypter)) throw new ZipException("invalid decryptor when trying to calculate compressed size for AES encrypted file: " + this.fileHeader.getFileName());
                    l2 -= (long)(((AESDecrypter)this.decrypter).getSaltLength() + ((AESDecrypter)this.decrypter).getPasswordVerifierLength() + 10);
                    l3 += (long)(((AESDecrypter)this.decrypter).getSaltLength() + ((AESDecrypter)this.decrypter).getPasswordVerifierLength());
                } else if (this.localFileHeader.getEncryptionMethod() == 0) {
                    l2 -= 12;
                    l3 += 12;
                }
            }
            int n2 = this.fileHeader.getCompressionMethod();
            if (this.fileHeader.getEncryptionMethod() == 99) {
                if (this.fileHeader.getAesExtraDataRecord() == null) throw new ZipException("AESExtraDataRecord does not exist for AES encrypted file: " + this.fileHeader.getFileName());
                n2 = this.fileHeader.getAesExtraDataRecord().getCompressionMethod();
            }
            randomAccessFile.seek(l3);
            switch (n2) {
                case 0: {
                    return new ZipInputStream(new PartInputStream(randomAccessFile, l3, l2, this));
                }
                case 8: {
                    return new ZipInputStream(new InflaterInputStream(randomAccessFile, l3, l2, this));
                }
            }
            throw new ZipException("compression type not supported");
        }
        catch (ZipException zipException) {
            if (randomAccessFile == null) throw zipException;
            try {
                randomAccessFile.close();
                throw zipException;
            }
            catch (IOException iOException) {
                // empty catch block
            }
            throw zipException;
        }
        catch (Exception exception) {
            if (randomAccessFile == null) throw new ZipException(exception);
            try {
                randomAccessFile.close();
                throw new ZipException(exception);
            }
            catch (IOException iOException) {
                // empty catch block
            }
            throw new ZipException(exception);
        }
    }

    private void init(RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("local file header is null, cannot initialize input stream");
        }
        try {
            this.initDecrypter(randomAccessFile);
        }
        catch (ZipException zipException) {
            throw zipException;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

    private void initDecrypter(RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("local file header is null, cannot init decrypter");
        }
        if (this.localFileHeader.isEncrypted()) {
            if (this.localFileHeader.getEncryptionMethod() == 0) {
                this.decrypter = new StandardDecrypter(this.fileHeader, this.getStandardDecrypterHeaderBytes(randomAccessFile));
            } else if (this.localFileHeader.getEncryptionMethod() == 99) {
                this.decrypter = new AESDecrypter(this.localFileHeader, this.getAESSalt(randomAccessFile), this.getAESPasswordVerifier(randomAccessFile));
            } else {
                throw new ZipException("unsupported encryption method");
            }
        }
    }

    private byte[] getStandardDecrypterHeaderBytes(RandomAccessFile randomAccessFile) throws ZipException {
        try {
            byte[] arrby = new byte[12];
            randomAccessFile.seek(this.localFileHeader.getOffsetStartOfData());
            randomAccessFile.read(arrby, 0, 12);
            return arrby;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

    private byte[] getAESSalt(RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader.getAesExtraDataRecord() == null) {
            return null;
        }
        try {
            AESExtraDataRecord aESExtraDataRecord = this.localFileHeader.getAesExtraDataRecord();
            byte[] arrby = new byte[this.calculateAESSaltLength(aESExtraDataRecord)];
            randomAccessFile.seek(this.localFileHeader.getOffsetStartOfData());
            randomAccessFile.read(arrby);
            return arrby;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private byte[] getAESPasswordVerifier(RandomAccessFile randomAccessFile) throws ZipException {
        try {
            byte[] arrby = new byte[2];
            randomAccessFile.read(arrby);
            return arrby;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private int calculateAESSaltLength(AESExtraDataRecord aESExtraDataRecord) throws ZipException {
        if (aESExtraDataRecord == null) {
            throw new ZipException("unable to determine salt length: AESExtraDataRecord is null");
        }
        switch (aESExtraDataRecord.getAesStrength()) {
            case 1: {
                return 8;
            }
            case 2: {
                return 12;
            }
            case 3: {
                return 16;
            }
        }
        throw new ZipException("unable to determine salt length: invalid aes key strength");
    }

    public void checkCRC() throws ZipException {
        if (this.fileHeader != null) {
            if (this.fileHeader.getEncryptionMethod() == 99) {
                if (this.decrypter != null && this.decrypter instanceof AESDecrypter) {
                    byte[] arrby = ((AESDecrypter)this.decrypter).getCalculatedAuthenticationBytes();
                    byte[] arrby2 = ((AESDecrypter)this.decrypter).getStoredMac();
                    byte[] arrby3 = new byte[10];
                    if (arrby3 == null || arrby2 == null) {
                        throw new ZipException("CRC (MAC) check failed for " + this.fileHeader.getFileName());
                    }
                    System.arraycopy(arrby, 0, arrby3, 0, 10);
                    if (!Arrays.equals(arrby3, arrby2)) {
                        throw new ZipException("invalid CRC (MAC) for file: " + this.fileHeader.getFileName());
                    }
                }
            } else {
                long l2 = this.crc.getValue() & 0xFFFFFFFFL;
                if (l2 != this.fileHeader.getCrc32()) {
                    String string = "invalid CRC for file: " + this.fileHeader.getFileName();
                    if (this.localFileHeader.isEncrypted() && this.localFileHeader.getEncryptionMethod() == 0) {
                        string = string + " - Wrong Password?";
                    }
                    throw new ZipException(string);
                }
            }
        }
    }

    private boolean checkLocalHeader() throws ZipException {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = this.checkSplitFile();
            if (randomAccessFile == null) {
                randomAccessFile = new RandomAccessFile(new File(this.zipModel.getZipFile()), "r");
            }
            HeaderReader headerReader = new HeaderReader(randomAccessFile);
            this.localFileHeader = headerReader.readLocalFileHeader(this.fileHeader);
            if (this.localFileHeader == null) {
                throw new ZipException("error reading local file header. Is this a valid zip file?");
            }
            if (this.localFileHeader.getCompressionMethod() != this.fileHeader.getCompressionMethod()) {
                boolean bl = false;
                return bl;
            }
            boolean bl = true;
            return bl;
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw new ZipException(fileNotFoundException);
        }
        finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                }
                catch (IOException iOException) {
                }
                catch (Exception exception) {}
            }
        }
    }

    private RandomAccessFile checkSplitFile() throws ZipException {
        if (this.zipModel.isSplitArchive()) {
            int n2 = this.fileHeader.getDiskNumberStart();
            this.currSplitFileCounter = n2 + 1;
            String string = this.zipModel.getZipFile();
            String string2 = null;
            string2 = n2 == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk() ? this.zipModel.getZipFile() : (n2 >= 9 ? string.substring(0, string.lastIndexOf(".")) + ".z" + (n2 + 1) : string.substring(0, string.lastIndexOf(".")) + ".z0" + (n2 + 1));
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(string2, "r");
                if (this.currSplitFileCounter == 1) {
                    byte[] arrby = new byte[4];
                    randomAccessFile.read(arrby);
                    if ((long)Raw.readIntLittleEndian(arrby, 0) != 134695760) {
                        throw new ZipException("invalid first part split file signature");
                    }
                }
                return randomAccessFile;
            }
            catch (FileNotFoundException fileNotFoundException) {
                throw new ZipException(fileNotFoundException);
            }
            catch (IOException iOException) {
                throw new ZipException(iOException);
            }
        }
        return null;
    }

    private RandomAccessFile createFileHandler(String string) throws ZipException {
        if (this.zipModel == null || !Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getZipFile())) {
            throw new ZipException("input parameter is null in getFilePointer");
        }
        try {
            RandomAccessFile randomAccessFile = null;
            randomAccessFile = this.zipModel.isSplitArchive() ? this.checkSplitFile() : new RandomAccessFile(new File(this.zipModel.getZipFile()), string);
            return randomAccessFile;
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw new ZipException(fileNotFoundException);
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

    private FileOutputStream getOutputStream(String string, String string2) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("invalid output path");
        }
        try {
            File file = new File(this.getOutputFileNameWithPath(string, string2));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            return fileOutputStream;
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw new ZipException(fileNotFoundException);
        }
    }

    private String getOutputFileNameWithPath(String string, String string2) throws ZipException {
        String string3 = null;
        string3 = Zip4jUtil.isStringNotNullAndNotEmpty(string2) ? string2 : this.fileHeader.getFileName();
        return string + System.getProperty("file.separator") + string3;
    }

    public RandomAccessFile startNextSplitFile() throws IOException, FileNotFoundException {
        String string = this.zipModel.getZipFile();
        String string2 = null;
        string2 = this.currSplitFileCounter == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk() ? this.zipModel.getZipFile() : (this.currSplitFileCounter >= 9 ? string.substring(0, string.lastIndexOf(".")) + ".z" + (this.currSplitFileCounter + 1) : string.substring(0, string.lastIndexOf(".")) + ".z0" + (this.currSplitFileCounter + 1));
        ++this.currSplitFileCounter;
        try {
            if (!Zip4jUtil.checkFileExists(string2)) {
                throw new IOException("zip split file does not exist: " + string2);
            }
        }
        catch (ZipException zipException) {
            throw new IOException(zipException.getMessage());
        }
        return new RandomAccessFile(string2, "r");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void closeStreams(InputStream inputStream, OutputStream outputStream) throws ZipException {
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
        }
        catch (IOException iOException) {
            if (iOException != null && Zip4jUtil.isStringNotNullAndNotEmpty(iOException.getMessage()) && iOException.getMessage().indexOf(" - Wrong Password?") >= 0) {
                throw new ZipException(iOException.getMessage());
            }
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
            }
            catch (IOException iOException) {}
        }
    }

    public void updateCRC(int n2) {
        this.crc.update(n2);
    }

    public void updateCRC(byte[] arrby, int n2, int n3) {
        if (arrby != null) {
            this.crc.update(arrby, n2, n3);
        }
    }

    public FileHeader getFileHeader() {
        return this.fileHeader;
    }

    public IDecrypter getDecrypter() {
        return this.decrypter;
    }

    public ZipModel getZipModel() {
        return this.zipModel;
    }

    public LocalFileHeader getLocalFileHeader() {
        return this.localFileHeader;
    }
}

