/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.util.ControlFlowException;

final class FsNeedsWriteLockException
extends ControlFlowException {
    private static final FsNeedsWriteLockException INSTANCE = new FsNeedsWriteLockException();

    private FsNeedsWriteLockException() {
    }

    static FsNeedsWriteLockException get() {
        return FsNeedsWriteLockException.isTraceable() ? new FsNeedsWriteLockException() : INSTANCE;
    }
}

