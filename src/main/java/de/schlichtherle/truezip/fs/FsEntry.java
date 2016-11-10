/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.entry.Entry;
import de.schlichtherle.truezip.util.BitField;
import java.util.Formatter;
import java.util.Set;

public abstract class FsEntry
implements Entry {
    public abstract String getName();

    public abstract Set<Entry.Type> getTypes();

    public boolean isType(Entry.Type type) {
        return this.getTypes().contains((Object)((Object)type));
    }

    public abstract Set<String> getMembers();

    public final boolean equals(Object object) {
        return this == object;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        long l2;
        Set<Entry.Type> set = this.getTypes();
        BitField<Entry.Type> bitField = set.isEmpty() ? BitField.noneOf(Entry.Type.class) : BitField.copyOf(set);
        StringBuilder stringBuilder = new StringBuilder(256);
        Formatter formatter = new Formatter(stringBuilder).format("%s[name=%s, types=%s", this.getClass().getName(), this.getName(), bitField);
        for (Enum enum_ : ALL_SIZE_SET) {
            l2 = this.getSize((Entry.Size)enum_);
            if (-1 == l2) continue;
            formatter.format(", size(%s)=%d", enum_, l2);
        }
        for (Enum enum_ : ALL_ACCESS_SET) {
            l2 = this.getTime((Entry.Access)enum_);
            if (-1 == l2) continue;
            formatter.format(", time(%s)=%tc", enum_, l2);
        }
        return formatter.format(", members=%s]", this.getMembers()).toString();
    }
}

