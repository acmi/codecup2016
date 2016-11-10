/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.lib.ExsltDynamic;
import org.apache.xalan.lib.ExsltSets;
import org.apache.xalan.lib.ObjectFactory;
import org.apache.xalan.xslt.EnvironmentCheck;
import org.apache.xml.utils.Hashtree2Node;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.NodeSet;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXNotSupportedException;

public class Extensions {
    static Class class$java$util$Hashtable;
    static Class class$java$lang$String;

    private Extensions() {
    }

    public static NodeSet nodeset(ExpressionContext expressionContext, Object object) {
        if (object instanceof NodeIterator) {
            return new NodeSet((NodeIterator)object);
        }
        String string = object instanceof String ? (String)object : (object instanceof Boolean ? new XBoolean((boolean)((Boolean)object)).str() : (object instanceof Double ? new XNumber((Double)object).str() : object.toString()));
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Text text = document.createTextNode(string);
            DocumentFragment documentFragment = document.createDocumentFragment();
            documentFragment.appendChild(text);
            return new NodeSet(documentFragment);
        }
        catch (ParserConfigurationException parserConfigurationException) {
            throw new WrappedRuntimeException(parserConfigurationException);
        }
    }

    public static NodeList intersection(NodeList nodeList, NodeList nodeList2) {
        return ExsltSets.intersection(nodeList, nodeList2);
    }

    public static NodeList difference(NodeList nodeList, NodeList nodeList2) {
        return ExsltSets.difference(nodeList, nodeList2);
    }

    public static NodeList distinct(NodeList nodeList) {
        return ExsltSets.distinct(nodeList);
    }

    public static boolean hasSameNodes(NodeList nodeList, NodeList nodeList2) {
        NodeSet nodeSet = new NodeSet(nodeList);
        NodeSet nodeSet2 = new NodeSet(nodeList2);
        if (nodeSet.getLength() != nodeSet2.getLength()) {
            return false;
        }
        for (int i2 = 0; i2 < nodeSet.getLength(); ++i2) {
            Node node = nodeSet.elementAt(i2);
            if (nodeSet2.contains(node)) continue;
            return false;
        }
        return true;
    }

    public static XObject evaluate(ExpressionContext expressionContext, String string) throws SAXNotSupportedException {
        return ExsltDynamic.evaluate(expressionContext, string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static NodeList tokenize(String string, String string2) {
        Document document = DocumentHolder.access$000();
        StringTokenizer stringTokenizer = new StringTokenizer(string, string2);
        NodeSet nodeSet = new NodeSet();
        Document document2 = document;
        synchronized (document2) {
            while (stringTokenizer.hasMoreTokens()) {
                nodeSet.addNode(document.createTextNode(stringTokenizer.nextToken()));
            }
        }
        return nodeSet;
    }

    public static NodeList tokenize(String string) {
        return Extensions.tokenize(string, " \t\n\r");
    }

    public static Node checkEnvironment(ExpressionContext expressionContext) {
        Object object;
        Document document;
        Object object2;
        try {
            object = DocumentBuilderFactory.newInstance();
            object2 = object.newDocumentBuilder();
            document = object2.newDocument();
        }
        catch (ParserConfigurationException parserConfigurationException) {
            throw new WrappedRuntimeException(parserConfigurationException);
        }
        object = null;
        try {
            object = Extensions.checkEnvironmentUsingWhich(expressionContext, document);
            if (null != object) {
                return object;
            }
            object2 = new EnvironmentCheck();
            Hashtable hashtable = object2.getEnvironmentHash();
            object = document.createElement("checkEnvironmentExtension");
            object2.appendEnvironmentReport((Node)object, document, hashtable);
            object2 = null;
        }
        catch (Exception exception) {
            throw new WrappedRuntimeException(exception);
        }
        return object;
    }

    private static Node checkEnvironmentUsingWhich(ExpressionContext expressionContext, Document document) {
        String string = "org.apache.env.Which";
        String string2 = "which";
        Class[] arrclass = new Class[3];
        Class class_ = class$java$util$Hashtable == null ? (Extensions.class$java$util$Hashtable = Extensions.class$("java.util.Hashtable")) : class$java$util$Hashtable;
        arrclass[0] = class_;
        Class class_2 = class$java$lang$String == null ? (Extensions.class$java$lang$String = Extensions.class$("java.lang.String")) : class$java$lang$String;
        arrclass[1] = class_2;
        arrclass[2] = class$java$lang$String == null ? (Extensions.class$java$lang$String = Extensions.class$("java.lang.String")) : class$java$lang$String;
        Class[] arrclass2 = arrclass;
        try {
            Class class_3 = ObjectFactory.findProviderClass("org.apache.env.Which", ObjectFactory.findClassLoader(), true);
            if (null == class_3) {
                return null;
            }
            Method method = class_3.getMethod("which", arrclass2);
            Hashtable hashtable = new Hashtable();
            Object[] arrobject = new Object[]{hashtable, "XmlCommons;Xalan;Xerces;Crimson;Ant", ""};
            Object object = method.invoke(null, arrobject);
            Element element = document.createElement("checkEnvironmentExtension");
            Hashtree2Node.appendHashToNode(hashtable, "whichReport", element, document);
            return element;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
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

