/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.NSInfo;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMHelper {
    Hashtable m_NSInfos = new Hashtable();
    protected static final NSInfo m_NSInfoUnProcWithXMLNS = new NSInfo(false, true);
    protected static final NSInfo m_NSInfoUnProcWithoutXMLNS = new NSInfo(false, false);
    protected static final NSInfo m_NSInfoUnProcNoAncestorXMLNS = new NSInfo(false, false, 2);
    protected static final NSInfo m_NSInfoNullWithXMLNS = new NSInfo(true, true);
    protected static final NSInfo m_NSInfoNullWithoutXMLNS = new NSInfo(true, false);
    protected static final NSInfo m_NSInfoNullNoAncestorXMLNS = new NSInfo(true, false, 2);
    protected Vector m_candidateNoAncestorXMLNS = new Vector();
    protected Document m_DOMFactory = null;

    public static Document createDocument(boolean bl) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.setValidating(true);
            if (bl) {
                try {
                    documentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                }
                catch (ParserConfigurationException parserConfigurationException) {
                    // empty catch block
                }
            }
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            return document;
        }
        catch (ParserConfigurationException parserConfigurationException) {
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_CREATEDOCUMENT_NOT_SUPPORTED", null));
        }
    }

    public static boolean isNodeAfter(Node node, Node node2) {
        Node node3;
        if (node == node2 || DOMHelper.isNodeTheSame(node, node2)) {
            return true;
        }
        boolean bl = true;
        Node node4 = DOMHelper.getParentOfNode(node);
        if (node4 == (node3 = DOMHelper.getParentOfNode(node2)) || DOMHelper.isNodeTheSame(node4, node3)) {
            if (null != node4) {
                bl = DOMHelper.isNodeAfterSibling(node4, node, node2);
            }
        } else {
            int n2;
            int n3;
            int n4 = 2;
            int n5 = 2;
            while (node4 != null) {
                ++n4;
                node4 = DOMHelper.getParentOfNode(node4);
            }
            while (node3 != null) {
                ++n5;
                node3 = DOMHelper.getParentOfNode(node3);
            }
            Node node5 = node;
            Node node6 = node2;
            if (n4 < n5) {
                n2 = n5 - n4;
                for (n3 = 0; n3 < n2; ++n3) {
                    node6 = DOMHelper.getParentOfNode(node6);
                }
            } else if (n4 > n5) {
                n2 = n4 - n5;
                for (n3 = 0; n3 < n2; ++n3) {
                    node5 = DOMHelper.getParentOfNode(node5);
                }
            }
            Node node7 = null;
            Node node8 = null;
            while (null != node5) {
                if (node5 == node6 || DOMHelper.isNodeTheSame(node5, node6)) {
                    if (null == node7) {
                        bl = n4 < n5;
                        break;
                    }
                    bl = DOMHelper.isNodeAfterSibling(node5, node7, node8);
                    break;
                }
                node7 = node5;
                node5 = DOMHelper.getParentOfNode(node5);
                node8 = node6;
                node6 = DOMHelper.getParentOfNode(node6);
            }
        }
        return bl;
    }

    public static boolean isNodeTheSame(Node node, Node node2) {
        if (node instanceof DTMNodeProxy && node2 instanceof DTMNodeProxy) {
            return ((DTMNodeProxy)node).equals((DTMNodeProxy)node2);
        }
        return node == node2;
    }

    private static boolean isNodeAfterSibling(Node node, Node node2, Node node3) {
        boolean bl;
        bl = false;
        short s2 = node2.getNodeType();
        short s3 = node3.getNodeType();
        if (2 != s2 && 2 == s3) {
            bl = false;
        } else if (2 == s2 && 2 != s3) {
            bl = true;
        } else if (2 == s2) {
            NamedNodeMap namedNodeMap = node.getAttributes();
            int n2 = namedNodeMap.getLength();
            boolean bl2 = false;
            boolean bl3 = false;
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node4 = namedNodeMap.item(i2);
                if (node2 == node4 || DOMHelper.isNodeTheSame(node2, node4)) {
                    if (bl3) {
                        bl = false;
                        break;
                    }
                    bl2 = true;
                    continue;
                }
                if (node3 != node4 && !DOMHelper.isNodeTheSame(node3, node4)) continue;
                if (bl2) {
                    bl = true;
                    break;
                }
                bl3 = true;
            }
        } else {
            boolean bl4 = false;
            boolean bl5 = false;
            for (Node node5 = node.getFirstChild(); null != node5; node5 = node5.getNextSibling()) {
                if (node2 == node5 || DOMHelper.isNodeTheSame(node2, node5)) {
                    if (bl5) {
                        bl = false;
                        break;
                    }
                    bl4 = true;
                    continue;
                }
                if (node3 != node5 && !DOMHelper.isNodeTheSame(node3, node5)) continue;
                if (bl4) {
                    bl = true;
                    break;
                }
                bl5 = true;
            }
        }
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getNamespaceOfNode(Node node) {
        Object object;
        NSInfo nSInfo;
        boolean bl;
        String string;
        short s2 = node.getNodeType();
        if (2 != s2) {
            object = this.m_NSInfos.get(node);
            nSInfo = object == null ? null : (NSInfo)object;
            bl = nSInfo == null ? false : nSInfo.m_hasProcessedNS;
        } else {
            bl = false;
            nSInfo = null;
        }
        if (bl) {
            return nSInfo.m_namespace;
        }
        String string2 = null;
        object = node.getNodeName();
        int n2 = object.indexOf(58);
        if (2 == s2) {
            if (n2 <= 0) return string2;
            string = object.substring(0, n2);
        } else {
            string = n2 >= 0 ? object.substring(0, n2) : "";
        }
        boolean bl2 = false;
        boolean bl3 = false;
        if (string.equals("xml")) {
            string2 = "http://www.w3.org/XML/1998/namespace";
        } else {
            int n3;
            Node node2 = node;
            while (null != node2 && null == string2 && (null == nSInfo || nSInfo.m_ancestorHasXMLNSAttrs != 2)) {
                short s3 = node2.getNodeType();
                if (null == nSInfo || nSInfo.m_hasXMLNSAttrs) {
                    n3 = 0;
                    if (s3 == 1) {
                        NamedNodeMap namedNodeMap = node2.getAttributes();
                        for (int i2 = 0; i2 < namedNodeMap.getLength(); ++i2) {
                            String string3;
                            Node node3 = namedNodeMap.item(i2);
                            String string4 = node3.getNodeName();
                            if (string4.charAt(0) != 'x') continue;
                            boolean bl4 = string4.startsWith("xmlns:");
                            if (!string4.equals("xmlns") && !bl4) continue;
                            if (node == node2) {
                                bl3 = true;
                            }
                            n3 = 1;
                            bl2 = true;
                            String string5 = string3 = bl4 ? string4.substring(6) : "";
                            if (!string3.equals(string)) continue;
                            string2 = node3.getNodeValue();
                            break;
                        }
                    }
                    if (2 != s3 && null == nSInfo && node != node2) {
                        nSInfo = n3 != 0 ? m_NSInfoUnProcWithXMLNS : m_NSInfoUnProcWithoutXMLNS;
                        this.m_NSInfos.put(node2, nSInfo);
                    }
                }
                if (2 == s3) {
                    node2 = DOMHelper.getParentOfNode(node2);
                } else {
                    this.m_candidateNoAncestorXMLNS.addElement(node2);
                    this.m_candidateNoAncestorXMLNS.addElement(nSInfo);
                    node2 = node2.getParentNode();
                }
                if (null == node2) continue;
                Object v2 = this.m_NSInfos.get(node2);
                nSInfo = v2 == null ? null : (NSInfo)v2;
            }
            n3 = this.m_candidateNoAncestorXMLNS.size();
            if (n3 > 0) {
                if (!bl2 && null == node2) {
                    for (int i3 = 0; i3 < n3; i3 += 2) {
                        Object e2 = this.m_candidateNoAncestorXMLNS.elementAt(i3 + 1);
                        if (e2 == m_NSInfoUnProcWithoutXMLNS) {
                            this.m_NSInfos.put(this.m_candidateNoAncestorXMLNS.elementAt(i3), m_NSInfoUnProcNoAncestorXMLNS);
                            continue;
                        }
                        if (e2 != m_NSInfoNullWithoutXMLNS) continue;
                        this.m_NSInfos.put(this.m_candidateNoAncestorXMLNS.elementAt(i3), m_NSInfoNullNoAncestorXMLNS);
                    }
                }
                this.m_candidateNoAncestorXMLNS.removeAllElements();
            }
        }
        if (2 == s2) return string2;
        if (null == string2) {
            if (bl2) {
                if (bl3) {
                    this.m_NSInfos.put(node, m_NSInfoNullWithXMLNS);
                    return string2;
                } else {
                    this.m_NSInfos.put(node, m_NSInfoNullWithoutXMLNS);
                }
                return string2;
            } else {
                this.m_NSInfos.put(node, m_NSInfoNullNoAncestorXMLNS);
            }
            return string2;
        } else {
            this.m_NSInfos.put(node, new NSInfo(string2, bl3));
        }
        return string2;
    }

    public String getLocalNameOfNode(Node node) {
        String string = node.getNodeName();
        int n2 = string.indexOf(58);
        return n2 < 0 ? string : string.substring(n2 + 1);
    }

    public static Node getParentOfNode(Node node) throws RuntimeException {
        Node node2;
        short s2 = node.getNodeType();
        if (2 == s2) {
            Document document = node.getOwnerDocument();
            DOMImplementation dOMImplementation = document.getImplementation();
            if (dOMImplementation != null && dOMImplementation.hasFeature("Core", "2.0")) {
                Element element = ((Attr)node).getOwnerElement();
                return element;
            }
            Element element = document.getDocumentElement();
            if (null == element) {
                throw new RuntimeException(XMLMessages.createXMLMessage("ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", null));
            }
            node2 = DOMHelper.locateAttrParent(element, node);
        } else {
            node2 = node.getParentNode();
        }
        return node2;
    }

    private static Node locateAttrParent(Element element, Node node) {
        Node node2 = null;
        Attr attr = element.getAttributeNode(node.getNodeName());
        if (attr == node) {
            node2 = element;
        }
        if (null == node2) {
            for (Node node3 = element.getFirstChild(); null != node3 && (1 != node3.getNodeType() || null == (node2 = DOMHelper.locateAttrParent((Element)node3, node))); node3 = node3.getNextSibling()) {
            }
        }
        return node2;
    }
}

