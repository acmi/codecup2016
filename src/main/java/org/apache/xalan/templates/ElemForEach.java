/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemSort;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.NodeSorter;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.IntStack;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.SourceTreeManager;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class ElemForEach
extends ElemTemplateElement
implements ExpressionOwner {
    static final long serialVersionUID = 6018140636363583690L;
    static final boolean DEBUG = false;
    public boolean m_doc_cache_off = false;
    protected Expression m_selectExpression = null;
    protected XPath m_xpath = null;
    protected Vector m_sortElems = null;

    public void setSelect(XPath xPath) {
        this.m_selectExpression = xPath.getExpression();
        this.m_xpath = xPath;
    }

    public Expression getSelect() {
        return this.m_selectExpression;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        int n2 = this.getSortElemCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.getSortElem(i2).compose(stylesheetRoot);
        }
        Vector vector = stylesheetRoot.getComposeState().getVariableNames();
        if (null != this.m_selectExpression) {
            this.m_selectExpression.fixupVariables(vector, stylesheetRoot.getComposeState().getGlobalsSize());
        } else {
            this.m_selectExpression = this.getStylesheetRoot().m_selectDefault.getExpression();
        }
    }

    public void endCompose(StylesheetRoot stylesheetRoot) throws TransformerException {
        int n2 = this.getSortElemCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.getSortElem(i2).endCompose(stylesheetRoot);
        }
        super.endCompose(stylesheetRoot);
    }

    public int getSortElemCount() {
        return this.m_sortElems == null ? 0 : this.m_sortElems.size();
    }

    public ElemSort getSortElem(int n2) {
        return (ElemSort)this.m_sortElems.elementAt(n2);
    }

    public void setSortElem(ElemSort elemSort) {
        if (null == this.m_sortElems) {
            this.m_sortElems = new Vector();
        }
        this.m_sortElems.addElement(elemSort);
    }

    public int getXSLToken() {
        return 28;
    }

    public String getNodeName() {
        return "for-each";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        transformerImpl.pushCurrentTemplateRuleIsNull(true);
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        try {
            this.transformSelectedNodes(transformerImpl);
        }
        finally {
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
            transformerImpl.popCurrentTemplateRuleIsNull();
        }
    }

    protected ElemTemplateElement getTemplateMatch() {
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public DTMIterator sortNodes(XPathContext xPathContext, Vector vector, DTMIterator dTMIterator) throws TransformerException {
        NodeSorter nodeSorter = new NodeSorter(xPathContext);
        dTMIterator.setShouldCacheNodes(true);
        dTMIterator.runTo(-1);
        xPathContext.pushContextNodeList(dTMIterator);
        try {
            nodeSorter.sort(dTMIterator, vector, xPathContext);
            dTMIterator.setCurrentPos(0);
        }
        finally {
            xPathContext.popContextNodeList();
        }
        return dTMIterator;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void transformSelectedNodes(TransformerImpl transformerImpl) throws TransformerException {
        XPathContext xPathContext = transformerImpl.getXPathContext();
        int n2 = xPathContext.getCurrentNode();
        DTMIterator dTMIterator = this.m_selectExpression.asIterator(xPathContext, n2);
        try {
            Vector vector;
            int n3;
            Object object;
            Vector vector2 = vector = this.m_sortElems == null ? null : transformerImpl.processSortKeys(this, n2);
            if (null != vector) {
                dTMIterator = this.sortNodes(xPathContext, vector, dTMIterator);
            }
            if (transformerImpl.getDebug()) {
                object = this.m_xpath.getExpression();
                XObject xObject = object.execute(xPathContext);
                int dTM = xPathContext.getCurrentNode();
                transformerImpl.getTraceManager().fireSelectedEvent(dTM, this, "select", this.m_xpath, xObject);
            }
            xPathContext.pushCurrentNode(-1);
            object = xPathContext.getCurrentNodeStack();
            xPathContext.pushCurrentExpressionNode(-1);
            IntStack intStack = xPathContext.getCurrentExpressionNodeStack();
            xPathContext.pushSAXLocatorNull();
            xPathContext.pushContextNodeList(dTMIterator);
            transformerImpl.pushElemTemplateElement(null);
            DTM dTM = xPathContext.getDTM(n2);
            int n4 = n2 & -65536;
            while (-1 != (n3 = dTMIterator.nextNode())) {
                object.setTop(n3);
                intStack.setTop(n3);
                if ((n3 & -65536) != n4) {
                    dTM = xPathContext.getDTM(n3);
                    n4 = n3 & -65536;
                }
                short s2 = dTM.getNodeType(n3);
                if (transformerImpl.getDebug()) {
                    transformerImpl.getTraceManager().fireTraceEvent(this);
                }
                ElemTemplateElement elemTemplateElement = this.m_firstChild;
                while (elemTemplateElement != null) {
                    xPathContext.setSAXLocator(elemTemplateElement);
                    transformerImpl.setCurrentElement(elemTemplateElement);
                    elemTemplateElement.execute(transformerImpl);
                    elemTemplateElement = elemTemplateElement.m_nextSibling;
                }
                if (transformerImpl.getDebug()) {
                    transformerImpl.setCurrentElement(null);
                    transformerImpl.getTraceManager().fireTraceEndEvent(this);
                }
                if (!this.m_doc_cache_off) continue;
                xPathContext.getSourceTreeManager().removeDocumentFromCache(dTM.getDocument());
                xPathContext.release(dTM, false);
            }
        }
        finally {
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireSelectedEndEvent(n2, this, "select", new XPath(this.m_selectExpression), new XNodeSet(dTMIterator));
            }
            xPathContext.popSAXLocator();
            xPathContext.popContextNodeList();
            transformerImpl.popElemTemplateElement();
            xPathContext.popCurrentExpressionNode();
            xPathContext.popCurrentNode();
            dTMIterator.detach();
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        if (64 == n2) {
            this.setSortElem((ElemSort)elemTemplateElement);
            return elemTemplateElement;
        }
        return super.appendChild(elemTemplateElement);
    }

    public void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (bl && null != this.m_selectExpression) {
            this.m_selectExpression.callVisitors(this, xSLTVisitor);
        }
        int n2 = this.getSortElemCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.getSortElem(i2).callVisitors(xSLTVisitor);
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }

    public Expression getExpression() {
        return this.m_selectExpression;
    }

    public void setExpression(Expression expression) {
        expression.exprSetParent(this);
        this.m_selectExpression = expression;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.m_xpath = null;
    }
}

