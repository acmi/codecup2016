/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.io.SequentialIOException;
import java.io.IOException;

public class FsSyncException
extends SequentialIOException {
    @Override
    public IOException getCause() {
        return (IOException)super.getCause();
    }

    @Override
    public final FsSyncException initCause(Throwable throwable) {
        super.initCause((IOException)throwable);
        return this;
    }
}

