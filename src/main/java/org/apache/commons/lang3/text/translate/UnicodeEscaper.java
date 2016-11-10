/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CodePointTranslator;

public class UnicodeEscaper
extends CodePointTranslator {
    private final int below;
    private final int above;
    private final boolean between;

    protected UnicodeEscaper(int n2, int n3, boolean bl) {
        this.below = n2;
        this.above = n3;
        this.between = bl;
    }

    @Override
    public boolean translate(int n2, Writer writer) throws IOException {
        if (this.between ? n2 < this.below || n2 > this.above : n2 >= this.below && n2 <= this.above) {
            return false;
        }
        if (n2 > 65535) {
            writer.write(this.toUtf16Escape(n2));
        } else {
            writer.write("\\u");
            writer.write(HEX_DIGITS[n2 >> 12 & 15]);
            writer.write(HEX_DIGITS[n2 >> 8 & 15]);
            writer.write(HEX_DIGITS[n2 >> 4 & 15]);
            writer.write(HEX_DIGITS[n2 & 15]);
        }
        return true;
    }

    protected String toUtf16Escape(int n2) {
        return "\\u" + UnicodeEscaper.hex(n2);
    }
}

