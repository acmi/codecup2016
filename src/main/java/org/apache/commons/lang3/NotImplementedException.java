/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3;

public class NotImplementedException
extends UnsupportedOperationException {
    private final String code;

    public NotImplementedException(String string) {
        this(string, null);
    }

    public NotImplementedException(String string, String string2) {
        super(string);
        this.code = string2;
    }
}

