/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitable;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class Expression
implements Serializable,
ExpressionNode,
XPathVisitable {
    static final long serialVersionUID = 565665869777906902L;
    private ExpressionNode m_parent;

    public boolean canTraverseOutsideSubtree() {
        return false;
    }

    public XObject execute(XPathContext xPathContext, int n2) throws TransformerException {
        return this.execute(xPathContext);
    }

    public XObject execute(XPathContext xPathContext, int n2, DTM dTM, int n3) throws TransformerException {
        return this.execute(xPathContext);
    }

    public abstract XObject execute(XPathContext var1) throws TransformerException;

    public XObject execute(XPathContext xPathContext, boolean bl) throws TransformerException {
        return this.execute(xPathContext);
    }

    public double num(XPathContext xPathContext) throws TransformerException {
        return this.execute(xPathContext).num();
    }

    public boolean bool(XPathContext xPathContext) throws TransformerException {
        return this.execute(xPathContext).bool();
    }

    public XMLString xstr(XPathContext xPathContext) throws TransformerException {
        return this.execute(xPathContext).xstr();
    }

    public boolean isNodesetExpr() {
        return false;
    }

    public int asNode(XPathContext xPathContext) throws TransformerException {
        DTMIterator dTMIterator = this.execute(xPathContext).iter();
        return dTMIterator.nextNode();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public DTMIterator asIterator(XPathContext xPathContext, int n2) throws TransformerException {
        try {
            xPathContext.pushCurrentNodeAndExpression(n2, n2);
            DTMIterator dTMIterator = this.execute(xPathContext).iter();
            return dTMIterator;
        }
        finally {
            xPathContext.popCurrentNodeAndExpression();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public DTMIterator asIteratorRaw(XPathContext xPathContext, int n2) throws TransformerException {
        try {
            xPathContext.pushCurrentNodeAndExpression(n2, n2);
            XNodeSet xNodeSet = (XNodeSet)this.execute(xPathContext);
            DTMIterator dTMIterator = xNodeSet.iterRaw();
            return dTMIterator;
        }
        finally {
            xPathContext.popCurrentNodeAndExpression();
        }
    }

    public void executeCharsToContentHandler(XPathContext xPathContext, ContentHandler contentHandler) throws TransformerException, SAXException {
        XObject xObject = this.execute(xPathContext);
        xObject.dispatchCharactersEvents(contentHandler);
        xObject.detach();
    }

    public boolean isStableNumber() {
        return false;
    }

    public abstract void fixupVariables(Vector var1, int var2);

    public abstract boolean deepEquals(Expression var1);

    protected final boolean isSameClass(Expression expression) {
        if (null == expression) {
            return false;
        }
        return this.getClass() == expression.getClass();
    }

    public void warn(XPathContext xPathContext, String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createXPATHWarning(string, arrobject);
        if (null != xPathContext) {
            ErrorListener errorListener = xPathContext.getErrorListener();
            errorListener.warning(new TransformerException(string2, xPathContext.getSAXLocator()));
        }
    }

    public void assertion(boolean bl, String string) {
        if (!bl) {
            String string2 = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{string});
            throw new RuntimeException(string2);
        }
    }

    public void error(XPathContext xPathContext, String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createXPATHMessage(string, arrobject);
        if (null != xPathContext) {
            ErrorListener errorListener = xPathContext.getErrorListener();
            TransformerException transformerException = new TransformerException(string2, this);
            errorListener.fatalError(transformerException);
        }
    }

    public ExpressionNode getExpressionOwner() {
        ExpressionNode expressionNode;
        for (expressionNode = this.exprGetParent(); null != expressionNode && expressionNode instanceof Expression; expressionNode = expressionNode.exprGetParent()) {
        }
        return expressionNode;
    }

    public void exprSetParent(ExpressionNode expressionNode) {
        this.assertion(expressionNode != this, "Can not parent an expression to itself!");
        this.m_parent = expressionNode;
    }

    public ExpressionNode exprGetParent() {
        return this.m_parent;
    }

    public void exprAddChild(ExpressionNode expressionNode, int n2) {
        this.assertion(false, "exprAddChild method not implemented!");
    }

    public ExpressionNode exprGetChild(int n2) {
        return null;
    }

    public int exprGetNumChildren() {
        return 0;
    }

    public String getPublicId() {
        if (null == this.m_parent) {
            return null;
        }
        return this.m_parent.getPublicId();
    }

    public String getSystemId() {
        if (null == this.m_parent) {
            return null;
        }
        return this.m_parent.getSystemId();
    }

    public int getLineNumber() {
        if (null == this.m_parent) {
            return 0;
        }
        return this.m_parent.getLineNumber();
    }

    public int getColumnNumber() {
        if (null == this.m_parent) {
            return 0;
        }
        return this.m_parent.getColumnNumber();
    }
}

