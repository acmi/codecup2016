/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncUnparsedEntityURI
extends FunctionOneArg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        String string = this.m_arg0.execute(xPathContext).str();
        int n2 = xPathContext.getCurrentNode();
        DTM dTM = xPathContext.getDTM(n2);
        int n3 = dTM.getDocument();
        String string2 = dTM.getUnparsedEntityURI(string);
        return new XString(string2);
    }
}

