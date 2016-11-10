/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.impl.xpath.regex.REUtil;
import org.apache.xerces.impl.xpath.regex.RangeToken;

class Token
implements Serializable {
    private static final long serialVersionUID = 8484976002585487481L;
    static final boolean COUNTTOKENS = true;
    static int tokens = 0;
    static final int CHAR = 0;
    static final int DOT = 11;
    static final int CONCAT = 1;
    static final int UNION = 2;
    static final int CLOSURE = 3;
    static final int RANGE = 4;
    static final int NRANGE = 5;
    static final int PAREN = 6;
    static final int EMPTY = 7;
    static final int ANCHOR = 8;
    static final int NONGREEDYCLOSURE = 9;
    static final int STRING = 10;
    static final int BACKREFERENCE = 12;
    static final int LOOKAHEAD = 20;
    static final int NEGATIVELOOKAHEAD = 21;
    static final int LOOKBEHIND = 22;
    static final int NEGATIVELOOKBEHIND = 23;
    static final int INDEPENDENT = 24;
    static final int MODIFIERGROUP = 25;
    static final int CONDITION = 26;
    static final int UTF16_MAX = 1114111;
    final int type;
    static Token token_dot;
    static Token token_0to9;
    static Token token_wordchars;
    static Token token_not_0to9;
    static Token token_not_wordchars;
    static Token token_spaces;
    static Token token_not_spaces;
    static Token token_empty;
    static Token token_linebeginning;
    static Token token_linebeginning2;
    static Token token_lineend;
    static Token token_stringbeginning;
    static Token token_stringend;
    static Token token_stringend2;
    static Token token_wordedge;
    static Token token_not_wordedge;
    static Token token_wordbeginning;
    static Token token_wordend;
    static final int FC_CONTINUE = 0;
    static final int FC_TERMINAL = 1;
    static final int FC_ANY = 2;
    private static final Hashtable categories;
    private static final Hashtable categories2;
    private static final String[] categoryNames;
    static final int CHAR_INIT_QUOTE = 29;
    static final int CHAR_FINAL_QUOTE = 30;
    static final int CHAR_LETTER = 31;
    static final int CHAR_MARK = 32;
    static final int CHAR_NUMBER = 33;
    static final int CHAR_SEPARATOR = 34;
    static final int CHAR_OTHER = 35;
    static final int CHAR_PUNCTUATION = 36;
    static final int CHAR_SYMBOL = 37;
    private static final String[] blockNames;
    static final String blockRanges = "\u0000\u00ff\u0100\u017f\u0180\u024f\u0250\u02af\u02b0\u02ff\u0300\u036f\u0370\u03ff\u0400\u04ff\u0530\u058f\u0590\u05ff\u0600\u06ff\u0700\u074f\u0780\u07bf\u0900\u097f\u0980\u09ff\u0a00\u0a7f\u0a80\u0aff\u0b00\u0b7f\u0b80\u0bff\u0c00\u0c7f\u0c80\u0cff\u0d00\u0d7f\u0d80\u0dff\u0e00\u0e7f\u0e80\u0eff\u0f00\u0fff\u1000\u109f\u10a0\u10ff\u1100\u11ff\u1200\u137f\u13a0\u13ff\u1400\u167f\u1680\u169f\u16a0\u16ff\u1780\u17ff\u1800\u18af\u1e00\u1eff\u1f00\u1fff\u2000\u206f\u2070\u209f\u20a0\u20cf\u20d0\u20ff\u2100\u214f\u2150\u218f\u2190\u21ff\u2200\u22ff\u2300\u23ff\u2400\u243f\u2440\u245f\u2460\u24ff\u2500\u257f\u2580\u259f\u25a0\u25ff\u2600\u26ff\u2700\u27bf\u2800\u28ff\u2e80\u2eff\u2f00\u2fdf\u2ff0\u2fff\u3000\u303f\u3040\u309f\u30a0\u30ff\u3100\u312f\u3130\u318f\u3190\u319f\u31a0\u31bf\u3200\u32ff\u3300\u33ff\u3400\u4db5\u4e00\u9fff\ua000\ua48f\ua490\ua4cf\uac00\ud7a3\ue000\uf8ff\uf900\ufaff\ufb00\ufb4f\ufb50\ufdff\ufe20\ufe2f\ufe30\ufe4f\ufe50\ufe6f\ufe70\ufefe\ufeff\ufeff\uff00\uffef";
    static final int[] nonBMPBlockRanges;
    private static final int NONBMP_BLOCK_START = 84;
    static Hashtable nonxs;
    static final String viramaString = "\u094d\u09cd\u0a4d\u0acd\u0b4d\u0bcd\u0c4d\u0ccd\u0d4d\u0e3a\u0f84";
    private static Token token_grapheme;
    private static Token token_ccs;

    static ParenToken createLook(int n2, Token token) {
        ++tokens;
        return new ParenToken(n2, token, 0);
    }

    static ParenToken createParen(Token token, int n2) {
        ++tokens;
        return new ParenToken(6, token, n2);
    }

    static ClosureToken createClosure(Token token) {
        ++tokens;
        return new ClosureToken(3, token);
    }

    static ClosureToken createNGClosure(Token token) {
        ++tokens;
        return new ClosureToken(9, token);
    }

    static ConcatToken createConcat(Token token, Token token2) {
        ++tokens;
        return new ConcatToken(token, token2);
    }

    static UnionToken createConcat() {
        ++tokens;
        return new UnionToken(1);
    }

    static UnionToken createUnion() {
        ++tokens;
        return new UnionToken(2);
    }

    static Token createEmpty() {
        return token_empty;
    }

    static RangeToken createRange() {
        ++tokens;
        return new RangeToken(4);
    }

    static RangeToken createNRange() {
        ++tokens;
        return new RangeToken(5);
    }

    static CharToken createChar(int n2) {
        ++tokens;
        return new CharToken(0, n2);
    }

    private static CharToken createAnchor(int n2) {
        ++tokens;
        return new CharToken(8, n2);
    }

    static StringToken createBackReference(int n2) {
        ++tokens;
        return new StringToken(12, null, n2);
    }

    static StringToken createString(String string) {
        ++tokens;
        return new StringToken(10, string, 0);
    }

    static ModifierToken createModifierGroup(Token token, int n2, int n3) {
        ++tokens;
        return new ModifierToken(token, n2, n3);
    }

    static ConditionToken createCondition(int n2, Token token, Token token2, Token token3) {
        ++tokens;
        return new ConditionToken(n2, token, token2, token3);
    }

    protected Token(int n2) {
        this.type = n2;
    }

    int size() {
        return 0;
    }

    Token getChild(int n2) {
        return null;
    }

    void addChild(Token token) {
        throw new RuntimeException("Not supported.");
    }

    protected void addRange(int n2, int n3) {
        throw new RuntimeException("Not supported.");
    }

    protected void sortRanges() {
        throw new RuntimeException("Not supported.");
    }

    protected void compactRanges() {
        throw new RuntimeException("Not supported.");
    }

    protected void mergeRanges(Token token) {
        throw new RuntimeException("Not supported.");
    }

    protected void subtractRanges(Token token) {
        throw new RuntimeException("Not supported.");
    }

    protected void intersectRanges(Token token) {
        throw new RuntimeException("Not supported.");
    }

    static Token complementRanges(Token token) {
        return RangeToken.complementRanges(token);
    }

    void setMin(int n2) {
    }

    void setMax(int n2) {
    }

    int getMin() {
        return -1;
    }

    int getMax() {
        return -1;
    }

    int getReferenceNumber() {
        return 0;
    }

    String getString() {
        return null;
    }

    int getParenNumber() {
        return 0;
    }

    int getChar() {
        return -1;
    }

    public String toString() {
        return this.toString(0);
    }

    public String toString(int n2) {
        return this.type == 11 ? "." : "";
    }

    final int getMinLength() {
        switch (this.type) {
            case 1: {
                int n2 = 0;
                int n3 = 0;
                while (n3 < this.size()) {
                    n2 += this.getChild(n3).getMinLength();
                    ++n3;
                }
                return n2;
            }
            case 2: 
            case 26: {
                if (this.size() == 0) {
                    return 0;
                }
                int n4 = this.getChild(0).getMinLength();
                int n5 = 1;
                while (n5 < this.size()) {
                    int n6 = this.getChild(n5).getMinLength();
                    if (n6 < n4) {
                        n4 = n6;
                    }
                    ++n5;
                }
                return n4;
            }
            case 3: 
            case 9: {
                if (this.getMin() >= 0) {
                    return this.getMin() * this.getChild(0).getMinLength();
                }
                return 0;
            }
            case 7: 
            case 8: {
                return 0;
            }
            case 0: 
            case 4: 
            case 5: 
            case 11: {
                return 1;
            }
            case 6: 
            case 24: 
            case 25: {
                return this.getChild(0).getMinLength();
            }
            case 12: {
                return 0;
            }
            case 10: {
                return this.getString().length();
            }
            case 20: 
            case 21: 
            case 22: 
            case 23: {
                return 0;
            }
        }
        throw new RuntimeException("Token#getMinLength(): Invalid Type: " + this.type);
    }

    final int getMaxLength() {
        switch (this.type) {
            case 1: {
                int n2 = 0;
                int n3 = 0;
                while (n3 < this.size()) {
                    int n4 = this.getChild(n3).getMaxLength();
                    if (n4 < 0) {
                        return -1;
                    }
                    n2 += n4;
                    ++n3;
                }
                return n2;
            }
            case 2: 
            case 26: {
                if (this.size() == 0) {
                    return 0;
                }
                int n5 = this.getChild(0).getMaxLength();
                int n6 = 1;
                while (n5 >= 0 && n6 < this.size()) {
                    int n7 = this.getChild(n6).getMaxLength();
                    if (n7 < 0) {
                        n5 = -1;
                        break;
                    }
                    if (n7 > n5) {
                        n5 = n7;
                    }
                    ++n6;
                }
                return n5;
            }
            case 3: 
            case 9: {
                if (this.getMax() >= 0) {
                    return this.getMax() * this.getChild(0).getMaxLength();
                }
                return -1;
            }
            case 7: 
            case 8: {
                return 0;
            }
            case 0: {
                return 1;
            }
            case 4: 
            case 5: 
            case 11: {
                return 2;
            }
            case 6: 
            case 24: 
            case 25: {
                return this.getChild(0).getMaxLength();
            }
            case 12: {
                return -1;
            }
            case 10: {
                return this.getString().length();
            }
            case 20: 
            case 21: 
            case 22: 
            case 23: {
                return 0;
            }
        }
        throw new RuntimeException("Token#getMaxLength(): Invalid Type: " + this.type);
    }

    private static final boolean isSet(int n2, int n3) {
        return (n2 & n3) == n3;
    }

    final int analyzeFirstCharacter(RangeToken rangeToken, int n2) {
        switch (this.type) {
            case 1: {
                int n3 = 0;
                int n4 = 0;
                while (n4 < this.size()) {
                    n3 = this.getChild(n4).analyzeFirstCharacter(rangeToken, n2);
                    if (n3 != 0) break;
                    ++n4;
                }
                return n3;
            }
            case 2: {
                if (this.size() == 0) {
                    return 0;
                }
                int n5 = 0;
                boolean bl = false;
                int n6 = 0;
                while (n6 < this.size()) {
                    n5 = this.getChild(n6).analyzeFirstCharacter(rangeToken, n2);
                    if (n5 == 2) break;
                    if (n5 == 0) {
                        bl = true;
                    }
                    ++n6;
                }
                return bl ? 0 : n5;
            }
            case 26: {
                int n7 = this.getChild(0).analyzeFirstCharacter(rangeToken, n2);
                if (this.size() == 1) {
                    return 0;
                }
                if (n7 == 2) {
                    return n7;
                }
                int n8 = this.getChild(1).analyzeFirstCharacter(rangeToken, n2);
                if (n8 == 2) {
                    return n8;
                }
                return n7 == 0 || n8 == 0 ? 0 : 1;
            }
            case 3: 
            case 9: {
                this.getChild(0).analyzeFirstCharacter(rangeToken, n2);
                return 0;
            }
            case 7: 
            case 8: {
                return 0;
            }
            case 0: {
                int n9 = this.getChar();
                rangeToken.addRange(n9, n9);
                if (n9 < 65536 && Token.isSet(n2, 2)) {
                    n9 = Character.toUpperCase((char)n9);
                    rangeToken.addRange(n9, n9);
                    n9 = Character.toLowerCase((char)n9);
                    rangeToken.addRange(n9, n9);
                }
                return 1;
            }
            case 11: {
                return 2;
            }
            case 4: {
                rangeToken.mergeRanges(this);
                return 1;
            }
            case 5: {
                rangeToken.mergeRanges(Token.complementRanges(this));
                return 1;
            }
            case 6: 
            case 24: {
                return this.getChild(0).analyzeFirstCharacter(rangeToken, n2);
            }
            case 25: {
                n2 |= ((ModifierToken)this).getOptions();
                return this.getChild(0).analyzeFirstCharacter(rangeToken, n2 &= ~ ((ModifierToken)this).getOptionsMask());
            }
            case 12: {
                rangeToken.addRange(0, 1114111);
                return 2;
            }
            case 10: {
                char c2;
                int n10 = this.getString().charAt(0);
                if (REUtil.isHighSurrogate(n10) && this.getString().length() >= 2 && REUtil.isLowSurrogate(c2 = this.getString().charAt(1))) {
                    n10 = REUtil.composeFromSurrogates(n10, c2);
                }
                rangeToken.addRange(n10, n10);
                if (n10 < 65536 && Token.isSet(n2, 2)) {
                    n10 = Character.toUpperCase((char)n10);
                    rangeToken.addRange(n10, n10);
                    n10 = Character.toLowerCase((char)n10);
                    rangeToken.addRange(n10, n10);
                }
                return 1;
            }
            case 20: 
            case 21: 
            case 22: 
            case 23: {
                return 0;
            }
        }
        throw new RuntimeException("Token#analyzeHeadCharacter(): Invalid Type: " + this.type);
    }

    private final boolean isShorterThan(Token token) {
        if (token == null) {
            return false;
        }
        if (this.type != 10) {
            throw new RuntimeException("Internal Error: Illegal type: " + this.type);
        }
        int n2 = this.getString().length();
        if (token.type != 10) {
            throw new RuntimeException("Internal Error: Illegal type: " + token.type);
        }
        int n3 = token.getString().length();
        return n2 < n3;
    }

    final void findFixedString(FixedStringContainer fixedStringContainer, int n2) {
        switch (this.type) {
            case 1: {
                Token token = null;
                int n3 = 0;
                int n4 = 0;
                while (n4 < this.size()) {
                    this.getChild(n4).findFixedString(fixedStringContainer, n2);
                    if (token == null || token.isShorterThan(fixedStringContainer.token)) {
                        token = fixedStringContainer.token;
                        n3 = fixedStringContainer.options;
                    }
                    ++n4;
                }
                fixedStringContainer.token = token;
                fixedStringContainer.options = n3;
                return;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 7: 
            case 8: 
            case 9: 
            case 11: 
            case 12: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 26: {
                fixedStringContainer.token = null;
                return;
            }
            case 0: {
                fixedStringContainer.token = null;
                return;
            }
            case 10: {
                fixedStringContainer.token = this;
                fixedStringContainer.options = n2;
                return;
            }
            case 6: 
            case 24: {
                this.getChild(0).findFixedString(fixedStringContainer, n2);
                return;
            }
            case 25: {
                n2 |= ((ModifierToken)this).getOptions();
                this.getChild(0).findFixedString(fixedStringContainer, n2 &= ~ ((ModifierToken)this).getOptionsMask());
                return;
            }
        }
        throw new RuntimeException("Token#findFixedString(): Invalid Type: " + this.type);
    }

    boolean match(int n2) {
        throw new RuntimeException("NFAArrow#match(): Internal error: " + this.type);
    }

    protected static RangeToken getRange(String string, boolean bl) {
        if (categories.size() == 0) {
            Hashtable hashtable = categories;
            synchronized (hashtable) {
                Object c2;
                RangeToken rangeToken;
                Token[] arrtoken = new Token[categoryNames.length];
                int n2 = 0;
                while (n2 < arrtoken.length) {
                    arrtoken[n2] = Token.createRange();
                    ++n2;
                }
                int n3 = 0;
                while (n3 < 65536) {
                    int n4 = Character.getType((char)n3);
                    if (n4 == 21 || n4 == 22) {
                        if (n3 == 171 || n3 == 8216 || n3 == 8219 || n3 == 8220 || n3 == 8223 || n3 == 8249) {
                            n4 = 29;
                        }
                        if (n3 == 187 || n3 == 8217 || n3 == 8221 || n3 == 8250) {
                            n4 = 30;
                        }
                    }
                    arrtoken[n4].addRange(n3, n3);
                    switch (n4) {
                        case 1: 
                        case 2: 
                        case 3: 
                        case 4: 
                        case 5: {
                            n4 = 31;
                            break;
                        }
                        case 6: 
                        case 7: 
                        case 8: {
                            n4 = 32;
                            break;
                        }
                        case 9: 
                        case 10: 
                        case 11: {
                            n4 = 33;
                            break;
                        }
                        case 12: 
                        case 13: 
                        case 14: {
                            n4 = 34;
                            break;
                        }
                        case 0: 
                        case 15: 
                        case 16: 
                        case 18: 
                        case 19: {
                            n4 = 35;
                            break;
                        }
                        case 20: 
                        case 21: 
                        case 22: 
                        case 23: 
                        case 24: 
                        case 29: 
                        case 30: {
                            n4 = 36;
                            break;
                        }
                        case 25: 
                        case 26: 
                        case 27: 
                        case 28: {
                            n4 = 37;
                            break;
                        }
                        default: {
                            throw new RuntimeException("org.apache.xerces.utils.regex.Token#getRange(): Unknown Unicode category: " + n4);
                        }
                    }
                    arrtoken[n4].addRange(n3, n3);
                    ++n3;
                }
                arrtoken[0].addRange(65536, 1114111);
                int n5 = 0;
                while (n5 < arrtoken.length) {
                    if (categoryNames[n5] != null) {
                        if (n5 == 0) {
                            arrtoken[n5].addRange(65536, 1114111);
                        }
                        categories.put(categoryNames[n5], arrtoken[n5]);
                        categories2.put(categoryNames[n5], Token.complementRanges(arrtoken[n5]));
                    }
                    ++n5;
                }
                StringBuffer stringBuffer = new StringBuffer(50);
                int n6 = 0;
                while (n6 < blockNames.length) {
                    int rangeToken3;
                    int rangeToken2;
                    rangeToken = Token.createRange();
                    if (n6 < 84) {
                        rangeToken2 = n6 * 2;
                        char c3 = "\u0000\u00ff\u0100\u017f\u0180\u024f\u0250\u02af\u02b0\u02ff\u0300\u036f\u0370\u03ff\u0400\u04ff\u0530\u058f\u0590\u05ff\u0600\u06ff\u0700\u074f\u0780\u07bf\u0900\u097f\u0980\u09ff\u0a00\u0a7f\u0a80\u0aff\u0b00\u0b7f\u0b80\u0bff\u0c00\u0c7f\u0c80\u0cff\u0d00\u0d7f\u0d80\u0dff\u0e00\u0e7f\u0e80\u0eff\u0f00\u0fff\u1000\u109f\u10a0\u10ff\u1100\u11ff\u1200\u137f\u13a0\u13ff\u1400\u167f\u1680\u169f\u16a0\u16ff\u1780\u17ff\u1800\u18af\u1e00\u1eff\u1f00\u1fff\u2000\u206f\u2070\u209f\u20a0\u20cf\u20d0\u20ff\u2100\u214f\u2150\u218f\u2190\u21ff\u2200\u22ff\u2300\u23ff\u2400\u243f\u2440\u245f\u2460\u24ff\u2500\u257f\u2580\u259f\u25a0\u25ff\u2600\u26ff\u2700\u27bf\u2800\u28ff\u2e80\u2eff\u2f00\u2fdf\u2ff0\u2fff\u3000\u303f\u3040\u309f\u30a0\u30ff\u3100\u312f\u3130\u318f\u3190\u319f\u31a0\u31bf\u3200\u32ff\u3300\u33ff\u3400\u4db5\u4e00\u9fff\ua000\ua48f\ua490\ua4cf\uac00\ud7a3\ue000\uf8ff\uf900\ufaff\ufb00\ufb4f\ufb50\ufdff\ufe20\ufe2f\ufe30\ufe4f\ufe50\ufe6f\ufe70\ufefe\ufeff\ufeff\uff00\uffef".charAt(rangeToken2);
                        rangeToken3 = "\u0000\u00ff\u0100\u017f\u0180\u024f\u0250\u02af\u02b0\u02ff\u0300\u036f\u0370\u03ff\u0400\u04ff\u0530\u058f\u0590\u05ff\u0600\u06ff\u0700\u074f\u0780\u07bf\u0900\u097f\u0980\u09ff\u0a00\u0a7f\u0a80\u0aff\u0b00\u0b7f\u0b80\u0bff\u0c00\u0c7f\u0c80\u0cff\u0d00\u0d7f\u0d80\u0dff\u0e00\u0e7f\u0e80\u0eff\u0f00\u0fff\u1000\u109f\u10a0\u10ff\u1100\u11ff\u1200\u137f\u13a0\u13ff\u1400\u167f\u1680\u169f\u16a0\u16ff\u1780\u17ff\u1800\u18af\u1e00\u1eff\u1f00\u1fff\u2000\u206f\u2070\u209f\u20a0\u20cf\u20d0\u20ff\u2100\u214f\u2150\u218f\u2190\u21ff\u2200\u22ff\u2300\u23ff\u2400\u243f\u2440\u245f\u2460\u24ff\u2500\u257f\u2580\u259f\u25a0\u25ff\u2600\u26ff\u2700\u27bf\u2800\u28ff\u2e80\u2eff\u2f00\u2fdf\u2ff0\u2fff\u3000\u303f\u3040\u309f\u30a0\u30ff\u3100\u312f\u3130\u318f\u3190\u319f\u31a0\u31bf\u3200\u32ff\u3300\u33ff\u3400\u4db5\u4e00\u9fff\ua000\ua48f\ua490\ua4cf\uac00\ud7a3\ue000\uf8ff\uf900\ufaff\ufb00\ufb4f\ufb50\ufdff\ufe20\ufe2f\ufe30\ufe4f\ufe50\ufe6f\ufe70\ufefe\ufeff\ufeff\uff00\uffef".charAt(rangeToken2 + 1);
                        rangeToken.addRange(c3, rangeToken3);
                    } else {
                        rangeToken2 = (n6 - 84) * 2;
                        rangeToken.addRange(nonBMPBlockRanges[rangeToken2], nonBMPBlockRanges[rangeToken2 + 1]);
                    }
                    c2 = blockNames[n6];
                    if (c2.equals("Specials")) {
                        rangeToken.addRange(65520, 65533);
                    }
                    if (c2.equals("Private Use")) {
                        rangeToken.addRange(983040, 1048573);
                        rangeToken.addRange(1048576, 1114109);
                    }
                    categories.put(c2, rangeToken);
                    categories2.put(c2, Token.complementRanges(rangeToken));
                    stringBuffer.setLength(0);
                    stringBuffer.append("Is");
                    if (c2.indexOf(32) >= 0) {
                        rangeToken3 = 0;
                        while (rangeToken3 < c2.length()) {
                            if (c2.charAt(rangeToken3) != ' ') {
                                stringBuffer.append(c2.charAt(rangeToken3));
                            }
                            ++rangeToken3;
                        }
                    } else {
                        stringBuffer.append((String)c2);
                    }
                    Token.setAlias(stringBuffer.toString(), (String)c2, true);
                    ++n6;
                }
                Token.setAlias("ASSIGNED", "Cn", false);
                Token.setAlias("UNASSIGNED", "Cn", true);
                rangeToken = Token.createRange();
                rangeToken.addRange(0, 1114111);
                categories.put("ALL", rangeToken);
                categories2.put("ALL", Token.complementRanges(rangeToken));
                Token.registerNonXS("ASSIGNED");
                Token.registerNonXS("UNASSIGNED");
                Token.registerNonXS("ALL");
                RangeToken rangeToken2 = Token.createRange();
                rangeToken2.mergeRanges(arrtoken[1]);
                rangeToken2.mergeRanges(arrtoken[2]);
                rangeToken2.mergeRanges(arrtoken[5]);
                categories.put("IsAlpha", rangeToken2);
                categories2.put("IsAlpha", Token.complementRanges(rangeToken2));
                Token.registerNonXS("IsAlpha");
                c2 = Token.createRange();
                c2.mergeRanges(rangeToken2);
                c2.mergeRanges(arrtoken[9]);
                categories.put("IsAlnum", c2);
                categories2.put("IsAlnum", Token.complementRanges((Token)c2));
                Token.registerNonXS("IsAlnum");
                RangeToken rangeToken3 = Token.createRange();
                rangeToken3.mergeRanges(token_spaces);
                rangeToken3.mergeRanges(arrtoken[34]);
                categories.put("IsSpace", rangeToken3);
                categories2.put("IsSpace", Token.complementRanges(rangeToken3));
                Token.registerNonXS("IsSpace");
                RangeToken rangeToken4 = Token.createRange();
                rangeToken4.mergeRanges((Token)c2);
                rangeToken4.addRange(95, 95);
                categories.put("IsWord", rangeToken4);
                categories2.put("IsWord", Token.complementRanges(rangeToken4));
                Token.registerNonXS("IsWord");
                RangeToken rangeToken5 = Token.createRange();
                rangeToken5.addRange(0, 127);
                categories.put("IsASCII", rangeToken5);
                categories2.put("IsASCII", Token.complementRanges(rangeToken5));
                Token.registerNonXS("IsASCII");
                RangeToken rangeToken6 = Token.createRange();
                rangeToken6.mergeRanges(arrtoken[35]);
                rangeToken6.addRange(32, 32);
                categories.put("IsGraph", Token.complementRanges(rangeToken6));
                categories2.put("IsGraph", rangeToken6);
                Token.registerNonXS("IsGraph");
                RangeToken rangeToken7 = Token.createRange();
                rangeToken7.addRange(48, 57);
                rangeToken7.addRange(65, 70);
                rangeToken7.addRange(97, 102);
                categories.put("IsXDigit", Token.complementRanges(rangeToken7));
                categories2.put("IsXDigit", rangeToken7);
                Token.registerNonXS("IsXDigit");
                Token.setAlias("IsDigit", "Nd", true);
                Token.setAlias("IsUpper", "Lu", true);
                Token.setAlias("IsLower", "Ll", true);
                Token.setAlias("IsCntrl", "C", true);
                Token.setAlias("IsPrint", "C", false);
                Token.setAlias("IsPunct", "P", true);
                Token.registerNonXS("IsDigit");
                Token.registerNonXS("IsUpper");
                Token.registerNonXS("IsLower");
                Token.registerNonXS("IsCntrl");
                Token.registerNonXS("IsPrint");
                Token.registerNonXS("IsPunct");
                Token.setAlias("alpha", "IsAlpha", true);
                Token.setAlias("alnum", "IsAlnum", true);
                Token.setAlias("ascii", "IsASCII", true);
                Token.setAlias("cntrl", "IsCntrl", true);
                Token.setAlias("digit", "IsDigit", true);
                Token.setAlias("graph", "IsGraph", true);
                Token.setAlias("lower", "IsLower", true);
                Token.setAlias("print", "IsPrint", true);
                Token.setAlias("punct", "IsPunct", true);
                Token.setAlias("space", "IsSpace", true);
                Token.setAlias("upper", "IsUpper", true);
                Token.setAlias("word", "IsWord", true);
                Token.setAlias("xdigit", "IsXDigit", true);
                Token.registerNonXS("alpha");
                Token.registerNonXS("alnum");
                Token.registerNonXS("ascii");
                Token.registerNonXS("cntrl");
                Token.registerNonXS("digit");
                Token.registerNonXS("graph");
                Token.registerNonXS("lower");
                Token.registerNonXS("print");
                Token.registerNonXS("punct");
                Token.registerNonXS("space");
                Token.registerNonXS("upper");
                Token.registerNonXS("word");
                Token.registerNonXS("xdigit");
            }
        }
        RangeToken rangeToken = bl ? (RangeToken)categories.get(string) : (RangeToken)categories2.get(string);
        return rangeToken;
    }

    protected static RangeToken getRange(String string, boolean bl, boolean bl2) {
        RangeToken rangeToken = Token.getRange(string, bl);
        if (bl2 && rangeToken != null && Token.isRegisterNonXS(string)) {
            rangeToken = null;
        }
        return rangeToken;
    }

    protected static void registerNonXS(String string) {
        if (nonxs == null) {
            nonxs = new Hashtable();
        }
        nonxs.put(string, string);
    }

    protected static boolean isRegisterNonXS(String string) {
        if (nonxs == null) {
            return false;
        }
        return nonxs.containsKey(string);
    }

    private static void setAlias(String string, String string2, boolean bl) {
        Token token = (Token)categories.get(string2);
        Token token2 = (Token)categories2.get(string2);
        if (bl) {
            categories.put(string, token);
            categories2.put(string, token2);
        } else {
            categories2.put(string, token);
            categories.put(string, token2);
        }
    }

    static synchronized Token getGraphemePattern() {
        if (token_grapheme != null) {
            return token_grapheme;
        }
        RangeToken rangeToken = Token.createRange();
        rangeToken.mergeRanges(Token.getRange("ASSIGNED", true));
        rangeToken.subtractRanges(Token.getRange("M", true));
        rangeToken.subtractRanges(Token.getRange("C", true));
        RangeToken rangeToken2 = Token.createRange();
        int n2 = 0;
        while (n2 < "\u094d\u09cd\u0a4d\u0acd\u0b4d\u0bcd\u0c4d\u0ccd\u0d4d\u0e3a\u0f84".length()) {
            rangeToken2.addRange(n2, n2);
            ++n2;
        }
        RangeToken rangeToken3 = Token.createRange();
        rangeToken3.mergeRanges(Token.getRange("M", true));
        rangeToken3.addRange(4448, 4607);
        rangeToken3.addRange(65438, 65439);
        UnionToken unionToken = Token.createUnion();
        unionToken.addChild(rangeToken);
        unionToken.addChild(token_empty);
        Token token = Token.createUnion();
        token.addChild(Token.createConcat(rangeToken2, Token.getRange("L", true)));
        token.addChild(rangeToken3);
        token = Token.createClosure(token);
        token = Token.createConcat(unionToken, token);
        token_grapheme = token;
        return token_grapheme;
    }

    static synchronized Token getCombiningCharacterSequence() {
        if (token_ccs != null) {
            return token_ccs;
        }
        Token token = Token.createClosure(Token.getRange("M", true));
        token = Token.createConcat(Token.getRange("M", false), token);
        token_ccs = token;
        return token_ccs;
    }

    static {
        token_empty = new Token(7);
        token_linebeginning = Token.createAnchor(94);
        token_linebeginning2 = Token.createAnchor(64);
        token_lineend = Token.createAnchor(36);
        token_stringbeginning = Token.createAnchor(65);
        token_stringend = Token.createAnchor(122);
        token_stringend2 = Token.createAnchor(90);
        token_wordedge = Token.createAnchor(98);
        token_not_wordedge = Token.createAnchor(66);
        token_wordbeginning = Token.createAnchor(60);
        token_wordend = Token.createAnchor(62);
        token_dot = new Token(11);
        token_0to9 = Token.createRange();
        token_0to9.addRange(48, 57);
        token_wordchars = Token.createRange();
        token_wordchars.addRange(48, 57);
        token_wordchars.addRange(65, 90);
        token_wordchars.addRange(95, 95);
        token_wordchars.addRange(97, 122);
        token_spaces = Token.createRange();
        token_spaces.addRange(9, 9);
        token_spaces.addRange(10, 10);
        token_spaces.addRange(12, 12);
        token_spaces.addRange(13, 13);
        token_spaces.addRange(32, 32);
        token_not_0to9 = Token.complementRanges(token_0to9);
        token_not_wordchars = Token.complementRanges(token_wordchars);
        token_not_spaces = Token.complementRanges(token_spaces);
        categories = new Hashtable();
        categories2 = new Hashtable();
        categoryNames = new String[]{"Cn", "Lu", "Ll", "Lt", "Lm", "Lo", "Mn", "Me", "Mc", "Nd", "Nl", "No", "Zs", "Zl", "Zp", "Cc", "Cf", null, "Co", "Cs", "Pd", "Ps", "Pe", "Pc", "Po", "Sm", "Sc", "Sk", "So", "Pi", "Pf", "L", "M", "N", "Z", "C", "P", "S"};
        blockNames = new String[]{"Basic Latin", "Latin-1 Supplement", "Latin Extended-A", "Latin Extended-B", "IPA Extensions", "Spacing Modifier Letters", "Combining Diacritical Marks", "Greek", "Cyrillic", "Armenian", "Hebrew", "Arabic", "Syriac", "Thaana", "Devanagari", "Bengali", "Gurmukhi", "Gujarati", "Oriya", "Tamil", "Telugu", "Kannada", "Malayalam", "Sinhala", "Thai", "Lao", "Tibetan", "Myanmar", "Georgian", "Hangul Jamo", "Ethiopic", "Cherokee", "Unified Canadian Aboriginal Syllabics", "Ogham", "Runic", "Khmer", "Mongolian", "Latin Extended Additional", "Greek Extended", "General Punctuation", "Superscripts and Subscripts", "Currency Symbols", "Combining Marks for Symbols", "Letterlike Symbols", "Number Forms", "Arrows", "Mathematical Operators", "Miscellaneous Technical", "Control Pictures", "Optical Character Recognition", "Enclosed Alphanumerics", "Box Drawing", "Block Elements", "Geometric Shapes", "Miscellaneous Symbols", "Dingbats", "Braille Patterns", "CJK Radicals Supplement", "Kangxi Radicals", "Ideographic Description Characters", "CJK Symbols and Punctuation", "Hiragana", "Katakana", "Bopomofo", "Hangul Compatibility Jamo", "Kanbun", "Bopomofo Extended", "Enclosed CJK Letters and Months", "CJK Compatibility", "CJK Unified Ideographs Extension A", "CJK Unified Ideographs", "Yi Syllables", "Yi Radicals", "Hangul Syllables", "Private Use", "CJK Compatibility Ideographs", "Alphabetic Presentation Forms", "Arabic Presentation Forms-A", "Combining Half Marks", "CJK Compatibility Forms", "Small Form Variants", "Arabic Presentation Forms-B", "Specials", "Halfwidth and Fullwidth Forms", "Old Italic", "Gothic", "Deseret", "Byzantine Musical Symbols", "Musical Symbols", "Mathematical Alphanumeric Symbols", "CJK Unified Ideographs Extension B", "CJK Compatibility Ideographs Supplement", "Tags"};
        nonBMPBlockRanges = new int[]{66304, 66351, 66352, 66383, 66560, 66639, 118784, 119039, 119040, 119295, 119808, 120831, 131072, 173782, 194560, 195103, 917504, 917631};
        nonxs = null;
        token_grapheme = null;
        token_ccs = null;
    }

    static class UnionToken
    extends Token
    implements Serializable {
        private static final long serialVersionUID = -2568843945989489861L;
        Vector children;

        UnionToken(int n2) {
            super(n2);
        }

        void addChild(Token token) {
            int n2;
            StringBuffer stringBuffer;
            int n3;
            if (token == null) {
                return;
            }
            if (this.children == null) {
                this.children = new Vector();
            }
            if (this.type == 2) {
                this.children.addElement(token);
                return;
            }
            if (token.type == 1) {
                int n4 = 0;
                while (n4 < token.size()) {
                    this.addChild(token.getChild(n4));
                    ++n4;
                }
                return;
            }
            int n5 = this.children.size();
            if (n5 == 0) {
                this.children.addElement(token);
                return;
            }
            Token token2 = (Token)this.children.elementAt(n5 - 1);
            if (token2.type != 0 && token2.type != 10 || token.type != 0 && token.type != 10) {
                this.children.addElement(token);
                return;
            }
            int n6 = n3 = token.type == 0 ? 2 : token.getString().length();
            if (token2.type == 0) {
                stringBuffer = new StringBuffer(2 + n3);
                n2 = token2.getChar();
                if (n2 >= 65536) {
                    stringBuffer.append(REUtil.decomposeToSurrogates(n2));
                } else {
                    stringBuffer.append((char)n2);
                }
                token2 = Token.createString(null);
                this.children.setElementAt(token2, n5 - 1);
            } else {
                stringBuffer = new StringBuffer(token2.getString().length() + n3);
                stringBuffer.append(token2.getString());
            }
            if (token.type == 0) {
                n2 = token.getChar();
                if (n2 >= 65536) {
                    stringBuffer.append(REUtil.decomposeToSurrogates(n2));
                } else {
                    stringBuffer.append((char)n2);
                }
            } else {
                stringBuffer.append(token.getString());
            }
            ((StringToken)token2).string = new String(stringBuffer);
        }

        int size() {
            return this.children == null ? 0 : this.children.size();
        }

        Token getChild(int n2) {
            return (Token)this.children.elementAt(n2);
        }

        public String toString(int n2) {
            String string;
            if (this.type == 1) {
                String string2;
                if (this.children.size() == 2) {
                    Token token = this.getChild(0);
                    Token token2 = this.getChild(1);
                    string2 = token2.type == 3 && token2.getChild(0) == token ? token.toString(n2) + "+" : (token2.type == 9 && token2.getChild(0) == token ? token.toString(n2) + "+?" : token.toString(n2) + token2.toString(n2));
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    int n3 = 0;
                    while (n3 < this.children.size()) {
                        stringBuffer.append(((Token)this.children.elementAt(n3)).toString(n2));
                        ++n3;
                    }
                    string2 = new String(stringBuffer);
                }
                return string2;
            }
            if (this.children.size() == 2 && this.getChild((int)1).type == 7) {
                string = this.getChild(0).toString(n2) + "?";
            } else if (this.children.size() == 2 && this.getChild((int)0).type == 7) {
                string = this.getChild(1).toString(n2) + "??";
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(((Token)this.children.elementAt(0)).toString(n2));
                int n4 = 1;
                while (n4 < this.children.size()) {
                    stringBuffer.append('|');
                    stringBuffer.append(((Token)this.children.elementAt(n4)).toString(n2));
                    ++n4;
                }
                string = new String(stringBuffer);
            }
            return string;
        }
    }

    static class ModifierToken
    extends Token
    implements Serializable {
        private static final long serialVersionUID = -9114536559696480356L;
        final Token child;
        final int add;
        final int mask;

        ModifierToken(Token token, int n2, int n3) {
            super(25);
            this.child = token;
            this.add = n2;
            this.mask = n3;
        }

        int size() {
            return 1;
        }

        Token getChild(int n2) {
            return this.child;
        }

        int getOptions() {
            return this.add;
        }

        int getOptionsMask() {
            return this.mask;
        }

        public String toString(int n2) {
            return "(?" + (this.add == 0 ? "" : REUtil.createOptionString(this.add)) + (this.mask == 0 ? "" : REUtil.createOptionString(this.mask)) + ":" + this.child.toString(n2) + ")";
        }
    }

    static class ConditionToken
    extends Token
    implements Serializable {
        private static final long serialVersionUID = 4353765277910594411L;
        final int refNumber;
        final Token condition;
        final Token yes;
        final Token no;

        ConditionToken(int n2, Token token, Token token2, Token token3) {
            super(26);
            this.refNumber = n2;
            this.condition = token;
            this.yes = token2;
            this.no = token3;
        }

        int size() {
            return this.no == null ? 1 : 2;
        }

        Token getChild(int n2) {
            if (n2 == 0) {
                return this.yes;
            }
            if (n2 == 1) {
                return this.no;
            }
            throw new RuntimeException("Internal Error: " + n2);
        }

        public String toString(int n2) {
            String string = this.refNumber > 0 ? "(?(" + this.refNumber + ")" : (this.condition.type == 8 ? "(?(" + this.condition + ")" : "(?" + this.condition);
            string = this.no == null ? string + this.yes + ")" : string + this.yes + "|" + this.no + ")";
            return string;
        }
    }

    static class ParenToken
    extends Token
    implements Serializable {
        private static final long serialVersionUID = -5938014719827987704L;
        final Token child;
        final int parennumber;

        ParenToken(int n2, Token token, int n3) {
            super(n2);
            this.child = token;
            this.parennumber = n3;
        }

        int size() {
            return 1;
        }

        Token getChild(int n2) {
            return this.child;
        }

        int getParenNumber() {
            return this.parennumber;
        }

        public String toString(int n2) {
            String string = null;
            switch (this.type) {
                case 6: {
                    if (this.parennumber == 0) {
                        string = "(?:" + this.child.toString(n2) + ")";
                        break;
                    }
                    string = "(" + this.child.toString(n2) + ")";
                    break;
                }
                case 20: {
                    string = "(?=" + this.child.toString(n2) + ")";
                    break;
                }
                case 21: {
                    string = "(?!" + this.child.toString(n2) + ")";
                    break;
                }
                case 22: {
                    string = "(?<=" + this.child.toString(n2) + ")";
                    break;
                }
                case 23: {
                    string = "(?<!" + this.child.toString(n2) + ")";
                    break;
                }
                case 24: {
                    string = "(?>" + this.child.toString(n2) + ")";
                }
            }
            return string;
        }
    }

    static class ClosureToken
    extends Token
    implements Serializable {
        private static final long serialVersionUID = 1308971930673997452L;
        int min;
        int max;
        final Token child;

        ClosureToken(int n2, Token token) {
            super(n2);
            this.child = token;
            this.setMin(-1);
            this.setMax(-1);
        }

        int size() {
            return 1;
        }

        Token getChild(int n2) {
            return this.child;
        }

        final void setMin(int n2) {
            this.min = n2;
        }

        final void setMax(int n2) {
            this.max = n2;
        }

        final int getMin() {
            return this.min;
        }

        final int getMax() {
            return this.max;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public String toString(int n2) {
            if (this.type == 3) {
                if (this.getMin() < 0 && this.getMax() < 0) {
                    return this.child.toString(n2) + "*";
                }
                if (this.getMin() == this.getMax()) {
                    return this.child.toString(n2) + "{" + this.getMin() + "}";
                }
                if (this.getMin() >= 0 && this.getMax() >= 0) {
                    return this.child.toString(n2) + "{" + this.getMin() + "," + this.getMax() + "}";
                }
                if (this.getMin() < 0) throw new RuntimeException("Token#toString(): CLOSURE " + this.getMin() + ", " + this.getMax());
                if (this.getMax() >= 0) throw new RuntimeException("Token#toString(): CLOSURE " + this.getMin() + ", " + this.getMax());
                return this.child.toString(n2) + "{" + this.getMin() + ",}";
            }
            if (this.getMin() < 0 && this.getMax() < 0) {
                return this.child.toString(n2) + "*?";
            }
            if (this.getMin() == this.getMax()) {
                return this.child.toString(n2) + "{" + this.getMin() + "}?";
            }
            if (this.getMin() >= 0 && this.getMax() >= 0) {
                return this.child.toString(n2) + "{" + this.getMin() + "," + this.getMax() + "}?";
            }
            if (this.getMin() < 0) throw new RuntimeException("Token#toString(): NONGREEDYCLOSURE " + this.getMin() + ", " + this.getMax());
            if (this.getMax() >= 0) throw new RuntimeException("Token#toString(): NONGREEDYCLOSURE " + this.getMin() + ", " + this.getMax());
            return this.child.toString(n2) + "{" + this.getMin() + ",}?";
        }
    }

    static class CharToken
    extends Token
    implements Serializable {
        private static final long serialVersionUID = -4394272816279496989L;
        final int chardata;

        CharToken(int n2, int n3) {
            super(n2);
            this.chardata = n3;
        }

        int getChar() {
            return this.chardata;
        }

        public String toString(int n2) {
            String string;
            block0 : switch (this.type) {
                case 0: {
                    switch (this.chardata) {
                        case 40: 
                        case 41: 
                        case 42: 
                        case 43: 
                        case 46: 
                        case 63: 
                        case 91: 
                        case 92: 
                        case 123: 
                        case 124: {
                            string = "\\" + (char)this.chardata;
                            break block0;
                        }
                        case 12: {
                            string = "\\f";
                            break block0;
                        }
                        case 10: {
                            string = "\\n";
                            break block0;
                        }
                        case 13: {
                            string = "\\r";
                            break block0;
                        }
                        case 9: {
                            string = "\\t";
                            break block0;
                        }
                        case 27: {
                            string = "\\e";
                            break block0;
                        }
                    }
                    if (this.chardata >= 65536) {
                        String string2 = "0" + Integer.toHexString(this.chardata);
                        string = "\\v" + string2.substring(string2.length() - 6, string2.length());
                        break;
                    }
                    string = "" + (char)this.chardata;
                    break;
                }
                case 8: {
                    if (this == Token.token_linebeginning || this == Token.token_lineend) {
                        string = "" + (char)this.chardata;
                        break;
                    }
                    string = "\\" + (char)this.chardata;
                    break;
                }
                default: {
                    string = null;
                }
            }
            return string;
        }

        boolean match(int n2) {
            if (this.type == 0) {
                return n2 == this.chardata;
            }
            throw new RuntimeException("NFAArrow#match(): Internal error: " + this.type);
        }
    }

    static class ConcatToken
    extends Token
    implements Serializable {
        private static final long serialVersionUID = 8717321425541346381L;
        final Token child;
        final Token child2;

        ConcatToken(Token token, Token token2) {
            super(1);
            this.child = token;
            this.child2 = token2;
        }

        int size() {
            return 2;
        }

        Token getChild(int n2) {
            return n2 == 0 ? this.child : this.child2;
        }

        public String toString(int n2) {
            String string = this.child2.type == 3 && this.child2.getChild(0) == this.child ? this.child.toString(n2) + "+" : (this.child2.type == 9 && this.child2.getChild(0) == this.child ? this.child.toString(n2) + "+?" : this.child.toString(n2) + this.child2.toString(n2));
            return string;
        }
    }

    static class StringToken
    extends Token
    implements Serializable {
        private static final long serialVersionUID = -4614366944218504172L;
        String string;
        final int refNumber;

        StringToken(int n2, String string, int n3) {
            super(n2);
            this.string = string;
            this.refNumber = n3;
        }

        int getReferenceNumber() {
            return this.refNumber;
        }

        String getString() {
            return this.string;
        }

        public String toString(int n2) {
            if (this.type == 12) {
                return "\\" + this.refNumber;
            }
            return REUtil.quoteMeta(this.string);
        }
    }

    static class FixedStringContainer {
        Token token = null;
        int options = 0;

        FixedStringContainer() {
        }
    }

}

