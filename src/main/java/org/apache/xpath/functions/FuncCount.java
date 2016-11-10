/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncCount
extends FunctionOneArg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        DTMIterator dTMIterator = this.m_arg0.asIterator(xPathContext, xPathContext.getCurrentNode());
        int n2 = dTMIterator.getLength();
        dTMIterator.detach();
        return new XNumber(n2);
    }
}

