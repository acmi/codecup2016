/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.file;

import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsFilteringManager;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsMountPoint;
import de.schlichtherle.truezip.fs.FsSyncException;
import de.schlichtherle.truezip.fs.FsSyncOption;
import de.schlichtherle.truezip.fs.FsSyncOptions;
import de.schlichtherle.truezip.fs.FsUriModifier;
import de.schlichtherle.truezip.util.BitField;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public final class TVFS {
    public static void umount(TFile tFile) throws FsSyncException {
        TVFS.sync(tFile, FsSyncOptions.UMOUNT);
    }

    static FsMountPoint mountPoint(TFile tFile) {
        if (tFile.isArchive()) {
            return tFile.getController().getModel().getMountPoint();
        }
        try {
            return new FsMountPoint(new URI(tFile.getFile().toURI() + "/"), FsUriModifier.CANONICALIZE);
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new AssertionError(uRISyntaxException);
        }
    }

    public static void sync(TFile tFile, BitField<FsSyncOption> bitField) throws FsSyncException {
        TVFS.sync(TVFS.mountPoint(tFile), bitField);
    }

    public static void sync(FsMountPoint fsMountPoint, BitField<FsSyncOption> bitField) throws FsSyncException {
        new FsFilteringManager(TConfig.get().getFsManager(), fsMountPoint).sync(bitField);
    }
}

