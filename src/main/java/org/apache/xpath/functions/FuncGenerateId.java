/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionDef1Arg;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncGenerateId
extends FunctionDef1Arg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        int n2 = this.getArg0AsNode(xPathContext);
        if (-1 != n2) {
            return new XString("N" + Integer.toHexString(n2).toUpperCase());
        }
        return XString.EMPTYSTRING;
    }
}

