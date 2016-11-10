/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.apache.xpath.operations.UnaryOperation;

public class String
extends UnaryOperation {
    public XObject operate(XObject xObject) throws TransformerException {
        return (XString)xObject.xstr();
    }
}

