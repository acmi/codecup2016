/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.operations.UnaryOperation;

public class Bool
extends UnaryOperation {
    public XObject operate(XObject xObject) throws TransformerException {
        if (1 == xObject.getType()) {
            return xObject;
        }
        return xObject.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }

    public boolean bool(XPathContext xPathContext) throws TransformerException {
        return this.m_right.bool(xPathContext);
    }
}

