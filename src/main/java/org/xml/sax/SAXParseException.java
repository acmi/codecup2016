/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class SAXParseException
extends SAXException {
    private String publicId;
    private String systemId;
    private int lineNumber;
    private int columnNumber;

    public SAXParseException(String string, Locator locator) {
        super(string);
        if (locator != null) {
            this.init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            this.init(null, null, -1, -1);
        }
    }

    public SAXParseException(String string, Locator locator, Exception exception) {
        super(string, exception);
        if (locator != null) {
            this.init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            this.init(null, null, -1, -1);
        }
    }

    public SAXParseException(String string, String string2, String string3, int n2, int n3) {
        super(string);
        this.init(string2, string3, n2, n3);
    }

    public SAXParseException(String string, String string2, String string3, int n2, int n3, Exception exception) {
        super(string, exception);
        this.init(string2, string3, n2, n3);
    }

    private void init(String string, String string2, int n2, int n3) {
        this.publicId = string;
        this.systemId = string2;
        this.lineNumber = n2;
        this.columnNumber = n3;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }
}

