/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XObject;

public class ElemIf
extends ElemTemplateElement {
    static final long serialVersionUID = 2158774632427453022L;
    private XPath m_test = null;

    public void setTest(XPath xPath) {
        this.m_test = xPath;
    }

    public XPath getTest() {
        return this.m_test;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        Vector vector = stylesheetRoot.getComposeState().getVariableNames();
        if (null != this.m_test) {
            this.m_test.fixupVariables(vector, stylesheetRoot.getComposeState().getGlobalsSize());
        }
    }

    public int getXSLToken() {
        return 36;
    }

    public String getNodeName() {
        return "if";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        XPathContext xPathContext = transformerImpl.getXPathContext();
        int n2 = xPathContext.getCurrentNode();
        if (transformerImpl.getDebug()) {
            XObject xObject = this.m_test.execute(xPathContext, n2, (PrefixResolver)this);
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireSelectedEvent(n2, this, "test", this.m_test, xObject);
            }
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEvent(this);
            }
            if (xObject.bool()) {
                transformerImpl.executeChildTemplates((ElemTemplateElement)this, true);
            }
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
        } else if (this.m_test.bool(xPathContext, n2, this)) {
            transformerImpl.executeChildTemplates((ElemTemplateElement)this, true);
        }
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (bl) {
            this.m_test.getExpression().callVisitors(this.m_test, xSLTVisitor);
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }
}

