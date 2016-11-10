/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.core.HeaderReader;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.unzip.Unzip;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipFile {
    private String file;
    private int mode;
    private ZipModel zipModel;
    private ProgressMonitor progressMonitor;
    private boolean runInThread;
    private String fileNameCharset;

    public ZipFile(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("Input zip file parameter is not null", 1);
        }
        this.file = file.getPath();
        this.mode = 2;
        this.progressMonitor = new ProgressMonitor();
        this.runInThread = false;
    }

    private void readZipInfo() throws ZipException {
        if (!Zip4jUtil.checkFileExists(this.file)) {
            throw new ZipException("zip file does not exist");
        }
        if (!Zip4jUtil.checkFileReadAccess(this.file)) {
            throw new ZipException("no read access for the input zip file");
        }
        if (this.mode != 2) {
            throw new ZipException("Invalid mode");
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(new File(this.file), "r");
            if (this.zipModel == null) {
                HeaderReader headerReader = new HeaderReader(randomAccessFile);
                this.zipModel = headerReader.readAllHeaders(this.fileNameCharset);
                if (this.zipModel != null) {
                    this.zipModel.setZipFile(this.file);
                }
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw new ZipException(fileNotFoundException);
        }
        finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    public void extractFile(FileHeader fileHeader, String string) throws ZipException {
        this.extractFile(fileHeader, string, null);
    }

    public void extractFile(FileHeader fileHeader, String string, UnzipParameters unzipParameters) throws ZipException {
        this.extractFile(fileHeader, string, unzipParameters, null);
    }

    public void extractFile(FileHeader fileHeader, String string, UnzipParameters unzipParameters, String string2) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("input file header is null, cannot extract file");
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("destination path is empty or null, cannot extract file");
        }
        this.readZipInfo();
        if (this.progressMonitor.getState() == 1) {
            throw new ZipException("invalid operation - Zip4j is in busy state");
        }
        fileHeader.extractFile(this.zipModel, string, unzipParameters, string2, this.progressMonitor, this.runInThread);
    }

    public List getFileHeaders() throws ZipException {
        this.readZipInfo();
        if (this.zipModel == null || this.zipModel.getCentralDirectory() == null) {
            return null;
        }
        return this.zipModel.getCentralDirectory().getFileHeaders();
    }

    public FileHeader getFileHeader(String string) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("input file name is emtpy or null, cannot get FileHeader");
        }
        this.readZipInfo();
        if (this.zipModel == null || this.zipModel.getCentralDirectory() == null) {
            return null;
        }
        return Zip4jUtil.getFileHeader(this.zipModel, string);
    }

    private void checkZipModel() throws ZipException {
        if (this.zipModel == null) {
            if (Zip4jUtil.checkFileExists(this.file)) {
                this.readZipInfo();
            } else {
                this.createNewZipModel();
            }
        }
    }

    private void createNewZipModel() {
        this.zipModel = new ZipModel();
        this.zipModel.setZipFile(this.file);
        this.zipModel.setFileNameCharset(this.fileNameCharset);
    }

    public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("FileHeader is null, cannot get InputStream");
        }
        this.checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("zip model is null, cannot get inputstream");
        }
        Unzip unzip = new Unzip(this.zipModel);
        return unzip.getInputStream(fileHeader);
    }
}

