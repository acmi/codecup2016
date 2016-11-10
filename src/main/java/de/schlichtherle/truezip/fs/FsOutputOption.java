/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

public final class FsOutputOption
extends Enum<FsOutputOption> {
    public static final /* enum */ FsOutputOption CACHE = new FsOutputOption();
    public static final /* enum */ FsOutputOption CREATE_PARENTS = new FsOutputOption();
    public static final /* enum */ FsOutputOption APPEND = new FsOutputOption();
    public static final /* enum */ FsOutputOption EXCLUSIVE = new FsOutputOption();
    public static final /* enum */ FsOutputOption STORE = new FsOutputOption();
    public static final /* enum */ FsOutputOption COMPRESS = new FsOutputOption();
    public static final /* enum */ FsOutputOption GROW = new FsOutputOption();
    public static final /* enum */ FsOutputOption ENCRYPT = new FsOutputOption();
    private static final /* synthetic */ FsOutputOption[] $VALUES;

    public static FsOutputOption[] values() {
        return (FsOutputOption[])$VALUES.clone();
    }

    private FsOutputOption() {
        super(string, n2);
    }

    static {
        $VALUES = new FsOutputOption[]{CACHE, CREATE_PARENTS, APPEND, EXCLUSIVE, STORE, COMPRESS, GROW, ENCRYPT};
    }
}

