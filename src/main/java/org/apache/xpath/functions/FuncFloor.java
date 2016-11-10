/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncFloor
extends FunctionOneArg {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        return new XNumber(Math.floor(this.m_arg0.execute(xPathContext).num()));
    }
}

