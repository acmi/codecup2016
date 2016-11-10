/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class UnicodeUnescaper
extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence charSequence, int n2, Writer writer) throws IOException {
        if (charSequence.charAt(n2) == '\\' && n2 + 1 < charSequence.length() && charSequence.charAt(n2 + 1) == 'u') {
            int n3 = 2;
            while (n2 + n3 < charSequence.length() && charSequence.charAt(n2 + n3) == 'u') {
                ++n3;
            }
            if (n2 + n3 < charSequence.length() && charSequence.charAt(n2 + n3) == '+') {
                ++n3;
            }
            if (n2 + n3 + 4 <= charSequence.length()) {
                CharSequence charSequence2 = charSequence.subSequence(n2 + n3, n2 + n3 + 4);
                try {
                    int n4 = Integer.parseInt(charSequence2.toString(), 16);
                    writer.write((char)n4);
                }
                catch (NumberFormatException numberFormatException) {
                    throw new IllegalArgumentException("Unable to parse unicode value: " + charSequence2, numberFormatException);
                }
                return n3 + 4;
            }
            throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + charSequence.subSequence(n2, charSequence.length()) + "' due to end of CharSequence");
        }
        return 0;
    }
}

