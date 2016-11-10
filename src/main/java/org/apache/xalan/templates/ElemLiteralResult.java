/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemUse;
import org.apache.xalan.templates.NamespaceAlias;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.StringVector;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

public class ElemLiteralResult
extends ElemUse {
    static final long serialVersionUID = -8703409074421657260L;
    private static final String EMPTYSTRING = "";
    private boolean isLiteralResultAsStylesheet = false;
    private List m_avts = null;
    private List m_xslAttr = null;
    private String m_namespace;
    private String m_localName;
    private String m_rawName;
    private StringVector m_ExtensionElementURIs;
    private String m_version;
    private StringVector m_excludeResultPrefixes;

    public void setIsLiteralResultAsStylesheet(boolean bl) {
        this.isLiteralResultAsStylesheet = bl;
    }

    public boolean getIsLiteralResultAsStylesheet() {
        return this.isLiteralResultAsStylesheet;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
        Vector vector = composeState.getVariableNames();
        if (null != this.m_avts) {
            int n2 = this.m_avts.size();
            for (int i2 = n2 - 1; i2 >= 0; --i2) {
                AVT aVT = (AVT)this.m_avts.get(i2);
                aVT.fixupVariables(vector, composeState.getGlobalsSize());
            }
        }
    }

    public void addLiteralResultAttribute(AVT aVT) {
        if (null == this.m_avts) {
            this.m_avts = new ArrayList();
        }
        this.m_avts.add(aVT);
    }

    public void addLiteralResultAttribute(String string) {
        if (null == this.m_xslAttr) {
            this.m_xslAttr = new ArrayList();
        }
        this.m_xslAttr.add(string);
    }

    public void setXmlSpace(AVT aVT) {
        this.addLiteralResultAttribute(aVT);
        String string = aVT.getSimpleString();
        if (string.equals("default")) {
            super.setXmlSpace(2);
        } else if (string.equals("preserve")) {
            super.setXmlSpace(1);
        }
    }

    public AVT getLiteralResultAttributeNS(String string, String string2) {
        if (null != this.m_avts) {
            int n2 = this.m_avts.size();
            for (int i2 = n2 - 1; i2 >= 0; --i2) {
                AVT aVT = (AVT)this.m_avts.get(i2);
                if (!aVT.getName().equals(string2) || !aVT.getURI().equals(string)) continue;
                return aVT;
            }
        }
        return null;
    }

    public String getAttributeNS(String string, String string2) {
        AVT aVT = this.getLiteralResultAttributeNS(string, string2);
        if (null != aVT) {
            return aVT.getSimpleString();
        }
        return "";
    }

    public AVT getLiteralResultAttribute(String string) {
        if (null != this.m_avts) {
            int n2 = this.m_avts.size();
            String string2 = null;
            for (int i2 = n2 - 1; i2 >= 0; --i2) {
                AVT aVT = (AVT)this.m_avts.get(i2);
                string2 = aVT.getURI();
                if ((string2 == null || string2.length() == 0 || !(string2 + ":" + aVT.getName()).equals(string)) && (string2 != null && string2.length() != 0 || !aVT.getRawName().equals(string))) continue;
                return aVT;
            }
        }
        return null;
    }

    public String getAttribute(String string) {
        AVT aVT = this.getLiteralResultAttribute(string);
        if (null != aVT) {
            return aVT.getSimpleString();
        }
        return "";
    }

    public boolean containsExcludeResultPrefix(String string, String string2) {
        if (string2 == null || null == this.m_excludeResultPrefixes && null == this.m_ExtensionElementURIs) {
            return super.containsExcludeResultPrefix(string, string2);
        }
        if (string.length() == 0) {
            string = "#default";
        }
        if (this.m_excludeResultPrefixes != null) {
            for (int i2 = 0; i2 < this.m_excludeResultPrefixes.size(); ++i2) {
                if (!string2.equals(this.getNamespaceForPrefix(this.m_excludeResultPrefixes.elementAt(i2)))) continue;
                return true;
            }
        }
        if (this.m_ExtensionElementURIs != null && this.m_ExtensionElementURIs.contains(string2)) {
            return true;
        }
        return super.containsExcludeResultPrefix(string, string2);
    }

    public void resolvePrefixTables() throws TransformerException {
        NamespaceAlias namespaceAlias;
        super.resolvePrefixTables();
        StylesheetRoot stylesheetRoot = this.getStylesheetRoot();
        if (null != this.m_namespace && this.m_namespace.length() > 0 && null != (namespaceAlias = stylesheetRoot.getNamespaceAliasComposed(this.m_namespace))) {
            this.m_namespace = namespaceAlias.getResultNamespace();
            String string = namespaceAlias.getStylesheetPrefix();
            this.m_rawName = null != string && string.length() > 0 ? string + ":" + this.m_localName : this.m_localName;
        }
        if (null != this.m_avts) {
            int n2 = this.m_avts.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                NamespaceAlias namespaceAlias2;
                AVT aVT = (AVT)this.m_avts.get(i2);
                String string = aVT.getURI();
                if (null == string || string.length() <= 0 || null == (namespaceAlias2 = stylesheetRoot.getNamespaceAliasComposed(this.m_namespace))) continue;
                String string2 = namespaceAlias2.getResultNamespace();
                String string3 = namespaceAlias2.getStylesheetPrefix();
                String string4 = aVT.getName();
                if (null != string3 && string3.length() > 0) {
                    string4 = string3 + ":" + string4;
                }
                aVT.setURI(string2);
                aVT.setRawName(string4);
            }
        }
    }

    boolean needToCheckExclude() {
        if (null == this.m_excludeResultPrefixes && null == this.getPrefixTable() && this.m_ExtensionElementURIs == null) {
            return false;
        }
        if (null == this.getPrefixTable()) {
            this.setPrefixTable(new ArrayList());
        }
        return true;
    }

    public void setNamespace(String string) {
        if (null == string) {
            string = "";
        }
        this.m_namespace = string;
    }

    public String getNamespace() {
        return this.m_namespace;
    }

    public void setLocalName(String string) {
        this.m_localName = string;
    }

    public String getLocalName() {
        return this.m_localName;
    }

    public void setRawName(String string) {
        this.m_rawName = string;
    }

    public String getRawName() {
        return this.m_rawName;
    }

    public String getPrefix() {
        int n2 = this.m_rawName.length() - this.m_localName.length() - 1;
        return n2 > 0 ? this.m_rawName.substring(0, n2) : "";
    }

    public void setExtensionElementPrefixes(StringVector stringVector) {
        this.m_ExtensionElementURIs = stringVector;
    }

    public NamedNodeMap getAttributes() {
        return new LiteralElementAttributes(this);
    }

    public String getExtensionElementPrefix(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_ExtensionElementURIs) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.m_ExtensionElementURIs.elementAt(n2);
    }

    public int getExtensionElementPrefixCount() {
        return null != this.m_ExtensionElementURIs ? this.m_ExtensionElementURIs.size() : 0;
    }

    public boolean containsExtensionElementURI(String string) {
        if (null == this.m_ExtensionElementURIs) {
            return false;
        }
        return this.m_ExtensionElementURIs.contains(string);
    }

    public int getXSLToken() {
        return 77;
    }

    public String getNodeName() {
        return this.m_rawName;
    }

    public void setVersion(String string) {
        this.m_version = string;
    }

    public String getVersion() {
        return this.m_version;
    }

    public void setExcludeResultPrefixes(StringVector stringVector) {
        this.m_excludeResultPrefixes = stringVector;
    }

    private boolean excludeResultNSDecl(String string, String string2) throws TransformerException {
        if (null != this.m_excludeResultPrefixes) {
            return this.containsExcludeResultPrefix(string, string2);
        }
        return false;
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        SerializationHandler serializationHandler = transformerImpl.getSerializationHandler();
        try {
            if (transformerImpl.getDebug()) {
                serializationHandler.flushPending();
                transformerImpl.getTraceManager().fireTraceEvent(this);
            }
            serializationHandler.startPrefixMapping(this.getPrefix(), this.getNamespace());
            this.executeNSDecls(transformerImpl);
            serializationHandler.startElement(this.getNamespace(), this.getLocalName(), this.getRawName());
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
        TransformerException transformerException = null;
        try {
            super.execute(transformerImpl);
            if (null != this.m_avts) {
                int n2 = this.m_avts.size();
                for (int i2 = n2 - 1; i2 >= 0; --i2) {
                    int n3;
                    XPathContext xPathContext;
                    AVT aVT = (AVT)this.m_avts.get(i2);
                    String string = aVT.evaluate(xPathContext = transformerImpl.getXPathContext(), n3 = xPathContext.getCurrentNode(), this);
                    if (null == string) continue;
                    serializationHandler.addAttribute(aVT.getURI(), aVT.getName(), aVT.getRawName(), "CDATA", string, false);
                }
            }
            transformerImpl.executeChildTemplates((ElemTemplateElement)this, true);
        }
        catch (TransformerException transformerException2) {
            transformerException = transformerException2;
        }
        catch (SAXException sAXException) {
            transformerException = new TransformerException(sAXException);
        }
        try {
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
            serializationHandler.endElement(this.getNamespace(), this.getLocalName(), this.getRawName());
        }
        catch (SAXException sAXException) {
            if (transformerException != null) {
                throw transformerException;
            }
            throw new TransformerException(sAXException);
        }
        if (transformerException != null) {
            throw transformerException;
        }
        this.unexecuteNSDecls(transformerImpl);
        try {
            serializationHandler.endPrefixMapping(this.getPrefix());
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
    }

    public Iterator enumerateLiteralResultAttributes() {
        return null == this.m_avts ? null : this.m_avts.iterator();
    }

    protected boolean accept(XSLTVisitor xSLTVisitor) {
        return xSLTVisitor.visitLiteralResultElement(this);
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (bl && null != this.m_avts) {
            int n2 = this.m_avts.size();
            for (int i2 = n2 - 1; i2 >= 0; --i2) {
                AVT aVT = (AVT)this.m_avts.get(i2);
                aVT.callVisitors(xSLTVisitor);
            }
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }

    public void throwDOMException(short s2, String string) {
        String string2 = XSLMessages.createMessage(string, null);
        throw new DOMException(s2, string2);
    }

    static List access$000(ElemLiteralResult elemLiteralResult) {
        return elemLiteralResult.m_avts;
    }

    public class Attribute
    implements Attr {
        private AVT m_attribute;
        private Element m_owner;
        private final ElemLiteralResult this$0;

        public Attribute(ElemLiteralResult elemLiteralResult, AVT aVT, Element element) {
            this.this$0 = elemLiteralResult;
            this.m_owner = null;
            this.m_attribute = aVT;
            this.m_owner = element;
        }

        public Node appendChild(Node node) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
            return null;
        }

        public Node cloneNode(boolean bl) {
            return new Attribute(this.this$0, this.m_attribute, this.m_owner);
        }

        public NamedNodeMap getAttributes() {
            return null;
        }

        public NodeList getChildNodes() {
            return new NodeList(this){
                private final Attribute this$1;

                public int getLength() {
                    return 0;
                }

                public Node item(int n2) {
                    return null;
                }
            };
        }

        public Node getFirstChild() {
            return null;
        }

        public Node getLastChild() {
            return null;
        }

        public String getLocalName() {
            return this.m_attribute.getName();
        }

        public String getNamespaceURI() {
            String string = this.m_attribute.getURI();
            return string.length() == 0 ? null : string;
        }

        public Node getNextSibling() {
            return null;
        }

        public String getNodeName() {
            String string = this.m_attribute.getURI();
            String string2 = this.getLocalName();
            return string.length() == 0 ? string2 : string + ":" + string2;
        }

        public short getNodeType() {
            return 2;
        }

        public String getNodeValue() throws DOMException {
            return this.m_attribute.getSimpleString();
        }

        public Document getOwnerDocument() {
            return this.m_owner.getOwnerDocument();
        }

        public Node getParentNode() {
            return this.m_owner;
        }

        public String getPrefix() {
            String string = this.m_attribute.getURI();
            String string2 = this.m_attribute.getRawName();
            return string.length() == 0 ? null : string2.substring(0, string2.indexOf(":"));
        }

        public Node getPreviousSibling() {
            return null;
        }

        public boolean hasAttributes() {
            return false;
        }

        public boolean hasChildNodes() {
            return false;
        }

        public Node insertBefore(Node node, Node node2) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
            return null;
        }

        public boolean isSupported(String string, String string2) {
            return false;
        }

        public void normalize() {
        }

        public Node removeChild(Node node) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
            return null;
        }

        public Node replaceChild(Node node, Node node2) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
            return null;
        }

        public void setNodeValue(String string) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
        }

        public void setPrefix(String string) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
        }

        public String getName() {
            return this.m_attribute.getName();
        }

        public String getValue() {
            return this.m_attribute.getSimpleString();
        }

        public Element getOwnerElement() {
            return this.m_owner;
        }

        public boolean getSpecified() {
            return true;
        }

        public void setValue(String string) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
        }

        public TypeInfo getSchemaTypeInfo() {
            return null;
        }

        public boolean isId() {
            return false;
        }

        public Object setUserData(String string, Object object, UserDataHandler userDataHandler) {
            return this.getOwnerDocument().setUserData(string, object, userDataHandler);
        }

        public Object getUserData(String string) {
            return this.getOwnerDocument().getUserData(string);
        }

        public Object getFeature(String string, String string2) {
            return this.isSupported(string, string2) ? this : null;
        }

        public boolean isEqualNode(Node node) {
            return node == this;
        }

        public String lookupNamespaceURI(String string) {
            return null;
        }

        public boolean isDefaultNamespace(String string) {
            return false;
        }

        public String lookupPrefix(String string) {
            return null;
        }

        public boolean isSameNode(Node node) {
            return this == node;
        }

        public void setTextContent(String string) throws DOMException {
            this.setNodeValue(string);
        }

        public String getTextContent() throws DOMException {
            return this.getNodeValue();
        }

        public short compareDocumentPosition(Node node) throws DOMException {
            return 0;
        }

        public String getBaseURI() {
            return null;
        }

    }

    public class LiteralElementAttributes
    implements NamedNodeMap {
        private int m_count;
        private final ElemLiteralResult this$0;

        public LiteralElementAttributes(ElemLiteralResult elemLiteralResult) {
            this.this$0 = elemLiteralResult;
            this.m_count = -1;
        }

        public int getLength() {
            if (this.m_count == -1) {
                this.m_count = null != ElemLiteralResult.access$000(this.this$0) ? ElemLiteralResult.access$000(this.this$0).size() : 0;
            }
            return this.m_count;
        }

        public Node getNamedItem(String string) {
            if (this.getLength() == 0) {
                return null;
            }
            String string2 = null;
            String string3 = string;
            int n2 = string.indexOf(":");
            if (-1 != n2) {
                string2 = string.substring(0, n2);
                string3 = string.substring(n2 + 1);
            }
            Attribute attribute = null;
            Iterator iterator = ElemLiteralResult.access$000(this.this$0).iterator();
            while (iterator.hasNext()) {
                AVT aVT = (AVT)iterator.next();
                if (!string3.equals(aVT.getName())) continue;
                String string4 = aVT.getURI();
                if ((string2 != null || string4 != null) && (string2 == null || !string2.equals(string4))) continue;
                attribute = new Attribute(this.this$0, aVT, this.this$0);
                break;
            }
            return attribute;
        }

        public Node getNamedItemNS(String string, String string2) {
            if (this.getLength() == 0) {
                return null;
            }
            Attribute attribute = null;
            Iterator iterator = ElemLiteralResult.access$000(this.this$0).iterator();
            while (iterator.hasNext()) {
                AVT aVT = (AVT)iterator.next();
                if (!string2.equals(aVT.getName())) continue;
                String string3 = aVT.getURI();
                if ((string != null || string3 != null) && (string == null || !string.equals(string3))) continue;
                attribute = new Attribute(this.this$0, aVT, this.this$0);
                break;
            }
            return attribute;
        }

        public Node item(int n2) {
            if (this.getLength() == 0 || n2 >= ElemLiteralResult.access$000(this.this$0).size()) {
                return null;
            }
            return new Attribute(this.this$0, (AVT)ElemLiteralResult.access$000(this.this$0).get(n2), this.this$0);
        }

        public Node removeNamedItem(String string) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
            return null;
        }

        public Node removeNamedItemNS(String string, String string2) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
            return null;
        }

        public Node setNamedItem(Node node) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
            return null;
        }

        public Node setNamedItemNS(Node node) throws DOMException {
            this.this$0.throwDOMException(7, "NO_MODIFICATION_ALLOWED_ERR");
            return null;
        }
    }

}

