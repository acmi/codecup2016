/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.w3c.dom.DOMLocator;
import org.w3c.dom.Node;

public class DOMLocatorImpl
implements DOMLocator {
    public int fColumnNumber = -1;
    public int fLineNumber = -1;
    public Node fRelatedNode = null;
    public String fUri = null;
    public int fByteOffset = -1;
    public int fUtf16Offset = -1;

    public DOMLocatorImpl() {
    }

    public DOMLocatorImpl(int n2, int n3, String string) {
        this.fLineNumber = n2;
        this.fColumnNumber = n3;
        this.fUri = string;
    }

    public DOMLocatorImpl(int n2, int n3, int n4, String string) {
        this.fLineNumber = n2;
        this.fColumnNumber = n3;
        this.fUri = string;
        this.fUtf16Offset = n4;
    }

    public DOMLocatorImpl(int n2, int n3, int n4, Node node, String string) {
        this.fLineNumber = n2;
        this.fColumnNumber = n3;
        this.fByteOffset = n4;
        this.fRelatedNode = node;
        this.fUri = string;
    }

    public DOMLocatorImpl(int n2, int n3, int n4, Node node, String string, int n5) {
        this.fLineNumber = n2;
        this.fColumnNumber = n3;
        this.fByteOffset = n4;
        this.fRelatedNode = node;
        this.fUri = string;
        this.fUtf16Offset = n5;
    }

    public int getLineNumber() {
        return this.fLineNumber;
    }

    public int getColumnNumber() {
        return this.fColumnNumber;
    }

    public String getUri() {
        return this.fUri;
    }

    public Node getRelatedNode() {
        return this.fRelatedNode;
    }

    public int getByteOffset() {
        return this.fByteOffset;
    }

    public int getUtf16Offset() {
        return this.fUtf16Offset;
    }
}

