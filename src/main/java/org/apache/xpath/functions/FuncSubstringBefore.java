/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function2Args;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncSubstringBefore
extends Function2Args {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        String string;
        String string2 = this.m_arg0.execute(xPathContext).str();
        int n2 = string2.indexOf(string = this.m_arg1.execute(xPathContext).str());
        return -1 == n2 ? XString.EMPTYSTRING : new XString(string2.substring(0, n2));
    }
}

