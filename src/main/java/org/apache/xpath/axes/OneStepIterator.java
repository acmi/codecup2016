/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class OneStepIterator
extends ChildTestIterator {
    protected int m_axis = -1;
    protected DTMAxisIterator m_iterator;

    OneStepIterator(Compiler compiler, int n2, int n3) throws TransformerException {
        super(compiler, n2, n3);
        int n4 = OpMap.getFirstChildPos(n2);
        this.m_axis = WalkerFactory.getAxisFromStep(compiler, n4);
    }

    public OneStepIterator(DTMAxisIterator dTMAxisIterator, int n2) throws TransformerException {
        super(null);
        this.m_iterator = dTMAxisIterator;
        this.m_axis = n2;
        int n3 = -1;
        this.initNodeTest(n3);
    }

    public void setRoot(int n2, Object object) {
        super.setRoot(n2, object);
        if (this.m_axis > -1) {
            this.m_iterator = this.m_cdtm.getAxisIterator(this.m_axis);
        }
        this.m_iterator.setStartNode(this.m_context);
    }

    public void detach() {
        if (this.m_allowDetach) {
            if (this.m_axis > -1) {
                this.m_iterator = null;
            }
            super.detach();
        }
    }

    protected int getNextNode() {
        this.m_lastFetched = this.m_iterator.next();
        return this.m_lastFetched;
    }

    public Object clone() throws CloneNotSupportedException {
        OneStepIterator oneStepIterator = (OneStepIterator)super.clone();
        if (this.m_iterator != null) {
            oneStepIterator.m_iterator = this.m_iterator.cloneIterator();
        }
        return oneStepIterator;
    }

    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        OneStepIterator oneStepIterator = (OneStepIterator)super.cloneWithReset();
        oneStepIterator.m_iterator = this.m_iterator;
        return oneStepIterator;
    }

    public boolean isReverseAxes() {
        return this.m_iterator.isReverse();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected int getProximityPosition(int n2) {
        if (!this.isReverseAxes()) {
            return super.getProximityPosition(n2);
        }
        if (n2 < 0) {
            return -1;
        }
        if (this.m_proximityPositions[n2] <= 0) {
            XPathContext xPathContext = this.getXPathContext();
            try {
                try {
                    int n3;
                    OneStepIterator oneStepIterator = (OneStepIterator)this.clone();
                    int n4 = this.getRoot();
                    xPathContext.pushCurrentNode(n4);
                    oneStepIterator.setRoot(n4, xPathContext);
                    oneStepIterator.m_predCount = n2;
                    int n5 = 1;
                    while (-1 != (n3 = oneStepIterator.nextNode())) {
                        ++n5;
                    }
                    int[] arrn = this.m_proximityPositions;
                    int n6 = n2;
                    arrn[n6] = arrn[n6] + n5;
                }
                catch (CloneNotSupportedException cloneNotSupportedException) {
                    Object var8_9 = null;
                    xPathContext.popCurrentNode();
                }
                Object var8_8 = null;
                xPathContext.popCurrentNode();
            }
            catch (Throwable throwable) {
                Object var8_10 = null;
                xPathContext.popCurrentNode();
                throw throwable;
            }
        }
        return this.m_proximityPositions[n2];
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getLength() {
        if (!this.isReverseAxes()) {
            return super.getLength();
        }
        boolean bl = this == this.m_execContext.getSubContextList();
        int n2 = this.getPredicateCount();
        if (-1 != this.m_length && bl && this.m_predicateIndex < 1) {
            return this.m_length;
        }
        int n3 = 0;
        XPathContext xPathContext = this.getXPathContext();
        try {
            try {
                int n4;
                OneStepIterator oneStepIterator = (OneStepIterator)this.cloneWithReset();
                int n5 = this.getRoot();
                xPathContext.pushCurrentNode(n5);
                oneStepIterator.setRoot(n5, xPathContext);
                oneStepIterator.m_predCount = this.m_predicateIndex;
                while (-1 != (n4 = oneStepIterator.nextNode())) {
                    ++n3;
                }
                Object var9_9 = null;
                xPathContext.popCurrentNode();
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                Object var9_10 = null;
                xPathContext.popCurrentNode();
            }
        }
        catch (Throwable throwable) {
            Object var9_11 = null;
            xPathContext.popCurrentNode();
            throw throwable;
        }
        if (bl && this.m_predicateIndex < 1) {
            this.m_length = n3;
        }
        return n3;
    }

    protected void countProximityPosition(int n2) {
        if (!this.isReverseAxes()) {
            super.countProximityPosition(n2);
        } else if (n2 < this.m_proximityPositions.length) {
            int[] arrn = this.m_proximityPositions;
            int n3 = n2;
            arrn[n3] = arrn[n3] - 1;
        }
    }

    public void reset() {
        super.reset();
        if (null != this.m_iterator) {
            this.m_iterator.reset();
        }
    }

    public int getAxis() {
        return this.m_axis;
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        if (this.m_axis != ((OneStepIterator)expression).m_axis) {
            return false;
        }
        return true;
    }
}

