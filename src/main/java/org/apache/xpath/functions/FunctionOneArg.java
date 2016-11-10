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
import org.apache.xpath.functions.Function;
import org.apache.xpath.functions.WrongNumberArgsException;

public class FunctionOneArg
extends Function
implements ExpressionOwner {
    static final long serialVersionUID = -5180174180765609758L;
    Expression m_arg0;

    public Expression getArg0() {
        return this.m_arg0;
    }

    public void setArg(Expression expression, int n2) throws WrongNumberArgsException {
        if (0 == n2) {
            this.m_arg0 = expression;
            expression.exprSetParent(this);
        } else {
            this.reportWrongNumberArgs();
        }
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
        if (n2 != 1) {
            this.reportWrongNumberArgs();
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("one", null));
    }

    public boolean canTraverseOutsideSubtree() {
        return this.m_arg0.canTraverseOutsideSubtree();
    }

    public void fixupVariables(Vector vector, int n2) {
        if (null != this.m_arg0) {
            this.m_arg0.fixupVariables(vector, n2);
        }
    }

    public void callArgVisitors(XPathVisitor xPathVisitor) {
        if (null != this.m_arg0) {
            this.m_arg0.callVisitors(this, xPathVisitor);
        }
    }

    public Expression getExpression() {
        return this.m_arg0;
    }

    public void setExpression(Expression expression) {
        expression.exprSetParent(this);
        this.m_arg0 = expression;
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        if (null != this.m_arg0) {
            if (null == ((FunctionOneArg)expression).m_arg0) {
                return false;
            }
            if (!this.m_arg0.deepEquals(((FunctionOneArg)expression).m_arg0)) {
                return false;
            }
        } else if (null != ((FunctionOneArg)expression).m_arg0) {
            return false;
        }
        return true;
    }
}

