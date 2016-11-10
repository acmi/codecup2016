/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import java.net.URISyntaxException;

public class QuotedUriSyntaxException
extends URISyntaxException {
    public QuotedUriSyntaxException(Object object, String string) {
        this(object, string, -1);
    }

    public QuotedUriSyntaxException(Object object, String string, int n2) {
        super("\"" + object + "\"", string, n2);
    }
}

