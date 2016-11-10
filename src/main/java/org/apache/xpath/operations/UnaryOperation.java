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

public abstract class UnaryOperation
extends Expression
implements ExpressionOwner {
    protected Expression m_right;

    public void fixupVariables(Vector vector, int n2) {
        this.m_right.fixupVariables(vector, n2);
    }

    public boolean canTraverseOutsideSubtree() {
        if (null != this.m_right && this.m_right.canTraverseOutsideSubtree()) {
            return true;
        }
        return false;
    }

    public void setRight(Expression expression) {
        this.m_right = expression;
        expression.exprSetParent(this);
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        return this.operate(this.m_right.execute(xPathContext));
    }

    public abstract XObject operate(XObject var1) throws TransformerException;

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        if (xPathVisitor.visitUnaryOperation(expressionOwner, this)) {
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
        if (!this.m_right.deepEquals(((UnaryOperation)expression).m_right)) {
            return false;
        }
        return true;
    }
}

