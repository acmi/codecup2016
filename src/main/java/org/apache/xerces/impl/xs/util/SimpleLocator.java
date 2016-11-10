/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import org.apache.xerces.xni.XMLLocator;

public final class SimpleLocator
implements XMLLocator {
    private String lsid;
    private String esid;
    private int line;
    private int column;
    private int charOffset;

    public SimpleLocator() {
    }

    public SimpleLocator(String string, String string2, int n2, int n3) {
        this(string, string2, n2, n3, -1);
    }

    public void setValues(String string, String string2, int n2, int n3) {
        this.setValues(string, string2, n2, n3, -1);
    }

    public SimpleLocator(String string, String string2, int n2, int n3, int n4) {
        this.line = n2;
        this.column = n3;
        this.lsid = string;
        this.esid = string2;
        this.charOffset = n4;
    }

    public void setValues(String string, String string2, int n2, int n3, int n4) {
        this.line = n2;
        this.column = n3;
        this.lsid = string;
        this.esid = string2;
        this.charOffset = n4;
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

    public String getPublicId() {
        return null;
    }

    public String getExpandedSystemId() {
        return this.esid;
    }

    public String getLiteralSystemId() {
        return this.lsid;
    }

    public String getBaseSystemId() {
        return null;
    }

    public void setColumnNumber(int n2) {
        this.column = n2;
    }

    public void setLineNumber(int n2) {
        this.line = n2;
    }

    public void setCharacterOffset(int n2) {
        this.charOffset = n2;
    }

    public void setBaseSystemId(String string) {
    }

    public void setExpandedSystemId(String string) {
        this.esid = string;
    }

    public void setLiteralSystemId(String string) {
        this.lsid = string;
    }

    public void setPublicId(String string) {
    }

    public String getEncoding() {
        return null;
    }

    public String getXMLVersion() {
        return null;
    }
}

