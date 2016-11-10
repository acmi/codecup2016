/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.xerces.impl.xpath.regex.CaseInsensitiveMap;
import org.apache.xerces.impl.xpath.regex.ParseException;
import org.apache.xerces.impl.xpath.regex.REUtil;
import org.apache.xerces.impl.xpath.regex.RangeToken;
import org.apache.xerces.impl.xpath.regex.Token;

class RegexParser {
    static final int T_CHAR = 0;
    static final int T_EOF = 1;
    static final int T_OR = 2;
    static final int T_STAR = 3;
    static final int T_PLUS = 4;
    static final int T_QUESTION = 5;
    static final int T_LPAREN = 6;
    static final int T_RPAREN = 7;
    static final int T_DOT = 8;
    static final int T_LBRACKET = 9;
    static final int T_BACKSOLIDUS = 10;
    static final int T_CARET = 11;
    static final int T_DOLLAR = 12;
    static final int T_LPAREN2 = 13;
    static final int T_LOOKAHEAD = 14;
    static final int T_NEGATIVELOOKAHEAD = 15;
    static final int T_LOOKBEHIND = 16;
    static final int T_NEGATIVELOOKBEHIND = 17;
    static final int T_INDEPENDENT = 18;
    static final int T_SET_OPERATIONS = 19;
    static final int T_POSIX_CHARCLASS_START = 20;
    static final int T_COMMENT = 21;
    static final int T_MODIFIERS = 22;
    static final int T_CONDITION = 23;
    static final int T_XMLSCHEMA_CC_SUBTRACTION = 24;
    int offset;
    String regex;
    int regexlen;
    int options;
    ResourceBundle resources;
    int chardata;
    int nexttoken;
    protected static final int S_NORMAL = 0;
    protected static final int S_INBRACKETS = 1;
    protected static final int S_INXBRACKETS = 2;
    int context = 0;
    int parenOpened = 1;
    int parennumber = 1;
    boolean hasBackReferences;
    Vector references = null;

    public RegexParser() {
        this.setLocale(Locale.getDefault());
    }

    public RegexParser(Locale locale) {
        this.setLocale(locale);
    }

    public void setLocale(Locale locale) {
        try {
            this.resources = locale != null ? ResourceBundle.getBundle("org.apache.xerces.impl.xpath.regex.message", locale) : ResourceBundle.getBundle("org.apache.xerces.impl.xpath.regex.message");
        }
        catch (MissingResourceException missingResourceException) {
            throw new RuntimeException("Installation Problem???  Couldn't load messages: " + missingResourceException.getMessage());
        }
    }

    final ParseException ex(String string, int n2) {
        return new ParseException(this.resources.getString(string), n2);
    }

    protected final boolean isSet(int n2) {
        return (this.options & n2) == n2;
    }

    synchronized Token parse(String string, int n2) throws ParseException {
        this.options = n2;
        this.offset = 0;
        this.setContext(0);
        this.parennumber = 1;
        this.parenOpened = 1;
        this.hasBackReferences = false;
        this.regex = string;
        if (this.isSet(16)) {
            this.regex = REUtil.stripExtendedComment(this.regex);
        }
        this.regexlen = this.regex.length();
        this.next();
        Token token = this.parseRegex();
        if (this.offset != this.regexlen) {
            throw this.ex("parser.parse.1", this.offset);
        }
        if (this.references != null) {
            int n3 = 0;
            while (n3 < this.references.size()) {
                ReferencePosition referencePosition = (ReferencePosition)this.references.elementAt(n3);
                if (this.parennumber <= referencePosition.refNumber) {
                    throw this.ex("parser.parse.2", referencePosition.position);
                }
                ++n3;
            }
            this.references.removeAllElements();
        }
        return token;
    }

    protected final void setContext(int n2) {
        this.context = n2;
    }

    final int read() {
        return this.nexttoken;
    }

