/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTMManager;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.PredicatedNodeTest;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.functions.Function;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.StepPattern;

public class FuncCurrent
extends Function {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        SubContextList subContextList = xPathContext.getCurrentNodeList();
        int n2 = -1;
        if (null != subContextList) {
            if (subContextList instanceof PredicatedNodeTest) {
                LocPathIterator locPathIterator = ((PredicatedNodeTest)subContextList).getLocPathIterator();
                n2 = locPathIterator.getCurrentContextNode();
            } else if (subContextList instanceof StepPattern) {
                throw new RuntimeException(XSLMessages.createMessage("ER_PROCESSOR_ERROR", null));
            }
        } else {
            n2 = xPathContext.getContextNode();
        }
        return new XNodeSet(n2, xPathContext.getDTMManager());
    }

    public void fixupVariables(Vector vector, int n2) {
    }
}

