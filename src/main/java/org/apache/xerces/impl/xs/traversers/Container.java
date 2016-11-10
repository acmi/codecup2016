/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.traversers.LargeContainer;
import org.apache.xerces.impl.xs.traversers.OneAttr;
import org.apache.xerces.impl.xs.traversers.SmallContainer;

abstract class Container {
    static final int THRESHOLD = 5;
    OneAttr[] values;
    int pos = 0;

    Container() {
    }

    static Container getContainer(int n2) {
        if (n2 > 5) {
            return new LargeContainer(n2);
        }
        return new SmallContainer(n2);
    }

    abstract void put(String var1, OneAttr var2);

    abstract OneAttr get(String var1);
}

