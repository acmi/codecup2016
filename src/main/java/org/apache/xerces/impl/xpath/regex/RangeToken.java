/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

import java.io.PrintStream;
import java.io.Serializable;
import org.apache.xerces.impl.xpath.regex.Token;

final class RangeToken
extends Token
implements Serializable {
    private static final long serialVersionUID = -553983121197679934L;
    int[] ranges;
    boolean sorted;
    boolean compacted;
    RangeToken icaseCache = null;
    int[] map = null;
    int nonMapIndex;
    private static final int MAPSIZE = 256;

    RangeToken(int n2) {
        super(n2);
        this.setSorted(false);
    }

    protected void addRange(int n2, int n3) {
        int n4;
        int n5;
        this.icaseCache = null;
        if (n2 <= n3) {
            n5 = n2;
            n4 = n3;
        } else {
            n5 = n3;
            n4 = n2;
        }
        int n6 = 0;
        if (this.ranges == null) {
            this.ranges = new int[2];
            this.ranges[0] = n5;
            this.ranges[1] = n4;
            this.setSorted(true);
        } else {
            n6 = this.ranges.length;
            if (this.ranges[n6 - 1] + 1 == n5) {
                this.ranges[n6 - 1] = n4;
                return;
            }
            int[] arrn = new int[n6 + 2];
            System.arraycopy(this.ranges, 0, arrn, 0, n6);
            this.ranges = arrn;
            if (this.ranges[n6 - 1] >= n5) {
                this.setSorted(false);
            }
            this.ranges[n6++] = n5;
            this.ranges[n6] = n4;
            if (!this.sorted) {
                this.sortRanges();
            }
        }
    }

    private final boolean isSorted() {
        return this.sorted;
    }

    private final void setSorted(boolean bl) {
        this.sorted = bl;
        if (!bl) {
            this.compacted = false;
        }
    }

    private final boolean isCompacted() {
        return this.compacted;
    }

    private final void setCompacted() {
        this.compacted = true;
    }

    protected void sortRanges() {
        if (this.isSorted()) {
            return;
        }
        if (this.ranges == null) {
            return;
        }
        int n2 = this.ranges.length - 4;
        while (n2 >= 0) {
            int n3 = 0;
            while (n3 <= n2) {
                if (this.ranges[n3] > this.ranges[n3 + 2] || this.ranges[n3] == this.ranges[n3 + 2] && this.ranges[n3 + 1] > this.ranges[n3 + 3]) {
                    int n4 = this.ranges[n3 + 2];
                    this.ranges[n3 + 2] = this.ranges[n3];
                    this.ranges[n3] = n4;
                    n4 = this.ranges[n3 + 3];
                    this.ranges[n3 + 3] = this.ranges[n3 + 1];
                    this.ranges[n3 + 1] = n4;
                }
                n3 += 2;
            }
            n2 -= 2;
        }
        this.setSorted(true);
    }

    protected void compactRanges() {
        boolean bl = false;
        if (this.ranges == null || this.ranges.length <= 2) {
            return;
        }
        if (this.isCompacted()) {
            return;
        }
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.ranges.length) {
            if (n2 != n3) {
                this.ranges[n2] = this.ranges[n3++];
                this.ranges[n2 + 1] = this.ranges[n3++];
            } else {
                n3 += 2;
            }
            int n4 = this.ranges[n2 + 1];
            while (n3 < this.ranges.length) {
                if (n4 + 1 < this.ranges[n3]) break;
                if (n4 + 1 == this.ranges[n3]) {
                    if (bl) {
                        System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[n2] + ", " + this.ranges[n2 + 1] + "], [" + this.ranges[n3] + ", " + this.ranges[n3 + 1] + "] -> [" + this.ranges[n2] + ", " + this.ranges[n3 + 1] + "]");
                    }
                    this.ranges[n2 + 1] = this.ranges[n3 + 1];
                    n4 = this.ranges[n2 + 1];
                    n3 += 2;
                    continue;
                }
                if (n4 >= this.ranges[n3 + 1]) {
                    if (bl) {
                        System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[n2] + ", " + this.ranges[n2 + 1] + "], [" + this.ranges[n3] + ", " + this.ranges[n3 + 1] + "] -> [" + this.ranges[n2] + ", " + this.ranges[n2 + 1] + "]");
                    }
                    n3 += 2;
                    continue;
                }
                if (n4 < this.ranges[n3 + 1]) {
                    if (bl) {
                        System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[n2] + ", " + this.ranges[n2 + 1] + "], [" + this.ranges[n3] + ", " + this.ranges[n3 + 1] + "] -> [" + this.ranges[n2] + ", " + this.ranges[n3 + 1] + "]");
                    }
                    this.ranges[n2 + 1] = this.ranges[n3 + 1];
                    n4 = this.ranges[n2 + 1];
                    n3 += 2;
                    continue;
                }
                throw new RuntimeException("Token#compactRanges(): Internel Error: [" + this.ranges[n2] + "," + this.ranges[n2 + 1] + "] [" + this.ranges[n3] + "," + this.ranges[n3 + 1] + "]");
            }
            n2 += 2;
        }
        if (n2 != this.ranges.length) {
            int[] arrn = new int[n2];
            System.arraycopy(this.ranges, 0, arrn, 0, n2);
            this.ranges = arrn;
        }
        this.setCompacted();
    }

    protected void mergeRanges(Token token) {
        RangeToken rangeToken = (RangeToken)token;
        this.sortRanges();
        rangeToken.sortRanges();
        if (rangeToken.ranges == null) {
            return;
        }
        this.icaseCache = null;
        this.setSorted(true);
        if (this.ranges == null) {
            this.ranges = new int[rangeToken.ranges.length];
            System.arraycopy(rangeToken.ranges, 0, this.ranges, 0, rangeToken.ranges.length);
            return;
        }
        int[] arrn = new int[this.ranges.length + rangeToken.ranges.length];
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        while (n2 < this.ranges.length || n3 < rangeToken.ranges.length) {
            if (n2 >= this.ranges.length) {
                arrn[n4++] = rangeToken.ranges[n3++];
                arrn[n4++] = rangeToken.ranges[n3++];
                continue;
            }
            if (n3 >= rangeToken.ranges.length) {
                arrn[n4++] = this.ranges[n2++];
                arrn[n4++] = this.ranges[n2++];
                continue;
            }
            if (rangeToken.ranges[n3] < this.ranges[n2] || rangeToken.ranges[n3] == this.ranges[n2] && rangeToken.ranges[n3 + 1] < this.ranges[n2 + 1]) {
                arrn[n4++] = rangeToken.ranges[n3++];
                arrn[n4++] = rangeToken.ranges[n3++];
                continue;
            }
            arrn[n4++] = this.ranges[n2++];
            arrn[n4++] = this.ranges[n2++];
        }
        this.ranges = arrn;
    }

    protected void subtractRanges(Token token) {
        if (token.type == 5) {
            this.intersectRanges(token);
            return;
        }
        RangeToken rangeToken = (RangeToken)token;
        if (rangeToken.ranges == null || this.ranges == null) {
            return;
        }
        this.icaseCache = null;
        this.sortRanges();
        this.compactRanges();
        rangeToken.sortRanges();
        rangeToken.compactRanges();
        int[] arrn = new int[this.ranges.length + rangeToken.ranges.length];
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        while (n3 < this.ranges.length && n4 < rangeToken.ranges.length) {
            int n5 = this.ranges[n3];
            int n6 = this.ranges[n3 + 1];
            int n7 = rangeToken.ranges[n4];
            int n8 = rangeToken.ranges[n4 + 1];
            if (n6 < n7) {
                arrn[n2++] = this.ranges[n3++];
                arrn[n2++] = this.ranges[n3++];
                continue;
            }
            if (n6 >= n7 && n5 <= n8) {
                if (n7 <= n5 && n6 <= n8) {
                    n3 += 2;
                    continue;
                }
                if (n7 <= n5) {
                    this.ranges[n3] = n8 + 1;
                    n4 += 2;
                    continue;
                }
                if (n6 <= n8) {
                    arrn[n2++] = n5;
                    arrn[n2++] = n7 - 1;
                    n3 += 2;
                    continue;
                }
                arrn[n2++] = n5;
                arrn[n2++] = n7 - 1;
                this.ranges[n3] = n8 + 1;
                n4 += 2;
                continue;
            }
            if (n8 < n5) {
                n4 += 2;
                continue;
            }
            throw new RuntimeException("Token#subtractRanges(): Internal Error: [" + this.ranges[n3] + "," + this.ranges[n3 + 1] + "] - [" + rangeToken.ranges[n4] + "," + rangeToken.ranges[n4 + 1] + "]");
        }
        while (n3 < this.ranges.length) {
            arrn[n2++] = this.ranges[n3++];
            arrn[n2++] = this.ranges[n3++];
        }
        this.ranges = new int[n2];
        System.arraycopy(arrn, 0, this.ranges, 0, n2);
    }

    protected void intersectRanges(Token token) {
        RangeToken rangeToken = (RangeToken)token;
        if (rangeToken.ranges == null || this.ranges == null) {
            return;
        }
        this.icaseCache = null;
        this.sortRanges();
        this.compactRanges();
        rangeToken.sortRanges();
        rangeToken.compactRanges();
        int[] arrn = new int[this.ranges.length + rangeToken.ranges.length];
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        while (n3 < this.ranges.length && n4 < rangeToken.ranges.length) {
            int n5 = this.ranges[n3];
            int n6 = this.ranges[n3 + 1];
            int n7 = rangeToken.ranges[n4];
            int n8 = rangeToken.ranges[n4 + 1];
            if (n6 < n7) {
                n3 += 2;
                continue;
            }
            if (n6 >= n7 && n5 <= n8) {
                if (n7 <= n5 && n6 <= n8) {
                    arrn[n2++] = n5;
                    arrn[n2++] = n6;
                    n3 += 2;
                    continue;
                }
                if (n7 <= n5) {
                    arrn[n2++] = n5;
                    arrn[n2++] = n8;
                    this.ranges[n3] = n8 + 1;
                    n4 += 2;
                    continue;
                }
                if (n6 <= n8) {
                    arrn[n2++] = n7;
                    arrn[n2++] = n6;
                    n3 += 2;
                    continue;
                }
                arrn[n2++] = n7;
                arrn[n2++] = n8;
                this.ranges[n3] = n8 + 1;
                continue;
            }
            if (n8 < n5) {
                n4 += 2;
                continue;
            }
            throw new RuntimeException("Token#intersectRanges(): Internal Error: [" + this.ranges[n3] + "," + this.ranges[n3 + 1] + "] & [" + rangeToken.ranges[n4] + "," + rangeToken.ranges[n4 + 1] + "]");
        }
        while (n3 < this.ranges.length) {
            arrn[n2++] = this.ranges[n3++];
            arrn[n2++] = this.ranges[n3++];
        }
        this.ranges = new int[n2];
        System.arraycopy(arrn, 0, this.ranges, 0, n2);
    }

    static Token complementRanges(Token token) {
        int n2;
        if (token.type != 4 && token.type != 5) {
            throw new IllegalArgumentException("Token#complementRanges(): must be RANGE: " + token.type);
        }
        RangeToken rangeToken = (RangeToken)token;
        rangeToken.sortRanges();
        rangeToken.compactRanges();
        int n3 = rangeToken.ranges.length + 2;
        if (rangeToken.ranges[0] == 0) {
            n3 -= 2;
        }
        if ((n2 = rangeToken.ranges[rangeToken.ranges.length - 1]) == 1114111) {
            n3 -= 2;
        }
        RangeToken rangeToken2 = Token.createRange();
        rangeToken2.ranges = new int[n3];
        int n4 = 0;
        if (rangeToken.ranges[0] > 0) {
            rangeToken2.ranges[n4++] = 0;
            rangeToken2.ranges[n4++] = rangeToken.ranges[0] - 1;
        }
        int n5 = 1;
        while (n5 < rangeToken.ranges.length - 2) {
            rangeToken2.ranges[n4++] = rangeToken.ranges[n5] + 1;
            rangeToken2.ranges[n4++] = rangeToken.ranges[n5 + 1] - 1;
            n5 += 2;
        }
        if (n2 != 1114111) {
            rangeToken2.ranges[n4++] = n2 + 1;
            rangeToken2.ranges[n4] = 1114111;
        }
        rangeToken2.setCompacted();
        return rangeToken2;
    }

    synchronized RangeToken getCaseInsensitiveToken() {
        int n2;
        if (this.icaseCache != null) {
            return this.icaseCache;
        }
        RangeToken rangeToken = this.type == 4 ? Token.createRange() : Token.createNRange();
        int n3 = 0;
        while (n3 < this.ranges.length) {
            int n4 = this.ranges[n3];
            while (n4 <= this.ranges[n3 + 1]) {
                if (n4 > 65535) {
                    rangeToken.addRange(n4, n4);
                } else {
                    n2 = Character.toUpperCase((char)n4);
                    rangeToken.addRange(n2, n2);
                }
                ++n4;
            }
            n3 += 2;
        }
        RangeToken rangeToken2 = this.type == 4 ? Token.createRange() : Token.createNRange();
        n2 = 0;
        while (n2 < rangeToken.ranges.length) {
            int n5 = rangeToken.ranges[n2];
            while (n5 <= rangeToken.ranges[n2 + 1]) {
                if (n5 > 65535) {
                    rangeToken2.addRange(n5, n5);
                } else {
                    char c2 = Character.toLowerCase((char)n5);
                    rangeToken2.addRange(c2, c2);
                }
                ++n5;
            }
            n2 += 2;
        }
        rangeToken2.mergeRanges(rangeToken);
        rangeToken2.mergeRanges(this);
        rangeToken2.compactRanges();
        this.icaseCache = rangeToken2;
        return rangeToken2;
    }

    void dumpRanges() {
        System.err.print("RANGE: ");
        if (this.ranges == null) {
            System.err.println(" NULL");
            return;
        }
        int n2 = 0;
        while (n2 < this.ranges.length) {
            System.err.print("[" + this.ranges[n2] + "," + this.ranges[n2 + 1] + "] ");
            n2 += 2;
        }
        System.err.println("");
    }

    boolean match(int n2) {
        boolean bl;
        if (this.map == null) {
            this.createMap();
        }
        if (this.type == 4) {
            if (n2 < 256) {
                return (this.map[n2 / 32] & 1 << (n2 & 31)) != 0;
            }
            bl = false;
            int n3 = this.nonMapIndex;
            while (n3 < this.ranges.length) {
                if (this.ranges[n3] <= n2 && n2 <= this.ranges[n3 + 1]) {
                    return true;
                }
                n3 += 2;
            }
        } else {
            if (n2 < 256) {
                return (this.map[n2 / 32] & 1 << (n2 & 31)) == 0;
            }
            bl = true;
            int n4 = this.nonMapIndex;
            while (n4 < this.ranges.length) {
                if (this.ranges[n4] <= n2 && n2 <= this.ranges[n4 + 1]) {
                    return false;
                }
                n4 += 2;
            }
        }
        return bl;
    }

    private void createMap() {
        int n2 = 8;
        int[] arrn = new int[n2];
        int n3 = this.ranges.length;
        int n4 = 0;
        while (n4 < n2) {
            arrn[n4] = 0;
            ++n4;
        }
        int n5 = 0;
        while (n5 < this.ranges.length) {
            int n6 = this.ranges[n5];
            int n7 = this.ranges[n5 + 1];
            if (n6 < 256) {
                int n8 = n6;
                while (n8 <= n7 && n8 < 256) {
                    int[] arrn2 = arrn;
                    int n9 = n8 / 32;
                    arrn2[n9] = arrn2[n9] | 1 << (n8 & 31);
                    ++n8;
                }
            } else {
                n3 = n5;
                break;
            }
            if (n7 >= 256) {
                n3 = n5;
                break;
            }
            n5 += 2;
        }
        this.map = arrn;
        this.nonMapIndex = n3;
    }

    public String toString(int n2) {
        String string;
        if (this.type == 4) {
            if (this == Token.token_dot) {
                string = ".";
            } else if (this == Token.token_0to9) {
                string = "\\d";
            } else if (this == Token.token_wordchars) {
                string = "\\w";
            } else if (this == Token.token_spaces) {
                string = "\\s";
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("[");
                int n3 = 0;
                while (n3 < this.ranges.length) {
                    if ((n2 & 1024) != 0 && n3 > 0) {
                        stringBuffer.append(",");
                    }
                    if (this.ranges[n3] == this.ranges[n3 + 1]) {
                        stringBuffer.append(RangeToken.escapeCharInCharClass(this.ranges[n3]));
                    } else {
                        stringBuffer.append(RangeToken.escapeCharInCharClass(this.ranges[n3]));
                        stringBuffer.append('-');
                        stringBuffer.append(RangeToken.escapeCharInCharClass(this.ranges[n3 + 1]));
                    }
                    n3 += 2;
                }
                stringBuffer.append("]");
                string = stringBuffer.toString();
            }
        } else if (this == Token.token_not_0to9) {
            string = "\\D";
        } else if (this == Token.token_not_wordchars) {
            string = "\\W";
        } else if (this == Token.token_not_spaces) {
            string = "\\S";
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[^");
            int n4 = 0;
            while (n4 < this.ranges.length) {
                if ((n2 & 1024) != 0 && n4 > 0) {
                    stringBuffer.append(",");
                }
                if (this.ranges[n4] == this.ranges[n4 + 1]) {
                    stringBuffer.append(RangeToken.escapeCharInCharClass(this.ranges[n4]));
                } else {
                    stringBuffer.append(RangeToken.escapeCharInCharClass(this.ranges[n4]));
                    stringBuffer.append('-');
                    stringBuffer.append(RangeToken.escapeCharInCharClass(this.ranges[n4 + 1]));
                }
                n4 += 2;
            }
            stringBuffer.append("]");
            string = stringBuffer.toString();
        }
        return string;
    }

    private static String escapeCharInCharClass(int n2) {
        String string;
        switch (n2) {
            case 44: 
            case 45: 
            case 91: 
            case 92: 
            case 93: 
            case 94: {
                string = "\\" + (char)n2;
                break;
            }
            case 12: {
                string = "\\f";
                break;
            }
            case 10: {
                string = "\\n";
                break;
            }
            case 13: {
                string = "\\r";
                break;
            }
            case 9: {
                string = "\\t";
                break;
            }
            case 27: {
                string = "\\e";
                break;
            }
            default: {
                if (n2 < 32) {
                    String string2 = "0" + Integer.toHexString(n2);
                    string = "\\x" + string2.substring(string2.length() - 2, string2.length());
                    break;
                }
                if (n2 >= 65536) {
                    String string3 = "0" + Integer.toHexString(n2);
                    string = "\\v" + string3.substring(string3.length() - 6, string3.length());
                    break;
                }
                string = "" + (char)n2;
            }
        }
        return string;
    }
}

