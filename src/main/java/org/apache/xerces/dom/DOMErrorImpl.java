/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.DOMLocatorImpl;
import org.apache.xerces.xni.parser.XMLParseException;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMLocator;

public class DOMErrorImpl
implements DOMError {
    public short fSeverity = 1;
    public String fMessage = null;
    public DOMLocatorImpl fLocator = new DOMLocatorImpl();
    public Exception fException = null;
    public String fType;
    public Object fRelatedData;

    public DOMErrorImpl() {
    }

    public DOMErrorImpl(short s2, XMLParseException xMLParseException) {
        this.fSeverity = s2;
        this.fException = xMLParseException;
        this.fLocator = this.createDOMLocator(xMLParseException);
    }

    public short getSeverity() {
        return this.fSeverity;
    }

    public String getMessage() {
        return this.fMessage;
    }

    public DOMLocator getLocation() {
        return this.fLocator;
    }

    private DOMLocatorImpl createDOMLocator(XMLParseException xMLParseException) {
        return new DOMLocatorImpl(xMLParseException.getLineNumber(), xMLParseException.getColumnNumber(), xMLParseException.getCharacterOffset(), xMLParseException.getExpandedSystemId());
    }

    public Object getRelatedException() {
        return this.fException;
    }

    public void reset() {
        this.fSeverity = 1;
        this.fException = null;
    }

    public String getType() {
        return this.fType;
    }

    public Object getRelatedData() {
        return this.fRelatedData;
    }
}

