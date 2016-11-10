/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;

public final class BitField<E extends Enum<E>>
implements Serializable,
Iterable<E> {
    private final EnumSet<E> bits;

    public static <E extends Enum<E>> BitField<E> noneOf(Class<E> class_) {
        return new BitField<E>(class_, false);
    }

    public static <E extends Enum<E>> BitField<E> of(E e2) {
        return new BitField<E>(e2);
    }

    public static /* varargs */ <E extends Enum<E>> BitField<E> of(E e2, E ... arrE) {
        return new BitField(e2, arrE);
    }

    public static <E extends Enum<E>> BitField<E> copyOf(Collection<E> collection) {
        return new BitField<E>(EnumSet.copyOf(collection));
    }

    private BitField(Class<E> class_, boolean bl) {
        this.bits = bl ? EnumSet.allOf(class_) : EnumSet.noneOf(class_);
    }

    private BitField(E e2) {
        this.bits = EnumSet.of(e2);
    }

    private /* varargs */ BitField(E e2, E ... arrE) {
        this.bits = EnumSet.of(e2, arrE);
    }

    private BitField(EnumSet<E> enumSet) {
        assert (null != enumSet);
        this.bits = enumSet;
    }

    public boolean get(E e2) {
        return this.bits.contains(e2);
    }

    public BitField<E> set(E e2, boolean bl) {
        Object object;
        if (bl) {
            if (this.bits.contains(e2)) {
                return this;
            }
            object = this.bits.clone();
            object.add(e2);
        } else {
            if (!this.bits.contains(e2)) {
                return this;
            }
            object = this.bits.clone();
            object.remove(e2);
        }
        return new BitField<E>((EnumSet<E>)object);
    }

    public BitField<E> set(E e2) {
        return this.set(e2, true);
    }

    public BitField<E> clear(E e2) {
        return this.set(e2, false);
    }

    public BitField<E> not() {
        return new BitField<E>(EnumSet.complementOf(this.bits));
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableSet(this.bits).iterator();
    }

    public String toString() {
        int n2 = this.bits.size() * 11;
        if (0 >= n2) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(n2);
        for (Enum enum_ : this.bits) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append('|');
            }
            stringBuilder.append(enum_);
        }
        return stringBuilder.toString();
    }

    public boolean equals(Object object) {
        return this == object || object instanceof BitField && this.bits.equals(((BitField)object).bits);
    }

    public int hashCode() {
        return this.bits.hashCode();
    }
}

