/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathVisitor;

public class ElemWhen
extends ElemTemplateElement {
    static final long serialVersionUID = 5984065730262071360L;
    private XPath m_test;

    public void setTest(XPath xPath) {
        this.m_test = xPath;
    }

    public XPath getTest() {
        return this.m_test;
    }

    public int getXSLToken() {
        return 38;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        Vector vector = stylesheetRoot.getComposeState().getVariableNames();
        if (null != this.m_test) {
            this.m_test.fixupVariables(vector, stylesheetRoot.getComposeState().getGlobalsSize());
        }
    }

    public String getNodeName() {
        return "when";
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (bl) {
            this.m_test.getExpression().callVisitors(this.m_test, xSLTVisitor);
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }
}

