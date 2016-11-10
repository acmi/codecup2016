/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class AggregateTranslator
extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public /* varargs */ AggregateTranslator(CharSequenceTranslator ... arrcharSequenceTranslator) {
        this.translators = ArrayUtils.clone(arrcharSequenceTranslator);
    }

    @Override
    public int translate(CharSequence charSequence, int n2, Writer writer) throws IOException {
        for (CharSequenceTranslator charSequenceTranslator : this.translators) {
            int n3 = charSequenceTranslator.translate(charSequence, n2, writer);
            if (n3 == 0) continue;
            return n3;
        }
        return 0;
    }
}

