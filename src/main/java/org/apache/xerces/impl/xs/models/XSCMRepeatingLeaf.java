/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.xs.models.XSCMLeaf;

public final class XSCMRepeatingLeaf
extends XSCMLeaf {
    private final int fMinOccurs;
    private final int fMaxOccurs;

    public XSCMRepeatingLeaf(int n2, Object object, int n3, int n4, int n5, int n6) {
        super(n2, object, n5, n6);
        this.fMinOccurs = n3;
        this.fMaxOccurs = n4;
    }

    final int getMinOccurs() {
        return this.fMinOccurs;
    }

    final int getMaxOccurs() {
        return this.fMaxOccurs;
    }
}

