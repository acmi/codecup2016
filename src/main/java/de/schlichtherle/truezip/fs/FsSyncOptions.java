/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsSyncOption;
import de.schlichtherle.truezip.util.BitField;

public final class FsSyncOptions {
    public static final BitField<FsSyncOption> NONE = BitField.noneOf(FsSyncOption.class);
    public static final BitField<FsSyncOption> UMOUNT = BitField.of((Enum)FsSyncOption.FORCE_CLOSE_INPUT, (Enum[])new FsSyncOption[]{FsSyncOption.FORCE_CLOSE_OUTPUT, FsSyncOption.CLEAR_CACHE});
    public static final BitField<FsSyncOption> SYNC = BitField.of((Enum)FsSyncOption.WAIT_CLOSE_INPUT, (Enum[])new FsSyncOption[]{FsSyncOption.WAIT_CLOSE_OUTPUT});
    public static final BitField<FsSyncOption> RESET = BitField.of(FsSyncOption.ABORT_CHANGES);
}

