/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.UnicodeEscaper;

public class JavaUnicodeEscaper
extends UnicodeEscaper {
    public static JavaUnicodeEscaper outsideOf(int n2, int n3) {
        return new JavaUnicodeEscaper(n2, n3, false);
    }

    public JavaUnicodeEscaper(int n2, int n3, boolean bl) {
        super(n2, n3, bl);
    }

    @Override
    protected String toUtf16Escape(int n2) {
        char[] arrc = Character.toChars(n2);
        return "\\u" + JavaUnicodeEscaper.hex(arrc[0]) + "\\u" + JavaUnicodeEscaper.hex(arrc[1]);
    }
}