    final void next() {
        int n2;
        if (this.offset >= this.regexlen) {
            this.chardata = -1;
            this.nexttoken = 1;
            return;
        }
        char c2 = this.regex.charAt(this.offset++);
        this.chardata = c2;
        if (this.context == 1) {
            int n3;
            switch (c2) {
                case '\\': {
                    n3 = 10;
                    if (this.offset >= this.regexlen) {
                        throw this.ex("parser.next.1", this.offset - 1);
                    }
                    this.chardata = this.regex.charAt(this.offset++);
                    break;
                }
                case '-': {
                    if (this.offset < this.regexlen && this.regex.charAt(this.offset) == '[') {
                        ++this.offset;
                        n3 = 24;
                        break;
                    }
                    n3 = 0;
                    break;
                }
                case '[': {
                    if (!this.isSet(512) && this.offset < this.regexlen && this.regex.charAt(this.offset) == ':') {
                        ++this.offset;
                        n3 = 20;
                        break;
                    }
                }
                default: {
                    char c3;
                    if (REUtil.isHighSurrogate(c2) && this.offset < this.regexlen && REUtil.isLowSurrogate(c3 = this.regex.charAt(this.offset))) {
                        this.chardata = REUtil.composeFromSurrogates(c2, c3);
                        ++this.offset;
                    }
                    n3 = 0;
                }
            }
            this.nexttoken = n3;
            return;
        }
        block5 : switch (c2) {
            case '|': {
                n2 = 2;
                break;
            }
            case '*': {
                n2 = 3;
                break;
            }
            case '+': {
                n2 = 4;
                break;
            }
            case '?': {
                n2 = 5;
                break;
            }
            case ')': {
                n2 = 7;
                break;
            }
            case '.': {
                n2 = 8;
                break;
            }
            case '[': {
                n2 = 9;
                break;
            }
            case '^': {
                if (this.isSet(512)) {
                    n2 = 0;
                    break;
                }
                n2 = 11;
                break;
            }
            case '$': {
                if (this.isSet(512)) {
                    n2 = 0;
                    break;
                }
                n2 = 12;
                break;
            }
            case '(': {
                n2 = 6;
                if (this.offset >= this.regexlen || this.regex.charAt(this.offset) != '?') break;
                if (++this.offset >= this.regexlen) {
                    throw this.ex("parser.next.2", this.offset - 1);
                }
                c2 = this.regex.charAt(this.offset++);
                switch (c2) {
                    case ':': {
                        n2 = 13;
                        break block5;
                    }
                    case '=': {
                        n2 = 14;
                        break block5;
                    }
                    case '!': {
                        n2 = 15;
                        break block5;
                    }
                    case '[': {
                        n2 = 19;
                        break block5;
                    }
                    case '>': {
                        n2 = 18;
                        break block5;
                    }
                    case '<': {
                        if (this.offset >= this.regexlen) {
                            throw this.ex("parser.next.2", this.offset - 3);
                        }
                        if ((c2 = this.regex.charAt(this.offset++)) == '=') {
                            n2 = 16;
                            break block5;
                        }
                        if (c2 == '!') {
                            n2 = 17;
                            break block5;
                        }
                        throw this.ex("parser.next.3", this.offset - 3);
                    }
                    case '#': {
                        while (this.offset < this.regexlen) {
                            if ((c2 = this.regex.charAt(this.offset++)) == ')') break;
                        }
                        if (c2 != ')') {
                            throw this.ex("parser.next.4", this.offset - 1);
                        }
                        n2 = 21;
                        break block5;
                    }
                }
                if (c2 == '-' || 'a' <= c2 && c2 <= 'z' || 'A' <= c2 && c2 <= 'Z') {
                    --this.offset;
                    n2 = 22;
                    break;
                }
                if (c2 == '(') {
                    n2 = 23;
                    break;
                }
                throw this.ex("parser.next.2", this.offset - 2);
            }
            case '\\': {
                n2 = 10;
                if (this.offset >= this.regexlen) {
                    throw this.ex("parser.next.1", this.offset - 1);
                }
                this.chardata = this.regex.charAt(this.offset++);
                break;
            }
            default: {
                n2 = 0;
            }
        }
        this.nexttoken = n2;
    }

    Token parseRegex() throws ParseException {
        Token token = this.parseTerm();
        Token.UnionToken unionToken = null;
        while (this.read() == 2) {
            this.next();
            if (unionToken == null) {
                unionToken = Token.createUnion();
                unionToken.addChild(token);
                token = unionToken;
            }
            token.addChild(this.parseTerm());
        }
        return token;
    }

    Token parseTerm() throws ParseException {
        int n2 = this.read();
        if (n2 == 2 || n2 == 7 || n2 == 1) {
            return Token.createEmpty();
        }
        Token token = this.parseFactor();
        Token.UnionToken unionToken = null;
        while ((n2 = this.read()) != 2 && n2 != 7 && n2 != 1) {
            if (unionToken == null) {
                unionToken = Token.createConcat();
                unionToken.addChild(token);
                token = unionToken;
            }
            unionToken.addChild(this.parseFactor());
        }
        return token;
    }

    Token processCaret() throws ParseException {
        this.next();
        return Token.token_linebeginning;
    }

    Token processDollar() throws ParseException {
        this.next();
        return Token.token_lineend;
    }

