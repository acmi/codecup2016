/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FunctionDef1Arg
extends FunctionOneArg {
    protected int getArg0AsNode(XPathContext xPathContext) throws TransformerException {
        return null == this.m_arg0 ? xPathContext.getCurrentNode() : this.m_arg0.asNode(xPathContext);
    }

    public boolean Arg0IsNodesetExpr() {
        return null == this.m_arg0 ? true : this.m_arg0.isNodesetExpr();
    }

    protected XMLString getArg0AsString(XPathContext xPathContext) throws TransformerException {
        if (null == this.m_arg0) {
            int n2 = xPathContext.getCurrentNode();
            if (-1 == n2) {
                return XString.EMPTYSTRING;
            }
            DTM dTM = xPathContext.getDTM(n2);
            return dTM.getStringValue(n2);
        }
        return this.m_arg0.execute(xPathContext).xstr();
    }

    protected double getArg0AsNumber(XPathContext xPathContext) throws TransformerException {
        if (null == this.m_arg0) {
            int n2 = xPathContext.getCurrentNode();
            if (-1 == n2) {
                return 0.0;
            }
            DTM dTM = xPathContext.getDTM(n2);
            XMLString xMLString = dTM.getStringValue(n2);
            return xMLString.toDouble();
        }
        return this.m_arg0.execute(xPathContext).num();
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
        if (n2 > 1) {
            this.reportWrongNumberArgs();
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_ZERO_OR_ONE", null));
    }

    public boolean canTraverseOutsideSubtree() {
        return null == this.m_arg0 ? false : super.canTraverseOutsideSubtree();
    }
}

