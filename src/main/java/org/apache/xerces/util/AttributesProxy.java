/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLAttributes;
import org.xml.sax.AttributeList;
import org.xml.sax.ext.Attributes2;

public final class AttributesProxy
implements AttributeList,
Attributes2 {
    private XMLAttributes fAttributes;

    public AttributesProxy(XMLAttributes xMLAttributes) {
        this.fAttributes = xMLAttributes;
    }

    public void setAttributes(XMLAttributes xMLAttributes) {
        this.fAttributes = xMLAttributes;
    }

    public XMLAttributes getAttributes() {
        return this.fAttributes;
    }

    public int getLength() {
        return this.fAttributes.getLength();
    }

    public String getQName(int n2) {
        return this.fAttributes.getQName(n2);
    }

    public String getURI(int n2) {
        String string = this.fAttributes.getURI(n2);
        return string != null ? string : XMLSymbols.EMPTY_STRING;
    }

    public String getLocalName(int n2) {
        return this.fAttributes.getLocalName(n2);
    }

    public String getType(int n2) {
        return this.fAttributes.getType(n2);
    }

    public String getType(String string) {
        return this.fAttributes.getType(string);
    }

    public String getType(String string, String string2) {
        return string.equals(XMLSymbols.EMPTY_STRING) ? this.fAttributes.getType(null, string2) : this.fAttributes.getType(string, string2);
    }

    public String getValue(int n2) {
        return this.fAttributes.getValue(n2);
    }

    public String getValue(String string) {
        return this.fAttributes.getValue(string);
    }

    public String getValue(String string, String string2) {
        return string.equals(XMLSymbols.EMPTY_STRING) ? this.fAttributes.getValue(null, string2) : this.fAttributes.getValue(string, string2);
    }

    public int getIndex(String string) {
        return this.fAttributes.getIndex(string);
    }

    public int getIndex(String string, String string2) {
        return string.equals(XMLSymbols.EMPTY_STRING) ? this.fAttributes.getIndex(null, string2) : this.fAttributes.getIndex(string, string2);
    }

    public boolean isDeclared(int n2) {
        if (n2 < 0 || n2 >= this.fAttributes.getLength()) {
            throw new ArrayIndexOutOfBoundsException(n2);
        }
        return Boolean.TRUE.equals(this.fAttributes.getAugmentations(n2).getItem("ATTRIBUTE_DECLARED"));
    }

    public boolean isDeclared(String string) {
        int n2 = this.getIndex(string);
        if (n2 == -1) {
            throw new IllegalArgumentException(string);
        }
        return Boolean.TRUE.equals(this.fAttributes.getAugmentations(n2).getItem("ATTRIBUTE_DECLARED"));
    }

    public boolean isDeclared(String string, String string2) {
        int n2 = this.getIndex(string, string2);
        if (n2 == -1) {
            throw new IllegalArgumentException(string2);
        }
        return Boolean.TRUE.equals(this.fAttributes.getAugmentations(n2).getItem("ATTRIBUTE_DECLARED"));
    }

    public boolean isSpecified(int n2) {
        if (n2 < 0 || n2 >= this.fAttributes.getLength()) {
            throw new ArrayIndexOutOfBoundsException(n2);
        }
        return this.fAttributes.isSpecified(n2);
    }

    public boolean isSpecified(String string) {
        int n2 = this.getIndex(string);
        if (n2 == -1) {
            throw new IllegalArgumentException(string);
        }
        return this.fAttributes.isSpecified(n2);
    }

    public boolean isSpecified(String string, String string2) {
        int n2 = this.getIndex(string, string2);
        if (n2 == -1) {
            throw new IllegalArgumentException(string2);
        }
        return this.fAttributes.isSpecified(n2);
    }

    public String getName(int n2) {
        return this.fAttributes.getQName(n2);
    }
}

