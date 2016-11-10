/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

class OneAttr {
    public String name;
    public int dvIndex;
    public int valueIndex;
    public Object dfltValue;

    public OneAttr(String string, int n2, int n3, Object object) {
        this.name = string;
        this.dvIndex = n2;
        this.valueIndex = n3;
        this.dfltValue = object;
    }
}

