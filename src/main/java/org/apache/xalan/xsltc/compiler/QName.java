/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

final class QName {
    private final String _localname;
    private String _prefix;
    private String _namespace;
    private String _stringRep;
    private int _hashCode;

    public QName(String string, String string2, String string3) {
        this._namespace = string;
        this._prefix = string2;
        this._localname = string3;
        this._stringRep = string != null && !string.equals("") ? string + ':' + string3 : string3;
        this._hashCode = this._stringRep.hashCode() + 19;
    }

    public void clearNamespace() {
        this._namespace = "";
    }

    public String toString() {
        return this._stringRep;
    }

    public String getStringRep() {
        return this._stringRep;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof QName && this._stringRep.equals(((QName)object).getStringRep());
    }

    public String getLocalPart() {
        return this._localname;
    }

    public String getNamespace() {
        return this._namespace;
    }

    public String getPrefix() {
        return this._prefix;
    }

    public int hashCode() {
        return this._hashCode;
    }

    public String dump() {
        return "QName: " + this._namespace + "(" + this._prefix + "):" + this._localname;
    }
}

