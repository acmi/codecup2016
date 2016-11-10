/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.w3c.dom.Element;

final class XSAnnotationInfo {
    String fAnnotation;
    int fLine;
    int fColumn;
    int fCharOffset;
    XSAnnotationInfo next;

    XSAnnotationInfo(String string, int n2, int n3, int n4) {
        this.fAnnotation = string;
        this.fLine = n2;
        this.fColumn = n3;
        this.fCharOffset = n4;
    }

    XSAnnotationInfo(String string, Element element) {
        this.fAnnotation = string;
        if (element instanceof ElementImpl) {
            ElementImpl elementImpl = (ElementImpl)element;
            this.fLine = elementImpl.getLineNumber();
            this.fColumn = elementImpl.getColumnNumber();
            this.fCharOffset = elementImpl.getCharacterOffset();
        } else {
            this.fLine = -1;
            this.fColumn = -1;
            this.fCharOffset = -1;
        }
    }
}

