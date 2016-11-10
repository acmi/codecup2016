/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import javax.xml.transform.SourceLocator;

public class NodeLocator
implements SourceLocator {
    protected String m_publicId;
    protected String m_systemId;
    protected int m_lineNumber;
    protected int m_columnNumber;

    public NodeLocator(String string, String string2, int n2, int n3) {
        this.m_publicId = string;
        this.m_systemId = string2;
        this.m_lineNumber = n2;
        this.m_columnNumber = n3;
    }

    public String getPublicId() {
        return this.m_publicId;
    }

    public String getSystemId() {
        return this.m_systemId;
    }

    public int getLineNumber() {
        return this.m_lineNumber;
    }

    public int getColumnNumber() {
        return this.m_columnNumber;
    }

    public String toString() {
        return "file '" + this.m_systemId + "', line #" + this.m_lineNumber + ", column #" + this.m_columnNumber;
    }
}

