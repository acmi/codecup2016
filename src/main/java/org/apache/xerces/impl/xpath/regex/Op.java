/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

import java.util.Vector;
import org.apache.xerces.impl.xpath.regex.RangeToken;
import org.apache.xerces.impl.xpath.regex.Token;

class Op {
    static final int DOT = 0;
    static final int CHAR = 1;
    static final int RANGE = 3;
    static final int NRANGE = 4;
    static final int ANCHOR = 5;
    static final int STRING = 6;
    static final int CLOSURE = 7;
    static final int NONGREEDYCLOSURE = 8;
    static final int QUESTION = 9;
    static final int NONGREEDYQUESTION = 10;
    static final int UNION = 11;
    static final int CAPTURE = 15;
    static final int BACKREFERENCE = 16;
    static final int LOOKAHEAD = 20;
    static final int NEGATIVELOOKAHEAD = 21;
    static final int LOOKBEHIND = 22;
    static final int NEGATIVELOOKBEHIND = 23;
    static final int INDEPENDENT = 24;
    static final int MODIFIER = 25;
    static final int CONDITION = 26;
    static int nofinstances = 0;
    static final boolean COUNT = false;
    final int type;
    Op next = null;

    static Op createDot() {
        return new Op(0);
    }

    static CharOp createChar(int n2) {
        return new CharOp(1, n2);
    }

    static CharOp createAnchor(int n2) {
        return new CharOp(5, n2);
    }

    static CharOp createCapture(int n2, Op op) {
        CharOp charOp = new CharOp(15, n2);
        charOp.next = op;
        return charOp;
    }

    static UnionOp createUnion(int n2) {
        return new UnionOp(11, n2);
    }

    static ChildOp createClosure(int n2) {
        return new ModifierOp(7, n2, -1);
    }

    static ChildOp createNonGreedyClosure() {
        return new ChildOp(8);
    }

    static ChildOp createQuestion(boolean bl) {
        return new ChildOp(bl ? 10 : 9);
    }

    static RangeOp createRange(Token token) {
        return new RangeOp(3, token);
    }

    static ChildOp createLook(int n2, Op op, Op op2) {
        ChildOp childOp = new ChildOp(n2);
        childOp.setChild(op2);
        childOp.next = op;
        return childOp;
    }

    static CharOp createBackReference(int n2) {
        return new CharOp(16, n2);
    }

    static StringOp createString(String string) {
        return new StringOp(6, string);
    }

    static ChildOp createIndependent(Op op, Op op2) {
        ChildOp childOp = new ChildOp(24);
        childOp.setChild(op2);
        childOp.next = op;
        return childOp;
    }

    static ModifierOp createModifier(Op op, Op op2, int n2, int n3) {
        ModifierOp modifierOp = new ModifierOp(25, n2, n3);
        modifierOp.setChild(op2);
        modifierOp.next = op;
        return modifierOp;
    }

    static ConditionOp createCondition(Op op, int n2, Op op2, Op op3, Op op4) {
        ConditionOp conditionOp = new ConditionOp(26, n2, op2, op3, op4);
        conditionOp.next = op;
        return conditionOp;
    }

    protected Op(int n2) {
        this.type = n2;
    }

    int size() {
        return 0;
    }

    Op elementAt(int n2) {
        throw new RuntimeException("Internal Error: type=" + this.type);
    }

    Op getChild() {
        throw new RuntimeException("Internal Error: type=" + this.type);
    }

    int getData() {
        throw new RuntimeException("Internal Error: type=" + this.type);
    }

    int getData2() {
        throw new RuntimeException("Internal Error: type=" + this.type);
    }

    RangeToken getToken() {
        throw new RuntimeException("Internal Error: type=" + this.type);
    }

    String getString() {
        throw new RuntimeException("Internal Error: type=" + this.type);
    }

    static class ConditionOp
    extends Op {
        final int refNumber;
        final Op condition;
        final Op yes;
        final Op no;

        ConditionOp(int n2, int n3, Op op, Op op2, Op op3) {
            super(n2);
            this.refNumber = n3;
            this.condition = op;
            this.yes = op2;
            this.no = op3;
        }
    }

    static class StringOp
    extends Op {
        final String string;

        StringOp(int n2, String string) {
            super(n2);
            this.string = string;
        }

        String getString() {
            return this.string;
        }
    }

    static class RangeOp
    extends Op {
        final Token tok;

        RangeOp(int n2, Token token) {
            super(n2);
            this.tok = token;
        }

        RangeToken getToken() {
            return (RangeToken)this.tok;
        }
    }

    static class ModifierOp
    extends ChildOp {
        final int v1;
        final int v2;

        ModifierOp(int n2, int n3, int n4) {
            super(n2);
            this.v1 = n3;
            this.v2 = n4;
        }

        int getData() {
            return this.v1;
        }

        int getData2() {
            return this.v2;
        }
    }

    static class ChildOp
    extends Op {
        Op child;

        ChildOp(int n2) {
            super(n2);
        }

        void setChild(Op op) {
            this.child = op;
        }

        Op getChild() {
            return this.child;
        }
    }

    static class UnionOp
    extends Op {
        final Vector branches;

        UnionOp(int n2, int n3) {
            super(n2);
            this.branches = new Vector(n3);
        }

        void addElement(Op op) {
            this.branches.addElement(op);
        }

        int size() {
            return this.branches.size();
        }

        Op elementAt(int n2) {
            return (Op)this.branches.elementAt(n2);
        }
    }

    static class CharOp
    extends Op {
        final int charData;

        CharOp(int n2, int n3) {
            super(n2);
            this.charData = n3;
        }

        int getData() {
            return this.charData;
        }
    }

}

