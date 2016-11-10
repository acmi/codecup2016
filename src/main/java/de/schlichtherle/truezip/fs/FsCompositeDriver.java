/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsModel;

public interface FsCompositeDriver {
    public FsController<? extends FsModel> newController(FsManager var1, FsModel var2, FsController<? extends FsModel> var3);
}

