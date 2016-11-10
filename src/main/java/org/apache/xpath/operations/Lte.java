/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.operations.Operation;

public class Lte
extends Operation {
    public XObject operate(XObject xObject, XObject xObject2) throws TransformerException {
        return xObject.lessThanOrEqual(xObject2) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
}

