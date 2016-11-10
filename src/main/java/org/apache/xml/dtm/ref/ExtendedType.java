/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

public final class ExtendedType {
    private int nodetype;
    private String namespace;
    private String localName;
    private int hash;

    public ExtendedType(int n2, String string, String string2) {
        this.nodetype = n2;
        this.namespace = string;
        this.localName = string2;
        this.hash = n2 + string.hashCode() + string2.hashCode();
    }

    public ExtendedType(int n2, String string, String string2, int n3) {
        this.nodetype = n2;
        this.namespace = string;
        this.localName = string2;
        this.hash = n3;
    }

    protected void redefine(int n2, String string, String string2, int n3) {
        this.nodetype = n2;
        this.namespace = string;
        this.localName = string2;
        this.hash = n3;
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean equals(ExtendedType extendedType) {
        try {
            return extendedType.nodetype == this.nodetype && extendedType.localName.equals(this.localName) && extendedType.namespace.equals(this.namespace);
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
    }

    public int getNodeType() {
        return this.nodetype;
    }

    public String getLocalName() {
        return this.localName;
    }

    public String getNamespace() {
        return this.namespace;
    }
}

