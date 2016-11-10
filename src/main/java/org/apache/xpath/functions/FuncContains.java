/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function2Args;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncContains
extends Function2Args {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        String string = this.m_arg0.execute(xPathContext).str();
        String string2 = this.m_arg1.execute(xPathContext).str();
        if (string.length() == 0 && string2.length() == 0) {
            return XBoolean.S_TRUE;
        }
        int n2 = string.indexOf(string2);
        return n2 > -1 ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
}

