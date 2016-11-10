/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.impl.xs.opti.DefaultNode;

public class NodeImpl
extends DefaultNode {
    String prefix;
    String localpart;
    String rawname;
    String uri;
    short nodeType;
    boolean hidden;

    public NodeImpl() {
    }

    public NodeImpl(String string, String string2, String string3, String string4, short s2) {
        this.prefix = string;
        this.localpart = string2;
        this.rawname = string3;
        this.uri = string4;
        this.nodeType = s2;
    }

    public String getNodeName() {
        return this.rawname;
    }

    public String getNamespaceURI() {
        return this.uri;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getLocalName() {
        return this.localpart;
    }

    public short getNodeType() {
        return this.nodeType;
    }

    public void setReadOnly(boolean bl, boolean bl2) {
        this.hidden = bl;
    }

    public boolean getReadOnly() {
        return this.hidden;
    }

    public String toString() {
        return "[" + this.getNodeName() + ": " + this.getNodeValue() + "]";
    }
}

