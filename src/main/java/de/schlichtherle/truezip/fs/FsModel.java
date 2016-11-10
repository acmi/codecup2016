/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsMountPoint;

public abstract class FsModel {
    private final FsMountPoint mountPoint;
    private final FsModel parent;

    protected FsModel(FsMountPoint fsMountPoint, FsModel fsModel) {
        if (!FsModel.equals(fsMountPoint.getParent(), null == fsModel ? null : fsModel.getMountPoint())) {
            throw new IllegalArgumentException("Parent/Member mismatch!");
        }
        this.mountPoint = fsMountPoint;
        this.parent = fsModel;
    }

    private static boolean equals(Object object, Object object2) {
        return object == object2 || null != object && object.equals(object2);
    }

    public final FsMountPoint getMountPoint() {
        return this.mountPoint;
    }

    public final FsModel getParent() {
        return this.parent;
    }

    public abstract boolean isMounted();

    public final boolean equals(Object object) {
        return this == object;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return String.format("%s[mountPoint=%s, parent=%s, mounted=%b]", this.getClass().getName(), this.getMountPoint(), this.getParent(), this.isMounted());
    }
}

