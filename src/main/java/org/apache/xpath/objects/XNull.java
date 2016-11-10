/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class XNull
extends XNodeSet {
    public int getType() {
        return -1;
    }

    public String getTypeString() {
        return "#CLASS_NULL";
    }

    public double num() {
        return 0.0;
    }

    public boolean bool() {
        return false;
    }

    public String str() {
        return "";
    }

    public int rtf(XPathContext xPathContext) {
        return -1;
    }

    public boolean equals(XObject xObject) {
        return xObject.getType() == -1;
    }
}

