/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class FilterExprIteratorSimple
extends LocPathIterator {
    private Expression m_expr;
    private transient XNodeSet m_exprObj;
    private boolean m_mustHardReset = false;
    private boolean m_canDetachNodeset = true;

    public FilterExprIteratorSimple() {
        super(null);
    }

    public FilterExprIteratorSimple(Expression expression) {
        super(null);
        this.m_expr = expression;
    }

    public void setRoot(int n2, Object object) {
        super.setRoot(n2, object);
        this.m_exprObj = FilterExprIteratorSimple.executeFilterExpr(n2, this.m_execContext, this.getPrefixResolver(), this.getIsTopLevel(), this.m_stackFrame, this.m_expr);
    }

    public static XNodeSet executeFilterExpr(int n2, XPathContext xPathContext, PrefixResolver prefixResolver, boolean bl, int n3, Expression expression) throws WrappedRuntimeException {
        XNodeSet xNodeSet;
        PrefixResolver prefixResolver2 = xPathContext.getNamespaceContext();
        xNodeSet = null;
        try {
            block5 : {
                try {
                    xPathContext.pushCurrentNode(n2);
                    xPathContext.setNamespaceContext(prefixResolver);
                    if (bl) {
                        VariableStack variableStack = xPathContext.getVarStack();
                        int n4 = variableStack.getStackFrame();
                        variableStack.setStackFrame(n3);
                        xNodeSet = (XNodeSet)expression.execute(xPathContext);
                        xNodeSet.setShouldCacheNodes(true);
                        variableStack.setStackFrame(n4);
                        break block5;
                    }
                    xNodeSet = (XNodeSet)expression.execute(xPathContext);
                }
                catch (TransformerException transformerException) {
                    throw new WrappedRuntimeException(transformerException);
                }
            }
            Object var11_11 = null;
            xPathContext.popCurrentNode();
            xPathContext.setNamespaceContext(prefixResolver2);
        }
        catch (Throwable throwable) {
            Object var11_12 = null;
            xPathContext.popCurrentNode();
            xPathContext.setNamespaceContext(prefixResolver2);
            throw throwable;
        }
        return xNodeSet;
    }

    public int nextNode() {
        int n2;
        if (this.m_foundLast) {
            return -1;
        }
        if (null != this.m_exprObj) {
            this.m_lastFetched = n2 = this.m_exprObj.nextNode();
        } else {
            n2 = -1;
            this.m_lastFetched = -1;
        }
        if (-1 != n2) {
            ++this.m_pos;
            return n2;
        }
        this.m_foundLast = true;
        return -1;
    }

    public void detach() {
        if (this.m_allowDetach) {
            super.detach();
            this.m_exprObj.detach();
            this.m_exprObj = null;
        }
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        this.m_expr.fixupVariables(vector, n2);
    }

    public int getAnalysisBits() {
        if (null != this.m_expr && this.m_expr instanceof PathComponent) {
            return ((PathComponent)((Object)this.m_expr)).getAnalysisBits();
        }
        return 67108864;
    }

    public boolean isDocOrdered() {
        return this.m_exprObj.isDocOrdered();
    }

    public void callPredicateVisitors(XPathVisitor xPathVisitor) {
        this.m_expr.callVisitors(new filterExprOwner(this), xPathVisitor);
        super.callPredicateVisitors(xPathVisitor);
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        FilterExprIteratorSimple filterExprIteratorSimple = (FilterExprIteratorSimple)expression;
        if (!this.m_expr.deepEquals(filterExprIteratorSimple.m_expr)) {
            return false;
        }
        return true;
    }

    public int getAxis() {
        if (null != this.m_exprObj) {
            return this.m_exprObj.getAxis();
        }
        return 20;
    }

    static Expression access$000(FilterExprIteratorSimple filterExprIteratorSimple) {
        return filterExprIteratorSimple.m_expr;
    }

    static Expression access$002(FilterExprIteratorSimple filterExprIteratorSimple, Expression expression) {
        filterExprIteratorSimple.m_expr = expression;
        return filterExprIteratorSimple.m_expr;
    }

    class filterExprOwner
    implements ExpressionOwner {
        private final FilterExprIteratorSimple this$0;

        filterExprOwner(FilterExprIteratorSimple filterExprIteratorSimple) {
            this.this$0 = filterExprIteratorSimple;
        }

        public Expression getExpression() {
            return FilterExprIteratorSimple.access$000(this.this$0);
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            FilterExprIteratorSimple.access$002(this.this$0, expression);
        }
    }

}

