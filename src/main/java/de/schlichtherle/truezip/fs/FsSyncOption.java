/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

public final class FsSyncOption
extends Enum<FsSyncOption> {
    public static final /* enum */ FsSyncOption WAIT_CLOSE_INPUT = new FsSyncOption();
    public static final /* enum */ FsSyncOption FORCE_CLOSE_INPUT = new FsSyncOption();
    public static final /* enum */ FsSyncOption WAIT_CLOSE_OUTPUT = new FsSyncOption();
    public static final /* enum */ FsSyncOption FORCE_CLOSE_OUTPUT = new FsSyncOption();
    public static final /* enum */ FsSyncOption ABORT_CHANGES = new FsSyncOption();
    public static final /* enum */ FsSyncOption CLEAR_CACHE = new FsSyncOption();
    private static final /* synthetic */ FsSyncOption[] $VALUES;

    public static FsSyncOption[] values() {
        return (FsSyncOption[])$VALUES.clone();
    }

    private FsSyncOption() {
        super(string, n2);
    }

    static {
        $VALUES = new FsSyncOption[]{WAIT_CLOSE_INPUT, FORCE_CLOSE_INPUT, WAIT_CLOSE_OUTPUT, FORCE_CLOSE_OUTPUT, ABORT_CHANGES, CLEAR_CACHE};
    }
}

