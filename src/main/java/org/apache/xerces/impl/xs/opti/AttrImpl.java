/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.impl.xs.opti.NodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

public class AttrImpl
extends NodeImpl
implements Attr {
    Element element;
    String value;

    public AttrImpl() {
        this.nodeType = 2;
    }

    public AttrImpl(Element element, String string, String string2, String string3, String string4, String string5) {
        super(string, string2, string3, string4, 2);
        this.element = element;
        this.value = string5;
    }

    public String getName() {
        return this.rawname;
    }

    public boolean getSpecified() {
        return true;
    }

    public String getValue() {
        return this.value;
    }

    public String getNodeValue() {
        return this.getValue();
    }

    public Element getOwnerElement() {
        return this.element;
    }

    public void setValue(String string) throws DOMException {
        this.value = string;
    }

    public boolean isId() {
        return false;
    }

    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    public String toString() {
        return this.getName() + "=" + "\"" + this.getValue() + "\"";
    }
}

