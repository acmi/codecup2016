/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs.spi;

import de.schlichtherle.truezip.fs.FsManager;

public abstract class FsManagerDecorator {
    public abstract FsManager decorate(FsManager var1);

    public int getPriority() {
        return 0;
    }

    public String toString() {
        return String.format("%s[priority=%d]", this.getClass().getName(), this.getPriority());
    }
}

