/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionDef1Arg;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncQname
extends FunctionDef1Arg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        DTM dTM;
        String string;
        int n2 = this.getArg0AsNode(xPathContext);
        XString xString = -1 != n2 ? (null == (string = (dTM = xPathContext.getDTM(n2)).getNodeNameX(n2)) ? XString.EMPTYSTRING : new XString(string)) : XString.EMPTYSTRING;
        return xString;
    }
}

