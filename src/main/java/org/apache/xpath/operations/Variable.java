/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.operations;

import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class Variable
extends Expression
implements PathComponent {
    private boolean m_fixUpWasCalled = false;
    protected QName m_qname;
    protected int m_index;
    protected boolean m_isGlobal = false;

    public void setIndex(int n2) {
        this.m_index = n2;
    }

    public void setIsGlobal(boolean bl) {
        this.m_isGlobal = bl;
    }

    public void fixupVariables(Vector vector, int n2) {
        this.m_fixUpWasCalled = true;
        int n3 = vector.size();
        for (int i2 = vector.size() - 1; i2 >= 0; --i2) {
            QName qName = (QName)vector.elementAt(i2);
            if (!qName.equals(this.m_qname)) continue;
            if (i2 < n2) {
                this.m_isGlobal = true;
                this.m_index = i2;
            } else {
                this.m_index = i2 - n2;
            }
            return;
        }
        String string = XSLMessages.createXPATHMessage("ER_COULD_NOT_FIND_VAR", new Object[]{this.m_qname.toString()});
        TransformerException transformerException = new TransformerException(string, this);
        throw new WrappedRuntimeException(transformerException);
    }

    public void setQName(QName qName) {
        this.m_qname = qName;
    }

    public QName getQName() {
        return this.m_qname;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        return this.execute(xPathContext, false);
    }

    public XObject execute(XPathContext xPathContext, boolean bl) throws TransformerException {
        PrefixResolver prefixResolver = xPathContext.getNamespaceContext();
        XObject xObject = this.m_fixUpWasCalled ? (this.m_isGlobal ? xPathContext.getVarStack().getGlobalVariable(xPathContext, this.m_index, bl) : xPathContext.getVarStack().getLocalVariable(xPathContext, this.m_index, bl)) : xPathContext.getVarStack().getVariableOrParam(xPathContext, this.m_qname);
        if (null == xObject) {
            this.warn(xPathContext, "WG_ILLEGAL_VARIABLE_REFERENCE", new Object[]{this.m_qname.getLocalPart()});
            xObject = new XNodeSet(xPathContext.getDTMManager());
        }
        return xObject;
    }

    public ElemVariable getElemVariable() {
        ElemVariable elemVariable = null;
        ExpressionNode expressionNode = this.getExpressionOwner();
        if (null != expressionNode && expressionNode instanceof ElemTemplateElement) {
            ElemTemplateElement elemTemplateElement = (ElemTemplateElement)expressionNode;
            if (!(elemTemplateElement instanceof Stylesheet)) {
                while (elemTemplateElement != null && !(elemTemplateElement.getParentNode() instanceof Stylesheet)) {
                    ElemTemplateElement elemTemplateElement2 = elemTemplateElement;
                    while (null != (elemTemplateElement = elemTemplateElement.getPreviousSiblingElem())) {
                        if (!(elemTemplateElement instanceof ElemVariable)) continue;
                        elemVariable = (ElemVariable)elemTemplateElement;
                        if (elemVariable.getName().equals(this.m_qname)) {
                            return elemVariable;
                        }
                        elemVariable = null;
                    }
                    elemTemplateElement = elemTemplateElement2.getParentElem();
                }
            }
            if (elemTemplateElement != null) {
                elemVariable = elemTemplateElement.getStylesheetRoot().getVariableOrParamComposed(this.m_qname);
            }
        }
        return elemVariable;
    }

    public boolean isStableNumber() {
        return true;
    }

    public int getAnalysisBits() {
        Expression expression;
        XPath xPath;
        ElemVariable elemVariable = this.getElemVariable();
        if (null != elemVariable && null != (xPath = elemVariable.getSelect()) && null != (expression = xPath.getExpression()) && expression instanceof PathComponent) {
            return ((PathComponent)((Object)expression)).getAnalysisBits();
        }
        return 67108864;
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        xPathVisitor.visitVariableRef(expressionOwner, this);
    }

    public boolean deepEquals(Expression expression) {
        if (!this.isSameClass(expression)) {
            return false;
        }
        if (!this.m_qname.equals(((Variable)expression).m_qname)) {
            return false;
        }
        if (this.getElemVariable() != ((Variable)expression).getElemVariable()) {
            return false;
        }
        return true;
    }
}

