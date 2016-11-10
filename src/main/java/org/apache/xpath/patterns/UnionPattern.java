/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.patterns;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;
import org.apache.xpath.patterns.StepPattern;

public class UnionPattern
extends Expression {
    private StepPattern[] m_patterns;

    public void fixupVariables(Vector vector, int n2) {
        for (int i2 = 0; i2 < this.m_patterns.length; ++i2) {
            this.m_patterns[i2].fixupVariables(vector, n2);
        }
    }

    public boolean canTraverseOutsideSubtree() {
        if (null != this.m_patterns) {
            int n2 = this.m_patterns.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                if (!this.m_patterns[i2].canTraverseOutsideSubtree()) continue;
                return true;
            }
        }
        return false;
    }

    public void setPatterns(StepPattern[] arrstepPattern) {
        this.m_patterns = arrstepPattern;
        if (null != arrstepPattern) {
            for (int i2 = 0; i2 < arrstepPattern.length; ++i2) {
                arrstepPattern[i2].exprSetParent(this);
            }
        }
    }

    public StepPattern[] getPatterns() {
        return this.m_patterns;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XObject xObject = null;
        int n2 = this.m_patterns.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            XObject xObject2 = this.m_patterns[i2].execute(xPathContext);
            if (xObject2 == NodeTest.SCORE_NONE) continue;
            if (null == xObject) {
                xObject = xObject2;
                continue;
            }
            if (xObject2.num() <= xObject.num()) continue;
            xObject = xObject2;
        }
        if (null == xObject) {
            xObject = NodeTest.SCORE_NONE;
        }
        return xObject;
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        xPathVisitor.visitUnionPattern(expressionOwner, this);
        if (null != this.m_patterns) {
            int n2 = this.m_patterns.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                this.m_patterns[i2].callVisitors(new UnionPathPartOwner(this, i2), xPathVisitor);
            }
        }
    }

    public boolean deepEquals(Expression expression) {
        if (!this.isSameClass(expression)) {
            return false;
        }
        UnionPattern unionPattern = (UnionPattern)expression;
        if (null != this.m_patterns) {
            int n2 = this.m_patterns.length;
            if (null == unionPattern.m_patterns || unionPattern.m_patterns.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.m_patterns[i2].deepEquals(unionPattern.m_patterns[i2])) continue;
                return false;
            }
        } else if (unionPattern.m_patterns != null) {
            return false;
        }
        return true;
    }

    static StepPattern[] access$000(UnionPattern unionPattern) {
        return unionPattern.m_patterns;
    }

    class UnionPathPartOwner
    implements ExpressionOwner {
        int m_index;
        private final UnionPattern this$0;

        UnionPathPartOwner(UnionPattern unionPattern, int n2) {
            this.this$0 = unionPattern;
            this.m_index = n2;
        }

        public Expression getExpression() {
            return UnionPattern.access$000(this.this$0)[this.m_index];
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            UnionPattern.access$000((UnionPattern)this.this$0)[this.m_index] = (StepPattern)expression;
        }
    }

}

