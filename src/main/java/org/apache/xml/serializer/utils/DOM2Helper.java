/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer.utils;

import org.w3c.dom.Node;

public final class DOM2Helper {
    public String getLocalNameOfNode(Node node) {
        String string = node.getLocalName();
        return null == string ? this.getLocalNameOfNodeFallback(node) : string;
    }

    private String getLocalNameOfNodeFallback(Node node) {
        String string = node.getNodeName();
        int n2 = string.indexOf(58);
        return n2 < 0 ? string : string.substring(n2 + 1);
    }

    public String getNamespaceOfNode(Node node) {
        return node.getNamespaceURI();
    }
}

