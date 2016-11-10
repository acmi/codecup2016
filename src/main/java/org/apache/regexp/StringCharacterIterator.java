/*
 * Decompiled with CFR 0_119.
 */
package org.apache.regexp;

import org.apache.regexp.CharacterIterator;

public final class StringCharacterIterator
implements CharacterIterator {
    private final String src;

    public StringCharacterIterator(String string) {
        this.src = string;
    }

    public char charAt(int n2) {
        return this.src.charAt(n2);
    }

    public boolean isEnd(int n2) {
        return n2 >= this.src.length();
    }
}

