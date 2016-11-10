/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import org.apache.xerces.impl.xs.opti.AttrImpl;
import org.apache.xerces.impl.xs.opti.DefaultDocument;
import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.apache.xerces.impl.xs.opti.NodeImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SchemaDOM
extends DefaultDocument {
    static final int relationsRowResizeFactor = 15;
    static final int relationsColResizeFactor = 10;
    NodeImpl[][] relations;
    ElementImpl parent;
    int currLoc;
    int nextFreeLoc;
    boolean hidden;
    boolean inCDATA;
    StringBuffer fAnnotationBuffer = null;

    public SchemaDOM() {
        this.reset();
    }

    public ElementImpl startElement(QName qName, XMLAttributes xMLAttributes, int n2, int n3, int n4) {
        ElementImpl elementImpl = new ElementImpl(n2, n3, n4);
        this.processElement(qName, xMLAttributes, elementImpl);
        this.parent = elementImpl;
        return elementImpl;
    }

    public ElementImpl emptyElement(QName qName, XMLAttributes xMLAttributes, int n2, int n3, int n4) {
        ElementImpl elementImpl = new ElementImpl(n2, n3, n4);
        this.processElement(qName, xMLAttributes, elementImpl);
        return elementImpl;
    }

    public ElementImpl startElement(QName qName, XMLAttributes xMLAttributes, int n2, int n3) {
        return this.startElement(qName, xMLAttributes, n2, n3, -1);
    }

    public ElementImpl emptyElement(QName qName, XMLAttributes xMLAttributes, int n2, int n3) {
        return this.emptyElement(qName, xMLAttributes, n2, n3, -1);
    }

    private void processElement(QName qName, XMLAttributes xMLAttributes, ElementImpl elementImpl) {
        elementImpl.prefix = qName.prefix;
        elementImpl.localpart = qName.localpart;
        elementImpl.rawname = qName.rawname;
        elementImpl.uri = qName.uri;
        elementImpl.schemaDOM = this;
        Attr[] arrattr = new Attr[xMLAttributes.getLength()];
        int n2 = 0;
        while (n2 < xMLAttributes.getLength()) {
            arrattr[n2] = new AttrImpl(null, xMLAttributes.getPrefix(n2), xMLAttributes.getLocalName(n2), xMLAttributes.getQName(n2), xMLAttributes.getURI(n2), xMLAttributes.getValue(n2));
            ++n2;
        }
        elementImpl.attrs = arrattr;
        if (this.nextFreeLoc == this.relations.length) {
            this.resizeRelations();
        }
        if (this.relations[this.currLoc][0] != this.parent) {
            this.relations[this.nextFreeLoc][0] = this.parent;
            this.currLoc = this.nextFreeLoc++;
        }
        boolean bl = false;
        int n3 = 1;
        n3 = 1;
        while (n3 < this.relations[this.currLoc].length) {
            if (this.relations[this.currLoc][n3] == null) {
                bl = true;
                break;
            }
            ++n3;
        }
        if (!bl) {
            this.resizeRelations(this.currLoc);
        }
        this.relations[this.currLoc][n3] = elementImpl;
        this.parent.parentRow = this.currLoc;
        elementImpl.row = this.currLoc;
        elementImpl.col = n3;
    }

    public void endElement() {
        this.currLoc = this.parent.row;
        this.parent = (ElementImpl)this.relations[this.currLoc][0];
    }

    void comment(XMLString xMLString) {
        this.fAnnotationBuffer.append("<!--");
        if (xMLString.length > 0) {
            this.fAnnotationBuffer.append(xMLString.ch, xMLString.offset, xMLString.length);
        }
        this.fAnnotationBuffer.append("-->");
    }

    void processingInstruction(String string, XMLString xMLString) {
        this.fAnnotationBuffer.append("<?").append(string);
        if (xMLString.length > 0) {
            this.fAnnotationBuffer.append(' ').append(xMLString.ch, xMLString.offset, xMLString.length);
        }
        this.fAnnotationBuffer.append("?>");
    }

    void characters(XMLString xMLString) {
        if (!this.inCDATA) {
            int n2 = xMLString.offset;
            while (n2 < xMLString.offset + xMLString.length) {
                char c2 = xMLString.ch[n2];
                if (c2 == '&') {
                    this.fAnnotationBuffer.append("&amp;");
                } else if (c2 == '<') {
                    this.fAnnotationBuffer.append("&lt;");
                } else if (c2 == '>') {
                    this.fAnnotationBuffer.append("&gt;");
                } else if (c2 == '\r') {
                    this.fAnnotationBuffer.append("&#xD;");
                } else {
                    this.fAnnotationBuffer.append(c2);
                }
                ++n2;
            }
        } else {
            this.fAnnotationBuffer.append(xMLString.ch, xMLString.offset, xMLString.length);
        }
    }

    void endAnnotation(QName qName, ElementImpl elementImpl) {
        this.fAnnotationBuffer.append("\n</").append(qName.rawname).append(">");
        elementImpl.fAnnotation = this.fAnnotationBuffer.toString();
        this.fAnnotationBuffer = null;
    }

    void endAnnotationElement(QName qName) {
        this.fAnnotationBuffer.append("</").append(qName.rawname).append(">");
    }

    void endSyntheticAnnotationElement(QName qName, boolean bl) {
        if (bl) {
            this.fAnnotationBuffer.append("\n</").append(qName.rawname).append(">");
            this.parent.fSyntheticAnnotation = this.fAnnotationBuffer.toString();
            this.fAnnotationBuffer = null;
        } else {
            this.fAnnotationBuffer.append("</").append(qName.rawname).append(">");
        }
    }

    void startAnnotationCDATA() {
        this.inCDATA = true;
        this.fAnnotationBuffer.append("<![CDATA[");
    }

    void endAnnotationCDATA() {
        this.fAnnotationBuffer.append("]]>");
        this.inCDATA = false;
    }

    private void resizeRelations() {
        NodeImpl[][] arrarrnodeImpl = new NodeImpl[this.relations.length + 15][];
        System.arraycopy(this.relations, 0, arrarrnodeImpl, 0, this.relations.length);
        int n2 = this.relations.length;
        while (n2 < arrarrnodeImpl.length) {
            arrarrnodeImpl[n2] = new NodeImpl[10];
            ++n2;
        }
        this.relations = arrarrnodeImpl;
    }

    private void resizeRelations(int n2) {
        NodeImpl[] arrnodeImpl = new NodeImpl[this.relations[n2].length + 10];
        System.arraycopy(this.relations[n2], 0, arrnodeImpl, 0, this.relations[n2].length);
        this.relations[n2] = arrnodeImpl;
    }

    public void reset() {
        if (this.relations != null) {
            int n2 = 0;
            while (n2 < this.relations.length) {
                int n3 = 0;
                while (n3 < this.relations[n2].length) {
                    this.relations[n2][n3] = null;
                    ++n3;
                }
                ++n2;
            }
        }
        this.relations = new NodeImpl[15][];
        this.parent = new ElementImpl(0, 0, 0);
        this.parent.rawname = "DOCUMENT_NODE";
        this.currLoc = 0;
        this.nextFreeLoc = 1;
        this.inCDATA = false;
        int n4 = 0;
        while (n4 < 15) {
            this.relations[n4] = new NodeImpl[10];
            ++n4;
        }
        this.relations[this.currLoc][0] = this.parent;
    }

    public void printDOM() {
    }

    public static void traverse(Node node, int n2) {
        Object object;
        SchemaDOM.indent(n2);
        System.out.print("<" + node.getNodeName());
        if (node.hasAttributes()) {
            object = node.getAttributes();
            int n3 = 0;
            while (n3 < object.getLength()) {
                System.out.print("  " + ((Attr)object.item(n3)).getName() + "=\"" + ((Attr)object.item(n3)).getValue() + "\"");
                ++n3;
            }
        }
        if (node.hasChildNodes()) {
            System.out.println(">");
            n2 += 4;
            object = node.getFirstChild();
            while (object != null) {
                SchemaDOM.traverse((Node)object, n2);
                object = object.getNextSibling();
            }
            SchemaDOM.indent(n2 -= 4);
            System.out.println("</" + node.getNodeName() + ">");
        } else {
            System.out.println("/>");
        }
    }

    public static void indent(int n2) {
        int n3 = 0;
        while (n3 < n2) {
            System.out.print(' ');
            ++n3;
        }
    }

    public Element getDocumentElement() {
        return (ElementImpl)this.relations[0][1];
    }

    void startAnnotation(QName qName, XMLAttributes xMLAttributes, NamespaceContext namespaceContext) {
        String string;
        Object object;
        String string2;
        if (this.fAnnotationBuffer == null) {
            this.fAnnotationBuffer = new StringBuffer(256);
        }
        this.fAnnotationBuffer.append("<").append(qName.rawname).append(" ");
        ArrayList<String> arrayList = new ArrayList<String>();
        int n2 = 0;
        while (n2 < xMLAttributes.getLength()) {
            object = xMLAttributes.getValue(n2);
            string2 = xMLAttributes.getPrefix(n2);
            string = xMLAttributes.getQName(n2);
            if (string2 == XMLSymbols.PREFIX_XMLNS || string == XMLSymbols.PREFIX_XMLNS) {
                arrayList.add(string2 == XMLSymbols.PREFIX_XMLNS ? xMLAttributes.getLocalName(n2) : XMLSymbols.EMPTY_STRING);
            }
            this.fAnnotationBuffer.append(string).append("=\"").append(SchemaDOM.processAttValue((String)object)).append("\" ");
            ++n2;
        }
        object = namespaceContext.getAllPrefixes();
        while (object.hasMoreElements()) {
            string2 = (String)object.nextElement();
            string = namespaceContext.getURI(string2);
            if (string == null) {
                string = XMLSymbols.EMPTY_STRING;
            }
            if (arrayList.contains(string2)) continue;
            if (string2 == XMLSymbols.EMPTY_STRING) {
                this.fAnnotationBuffer.append("xmlns").append("=\"").append(SchemaDOM.processAttValue(string)).append("\" ");
                continue;
            }
            this.fAnnotationBuffer.append("xmlns:").append(string2).append("=\"").append(SchemaDOM.processAttValue(string)).append("\" ");
        }
        this.fAnnotationBuffer.append(">\n");
    }

    void startAnnotationElement(QName qName, XMLAttributes xMLAttributes) {
        this.fAnnotationBuffer.append("<").append(qName.rawname);
        int n2 = 0;
        while (n2 < xMLAttributes.getLength()) {
            String string = xMLAttributes.getValue(n2);
            this.fAnnotationBuffer.append(" ").append(xMLAttributes.getQName(n2)).append("=\"").append(SchemaDOM.processAttValue(string)).append("\"");
            ++n2;
        }
        this.fAnnotationBuffer.append(">");
    }

    private static String processAttValue(String string) {
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (c2 == '\"' || c2 == '<' || c2 == '&' || c2 == '\t' || c2 == '\n' || c2 == '\r') {
                return SchemaDOM.escapeAttValue(string, n3);
            }
            ++n3;
        }
        return string;
    }

    private static String escapeAttValue(String string, int n2) {
        int n3 = string.length();
        StringBuffer stringBuffer = new StringBuffer(n3);
        stringBuffer.append(string.substring(0, n2));
        int n4 = n2;
        while (n4 < n3) {
            char c2 = string.charAt(n4);
            if (c2 == '\"') {
                stringBuffer.append("&quot;");
            } else if (c2 == '<') {
                stringBuffer.append("&lt;");
            } else if (c2 == '&') {
                stringBuffer.append("&amp;");
            } else if (c2 == '\t') {
                stringBuffer.append("&#x9;");
            } else if (c2 == '\n') {
                stringBuffer.append("&#xA;");
            } else if (c2 == '\r') {
                stringBuffer.append("&#xD;");
            } else {
                stringBuffer.append(c2);
            }
            ++n4;
        }
        return stringBuffer.toString();
    }
}

