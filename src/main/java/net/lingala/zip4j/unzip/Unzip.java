/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.unzip;

import java.io.File;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.unzip.UnzipEngine;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class Unzip {
    private ZipModel zipModel;

    public Unzip(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("ZipModel is null");
        }
        this.zipModel = zipModel;
    }

    public void extractFile(final FileHeader fileHeader, final String string, final UnzipParameters unzipParameters, final String string2, final ProgressMonitor progressMonitor, boolean bl) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        progressMonitor.setCurrentOperation(1);
        progressMonitor.setTotalWork(fileHeader.getCompressedSize());
        progressMonitor.setState(1);
        progressMonitor.setPercentDone(0);
        progressMonitor.setFileName(fileHeader.getFileName());
        if (bl) {
            Thread thread = new Thread("Zip4j"){

                public void run() {
                    try {
                        Unzip.this.initExtractFile(fileHeader, string, unzipParameters, string2, progressMonitor);
                        progressMonitor.endProgressMonitorSuccess();
                    }
                    catch (ZipException zipException) {
                        // empty catch block
                    }
                }
            };
            thread.start();
        } else {
            this.initExtractFile(fileHeader, string, unzipParameters, string2, progressMonitor);
            progressMonitor.endProgressMonitorSuccess();
        }
    }

    private void initExtractFile(FileHeader fileHeader, String string, UnzipParameters unzipParameters, String string2, ProgressMonitor progressMonitor) throws ZipException {
        block12 : {
            if (fileHeader == null) {
                throw new ZipException("fileHeader is null");
            }
            try {
                progressMonitor.setFileName(fileHeader.getFileName());
                if (!string.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                    string = string + InternalZipConstants.FILE_SEPARATOR;
                }
                if (fileHeader.isDirectory()) {
                    try {
                        String string3 = fileHeader.getFileName();
                        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string3)) {
                            return;
                        }
                        String string4 = string + string3;
                        File file = new File(string4);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        break block12;
                    }
                    catch (Exception exception) {
                        progressMonitor.endProgressMonitorError(exception);
                        throw new ZipException(exception);
                    }
                }
                this.checkOutputDirectoryStructure(fileHeader, string, string2);
                UnzipEngine unzipEngine = new UnzipEngine(this.zipModel, fileHeader);
                try {
                    unzipEngine.unzipFile(progressMonitor, string, string2, unzipParameters);
                }
                catch (Exception exception) {
                    progressMonitor.endProgressMonitorError(exception);
                    throw new ZipException(exception);
                }
            }
            catch (ZipException zipException) {
                progressMonitor.endProgressMonitorError(zipException);
                throw zipException;
            }
            catch (Exception exception) {
                progressMonitor.endProgressMonitorError(exception);
                throw new ZipException(exception);
            }
        }
    }

    public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException {
        UnzipEngine unzipEngine = new UnzipEngine(this.zipModel, fileHeader);
        return unzipEngine.getInputStream();
    }

    private void checkOutputDirectoryStructure(FileHeader fileHeader, String string, String string2) throws ZipException {
        if (fileHeader == null || !Zip4jUtil.isStringNotNullAndNotEmpty(string)) {
            throw new ZipException("Cannot check output directory structure...one of the parameters was null");
        }
        String string3 = fileHeader.getFileName();
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            string3 = string2;
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string3)) {
            return;
        }
        String string4 = string + string3;
        try {
            File file = new File(string4);
            String string5 = file.getParent();
            File file2 = new File(string5);
            if (!file2.exists()) {
                file2.mkdirs();
            }
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
    }

}

