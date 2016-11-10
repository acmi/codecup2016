/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;

public class ElemWithParam
extends ElemTemplateElement {
    static final long serialVersionUID = -1070355175864326257L;
    int m_index;
    private XPath m_selectPattern = null;
    private QName m_qname = null;
    int m_qnameID;

    public void setSelect(XPath xPath) {
        this.m_selectPattern = xPath;
    }

    public XPath getSelect() {
        return this.m_selectPattern;
    }

    public void setName(QName qName) {
        this.m_qname = qName;
    }

    public QName getName() {
        return this.m_qname;
    }

    public int getXSLToken() {
        return 2;
    }

    public String getNodeName() {
        return "with-param";
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        Serializable serializable;
        if (null == this.m_selectPattern && stylesheetRoot.getOptimizer() && null != (serializable = ElemVariable.rewriteChildToExpression(this))) {
            this.m_selectPattern = serializable;
        }
        this.m_qnameID = stylesheetRoot.getComposeState().getQNameID(this.m_qname);
        super.compose(stylesheetRoot);
        serializable = stylesheetRoot.getComposeState().getVariableNames();
        if (null != this.m_selectPattern) {
            this.m_selectPattern.fixupVariables((Vector)serializable, stylesheetRoot.getComposeState().getGlobalsSize());
        }
    }

    public void setParentElem(ElemTemplateElement elemTemplateElement) {
        super.setParentElem(elemTemplateElement);
        elemTemplateElement.m_hasVariableDecl = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public XObject getValue(TransformerImpl transformerImpl, int n2) throws TransformerException {
        XObject xObject;
        XPathContext xPathContext = transformerImpl.getXPathContext();
        xPathContext.pushCurrentNode(n2);
        try {
            if (null != this.m_selectPattern) {
                xObject = this.m_selectPattern.execute(xPathContext, n2, (PrefixResolver)this);
                xObject.allowDetachToRelease(false);
                if (transformerImpl.getDebug()) {
                    transformerImpl.getTraceManager().fireSelectedEvent(n2, this, "select", this.m_selectPattern, xObject);
                }
            } else if (null == this.getFirstChildElem()) {
                xObject = XString.EMPTYSTRING;
            } else {
                int n3 = transformerImpl.transformToRTF(this);
                xObject = new XRTreeFrag(n3, xPathContext, this);
            }
            Object var7_6 = null;
            xPathContext.popCurrentNode();
        }
        catch (Throwable throwable) {
            Object var7_7 = null;
            xPathContext.popCurrentNode();
            throw throwable;
        }
        return xObject;
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (bl && null != this.m_selectPattern) {
            this.m_selectPattern.getExpression().callVisitors(this.m_selectPattern, xSLTVisitor);
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        if (this.m_selectPattern != null) {
            this.error("ER_CANT_HAVE_CONTENT_AND_SELECT", new Object[]{"xsl:" + this.getNodeName()});
            return null;
        }
        return super.appendChild(elemTemplateElement);
    }
}

