/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class LookupTranslator
extends CharSequenceTranslator {
    private final HashMap<String, String> lookupMap = new HashMap();
    private final HashSet<Character> prefixSet = new HashSet();
    private final int shortest;
    private final int longest;

    public /* varargs */ LookupTranslator(CharSequence[] ... arrcharSequence) {
        int n2 = Integer.MAX_VALUE;
        int n3 = 0;
        if (arrcharSequence != null) {
            for (CharSequence[] arrcharSequence2 : arrcharSequence) {
                this.lookupMap.put(arrcharSequence2[0].toString(), arrcharSequence2[1].toString());
                this.prefixSet.add(Character.valueOf(arrcharSequence2[0].charAt(0)));
                int n4 = arrcharSequence2[0].length();
                if (n4 < n2) {
                    n2 = n4;
                }
                if (n4 <= n3) continue;
                n3 = n4;
            }
        }
        this.shortest = n2;
        this.longest = n3;
    }

    @Override
    public int translate(CharSequence charSequence, int n2, Writer writer) throws IOException {
        if (this.prefixSet.contains(Character.valueOf(charSequence.charAt(n2)))) {
            int n3 = this.longest;
            if (n2 + this.longest > charSequence.length()) {
                n3 = charSequence.length() - n2;
            }
            for (int i2 = n3; i2 >= this.shortest; --i2) {
                CharSequence charSequence2 = charSequence.subSequence(n2, n2 + i2);
                String string = this.lookupMap.get(charSequence2.toString());
                if (string == null) continue;
                writer.write(string);
                return i2;
            }
        }
        return 0;
    }
}

