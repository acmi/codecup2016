/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

import java.util.Vector;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.xml.sax.Attributes;

public class AttributeList
implements Attributes {
    private static final String EMPTYSTRING = "";
    private static final String CDATASTRING = "CDATA";
    private Hashtable _attributes;
    private Vector _names;
    private Vector _qnames;
    private Vector _values;
    private Vector _uris;
    private int _length = 0;

    public AttributeList() {
    }

    public AttributeList(Attributes attributes) {
        this();
        if (attributes != null) {
            int n2 = attributes.getLength();
            for (int i2 = 0; i2 < n2; ++i2) {
                this.add(attributes.getQName(i2), attributes.getValue(i2));
            }
        }
    }

    private void alloc() {
        this._attributes = new Hashtable();
        this._names = new Vector();
        this._values = new Vector();
        this._qnames = new Vector();
        this._uris = new Vector();
    }

    public int getLength() {
        return this._length;
    }

    public String getURI(int n2) {
        if (n2 < this._length) {
            return (String)this._uris.elementAt(n2);
        }
        return null;
    }

    public String getLocalName(int n2) {
        if (n2 < this._length) {
            return (String)this._names.elementAt(n2);
        }
        return null;
    }

    public String getQName(int n2) {
        if (n2 < this._length) {
            return (String)this._qnames.elementAt(n2);
        }
        return null;
    }

    public String getType(int n2) {
        return "CDATA";
    }

    public int getIndex(String string, String string2) {
        return -1;
    }

    public int getIndex(String string) {
        return -1;
    }

    public String getType(String string, String string2) {
        return "CDATA";
    }

    public String getType(String string) {
        return "CDATA";
    }

    public String getValue(int n2) {
        if (n2 < this._length) {
            return (String)this._values.elementAt(n2);
        }
        return null;
    }

    public String getValue(String string) {
        if (this._attributes != null) {
            Integer n2 = (Integer)this._attributes.get(string);
            if (n2 == null) {
                return null;
            }
            return this.getValue(n2);
        }
        return null;
    }

    public String getValue(String string, String string2) {
        return this.getValue(string + ':' + string2);
    }

    public void add(String string, String string2) {
        Integer n2;
        if (this._attributes == null) {
            this.alloc();
        }
        if ((n2 = (Integer)this._attributes.get(string)) == null) {
            n2 = new Integer(this._length++);
            this._attributes.put(string, n2);
            this._qnames.addElement(string);
            this._values.addElement(string2);
            int n3 = string.lastIndexOf(58);
            if (n3 > -1) {
                this._uris.addElement(string.substring(0, n3));
                this._names.addElement(string.substring(n3 + 1));
            } else {
                this._uris.addElement("");
                this._names.addElement(string);
            }
        } else {
            int n4 = n2;
            this._values.set(n4, string2);
        }
    }

    public void clear() {
        this._length = 0;
        if (this._attributes != null) {
            this._attributes.clear();
            this._names.removeAllElements();
            this._values.removeAllElements();
            this._qnames.removeAllElements();
            this._uris.removeAllElements();
        }
    }
}

