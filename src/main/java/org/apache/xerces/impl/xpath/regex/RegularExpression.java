/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

import java.io.Serializable;
import java.text.CharacterIterator;
import java.util.Locale;
import java.util.Stack;
import org.apache.xerces.impl.xpath.regex.BMPattern;
import org.apache.xerces.impl.xpath.regex.Match;
import org.apache.xerces.impl.xpath.regex.Op;
import org.apache.xerces.impl.xpath.regex.ParseException;
import org.apache.xerces.impl.xpath.regex.ParserForXMLSchema;
import org.apache.xerces.impl.xpath.regex.REUtil;
import org.apache.xerces.impl.xpath.regex.RangeToken;
import org.apache.xerces.impl.xpath.regex.RegexParser;
import org.apache.xerces.impl.xpath.regex.Token;
import org.apache.xerces.util.IntStack;

public class RegularExpression
implements Serializable {
    private static final long serialVersionUID = 6242499334195006401L;
    static final boolean DEBUG = false;
    String regex;
    int options;
    int nofparen;
    Token tokentree;
    boolean hasBackReferences = false;
    transient int minlength;
    transient Op operations = null;
    transient int numberOfClosures;
    transient Context context = null;
    transient RangeToken firstChar = null;
    transient String fixedString = null;
    transient int fixedStringOptions;
    transient BMPattern fixedStringTable = null;
    transient boolean fixedStringOnly = false;
    static final int IGNORE_CASE = 2;
    static final int SINGLE_LINE = 4;
    static final int MULTIPLE_LINES = 8;
    static final int EXTENDED_COMMENT = 16;
    static final int USE_UNICODE_CATEGORY = 32;
    static final int UNICODE_WORD_BOUNDARY = 64;
    static final int PROHIBIT_HEAD_CHARACTER_OPTIMIZATION = 128;
    static final int PROHIBIT_FIXED_STRING_OPTIMIZATION = 256;
    static final int XMLSCHEMA_MODE = 512;
    static final int SPECIAL_COMMA = 1024;
    private static final int WT_IGNORE = 0;
    private static final int WT_LETTER = 1;
    private static final int WT_OTHER = 2;
    static final int LINE_FEED = 10;
    static final int CARRIAGE_RETURN = 13;
    static final int LINE_SEPARATOR = 8232;
    static final int PARAGRAPH_SEPARATOR = 8233;

    private synchronized void compile(Token token) {
        if (this.operations != null) {
            return;
        }
        this.numberOfClosures = 0;
        this.operations = this.compile(token, null, false);
    }

    private Op compile(Token token, Op op, boolean bl) {
        Op op2;
        switch (token.type) {
            case 11: {
                op2 = Op.createDot();
                op2.next = op;
                break;
            }
            case 0: {
                op2 = Op.createChar(token.getChar());
                op2.next = op;
                break;
            }
            case 8: {
                op2 = Op.createAnchor(token.getChar());
                op2.next = op;
                break;
            }
            case 4: 
            case 5: {
                op2 = Op.createRange(token);
                op2.next = op;
                break;
            }
            case 1: {
                op2 = op;
                if (!bl) {
                    int n2 = token.size() - 1;
                    while (n2 >= 0) {
                        op2 = this.compile(token.getChild(n2), op2, false);
                        --n2;
                    }
                } else {
                    int n3 = 0;
                    while (n3 < token.size()) {
                        op2 = this.compile(token.getChild(n3), op2, true);
                        ++n3;
                    }
                }
                break;
            }
            case 2: {
                Op.UnionOp unionOp = Op.createUnion(token.size());
                int n4 = 0;
                while (n4 < token.size()) {
                    unionOp.addElement(this.compile(token.getChild(n4), op, bl));
                    ++n4;
                }
                op2 = unionOp;
                break;
            }
            case 3: 
            case 9: {
                int n5;
                Token token2 = token.getChild(0);
                int n6 = token.getMin();
                int n7 = token.getMax();
                if (n6 >= 0 && n6 == n7) {
                    op2 = op;
                    n5 = 0;
                    while (n5 < n6) {
                        op2 = this.compile(token2, op2, bl);
                        ++n5;
                    }
                } else {
                    if (n6 > 0 && n7 > 0) {
                        n7 -= n6;
                    }
                    if (n7 > 0) {
                        op2 = op;
                        int n8 = 0;
                        while (n8 < n7) {
                            Op.ChildOp childOp = Op.createQuestion(token.type == 9);
                            childOp.next = op;
                            childOp.setChild(this.compile(token2, op2, bl));
                            op2 = childOp;
                            ++n8;
                        }
                    } else {
                        Op.ChildOp childOp = token.type == 9 ? Op.createNonGreedyClosure() : Op.createClosure(this.numberOfClosures++);
                        childOp.next = op;
                        childOp.setChild(this.compile(token2, childOp, bl));
                        op2 = childOp;
                    }
                    if (n6 <= 0) break;
                    n5 = 0;
                    while (n5 < n6) {
                        op2 = this.compile(token2, op2, bl);
                        ++n5;
                    }
                }
                break;
            }
            case 7: {
                op2 = op;
                break;
            }
            case 10: {
                op2 = Op.createString(token.getString());
                op2.next = op;
                break;
            }
            case 12: {
                op2 = Op.createBackReference(token.getReferenceNumber());
                op2.next = op;
                break;
            }
            case 6: {
                if (token.getParenNumber() == 0) {
                    op2 = this.compile(token.getChild(0), op, bl);
                    break;
                }
                if (bl) {
                    op = Op.createCapture(token.getParenNumber(), op);
                    op = this.compile(token.getChild(0), op, bl);
                    op2 = Op.createCapture(- token.getParenNumber(), op);
                    break;
                }
                op = Op.createCapture(- token.getParenNumber(), op);
                op = this.compile(token.getChild(0), op, bl);
                op2 = Op.createCapture(token.getParenNumber(), op);
                break;
            }
            case 20: {
                op2 = Op.createLook(20, op, this.compile(token.getChild(0), null, false));
                break;
            }
            case 21: {
                op2 = Op.createLook(21, op, this.compile(token.getChild(0), null, false));
                break;
            }
            case 22: {
                op2 = Op.createLook(22, op, this.compile(token.getChild(0), null, true));
                break;
            }
            case 23: {
                op2 = Op.createLook(23, op, this.compile(token.getChild(0), null, true));
                break;
            }
            case 24: {
                op2 = Op.createIndependent(op, this.compile(token.getChild(0), null, bl));
                break;
            }
            case 25: {
                op2 = Op.createModifier(op, this.compile(token.getChild(0), null, bl), ((Token.ModifierToken)token).getOptions(), ((Token.ModifierToken)token).getOptionsMask());
                break;
            }
            case 26: {
                Token.ConditionToken conditionToken = (Token.ConditionToken)token;
                int n9 = conditionToken.refNumber;
                Op op3 = conditionToken.condition == null ? null : this.compile(conditionToken.condition, null, bl);
                Op op4 = this.compile(conditionToken.yes, op, bl);
                Op op5 = conditionToken.no == null ? null : this.compile(conditionToken.no, op, bl);
                op2 = Op.createCondition(op, n9, op3, op4, op5);
                break;
            }
            default: {
                throw new RuntimeException("Unknown token type: " + token.type);
            }
        }
        return op2;
    }

    public boolean matches(char[] arrc) {
        return this.matches(arrc, 0, arrc.length, (Match)null);
    }

    public boolean matches(char[] arrc, int n2, int n3) {
        return this.matches(arrc, n2, n3, (Match)null);
    }

    public boolean matches(char[] arrc, Match match) {
        return this.matches(arrc, 0, arrc.length, match);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean matches(char[] var1_1, int var2_2, int var3_3, Match var4_4) {
        block23 : {
            var5_5 = this;
            // MONITORENTER : var5_5
            if (this.operations == null) {
                this.prepare();
            }
            if (this.context == null) {
                this.context = new Context();
            }
            // MONITOREXIT : var5_5
            var6_6 = null;
            var7_7 = this.context;
            // MONITORENTER : var7_7
            var6_6 = this.context.inuse != false ? new Context() : this.context;
            var6_6.reset(var1_1, var2_2, var3_3, this.numberOfClosures);
            // MONITOREXIT : var7_7
            if (var4_4 != null) {
                var4_4.setNumberOfGroups(this.nofparen);
                var4_4.setSource(var1_1);
            } else if (this.hasBackReferences) {
                var4_4 = new Match();
                var4_4.setNumberOfGroups(this.nofparen);
            }
            var6_6.match = var4_4;
            if (RegularExpression.isSet(this.options, 512)) {
                var8_8 = this.match(var6_6, this.operations, var6_6.start, 1, this.options);
                if (var8_8 != var6_6.limit) return false;
                if (var6_6.match != null) {
                    var6_6.match.setBeginning(0, var6_6.start);
                    var6_6.match.setEnd(0, var8_8);
                }
                var6_6.setInUse(false);
                return true;
            }
            if (this.fixedStringOnly) {
                var8_9 = this.fixedStringTable.matches(var1_1, var6_6.start, var6_6.limit);
                if (var8_9 < 0) {
                    var6_6.setInUse(false);
                    return false;
                }
                if (var6_6.match != null) {
                    var6_6.match.setBeginning(0, var8_9);
                    var6_6.match.setEnd(0, var8_9 + this.fixedString.length());
                }
                var6_6.setInUse(false);
                return true;
            }
            if (this.fixedString != null && (var8_10 = this.fixedStringTable.matches(var1_1, var6_6.start, var6_6.limit)) < 0) {
                var6_6.setInUse(false);
                return false;
            }
            var8_10 = var6_6.limit - this.minlength;
            var10_11 = -1;
            if (this.operations == null || this.operations.type != 7 || this.operations.getChild().type != 0) ** GOTO lbl53
            if (!RegularExpression.isSet(this.options, 4)) ** GOTO lbl50
            var9_12 = var6_6.start;
            var10_11 = this.match(var6_6, this.operations, var6_6.start, 1, this.options);
            ** GOTO lbl78
lbl50: // 1 sources:
            var11_13 = true;
            var9_12 = var6_6.start;
            ** GOTO lbl66
lbl53: // 1 sources:
            if (this.firstChar == null) ** GOTO lbl57
            var11_14 = this.firstChar;
            var9_12 = var6_6.start;
            ** GOTO lbl73
lbl57: // 1 sources:
            var9_12 = var6_6.start;
            ** GOTO lbl77
lbl-1000: // 1 sources:
            {
                var12_15 = var1_1[var9_12];
                if (RegularExpression.isEOLChar(var12_15)) {
                    var11_13 = true;
                } else {
                    if (var11_13 && 0 <= (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) break block23;
                    var11_13 = false;
                }
                ++var9_12;
lbl66: // 2 sources:
                ** while (var9_12 <= var8_10)
            }
lbl67: // 1 sources:
            ** GOTO lbl78
lbl-1000: // 1 sources:
            {
                var12_16 = var1_1[var9_12];
                if (REUtil.isHighSurrogate(var12_16) && var9_12 + 1 < var6_6.limit) {
                    var12_16 = REUtil.composeFromSurrogates(var12_16, var1_1[var9_12 + 1]);
                }
                if (var11_14.match(var12_16) && 0 <= (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) break block23;
                ++var9_12;
lbl73: // 2 sources:
                ** while (var9_12 <= var8_10)
            }
lbl74: // 1 sources:
            ** GOTO lbl78
            while (0 > (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) {
                ++var9_12;
lbl77: // 2 sources:
                if (var9_12 <= var8_10) continue;
            }
        }
        if (var10_11 < 0) {
            var6_6.setInUse(false);
            return false;
        }
        if (var6_6.match != null) {
            var6_6.match.setBeginning(0, var9_12);
            var6_6.match.setEnd(0, var10_11);
        }
        var6_6.setInUse(false);
        return true;
    }

    public boolean matches(String string) {
        return this.matches(string, 0, string.length(), (Match)null);
    }

    public boolean matches(String string, int n2, int n3) {
        return this.matches(string, n2, n3, (Match)null);
    }

    public boolean matches(String string, Match match) {
        return this.matches(string, 0, string.length(), match);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean matches(String var1_1, int var2_2, int var3_3, Match var4_4) {
        block23 : {
            var5_5 = this;
            // MONITORENTER : var5_5
            if (this.operations == null) {
                this.prepare();
            }
            if (this.context == null) {
                this.context = new Context();
            }
            // MONITOREXIT : var5_5
            var6_6 = null;
            var7_7 = this.context;
            // MONITORENTER : var7_7
            var6_6 = this.context.inuse != false ? new Context() : this.context;
            var6_6.reset(var1_1, var2_2, var3_3, this.numberOfClosures);
            // MONITOREXIT : var7_7
            if (var4_4 != null) {
                var4_4.setNumberOfGroups(this.nofparen);
                var4_4.setSource(var1_1);
            } else if (this.hasBackReferences) {
                var4_4 = new Match();
                var4_4.setNumberOfGroups(this.nofparen);
            }
            var6_6.match = var4_4;
            if (RegularExpression.isSet(this.options, 512)) {
                var8_8 = this.match(var6_6, this.operations, var6_6.start, 1, this.options);
                if (var8_8 != var6_6.limit) return false;
                if (var6_6.match != null) {
                    var6_6.match.setBeginning(0, var6_6.start);
                    var6_6.match.setEnd(0, var8_8);
                }
                var6_6.setInUse(false);
                return true;
            }
            if (this.fixedStringOnly) {
                var8_9 = this.fixedStringTable.matches(var1_1, var6_6.start, var6_6.limit);
                if (var8_9 < 0) {
                    var6_6.setInUse(false);
                    return false;
                }
                if (var6_6.match != null) {
                    var6_6.match.setBeginning(0, var8_9);
                    var6_6.match.setEnd(0, var8_9 + this.fixedString.length());
                }
                var6_6.setInUse(false);
                return true;
            }
            if (this.fixedString != null && (var8_10 = this.fixedStringTable.matches(var1_1, var6_6.start, var6_6.limit)) < 0) {
                var6_6.setInUse(false);
                return false;
            }
            var8_10 = var6_6.limit - this.minlength;
            var10_11 = -1;
            if (this.operations == null || this.operations.type != 7 || this.operations.getChild().type != 0) ** GOTO lbl53
            if (!RegularExpression.isSet(this.options, 4)) ** GOTO lbl50
            var9_12 = var6_6.start;
            var10_11 = this.match(var6_6, this.operations, var6_6.start, 1, this.options);
            ** GOTO lbl78
lbl50: // 1 sources:
            var11_13 = true;
            var9_12 = var6_6.start;
            ** GOTO lbl66
lbl53: // 1 sources:
            if (this.firstChar == null) ** GOTO lbl57
            var11_14 = this.firstChar;
            var9_12 = var6_6.start;
            ** GOTO lbl73
lbl57: // 1 sources:
            var9_12 = var6_6.start;
            ** GOTO lbl77
lbl-1000: // 1 sources:
            {
                var12_15 = var1_1.charAt(var9_12);
                if (RegularExpression.isEOLChar(var12_15)) {
                    var11_13 = true;
                } else {
                    if (var11_13 && 0 <= (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) break block23;
                    var11_13 = false;
                }
                ++var9_12;
lbl66: // 2 sources:
                ** while (var9_12 <= var8_10)
            }
lbl67: // 1 sources:
            ** GOTO lbl78
lbl-1000: // 1 sources:
            {
                var12_16 = var1_1.charAt(var9_12);
                if (REUtil.isHighSurrogate(var12_16) && var9_12 + 1 < var6_6.limit) {
                    var12_16 = REUtil.composeFromSurrogates(var12_16, var1_1.charAt(var9_12 + 1));
                }
                if (var11_14.match(var12_16) && 0 <= (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) break block23;
                ++var9_12;
lbl73: // 2 sources:
                ** while (var9_12 <= var8_10)
            }
lbl74: // 1 sources:
            ** GOTO lbl78
            while (0 > (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) {
                ++var9_12;
lbl77: // 2 sources:
                if (var9_12 <= var8_10) continue;
            }
        }
        if (var10_11 < 0) {
            var6_6.setInUse(false);
            return false;
        }
        if (var6_6.match != null) {
            var6_6.match.setBeginning(0, var9_12);
            var6_6.match.setEnd(0, var10_11);
        }
        var6_6.setInUse(false);
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int match(Context var1_1, Op var2_2, int var3_3, int var4_4, int var5_5) {
        var6_6 = var1_1.target;
        var7_7 = new Stack<Op>();
        var8_8 = new IntStack();
        var9_9 = RegularExpression.isSet(var5_5, 2);
        var10_10 = -1;
        var11_11 = false;
        do {
            if (var2_2 != null && var3_3 <= var1_1.limit && var3_3 >= var1_1.start) ** GOTO lbl12
            var10_10 = var2_2 == null ? (RegularExpression.isSet(var5_5, 512) != false && var3_3 != var1_1.limit ? -1 : var3_3) : -1;
            var11_11 = true;
            ** GOTO lbl239
lbl12: // 1 sources:
            var10_10 = -1;
            switch (var2_2.type) {
                case 1: {
                    v0 = var12_12 = var4_4 > 0 ? var3_3 : var3_3 - 1;
                    if (var12_12 >= var1_1.limit || var12_12 < 0 || !this.matchChar(var2_2.getData(), var6_6.charAt(var12_12), var9_9)) {
                        var11_11 = true;
                    } else {
                        var3_3 += var4_4;
                        var2_2 = var2_2.next;
                    }
                    ** GOTO lbl239
                }
                case 0: {
                    v1 = var12_12 = var4_4 > 0 ? var3_3 : var3_3 - 1;
                    if (var12_12 < var1_1.limit && var12_12 >= 0) ** GOTO lbl27
                    var11_11 = true;
                    ** GOTO lbl239
lbl27: // 1 sources:
                    if (!RegularExpression.isSet(var5_5, 4)) ** GOTO lbl31
                    if (REUtil.isHighSurrogate(var6_6.charAt(var12_12)) && var12_12 + var4_4 >= 0 && var12_12 + var4_4 < var1_1.limit) {
                        var12_12 += var4_4;
                    }
                    ** GOTO lbl-1000
lbl31: // 1 sources:
                    var13_15 = var6_6.charAt(var12_12);
                    if (REUtil.isHighSurrogate(var13_15) && var12_12 + var4_4 >= 0 && var12_12 + var4_4 < var1_1.limit) {
                        var13_15 = REUtil.composeFromSurrogates(var13_15, var6_6.charAt(var12_12 += var4_4));
                    }
                    if (RegularExpression.isEOLChar(var13_15)) {
                        var11_11 = true;
                    } else lbl-1000: // 2 sources:
                    {
                        var3_3 = var4_4 > 0 ? var12_12 + 1 : var12_12;
                        var2_2 = var2_2.next;
                    }
                    ** GOTO lbl239
                }
                case 3: 
                case 4: {
                    v2 = var12_12 = var4_4 > 0 ? var3_3 : var3_3 - 1;
                    if (var12_12 >= var1_1.limit || var12_12 < 0) {
                        var11_11 = true;
                    } else {
                        var13_15 = var6_6.charAt(var3_3);
                        if (REUtil.isHighSurrogate(var13_15) && var12_12 + var4_4 < var1_1.limit && var12_12 + var4_4 >= 0) {
                            var13_15 = REUtil.composeFromSurrogates(var13_15, var6_6.charAt(var12_12 += var4_4));
                        }
                        if (!(var14_16 = var2_2.getToken()).match(var13_15)) {
                            var11_11 = true;
                        } else {
                            var3_3 = var4_4 > 0 ? var12_12 + 1 : var12_12;
                            var2_2 = var2_2.next;
                        }
                    }
                    ** GOTO lbl239
                }
                case 5: {
                    if (!this.matchAnchor(var6_6, var2_2, var1_1, var3_3, var5_5)) {
                        var11_11 = true;
                    } else {
                        var2_2 = var2_2.next;
                    }
                    ** GOTO lbl239
                }
                case 16: {
                    var12_12 = var2_2.getData();
                    if (var12_12 <= 0) throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var12_12);
                    if (var12_12 >= this.nofparen) {
                        throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var12_12);
                    }
                    if (var1_1.match.getBeginning(var12_12) >= 0 && var1_1.match.getEnd(var12_12) >= 0) ** GOTO lbl68
                    var11_11 = true;
                    ** GOTO lbl239
lbl68: // 1 sources:
                    var13_15 = var1_1.match.getBeginning(var12_12);
                    var14_17 = var1_1.match.getEnd(var12_12) - var13_15;
                    if (var4_4 <= 0) ** GOTO lbl76
                    if (var6_6.regionMatches(var9_9, var3_3, var1_1.limit, var13_15, var14_17)) ** GOTO lbl74
                    var11_11 = true;
                    ** GOTO lbl239
lbl74: // 1 sources:
                    var3_3 += var14_17;
                    ** GOTO lbl80
lbl76: // 1 sources:
                    if (!var6_6.regionMatches(var9_9, var3_3 - var14_17, var1_1.limit, var13_15, var14_17)) {
                        var11_11 = true;
                    } else {
                        var3_3 -= var14_17;
lbl80: // 2 sources:
                        var2_2 = var2_2.next;
                    }
                    ** GOTO lbl239
                }
                case 6: {
                    var12_13 = var2_2.getString();
                    var13_15 = var12_13.length();
                    if (var4_4 <= 0) ** GOTO lbl91
                    if (var6_6.regionMatches(var9_9, var3_3, var1_1.limit, var12_13, var13_15)) ** GOTO lbl89
                    var11_11 = true;
                    ** GOTO lbl239
lbl89: // 1 sources:
                    var3_3 += var13_15;
                    ** GOTO lbl95
lbl91: // 1 sources:
                    if (!var6_6.regionMatches(var9_9, var3_3 - var13_15, var1_1.limit, var12_13, var13_15)) {
                        var11_11 = true;
                    } else {
                        var3_3 -= var13_15;
lbl95: // 2 sources:
                        var2_2 = var2_2.next;
                    }
                    ** GOTO lbl239
                }
                case 7: {
                    var12_12 = var2_2.getData();
                    var13_15 = var1_1.offsets[var12_12];
                    if (var13_15 != var3_3) ** GOTO lbl103
                    var11_11 = true;
                    ** GOTO lbl239
lbl103: // 1 sources:
                    var1_1.offsets[var12_12] = var3_3;
                    if (var3_3 >= var13_15) ** GOTO lbl107
                    var2_2 = var2_2.next;
                    ** GOTO lbl239
                }
lbl107: // 2 sources:
                case 9: {
                    var7_7.push(var2_2);
                    var8_8.push(var3_3);
                    var2_2 = var2_2.getChild();
                    ** GOTO lbl239
                }
                case 8: 
                case 10: {
                    var7_7.push(var2_2);
                    var8_8.push(var3_3);
                    var2_2 = var2_2.next;
                    ** GOTO lbl239
                }
                case 11: {
                    if (var2_2.size() == 0) {
                        var11_11 = true;
                    } else {
                        var7_7.push(var2_2);
                        var8_8.push(0);
                        var8_8.push(var3_3);
                        var2_2 = var2_2.elementAt(0);
                    }
                    ** GOTO lbl239
                }
                case 15: {
                    var12_12 = var2_2.getData();
                    if (var1_1.match != null) {
                        if (var12_12 > 0) {
                            var8_8.push(var1_1.match.getBeginning(var12_12));
                            var1_1.match.setBeginning(var12_12, var3_3);
                        } else {
                            var13_15 = - var12_12;
                            var8_8.push(var1_1.match.getEnd(var13_15));
                            var1_1.match.setEnd(var13_15, var3_3);
                        }
                        var7_7.push(var2_2);
                        var8_8.push(var3_3);
                    }
                    var2_2 = var2_2.next;
                    ** GOTO lbl239
                }
                case 20: 
                case 21: 
                case 22: 
                case 23: {
                    var7_7.push(var2_2);
                    var8_8.push(var4_4);
                    var8_8.push(var3_3);
                    var4_4 = var2_2.type == 20 || var2_2.type == 21 ? 1 : -1;
                    var2_2 = var2_2.getChild();
                    ** GOTO lbl239
                }
                case 24: {
                    var7_7.push(var2_2);
                    var8_8.push(var3_3);
                    var2_2 = var2_2.getChild();
                    ** GOTO lbl239
                }
                case 25: {
                    var12_12 = var5_5;
                    var12_12 |= var2_2.getData();
                    var7_7.push(var2_2);
                    var8_8.push(var5_5);
                    var8_8.push(var3_3);
                    var5_5 = var12_12 &= ~ var2_2.getData2();
                    var2_2 = var2_2.getChild();
                    ** GOTO lbl239
                }
                case 26: {
                    var12_14 = (Op.ConditionOp)var2_2;
                    if (var12_14.refNumber > 0) {
                        if (var12_14.refNumber >= this.nofparen) {
                            throw new RuntimeException("Internal Error: Reference number must be more than zero: " + var12_14.refNumber);
                        }
                        var2_2 = var1_1.match.getBeginning(var12_14.refNumber) >= 0 && var1_1.match.getEnd(var12_14.refNumber) >= 0 ? var12_14.yes : (var12_14.no != null ? var12_14.no : var12_14.next);
                    } else {
                        var7_7.push(var2_2);
                        var8_8.push(var3_3);
                        var2_2 = var12_14.condition;
                    }
                    ** GOTO lbl239
                }
                default: {
                    throw new RuntimeException("Unknown operation type: " + var2_2.type);
                }
            }
lbl-1000: // 1 sources:
            {
                if (var7_7.isEmpty()) {
                    return var10_10;
                }
                var2_2 = (Op)var7_7.pop();
                var3_3 = var8_8.pop();
                switch (var2_2.type) {
                    case 7: {
                        var1_1.offsets[var2_2.getData()] = var3_3;
                    }
                    case 9: {
                        if (var10_10 >= 0) break;
                        var2_2 = var2_2.next;
                        var11_11 = false;
                        break;
                    }
                    case 8: 
                    case 10: {
                        if (var10_10 >= 0) break;
                        var2_2 = var2_2.getChild();
                        var11_11 = false;
                        break;
                    }
                    case 11: {
                        var12_12 = var8_8.pop();
                        if (var10_10 >= 0) break;
                        if (++var12_12 < var2_2.size()) {
                            var7_7.push(var2_2);
                            var8_8.push(var12_12);
                            var8_8.push(var3_3);
                            var2_2 = var2_2.elementAt(var12_12);
                            var11_11 = false;
                            break;
                        }
                        var10_10 = -1;
                        break;
                    }
                    case 15: {
                        var12_12 = var2_2.getData();
                        var13_15 = var8_8.pop();
                        if (var10_10 >= 0) break;
                        if (var12_12 > 0) {
                            var1_1.match.setBeginning(var12_12, var13_15);
                            break;
                        }
                        var1_1.match.setEnd(- var12_12, var13_15);
                        break;
                    }
                    case 20: 
                    case 22: {
                        var4_4 = var8_8.pop();
                        if (0 <= var10_10) {
                            var2_2 = var2_2.next;
                            var11_11 = false;
                        }
                        var10_10 = -1;
                        break;
                    }
                    case 21: 
                    case 23: {
                        var4_4 = var8_8.pop();
                        if (0 > var10_10) {
                            var2_2 = var2_2.next;
                            var11_11 = false;
                        }
                        var10_10 = -1;
                        break;
                    }
                    case 25: {
                        var5_5 = var8_8.pop();
                    }
                    case 24: {
                        if (var10_10 < 0) break;
                        var3_3 = var10_10;
                        var2_2 = var2_2.next;
                        var11_11 = false;
                        break;
                    }
                    case 26: {
                        var14_16 = (Op.ConditionOp)var2_2;
                        var2_2 = 0 <= var10_10 ? var14_16.yes : (var14_16.no != null ? var14_16.no : var14_16.next);
                        var11_11 = false;
                        break;
                    }
                }
lbl239: // 46 sources:
                ** while (var11_11)
            }
lbl240: // 1 sources:
        } while (true);
    }

    private boolean matchChar(int n2, int n3, boolean bl) {
        return bl ? RegularExpression.matchIgnoreCase(n2, n3) : n2 == n3;
    }

    boolean matchAnchor(ExpressionTarget expressionTarget, Op op, Context context, int n2, int n3) {
        boolean bl = false;
        switch (op.getData()) {
            case 94: {
                if (!(RegularExpression.isSet(n3, 8) ? n2 != context.start && (n2 <= context.start || n2 >= context.limit || !RegularExpression.isEOLChar(expressionTarget.charAt(n2 - 1))) : n2 != context.start)) break;
                return false;
            }
            case 64: {
                if (n2 == context.start || n2 > context.start && RegularExpression.isEOLChar(expressionTarget.charAt(n2 - 1))) break;
                return false;
            }
            case 36: {
                if (!(RegularExpression.isSet(n3, 8) ? n2 != context.limit && (n2 >= context.limit || !RegularExpression.isEOLChar(expressionTarget.charAt(n2))) : !(n2 == context.limit || n2 + 1 == context.limit && RegularExpression.isEOLChar(expressionTarget.charAt(n2)) || n2 + 2 == context.limit && expressionTarget.charAt(n2) == '\r' && expressionTarget.charAt(n2 + 1) == '\n'))) break;
                return false;
            }
            case 65: {
                if (n2 == context.start) break;
                return false;
            }
            case 90: {
                if (n2 == context.limit || n2 + 1 == context.limit && RegularExpression.isEOLChar(expressionTarget.charAt(n2)) || n2 + 2 == context.limit && expressionTarget.charAt(n2) == '\r' && expressionTarget.charAt(n2 + 1) == '\n') break;
                return false;
            }
            case 122: {
                if (n2 == context.limit) break;
                return false;
            }
            case 98: {
                if (context.length == 0) {
                    return false;
                }
                int n4 = RegularExpression.getWordType(expressionTarget, context.start, context.limit, n2, n3);
                if (n4 == 0) {
                    return false;
                }
                int n5 = RegularExpression.getPreviousWordType(expressionTarget, context.start, context.limit, n2, n3);
                if (n4 != n5) break;
                return false;
            }
            case 66: {
                if (context.length == 0) {
                    bl = true;
                } else {
                    int n6 = RegularExpression.getWordType(expressionTarget, context.start, context.limit, n2, n3);
                    boolean bl2 = bl = n6 == 0 || n6 == RegularExpression.getPreviousWordType(expressionTarget, context.start, context.limit, n2, n3);
                }
                if (bl) break;
                return false;
            }
            case 60: {
                if (context.length == 0 || n2 == context.limit) {
                    return false;
                }
                if (RegularExpression.getWordType(expressionTarget, context.start, context.limit, n2, n3) == 1 && RegularExpression.getPreviousWordType(expressionTarget, context.start, context.limit, n2, n3) == 2) break;
                return false;
            }
            case 62: {
                if (context.length == 0 || n2 == context.start) {
                    return false;
                }
                if (RegularExpression.getWordType(expressionTarget, context.start, context.limit, n2, n3) == 2 && RegularExpression.getPreviousWordType(expressionTarget, context.start, context.limit, n2, n3) == 1) break;
                return false;
            }
        }
        return true;
    }

    private static final int getPreviousWordType(ExpressionTarget expressionTarget, int n2, int n3, int n4, int n5) {
        int n6 = RegularExpression.getWordType(expressionTarget, n2, n3, --n4, n5);
        while (n6 == 0) {
            n6 = RegularExpression.getWordType(expressionTarget, n2, n3, --n4, n5);
        }
        return n6;
    }

    private static final int getWordType(ExpressionTarget expressionTarget, int n2, int n3, int n4, int n5) {
        if (n4 < n2 || n4 >= n3) {
            return 2;
        }
        return RegularExpression.getWordType0(expressionTarget.charAt(n4), n5);
    }

    public boolean matches(CharacterIterator characterIterator) {
        return this.matches(characterIterator, (Match)null);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean matches(CharacterIterator var1_1, Match var2_2) {
        block23 : {
            var3_3 = var1_1.getBeginIndex();
            var4_4 = var1_1.getEndIndex();
            var5_5 = this;
            // MONITORENTER : var5_5
            if (this.operations == null) {
                this.prepare();
            }
            if (this.context == null) {
                this.context = new Context();
            }
            // MONITOREXIT : var5_5
            var6_6 = null;
            var7_7 = this.context;
            // MONITORENTER : var7_7
            var6_6 = this.context.inuse != false ? new Context() : this.context;
            var6_6.reset(var1_1, var3_3, var4_4, this.numberOfClosures);
            // MONITOREXIT : var7_7
            if (var2_2 != null) {
                var2_2.setNumberOfGroups(this.nofparen);
                var2_2.setSource(var1_1);
            } else if (this.hasBackReferences) {
                var2_2 = new Match();
                var2_2.setNumberOfGroups(this.nofparen);
            }
            var6_6.match = var2_2;
            if (RegularExpression.isSet(this.options, 512)) {
                var8_8 = this.match(var6_6, this.operations, var6_6.start, 1, this.options);
                if (var8_8 != var6_6.limit) return false;
                if (var6_6.match != null) {
                    var6_6.match.setBeginning(0, var6_6.start);
                    var6_6.match.setEnd(0, var8_8);
                }
                var6_6.setInUse(false);
                return true;
            }
            if (this.fixedStringOnly) {
                var8_9 = this.fixedStringTable.matches(var1_1, var6_6.start, var6_6.limit);
                if (var8_9 < 0) {
                    var6_6.setInUse(false);
                    return false;
                }
                if (var6_6.match != null) {
                    var6_6.match.setBeginning(0, var8_9);
                    var6_6.match.setEnd(0, var8_9 + this.fixedString.length());
                }
                var6_6.setInUse(false);
                return true;
            }
            if (this.fixedString != null && (var8_10 = this.fixedStringTable.matches(var1_1, var6_6.start, var6_6.limit)) < 0) {
                var6_6.setInUse(false);
                return false;
            }
            var8_10 = var6_6.limit - this.minlength;
            var10_11 = -1;
            if (this.operations == null || this.operations.type != 7 || this.operations.getChild().type != 0) ** GOTO lbl55
            if (!RegularExpression.isSet(this.options, 4)) ** GOTO lbl52
            var9_12 = var6_6.start;
            var10_11 = this.match(var6_6, this.operations, var6_6.start, 1, this.options);
            ** GOTO lbl80
lbl52: // 1 sources:
            var11_13 = true;
            var9_12 = var6_6.start;
            ** GOTO lbl68
lbl55: // 1 sources:
            if (this.firstChar == null) ** GOTO lbl59
            var11_14 = this.firstChar;
            var9_12 = var6_6.start;
            ** GOTO lbl75
lbl59: // 1 sources:
            var9_12 = var6_6.start;
            ** GOTO lbl79
lbl-1000: // 1 sources:
            {
                var12_15 = var1_1.setIndex(var9_12);
                if (RegularExpression.isEOLChar(var12_15)) {
                    var11_13 = true;
                } else {
                    if (var11_13 && 0 <= (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) break block23;
                    var11_13 = false;
                }
                ++var9_12;
lbl68: // 2 sources:
                ** while (var9_12 <= var8_10)
            }
lbl69: // 1 sources:
            ** GOTO lbl80
lbl-1000: // 1 sources:
            {
                var12_16 = var1_1.setIndex(var9_12);
                if (REUtil.isHighSurrogate(var12_16) && var9_12 + 1 < var6_6.limit) {
                    var12_16 = REUtil.composeFromSurrogates(var12_16, var1_1.setIndex(var9_12 + 1));
                }
                if (var11_14.match(var12_16) && 0 <= (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) break block23;
                ++var9_12;
lbl75: // 2 sources:
                ** while (var9_12 <= var8_10)
            }
lbl76: // 1 sources:
            ** GOTO lbl80
            while (0 > (var10_11 = this.match(var6_6, this.operations, var9_12, 1, this.options))) {
                ++var9_12;
lbl79: // 2 sources:
                if (var9_12 <= var8_10) continue;
            }
        }
        if (var10_11 < 0) {
            var6_6.setInUse(false);
            return false;
        }
        if (var6_6.match != null) {
            var6_6.match.setBeginning(0, var9_12);
            var6_6.match.setEnd(0, var10_11);
        }
        var6_6.setInUse(false);
        return true;
    }

    void prepare() {
        int n2;
        char[] arrc;
        this.compile(this.tokentree);
        this.minlength = this.tokentree.getMinLength();
        this.firstChar = null;
        if (!RegularExpression.isSet(this.options, 128) && !RegularExpression.isSet(this.options, 512) && (n2 = this.tokentree.analyzeFirstCharacter((RangeToken)(arrc = Token.createRange()), this.options)) == 1) {
            arrc.compactRanges();
            this.firstChar = arrc;
        }
        if (this.operations != null && (this.operations.type == 6 || this.operations.type == 1) && this.operations.next == null) {
            this.fixedStringOnly = true;
            if (this.operations.type == 6) {
                this.fixedString = this.operations.getString();
            } else if (this.operations.getData() >= 65536) {
                this.fixedString = REUtil.decomposeToSurrogates(this.operations.getData());
            } else {
                arrc = new char[]{(char)this.operations.getData()};
                this.fixedString = new String(arrc);
            }
            this.fixedStringOptions = this.options;
            this.fixedStringTable = new BMPattern(this.fixedString, 256, RegularExpression.isSet(this.fixedStringOptions, 2));
        } else if (!RegularExpression.isSet(this.options, 256) && !RegularExpression.isSet(this.options, 512)) {
            arrc = new Token.FixedStringContainer();
            this.tokentree.findFixedString((Token.FixedStringContainer)arrc, this.options);
            this.fixedString = arrc.token == null ? null : arrc.token.getString();
            this.fixedStringOptions = arrc.options;
            if (this.fixedString != null && this.fixedString.length() < 2) {
                this.fixedString = null;
            }
            if (this.fixedString != null) {
                this.fixedStringTable = new BMPattern(this.fixedString, 256, RegularExpression.isSet(this.fixedStringOptions, 2));
            }
        }
    }

    private static final boolean isSet(int n2, int n3) {
        return (n2 & n3) == n3;
    }

    public RegularExpression(String string) throws ParseException {
        this(string, null);
    }

    public RegularExpression(String string, String string2) throws ParseException {
        this.setPattern(string, string2);
    }

    public RegularExpression(String string, String string2, Locale locale) throws ParseException {
        this.setPattern(string, string2, locale);
    }

    RegularExpression(String string, Token token, int n2, boolean bl, int n3) {
        this.regex = string;
        this.tokentree = token;
        this.nofparen = n2;
        this.options = n3;
        this.hasBackReferences = bl;
    }

    public void setPattern(String string) throws ParseException {
        this.setPattern(string, Locale.getDefault());
    }

    public void setPattern(String string, Locale locale) throws ParseException {
        this.setPattern(string, this.options, locale);
    }

    private void setPattern(String string, int n2, Locale locale) throws ParseException {
        this.regex = string;
        this.options = n2;
        RegexParser regexParser = RegularExpression.isSet(this.options, 512) ? new ParserForXMLSchema(locale) : new RegexParser(locale);
        this.tokentree = regexParser.parse(this.regex, this.options);
        this.nofparen = regexParser.parennumber;
        this.hasBackReferences = regexParser.hasBackReferences;
        this.operations = null;
        this.context = null;
    }

    public void setPattern(String string, String string2) throws ParseException {
        this.setPattern(string, string2, Locale.getDefault());
    }

    public void setPattern(String string, String string2, Locale locale) throws ParseException {
        this.setPattern(string, REUtil.parseOptions(string2), locale);
    }

    public String getPattern() {
        return this.regex;
    }

    public String toString() {
        return this.tokentree.toString(this.options);
    }

    public String getOptions() {
        return REUtil.createOptionString(this.options);
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof RegularExpression)) {
            return false;
        }
        RegularExpression regularExpression = (RegularExpression)object;
        return this.regex.equals(regularExpression.regex) && this.options == regularExpression.options;
    }

    boolean equals(String string, int n2) {
        return this.regex.equals(string) && this.options == n2;
    }

    public int hashCode() {
        return (this.regex + "/" + this.getOptions()).hashCode();
    }

    public int getNumberOfGroups() {
        return this.nofparen;
    }

    private static final int getWordType0(char c2, int n2) {
        if (!RegularExpression.isSet(n2, 64)) {
            if (RegularExpression.isSet(n2, 32)) {
                return Token.getRange("IsWord", true).match(c2) ? 1 : 2;
            }
            return RegularExpression.isWordChar(c2) ? 1 : 2;
        }
        switch (Character.getType(c2)) {
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 8: 
            case 9: 
            case 10: 
            case 11: {
                return 1;
            }
            case 6: 
            case 7: 
            case 16: {
                return 0;
            }
            case 15: {
                switch (c2) {
                    case '\t': 
                    case '\n': 
                    case '\u000b': 
                    case '\f': 
                    case '\r': {
                        return 2;
                    }
                }
                return 0;
            }
        }
        return 2;
    }

    private static final boolean isEOLChar(int n2) {
        return n2 == 10 || n2 == 13 || n2 == 8232 || n2 == 8233;
    }

    private static final boolean isWordChar(int n2) {
        if (n2 == 95) {
            return true;
        }
        if (n2 < 48) {
            return false;
        }
        if (n2 > 122) {
            return false;
        }
        if (n2 <= 57) {
            return true;
        }
        if (n2 < 65) {
            return false;
        }
        if (n2 <= 90) {
            return true;
        }
        if (n2 < 97) {
            return false;
        }
        return true;
    }

    private static final boolean matchIgnoreCase(int n2, int n3) {
        char c2;
        if (n2 == n3) {
            return true;
        }
        if (n2 > 65535 || n3 > 65535) {
            return false;
        }
        char c3 = Character.toUpperCase((char)n2);
        if (c3 == (c2 = Character.toUpperCase((char)n3))) {
            return true;
        }
        return Character.toLowerCase(c3) == Character.toLowerCase(c2);
    }

    static final class Context {
        int start;
        int limit;
        int length;
        Match match;
        boolean inuse = false;
        int[] offsets;
        private StringTarget stringTarget;
        private CharArrayTarget charArrayTarget;
        private CharacterIteratorTarget characterIteratorTarget;
        ExpressionTarget target;

        Context() {
        }

        private void resetCommon(int n2) {
            this.length = this.limit - this.start;
            this.setInUse(true);
            this.match = null;
            if (this.offsets == null || this.offsets.length != n2) {
                this.offsets = new int[n2];
            }
            int n3 = 0;
            while (n3 < n2) {
                this.offsets[n3] = -1;
                ++n3;
            }
        }

        void reset(CharacterIterator characterIterator, int n2, int n3, int n4) {
            if (this.characterIteratorTarget == null) {
                this.characterIteratorTarget = new CharacterIteratorTarget(characterIterator);
            } else {
                this.characterIteratorTarget.resetTarget(characterIterator);
            }
            this.target = this.characterIteratorTarget;
            this.start = n2;
            this.limit = n3;
            this.resetCommon(n4);
        }

        void reset(String string, int n2, int n3, int n4) {
            if (this.stringTarget == null) {
                this.stringTarget = new StringTarget(string);
            } else {
                this.stringTarget.resetTarget(string);
            }
            this.target = this.stringTarget;
            this.start = n2;
            this.limit = n3;
            this.resetCommon(n4);
        }

        void reset(char[] arrc, int n2, int n3, int n4) {
            if (this.charArrayTarget == null) {
                this.charArrayTarget = new CharArrayTarget(arrc);
            } else {
                this.charArrayTarget.resetTarget(arrc);
            }
            this.target = this.charArrayTarget;
            this.start = n2;
            this.limit = n3;
            this.resetCommon(n4);
        }

        synchronized void setInUse(boolean bl) {
            this.inuse = bl;
        }
    }

    static final class CharacterIteratorTarget
    extends ExpressionTarget {
        CharacterIterator target;

        CharacterIteratorTarget(CharacterIterator characterIterator) {
            this.target = characterIterator;
        }

        final void resetTarget(CharacterIterator characterIterator) {
            this.target = characterIterator;
        }

        final char charAt(int n2) {
            return this.target.setIndex(n2);
        }

        final boolean regionMatches(boolean bl, int n2, int n3, String string, int n4) {
            if (n2 < 0 || n3 - n2 < n4) {
                return false;
            }
            return bl ? this.regionMatchesIgnoreCase(n2, n3, string, n4) : this.regionMatches(n2, n3, string, n4);
        }

        private final boolean regionMatches(int n2, int n3, String string, int n4) {
            int n5 = 0;
            while (n4-- > 0) {
                if (this.target.setIndex(n2++) == string.charAt(n5++)) continue;
                return false;
            }
            return true;
        }

        private final boolean regionMatchesIgnoreCase(int n2, int n3, String string, int n4) {
            int n5 = 0;
            while (n4-- > 0) {
                char c2;
                char c3;
                char c4;
                char c5;
                if ((c3 = this.target.setIndex(n2++)) == (c5 = string.charAt(n5++)) || (c2 = Character.toUpperCase(c3)) == (c4 = Character.toUpperCase(c5)) || Character.toLowerCase(c2) == Character.toLowerCase(c4)) continue;
                return false;
            }
            return true;
        }

        final boolean regionMatches(boolean bl, int n2, int n3, int n4, int n5) {
            if (n2 < 0 || n3 - n2 < n5) {
                return false;
            }
            return bl ? this.regionMatchesIgnoreCase(n2, n3, n4, n5) : this.regionMatches(n2, n3, n4, n5);
        }

        private final boolean regionMatches(int n2, int n3, int n4, int n5) {
            int n6 = n4;
            while (n5-- > 0) {
                if (this.target.setIndex(n2++) == this.target.setIndex(n6++)) continue;
                return false;
            }
            return true;
        }

        private final boolean regionMatchesIgnoreCase(int n2, int n3, int n4, int n5) {
            int n6 = n4;
            while (n5-- > 0) {
                char c2;
                char c3;
                char c4;
                char c5;
                if ((c3 = this.target.setIndex(n2++)) == (c5 = this.target.setIndex(n6++)) || (c2 = Character.toUpperCase(c3)) == (c4 = Character.toUpperCase(c5)) || Character.toLowerCase(c2) == Character.toLowerCase(c4)) continue;
                return false;
            }
            return true;
        }
    }

    static final class CharArrayTarget
    extends ExpressionTarget {
        char[] target;

        CharArrayTarget(char[] arrc) {
            this.target = arrc;
        }

        final void resetTarget(char[] arrc) {
            this.target = arrc;
        }

        char charAt(int n2) {
            return this.target[n2];
        }

        final boolean regionMatches(boolean bl, int n2, int n3, String string, int n4) {
            if (n2 < 0 || n3 - n2 < n4) {
                return false;
            }
            return bl ? this.regionMatchesIgnoreCase(n2, n3, string, n4) : this.regionMatches(n2, n3, string, n4);
        }

        private final boolean regionMatches(int n2, int n3, String string, int n4) {
            int n5 = 0;
            while (n4-- > 0) {
                if (this.target[n2++] == string.charAt(n5++)) continue;
                return false;
            }
            return true;
        }

        private final boolean regionMatchesIgnoreCase(int n2, int n3, String string, int n4) {
            int n5 = 0;
            while (n4-- > 0) {
                char c2;
                char c3;
                char c4;
                char c5;
                if ((c3 = this.target[n2++]) == (c5 = string.charAt(n5++)) || (c2 = Character.toUpperCase(c3)) == (c4 = Character.toUpperCase(c5)) || Character.toLowerCase(c2) == Character.toLowerCase(c4)) continue;
                return false;
            }
            return true;
        }

        final boolean regionMatches(boolean bl, int n2, int n3, int n4, int n5) {
            if (n2 < 0 || n3 - n2 < n5) {
                return false;
            }
            return bl ? this.regionMatchesIgnoreCase(n2, n3, n4, n5) : this.regionMatches(n2, n3, n4, n5);
        }

        private final boolean regionMatches(int n2, int n3, int n4, int n5) {
            int n6 = n4;
            while (n5-- > 0) {
                if (this.target[n2++] == this.target[n6++]) continue;
                return false;
            }
            return true;
        }

        private final boolean regionMatchesIgnoreCase(int n2, int n3, int n4, int n5) {
            int n6 = n4;
            while (n5-- > 0) {
                char c2;
                char c3;
                char c4;
                char c5;
                if ((c3 = this.target[n2++]) == (c5 = this.target[n6++]) || (c2 = Character.toUpperCase(c3)) == (c4 = Character.toUpperCase(c5)) || Character.toLowerCase(c2) == Character.toLowerCase(c4)) continue;
                return false;
            }
            return true;
        }
    }

    static final class StringTarget
    extends ExpressionTarget {
        private String target;

        StringTarget(String string) {
            this.target = string;
        }

        final void resetTarget(String string) {
            this.target = string;
        }

        final char charAt(int n2) {
            return this.target.charAt(n2);
        }

        final boolean regionMatches(boolean bl, int n2, int n3, String string, int n4) {
            if (n3 - n2 < n4) {
                return false;
            }
            return bl ? this.target.regionMatches(true, n2, string, 0, n4) : this.target.regionMatches(n2, string, 0, n4);
        }

        final boolean regionMatches(boolean bl, int n2, int n3, int n4, int n5) {
            if (n3 - n2 < n5) {
                return false;
            }
            return bl ? this.target.regionMatches(true, n2, this.target, n4, n5) : this.target.regionMatches(n2, this.target, n4, n5);
        }
    }

    static abstract class ExpressionTarget {
        ExpressionTarget() {
        }

        abstract char charAt(int var1);

        abstract boolean regionMatches(boolean var1, int var2, int var3, String var4, int var5);

        abstract boolean regionMatches(boolean var1, int var2, int var3, int var4, int var5);
    }

}

