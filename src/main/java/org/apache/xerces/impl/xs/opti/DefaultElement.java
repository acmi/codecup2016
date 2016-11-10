/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.impl.xs.opti.NodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

public class DefaultElement
extends NodeImpl
implements Element {
    public DefaultElement() {
    }

    public DefaultElement(String string, String string2, String string3, String string4, short s2) {
        super(string, string2, string3, string4, s2);
    }

    public String getTagName() {
        return null;
    }

    public String getAttribute(String string) {
        return null;
    }

    public Attr getAttributeNode(String string) {
        return null;
    }

    public NodeList getElementsByTagName(String string) {
        return null;
    }

    public String getAttributeNS(String string, String string2) {
        return null;
    }

    public Attr getAttributeNodeNS(String string, String string2) {
        return null;
    }

    public NodeList getElementsByTagNameNS(String string, String string2) {
        return null;
    }

    public boolean hasAttribute(String string) {
        return false;
    }

    public boolean hasAttributeNS(String string, String string2) {
        return false;
    }

    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    public void setAttribute(String string, String string2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void removeAttribute(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Attr removeAttributeNode(Attr attr) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Attr setAttributeNode(Attr attr) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void setAttributeNS(String string, String string2, String string3) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void removeAttributeNS(String string, String string2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Attr setAttributeNodeNS(Attr attr) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void setIdAttributeNode(Attr attr, boolean bl) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void setIdAttribute(String string, boolean bl) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void setIdAttributeNS(String string, String string2, boolean bl) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }
}

