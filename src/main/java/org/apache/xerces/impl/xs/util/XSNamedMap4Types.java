/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSTypeDefinition;

public final class XSNamedMap4Types
extends XSNamedMapImpl {
    private final short fType;

    public XSNamedMap4Types(String string, SymbolHash symbolHash, short s2) {
        super(string, symbolHash);
        this.fType = s2;
    }

    public XSNamedMap4Types(String[] arrstring, SymbolHash[] arrsymbolHash, int n2, short s2) {
        super(arrstring, arrsymbolHash, n2);
        this.fType = s2;
    }

    public synchronized int getLength() {
        if (this.fLength == -1) {
            int n2 = 0;
            int n3 = 0;
            while (n3 < this.fNSNum) {
                n2 += this.fMaps[n3].getLength();
                ++n3;
            }
            int n4 = 0;
            Object[] arrobject = new XSObject[n2];
            int n5 = 0;
            while (n5 < this.fNSNum) {
                n4 += this.fMaps[n5].getValues(arrobject, n4);
                ++n5;
            }
            this.fLength = 0;
            this.fArray = new XSObject[n2];
            int n6 = 0;
            while (n6 < n2) {
                XSTypeDefinition xSTypeDefinition = (XSTypeDefinition)arrobject[n6];
                if (xSTypeDefinition.getTypeCategory() == this.fType) {
                    this.fArray[this.fLength++] = xSTypeDefinition;
                }
                ++n6;
            }
        }
        return this.fLength;
    }

    public XSObject itemByName(String string, String string2) {
        int n2 = 0;
        while (n2 < this.fNSNum) {
            if (XSNamedMapImpl.isEqual(string, this.fNamespaces[n2])) {
                XSTypeDefinition xSTypeDefinition = (XSTypeDefinition)this.fMaps[n2].get(string2);
                if (xSTypeDefinition != null && xSTypeDefinition.getTypeCategory() == this.fType) {
                    return xSTypeDefinition;
                }
                return null;
            }
            ++n2;
        }
        return null;
    }

    public synchronized XSObject item(int n2) {
        if (this.fArray == null) {
            this.getLength();
        }
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fArray[n2];
    }
}

