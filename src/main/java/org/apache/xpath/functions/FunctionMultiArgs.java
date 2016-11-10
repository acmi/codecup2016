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
import org.apache.xpath.functions.Function3Args;
import org.apache.xpath.functions.WrongNumberArgsException;

public class FunctionMultiArgs
extends Function3Args {
    Expression[] m_args;

    public void setArg(Expression expression, int n2) throws WrongNumberArgsException {
        if (n2 < 3) {
            super.setArg(expression, n2);
        } else {
            if (null == this.m_args) {
                this.m_args = new Expression[1];
                this.m_args[0] = expression;
            } else {
                Expression[] arrexpression = new Expression[this.m_args.length + 1];
                System.arraycopy(this.m_args, 0, arrexpression, 0, this.m_args.length);
                arrexpression[this.m_args.length] = expression;
                this.m_args = arrexpression;
            }
            expression.exprSetParent(this);
        }
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        if (null != this.m_args) {
            for (int i2 = 0; i2 < this.m_args.length; ++i2) {
                this.m_args[i2].fixupVariables(vector, n2);
            }
        }
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        String string = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{"Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called."});
        throw new RuntimeException(string);
    }

    public boolean canTraverseOutsideSubtree() {
        if (super.canTraverseOutsideSubtree()) {
            return true;
        }
        int n2 = this.m_args.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!this.m_args[i2].canTraverseOutsideSubtree()) continue;
            return true;
        }
        return false;
    }

    public void callArgVisitors(XPathVisitor xPathVisitor) {
        super.callArgVisitors(xPathVisitor);
        if (null != this.m_args) {
            int n2 = this.m_args.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                this.m_args[i2].callVisitors(new ArgMultiOwner(this, i2), xPathVisitor);
            }
        }
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        FunctionMultiArgs functionMultiArgs = (FunctionMultiArgs)expression;
        if (null != this.m_args) {
            int n2 = this.m_args.length;
            if (null == functionMultiArgs || functionMultiArgs.m_args.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.m_args[i2].deepEquals(functionMultiArgs.m_args[i2])) continue;
                return false;
            }
        } else if (null != functionMultiArgs.m_args) {
            return false;
        }
        return true;
    }

    class ArgMultiOwner
    implements ExpressionOwner {
        int m_argIndex;
        private final FunctionMultiArgs this$0;

        ArgMultiOwner(FunctionMultiArgs functionMultiArgs, int n2) {
            this.this$0 = functionMultiArgs;
            this.m_argIndex = n2;
        }

        public Expression getExpression() {
            return this.this$0.m_args[this.m_argIndex];
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            this.this$0.m_args[this.m_argIndex] = expression;
        }
    }

}

