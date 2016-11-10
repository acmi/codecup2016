/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.extensions.ExtensionHandler;

public class XPathFunctionImpl
implements XPathFunction {
    private ExtensionHandler m_handler;
    private String m_funcName;

    public XPathFunctionImpl(ExtensionHandler extensionHandler, String string) {
        this.m_handler = extensionHandler;
        this.m_funcName = string;
    }

    public Object evaluate(List list) throws XPathFunctionException {
        Vector vector = XPathFunctionImpl.listToVector(list);
        try {
            return this.m_handler.callFunction(this.m_funcName, vector, null, null);
        }
        catch (TransformerException transformerException) {
            throw new XPathFunctionException(transformerException);
        }
    }

    private static Vector listToVector(List list) {
        if (list == null) {
            return null;
        }
        if (list instanceof Vector) {
            return (Vector)list;
        }
        Vector vector = new Vector();
        vector.addAll(list);
        return vector;
    }
}

