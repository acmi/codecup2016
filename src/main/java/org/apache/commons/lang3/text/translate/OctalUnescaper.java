/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class OctalUnescaper
extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence charSequence, int n2, Writer writer) throws IOException {
        int n3 = charSequence.length() - n2 - 1;
        StringBuilder stringBuilder = new StringBuilder();
        if (charSequence.charAt(n2) == '\\' && n3 > 0 && this.isOctalDigit(charSequence.charAt(n2 + 1))) {
            int n4 = n2 + 1;
            int n5 = n2 + 2;
            int n6 = n2 + 3;
            stringBuilder.append(charSequence.charAt(n4));
            if (n3 > 1 && this.isOctalDigit(charSequence.charAt(n5))) {
                stringBuilder.append(charSequence.charAt(n5));
                if (n3 > 2 && this.isZeroToThree(charSequence.charAt(n4)) && this.isOctalDigit(charSequence.charAt(n6))) {
                    stringBuilder.append(charSequence.charAt(n6));
                }
            }
            writer.write(Integer.parseInt(stringBuilder.toString(), 8));
            return 1 + stringBuilder.length();
        }
        return 0;
    }

    private boolean isOctalDigit(char c2) {
        return c2 >= '0' && c2 <= '7';
    }

    private boolean isZeroToThree(char c2) {
        return c2 >= '0' && c2 <= '3';
    }
}

