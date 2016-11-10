/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

import java.text.CharacterIterator;

public class BMPattern {
    final char[] pattern;
    final int[] shiftTable;
    final boolean ignoreCase;

    public BMPattern(String string, boolean bl) {
        this(string, 256, bl);
    }

    public BMPattern(String string, int n2, boolean bl) {
        this.pattern = string.toCharArray();
        this.shiftTable = new int[n2];
        this.ignoreCase = bl;
        int n3 = this.pattern.length;
        int n4 = 0;
        while (n4 < this.shiftTable.length) {
            this.shiftTable[n4] = n3;
            ++n4;
        }
        int n5 = 0;
        while (n5 < n3) {
            int n6 = n3 - n5 - 1;
            char c2 = this.pattern[n5];
            int n7 = c2 % this.shiftTable.length;
            if (n6 < this.shiftTable[n7]) {
                this.shiftTable[n7] = n6;
            }
            if (this.ignoreCase) {
                n7 = (c2 = Character.toUpperCase(c2)) % this.shiftTable.length;
                if (n6 < this.shiftTable[n7]) {
                    this.shiftTable[n7] = n6;
                }
                if (n6 < this.shiftTable[n7 = (c2 = Character.toLowerCase(c2)) % this.shiftTable.length]) {
                    this.shiftTable[n7] = n6;
                }
            }
            ++n5;
        }
    }

    public int matches(CharacterIterator characterIterator, int n2, int n3) {
        if (this.ignoreCase) {
            return this.matchesIgnoreCase(characterIterator, n2, n3);
        }
        int n4 = this.pattern.length;
        if (n4 == 0) {
            return n2;
        }
        int n5 = n2 + n4;
        while (n5 <= n3) {
            char c2;
            int n6 = n4;
            int n7 = n5 + 1;
            while ((c2 = characterIterator.setIndex(--n5)) == this.pattern[--n6]) {
                if (n6 == 0) {
                    return n5;
                }
                if (n6 > 0) continue;
            }
            if ((n5 += this.shiftTable[c2 % this.shiftTable.length] + 1) >= n7) continue;
            n5 = n7;
        }
        return -1;
    }

    public int matches(String string, int n2, int n3) {
        if (this.ignoreCase) {
            return this.matchesIgnoreCase(string, n2, n3);
        }
        int n4 = this.pattern.length;
        if (n4 == 0) {
            return n2;
        }
        int n5 = n2 + n4;
        while (n5 <= n3) {
            char c2;
            int n6 = n4;
            int n7 = n5 + 1;
            while ((c2 = string.charAt(--n5)) == this.pattern[--n6]) {
                if (n6 == 0) {
                    return n5;
                }
                if (n6 > 0) continue;
            }
            if ((n5 += this.shiftTable[c2 % this.shiftTable.length] + 1) >= n7) continue;
            n5 = n7;
        }
        return -1;
    }

    public int matches(char[] arrc, int n2, int n3) {
        if (this.ignoreCase) {
            return this.matchesIgnoreCase(arrc, n2, n3);
        }
        int n4 = this.pattern.length;
        if (n4 == 0) {
            return n2;
        }
        int n5 = n2 + n4;
        while (n5 <= n3) {
            char c2;
            int n6 = n4;
            int n7 = n5 + 1;
            while ((c2 = arrc[--n5]) == this.pattern[--n6]) {
                if (n6 == 0) {
                    return n5;
                }
                if (n6 > 0) continue;
            }
            if ((n5 += this.shiftTable[c2 % this.shiftTable.length] + 1) >= n7) continue;
            n5 = n7;
        }
        return -1;
    }

    int matchesIgnoreCase(CharacterIterator characterIterator, int n2, int n3) {
        int n4 = this.pattern.length;
        if (n4 == 0) {
            return n2;
        }
        int n5 = n2 + n4;
        while (n5 <= n3) {
            char c2;
            char c3;
            char c4;
            int n6 = n4;
            int n7 = n5 + 1;
            while ((c4 = (c2 = characterIterator.setIndex(--n5))) == (c3 = this.pattern[--n6]) || (c4 = Character.toUpperCase(c4)) == (c3 = Character.toUpperCase(c3)) || Character.toLowerCase(c4) == Character.toLowerCase(c3)) {
                if (n6 == 0) {
                    return n5;
                }
                if (n6 > 0) continue;
            }
            if ((n5 += this.shiftTable[c2 % this.shiftTable.length] + 1) >= n7) continue;
            n5 = n7;
        }
        return -1;
    }

    int matchesIgnoreCase(String string, int n2, int n3) {
        int n4 = this.pattern.length;
        if (n4 == 0) {
            return n2;
        }
        int n5 = n2 + n4;
        while (n5 <= n3) {
            char c2;
            char c3;
            char c4;
            int n6 = n4;
            int n7 = n5 + 1;
            while ((c4 = (c2 = string.charAt(--n5))) == (c3 = this.pattern[--n6]) || (c4 = Character.toUpperCase(c4)) == (c3 = Character.toUpperCase(c3)) || Character.toLowerCase(c4) == Character.toLowerCase(c3)) {
                if (n6 == 0) {
                    return n5;
                }
                if (n6 > 0) continue;
            }
            if ((n5 += this.shiftTable[c2 % this.shiftTable.length] + 1) >= n7) continue;
            n5 = n7;
        }
        return -1;
    }

    int matchesIgnoreCase(char[] arrc, int n2, int n3) {
        int n4 = this.pattern.length;
        if (n4 == 0) {
            return n2;
        }
        int n5 = n2 + n4;
        while (n5 <= n3) {
            char c2;
            char c3;
            char c4;
            int n6 = n4;
            int n7 = n5 + 1;
            while ((c4 = (c2 = arrc[--n5])) == (c3 = this.pattern[--n6]) || (c4 = Character.toUpperCase(c4)) == (c3 = Character.toUpperCase(c3)) || Character.toLowerCase(c4) == Character.toLowerCase(c3)) {
                if (n6 == 0) {
                    return n5;
                }
                if (n6 > 0) continue;
            }
            if ((n5 += this.shiftTable[c2 % this.shiftTable.length] + 1) >= n7) continue;
            n5 = n7;
        }
        return -1;
    }
}

