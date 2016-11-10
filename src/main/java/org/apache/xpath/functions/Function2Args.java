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
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.functions.WrongNumberArgsException;

public class Function2Args
extends FunctionOneArg {
    static final long serialVersionUID = 5574294996842710641L;
    Expression m_arg1;

    public Expression getArg1() {
        return this.m_arg1;
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        if (null != this.m_arg1) {
            this.m_arg1.fixupVariables(vector, n2);
        }
    }

    public void setArg(Expression expression, int n2) throws WrongNumberArgsException {
        if (n2 == 0) {
            super.setArg(expression, n2);
        } else if (1 == n2) {
            this.m_arg1 = expression;
            expression.exprSetParent(this);
        } else {
            this.reportWrongNumberArgs();
        }
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
        if (n2 != 2) {
            this.reportWrongNumberArgs();
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("two", null));
    }

    public boolean canTraverseOutsideSubtree() {
        return super.canTraverseOutsideSubtree() ? true : this.m_arg1.canTraverseOutsideSubtree();
    }

    public void callArgVisitors(XPathVisitor xPathVisitor) {
        super.callArgVisitors(xPathVisitor);
        if (null != this.m_arg1) {
            this.m_arg1.callVisitors(new Arg1Owner(this), xPathVisitor);
        }
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        if (null != this.m_arg1) {
            if (null == ((Function2Args)expression).m_arg1) {
                return false;
            }
            if (!this.m_arg1.deepEquals(((Function2Args)expression).m_arg1)) {
                return false;
            }
        } else if (null != ((Function2Args)expression).m_arg1) {
            return false;
        }
        return true;
    }

    class Arg1Owner
    implements ExpressionOwner {
        private final Function2Args this$0;

        Arg1Owner(Function2Args function2Args) {
            this.this$0 = function2Args;
        }

        public Expression getExpression() {
            return this.this$0.m_arg1;
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            this.this$0.m_arg1 = expression;
        }
    }

}

