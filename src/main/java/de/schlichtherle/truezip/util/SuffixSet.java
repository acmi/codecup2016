/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import de.schlichtherle.truezip.util.CanonicalStringSet;
import java.util.Collection;
import java.util.Locale;

public final class SuffixSet
extends CanonicalStringSet {
    public SuffixSet() {
        super(new SuffixMapper(), '|');
    }

    public SuffixSet(String string) {
        super(new SuffixMapper(), '|');
        super.addAll(string);
    }

    public SuffixSet(Collection<String> collection) {
        super(new SuffixMapper(), '|');
        super.addAll(collection);
    }

    private static class SuffixMapper
    implements CanonicalStringSet.Canonicalizer {
        private SuffixMapper() {
        }

        @Override
        public String map(Object object) {
            String string = object.toString();
            while (0 < string.length() && string.charAt(0) == '.') {
                string = string.substring(1);
            }
            return string.toLowerCase(Locale.ROOT);
        }
    }

}

