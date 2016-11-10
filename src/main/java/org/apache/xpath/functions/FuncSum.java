/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncSum
extends FunctionOneArg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        int n2;
        DTMIterator dTMIterator = this.m_arg0.asIterator(xPathContext, xPathContext.getCurrentNode());
        double d2 = 0.0;
        while (-1 != (n2 = dTMIterator.nextNode())) {
            DTM dTM = dTMIterator.getDTM(n2);
            XMLString xMLString = dTM.getStringValue(n2);
            if (null == xMLString) continue;
            d2 += xMLString.toDouble();
        }
        dTMIterator.detach();
        return new XNumber(d2);
    }
}

