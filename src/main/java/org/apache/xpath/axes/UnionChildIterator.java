/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.axes.PredicatedNodeTest;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;

public class UnionChildIterator
extends ChildTestIterator {
    private PredicatedNodeTest[] m_nodeTests = null;

    public UnionChildIterator() {
        super(null);
    }

    public void addNodeTest(PredicatedNodeTest predicatedNodeTest) {
        if (null == this.m_nodeTests) {
            this.m_nodeTests = new PredicatedNodeTest[1];
            this.m_nodeTests[0] = predicatedNodeTest;
        } else {
            PredicatedNodeTest[] arrpredicatedNodeTest = this.m_nodeTests;
            int n2 = this.m_nodeTests.length;
            this.m_nodeTests = new PredicatedNodeTest[n2 + 1];
            System.arraycopy(arrpredicatedNodeTest, 0, this.m_nodeTests, 0, n2);
            this.m_nodeTests[n2] = predicatedNodeTest;
        }
        predicatedNodeTest.exprSetParent(this);
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        if (this.m_nodeTests != null) {
            for (int i2 = 0; i2 < this.m_nodeTests.length; ++i2) {
                this.m_nodeTests[i2].fixupVariables(vector, n2);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public short acceptNode(int n2) {
        XPathContext xPathContext = this.getXPathContext();
        try {
            try {
                xPathContext.pushCurrentNode(n2);
                for (int i2 = 0; i2 < this.m_nodeTests.length; ++i2) {
                    PredicatedNodeTest predicatedNodeTest = this.m_nodeTests[i2];
                    XObject xObject = predicatedNodeTest.execute(xPathContext, n2);
                    if (xObject == NodeTest.SCORE_NONE) continue;
                    if (predicatedNodeTest.getPredicateCount() > 0) {
                        if (!predicatedNodeTest.executePredicates(n2, xPathContext)) continue;
                        short s2 = 1;
                        java.lang.Object var8_9 = null;
                        xPathContext.popCurrentNode();
                        return s2;
                    }
                    short s3 = 1;
                    java.lang.Object var8_10 = null;
                    xPathContext.popCurrentNode();
                    return s3;
                }
                java.lang.Object var8_11 = null;
                xPathContext.popCurrentNode();
                return 3;
            }
            catch (TransformerException transformerException) {
                throw new RuntimeException(transformerException.getMessage());
            }
        }
        catch (Throwable throwable) {
            java.lang.Object var8_12 = null;
            xPathContext.popCurrentNode();
            throw throwable;
        }
    }
}

