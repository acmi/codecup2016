/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import java.util.AbstractList;
import org.apache.xerces.xs.StringList;

final class PSVIErrorList
extends AbstractList
implements StringList {
    private final String[] fArray;
    private final int fLength;
    private final int fOffset;

    public PSVIErrorList(String[] arrstring, boolean bl) {
        this.fArray = arrstring;
        this.fLength = this.fArray.length >> 1;
        this.fOffset = bl ? 0 : 1;
    }

    public boolean contains(String string) {
        if (string == null) {
            int n2 = 0;
            while (n2 < this.fLength) {
                if (this.fArray[(n2 << 1) + this.fOffset] == null) {
                    return true;
                }
                ++n2;
            }
        } else {
            int n3 = 0;
            while (n3 < this.fLength) {
                if (string.equals(this.fArray[(n3 << 1) + this.fOffset])) {
                    return true;
                }
                ++n3;
            }
        }
        return false;
    }

    public int getLength() {
        return this.fLength;
    }

    public String item(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fArray[(n2 << 1) + this.fOffset];
    }

    public Object get(int n2) {
        if (n2 >= 0 && n2 < this.fLength) {
            return this.fArray[(n2 << 1) + this.fOffset];
        }
        throw new IndexOutOfBoundsException("Index: " + n2);
    }

    public int size() {
        return this.getLength();
    }
}