    Token processLookahead() throws ParseException {
        this.next();
        Token.ParenToken parenToken = Token.createLook(20, this.parseRegex());
        if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
        }
        this.next();
        return parenToken;
    }

    Token processNegativelookahead() throws ParseException {
        this.next();
        Token.ParenToken parenToken = Token.createLook(21, this.parseRegex());
        if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
        }
        this.next();
        return parenToken;
    }

    Token processLookbehind() throws ParseException {
        this.next();
        Token.ParenToken parenToken = Token.createLook(22, this.parseRegex());
        if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
        }
        this.next();
        return parenToken;
    }

    Token processNegativelookbehind() throws ParseException {
        this.next();
        Token.ParenToken parenToken = Token.createLook(23, this.parseRegex());
        if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
        }
        this.next();
        return parenToken;
    }

    Token processBacksolidus_A() throws ParseException {
        this.next();
        return Token.token_stringbeginning;
    }

    Token processBacksolidus_Z() throws ParseException {
        this.next();
        return Token.token_stringend2;
    }

    Token processBacksolidus_z() throws ParseException {
        this.next();
        return Token.token_stringend;
    }

    Token processBacksolidus_b() throws ParseException {
        this.next();
        return Token.token_wordedge;
    }

    Token processBacksolidus_B() throws ParseException {
        this.next();
        return Token.token_not_wordedge;
    }

    Token processBacksolidus_lt() throws ParseException {
        this.next();
        return Token.token_wordbeginning;
    }

    Token processBacksolidus_gt() throws ParseException {
        this.next();
        return Token.token_wordend;
    }

    Token processStar(Token token) throws ParseException {
        this.next();
        if (this.read() == 5) {
            this.next();
            return Token.createNGClosure(token);
        }
        return Token.createClosure(token);
    }

    Token processPlus(Token token) throws ParseException {
        this.next();
        if (this.read() == 5) {
            this.next();
            return Token.createConcat(token, Token.createNGClosure(token));
        }
        return Token.createConcat(token, Token.createClosure(token));
    }

    Token processQuestion(Token token) throws ParseException {
        this.next();
        Token.UnionToken unionToken = Token.createUnion();
        if (this.read() == 5) {
            this.next();
            unionToken.addChild(Token.createEmpty());
            unionToken.addChild(token);
        } else {
            unionToken.addChild(token);
            unionToken.addChild(Token.createEmpty());
        }
        return unionToken;
    }

    boolean checkQuestion(int n2) {
        return n2 < this.regexlen && this.regex.charAt(n2) == '?';
    }

    Token processParen() throws ParseException {
        this.next();
        int n2 = this.parenOpened++;
        Token.ParenToken parenToken = Token.createParen(this.parseRegex(), n2);
        if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
        }
        ++this.parennumber;
        this.next();
        return parenToken;
    }

    Token processParen2() throws ParseException {
        this.next();
        Token.ParenToken parenToken = Token.createParen(this.parseRegex(), 0);
        if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
        }
        this.next();
        return parenToken;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    Token processCondition() throws ParseException {
        if (this.offset + 1 >= this.regexlen) {
            throw this.ex("parser.factor.4", this.offset);
        }
        var1_1 = -1;
        var2_2 = null;
        var3_3 = this.regex.charAt(this.offset);
        if ('1' > var3_3 || var3_3 > '9') ** GOTO lbl11
        var4_4 = var1_1 = var3_3 - 48;
        if (this.parennumber <= var1_1) {
            throw this.ex("parser.parse.2", this.offset);
        }
        ** GOTO lbl26
lbl11: // 1 sources:
        if (var3_3 == '?') {
            --this.offset;
        }
        this.next();
        var2_2 = this.parseFactor();
        switch (var2_2.type) {
            case 20: 
            case 21: 
            case 22: 
            case 23: {
                ** break;
            }
            case 8: {
                if (this.read() != 7) {
                    throw this.ex("parser.factor.1", this.offset - 1);
                }
                ** GOTO lbl35
            }
        }
        throw this.ex("parser.factor.5", this.offset);
        while ('1' <= (var3_3 = this.regex.charAt(this.offset + 1)) && var3_3 <= '9' && (var1_1 = var1_1 * 10 + (var3_3 - 48)) < this.parennumber) {
            var4_4 = var1_1;
            ++this.offset;
lbl26: // 2 sources:
            if (this.offset + 1 < this.regexlen) continue;
        }
        this.hasBackReferences = true;
        if (this.references == null) {
            this.references = new Vector<E>();
        }
        this.references.addElement(new ReferencePosition(var4_4, this.offset));
        ++this.offset;
        if (this.regex.charAt(this.offset) != ')') {
            throw this.ex("parser.factor.1", this.offset);
        }
        ++this.offset;
lbl35: // 3 sources:
        this.next();
        var4_5 = this.parseRegex();
        var5_6 = null;
        if (var4_5.type == 2) {
            if (var4_5.size() != 2) {
                throw this.ex("parser.factor.6", this.offset);
            }
            var5_6 = var4_5.getChild(1);
            var4_5 = var4_5.getChild(0);
        }
        if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
        }
        this.next();
        return Token.createCondition(var1_1, var2_2, var4_5, var5_6);
    }

    Token processModifiers() throws ParseException {
        Token.ModifierToken modifierToken;
        int n2;
        int n3 = 0;
        int n4 = 0;
        int n5 = -1;
        while (this.offset < this.regexlen) {
            n5 = this.regex.charAt(this.offset);
            n2 = REUtil.getOptionValue(n5);
            if (n2 == 0) break;
            n3 |= n2;
            ++this.offset;
        }
        if (this.offset >= this.regexlen) {
            throw this.ex("parser.factor.2", this.offset - 1);
        }
        if (n5 == 45) {
            ++this.offset;
            while (this.offset < this.regexlen) {
                n5 = this.regex.charAt(this.offset);
                n2 = REUtil.getOptionValue(n5);
                if (n2 == 0) break;
                n4 |= n2;
                ++this.offset;
            }
            if (this.offset >= this.regexlen) {
                throw this.ex("parser.factor.2", this.offset - 1);
            }
        }
        if (n5 == 58) {
            ++this.offset;
            this.next();
            modifierToken = Token.createModifierGroup(this.parseRegex(), n3, n4);
            if (this.read() != 7) {
                throw this.ex("parser.factor.1", this.offset - 1);
            }
            this.next();
        } else if (n5 == 41) {
            ++this.offset;
            this.next();
            modifierToken = Token.createModifierGroup(this.parseRegex(), n3, n4);
        } else {
            throw this.ex("parser.factor.3", this.offset);
        }
        return modifierToken;
    }

    Token processIndependent() throws ParseException {
        this.next();
        Token.ParenToken parenToken = Token.createLook(24, this.parseRegex());
        if (this.read() != 7) {
            throw this.ex("parser.factor.1", this.offset - 1);
        }
        this.next();
        return parenToken;
    }

    Token processBacksolidus_c() throws ParseException {
        char c2;
        if (this.offset >= this.regexlen || ((c2 = this.regex.charAt(this.offset++)) & 65504) != 64) {
            throw this.ex("parser.atom.1", this.offset - 1);
        }
        this.next();
        return Token.createChar(c2 - 64);
    }

    Token processBacksolidus_C() throws ParseException {
        throw this.ex("parser.process.1", this.offset);
    }

    Token processBacksolidus_i() throws ParseException {
        Token.CharToken charToken = Token.createChar(105);
        this.next();
        return charToken;
    }

    Token processBacksolidus_I() throws ParseException {
        throw this.ex("parser.process.1", this.offset);
    }

    Token processBacksolidus_g() throws ParseException {
        this.next();
        return Token.getGraphemePattern();
    }

    Token processBacksolidus_X() throws ParseException {
        this.next();
        return Token.getCombiningCharacterSequence();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    Token processBackreference() throws ParseException {
        var2_2 = var1_1 = this.chardata - 48;
        if (this.parennumber > var1_1) ** GOTO lbl8
        throw this.ex("parser.parse.2", this.offset - 2);
        while ('1' <= (var3_3 = this.regex.charAt(this.offset)) && var3_3 <= '9' && (var1_1 = var1_1 * 10 + (var3_3 - 48)) < this.parennumber) {
            ++this.offset;
            var2_2 = var1_1;
            this.chardata = var3_3;
lbl8: // 2 sources:
            if (this.offset < this.regexlen) continue;
        }
        var3_4 = Token.createBackReference(var2_2);
        this.hasBackReferences = true;
        if (this.references == null) {
            this.references = new Vector<E>();
        }
        this.references.addElement(new ReferencePosition(var2_2, this.offset - 2));
        this.next();
        return var3_4;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    Token parseFactor() throws ParseException {
        var1_1 = this.read();
        switch (var1_1) {
            case 11: {
                return this.processCaret();
            }
            case 12: {
                return this.processDollar();
            }
            case 14: {
                return this.processLookahead();
            }
            case 15: {
                return this.processNegativelookahead();
            }
            case 16: {
                return this.processLookbehind();
            }
            case 17: {
                return this.processNegativelookbehind();
            }
            case 21: {
                this.next();
                return Token.createEmpty();
            }
            case 10: {
                switch (this.chardata) {
                    case 65: {
                        return this.processBacksolidus_A();
                    }
                    case 90: {
                        return this.processBacksolidus_Z();
                    }
                    case 122: {
                        return this.processBacksolidus_z();
                    }
                    case 98: {
                        return this.processBacksolidus_b();
                    }
                    case 66: {
                        return this.processBacksolidus_B();
                    }
                    case 60: {
                        return this.processBacksolidus_lt();
                    }
                    case 62: {
                        return this.processBacksolidus_gt();
                    }
                }
            }
        }
        var2_2 = this.parseAtom();
        var1_1 = this.read();
        switch (var1_1) {
            case 3: {
                return this.processStar(var2_2);
            }
            case 4: {
                return this.processPlus(var2_2);
            }
            case 5: {
                return this.processQuestion(var2_2);
            }
            case 0: {
                if (this.chardata != 123) return var2_2;
                if (this.offset >= this.regexlen) return var2_2;
                var3_3 = this.offset;
                var4_4 = 0;
                var5_5 = -1;
                v0 = this.regex.charAt(var3_3++);
                var1_1 = v0;
                if (v0 < '0') throw this.ex("parser.quantifier.1", this.offset);
                if (var1_1 > 57) throw this.ex("parser.quantifier.1", this.offset);
                var4_4 = var1_1 - 48;
                ** GOTO lbl57
lbl-1000: // 1 sources:
                {
                    if ((var4_4 = var4_4 * 10 + var1_1 - 48) < 0) {
                        throw this.ex("parser.quantifier.5", this.offset);
                    }
lbl57: // 3 sources:
                    if (var3_3 >= this.regexlen) break;
                    v1 = this.regex.charAt(var3_3++);
                    var1_1 = v1;
                    ** while (v1 >= '0' && var1_1 <= 57)
                }
lbl61: // 2 sources:
                var5_5 = var4_4;
                if (var1_1 != 44) ** GOTO lbl80
                if (var3_3 >= this.regexlen) {
                    throw this.ex("parser.quantifier.3", this.offset);
                }
                v2 = this.regex.charAt(var3_3++);
                var1_1 = v2;
                if (v2 < '0' || var1_1 > 57) ** GOTO lbl70
                var5_5 = var1_1 - 48;
                ** GOTO lbl74
lbl70: // 1 sources:
                var5_5 = -1;
                ** GOTO lbl80
lbl-1000: // 1 sources:
                {
                    if ((var5_5 = var5_5 * 10 + var1_1 - 48) < 0) {
                        throw this.ex("parser.quantifier.5", this.offset);
                    }
lbl74: // 3 sources:
                    if (var3_3 >= this.regexlen) break;
                    v3 = this.regex.charAt(var3_3++);
                    var1_1 = v3;
                    ** while (v3 >= '0' && var1_1 <= 57)
                }
lbl78: // 2 sources:
                if (var4_4 > var5_5) {
                    throw this.ex("parser.quantifier.4", this.offset);
                }
lbl80: // 4 sources:
                if (var1_1 != 125) {
                    throw this.ex("parser.quantifier.2", this.offset);
                }
                if (this.checkQuestion(var3_3)) {
                    var2_2 = Token.createNGClosure(var2_2);
                    this.offset = var3_3 + 1;
                } else {
                    var2_2 = Token.createClosure(var2_2);
                    this.offset = var3_3;
                }
                var2_2.setMin(var4_4);
                var2_2.setMax(var5_5);
                this.next();
            }
        }
        return var2_2;
    }

    Token parseAtom() throws ParseException {
        int n2 = this.read();
        Token token = null;
        switch (n2) {
            case 6: {
                return this.processParen();
            }
            case 13: {
                return this.processParen2();
            }
            case 23: {
                return this.processCondition();
            }
            case 22: {
                return this.processModifiers();
            }
            case 18: {
                return this.processIndependent();
            }
            case 8: {
                this.next();
                token = Token.token_dot;
                break;
            }
            case 9: {
                return this.parseCharacterClass(true);
            }
            case 19: {
                return this.parseSetOperations();
            }
            case 10: {
                switch (this.chardata) {
                    case 68: 
                    case 83: 
                    case 87: 
                    case 100: 
                    case 115: 
                    case 119: {
                        token = this.getTokenForShorthand(this.chardata);
                        this.next();
                        return token;
                    }
                    case 101: 
                    case 102: 
                    case 110: 
                    case 114: 
                    case 116: 
                    case 117: 
                    case 118: 
                    case 120: {
                        int n3 = this.decodeEscaped();
                        if (n3 < 65536) {
                            token = Token.createChar(n3);
                            break;
                        }
                        token = Token.createString(REUtil.decomposeToSurrogates(n3));
                        break;
                    }
                    case 99: {
                        return this.processBacksolidus_c();
                    }
                    case 67: {
                        return this.processBacksolidus_C();
                    }
                    case 105: {
                        return this.processBacksolidus_i();
                    }
                    case 73: {
                        return this.processBacksolidus_I();
                    }
                    case 103: {
                        return this.processBacksolidus_g();
                    }
                    case 88: {
                        return this.processBacksolidus_X();
                    }
                    case 49: 
                    case 50: 
                    case 51: 
                    case 52: 
                    case 53: 
                    case 54: 
                    case 55: 
                    case 56: 
                    case 57: {
                        return this.processBackreference();
                    }
                    case 80: 
                    case 112: {
                        int n4 = this.offset;
                        token = this.processBacksolidus_pP(this.chardata);
                        if (token != null) break;
                        throw this.ex("parser.atom.5", n4);
                    }
                    default: {
                        token = Token.createChar(this.chardata);
                    }
                }
                this.next();
                break;
            }
            case 0: {
                if (this.chardata == 93 || this.chardata == 123 || this.chardata == 125) {
                    throw this.ex("parser.atom.4", this.offset - 1);
                }
                token = Token.createChar(this.chardata);
                int n5 = this.chardata;
                this.next();
                if (!REUtil.isHighSurrogate(n5) || this.read() != 0 || !REUtil.isLowSurrogate(this.chardata)) break;
                char[] arrc = new char[]{(char)n5, (char)this.chardata};
                token = Token.createParen(Token.createString(new String(arrc)), 0);
                this.next();
                break;
            }
            default: {
                throw this.ex("parser.atom.4", this.offset - 1);
            }
        }
        return token;
    }

    protected RangeToken processBacksolidus_pP(int n2) throws ParseException {
        this.next();
        if (this.read() != 0 || this.chardata != 123) {
            throw this.ex("parser.atom.2", this.offset - 1);
        }
        boolean bl = n2 == 112;
        int n3 = this.offset;
        int n4 = this.regex.indexOf(125, n3);
        if (n4 < 0) {
            throw this.ex("parser.atom.3", this.offset);
        }
        String string = this.regex.substring(n3, n4);
        this.offset = n4 + 1;
        return Token.getRange(string, bl, this.isSet(512));
    }

    int processCIinCharacterClass(RangeToken rangeToken, int n2) {
        return this.decodeEscaped();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected RangeToken parseCharacterClass(boolean var1_1) throws ParseException {
        this.setContext(1);
        this.next();
        var2_2 = false;
        var3_3 = null;
        if (this.read() == 0 && this.chardata == 94) {
            var2_2 = true;
            this.next();
            if (var1_1) {
                var4_4 = Token.createNRange();
            } else {
                var3_3 = Token.createRange();
                var3_3.addRange(0, 1114111);
                var4_4 = Token.createRange();
            }
        } else {
            var4_4 = Token.createRange();
        }
        var6_5 = true;
        while ((var5_6 = this.read()) != 1) {
            if (var5_6 == 0 && this.chardata == 93 && !var6_5) break;
            var7_7 = this.chardata;
            var8_8 = false;
            if (var5_6 != 10) ** GOTO lbl45
            switch (var7_7) {
                case 68: 
                case 83: 
                case 87: 
                case 100: 
                case 115: 
                case 119: {
                    var4_4.mergeRanges(this.getTokenForShorthand(var7_7));
                    var8_8 = true;
                    break;
                }
                case 67: 
                case 73: 
                case 99: 
                case 105: {
                    var7_7 = this.processCIinCharacterClass(var4_4, var7_7);
                    if (var7_7 < 0) {
                        var8_8 = true;
                        break;
                    }
                    ** GOTO lbl75
                }
                case 80: 
                case 112: {
                    var9_9 = this.offset;
                    var10_11 = this.processBacksolidus_pP(var7_7);
                    if (var10_11 == null) {
                        throw this.ex("parser.atom.5", var9_9);
                    }
                    var4_4.mergeRanges(var10_11);
                    var8_8 = true;
                    break;
                }
                default: {
                    var7_7 = this.decodeEscaped();
                    break;
                }
            }
            ** GOTO lbl75
lbl45: // 1 sources:
            if (var5_6 == 20) {
                var9_9 = this.regex.indexOf(58, this.offset);
                if (var9_9 < 0) {
                    throw this.ex("parser.cc.1", this.offset);
                }
                var10_12 = true;
                if (this.regex.charAt(this.offset) == '^') {
                    ++this.offset;
                    var10_12 = false;
                }
                if ((var12_14 = Token.getRange(var11_13 = this.regex.substring(this.offset, var9_9), var10_12, this.isSet(512))) == null) {
                    throw this.ex("parser.cc.3", this.offset);
                }
                var4_4.mergeRanges(var12_14);
                var8_8 = true;
                if (var9_9 + 1 >= this.regexlen) throw this.ex("parser.cc.1", var9_9);
                if (this.regex.charAt(var9_9 + 1) != ']') {
                    throw this.ex("parser.cc.1", var9_9);
                }
                this.offset = var9_9 + 2;
            } else if (var5_6 == 24 && !var6_5) {
                if (var2_2) {
                    var2_2 = false;
                    if (var1_1) {
                        var4_4 = (RangeToken)Token.complementRanges(var4_4);
                    } else {
                        var3_3.subtractRanges(var4_4);
                        var4_4 = var3_3;
                    }
                }
                var9_10 = this.parseCharacterClass(false);
                var4_4.subtractRanges(var9_10);
                if (this.read() != 0) throw this.ex("parser.cc.5", this.offset);
                if (this.chardata == 93) break;
                throw this.ex("parser.cc.5", this.offset);
            }
lbl75: // 5 sources:
            this.next();
            if (!var8_8) {
                if (this.read() != 0 || this.chardata != 45) {
                    if (!this.isSet(2) || var7_7 > 65535) {
                        var4_4.addRange(var7_7, var7_7);
                    } else {
                        RegexParser.addCaseInsensitiveChar(var4_4, var7_7);
                    }
                } else {
                    if (var5_6 == 24) {
                        throw this.ex("parser.cc.8", this.offset - 1);
                    }
                    this.next();
                    var5_6 = this.read();
                    if (var5_6 == 1) {
                        throw this.ex("parser.cc.2", this.offset);
                    }
                    if (var5_6 == 0 && this.chardata == 93) {
                        if (!this.isSet(2) || var7_7 > 65535) {
                            var4_4.addRange(var7_7, var7_7);
                        } else {
                            RegexParser.addCaseInsensitiveChar(var4_4, var7_7);
                        }
                        var4_4.addRange(45, 45);
                    } else {
                        var9_9 = this.chardata;
                        if (var5_6 == 10) {
                            var9_9 = this.decodeEscaped();
                        }
                        this.next();
                        if (var7_7 > var9_9) {
                            throw this.ex("parser.ope.3", this.offset - 1);
                        }
                        if (!this.isSet(2) || var7_7 > 65535 && var9_9 > 65535) {
                            var4_4.addRange(var7_7, var9_9);
                        } else {
                            RegexParser.addCaseInsensitiveCharRange(var4_4, var7_7, var9_9);
                        }
                    }
                }
            }
            if (this.isSet(1024) && this.read() == 0 && this.chardata == 44) {
                this.next();
            }
            var6_5 = false;
        }
        if (this.read() == 1) {
            throw this.ex("parser.cc.2", this.offset);
        }
        if (!var1_1 && var2_2) {
            var3_3.subtractRanges(var4_4);
            var4_4 = var3_3;
        }
        var4_4.sortRanges();
        var4_4.compactRanges();
        this.setContext(0);
        this.next();
        return var4_4;
    }

    protected RangeToken parseSetOperations() throws ParseException {
        int n2;
        RangeToken rangeToken = this.parseCharacterClass(false);
        while ((n2 = this.read()) != 7) {
            int n3 = this.chardata;
            if (n2 == 0 && (n3 == 45 || n3 == 38) || n2 == 4) {
                this.next();
                if (this.read() != 9) {
                    throw this.ex("parser.ope.1", this.offset - 1);
                }
                RangeToken rangeToken2 = this.parseCharacterClass(false);
                if (n2 == 4) {
                    rangeToken.mergeRanges(rangeToken2);
                    continue;
                }
                if (n3 == 45) {
                    rangeToken.subtractRanges(rangeToken2);
                    continue;
                }
                if (n3 == 38) {
                    rangeToken.intersectRanges(rangeToken2);
                    continue;
                }
                throw new RuntimeException("ASSERT");
            }
            throw this.ex("parser.ope.2", this.offset - 1);
        }
        this.next();
        return rangeToken;
    }

    Token getTokenForShorthand(int n2) {
        Token token;
        switch (n2) {
            case 100: {
                token = this.isSet(32) ? Token.getRange("Nd", true) : Token.token_0to9;
                break;
            }
            case 68: {
                token = this.isSet(32) ? Token.getRange("Nd", false) : Token.token_not_0to9;
                break;
            }
            case 119: {
                token = this.isSet(32) ? Token.getRange("IsWord", true) : Token.token_wordchars;
                break;
            }
            case 87: {
                token = this.isSet(32) ? Token.getRange("IsWord", false) : Token.token_not_wordchars;
                break;
            }
            case 115: {
                token = this.isSet(32) ? Token.getRange("IsSpace", true) : Token.token_spaces;
                break;
            }
            case 83: {
                token = this.isSet(32) ? Token.getRange("IsSpace", false) : Token.token_not_spaces;
                break;
            }
            default: {
                throw new RuntimeException("Internal Error: shorthands: \\u" + Integer.toString(n2, 16));
            }
        }
        return token;
    }

    int decodeEscaped() throws ParseException {
        if (this.read() != 10) {
            throw this.ex("parser.next.1", this.offset - 1);
        }
        int n2 = this.chardata;
        switch (n2) {
            case 101: {
                n2 = 27;
                break;
            }
            case 102: {
                n2 = 12;
                break;
            }
            case 110: {
                n2 = 10;
                break;
            }
            case 114: {
                n2 = 13;
                break;
            }
            case 116: {
                n2 = 9;
                break;
            }
            case 120: {
                this.next();
                if (this.read() != 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                if (this.chardata == 123) {
                    int n3 = 0;
                    int n4 = 0;
                    do {
                        this.next();
                        if (this.read() != 0) {
                            throw this.ex("parser.descape.1", this.offset - 1);
                        }
                        n3 = RegexParser.hexChar(this.chardata);
                        if (n3 < 0) break;
                        if (n4 > n4 * 16) {
                            throw this.ex("parser.descape.2", this.offset - 1);
                        }
                        n4 = n4 * 16 + n3;
                    } while (true);
                    if (this.chardata != 125) {
                        throw this.ex("parser.descape.3", this.offset - 1);
                    }
                    if (n4 > 1114111) {
                        throw this.ex("parser.descape.4", this.offset - 1);
                    }
                    n2 = n4;
                    break;
                }
                int n5 = 0;
                if (this.read() != 0 || (n5 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                int n6 = n5;
                this.next();
                if (this.read() != 0 || (n5 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                n2 = n6 = n6 * 16 + n5;
                break;
            }
            case 117: {
                int n7 = 0;
                this.next();
                if (this.read() != 0 || (n7 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                int n8 = n7;
                this.next();
                if (this.read() != 0 || (n7 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                n8 = n8 * 16 + n7;
                this.next();
                if (this.read() != 0 || (n7 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                n8 = n8 * 16 + n7;
                this.next();
                if (this.read() != 0 || (n7 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                n2 = n8 = n8 * 16 + n7;
                break;
            }
            case 118: {
                int n9;
                this.next();
                if (this.read() != 0 || (n9 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                int n10 = n9;
                this.next();
                if (this.read() != 0 || (n9 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                n10 = n10 * 16 + n9;
                this.next();
                if (this.read() != 0 || (n9 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                n10 = n10 * 16 + n9;
                this.next();
                if (this.read() != 0 || (n9 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                n10 = n10 * 16 + n9;
                this.next();
                if (this.read() != 0 || (n9 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                n10 = n10 * 16 + n9;
                this.next();
                if (this.read() != 0 || (n9 = RegexParser.hexChar(this.chardata)) < 0) {
                    throw this.ex("parser.descape.1", this.offset - 1);
                }
                if ((n10 = n10 * 16 + n9) > 1114111) {
                    throw this.ex("parser.descappe.4", this.offset - 1);
                }
                n2 = n10;
                break;
            }
            case 65: 
            case 90: 
            case 122: {
                throw this.ex("parser.descape.5", this.offset - 2);
            }
        }
        return n2;
    }

    private static final int hexChar(int n2) {
        if (n2 < 48) {
            return -1;
        }
        if (n2 > 102) {
            return -1;
        }
        if (n2 <= 57) {
            return n2 - 48;
        }
        if (n2 < 65) {
            return -1;
        }
        if (n2 <= 70) {
            return n2 - 65 + 10;
        }
        if (n2 < 97) {
            return -1;
        }
        return n2 - 97 + 10;
    }

    protected static final void addCaseInsensitiveChar(RangeToken rangeToken, int n2) {
        int[] arrn = CaseInsensitiveMap.get(n2);
        rangeToken.addRange(n2, n2);
        if (arrn != null) {
            int n3 = 0;
            while (n3 < arrn.length) {
                rangeToken.addRange(arrn[n3], arrn[n3]);
                n3 += 2;
            }
        }
    }

    protected static final void addCaseInsensitiveCharRange(RangeToken rangeToken, int n2, int n3) {
        int n4;
        int n5;
        if (n2 <= n3) {
            n5 = n2;
            n4 = n3;
        } else {
            n5 = n3;
            n4 = n2;
        }
        rangeToken.addRange(n5, n4);
        int n6 = n5;
        while (n6 <= n4) {
            int[] arrn = CaseInsensitiveMap.get(n6);
            if (arrn != null) {
                int n7 = 0;
                while (n7 < arrn.length) {
                    rangeToken.addRange(arrn[n7], arrn[n7]);
                    n7 += 2;
                }
            }
            ++n6;
        }
    }

    static class ReferencePosition {
        int refNumber;
        int position;

        ReferencePosition(int n2, int n3) {
            this.refNumber = n2;
            this.position = n3;
        }
    }

}

