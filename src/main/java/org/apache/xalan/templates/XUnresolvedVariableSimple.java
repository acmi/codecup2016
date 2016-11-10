/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xpath.Expression;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class XUnresolvedVariableSimple
extends XObject {
    static final long serialVersionUID = -1224413807443958985L;

    public XUnresolvedVariableSimple(ElemVariable elemVariable) {
        super(elemVariable);
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        Expression expression = ((ElemVariable)this.m_obj).getSelect().getExpression();
        XObject xObject = expression.execute(xPathContext);
        xObject.allowDetachToRelease(false);
        return xObject;
    }

    public int getType() {
        return 600;
    }

    public String getTypeString() {
        return "XUnresolvedVariableSimple (" + this.object().getClass().getName() + ")";
    }
}

