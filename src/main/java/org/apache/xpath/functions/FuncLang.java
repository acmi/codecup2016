/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncLang
extends FunctionOneArg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        String string = this.m_arg0.execute(xPathContext).str();
        int n2 = xPathContext.getCurrentNode();
        boolean bl = false;
        DTM dTM = xPathContext.getDTM(n2);
        while (-1 != n2) {
            int n3;
            if (1 == dTM.getNodeType(n2) && -1 != (n3 = dTM.getAttributeNode(n2, "http://www.w3.org/XML/1998/namespace", "lang"))) {
                String string2 = dTM.getNodeValue(n3);
                if (!string2.toLowerCase().startsWith(string.toLowerCase())) break;
                int n4 = string.length();
                if (string2.length() != n4 && string2.charAt(n4) != '-') break;
                bl = true;
                break;
            }
            n2 = dTM.getParent(n2);
        }
        return bl ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
}

