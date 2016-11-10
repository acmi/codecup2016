/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncRound
extends FunctionOneArg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XObject xObject = this.m_arg0.execute(xPathContext);
        double d2 = xObject.num();
        if (d2 >= -0.5 && d2 < 0.0) {
            return new XNumber(-0.0);
        }
        if (d2 == 0.0) {
            return new XNumber(d2);
        }
        return new XNumber(Math.floor(d2 + 0.5));
    }
}

