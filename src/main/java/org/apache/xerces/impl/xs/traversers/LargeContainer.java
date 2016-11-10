/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import java.util.Hashtable;
import org.apache.xerces.impl.xs.traversers.Container;
import org.apache.xerces.impl.xs.traversers.OneAttr;

class LargeContainer
extends Container {
    Hashtable items;

    LargeContainer(int n2) {
        this.items = new Hashtable(n2 * 2 + 1);
        this.values = new OneAttr[n2];
    }

    void put(String string, OneAttr oneAttr) {
        this.items.put(string, oneAttr);
        this.values[this.pos++] = oneAttr;
    }

    OneAttr get(String string) {
        OneAttr oneAttr = (OneAttr)this.items.get(string);
        return oneAttr;
    }
}

