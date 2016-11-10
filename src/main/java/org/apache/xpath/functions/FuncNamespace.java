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

public class FuncNamespace
extends FunctionDef1Arg {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        String string;
        int n2 = this.getArg0AsNode(xPathContext);
        if (n2 == -1) return XString.EMPTYSTRING;
        DTM dTM = xPathContext.getDTM(n2);
        short s2 = dTM.getNodeType(n2);
        if (s2 == 1) {
            string = dTM.getNamespaceURI(n2);
            return null == string ? XString.EMPTYSTRING : new XString(string);
        } else {
            if (s2 != 2) return XString.EMPTYSTRING;
            string = dTM.getNodeName(n2);
            if (string.startsWith("xmlns:") || string.equals("xmlns")) {
                return XString.EMPTYSTRING;
            }
            string = dTM.getNamespaceURI(n2);
        }
        return null == string ? XString.EMPTYSTRING : new XString(string);
    }
}

