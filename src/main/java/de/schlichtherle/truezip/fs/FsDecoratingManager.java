/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsCompositeDriver;
import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsMountPoint;
import java.util.Iterator;

public abstract class FsDecoratingManager<M extends FsManager>
extends FsManager {
    protected final M delegate;

    protected FsDecoratingManager(M m2) {
        if (null == m2) {
            throw new NullPointerException();
        }
        this.delegate = m2;
    }

    @Override
    public FsController<?> getController(FsMountPoint fsMountPoint, FsCompositeDriver fsCompositeDriver) {
        return this.delegate.getController(fsMountPoint, fsCompositeDriver);
    }

    @Override
    public int getSize() {
        return this.delegate.getSize();
    }

    @Override
    public Iterator<FsController<?>> iterator() {
        return this.delegate.iterator();
    }

    @Override
    public String toString() {
        return String.format("%s[delegate=%s]", this.getClass().getName(), this.delegate);
    }
}

