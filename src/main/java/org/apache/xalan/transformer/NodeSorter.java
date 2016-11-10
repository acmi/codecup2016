/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.NodeSortKey;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class NodeSorter {
    XPathContext m_execContext;
    Vector m_keys;

    public NodeSorter(XPathContext xPathContext) {
        this.m_execContext = xPathContext;
    }

    public void sort(DTMIterator dTMIterator, Vector vector, XPathContext xPathContext) throws TransformerException {
        this.m_keys = vector;
        int n2 = dTMIterator.getLength();
        Vector<NodeCompareElem> vector2 = new Vector<NodeCompareElem>();
        for (int i2 = 0; i2 < n2; ++i2) {
            NodeCompareElem nodeCompareElem = new NodeCompareElem(this, dTMIterator.item(i2));
            vector2.addElement(nodeCompareElem);
        }
        Vector vector3 = new Vector();
        this.mergesort(vector2, vector3, 0, n2 - 1, xPathContext);
        for (int i3 = 0; i3 < n2; ++i3) {
            dTMIterator.setItem(((NodeCompareElem)vector2.elementAt((int)i3)).m_node, i3);
        }
        dTMIterator.setCurrentPos(0);
    }

    int compare(NodeCompareElem nodeCompareElem, NodeCompareElem nodeCompareElem2, int n2, XPathContext xPathContext) throws TransformerException {
        int n3 = 0;
        NodeSortKey nodeSortKey = (NodeSortKey)this.m_keys.elementAt(n2);
        if (nodeSortKey.m_treatAsNumbers) {
            double d2;
            double d3;
            if (n2 == 0) {
                d3 = (Double)nodeCompareElem.m_key1Value;
                d2 = (Double)nodeCompareElem2.m_key1Value;
            } else if (n2 == 1) {
                d3 = (Double)nodeCompareElem.m_key2Value;
                d2 = (Double)nodeCompareElem2.m_key2Value;
            } else {
                XObject xObject = nodeSortKey.m_selectPat.execute(this.m_execContext, nodeCompareElem.m_node, nodeSortKey.m_namespaceContext);
                XObject xObject2 = nodeSortKey.m_selectPat.execute(this.m_execContext, nodeCompareElem2.m_node, nodeSortKey.m_namespaceContext);
                d3 = xObject.num();
                d2 = xObject2.num();
            }
            if (d3 == d2 && n2 + 1 < this.m_keys.size()) {
                n3 = this.compare(nodeCompareElem, nodeCompareElem2, n2 + 1, xPathContext);
            } else {
                double d4 = Double.isNaN(d3) ? (Double.isNaN(d2) ? 0.0 : -1.0) : (Double.isNaN(d2) ? 1.0 : d3 - d2);
                n3 = d4 < 0.0 ? (nodeSortKey.m_descending ? 1 : -1) : (d4 > 0.0 ? (nodeSortKey.m_descending ? -1 : 1) : 0);
            }
        } else {
            Object object;
            CollationKey collationKey;
            Object object2;
            CollationKey collationKey2;
            if (n2 == 0) {
                collationKey = (CollationKey)nodeCompareElem.m_key1Value;
                collationKey2 = (CollationKey)nodeCompareElem2.m_key1Value;
            } else if (n2 == 1) {
                collationKey = (CollationKey)nodeCompareElem.m_key2Value;
                collationKey2 = (CollationKey)nodeCompareElem2.m_key2Value;
            } else {
                object2 = nodeSortKey.m_selectPat.execute(this.m_execContext, nodeCompareElem.m_node, nodeSortKey.m_namespaceContext);
                object = nodeSortKey.m_selectPat.execute(this.m_execContext, nodeCompareElem2.m_node, nodeSortKey.m_namespaceContext);
                collationKey = nodeSortKey.m_col.getCollationKey(object2.str());
                collationKey2 = nodeSortKey.m_col.getCollationKey(object.str());
            }
            n3 = collationKey.compareTo(collationKey2);
            if (nodeSortKey.m_caseOrderUpper && (object2 = collationKey.getSourceString().toLowerCase()).equals(object = collationKey2.getSourceString().toLowerCase())) {
                int n4 = n3 = n3 == 0 ? 0 : - n3;
            }
            if (nodeSortKey.m_descending) {
                n3 = - n3;
            }
        }
        if (0 == n3 && n2 + 1 < this.m_keys.size()) {
            n3 = this.compare(nodeCompareElem, nodeCompareElem2, n2 + 1, xPathContext);
        }
        if (0 == n3) {
            DTM dTM = xPathContext.getDTM(nodeCompareElem.m_node);
            n3 = dTM.isNodeAfter(nodeCompareElem.m_node, nodeCompareElem2.m_node) ? -1 : 1;
        }
        return n3;
    }

    void mergesort(Vector vector, Vector vector2, int n2, int n3, XPathContext xPathContext) throws TransformerException {
        if (n3 - n2 > 0) {
            int n4;
            int n5;
            int n6 = (n3 + n2) / 2;
            this.mergesort(vector, vector2, n2, n6, xPathContext);
            this.mergesort(vector, vector2, n6 + 1, n3, xPathContext);
            for (n4 = n6; n4 >= n2; --n4) {
                if (n4 >= vector2.size()) {
                    vector2.insertElementAt(vector.elementAt(n4), n4);
                    continue;
                }
                vector2.setElementAt(vector.elementAt(n4), n4);
            }
            n4 = n2;
            for (n5 = n6 + 1; n5 <= n3; ++n5) {
                if (n3 + n6 + 1 - n5 >= vector2.size()) {
                    vector2.insertElementAt(vector.elementAt(n5), n3 + n6 + 1 - n5);
                    continue;
                }
                vector2.setElementAt(vector.elementAt(n5), n3 + n6 + 1 - n5);
            }
            n5 = n3;
            for (int i2 = n2; i2 <= n3; ++i2) {
                int n7 = n4 == n5 ? -1 : this.compare((NodeCompareElem)vector2.elementAt(n4), (NodeCompareElem)vector2.elementAt(n5), 0, xPathContext);
                if (n7 < 0) {
                    vector.setElementAt(vector2.elementAt(n4), i2);
                    ++n4;
                    continue;
                }
                if (n7 <= 0) continue;
                vector.setElementAt(vector2.elementAt(n5), i2);
                --n5;
            }
        }
    }

    class NodeCompareElem {
        int m_node;
        int maxkey;
        Object m_key1Value;
        Object m_key2Value;
        private final NodeSorter this$0;

        NodeCompareElem(NodeSorter nodeSorter, int n2) throws TransformerException {
            this.this$0 = nodeSorter;
            this.maxkey = 2;
            this.m_node = n2;
            if (!nodeSorter.m_keys.isEmpty()) {
                double d2;
                Object object;
                int n3;
                NodeSortKey nodeSortKey = (NodeSortKey)nodeSorter.m_keys.elementAt(0);
                XObject xObject = nodeSortKey.m_selectPat.execute(nodeSorter.m_execContext, n2, nodeSortKey.m_namespaceContext);
                if (nodeSortKey.m_treatAsNumbers) {
                    d2 = xObject.num();
                    this.m_key1Value = new Double(d2);
                } else {
                    this.m_key1Value = nodeSortKey.m_col.getCollationKey(xObject.str());
                }
                if (xObject.getType() == 4 && -1 == (n3 = (object = ((XNodeSet)xObject).iterRaw()).getCurrentNode())) {
                    n3 = object.nextNode();
                }
                if (nodeSorter.m_keys.size() > 1) {
                    object = (NodeSortKey)nodeSorter.m_keys.elementAt(1);
                    XObject xObject2 = object.m_selectPat.execute(nodeSorter.m_execContext, n2, object.m_namespaceContext);
                    if (object.m_treatAsNumbers) {
                        d2 = xObject2.num();
                        this.m_key2Value = new Double(d2);
                    } else {
                        this.m_key2Value = object.m_col.getCollationKey(xObject2.str());
                    }
                }
            }
        }
    }

}

