/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function3Args;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncSubstring
extends Function3Args {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XMLString xMLString;
        int n2;
        XMLString xMLString2 = this.m_arg0.execute(xPathContext).xstr();
        double d2 = this.m_arg1.execute(xPathContext).num();
        int n3 = xMLString2.length();
        if (n3 <= 0) {
            return XString.EMPTYSTRING;
        }
        if (Double.isNaN(d2)) {
            d2 = -1000000.0;
            n2 = 0;
        } else {
            int n4 = n2 = (d2 = (double)Math.round(d2)) > 0.0 ? (int)d2 - 1 : 0;
        }
        if (null != this.m_arg2) {
            double d3 = this.m_arg2.num(xPathContext);
            int n5 = (int)((double)Math.round(d3) + d2) - 1;
            if (n5 < 0) {
                n5 = 0;
            } else if (n5 > n3) {
                n5 = n3;
            }
            if (n2 > n3) {
                n2 = n3;
            }
            xMLString = xMLString2.substring(n2, n5);
        } else {
            if (n2 > n3) {
                n2 = n3;
            }
            xMLString = xMLString2.substring(n2);
        }
        return (XString)xMLString;
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
        if (n2 < 2) {
            this.reportWrongNumberArgs();
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_TWO_OR_THREE", null));
    }
}

