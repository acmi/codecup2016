/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

public class ParseException
extends RuntimeException {
    static final long serialVersionUID = -7012400318097691370L;
    final int location;

    public ParseException(String string, int n2) {
        super(string);
        this.location = n2;
    }

    public int getLocation() {
        return this.location;
    }
}

