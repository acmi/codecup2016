/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import javax.xml.transform.Transformer;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformState;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public class XalanTransformState
implements TransformState {
    Node m_node = null;
    ElemTemplateElement m_currentElement = null;
    ElemTemplate m_currentTemplate = null;
    ElemTemplate m_matchedTemplate = null;
    int m_currentNodeHandle = -1;
    Node m_currentNode = null;
    int m_matchedNode = -1;
    DTMIterator m_contextNodeList = null;
    boolean m_elemPending = false;
    TransformerImpl m_transformer = null;

    public void setCurrentNode(Node node) {
        this.m_node = node;
    }

    public void resetState(Transformer transformer) {
        if (transformer != null && transformer instanceof TransformerImpl) {
            this.m_transformer = (TransformerImpl)transformer;
            this.m_currentElement = this.m_transformer.getCurrentElement();
            this.m_currentTemplate = this.m_transformer.getCurrentTemplate();
            this.m_matchedTemplate = this.m_transformer.getMatchedTemplate();
            int n2 = this.m_transformer.getCurrentNode();
            DTM dTM = this.m_transformer.getXPathContext().getDTM(n2);
            this.m_currentNode = dTM.getNode(n2);
            this.m_matchedNode = this.m_transformer.getMatchedNode();
            this.m_contextNodeList = this.m_transformer.getContextNodeList();
        }
    }

    public ElemTemplateElement getCurrentElement() {
        if (this.m_elemPending) {
            return this.m_currentElement;
        }
        return this.m_transformer.getCurrentElement();
    }

    public Node getCurrentNode() {
        if (this.m_currentNode != null) {
            return this.m_currentNode;
        }
        DTM dTM = this.m_transformer.getXPathContext().getDTM(this.m_transformer.getCurrentNode());
        return dTM.getNode(this.m_transformer.getCurrentNode());
    }

    public ElemTemplate getCurrentTemplate() {
        if (this.m_elemPending) {
            return this.m_currentTemplate;
        }
        return this.m_transformer.getCurrentTemplate();
    }

    public ElemTemplate getMatchedTemplate() {
        if (this.m_elemPending) {
            return this.m_matchedTemplate;
        }
        return this.m_transformer.getMatchedTemplate();
    }

    public Node getMatchedNode() {
        if (this.m_elemPending) {
            DTM dTM = this.m_transformer.getXPathContext().getDTM(this.m_matchedNode);
            return dTM.getNode(this.m_matchedNode);
        }
        DTM dTM = this.m_transformer.getXPathContext().getDTM(this.m_transformer.getMatchedNode());
        return dTM.getNode(this.m_transformer.getMatchedNode());
    }

    public NodeIterator getContextNodeList() {
        if (this.m_elemPending) {
            return new DTMNodeIterator(this.m_contextNodeList);
        }
        return new DTMNodeIterator(this.m_transformer.getContextNodeList());
    }

    public Transformer getTransformer() {
        return this.m_transformer;
    }
}

