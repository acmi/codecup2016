/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import javax.xml.transform.SourceLocator;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeInfo {
    public static String systemId(ExpressionContext expressionContext) {
        Node node = expressionContext.getContextNode();
        int n2 = ((DTMNodeProxy)node).getDTMNodeNumber();
        SourceLocator sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(n2);
        if (sourceLocator != null) {
            return sourceLocator.getSystemId();
        }
        return null;
    }

    public static String systemId(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return null;
        }
        Node node = nodeList.item(0);
        int n2 = ((DTMNodeProxy)node).getDTMNodeNumber();
        SourceLocator sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(n2);
        if (sourceLocator != null) {
            return sourceLocator.getSystemId();
        }
        return null;
    }

    public static String publicId(ExpressionContext expressionContext) {
        Node node = expressionContext.getContextNode();
        int n2 = ((DTMNodeProxy)node).getDTMNodeNumber();
        SourceLocator sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(n2);
        if (sourceLocator != null) {
            return sourceLocator.getPublicId();
        }
        return null;
    }

    public static String publicId(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return null;
        }
        Node node = nodeList.item(0);
        int n2 = ((DTMNodeProxy)node).getDTMNodeNumber();
        SourceLocator sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(n2);
        if (sourceLocator != null) {
            return sourceLocator.getPublicId();
        }
        return null;
    }

    public static int lineNumber(ExpressionContext expressionContext) {
        Node node = expressionContext.getContextNode();
        int n2 = ((DTMNodeProxy)node).getDTMNodeNumber();
        SourceLocator sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(n2);
        if (sourceLocator != null) {
            return sourceLocator.getLineNumber();
        }
        return -1;
    }

    public static int lineNumber(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return -1;
        }
        Node node = nodeList.item(0);
        int n2 = ((DTMNodeProxy)node).getDTMNodeNumber();
        SourceLocator sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(n2);
        if (sourceLocator != null) {
            return sourceLocator.getLineNumber();
        }
        return -1;
    }

    public static int columnNumber(ExpressionContext expressionContext) {
        Node node = expressionContext.getContextNode();
        int n2 = ((DTMNodeProxy)node).getDTMNodeNumber();
        SourceLocator sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(n2);
        if (sourceLocator != null) {
            return sourceLocator.getColumnNumber();
        }
        return -1;
    }

    public static int columnNumber(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return -1;
        }
        Node node = nodeList.item(0);
        int n2 = ((DTMNodeProxy)node).getDTMNodeNumber();
        SourceLocator sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(n2);
        if (sourceLocator != null) {
            return sourceLocator.getColumnNumber();
        }
        return -1;
    }
}

