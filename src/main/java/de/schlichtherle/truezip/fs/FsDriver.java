/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsModel;

public abstract class FsDriver {
    public boolean isFederated() {
        return false;
    }

    public int getPriority() {
        return 0;
    }

    public FsController<? extends FsModel> newController(FsManager fsManager, FsModel fsModel, FsController<? extends FsModel> fsController) {
        return this.newController(fsModel, fsController);
    }

    public abstract FsController<?> newController(FsModel var1, FsController<?> var2);

    public String toString() {
        return String.format("%s[federated=%b, priority=%d]", this.getClass().getName(), this.isFederated(), this.getPriority());
    }
}

