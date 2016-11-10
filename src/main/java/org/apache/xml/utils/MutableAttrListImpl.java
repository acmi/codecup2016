/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.Serializable;
import org.xml.sax.helpers.AttributesImpl;

public class MutableAttrListImpl
extends AttributesImpl
implements Serializable {
    public void addAttribute(String string, String string2, String string3, String string4, String string5) {
        int n2;
        if (null == string) {
            string = "";
        }
        if ((n2 = this.getIndex(string3)) >= 0) {
            this.setAttribute(n2, string, string2, string3, string4, string5);
        } else {
            super.addAttribute(string, string2, string3, string4, string5);
        }
    }
}

