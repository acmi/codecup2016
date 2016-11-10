/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function3Args;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncTranslate
extends Function3Args {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        String string = this.m_arg0.execute(xPathContext).str();
        String string2 = this.m_arg1.execute(xPathContext).str();
        String string3 = this.m_arg2.execute(xPathContext).str();
        int n2 = string.length();
        int n3 = string3.length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            int n4 = string2.indexOf(c2);
            if (n4 < 0) {
                stringBuffer.append(c2);
                continue;
            }
            if (n4 >= n3) continue;
            stringBuffer.append(string3.charAt(n4));
        }
        return new XString(stringBuffer.toString());
    }
}

