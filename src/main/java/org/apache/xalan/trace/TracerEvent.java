/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.trace;

import java.util.EventListener;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TracerEvent
implements EventListener {
    public final ElemTemplateElement m_styleNode;
    public final TransformerImpl m_processor;
    public final Node m_sourceNode;
    public final QName m_mode;

    public TracerEvent(TransformerImpl transformerImpl, Node node, QName qName, ElemTemplateElement elemTemplateElement) {
        this.m_processor = transformerImpl;
        this.m_sourceNode = node;
        this.m_mode = qName;
        this.m_styleNode = elemTemplateElement;
    }

    public static String printNode(Node node) {
        String string = "" + node.hashCode() + " ";
        if (node instanceof Element) {
            string = string + "<" + node.getNodeName();
            for (Node node2 = node.getFirstChild(); null != node2; node2 = node2.getNextSibling()) {
                if (!(node2 instanceof Attr)) continue;
                string = string + TracerEvent.printNode(node2) + " ";
            }
            string = string + ">";
        } else {
            string = node instanceof Attr ? string + node.getNodeName() + "=" + node.getNodeValue() : string + node.getNodeName();
        }
        return string;
    }

    public static String printNodeList(NodeList nodeList) {
        Node node;
        int n2;
        String string = "" + nodeList.hashCode() + "[";
        int n3 = nodeList.getLength() - 1;
        for (n2 = 0; n2 < n3; ++n2) {
            node = nodeList.item(n2);
            if (null == node) continue;
            string = string + TracerEvent.printNode(node) + ", ";
        }
        if (n2 == n3 && null != (node = nodeList.item(n3))) {
            string = string + TracerEvent.printNode(node);
        }
        return string + "]";
    }
}

