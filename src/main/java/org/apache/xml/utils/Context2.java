/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

final class Context2 {
    private static final Enumeration EMPTY_ENUMERATION = new Vector().elements();
    Hashtable prefixTable;
    Hashtable uriTable;
    Hashtable elementNameTable;
    Hashtable attributeNameTable;
    String defaultNS = null;
    private Vector declarations = null;
    private boolean tablesDirty = false;
    private Context2 parent = null;
    private Context2 child = null;

    Context2(Context2 context2) {
        if (context2 == null) {
            this.prefixTable = new Hashtable();
            this.uriTable = new Hashtable();
            this.elementNameTable = null;
            this.attributeNameTable = null;
        } else {
            this.setParent(context2);
        }
    }

    Context2 getChild() {
        return this.child;
    }

    Context2 getParent() {
        return this.parent;
    }

    void setParent(Context2 context2) {
        this.parent = context2;
        context2.child = this;
        this.declarations = null;
        this.prefixTable = context2.prefixTable;
        this.uriTable = context2.uriTable;
        this.elementNameTable = context2.elementNameTable;
        this.attributeNameTable = context2.attributeNameTable;
        this.defaultNS = context2.defaultNS;
        this.tablesDirty = false;
    }

    void declarePrefix(String string, String string2) {
        if (!this.tablesDirty) {
            this.copyTables();
        }
        if (this.declarations == null) {
            this.declarations = new Vector();
        }
        string = string.intern();
        string2 = string2.intern();
        if ("".equals(string)) {
            this.defaultNS = "".equals(string2) ? null : string2;
        } else {
            this.prefixTable.put(string, string2);
            this.uriTable.put(string2, string);
        }
        this.declarations.addElement(string);
    }

    String[] processName(String string, boolean bl) {
        Hashtable hashtable;
        if (bl) {
            if (this.elementNameTable == null) {
                this.elementNameTable = new Hashtable();
            }
            hashtable = this.elementNameTable;
        } else {
            if (this.attributeNameTable == null) {
                this.attributeNameTable = new Hashtable();
            }
            hashtable = this.attributeNameTable;
        }
        String[] arrstring = (String[])hashtable.get(string);
        if (arrstring != null) {
            return arrstring;
        }
        arrstring = new String[3];
        int n2 = string.indexOf(58);
        if (n2 == -1) {
            arrstring[0] = bl || this.defaultNS == null ? "" : this.defaultNS;
            arrstring[1] = string.intern();
            arrstring[2] = arrstring[1];
        } else {
            String string2 = string.substring(0, n2);
            String string3 = string.substring(n2 + 1);
            String string4 = "".equals(string2) ? this.defaultNS : (String)this.prefixTable.get(string2);
            if (string4 == null) {
                return null;
            }
            arrstring[0] = string4;
            arrstring[1] = string3.intern();
            arrstring[2] = string.intern();
        }
        hashtable.put(arrstring[2], arrstring);
        this.tablesDirty = true;
        return arrstring;
    }

    String getURI(String string) {
        if ("".equals(string)) {
            return this.defaultNS;
        }
        if (this.prefixTable == null) {
            return null;
        }
        return (String)this.prefixTable.get(string);
    }

    Enumeration getDeclaredPrefixes() {
        if (this.declarations == null) {
            return EMPTY_ENUMERATION;
        }
        return this.declarations.elements();
    }

    private void copyTables() {
        this.prefixTable = (Hashtable)this.prefixTable.clone();
        this.uriTable = (Hashtable)this.uriTable.clone();
        if (this.elementNameTable != null) {
            this.elementNameTable = new Hashtable();
        }
        if (this.attributeNameTable != null) {
            this.attributeNameTable = new Hashtable();
        }
        this.tablesDirty = true;
    }
}

