/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.RTFIterator;
import org.apache.xpath.objects.DTMXRTreeFrag;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.w3c.dom.NodeList;

public class XRTreeFrag
extends XObject
implements Cloneable {
    private DTMXRTreeFrag m_DTMXRTreeFrag;
    private int m_dtmRoot = -1;
    protected boolean m_allowRelease = false;
    private XMLString m_xmlStr = null;

    public XRTreeFrag(int n2, XPathContext xPathContext, ExpressionNode expressionNode) {
        super(null);
        this.exprSetParent(expressionNode);
        this.initDTM(n2, xPathContext);
    }

    public XRTreeFrag(int n2, XPathContext xPathContext) {
        super(null);
        this.initDTM(n2, xPathContext);
    }

    private final void initDTM(int n2, XPathContext xPathContext) {
        this.m_dtmRoot = n2;
        DTM dTM = xPathContext.getDTM(n2);
        if (dTM != null) {
            this.m_DTMXRTreeFrag = xPathContext.getDTMXRTreeFrag(xPathContext.getDTMIdentity(dTM));
        }
    }

    public Object object() {
        if (this.m_DTMXRTreeFrag.getXPathContext() != null) {
            return new DTMNodeIterator(new NodeSetDTM(this.m_dtmRoot, this.m_DTMXRTreeFrag.getXPathContext().getDTMManager()));
        }
        return super.object();
    }

    public XRTreeFrag(Expression expression) {
        super(expression);
    }

    public void allowDetachToRelease(boolean bl) {
        this.m_allowRelease = bl;
    }

    public void detach() {
        if (this.m_allowRelease) {
            this.m_DTMXRTreeFrag.destruct();
            this.setObject(null);
        }
    }

    public int getType() {
        return 5;
    }

    public String getTypeString() {
        return "#RTREEFRAG";
    }

    public double num() throws TransformerException {
        XMLString xMLString = this.xstr();
        return xMLString.toDouble();
    }

    public boolean bool() {
        return true;
    }

    public XMLString xstr() {
        if (null == this.m_xmlStr) {
            this.m_xmlStr = this.m_DTMXRTreeFrag.getDTM().getStringValue(this.m_dtmRoot);
        }
        return this.m_xmlStr;
    }

    public void appendToFsb(FastStringBuffer fastStringBuffer) {
        XString xString = (XString)this.xstr();
        xString.appendToFsb(fastStringBuffer);
    }

    public String str() {
        String string = this.m_DTMXRTreeFrag.getDTM().getStringValue(this.m_dtmRoot).toString();
        return null == string ? "" : string;
    }

    public int rtf() {
        return this.m_dtmRoot;
    }

    public DTMIterator asNodeIterator() {
        return new RTFIterator(this.m_dtmRoot, this.m_DTMXRTreeFrag.getXPathContext().getDTMManager());
    }

    public NodeList convertToNodeset() {
        if (this.m_obj instanceof NodeList) {
            return (NodeList)this.m_obj;
        }
        return new DTMNodeList(this.asNodeIterator());
    }

    public boolean equals(XObject xObject) {
        try {
            if (4 == xObject.getType()) {
                return xObject.equals(this);
            }
            if (1 == xObject.getType()) {
                return this.bool() == xObject.bool();
            }
            if (2 == xObject.getType()) {
                return this.num() == xObject.num();
            }
            if (4 == xObject.getType()) {
                return this.xstr().equals(xObject.xstr());
            }
            if (3 == xObject.getType()) {
                return this.xstr().equals(xObject.xstr());
            }
            if (5 == xObject.getType()) {
                return this.xstr().equals(xObject.xstr());
            }
            return super.equals(xObject);
        }
        catch (TransformerException transformerException) {
            throw new WrappedRuntimeException(transformerException);
        }
    }
}

