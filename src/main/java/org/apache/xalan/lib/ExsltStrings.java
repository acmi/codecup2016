/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xalan.lib.ExsltBase;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.NodeSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ExsltStrings
extends ExsltBase {
    public static String align(String string, String string2, String string3) {
        if (string.length() >= string2.length()) {
            return string.substring(0, string2.length());
        }
        if (string3.equals("right")) {
            return string2.substring(0, string2.length() - string.length()) + string;
        }
        if (string3.equals("center")) {
            int n2 = (string2.length() - string.length()) / 2;
            return string2.substring(0, n2) + string + string2.substring(n2 + string.length());
        }
        return string + string2.substring(string.length());
    }

    public static String align(String string, String string2) {
        return ExsltStrings.align(string, string2, "left");
    }

    public static String concat(NodeList nodeList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
            Node node = nodeList.item(i2);
            String string = ExsltStrings.toString(node);
            if (string == null || string.length() <= 0) continue;
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }

    public static String padding(double d2, String string) {
        if (string == null || string.length() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = (int)d2;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (n3 == string.length()) {
                n3 = 0;
            }
            stringBuffer.append(string.charAt(n3));
            ++n3;
        }
        return stringBuffer.toString();
    }

    public static String padding(double d2) {
        return ExsltStrings.padding(d2, " ");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static NodeList split(String string, String string2) {
        NodeSet nodeSet = new NodeSet();
        nodeSet.setShouldCacheNodes(true);
        boolean bl = false;
        int n2 = 0;
        int n3 = 0;
        String string3 = null;
        while (!bl && n2 < string.length()) {
            Document document;
            n3 = string.indexOf(string2, n2);
            if (n3 >= 0) {
                string3 = string.substring(n2, n3);
                n2 = n3 + string2.length();
            } else {
                bl = true;
                string3 = string.substring(n2);
            }
            Document document2 = document = DocumentHolder.access$000();
            synchronized (document2) {
                Element element = document.createElement("token");
                Text text = document.createTextNode(string3);
                element.appendChild(text);
                nodeSet.addNode(element);
                continue;
            }
        }
        return nodeSet;
    }

    public static NodeList split(String string) {
        return ExsltStrings.split(string, " ");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static NodeList tokenize(String string, String string2) {
        Document document;
        NodeSet nodeSet = new NodeSet();
        if (string2 != null && string2.length() > 0) {
            Document document2;
            StringTokenizer stringTokenizer = new StringTokenizer(string, string2);
            Document document3 = document2 = DocumentHolder.access$000();
            synchronized (document3) {
                while (stringTokenizer.hasMoreTokens()) {
                    Element element = document2.createElement("token");
                    element.appendChild(document2.createTextNode(stringTokenizer.nextToken()));
                    nodeSet.addNode(element);
                }
            }
        }
        Document document4 = document = DocumentHolder.access$000();
        synchronized (document4) {
            for (int i2 = 0; i2 < string.length(); ++i2) {
                Element element = document.createElement("token");
                element.appendChild(document.createTextNode(string.substring(i2, i2 + 1)));
                nodeSet.addNode(element);
            }
        }
        return nodeSet;
    }

    public static NodeList tokenize(String string) {
        return ExsltStrings.tokenize(string, " \t\n\r");
    }

    private static class DocumentHolder {
        private static final Document m_doc;

        private DocumentHolder() {
        }

        static Document access$000() {
            return m_doc;
        }

        static {
            try {
                m_doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            }
            catch (ParserConfigurationException parserConfigurationException) {
                throw new WrappedRuntimeException(parserConfigurationException);
            }
        }
    }

}

