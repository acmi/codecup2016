/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs.spi;

import de.schlichtherle.truezip.fs.FsManagerProvider;

public abstract class FsManagerService
implements FsManagerProvider {
    public int getPriority() {
        return 0;
    }

    public String toString() {
        return String.format("%s[priority=%d]", this.getClass().getName(), this.getPriority());
    }
}

