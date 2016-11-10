/*
 * Decompiled with CFR 0_119.
 */
package org.w3c.dom.ls;

public class LSException
extends RuntimeException {
    public short code;
    public static final short PARSE_ERR = 81;
    public static final short SERIALIZE_ERR = 82;

    public LSException(short s2, String string) {
        super(string);
        this.code = s2;
    }
}

