/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsOutputOption;
import de.schlichtherle.truezip.util.BitField;

public final class FsOutputOptions {
    public static final BitField<FsOutputOption> NONE = BitField.noneOf(FsOutputOption.class);
    @Deprecated
    public static final BitField<FsOutputOption> NO_OUTPUT_OPTIONS = NONE;
    @Deprecated
    public static final BitField<FsOutputOption> NO_OUTPUT_OPTION = NONE;
    public static final BitField<FsOutputOption> OUTPUT_PREFERENCES_MASK = BitField.of((Enum)FsOutputOption.CACHE, (Enum[])new FsOutputOption[]{FsOutputOption.CREATE_PARENTS, FsOutputOption.STORE, FsOutputOption.COMPRESS, FsOutputOption.GROW, FsOutputOption.ENCRYPT});
}

