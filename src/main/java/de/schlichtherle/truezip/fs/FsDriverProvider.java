/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsDriver;
import de.schlichtherle.truezip.fs.FsScheme;
import java.util.Map;

public interface FsDriverProvider {
    public Map<FsScheme, FsDriver> get();
}

