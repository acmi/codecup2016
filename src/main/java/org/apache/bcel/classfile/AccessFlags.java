/*
 * Decompiled with CFR 0_119.
 */
package org.apache.bcel.classfile;

public abstract class AccessFlags {
    protected int access_flags;

    public AccessFlags() {
    }

    public AccessFlags(int n2) {
        this.access_flags = n2;
    }

    public final int getAccessFlags() {
        return this.access_flags;
    }

    public final void setAccessFlags(int n2) {
        this.access_flags = n2;
    }

    private final void setFlag(int n2, boolean bl) {
        if ((this.access_flags & n2) != 0) {
            if (!bl) {
                this.access_flags ^= n2;
            }
        } else if (bl) {
            this.access_flags |= n2;
        }
    }

    public final void isPublic(boolean bl) {
        this.setFlag(1, bl);
    }

    public final boolean isPublic() {
        return (this.access_flags & 1) != 0;
    }

    public final void isPrivate(boolean bl) {
        this.setFlag(2, bl);
    }

    public final boolean isPrivate() {
        return (this.access_flags & 2) != 0;
    }

    public final void isProtected(boolean bl) {
        this.setFlag(4, bl);
    }

    public final boolean isProtected() {
        return (this.access_flags & 4) != 0;
    }

    public final void isStatic(boolean bl) {
        this.setFlag(8, bl);
    }

    public final boolean isStatic() {
        return (this.access_flags & 8) != 0;
    }

    public final void isFinal(boolean bl) {
        this.setFlag(16, bl);
    }

    public final boolean isFinal() {
        return (this.access_flags & 16) != 0;
    }

    public final void isSynchronized(boolean bl) {
        this.setFlag(32, bl);
    }

    public final boolean isSynchronized() {
        return (this.access_flags & 32) != 0;
    }

    public final void isVolatile(boolean bl) {
        this.setFlag(64, bl);
    }

    public final boolean isVolatile() {
        return (this.access_flags & 64) != 0;
    }

    public final void isTransient(boolean bl) {
        this.setFlag(128, bl);
    }

    public final boolean isTransient() {
        return (this.access_flags & 128) != 0;
    }

    public final void isNative(boolean bl) {
        this.setFlag(256, bl);
    }

    public final boolean isNative() {
        return (this.access_flags & 256) != 0;
    }

    public final void isInterface(boolean bl) {
        this.setFlag(512, bl);
    }

    public final boolean isInterface() {
        return (this.access_flags & 512) != 0;
    }

    public final void isAbstract(boolean bl) {
        this.setFlag(1024, bl);
    }

    public final boolean isAbstract() {
        return (this.access_flags & 1024) != 0;
    }

    public final void isStrictfp(boolean bl) {
        this.setFlag(2048, bl);
    }

    public final boolean isStrictfp() {
        return (this.access_flags & 2048) != 0;
    }
}

