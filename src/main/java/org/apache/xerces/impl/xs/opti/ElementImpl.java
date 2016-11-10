/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.impl.xs.opti.DefaultElement;
import org.apache.xerces.impl.xs.opti.NamedNodeMapImpl;
import org.apache.xerces.impl.xs.opti.NodeImpl;
import org.apache.xerces.impl.xs.opti.SchemaDOM;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ElementImpl
extends DefaultElement {
    SchemaDOM schemaDOM;
    Attr[] attrs;
    int row = -1;
    int col = -1;
    int parentRow = -1;
    int line;
    int column;
    int charOffset;
    String fAnnotation;
    String fSyntheticAnnotation;

    public ElementImpl(int n2, int n3, int n4) {
        this.nodeType = 1;
        this.line = n2;
        this.column = n3;
        this.charOffset = n4;
    }

    public ElementImpl(int n2, int n3) {
        this(n2, n3, -1);
    }

    public ElementImpl(String string, String string2, String string3, String string4, int n2, int n3, int n4) {
        super(string, string2, string3, string4, 1);
        this.line = n2;
        this.column = n3;
        this.charOffset = n4;
    }

    public ElementImpl(String string, String string2, String string3, String string4, int n2, int n3) {
        this(string, string2, string3, string4, n2, n3, -1);
    }

    public Document getOwnerDocument() {
        return this.schemaDOM;
    }

    public Node getParentNode() {
        return this.schemaDOM.relations[this.row][0];
    }

    public boolean hasChildNodes() {
        if (this.parentRow == -1) {
            return false;
        }
        return true;
    }

    public Node getFirstChild() {
        if (this.parentRow == -1) {
            return null;
        }
        return this.schemaDOM.relations[this.parentRow][1];
    }

    public Node getLastChild() {
        if (this.parentRow == -1) {
            return null;
        }
        int n2 = 1;
        while (n2 < this.schemaDOM.relations[this.parentRow].length) {
            if (this.schemaDOM.relations[this.parentRow][n2] == null) {
                return this.schemaDOM.relations[this.parentRow][n2 - 1];
            }
            ++n2;
        }
        if (n2 == 1) {
            ++n2;
        }
        return this.schemaDOM.relations[this.parentRow][n2 - 1];
    }

    public Node getPreviousSibling() {
        if (this.col == 1) {
            return null;
        }
        return this.schemaDOM.relations[this.row][this.col - 1];
    }

    public Node getNextSibling() {
        if (this.col == this.schemaDOM.relations[this.row].length - 1) {
            return null;
        }
        return this.schemaDOM.relations[this.row][this.col + 1];
    }

    public NamedNodeMap getAttributes() {
        return new NamedNodeMapImpl(this.attrs);
    }

    public boolean hasAttributes() {
        return this.attrs.length != 0;
    }

    public String getTagName() {
        return this.rawname;
    }

    public String getAttribute(String string) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getName().equals(string)) {
                return this.attrs[n2].getValue();
            }
            ++n2;
        }
        return "";
    }

    public Attr getAttributeNode(String string) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getName().equals(string)) {
                return this.attrs[n2];
            }
            ++n2;
        }
        return null;
    }

    public String getAttributeNS(String string, String string2) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getLocalName().equals(string2) && ElementImpl.nsEquals(this.attrs[n2].getNamespaceURI(), string)) {
                return this.attrs[n2].getValue();
            }
            ++n2;
        }
        return "";
    }

    public Attr getAttributeNodeNS(String string, String string2) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getName().equals(string2) && ElementImpl.nsEquals(this.attrs[n2].getNamespaceURI(), string)) {
                return this.attrs[n2];
            }
            ++n2;
        }
        return null;
    }

    public boolean hasAttribute(String string) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getName().equals(string)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public boolean hasAttributeNS(String string, String string2) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getName().equals(string2) && ElementImpl.nsEquals(this.attrs[n2].getNamespaceURI(), string)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public void setAttribute(String string, String string2) {
        int n2 = 0;
        while (n2 < this.attrs.length) {
            if (this.attrs[n2].getName().equals(string)) {
                this.attrs[n2].setValue(string2);
                return;
            }
            ++n2;
        }
    }

    public int getLineNumber() {
        return this.line;
    }

    public int getColumnNumber() {
        return this.column;
    }

    public int getCharacterOffset() {
        return this.charOffset;
    }

    public String getAnnotation() {
        return this.fAnnotation;
    }

    public String getSyntheticAnnotation() {
        return this.fSyntheticAnnotation;
    }

    private static boolean nsEquals(String string, String string2) {
        if (string == null) {
            return string2 == null;
        }
        return string.equals(string2);
    }
}

