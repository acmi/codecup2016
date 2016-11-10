/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XObject;

public class Operation
extends Expression
implements ExpressionOwner {
    protected Expression m_left;
    protected Expression m_right;

    public void fixupVariables(Vector vector, int n2) {
        this.m_left.fixupVariables(vector, n2);
        this.m_right.fixupVariables(vector, n2);
    }

    public boolean canTraverseOutsideSubtree() {
        if (null != this.m_left && this.m_left.canTraverseOutsideSubtree()) {
            return true;
        }
        if (null != this.m_right && this.m_right.canTraverseOutsideSubtree()) {
            return true;
        }
        return false;
    }

    public void setLeftRight(Expression expression, Expression expression2) {
        this.m_left = expression;
        this.m_right = expression2;
        expression.exprSetParent(this);
        expression2.exprSetParent(this);
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XObject xObject = this.m_left.execute(xPathContext, true);
        XObject xObject2 = this.m_right.execute(xPathContext, true);
        XObject xObject3 = this.operate(xObject, xObject2);
        xObject.detach();
        xObject2.detach();
        return xObject3;
    }

    public XObject operate(XObject xObject, XObject xObject2) throws TransformerException {
        return null;
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        if (xPathVisitor.visitBinaryOperation(expressionOwner, this)) {
            this.m_left.callVisitors(new LeftExprOwner(this), xPathVisitor);
            this.m_right.callVisitors(this, xPathVisitor);
        }
    }

    public Expression getExpression() {
        return this.m_right;
    }

    public void setExpression(Expression expression) {
        expression.exprSetParent(this);
        this.m_right = expression;
    }

    public boolean deepEquals(Expression expression) {
        if (!this.isSameClass(expression)) {
            return false;
        }
        if (!this.m_left.deepEquals(((Operation)expression).m_left)) {
            return false;
        }
        if (!this.m_right.deepEquals(((Operation)expression).m_right)) {
            return false;
        }
        return true;
    }

    class LeftExprOwner
    implements ExpressionOwner {
        private final Operation this$0;

        LeftExprOwner(Operation operation) {
            this.this$0 = operation;
        }

        public Expression getExpression() {
            return this.this$0.m_left;
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            this.this$0.m_left = expression;
        }
    }

}

