/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsInputOption;
import de.schlichtherle.truezip.util.BitField;

public final class FsInputOptions {
    public static final BitField<FsInputOption> NONE = BitField.noneOf(FsInputOption.class);
    @Deprecated
    public static final BitField<FsInputOption> NO_INPUT_OPTIONS = NONE;
    @Deprecated
    public static final BitField<FsInputOption> NO_INPUT_OPTION = NONE;
    public static final BitField<FsInputOption> INPUT_PREFERENCES_MASK = BitField.of(FsInputOption.CACHE);
}

