/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.HashMap;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncExtElementAvailable
extends FunctionOneArg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        String string;
        String string2;
        String string3 = this.m_arg0.execute(xPathContext).str();
        int n2 = string3.indexOf(58);
        if (n2 < 0) {
            String string4 = "";
            string = "http://www.w3.org/1999/XSL/Transform";
            string2 = string3;
        } else {
            String string5 = string3.substring(0, n2);
            string = xPathContext.getNamespaceContext().getNamespaceForPrefix(string5);
            if (null == string) {
                return XBoolean.S_FALSE;
            }
            string2 = string3.substring(n2 + 1);
        }
        if (string.equals("http://www.w3.org/1999/XSL/Transform") || string.equals("http://xml.apache.org/xalan")) {
            try {
                TransformerImpl transformerImpl = (TransformerImpl)xPathContext.getOwnerObject();
                return transformerImpl.getStylesheet().getAvailableElements().containsKey(new QName(string, string2)) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
            }
            catch (Exception exception) {
                return XBoolean.S_FALSE;
            }
        }
        ExtensionsProvider extensionsProvider = (ExtensionsProvider)xPathContext.getOwnerObject();
        return extensionsProvider.elementAvailable(string, string2) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
}

