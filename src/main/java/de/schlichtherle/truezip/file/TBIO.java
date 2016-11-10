/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.file;

import de.schlichtherle.truezip.entry.Entry;
import de.schlichtherle.truezip.file.TArchiveDetector;
import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.fs.FsCompositeDriver;
import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsEntryName;
import de.schlichtherle.truezip.fs.FsInputOption;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsMountPoint;
import de.schlichtherle.truezip.fs.FsOutputOption;
import de.schlichtherle.truezip.fs.FsPath;
import de.schlichtherle.truezip.io.Paths;
import de.schlichtherle.truezip.socket.IOSocket;
import de.schlichtherle.truezip.socket.InputSocket;
import de.schlichtherle.truezip.socket.OutputSocket;
import de.schlichtherle.truezip.util.BitField;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

final class TBIO {
    static void mv(File file, File file2, TArchiveDetector tArchiveDetector) throws IOException {
        TBIO.checkContains(file, file2);
        if (file2.exists()) {
            throw new IOException(file2 + " (destination exists already)");
        }
        TBIO.mv0(file, file2, tArchiveDetector);
    }

    private static void mv0(File file, File file2, TArchiveDetector tArchiveDetector) throws IOException {
        if (file.isDirectory()) {
            boolean bl;
            long l2 = file.lastModified();
            boolean bl2 = file instanceof TFile && null != ((TFile)file).getInnerArchive();
            boolean bl3 = file2 instanceof TFile && null != ((TFile)file2).getInnerArchive();
            boolean bl4 = bl = bl2 && 0 >= l2;
            if (!(bl && bl3 && TConfig.get().isLenient() || file2.mkdir() || file2.isDirectory())) {
                throw new IOException(file2 + " (not a directory)");
            }
            Object[] arrobject = file.list();
            if (null == arrobject) {
                throw new IOException(file2 + " (cannot list directory)");
            }
            if (!bl2 && bl3) {
                Arrays.sort(arrobject);
            }
            for (Object object : arrobject) {
                TBIO.mv0(new TFile(file, (String)object, tArchiveDetector), new TFile(file2, (String)object, tArchiveDetector), tArchiveDetector);
            }
            if (!bl && !file2.setLastModified(l2)) {
                throw new IOException(file2 + " (cannot set last modification time)");
            }
        } else if (file.isFile()) {
            if (file2.exists() && !file2.isFile()) {
                throw new IOException(file2 + " (not a file)");
            }
            TBIO.cp0(true, file, file2);
        } else {
            if (file.exists()) {
                throw new IOException(file + " (cannot move special file)");
            }
            throw new IOException(file + " (missing file)");
        }
        if (!file.delete()) {
            throw new IOException(file + " (cannot delete)");
        }
    }

    private static void cp0(boolean bl, File file, File file2) throws IOException {
        TConfig tConfig = TConfig.get();
        InputSocket inputSocket = TBIO.getInputSocket(file, tConfig.getInputPreferences());
        OutputSocket outputSocket = TBIO.getOutputSocket(file2, tConfig.getOutputPreferences(), bl ? (Entry)inputSocket.getLocalTarget() : null);
        IOSocket.copy(inputSocket, outputSocket);
    }

    private static void checkContains(File file, File file2) throws IOException {
        if (Paths.contains(file.getAbsolutePath(), file2.getAbsolutePath(), File.separatorChar)) {
            throw new IOException(file2 + " (contained in " + file + ")");
        }
    }

    static InputSocket<?> getInputSocket(File file, BitField<FsInputOption> bitField) {
        TFile tFile;
        TFile tFile2;
        if (file instanceof TFile && null != (tFile = (tFile2 = (TFile)file).getInnerArchive())) {
            return tFile.getController().getInputSocket(tFile2.getInnerFsEntryName(), bitField);
        }
        tFile2 = new FsPath(file);
        return TConfig.get().getFsManager().getController(tFile2.getMountPoint(), TBIO.getDetector(file)).getInputSocket(tFile2.getEntryName(), bitField);
    }

    static OutputSocket<?> getOutputSocket(File file, BitField<FsOutputOption> bitField, Entry entry) {
        TFile tFile;
        TFile tFile2;
        if (file instanceof TFile && null != (tFile = (tFile2 = (TFile)file).getInnerArchive())) {
            return tFile.getController().getOutputSocket(tFile2.getInnerFsEntryName(), bitField, entry);
        }
        tFile2 = new FsPath(file);
        return TConfig.get().getFsManager().getController(tFile2.getMountPoint(), TBIO.getDetector(file)).getOutputSocket(tFile2.getEntryName(), bitField.clear(FsOutputOption.CREATE_PARENTS), entry);
    }

    private static TArchiveDetector getDetector(File file) {
        return file instanceof TFile ? ((TFile)file).getArchiveDetector() : TConfig.get().getArchiveDetector();
    }
}

