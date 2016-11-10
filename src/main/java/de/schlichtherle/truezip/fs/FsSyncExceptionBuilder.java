/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsSyncException;
import de.schlichtherle.truezip.io.SequentialIOExceptionBuilder;
import java.io.IOException;

public final class FsSyncExceptionBuilder
extends SequentialIOExceptionBuilder<IOException, FsSyncException> {
    public FsSyncExceptionBuilder() {
        super(IOException.class, FsSyncException.class);
    }
}

