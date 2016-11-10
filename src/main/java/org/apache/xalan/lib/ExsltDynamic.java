/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.lib.ExsltBase;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.NodeSet;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXNotSupportedException;

public class ExsltDynamic
extends ExsltBase {
    public static final String EXSL_URI = "http://exslt.org/common";

    public static double max(ExpressionContext expressionContext, NodeList nodeList, String string) throws SAXNotSupportedException {
        XPathContext xPathContext = null;
        if (!(expressionContext instanceof XPathContext.XPathExpressionContext)) {
            throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{expressionContext}));
        }
        xPathContext = ((XPathContext.XPathExpressionContext)expressionContext).getXPathContext();
        if (string == null || string.length() == 0) {
            return Double.NaN;
        }
        NodeSetDTM nodeSetDTM = new NodeSetDTM(nodeList, xPathContext);
        xPathContext.pushContextNodeList(nodeSetDTM);
        double d2 = -1.7976931348623157E308;
        for (int i2 = 0; i2 < nodeSetDTM.getLength(); ++i2) {
            int n2 = nodeSetDTM.item(i2);
            xPathContext.pushCurrentNode(n2);
            double d3 = 0.0;
            try {
                XPath xPath = new XPath(string, xPathContext.getSAXLocator(), xPathContext.getNamespaceContext(), 0);
                d3 = xPath.execute(xPathContext, n2, xPathContext.getNamespaceContext()).num();
            }
            catch (TransformerException transformerException) {
                xPathContext.popCurrentNode();
                xPathContext.popContextNodeList();
                return Double.NaN;
            }
            xPathContext.popCurrentNode();
            if (d3 <= d2) continue;
            d2 = d3;
        }
        xPathContext.popContextNodeList();
        return d2;
    }

    public static double min(ExpressionContext expressionContext, NodeList nodeList, String string) throws SAXNotSupportedException {
        XPathContext xPathContext = null;
        if (!(expressionContext instanceof XPathContext.XPathExpressionContext)) {
            throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{expressionContext}));
        }
        xPathContext = ((XPathContext.XPathExpressionContext)expressionContext).getXPathContext();
        if (string == null || string.length() == 0) {
            return Double.NaN;
        }
        NodeSetDTM nodeSetDTM = new NodeSetDTM(nodeList, xPathContext);
        xPathContext.pushContextNodeList(nodeSetDTM);
        double d2 = Double.MAX_VALUE;
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            int n2 = nodeSetDTM.item(i2);
            xPathContext.pushCurrentNode(n2);
            double d3 = 0.0;
            try {
                XPath xPath = new XPath(string, xPathContext.getSAXLocator(), xPathContext.getNamespaceContext(), 0);
                d3 = xPath.execute(xPathContext, n2, xPathContext.getNamespaceContext()).num();
            }
            catch (TransformerException transformerException) {
                xPathContext.popCurrentNode();
                xPathContext.popContextNodeList();
                return Double.NaN;
            }
            xPathContext.popCurrentNode();
            if (d3 >= d2) continue;
            d2 = d3;
        }
        xPathContext.popContextNodeList();
        return d2;
    }

    public static double sum(ExpressionContext expressionContext, NodeList nodeList, String string) throws SAXNotSupportedException {
        XPathContext xPathContext = null;
        if (!(expressionContext instanceof XPathContext.XPathExpressionContext)) {
            throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{expressionContext}));
        }
        xPathContext = ((XPathContext.XPathExpressionContext)expressionContext).getXPathContext();
        if (string == null || string.length() == 0) {
            return Double.NaN;
        }
        NodeSetDTM nodeSetDTM = new NodeSetDTM(nodeList, xPathContext);
        xPathContext.pushContextNodeList(nodeSetDTM);
        double d2 = 0.0;
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            int n2 = nodeSetDTM.item(i2);
            xPathContext.pushCurrentNode(n2);
            double d3 = 0.0;
            try {
                XPath xPath = new XPath(string, xPathContext.getSAXLocator(), xPathContext.getNamespaceContext(), 0);
                d3 = xPath.execute(xPathContext, n2, xPathContext.getNamespaceContext()).num();
            }
            catch (TransformerException transformerException) {
                xPathContext.popCurrentNode();
                xPathContext.popContextNodeList();
                return Double.NaN;
            }
            xPathContext.popCurrentNode();
            d2 += d3;
        }
        xPathContext.popContextNodeList();
        return d2;
    }

    public static NodeList map(ExpressionContext expressionContext, NodeList nodeList, String string) throws SAXNotSupportedException {
        XPathContext xPathContext = null;
        Document document = null;
        if (!(expressionContext instanceof XPathContext.XPathExpressionContext)) {
            throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{expressionContext}));
        }
        xPathContext = ((XPathContext.XPathExpressionContext)expressionContext).getXPathContext();
        if (string == null || string.length() == 0) {
            return new NodeSet();
        }
        NodeSetDTM nodeSetDTM = new NodeSetDTM(nodeList, xPathContext);
        xPathContext.pushContextNodeList(nodeSetDTM);
        NodeSet nodeSet = new NodeSet();
        nodeSet.setShouldCacheNodes(true);
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            int n2 = nodeSetDTM.item(i2);
            xPathContext.pushCurrentNode(n2);
            XObject xObject = null;
            try {
                Object object;
                XPath xPath = new XPath(string, xPathContext.getSAXLocator(), xPathContext.getNamespaceContext(), 0);
                xObject = xPath.execute(xPathContext, n2, xPathContext.getNamespaceContext());
                if (xObject instanceof XNodeSet) {
                    object = null;
                    object = ((XNodeSet)xObject).nodelist();
                    for (int i3 = 0; i3 < object.getLength(); ++i3) {
                        Node node = object.item(i3);
                        if (nodeSet.contains(node)) continue;
                        nodeSet.addNode(node);
                    }
                } else {
                    if (document == null) {
                        object = DocumentBuilderFactory.newInstance();
                        object.setNamespaceAware(true);
                        DocumentBuilder documentBuilder = object.newDocumentBuilder();
                        document = documentBuilder.newDocument();
                    }
                    object = null;
                    object = xObject instanceof XNumber ? document.createElementNS("http://exslt.org/common", "exsl:number") : (xObject instanceof XBoolean ? document.createElementNS("http://exslt.org/common", "exsl:boolean") : document.createElementNS("http://exslt.org/common", "exsl:string"));
                    Text text = document.createTextNode(xObject.str());
                    object.appendChild(text);
                    nodeSet.addNode((Node)object);
                }
            }
            catch (Exception exception) {
                xPathContext.popCurrentNode();
                xPathContext.popContextNodeList();
                return new NodeSet();
            }
            xPathContext.popCurrentNode();
        }
        xPathContext.popContextNodeList();
        return nodeSet;
    }

    public static XObject evaluate(ExpressionContext expressionContext, String string) throws SAXNotSupportedException {
        if (expressionContext instanceof XPathContext.XPathExpressionContext) {
            XPathContext xPathContext = null;
            try {
                xPathContext = ((XPathContext.XPathExpressionContext)expressionContext).getXPathContext();
                XPath xPath = new XPath(string, xPathContext.getSAXLocator(), xPathContext.getNamespaceContext(), 0);
                return xPath.execute(xPathContext, expressionContext.getContextNode(), xPathContext.getNamespaceContext());
            }
            catch (TransformerException transformerException) {
                return new XNodeSet(xPathContext.getDTMManager());
            }
        }
        throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{expressionContext}));
    }

    public static NodeList closure(ExpressionContext expressionContext, NodeList nodeList, String string) throws SAXNotSupportedException {
        XPathContext xPathContext = null;
        if (!(expressionContext instanceof XPathContext.XPathExpressionContext)) {
            throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{expressionContext}));
        }
        xPathContext = ((XPathContext.XPathExpressionContext)expressionContext).getXPathContext();
        if (string == null || string.length() == 0) {
            return new NodeSet();
        }
        NodeSet nodeSet = new NodeSet();
        nodeSet.setShouldCacheNodes(true);
        NodeList nodeList2 = nodeList;
        do {
            Object object;
            int n2;
            NodeSet nodeSet2 = new NodeSet();
            NodeSetDTM nodeSetDTM = new NodeSetDTM(nodeList2, xPathContext);
            xPathContext.pushContextNodeList(nodeSetDTM);
            for (n2 = 0; n2 < nodeList2.getLength(); ++n2) {
                block9 : {
                    object = nodeSetDTM.item(n2);
                    xPathContext.pushCurrentNode((int)object);
                    XObject xObject = null;
                    try {
                        XPath xPath = new XPath(string, xPathContext.getSAXLocator(), xPathContext.getNamespaceContext(), 0);
                        xObject = xPath.execute(xPathContext, (int)object, xPathContext.getNamespaceContext());
                        if (xObject instanceof XNodeSet) {
                            NodeList nodeList3 = null;
                            nodeList3 = ((XNodeSet)xObject).nodelist();
                            for (int i2 = 0; i2 < nodeList3.getLength(); ++i2) {
                                Node node = nodeList3.item(i2);
                                if (nodeSet2.contains(node)) continue;
                                nodeSet2.addNode(node);
                            }
                            break block9;
                        }
                        xPathContext.popCurrentNode();
                        xPathContext.popContextNodeList();
                        return new NodeSet();
                    }
                    catch (TransformerException transformerException) {
                        xPathContext.popCurrentNode();
                        xPathContext.popContextNodeList();
                        return new NodeSet();
                    }
                }
                xPathContext.popCurrentNode();
            }
            xPathContext.popContextNodeList();
            nodeList2 = nodeSet2;
            for (n2 = 0; n2 < nodeList2.getLength(); ++n2) {
                object = nodeList2.item(n2);
                if (nodeSet.contains((Node)object)) continue;
                nodeSet.addNode((Node)object);
            }
        } while (nodeList2.getLength() > 0);
        return nodeSet;
    }
}

