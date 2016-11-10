/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CodePointTranslator;

public class UnicodeUnpairedSurrogateRemover
extends CodePointTranslator {
    @Override
    public boolean translate(int n2, Writer writer) throws IOException {
        if (n2 >= 55296 && n2 <= 57343) {
            return true;
        }
        return false;
    }
}

