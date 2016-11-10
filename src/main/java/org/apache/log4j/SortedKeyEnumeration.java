/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

class SortedKeyEnumeration
implements Enumeration {
    private Enumeration e;

    public SortedKeyEnumeration(Hashtable hashtable) {
        Enumeration enumeration = hashtable.keys();
        Vector<String> vector = new Vector<String>(hashtable.size());
        int n2 = 0;
        while (enumeration.hasMoreElements()) {
            int n3;
            String string;
            String string2 = (String)enumeration.nextElement();
            for (n3 = 0; n3 < n2 && string2.compareTo(string = (String)vector.get(n3)) > 0; ++n3) {
            }
            vector.add(n3, string2);
            ++n2;
        }
        this.e = vector.elements();
    }

    public boolean hasMoreElements() {
        return this.e.hasMoreElements();
    }

    public Object nextElement() {
        return this.e.nextElement();
    }
}

