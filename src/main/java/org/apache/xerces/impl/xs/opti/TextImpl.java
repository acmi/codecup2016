/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.impl.xs.opti.DefaultText;
import org.apache.xerces.impl.xs.opti.NodeImpl;
import org.apache.xerces.impl.xs.opti.SchemaDOM;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class TextImpl
extends DefaultText {
    String fData = null;
    SchemaDOM fSchemaDOM = null;
    int fRow;
    int fCol;

    public TextImpl(StringBuffer stringBuffer, SchemaDOM schemaDOM, int n2, int n3) {
        this.fData = stringBuffer.toString();
        this.fSchemaDOM = schemaDOM;
        this.fRow = n2;
        this.fCol = n3;
        this.uri = null;
        this.localpart = null;
        this.prefix = null;
        this.rawname = null;
        this.nodeType = 3;
    }

    public String getNodeName() {
        return "#text";
    }

    public Node getParentNode() {
        return this.fSchemaDOM.relations[this.fRow][0];
    }

    public Node getPreviousSibling() {
        if (this.fCol == 1) {
            return null;
        }
        return this.fSchemaDOM.relations[this.fRow][this.fCol - 1];
    }

    public Node getNextSibling() {
        if (this.fCol == this.fSchemaDOM.relations[this.fRow].length - 1) {
            return null;
        }
        return this.fSchemaDOM.relations[this.fRow][this.fCol + 1];
    }

    public String getData() throws DOMException {
        return this.fData;
    }

    public int getLength() {
        if (this.fData == null) {
            return 0;
        }
        return this.fData.length();
    }

    public String substringData(int n2, int n3) throws DOMException {
        if (this.fData == null) {
            return null;
        }
        if (n3 < 0 || n2 < 0 || n2 > this.fData.length()) {
            throw new DOMException(1, "parameter error");
        }
        if (n2 + n3 >= this.fData.length()) {
            return this.fData.substring(n2);
        }
        return this.fData.substring(n2, n2 + n3);
    }
}

