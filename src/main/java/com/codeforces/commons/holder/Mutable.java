/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.holder;

import com.codeforces.commons.holder.Readable;
import com.codeforces.commons.holder.Writable;

public abstract class Mutable<T>
implements Readable<T>,
Writable<T> {
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Mutable)) {
            return false;
        }
        Object t2 = this.get();
        return t2 == null ? ((Readable)object).get() == null : t2.equals(((Readable)object).get());
    }

    public int hashCode() {
        Object t2 = this.get();
        return t2 == null ? 0 : t2.hashCode();
    }

    public String toString() {
        return String.valueOf(this.get());
    }
}

