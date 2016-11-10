/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax.helpers;

import org.xml.sax.Locator;

public class LocatorImpl
implements Locator {
    private String publicId;
    private String systemId;
    private int lineNumber;
    private int columnNumber;

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

    public void setPublicId(String string) {
        this.publicId = string;
    }

    public void setSystemId(String string) {
        this.systemId = string;
    }

    public void setLineNumber(int n2) {
        this.lineNumber = n2;
    }

    public void setColumnNumber(int n2) {
        this.columnNumber = n2;
    }
}

