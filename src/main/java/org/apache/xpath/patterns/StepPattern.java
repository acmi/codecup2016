/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.patterns;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;

public class StepPattern
extends NodeTest
implements ExpressionOwner,
SubContextList {
    protected int m_axis;
    String m_targetString;
    StepPattern m_relativePathPattern;
    Expression[] m_predicates;

    public StepPattern(int n2, String string, String string2, int n3, int n4) {
        super(n2, string, string2);
        this.m_axis = n3;
    }

    public StepPattern(int n2, int n3, int n4) {
        super(n2);
        this.m_axis = n3;
    }

    public void calcTargetString() {
        int n2 = this.getWhatToShow();
        switch (n2) {
            case 128: {
                this.m_targetString = "#comment";
                break;
            }
            case 4: 
            case 8: 
            case 12: {
                this.m_targetString = "#text";
                break;
            }
            case -1: {
                this.m_targetString = "*";
                break;
            }
            case 256: 
            case 1280: {
                this.m_targetString = "/";
                break;
            }
            case 1: {
                if ("*" == this.m_name) {
                    this.m_targetString = "*";
                    break;
                }
                this.m_targetString = this.m_name;
                break;
            }
            default: {
                this.m_targetString = "*";
            }
        }
    }

    public String getTargetString() {
        return this.m_targetString;
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        if (null != this.m_predicates) {
            for (int i2 = 0; i2 < this.m_predicates.length; ++i2) {
                this.m_predicates[i2].fixupVariables(vector, n2);
            }
        }
        if (null != this.m_relativePathPattern) {
            this.m_relativePathPattern.fixupVariables(vector, n2);
        }
    }

    public void setRelativePathPattern(StepPattern stepPattern) {
        this.m_relativePathPattern = stepPattern;
        stepPattern.exprSetParent(this);
        this.calcScore();
    }

    public boolean canTraverseOutsideSubtree() {
        int n2 = this.getPredicateCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!this.getPredicate(i2).canTraverseOutsideSubtree()) continue;
            return true;
        }
        return false;
    }

    public Expression getPredicate(int n2) {
        return this.m_predicates[n2];
    }

    public final int getPredicateCount() {
        return null == this.m_predicates ? 0 : this.m_predicates.length;
    }

    public void setPredicates(Expression[] arrexpression) {
        this.m_predicates = arrexpression;
        if (null != arrexpression) {
            for (int i2 = 0; i2 < arrexpression.length; ++i2) {
                arrexpression[i2].exprSetParent(this);
            }
        }
        this.calcScore();
    }

    public void calcScore() {
        if (this.getPredicateCount() > 0 || null != this.m_relativePathPattern) {
            this.m_score = SCORE_OTHER;
        } else {
            super.calcScore();
        }
        if (null == this.m_targetString) {
            this.calcTargetString();
        }
    }

    public XObject execute(XPathContext xPathContext, int n2) throws TransformerException {
        DTM dTM = xPathContext.getDTM(n2);
        if (dTM != null) {
            int n3 = dTM.getExpandedTypeID(n2);
            return this.execute(xPathContext, n2, dTM, n3);
        }
        return NodeTest.SCORE_NONE;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        return this.execute(xPathContext, xPathContext.getCurrentNode());
    }

    public XObject execute(XPathContext xPathContext, int n2, DTM dTM, int n3) throws TransformerException {
        if (this.m_whatToShow == 65536) {
            if (null != this.m_relativePathPattern) {
                return this.m_relativePathPattern.execute(xPathContext);
            }
            return NodeTest.SCORE_NONE;
        }
        XObject xObject = super.execute(xPathContext, n2, dTM, n3);
        if (xObject == NodeTest.SCORE_NONE) {
            return NodeTest.SCORE_NONE;
        }
        if (this.getPredicateCount() != 0 && !this.executePredicates(xPathContext, dTM, n2)) {
            return NodeTest.SCORE_NONE;
        }
        if (null != this.m_relativePathPattern) {
            return this.m_relativePathPattern.executeRelativePathPattern(xPathContext, dTM, n2);
        }
        return xObject;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final boolean checkProximityPosition(XPathContext xPathContext, int n2, DTM dTM, int n3, int n4) {
        try {
            DTMAxisTraverser dTMAxisTraverser = dTM.getAxisTraverser(12);
            int n5 = dTMAxisTraverser.first(n3);
            while (-1 != n5) {
                block20 : {
                    try {
                        int n6;
                        boolean bl;
                        xPathContext.pushCurrentNode(n5);
                        if (NodeTest.SCORE_NONE == super.execute(xPathContext, n5)) break block20;
                        bl = true;
                        try {
                            xPathContext.pushSubContextList(this);
                            for (n6 = 0; n6 < n2; ++n6) {
                                xPathContext.pushPredicatePos(n6);
                                try {
                                    XObject xObject = this.m_predicates[n6].execute(xPathContext);
                                    try {
                                        if (2 == xObject.getType()) {
                                            throw new Error("Why: Should never have been called");
                                        }
                                        if (xObject.boolWithSideEffects()) continue;
                                        bl = false;
                                        return bl;
                                    }
                                    finally {
                                        xObject.detach();
                                    }
                                }
                                finally {
                                    xPathContext.popPredicatePos();
                                }
                            }
                        }
                        finally {
                            xPathContext.popSubContextList();
                        }
                        if (bl) {
                            --n4;
                        }
                        if (n4 < 1) {
                            n6 = 0;
                            return (boolean)n6;
                        }
                    }
                    finally {
                        xPathContext.popCurrentNode();
                    }
                }
                n5 = dTMAxisTraverser.next(n3, n5);
            }
        }
        catch (TransformerException transformerException) {
            throw new RuntimeException(transformerException.getMessage());
        }
        if (n4 != 1) return false;
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final int getProximityPosition(XPathContext xPathContext, int n2, boolean bl) {
        int n3 = 0;
        int n4 = xPathContext.getCurrentNode();
        DTM dTM = xPathContext.getDTM(n4);
        int n5 = dTM.getParent(n4);
        try {
            DTMAxisTraverser dTMAxisTraverser = dTM.getAxisTraverser(3);
            int n6 = dTMAxisTraverser.first(n5);
            while (-1 != n6) {
                block22 : {
                    try {
                        boolean bl2;
                        int n7;
                        xPathContext.pushCurrentNode(n6);
                        if (NodeTest.SCORE_NONE == super.execute(xPathContext, n6)) break block22;
                        bl2 = true;
                        try {
                            xPathContext.pushSubContextList(this);
                            for (n7 = 0; n7 < n2; ++n7) {
                                xPathContext.pushPredicatePos(n7);
                                try {
                                    XObject xObject = this.m_predicates[n7].execute(xPathContext);
                                    try {
                                        if (2 == xObject.getType()) {
                                            if (n3 + 1 == (int)xObject.numWithSideEffects()) continue;
                                            bl2 = false;
                                            return (int)bl2 ? 1 : 0;
                                        }
                                        if (xObject.boolWithSideEffects()) continue;
                                        bl2 = false;
                                        return (int)bl2 ? 1 : 0;
                                    }
                                    finally {
                                        xObject.detach();
                                    }
                                }
                                finally {
                                    xPathContext.popPredicatePos();
                                }
                            }
                        }
                        finally {
                            xPathContext.popSubContextList();
                        }
                        if (bl2) {
                            ++n3;
                        }
                        if (!bl && n6 == n4) {
                            n7 = n3;
                            return n7;
                        }
                    }
                    finally {
                        xPathContext.popCurrentNode();
                    }
                }
                n6 = dTMAxisTraverser.next(n5, n6);
            }
            return n3;
        }
        catch (TransformerException transformerException) {
            throw new RuntimeException(transformerException.getMessage());
        }
    }

    public int getProximityPosition(XPathContext xPathContext) {
        return this.getProximityPosition(xPathContext, xPathContext.getPredicatePos(), false);
    }

    public int getLastPos(XPathContext xPathContext) {
        return this.getProximityPosition(xPathContext, xPathContext.getPredicatePos(), true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected final XObject executeRelativePathPattern(XPathContext xPathContext, DTM dTM, int n2) throws TransformerException {
        XObject xObject = NodeTest.SCORE_NONE;
        int n3 = n2;
        DTMAxisTraverser dTMAxisTraverser = dTM.getAxisTraverser(this.m_axis);
        int n4 = dTMAxisTraverser.first(n3);
        while (-1 != n4) {
            try {
                xPathContext.pushCurrentNode(n4);
                xObject = this.execute(xPathContext);
                if (xObject != NodeTest.SCORE_NONE) {
                    break;
                }
            }
            finally {
                xPathContext.popCurrentNode();
            }
            n4 = dTMAxisTraverser.next(n3, n4);
        }
        return xObject;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected final boolean executePredicates(XPathContext xPathContext, DTM dTM, int n2) throws TransformerException {
        boolean bl;
        bl = true;
        boolean bl2 = false;
        int n3 = this.getPredicateCount();
        try {
            xPathContext.pushSubContextList(this);
            for (int i2 = 0; i2 < n3; ++i2) {
                xPathContext.pushPredicatePos(i2);
                try {
                    XObject xObject = this.m_predicates[i2].execute(xPathContext);
                    try {
                        if (2 == xObject.getType()) {
                            int n4 = (int)xObject.num();
                            if (bl2) {
                                bl = n4 == 1;
                                return bl;
                            }
                            bl2 = true;
                            if (this.checkProximityPosition(xPathContext, i2, dTM, n2, n4)) continue;
                            bl = false;
                            return bl;
                        }
                        if (xObject.boolWithSideEffects()) continue;
                        bl = false;
                        return bl;
                    }
                    finally {
                        xObject.detach();
                    }
                }
                finally {
                    xPathContext.popPredicatePos();
                }
            }
        }
        finally {
            xPathContext.popSubContextList();
        }
        return bl;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        StepPattern stepPattern = this;
        while (stepPattern != null) {
            if (stepPattern != this) {
                stringBuffer.append("/");
            }
            stringBuffer.append(Axis.getNames(stepPattern.m_axis));
            stringBuffer.append("::");
            if (20480 == stepPattern.m_whatToShow) {
                stringBuffer.append("doc()");
            } else if (65536 == stepPattern.m_whatToShow) {
                stringBuffer.append("function()");
            } else if (-1 == stepPattern.m_whatToShow) {
                stringBuffer.append("node()");
            } else if (4 == stepPattern.m_whatToShow) {
                stringBuffer.append("text()");
            } else if (64 == stepPattern.m_whatToShow) {
                stringBuffer.append("processing-instruction(");
                if (null != stepPattern.m_name) {
                    stringBuffer.append(stepPattern.m_name);
                }
                stringBuffer.append(")");
            } else if (128 == stepPattern.m_whatToShow) {
                stringBuffer.append("comment()");
            } else if (null != stepPattern.m_name) {
                if (2 == stepPattern.m_whatToShow) {
                    stringBuffer.append("@");
                }
                if (null != stepPattern.m_namespace) {
                    stringBuffer.append("{");
                    stringBuffer.append(stepPattern.m_namespace);
                    stringBuffer.append("}");
                }
                stringBuffer.append(stepPattern.m_name);
            } else if (2 == stepPattern.m_whatToShow) {
                stringBuffer.append("@");
            } else if (1280 == stepPattern.m_whatToShow) {
                stringBuffer.append("doc-root()");
            } else {
                stringBuffer.append("?" + Integer.toHexString(stepPattern.m_whatToShow));
            }
            if (null != stepPattern.m_predicates) {
                for (int i2 = 0; i2 < stepPattern.m_predicates.length; ++i2) {
                    stringBuffer.append("[");
                    stringBuffer.append(stepPattern.m_predicates[i2]);
                    stringBuffer.append("]");
                }
            }
            stepPattern = stepPattern.m_relativePathPattern;
        }
        return stringBuffer.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public double getMatchScore(XPathContext xPathContext, int n2) throws TransformerException {
        xPathContext.pushCurrentNode(n2);
        xPathContext.pushCurrentExpressionNode(n2);
        try {
            XObject xObject = this.execute(xPathContext);
            double d2 = xObject.num();
            return d2;
        }
        finally {
            xPathContext.popCurrentNode();
            xPathContext.popCurrentExpressionNode();
        }
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        if (xPathVisitor.visitMatchPattern(expressionOwner, this)) {
            this.callSubtreeVisitors(xPathVisitor);
        }
    }

    protected void callSubtreeVisitors(XPathVisitor xPathVisitor) {
        if (null != this.m_predicates) {
            int n2 = this.m_predicates.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                PredOwner predOwner = new PredOwner(this, i2);
                if (!xPathVisitor.visitPredicate(predOwner, this.m_predicates[i2])) continue;
                this.m_predicates[i2].callVisitors(predOwner, xPathVisitor);
            }
        }
        if (null != this.m_relativePathPattern) {
            this.m_relativePathPattern.callVisitors(this, xPathVisitor);
        }
    }

    public Expression getExpression() {
        return this.m_relativePathPattern;
    }

    public void setExpression(Expression expression) {
        expression.exprSetParent(this);
        this.m_relativePathPattern = (StepPattern)expression;
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        StepPattern stepPattern = (StepPattern)expression;
        if (null != this.m_predicates) {
            int n2 = this.m_predicates.length;
            if (null == stepPattern.m_predicates || stepPattern.m_predicates.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.m_predicates[i2].deepEquals(stepPattern.m_predicates[i2])) continue;
                return false;
            }
        } else if (null != stepPattern.m_predicates) {
            return false;
        }
        if (null != this.m_relativePathPattern ? !this.m_relativePathPattern.deepEquals(stepPattern.m_relativePathPattern) : stepPattern.m_relativePathPattern != null) {
            return false;
        }
        return true;
    }

    class PredOwner
    implements ExpressionOwner {
        int m_index;
        private final StepPattern this$0;

        PredOwner(StepPattern stepPattern, int n2) {
            this.this$0 = stepPattern;
            this.m_index = n2;
        }

        public Expression getExpression() {
            return this.this$0.m_predicates[this.m_index];
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            this.this$0.m_predicates[this.m_index] = expression;
        }
    }

}

