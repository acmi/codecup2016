/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import java.util.HashMap;
import org.apache.xalan.lib.ExsltBase;
import org.apache.xml.utils.DOMHelper;
import org.apache.xpath.NodeSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExsltSets
extends ExsltBase {
    public static NodeList leading(NodeList nodeList, NodeList nodeList2) {
        if (nodeList2.getLength() == 0) {
            return nodeList;
        }
        NodeSet nodeSet = new NodeSet(nodeList);
        NodeSet nodeSet2 = new NodeSet();
        Node node = nodeList2.item(0);
        if (!nodeSet.contains(node)) {
            return nodeSet2;
        }
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            Node node2 = nodeList.item(i2);
            if (!DOMHelper.isNodeAfter(node2, node) || DOMHelper.isNodeTheSame(node2, node)) continue;
            nodeSet2.addElement(node2);
        }
        return nodeSet2;
    }

    public static NodeList trailing(NodeList nodeList, NodeList nodeList2) {
        if (nodeList2.getLength() == 0) {
            return nodeList;
        }
        NodeSet nodeSet = new NodeSet(nodeList);
        NodeSet nodeSet2 = new NodeSet();
        Node node = nodeList2.item(0);
        if (!nodeSet.contains(node)) {
            return nodeSet2;
        }
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            Node node2 = nodeList.item(i2);
            if (!DOMHelper.isNodeAfter(node, node2) || DOMHelper.isNodeTheSame(node, node2)) continue;
            nodeSet2.addElement(node2);
        }
        return nodeSet2;
    }

    public static NodeList intersection(NodeList nodeList, NodeList nodeList2) {
        NodeSet nodeSet = new NodeSet(nodeList);
        NodeSet nodeSet2 = new NodeSet(nodeList2);
        NodeSet nodeSet3 = new NodeSet();
        nodeSet3.setShouldCacheNodes(true);
        for (int i2 = 0; i2 < nodeSet.getLength(); ++i2) {
            Node node = nodeSet.elementAt(i2);
            if (!nodeSet2.contains(node)) continue;
            nodeSet3.addElement(node);
        }
        return nodeSet3;
    }

    public static NodeList difference(NodeList nodeList, NodeList nodeList2) {
        NodeSet nodeSet = new NodeSet(nodeList);
        NodeSet nodeSet2 = new NodeSet(nodeList2);
        NodeSet nodeSet3 = new NodeSet();
        nodeSet3.setShouldCacheNodes(true);
        for (int i2 = 0; i2 < nodeSet.getLength(); ++i2) {
            Node node = nodeSet.elementAt(i2);
            if (nodeSet2.contains(node)) continue;
            nodeSet3.addElement(node);
        }
        return nodeSet3;
    }

    public static NodeList distinct(NodeList nodeList) {
        NodeSet nodeSet = new NodeSet();
        nodeSet.setShouldCacheNodes(true);
        HashMap<String, Node> hashMap = new HashMap<String, Node>();
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            Node node = nodeList.item(i2);
            String string = ExsltSets.toString(node);
            if (string == null) {
                nodeSet.addElement(node);
                continue;
            }
            if (hashMap.containsKey(string)) continue;
            hashMap.put(string, node);
            nodeSet.addElement(node);
        }
        return nodeSet;
    }

    public static boolean hasSameNode(NodeList nodeList, NodeList nodeList2) {
        NodeSet nodeSet = new NodeSet(nodeList);
        NodeSet nodeSet2 = new NodeSet(nodeList2);
        for (int i2 = 0; i2 < nodeSet.getLength(); ++i2) {
            if (!nodeSet2.contains(nodeSet.elementAt(i2))) continue;
            return true;
        }
        return false;
    }
}

