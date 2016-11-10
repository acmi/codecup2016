/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.impl.xs.opti.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

public class DefaultText
extends NodeImpl
implements Text {
    public String getData() throws DOMException {
        return null;
    }

    public void setData(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public int getLength() {
        return 0;
    }

    public String substringData(int n2, int n3) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void appendData(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void insertData(int n2, String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void deleteData(int n2, int n3) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public void replaceData(int n2, int n3, String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public Text splitText(int n2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    public boolean isElementContentWhitespace() {
        throw new DOMException(9, "Method not supported");
    }

    public String getWholeText() {
        throw new DOMException(9, "Method not supported");
    }

    public Text replaceWholeText(String string) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }
}

