/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;

public class CMAny
extends CMNode {
    private final int fType;
    private final String fURI;
    private int fPosition = -1;

    public CMAny(int n2, String string, int n3) {
        super(n2);
        this.fType = n2;
        this.fURI = string;
        this.fPosition = n3;
    }

    final int getType() {
        return this.fType;
    }

    final String getURI() {
        return this.fURI;
    }

    final int getPosition() {
        return this.fPosition;
    }

    final void setPosition(int n2) {
        this.fPosition = n2;
    }

    public boolean isNullable() {
        return this.fPosition == -1;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        stringBuffer.append("##any:uri=");
        stringBuffer.append(this.fURI);
        stringBuffer.append(')');
        if (this.fPosition >= 0) {
            stringBuffer.append(" (Pos:").append(Integer.toString(this.fPosition)).append(')');
        }
        return stringBuffer.toString();
    }

    protected void calcFirstPos(CMStateSet cMStateSet) {
        if (this.fPosition == -1) {
            cMStateSet.zeroBits();
        } else {
            cMStateSet.setBit(this.fPosition);
        }
    }

    protected void calcLastPos(CMStateSet cMStateSet) {
        if (this.fPosition == -1) {
            cMStateSet.zeroBits();
        } else {
            cMStateSet.setBit(this.fPosition);
        }
    }
}
