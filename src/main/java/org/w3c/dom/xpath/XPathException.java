/*
 * Decompiled with CFR 0_119.
 */
package org.w3c.dom.xpath;

public class XPathException
extends RuntimeException {
    public short code;
    public static final short INVALID_EXPRESSION_ERR = 51;
    public static final short TYPE_ERR = 52;

    public XPathException(short s2, String string) {
        super(string);
        this.code = s2;
    }
}

