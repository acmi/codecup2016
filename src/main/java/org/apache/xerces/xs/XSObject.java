/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xs;

import org.apache.xerces.xs.XSNamespaceItem;

public interface XSObject {
    public short getType();

    public String getName();

    public String getNamespace();

    public XSNamespaceItem getNamespaceItem();
}

