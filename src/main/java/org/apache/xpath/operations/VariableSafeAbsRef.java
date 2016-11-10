/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.operations.Variable;

public class VariableSafeAbsRef
extends Variable {
    public XObject execute(XPathContext xPathContext, boolean bl) throws TransformerException {
        XNodeSet xNodeSet = (XNodeSet)super.execute(xPathContext, bl);
        DTMManager dTMManager = xPathContext.getDTMManager();
        int n2 = xPathContext.getContextNode();
        if (dTMManager.getDTM(xNodeSet.getRoot()).getDocument() != dTMManager.getDTM(n2).getDocument()) {
            Expression expression = (Expression)((Object)xNodeSet.getContainedIter());
            xNodeSet = (XNodeSet)expression.asIterator(xPathContext, n2);
        }
        return xNodeSet;
    }
}

