/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import org.apache.xml.utils.DOMHelper;
import org.w3c.dom.Node;

public class DOM2Helper
extends DOMHelper {
    public String getLocalNameOfNode(Node node) {
        String string = node.getLocalName();
        return null == string ? super.getLocalNameOfNode(node) : string;
    }

    public String getNamespaceOfNode(Node node) {
        return node.getNamespaceURI();
    }
}

