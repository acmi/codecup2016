/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.axes.NodeSequence;
import org.apache.xpath.objects.Comparator;
import org.apache.xpath.objects.EqualComparator;
import org.apache.xpath.objects.GreaterThanComparator;
import org.apache.xpath.objects.GreaterThanOrEqualComparator;
import org.apache.xpath.objects.LessThanComparator;
import org.apache.xpath.objects.LessThanOrEqualComparator;
import org.apache.xpath.objects.NotEqualComparator;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class XNodeSet
extends NodeSequence {
    static final long serialVersionUID = 1916026368035639667L;
    static final LessThanComparator S_LT = new LessThanComparator();
    static final LessThanOrEqualComparator S_LTE = new LessThanOrEqualComparator();
    static final GreaterThanComparator S_GT = new GreaterThanComparator();
    static final GreaterThanOrEqualComparator S_GTE = new GreaterThanOrEqualComparator();
    static final EqualComparator S_EQ = new EqualComparator();
    static final NotEqualComparator S_NEQ = new NotEqualComparator();

    protected XNodeSet() {
    }

    public XNodeSet(DTMIterator dTMIterator) {
        if (dTMIterator instanceof XNodeSet) {
            XNodeSet xNodeSet = (XNodeSet)dTMIterator;
            this.setIter(xNodeSet.m_iter);
            this.m_dtmMgr = xNodeSet.m_dtmMgr;
            this.m_last = xNodeSet.m_last;
            if (!xNodeSet.hasCache()) {
                xNodeSet.setShouldCacheNodes(true);
            }
            this.setObject(xNodeSet.getIteratorCache());
        } else {
            this.setIter(dTMIterator);
        }
    }

    public XNodeSet(XNodeSet xNodeSet) {
        this.setIter(xNodeSet.m_iter);
        this.m_dtmMgr = xNodeSet.m_dtmMgr;
        this.m_last = xNodeSet.m_last;
        if (!xNodeSet.hasCache()) {
            xNodeSet.setShouldCacheNodes(true);
        }
        this.setObject(xNodeSet.m_obj);
    }

    public XNodeSet(DTMManager dTMManager) {
        this(-1, dTMManager);
    }

    public XNodeSet(int n2, DTMManager dTMManager) {
        super(new NodeSetDTM(dTMManager));
        this.m_dtmMgr = dTMManager;
        if (-1 != n2) {
            ((NodeSetDTM)this.m_obj).addNode(n2);
            this.m_last = 1;
        } else {
            this.m_last = 0;
        }
    }

    public int getType() {
        return 4;
    }

    public String getTypeString() {
        return "#NODESET";
    }

    public double getNumberFromNode(int n2) {
        XMLString xMLString = this.m_dtmMgr.getDTM(n2).getStringValue(n2);
        return xMLString.toDouble();
    }

    public double num() {
        int n2 = this.item(0);
        return n2 != -1 ? this.getNumberFromNode(n2) : Double.NaN;
    }

    public double numWithSideEffects() {
        int n2 = this.nextNode();
        return n2 != -1 ? this.getNumberFromNode(n2) : Double.NaN;
    }

    public boolean bool() {
        return this.item(0) != -1;
    }

    public boolean boolWithSideEffects() {
        return this.nextNode() != -1;
    }

    public XMLString getStringFromNode(int n2) {
        if (-1 != n2) {
            return this.m_dtmMgr.getDTM(n2).getStringValue(n2);
        }
        return XString.EMPTYSTRING;
    }

    public void dispatchCharactersEvents(ContentHandler contentHandler) throws SAXException {
        int n2 = this.item(0);
        if (n2 != -1) {
            this.m_dtmMgr.getDTM(n2).dispatchCharactersEvents(n2, contentHandler, false);
        }
    }

    public XMLString xstr() {
        int n2 = this.item(0);
        return n2 != -1 ? this.getStringFromNode(n2) : XString.EMPTYSTRING;
    }

    public void appendToFsb(FastStringBuffer fastStringBuffer) {
        XString xString = (XString)this.xstr();
        xString.appendToFsb(fastStringBuffer);
    }

    public String str() {
        int n2 = this.item(0);
        return n2 != -1 ? this.getStringFromNode(n2).toString() : "";
    }

    public Object object() {
        if (null == this.m_obj) {
            return this;
        }
        return this.m_obj;
    }

    public NodeIterator nodeset() throws TransformerException {
        return new DTMNodeIterator(this.iter());
    }

    public NodeList nodelist() throws TransformerException {
        DTMNodeList dTMNodeList = new DTMNodeList(this);
        XNodeSet xNodeSet = (XNodeSet)dTMNodeList.getDTMIterator();
        this.SetVector(xNodeSet.getVector());
        return dTMNodeList;
    }

    public DTMIterator iterRaw() {
        return this;
    }

    public void release(DTMIterator dTMIterator) {
    }

    public DTMIterator iter() {
        try {
            if (this.hasCache()) {
                return this.cloneWithReset();
            }
            return this;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new RuntimeException(cloneNotSupportedException.getMessage());
        }
    }

    public XObject getFresh() {
        try {
            if (this.hasCache()) {
                return (XObject)((Object)this.cloneWithReset());
            }
            return this;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new RuntimeException(cloneNotSupportedException.getMessage());
        }
    }

    public NodeSetDTM mutableNodeset() {
        NodeSetDTM nodeSetDTM;
        if (this.m_obj instanceof NodeSetDTM) {
            nodeSetDTM = (NodeSetDTM)this.m_obj;
        } else {
            nodeSetDTM = new NodeSetDTM(this.iter());
            this.setObject(nodeSetDTM);
            this.setCurrentPos(0);
        }
        return nodeSetDTM;
    }

    public boolean compare(XObject xObject, Comparator comparator) throws TransformerException {
        boolean bl = false;
        int n2 = xObject.getType();
        if (4 == n2) {
            int n3;
            DTMIterator dTMIterator = this.iterRaw();
            DTMIterator dTMIterator2 = ((XNodeSet)xObject).iterRaw();
            Vector<XMLString> vector = null;
            block0 : while (-1 != (n3 = dTMIterator.nextNode())) {
                int n4;
                XMLString xMLString = this.getStringFromNode(n3);
                if (null == vector) {
                    while (-1 != (n4 = dTMIterator2.nextNode())) {
                        XMLString xMLString2 = this.getStringFromNode(n4);
                        if (comparator.compareStrings(xMLString, xMLString2)) {
                            bl = true;
                            continue block0;
                        }
                        if (null == vector) {
                            vector = new Vector<XMLString>();
                        }
                        vector.addElement(xMLString2);
                    }
                    continue;
                }
                n4 = vector.size();
                for (int i2 = 0; i2 < n4; ++i2) {
                    if (!comparator.compareStrings(xMLString, (XMLString)vector.elementAt(i2))) continue;
                    bl = true;
                    continue block0;
                }
            }
            dTMIterator.reset();
            dTMIterator2.reset();
        } else if (1 == n2) {
            double d2 = this.bool() ? 1.0 : 0.0;
            double d3 = xObject.num();
            bl = comparator.compareNumbers(d2, d3);
        } else if (2 == n2) {
            int n5;
            DTMIterator dTMIterator = this.iterRaw();
            double d4 = xObject.num();
            while (-1 != (n5 = dTMIterator.nextNode())) {
                double d5 = this.getNumberFromNode(n5);
                if (!comparator.compareNumbers(d5, d4)) continue;
                bl = true;
                break;
            }
            dTMIterator.reset();
        } else if (5 == n2) {
            int n6;
            XMLString xMLString = xObject.xstr();
            DTMIterator dTMIterator = this.iterRaw();
            while (-1 != (n6 = dTMIterator.nextNode())) {
                XMLString xMLString3 = this.getStringFromNode(n6);
                if (!comparator.compareStrings(xMLString3, xMLString)) continue;
                bl = true;
                break;
            }
            dTMIterator.reset();
        } else if (3 == n2) {
            int n7;
            XMLString xMLString = xObject.xstr();
            DTMIterator dTMIterator = this.iterRaw();
            while (-1 != (n7 = dTMIterator.nextNode())) {
                XMLString xMLString4 = this.getStringFromNode(n7);
                if (!comparator.compareStrings(xMLString4, xMLString)) continue;
                bl = true;
                break;
            }
            dTMIterator.reset();
        } else {
            bl = comparator.compareNumbers(this.num(), xObject.num());
        }
        return bl;
    }

    public boolean lessThan(XObject xObject) throws TransformerException {
        return this.compare(xObject, S_LT);
    }

    public boolean lessThanOrEqual(XObject xObject) throws TransformerException {
        return this.compare(xObject, S_LTE);
    }

    public boolean greaterThan(XObject xObject) throws TransformerException {
        return this.compare(xObject, S_GT);
    }

    public boolean greaterThanOrEqual(XObject xObject) throws TransformerException {
        return this.compare(xObject, S_GTE);
    }

    public boolean equals(XObject xObject) {
        try {
            return this.compare(xObject, S_EQ);
        }
        catch (TransformerException transformerException) {
            throw new WrappedRuntimeException(transformerException);
        }
    }

    public boolean notEquals(XObject xObject) throws TransformerException {
        return this.compare(xObject, S_NEQ);
    }
}

