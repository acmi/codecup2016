/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import org.apache.xerces.impl.xs.util.XInt;

public final class XIntPool {
    private static final short POOL_SIZE = 10;
    private static final XInt[] fXIntPool = new XInt[10];

    public final XInt getXInt(int n2) {
        if (n2 >= 0 && n2 < fXIntPool.length) {
            return fXIntPool[n2];
        }
        return new XInt(n2);
    }

    static {
        int n2 = 0;
        while (n2 < 10) {
            XIntPool.fXIntPool[n2] = new XInt(n2);
            ++n2;
        }
    }
}

