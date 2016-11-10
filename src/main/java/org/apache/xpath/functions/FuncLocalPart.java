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

public class FuncLocalPart
extends FunctionDef1Arg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        String string;
        int n2 = this.getArg0AsNode(xPathContext);
        if (-1 == n2) {
            return XString.EMPTYSTRING;
        }
        DTM dTM = xPathContext.getDTM(n2);
        String string2 = string = n2 != -1 ? dTM.getLocalName(n2) : "";
        if (string.startsWith("#") || string.equals("xmlns")) {
            return XString.EMPTYSTRING;
        }
        return new XString(string);
    }
}

