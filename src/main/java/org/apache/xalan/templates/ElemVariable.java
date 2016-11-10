/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xalan.templates.ElemValueOf;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XRTreeFragSelectWrapper;
import org.apache.xpath.objects.XString;
import org.w3c.dom.Node;

public class ElemVariable
extends ElemTemplateElement {
    static final long serialVersionUID = 9111131075322790061L;
    protected int m_index;
    int m_frameSize = -1;
    private XPath m_selectPattern;
    protected QName m_qname;
    private boolean m_isTopLevel = false;

    public ElemVariable() {
    }

    public void setIndex(int n2) {
        this.m_index = n2;
    }

    public int getIndex() {
        return this.m_index;
    }

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

    public void setIsTopLevel(boolean bl) {
        this.m_isTopLevel = bl;
    }

    public boolean getIsTopLevel() {
        return this.m_isTopLevel;
    }

    public int getXSLToken() {
        return 73;
    }

    public String getNodeName() {
        return "variable";
    }

    public ElemVariable(ElemVariable elemVariable) throws TransformerException {
        this.m_selectPattern = elemVariable.m_selectPattern;
        this.m_qname = elemVariable.m_qname;
        this.m_isTopLevel = elemVariable.m_isTopLevel;
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        int n2 = transformerImpl.getXPathContext().getCurrentNode();
        XObject xObject = this.getValue(transformerImpl, n2);
        transformerImpl.getXPathContext().getVarStack().setLocalVariable(this.m_index, xObject);
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public XObject getValue(TransformerImpl transformerImpl, int n2) throws TransformerException {
        XObject xObject;
        block8 : {
            XPathContext xPathContext = transformerImpl.getXPathContext();
            xPathContext.pushCurrentNode(n2);
            try {
                if (null != this.m_selectPattern) {
                    xObject = this.m_selectPattern.execute(xPathContext, n2, (PrefixResolver)this);
                    xObject.allowDetachToRelease(false);
                    if (transformerImpl.getDebug()) {
                        transformerImpl.getTraceManager().fireSelectedEvent(n2, this, "select", this.m_selectPattern, xObject);
                    }
                    break block8;
                }
                if (null == this.getFirstChildElem()) {
                    xObject = XString.EMPTYSTRING;
                    break block8;
                }
                int n3 = this.m_parentNode instanceof Stylesheet ? transformerImpl.transformToGlobalRTF(this) : transformerImpl.transformToRTF(this);
                xObject = new XRTreeFrag(n3, xPathContext, this);
            }
            finally {
                xPathContext.popCurrentNode();
            }
        }
        return xObject;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        Object object;
        if (null == this.m_selectPattern && stylesheetRoot.getOptimizer() && null != (object = ElemVariable.rewriteChildToExpression(this))) {
            this.m_selectPattern = object;
        }
        object = stylesheetRoot.getComposeState();
        Vector vector = object.getVariableNames();
        if (null != this.m_selectPattern) {
            this.m_selectPattern.fixupVariables(vector, object.getGlobalsSize());
        }
        if (!(this.m_parentNode instanceof Stylesheet) && this.m_qname != null) {
            this.m_index = object.addVariableName(this.m_qname) - object.getGlobalsSize();
        } else if (this.m_parentNode instanceof Stylesheet) {
            object.resetStackFrameSize();
        }
        super.compose(stylesheetRoot);
    }

    public void endCompose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.endCompose(stylesheetRoot);
        if (this.m_parentNode instanceof Stylesheet) {
            StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
            this.m_frameSize = composeState.getFrameSize();
            composeState.resetStackFrameSize();
        }
    }

    static XPath rewriteChildToExpression(ElemTemplateElement elemTemplateElement) throws TransformerException {
        ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getFirstChildElem();
        if (null != elemTemplateElement2 && null == elemTemplateElement2.getNextSiblingElem()) {
            ElemTextLiteral elemTextLiteral;
            int n2 = elemTemplateElement2.getXSLToken();
            if (30 == n2) {
                ElemValueOf elemValueOf = (ElemValueOf)elemTemplateElement2;
                if (!elemValueOf.getDisableOutputEscaping() && elemValueOf.getDOMBackPointer() == null) {
                    elemTemplateElement.m_firstChild = null;
                    return new XPath(new XRTreeFragSelectWrapper(elemValueOf.getSelect().getExpression()));
                }
            } else if (78 == n2 && !(elemTextLiteral = (ElemTextLiteral)elemTemplateElement2).getDisableOutputEscaping() && elemTextLiteral.getDOMBackPointer() == null) {
                String string = elemTextLiteral.getNodeValue();
                XString xString = new XString(string);
                elemTemplateElement.m_firstChild = null;
                return new XPath(new XRTreeFragSelectWrapper(xString));
            }
        }
        return null;
    }

    public void recompose(StylesheetRoot stylesheetRoot) {
        stylesheetRoot.recomposeVariables(this);
    }

    public void setParentElem(ElemTemplateElement elemTemplateElement) {
        super.setParentElem(elemTemplateElement);
        elemTemplateElement.m_hasVariableDecl = true;
    }

    protected boolean accept(XSLTVisitor xSLTVisitor) {
        return xSLTVisitor.visitVariableOrParamDecl(this);
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (null != this.m_selectPattern) {
            this.m_selectPattern.getExpression().callVisitors(this.m_selectPattern, xSLTVisitor);
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }

    public boolean isPsuedoVar() {
        String string = this.m_qname.getNamespaceURI();
        if (null != string && string.equals("http://xml.apache.org/xalan/psuedovar") && this.m_qname.getLocalName().startsWith("#")) {
            return true;
        }
        return false;
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        if (this.m_selectPattern != null) {
            this.error("ER_CANT_HAVE_CONTENT_AND_SELECT", new Object[]{"xsl:" + this.getNodeName()});
            return null;
        }
        return super.appendChild(elemTemplateElement);
    }
}

