/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CodePointTranslator;

public class NumericEntityEscaper
extends CodePointTranslator {
    private final int below;
    private final int above;
    private final boolean between;

    private NumericEntityEscaper(int n2, int n3, boolean bl) {
        this.below = n2;
        this.above = n3;
        this.between = bl;
    }

    public NumericEntityEscaper() {
        this(0, Integer.MAX_VALUE, true);
    }

    public static NumericEntityEscaper between(int n2, int n3) {
        return new NumericEntityEscaper(n2, n3, true);
    }

    @Override
    public boolean translate(int n2, Writer writer) throws IOException {
        if (this.between ? n2 < this.below || n2 > this.above : n2 >= this.below && n2 <= this.above) {
            return false;
        }
        writer.write("&#");
        writer.write(Integer.toString(n2, 10));
        writer.write(59);
        return true;
    }
}

