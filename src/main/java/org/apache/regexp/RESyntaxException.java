/*
 * Decompiled with CFR 0_119.
 */
package org.apache.regexp;

public class RESyntaxException
extends Exception {
    public RESyntaxException(String string) {
        super("Syntax error: " + string);
    }
}

