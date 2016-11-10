/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function2Args;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncSubstringAfter
extends Function2Args {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XMLString xMLString;
        XMLString xMLString2 = this.m_arg0.execute(xPathContext).xstr();
        int n2 = xMLString2.indexOf(xMLString = this.m_arg1.execute(xPathContext).xstr());
        return -1 == n2 ? XString.EMPTYSTRING : (XString)xMLString2.substring(n2 + xMLString.length());
    }
}

