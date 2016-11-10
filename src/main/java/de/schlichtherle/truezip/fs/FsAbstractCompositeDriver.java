/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsCompositeDriver;
import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsDriver;
import de.schlichtherle.truezip.fs.FsDriverProvider;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsModel;
import de.schlichtherle.truezip.fs.FsMountPoint;
import de.schlichtherle.truezip.fs.FsScheme;
import java.util.Map;
import java.util.ServiceConfigurationError;

public abstract class FsAbstractCompositeDriver
implements FsCompositeDriver,
FsDriverProvider {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public final FsController<? extends FsModel> newController(FsManager fsManager, FsModel fsModel, FsController<? extends FsModel> fsController) {
        if (!$assertionsDisabled) {
            if (null == fsModel.getParent()) {
                if (null != fsController) throw new AssertionError();
            } else if (!fsModel.getParent().equals(fsController.getModel())) {
                throw new AssertionError();
            }
        }
        FsScheme fsScheme = fsModel.getMountPoint().getScheme();
        FsDriver fsDriver = this.get().get(fsScheme);
        if (null != fsDriver) return fsDriver.newController(fsManager, fsModel, fsController);
        throw new ServiceConfigurationError(fsScheme + " (Unknown file system scheme! May be the class path doesn't contain the respective driver module or it isn't set up correctly?)");
    }
}

