/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import java.util.Hashtable;
import java.util.Vector;

public final class MultiHashtable
extends Hashtable {
    static final long serialVersionUID = -6151608290510033572L;

    public Object put(Object object, Object object2) {
        Vector<Object> vector = (Vector<Object>)this.get(object);
        if (vector == null) {
            vector = new Vector<Object>();
            super.put(object, vector);
        }
        vector.add(object2);
        return vector;
    }

    public Object maps(Object object, Object object2) {
        if (object == null) {
            return null;
        }
        Vector vector = (Vector)this.get(object);
        if (vector != null) {
            int n2 = vector.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Object e2 = vector.elementAt(i2);
                if (!e2.equals(object2)) continue;
                return e2;
            }
        }
        return null;
    }
}

