/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.functions.Function;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncPosition
extends Function {
    private boolean m_isTopLevel;

    public void postCompileStep(Compiler compiler) {
        this.m_isTopLevel = compiler.getLocationPathDepth() == -1;
    }

    public int getPositionInContextNodeList(XPathContext xPathContext) {
        SubContextList subContextList;
        SubContextList subContextList2 = subContextList = this.m_isTopLevel ? null : xPathContext.getSubContextList();
        if (null != subContextList) {
            int n2 = subContextList.getProximityPosition(xPathContext);
            return n2;
        }
        DTMIterator dTMIterator = xPathContext.getContextNodeList();
        if (null != dTMIterator) {
            int n3 = dTMIterator.getCurrentNode();
            if (n3 == -1) {
                if (dTMIterator.getCurrentPos() == 0) {
                    return 0;
                }
                try {
                    dTMIterator = dTMIterator.cloneWithReset();
                }
                catch (CloneNotSupportedException cloneNotSupportedException) {
                    throw new WrappedRuntimeException(cloneNotSupportedException);
                }
                int n4 = xPathContext.getContextNode();
                while (-1 != (n3 = dTMIterator.nextNode()) && n3 != n4) {
                }
            }
            return dTMIterator.getCurrentPos();
        }
        return -1;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        double d2 = this.getPositionInContextNodeList(xPathContext);
        return new XNumber(d2);
    }

    public void fixupVariables(Vector vector, int n2) {
    }
}

