/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.model;

public class UnzipParameters {
    private boolean ignoreReadOnlyFileAttribute;
    private boolean ignoreHiddenFileAttribute;
    private boolean ignoreArchiveFileAttribute;
    private boolean ignoreSystemFileAttribute;
    private boolean ignoreAllFileAttributes;
    private boolean ignoreDateTimeAttributes;

    public boolean isIgnoreReadOnlyFileAttribute() {
        return this.ignoreReadOnlyFileAttribute;
    }

    public boolean isIgnoreHiddenFileAttribute() {
        return this.ignoreHiddenFileAttribute;
    }

    public boolean isIgnoreArchiveFileAttribute() {
        return this.ignoreArchiveFileAttribute;
    }

    public boolean isIgnoreSystemFileAttribute() {
        return this.ignoreSystemFileAttribute;
    }

    public boolean isIgnoreAllFileAttributes() {
        return this.ignoreAllFileAttributes;
    }

    public boolean isIgnoreDateTimeAttributes() {
        return this.ignoreDateTimeAttributes;
    }
}

