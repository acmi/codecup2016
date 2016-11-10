/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs.spi;

import de.schlichtherle.truezip.fs.FsDriverProvider;

public abstract class FsDriverService
implements FsDriverProvider {
    public String toString() {
        return this.getClass().getName();
    }
}

