/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.patterns;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.StepPattern;

public class FunctionPattern
extends StepPattern {
    Expression m_functionExpr;

    public FunctionPattern(Expression expression, int n2, int n3) {
        super(0, null, null, n2, n3);
        this.m_functionExpr = expression;
    }

    public final void calcScore() {
        this.m_score = SCORE_OTHER;
        if (null == this.m_targetString) {
            this.calcTargetString();
        }
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        this.m_functionExpr.fixupVariables(vector, n2);
    }

    public XObject execute(XPathContext xPathContext, int n2) throws TransformerException {
        DTMIterator dTMIterator = this.m_functionExpr.asIterator(xPathContext, n2);
        XNumber xNumber = SCORE_NONE;
        if (null != dTMIterator) {
            int n3;
            while (-1 != (n3 = dTMIterator.nextNode())) {
                xNumber = n3 == n2 ? SCORE_OTHER : SCORE_NONE;
                if (xNumber != SCORE_OTHER) continue;
                n2 = n3;
                break;
            }
        }
        dTMIterator.detach();
        return xNumber;
    }

    public XObject execute(XPathContext xPathContext, int n2, DTM dTM, int n3) throws TransformerException {
        DTMIterator dTMIterator = this.m_functionExpr.asIterator(xPathContext, n2);
        XNumber xNumber = SCORE_NONE;
        if (null != dTMIterator) {
            int n4;
            while (-1 != (n4 = dTMIterator.nextNode())) {
                xNumber = n4 == n2 ? SCORE_OTHER : SCORE_NONE;
                if (xNumber != SCORE_OTHER) continue;
                n2 = n4;
                break;
            }
            dTMIterator.detach();
        }
        return xNumber;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        int n2 = xPathContext.getCurrentNode();
        DTMIterator dTMIterator = this.m_functionExpr.asIterator(xPathContext, n2);
        XNumber xNumber = SCORE_NONE;
        if (null != dTMIterator) {
            int n3;
            while (-1 != (n3 = dTMIterator.nextNode())) {
                xNumber = n3 == n2 ? SCORE_OTHER : SCORE_NONE;
                if (xNumber != SCORE_OTHER) continue;
                n2 = n3;
                break;
            }
            dTMIterator.detach();
        }
        return xNumber;
    }

    protected void callSubtreeVisitors(XPathVisitor xPathVisitor) {
        this.m_functionExpr.callVisitors(new FunctionOwner(this), xPathVisitor);
        super.callSubtreeVisitors(xPathVisitor);
    }

    class FunctionOwner
    implements ExpressionOwner {
        private final FunctionPattern this$0;

        FunctionOwner(FunctionPattern functionPattern) {
            this.this$0 = functionPattern;
        }

        public Expression getExpression() {
            return this.this$0.m_functionExpr;
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            this.this$0.m_functionExpr = expression;
        }
    }

}

