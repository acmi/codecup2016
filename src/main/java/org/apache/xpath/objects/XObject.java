/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathException;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XMLStringFactoryImpl;
import org.apache.xpath.objects.XObjectFactory;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class XObject
extends Expression
implements Serializable,
Cloneable {
    static final long serialVersionUID = -821887098985662951L;
    protected Object m_obj;
    public static final int CLASS_NULL = -1;
    public static final int CLASS_UNKNOWN = 0;
    public static final int CLASS_BOOLEAN = 1;
    public static final int CLASS_NUMBER = 2;
    public static final int CLASS_STRING = 3;
    public static final int CLASS_NODESET = 4;
    public static final int CLASS_RTREEFRAG = 5;
    public static final int CLASS_UNRESOLVEDVARIABLE = 600;

    public XObject() {
    }

    public XObject(Object object) {
        this.setObject(object);
    }

    protected void setObject(Object object) {
        this.m_obj = object;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        return this;
    }

    public void allowDetachToRelease(boolean bl) {
    }

    public void detach() {
    }

    public void destruct() {
        if (null != this.m_obj) {
            this.allowDetachToRelease(true);
            this.detach();
            this.setObject(null);
        }
    }

    public void reset() {
    }

    public void dispatchCharactersEvents(ContentHandler contentHandler) throws SAXException {
        this.xstr().dispatchCharactersEvents(contentHandler);
    }

    public static XObject create(Object object) {
        return XObjectFactory.create(object);
    }

    public static XObject create(Object object, XPathContext xPathContext) {
        return XObjectFactory.create(object, xPathContext);
    }

    public int getType() {
        return 0;
    }

    public String getTypeString() {
        return "#UNKNOWN (" + this.object().getClass().getName() + ")";
    }

    public double num() throws TransformerException {
        this.error("ER_CANT_CONVERT_TO_NUMBER", new Object[]{this.getTypeString()});
        return 0.0;
    }

    public double numWithSideEffects() throws TransformerException {
        return this.num();
    }

    public boolean bool() throws TransformerException {
        this.error("ER_CANT_CONVERT_TO_NUMBER", new Object[]{this.getTypeString()});
        return false;
    }

    public boolean boolWithSideEffects() throws TransformerException {
        return this.bool();
    }

    public XMLString xstr() {
        return XMLStringFactoryImpl.getFactory().newstr(this.str());
    }

    public String str() {
        return this.m_obj != null ? this.m_obj.toString() : "";
    }

    public String toString() {
        return this.str();
    }

    public int rtf(XPathContext xPathContext) {
        int n2 = this.rtf();
        if (-1 == n2) {
            DTM dTM = xPathContext.createDocumentFragment();
            dTM.appendTextChild(this.str());
            n2 = dTM.getDocument();
        }
        return n2;
    }

    public DocumentFragment rtree(XPathContext xPathContext) {
        DocumentFragment documentFragment = null;
        int n2 = this.rtf();
        if (-1 == n2) {
            DTM dTM = xPathContext.createDocumentFragment();
            dTM.appendTextChild(this.str());
            documentFragment = (DocumentFragment)dTM.getNode(dTM.getDocument());
        } else {
            DTM dTM = xPathContext.getDTM(n2);
            documentFragment = (DocumentFragment)dTM.getNode(dTM.getDocument());
        }
        return documentFragment;
    }

    public DocumentFragment rtree() {
        return null;
    }

    public int rtf() {
        return -1;
    }

    public Object object() {
        return this.m_obj;
    }

    public DTMIterator iter() throws TransformerException {
        this.error("ER_CANT_CONVERT_TO_NODELIST", new Object[]{this.getTypeString()});
        return null;
    }

    public XObject getFresh() {
        return this;
    }

    public NodeIterator nodeset() throws TransformerException {
        this.error("ER_CANT_CONVERT_TO_NODELIST", new Object[]{this.getTypeString()});
        return null;
    }

    public NodeList nodelist() throws TransformerException {
        this.error("ER_CANT_CONVERT_TO_NODELIST", new Object[]{this.getTypeString()});
        return null;
    }

    public NodeSetDTM mutableNodeset() throws TransformerException {
        this.error("ER_CANT_CONVERT_TO_MUTABLENODELIST", new Object[]{this.getTypeString()});
        return (NodeSetDTM)this.m_obj;
    }

    public Object castToType(int n2, XPathContext xPathContext) throws TransformerException {
        Object object;
        switch (n2) {
            case 3: {
                object = this.str();
                break;
            }
            case 2: {
                object = new Double(this.num());
                break;
            }
            case 4: {
                object = this.iter();
                break;
            }
            case 1: {
                object = this.bool() ? Boolean.TRUE : Boolean.FALSE;
                break;
            }
            case 0: {
                object = this.m_obj;
                break;
            }
            default: {
                this.error("ER_CANT_CONVERT_TO_TYPE", new Object[]{this.getTypeString(), Integer.toString(n2)});
                object = null;
            }
        }
        return object;
    }

    public boolean lessThan(XObject xObject) throws TransformerException {
        if (xObject.getType() == 4) {
            return xObject.greaterThan(this);
        }
        return this.num() < xObject.num();
    }

    public boolean lessThanOrEqual(XObject xObject) throws TransformerException {
        if (xObject.getType() == 4) {
            return xObject.greaterThanOrEqual(this);
        }
        return this.num() <= xObject.num();
    }

    public boolean greaterThan(XObject xObject) throws TransformerException {
        if (xObject.getType() == 4) {
            return xObject.lessThan(this);
        }
        return this.num() > xObject.num();
    }

    public boolean greaterThanOrEqual(XObject xObject) throws TransformerException {
        if (xObject.getType() == 4) {
            return xObject.lessThanOrEqual(this);
        }
        return this.num() >= xObject.num();
    }

    public boolean equals(XObject xObject) {
        if (xObject.getType() == 4) {
            return xObject.equals(this);
        }
        if (null != this.m_obj) {
            return this.m_obj.equals(xObject.m_obj);
        }
        return xObject.m_obj == null;
    }

    public boolean notEquals(XObject xObject) throws TransformerException {
        if (xObject.getType() == 4) {
            return xObject.notEquals(this);
        }
        return !this.equals(xObject);
    }

    protected void error(String string) throws TransformerException {
        this.error(string, null);
    }

    protected void error(String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createXPATHMessage(string, arrobject);
        throw new XPathException(string2, this);
    }

    public void fixupVariables(Vector vector, int n2) {
    }

    public void appendToFsb(FastStringBuffer fastStringBuffer) {
        fastStringBuffer.append(this.str());
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        this.assertion(false, "callVisitors should not be called for this object!!!");
    }

    public boolean deepEquals(Expression expression) {
        if (!this.isSameClass(expression)) {
            return false;
        }
        if (!this.equals((XObject)expression)) {
            return false;
        }
        return true;
    }
}

