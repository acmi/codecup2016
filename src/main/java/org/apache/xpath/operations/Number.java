/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.operations.UnaryOperation;

public class Number
extends UnaryOperation {
    public XObject operate(XObject xObject) throws TransformerException {
        if (2 == xObject.getType()) {
            return xObject;
        }
        return new XNumber(xObject.num());
    }

    public double num(XPathContext xPathContext) throws TransformerException {
        return this.m_right.num(xPathContext);
    }
}

