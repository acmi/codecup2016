/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.operations.Operation;

public class Equals
extends Operation {
    public XObject operate(XObject xObject, XObject xObject2) throws TransformerException {
        return xObject.equals(xObject2) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }

    public boolean bool(XPathContext xPathContext) throws TransformerException {
        XObject xObject;
        XObject xObject2 = this.m_left.execute(xPathContext, true);
        boolean bl = xObject2.equals(xObject = this.m_right.execute(xPathContext, true));
        xObject2.detach();
        xObject.detach();
        return bl;
    }
}

