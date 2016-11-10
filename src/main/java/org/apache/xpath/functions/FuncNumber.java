/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionDef1Arg;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncNumber
extends FunctionDef1Arg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        return new XNumber(this.getArg0AsNumber(xPathContext));
    }
}

