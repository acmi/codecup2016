/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsCompositeDriver;
import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsMountPoint;
import de.schlichtherle.truezip.fs.FsSyncException;
import de.schlichtherle.truezip.fs.FsSyncExceptionBuilder;
import de.schlichtherle.truezip.fs.FsSyncOption;
import de.schlichtherle.truezip.fs.FsSyncWarningException;
import de.schlichtherle.truezip.util.BitField;
import java.util.Iterator;

public abstract class FsManager
implements Iterable<FsController<?>> {
    public abstract FsController<?> getController(FsMountPoint var1, FsCompositeDriver var2);

    public abstract int getSize();

    @Override
    public abstract Iterator<FsController<?>> iterator();

    public void sync(BitField<FsSyncOption> bitField) throws FsSyncWarningException, FsSyncException {
        if (bitField.get(FsSyncOption.ABORT_CHANGES)) {
            throw new IllegalArgumentException();
        }
        FsSyncExceptionBuilder fsSyncExceptionBuilder = new FsSyncExceptionBuilder();
        for (FsController<?> fsController : this) {
            try {
                fsController.sync(bitField);
            }
            catch (FsSyncException fsSyncException) {
                fsSyncExceptionBuilder.warn(fsSyncException);
            }
        }
        fsSyncExceptionBuilder.check();
    }

    public final boolean equals(Object object) {
        return this == object;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return String.format("%s[size=%d]", this.getClass().getName(), this.getSize());
    }
}

