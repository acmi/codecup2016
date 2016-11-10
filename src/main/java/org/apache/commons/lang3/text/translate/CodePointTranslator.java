/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public abstract class CodePointTranslator
extends CharSequenceTranslator {
    @Override
    public final int translate(CharSequence charSequence, int n2, Writer writer) throws IOException {
        int n3 = Character.codePointAt(charSequence, n2);
        boolean bl = this.translate(n3, writer);
        return bl ? 1 : 0;
    }

    public abstract boolean translate(int var1, Writer var2) throws IOException;
}

