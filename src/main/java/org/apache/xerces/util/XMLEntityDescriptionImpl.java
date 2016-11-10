/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.impl.XMLEntityDescription;
import org.apache.xerces.util.XMLResourceIdentifierImpl;

public class XMLEntityDescriptionImpl
extends XMLResourceIdentifierImpl
implements XMLEntityDescription {
    protected String fEntityName;

    public XMLEntityDescriptionImpl() {
    }

    public XMLEntityDescriptionImpl(String string, String string2, String string3, String string4, String string5) {
        this.setDescription(string, string2, string3, string4, string5);
    }

    public XMLEntityDescriptionImpl(String string, String string2, String string3, String string4, String string5, String string6) {
        this.setDescription(string, string2, string3, string4, string5, string6);
    }

    public void setEntityName(String string) {
        this.fEntityName = string;
    }

    public String getEntityName() {
        return this.fEntityName;
    }

    public void setDescription(String string, String string2, String string3, String string4, String string5) {
        this.setDescription(string, string2, string3, string4, string5, null);
    }

    public void setDescription(String string, String string2, String string3, String string4, String string5, String string6) {
        this.fEntityName = string;
        this.setValues(string2, string3, string4, string5, string6);
    }

    public void clear() {
        super.clear();
        this.fEntityName = null;
    }

    public int hashCode() {
        int n2 = super.hashCode();
        if (this.fEntityName != null) {
            n2 += this.fEntityName.hashCode();
        }
        return n2;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.fEntityName != null) {
            stringBuffer.append(this.fEntityName);
        }
        stringBuffer.append(':');
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

