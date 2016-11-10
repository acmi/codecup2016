/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncTrue
extends Function {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        return XBoolean.S_TRUE;
    }

    public void fixupVariables(Vector vector, int n2) {
    }
}

