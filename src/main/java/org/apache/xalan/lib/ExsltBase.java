/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class ExsltBase {
    protected static String toString(Node node) {
        if (node instanceof DTMNodeProxy) {
            return ((DTMNodeProxy)node).getStringValue();
        }
        String string = node.getNodeValue();
        if (string == null) {
            NodeList nodeList = node.getChildNodes();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
                Node node2 = nodeList.item(i2);
                stringBuffer.append(ExsltBase.toString(node2));
            }
            return stringBuffer.toString();
        }
        return string;
    }

    protected static double toNumber(Node node) {
        double d2 = 0.0;
        String string = ExsltBase.toString(node);
        try {
            d2 = Double.valueOf(string);
        }
        catch (NumberFormatException numberFormatException) {
            d2 = Double.NaN;
        }
        return d2;
    }
}

