/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.traversers.Container;
import org.apache.xerces.impl.xs.traversers.OneAttr;

class SmallContainer
extends Container {
    String[] keys;

    SmallContainer(int n2) {
        this.keys = new String[n2];
        this.values = new OneAttr[n2];
    }

    void put(String string, OneAttr oneAttr) {
        this.keys[this.pos] = string;
        this.values[this.pos++] = oneAttr;
    }

    OneAttr get(String string) {
        int n2 = 0;
        while (n2 < this.pos) {
            if (this.keys[n2].equals(string)) {
                return this.values[n2];
            }
            ++n2;
        }
        return null;
    }
}

