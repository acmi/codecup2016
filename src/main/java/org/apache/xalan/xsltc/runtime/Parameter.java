/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

public class Parameter {
    public String _name;
    public Object _value;
    public boolean _isDefault;

    public Parameter(String string, Object object) {
        this._name = string;
        this._value = object;
        this._isDefault = true;
    }

    public Parameter(String string, Object object, boolean bl) {
        this._name = string;
        this._value = object;
        this._isDefault = bl;
    }
}

