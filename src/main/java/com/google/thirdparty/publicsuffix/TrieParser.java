/*
 * Decompiled with CFR 0_119.
 */
package com.google.thirdparty.publicsuffix;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.thirdparty.publicsuffix.PublicSuffixType;
import java.util.List;

class TrieParser {
    private static final Joiner PREFIX_JOINER = Joiner.on("");

    TrieParser() {
    }

    static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence charSequence) {
        ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
        int n2 = charSequence.length();
        for (int i2 = 0; i2 < n2; i2 += TrieParser.doParseTrieToBuilder(Lists.<CharSequence>newLinkedList(), (CharSequence)charSequence.subSequence((int)i2, (int)n2), builder)) {
        }
        return builder.build();
    }

    private static int doParseTrieToBuilder(List<CharSequence> list, CharSequence charSequence, ImmutableMap.Builder<String, PublicSuffixType> builder) {
        String string;
        int n2;
        int n3 = charSequence.length();
        char c2 = '\u0000';
        for (n2 = 0; n2 < n3 && (c2 = charSequence.charAt(n2)) != '&' && c2 != '?' && c2 != '!' && c2 != ':' && c2 != ','; ++n2) {
        }
        list.add(0, TrieParser.reverse(charSequence.subSequence(0, n2)));
        if ((c2 == '!' || c2 == '?' || c2 == ':' || c2 == ',') && (string = PREFIX_JOINER.join(list)).length() > 0) {
            builder.put(string, PublicSuffixType.fromCode(c2));
        }
        ++n2;
        if (c2 != '?' && c2 != ',') {
            while (n2 < n3) {
                if (charSequence.charAt(n2 += TrieParser.doParseTrieToBuilder(list, charSequence.subSequence(n2, n3), builder)) != '?' && charSequence.charAt(n2) != ',') continue;
                ++n2;
                break;
            }
        }
        list.remove(0);
        return n2;
    }

    private static CharSequence reverse(CharSequence charSequence) {
        int n2 = charSequence.length();
        if (n2 <= 1) {
            return charSequence;
        }
        char[] arrc = new char[n2];
        arrc[0] = charSequence.charAt(n2 - 1);
        for (int i2 = 1; i2 < n2; ++i2) {
            arrc[i2] = charSequence.charAt(n2 - 1 - i2);
            if (!Character.isSurrogatePair(arrc[i2], arrc[i2 - 1])) continue;
            TrieParser.swap(arrc, i2 - 1, i2);
        }
        return new String(arrc);
    }

    private static void swap(char[] arrc, int n2, int n3) {
        char c2 = arrc[n2];
        arrc[n2] = arrc[n3];
        arrc[n3] = c2;
    }
}

