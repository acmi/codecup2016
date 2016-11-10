/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.EmptyStackException;
import java.util.Enumeration;
import org.apache.xml.utils.Context2;
import org.xml.sax.helpers.NamespaceSupport;

public class NamespaceSupport2
extends NamespaceSupport {
    private Context2 currentContext;

    public NamespaceSupport2() {
        this.reset();
    }

    public void reset() {
        this.currentContext = new Context2(null);
        this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
    }

    public void pushContext() {
        Context2 context2 = this.currentContext;
        this.currentContext = context2.getChild();
        if (this.currentContext == null) {
            this.currentContext = new Context2(context2);
        } else {
            this.currentContext.setParent(context2);
        }
    }

    public void popContext() {
        Context2 context2 = this.currentContext.getParent();
        if (context2 == null) {
            throw new EmptyStackException();
        }
        this.currentContext = context2;
    }

    public boolean declarePrefix(String string, String string2) {
        if (string.equals("xml") || string.equals("xmlns")) {
            return false;
        }
        this.currentContext.declarePrefix(string, string2);
        return true;
    }

    public String[] processName(String string, String[] arrstring, boolean bl) {
        String[] arrstring2 = this.currentContext.processName(string, bl);
        if (arrstring2 == null) {
            return null;
        }
        System.arraycopy(arrstring2, 0, arrstring, 0, 3);
        return arrstring;
    }

    public String getURI(String string) {
        return this.currentContext.getURI(string);
    }

    public Enumeration getDeclaredPrefixes() {
        return this.currentContext.getDeclaredPrefixes();
    }
}

