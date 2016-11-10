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

public class FuncDoclocation
extends FunctionDef1Arg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        int n2 = this.getArg0AsNode(xPathContext);
        String string = null;
        if (-1 != n2) {
            DTM dTM = xPathContext.getDTM(n2);
            if (11 == dTM.getNodeType(n2)) {
                n2 = dTM.getFirstChild(n2);
            }
            if (-1 != n2) {
                string = dTM.getDocumentBaseURI();
            }
        }
        return new XString(null != string ? string : "");
    }
}

