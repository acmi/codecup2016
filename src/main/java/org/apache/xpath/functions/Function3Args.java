/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.Vector;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.functions.Function2Args;
import org.apache.xpath.functions.WrongNumberArgsException;

public class Function3Args
extends Function2Args {
    static final long serialVersionUID = 7915240747161506646L;
    Expression m_arg2;

    public Expression getArg2() {
        return this.m_arg2;
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        if (null != this.m_arg2) {
            this.m_arg2.fixupVariables(vector, n2);
        }
    }

    public void setArg(Expression expression, int n2) throws WrongNumberArgsException {
        if (n2 < 2) {
            super.setArg(expression, n2);
        } else if (2 == n2) {
            this.m_arg2 = expression;
            expression.exprSetParent(this);
        } else {
            this.reportWrongNumberArgs();
        }
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
        if (n2 != 3) {
            this.reportWrongNumberArgs();
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("three", null));
    }

    public boolean canTraverseOutsideSubtree() {
        return super.canTraverseOutsideSubtree() ? true : this.m_arg2.canTraverseOutsideSubtree();
    }

    public void callArgVisitors(XPathVisitor xPathVisitor) {
        super.callArgVisitors(xPathVisitor);
        if (null != this.m_arg2) {
            this.m_arg2.callVisitors(new Arg2Owner(this), xPathVisitor);
        }
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        if (null != this.m_arg2) {
            if (null == ((Function3Args)expression).m_arg2) {
                return false;
            }
            if (!this.m_arg2.deepEquals(((Function3Args)expression).m_arg2)) {
                return false;
            }
        } else if (null != ((Function3Args)expression).m_arg2) {
            return false;
        }
        return true;
    }

    class Arg2Owner
    implements ExpressionOwner {
        private final Function3Args this$0;

        Arg2Owner(Function3Args function3Args) {
            this.this$0 = function3Args;
        }

        public Expression getExpression() {
            return this.this$0.m_arg2;
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            this.this$0.m_arg2 = expression;
        }
    }

}

