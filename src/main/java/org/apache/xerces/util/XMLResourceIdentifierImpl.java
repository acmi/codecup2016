/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.xni.XMLResourceIdentifier;

public class XMLResourceIdentifierImpl
implements XMLResourceIdentifier {
    protected String fPublicId;
    protected String fLiteralSystemId;
    protected String fBaseSystemId;
    protected String fExpandedSystemId;
    protected String fNamespace;

    public XMLResourceIdentifierImpl() {
    }

    public XMLResourceIdentifierImpl(String string, String string2, String string3, String string4) {
        this.setValues(string, string2, string3, string4, null);
    }

    public XMLResourceIdentifierImpl(String string, String string2, String string3, String string4, String string5) {
        this.setValues(string, string2, string3, string4, string5);
    }

    public void setValues(String string, String string2, String string3, String string4) {
        this.setValues(string, string2, string3, string4, null);
    }

    public void setValues(String string, String string2, String string3, String string4, String string5) {
        this.fPublicId = string;
        this.fLiteralSystemId = string2;
        this.fBaseSystemId = string3;
        this.fExpandedSystemId = string4;
        this.fNamespace = string5;
    }

    public void clear() {
        this.fPublicId = null;
        this.fLiteralSystemId = null;
        this.fBaseSystemId = null;
        this.fExpandedSystemId = null;
        this.fNamespace = null;
    }

    public void setPublicId(String string) {
        this.fPublicId = string;
    }

    public void setLiteralSystemId(String string) {
        this.fLiteralSystemId = string;
    }

    public void setBaseSystemId(String string) {
        this.fBaseSystemId = string;
    }

    public void setExpandedSystemId(String string) {
        this.fExpandedSystemId = string;
    }

    public void setNamespace(String string) {
        this.fNamespace = string;
    }

    public String getPublicId() {
        return this.fPublicId;
    }

    public String getLiteralSystemId() {
        return this.fLiteralSystemId;
    }

    public String getBaseSystemId() {
        return this.fBaseSystemId;
    }

    public String getExpandedSystemId() {
        return this.fExpandedSystemId;
    }

    public String getNamespace() {
        return this.fNamespace;
    }

    public int hashCode() {
        int n2 = 0;
        if (this.fPublicId != null) {
            n2 += this.fPublicId.hashCode();
        }
        if (this.fLiteralSystemId != null) {
            n2 += this.fLiteralSystemId.hashCode();
        }
        if (this.fBaseSystemId != null) {
            n2 += this.fBaseSystemId.hashCode();
        }
        if (this.fExpandedSystemId != null) {
            n2 += this.fExpandedSystemId.hashCode();
        }
        if (this.fNamespace != null) {
            n2 += this.fNamespace.hashCode();
        }
        return n2;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.fPublicId != null) {
            stringBuffer.append(this.fPublicId);
        }
        stringBuffer.append(':');
        if (this.fLiteralSystemId != null) {
            stringBuffer.append(this.fLiteralSystemId);
        }
        stringBuffer.append(':');
        if (this.fBaseSystemId != null) {
            stringBuffer.append(this.fBaseSystemId);
        }
        stringBuffer.append(':');
        if (this.fExpandedSystemId != null) {
            stringBuffer.append(this.fExpandedSystemId);
        }
        stringBuffer.append(':');
        if (this.fNamespace != null) {
            stringBuffer.append(this.fNamespace);
        }
        return stringBuffer.toString();
    }
}

