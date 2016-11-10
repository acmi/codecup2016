/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public class XNodeSetForDOM
extends XNodeSet {
    Object m_origObj;

    public XNodeSetForDOM(Node node, DTMManager dTMManager) {
        this.m_dtmMgr = dTMManager;
        this.m_origObj = node;
        int n2 = dTMManager.getDTMHandleFromNode(node);
        this.setObject(new NodeSetDTM(dTMManager));
        ((NodeSetDTM)this.m_obj).addNode(n2);
    }

    public XNodeSetForDOM(NodeList nodeList, XPathContext xPathContext) {
        this.m_dtmMgr = xPathContext.getDTMManager();
        this.m_origObj = nodeList;
        NodeSetDTM nodeSetDTM = new NodeSetDTM(nodeList, xPathContext);
        this.m_last = nodeSetDTM.getLength();
        this.setObject(nodeSetDTM);
    }

    public XNodeSetForDOM(NodeIterator nodeIterator, XPathContext xPathContext) {
        this.m_dtmMgr = xPathContext.getDTMManager();
        this.m_origObj = nodeIterator;
        NodeSetDTM nodeSetDTM = new NodeSetDTM(nodeIterator, xPathContext);
        this.m_last = nodeSetDTM.getLength();
        this.setObject(nodeSetDTM);
    }

    public Object object() {
        return this.m_origObj;
    }

    public NodeIterator nodeset() throws TransformerException {
        return this.m_origObj instanceof NodeIterator ? (NodeIterator)this.m_origObj : super.nodeset();
    }

    public NodeList nodelist() throws TransformerException {
        return this.m_origObj instanceof NodeList ? (NodeList)this.m_origObj : super.nodelist();
    }
}

