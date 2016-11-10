/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.functions.Function;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncLast
extends Function {
    private boolean m_isTopLevel;

    public void postCompileStep(Compiler compiler) {
        this.m_isTopLevel = compiler.getLocationPathDepth() == -1;
    }

    public int getCountOfContextNodeList(XPathContext xPathContext) throws TransformerException {
        SubContextList subContextList;
        SubContextList subContextList2 = subContextList = this.m_isTopLevel ? null : xPathContext.getSubContextList();
        if (null != subContextList) {
            return subContextList.getLastPos(xPathContext);
        }
        DTMIterator dTMIterator = xPathContext.getContextNodeList();
        int n2 = null != dTMIterator ? dTMIterator.getLength() : 0;
        return n2;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        XNumber xNumber = new XNumber(this.getCountOfContextNodeList(xPathContext));
        return xNumber;
    }

    public void fixupVariables(Vector vector, int n2) {
    }
}

