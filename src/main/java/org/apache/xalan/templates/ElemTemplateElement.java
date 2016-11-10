/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.NamespaceAlias;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XMLNSDecl;
import org.apache.xalan.templates.XSLTVisitable;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.UnImplNode;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.XPathContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

public class ElemTemplateElement
extends UnImplNode
implements Serializable,
XSLTVisitable,
PrefixResolver,
ExpressionNode {
    static final long serialVersionUID = 4440018597841834447L;
    private int m_lineNumber;
    private int m_endLineNumber;
    private int m_columnNumber;
    private int m_endColumnNumber;
    private boolean m_defaultSpace = true;
    private boolean m_hasTextLitOnly = false;
    protected boolean m_hasVariableDecl = false;
    private List m_declaredPrefixes;
    private List m_prefixTable;
    protected int m_docOrderNumber = -1;
    protected ElemTemplateElement m_parentNode;
    ElemTemplateElement m_nextSibling;
    ElemTemplateElement m_firstChild;
    private transient Node m_DOMBackPointer;

    public boolean isCompiledTemplate() {
        return false;
    }

    public int getXSLToken() {
        return -1;
    }

    public String getNodeName() {
        return "Unknown XSLT Element";
    }

    public String getLocalName() {
        return this.getNodeName();
    }

    public void runtimeInit(TransformerImpl transformerImpl) throws TransformerException {
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
    }

    public StylesheetComposed getStylesheetComposed() {
        return this.m_parentNode.getStylesheetComposed();
    }

    public Stylesheet getStylesheet() {
        return null == this.m_parentNode ? null : this.m_parentNode.getStylesheet();
    }

    public StylesheetRoot getStylesheetRoot() {
        return this.m_parentNode.getStylesheetRoot();
    }

    public void recompose(StylesheetRoot stylesheetRoot) throws TransformerException {
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        this.resolvePrefixTables();
        ElemTemplateElement elemTemplateElement = this.getFirstChildElem();
        this.m_hasTextLitOnly = elemTemplateElement != null && elemTemplateElement.getXSLToken() == 78 && elemTemplateElement.getNextSiblingElem() == null;
        StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
        composeState.pushStackMark();
    }

    public void endCompose(StylesheetRoot stylesheetRoot) throws TransformerException {
        StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
        composeState.popStackMark();
    }

    public void error(String string, Object[] arrobject) {
        String string2 = XSLMessages.createMessage(string, arrobject);
        throw new RuntimeException(XSLMessages.createMessage("ER_ELEMTEMPLATEELEM_ERR", new Object[]{string2}));
    }

    public void error(String string) {
        this.error(string, null);
    }

    public Node appendChild(Node node) throws DOMException {
        if (null == node) {
            this.error("ER_NULL_CHILD", null);
        }
        ElemTemplateElement elemTemplateElement = (ElemTemplateElement)node;
        if (null == this.m_firstChild) {
            this.m_firstChild = elemTemplateElement;
        } else {
            ElemTemplateElement elemTemplateElement2 = (ElemTemplateElement)this.getLastChild();
            elemTemplateElement2.m_nextSibling = elemTemplateElement;
        }
        elemTemplateElement.m_parentNode = this;
        return node;
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        if (null == elemTemplateElement) {
            this.error("ER_NULL_CHILD", null);
        }
        if (null == this.m_firstChild) {
            this.m_firstChild = elemTemplateElement;
        } else {
            ElemTemplateElement elemTemplateElement2 = this.getLastChildElem();
            elemTemplateElement2.m_nextSibling = elemTemplateElement;
        }
        elemTemplateElement.setParentElem(this);
        return elemTemplateElement;
    }

    public boolean hasChildNodes() {
        return null != this.m_firstChild;
    }

    public short getNodeType() {
        return 1;
    }

    public NodeList getChildNodes() {
        return this;
    }

    public ElemTemplateElement removeChild(ElemTemplateElement elemTemplateElement) {
        if (elemTemplateElement == null || elemTemplateElement.m_parentNode != this) {
            return null;
        }
        if (elemTemplateElement == this.m_firstChild) {
            this.m_firstChild = elemTemplateElement.m_nextSibling;
        } else {
            ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getPreviousSiblingElem();
            elemTemplateElement2.m_nextSibling = elemTemplateElement.m_nextSibling;
        }
        elemTemplateElement.m_parentNode = null;
        elemTemplateElement.m_nextSibling = null;
        return elemTemplateElement;
    }

    public Node replaceChild(Node node, Node node2) throws DOMException {
        if (node2 == null || node2.getParentNode() != this) {
            return null;
        }
        ElemTemplateElement elemTemplateElement = (ElemTemplateElement)node;
        ElemTemplateElement elemTemplateElement2 = (ElemTemplateElement)node2;
        ElemTemplateElement elemTemplateElement3 = (ElemTemplateElement)elemTemplateElement2.getPreviousSibling();
        if (null != elemTemplateElement3) {
            elemTemplateElement3.m_nextSibling = elemTemplateElement;
        }
        if (this.m_firstChild == elemTemplateElement2) {
            this.m_firstChild = elemTemplateElement;
        }
        elemTemplateElement.m_parentNode = this;
        elemTemplateElement2.m_parentNode = null;
        elemTemplateElement.m_nextSibling = elemTemplateElement2.m_nextSibling;
        elemTemplateElement2.m_nextSibling = null;
        return elemTemplateElement;
    }

    public Node insertBefore(Node node, Node node2) throws DOMException {
        if (null == node2) {
            this.appendChild(node);
            return node;
        }
        if (node == node2) {
            return node;
        }
        Node node3 = this.m_firstChild;
        Node node4 = null;
        boolean bl = false;
        while (null != node3) {
            if (node == node3) {
                if (null != node4) {
                    ((ElemTemplateElement)node4).m_nextSibling = (ElemTemplateElement)node3.getNextSibling();
                } else {
                    this.m_firstChild = (ElemTemplateElement)node3.getNextSibling();
                }
                node3 = node3.getNextSibling();
                continue;
            }
            if (node2 == node3) {
                if (null != node4) {
                    node4.m_nextSibling = (ElemTemplateElement)node;
                } else {
                    this.m_firstChild = (ElemTemplateElement)node;
                }
                ((ElemTemplateElement)node).m_nextSibling = (ElemTemplateElement)node2;
                ((ElemTemplateElement)node).setParentElem(this);
                node4 = node;
                node3 = node3.getNextSibling();
                bl = true;
                continue;
            }
            node4 = node3;
            node3 = node3.getNextSibling();
        }
        if (!bl) {
            throw new DOMException(8, "refChild was not found in insertBefore method!");
        }
        return node;
    }

    public ElemTemplateElement replaceChild(ElemTemplateElement elemTemplateElement, ElemTemplateElement elemTemplateElement2) {
        if (elemTemplateElement2 == null || elemTemplateElement2.getParentElem() != this) {
            return null;
        }
        ElemTemplateElement elemTemplateElement3 = elemTemplateElement2.getPreviousSiblingElem();
        if (null != elemTemplateElement3) {
            elemTemplateElement3.m_nextSibling = elemTemplateElement;
        }
        if (this.m_firstChild == elemTemplateElement2) {
            this.m_firstChild = elemTemplateElement;
        }
        elemTemplateElement.m_parentNode = this;
        elemTemplateElement2.m_parentNode = null;
        elemTemplateElement.m_nextSibling = elemTemplateElement2.m_nextSibling;
        elemTemplateElement2.m_nextSibling = null;
        return elemTemplateElement;
    }

    public int getLength() {
        int n2 = 0;
        ElemTemplateElement elemTemplateElement = this.m_firstChild;
        while (elemTemplateElement != null) {
            ++n2;
            elemTemplateElement = elemTemplateElement.m_nextSibling;
        }
        return n2;
    }

    public Node item(int n2) {
        ElemTemplateElement elemTemplateElement = this.m_firstChild;
        for (int i2 = 0; i2 < n2 && elemTemplateElement != null; ++i2) {
            elemTemplateElement = elemTemplateElement.m_nextSibling;
        }
        return elemTemplateElement;
    }

    public Document getOwnerDocument() {
        return this.getStylesheet();
    }

    public ElemTemplate getOwnerXSLTemplate() {
        ElemTemplateElement elemTemplateElement = this;
        int n2 = elemTemplateElement.getXSLToken();
        while (null != elemTemplateElement && n2 != 19) {
            if (null == (elemTemplateElement = elemTemplateElement.getParentElem())) continue;
            n2 = elemTemplateElement.getXSLToken();
        }
        return (ElemTemplate)elemTemplateElement;
    }

    public String getTagName() {
        return this.getNodeName();
    }

    public boolean hasTextLitOnly() {
        return this.m_hasTextLitOnly;
    }

    public String getBaseIdentifier() {
        return this.getSystemId();
    }

    public int getEndLineNumber() {
        return this.m_endLineNumber;
    }

    public int getLineNumber() {
        return this.m_lineNumber;
    }

    public int getEndColumnNumber() {
        return this.m_endColumnNumber;
    }

    public int getColumnNumber() {
        return this.m_columnNumber;
    }

    public String getPublicId() {
        return null != this.m_parentNode ? this.m_parentNode.getPublicId() : null;
    }

    public String getSystemId() {
        Stylesheet stylesheet = this.getStylesheet();
        return stylesheet == null ? null : stylesheet.getHref();
    }

    public void setLocaterInfo(SourceLocator sourceLocator) {
        this.m_lineNumber = sourceLocator.getLineNumber();
        this.m_columnNumber = sourceLocator.getColumnNumber();
    }

    public void setEndLocaterInfo(SourceLocator sourceLocator) {
        this.m_endLineNumber = sourceLocator.getLineNumber();
        this.m_endColumnNumber = sourceLocator.getColumnNumber();
    }

    public boolean hasVariableDecl() {
        return this.m_hasVariableDecl;
    }

    public void setXmlSpace(int n2) {
        this.m_defaultSpace = 2 == n2;
    }

    public boolean getXmlSpace() {
        return this.m_defaultSpace;
    }

    public List getDeclaredPrefixes() {
        return this.m_declaredPrefixes;
    }

    public void setPrefixes(NamespaceSupport namespaceSupport) throws TransformerException {
        this.setPrefixes(namespaceSupport, false);
    }

    public void setPrefixes(NamespaceSupport namespaceSupport, boolean bl) throws TransformerException {
        Enumeration enumeration = namespaceSupport.getDeclaredPrefixes();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            if (null == this.m_declaredPrefixes) {
                this.m_declaredPrefixes = new ArrayList();
            }
            String string2 = namespaceSupport.getURI(string);
            if (bl && string2.equals("http://www.w3.org/1999/XSL/Transform")) continue;
            XMLNSDecl xMLNSDecl = new XMLNSDecl(string, string2, false);
            this.m_declaredPrefixes.add(xMLNSDecl);
        }
    }

    public String getNamespaceForPrefix(String string, Node node) {
        this.error("ER_CANT_RESOLVE_NSPREFIX", null);
        return null;
    }

    public String getNamespaceForPrefix(String string) {
        List list = this.m_declaredPrefixes;
        if (null != list) {
            int n2 = list.size();
            if (string.equals("#default")) {
                string = "";
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                XMLNSDecl xMLNSDecl = (XMLNSDecl)list.get(i2);
                if (!string.equals(xMLNSDecl.getPrefix())) continue;
                return xMLNSDecl.getURI();
            }
        }
        if (null != this.m_parentNode) {
            return this.m_parentNode.getNamespaceForPrefix(string);
        }
        if ("xml".equals(string)) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        return null;
    }

    List getPrefixTable() {
        return this.m_prefixTable;
    }

    void setPrefixTable(List list) {
        this.m_prefixTable = list;
    }

    public boolean containsExcludeResultPrefix(String string, String string2) {
        ElemTemplateElement elemTemplateElement = this.getParentElem();
        if (null != elemTemplateElement) {
            return elemTemplateElement.containsExcludeResultPrefix(string, string2);
        }
        return false;
    }

    private boolean excludeResultNSDecl(String string, String string2) throws TransformerException {
        if (string2 != null) {
            if (string2.equals("http://www.w3.org/1999/XSL/Transform") || this.getStylesheet().containsExtensionElementURI(string2)) {
                return true;
            }
            if (this.containsExcludeResultPrefix(string, string2)) {
                return true;
            }
        }
        return false;
    }

    public void resolvePrefixTables() throws TransformerException {
        Object object;
        int n2;
        StylesheetRoot stylesheetRoot;
        this.setPrefixTable(null);
        if (null != this.m_declaredPrefixes) {
            stylesheetRoot = this.getStylesheetRoot();
            int n3 = this.m_declaredPrefixes.size();
            for (n2 = 0; n2 < n3; ++n2) {
                NamespaceAlias namespaceAlias;
                XMLNSDecl xMLNSDecl = (XMLNSDecl)this.m_declaredPrefixes.get(n2);
                object = xMLNSDecl.getPrefix();
                String string = xMLNSDecl.getURI();
                if (null == string) {
                    string = "";
                }
                boolean bl = this.excludeResultNSDecl((String)object, string);
                if (null == this.m_prefixTable) {
                    this.setPrefixTable(new ArrayList());
                }
                xMLNSDecl = null != (namespaceAlias = stylesheetRoot.getNamespaceAliasComposed(string)) ? new XMLNSDecl(namespaceAlias.getStylesheetPrefix(), namespaceAlias.getResultNamespace(), bl) : new XMLNSDecl((String)object, string, bl);
                this.m_prefixTable.add(xMLNSDecl);
            }
        }
        if (null != (stylesheetRoot = this.getParentNodeElem())) {
            List list = stylesheetRoot.m_prefixTable;
            if (null == this.m_prefixTable && !this.needToCheckExclude()) {
                this.setPrefixTable(stylesheetRoot.m_prefixTable);
            } else {
                n2 = list.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    object = (XMLNSDecl)list.get(i2);
                    boolean bl = this.excludeResultNSDecl(object.getPrefix(), object.getURI());
                    if (bl != object.getIsExcluded()) {
                        object = new XMLNSDecl(object.getPrefix(), object.getURI(), bl);
                    }
                    this.addOrReplaceDecls((XMLNSDecl)object);
                }
            }
        } else if (null == this.m_prefixTable) {
            this.setPrefixTable(new ArrayList());
        }
    }

    void addOrReplaceDecls(XMLNSDecl xMLNSDecl) {
        int n2 = this.m_prefixTable.size();
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            XMLNSDecl xMLNSDecl2 = (XMLNSDecl)this.m_prefixTable.get(i2);
            if (!xMLNSDecl2.getPrefix().equals(xMLNSDecl.getPrefix())) continue;
            return;
        }
        this.m_prefixTable.add(xMLNSDecl);
    }

    boolean needToCheckExclude() {
        return false;
    }

    void executeNSDecls(TransformerImpl transformerImpl) throws TransformerException {
        this.executeNSDecls(transformerImpl, null);
    }

    void executeNSDecls(TransformerImpl transformerImpl, String string) throws TransformerException {
        try {
            if (null != this.m_prefixTable) {
                SerializationHandler serializationHandler = transformerImpl.getResultTreeHandler();
                int n2 = this.m_prefixTable.size();
                for (int i2 = n2 - 1; i2 >= 0; --i2) {
                    XMLNSDecl xMLNSDecl = (XMLNSDecl)this.m_prefixTable.get(i2);
                    if (xMLNSDecl.getIsExcluded() || null != string && xMLNSDecl.getPrefix().equals(string)) continue;
                    serializationHandler.startPrefixMapping(xMLNSDecl.getPrefix(), xMLNSDecl.getURI(), true);
                }
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
    }

    void unexecuteNSDecls(TransformerImpl transformerImpl) throws TransformerException {
        this.unexecuteNSDecls(transformerImpl, null);
    }

    void unexecuteNSDecls(TransformerImpl transformerImpl, String string) throws TransformerException {
        try {
            if (null != this.m_prefixTable) {
                SerializationHandler serializationHandler = transformerImpl.getResultTreeHandler();
                int n2 = this.m_prefixTable.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    XMLNSDecl xMLNSDecl = (XMLNSDecl)this.m_prefixTable.get(i2);
                    if (xMLNSDecl.getIsExcluded() || null != string && xMLNSDecl.getPrefix().equals(string)) continue;
                    serializationHandler.endPrefixMapping(xMLNSDecl.getPrefix());
                }
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
    }

    public void setUid(int n2) {
        this.m_docOrderNumber = n2;
    }

    public int getUid() {
        return this.m_docOrderNumber;
    }

    public Node getParentNode() {
        return this.m_parentNode;
    }

    public ElemTemplateElement getParentElem() {
        return this.m_parentNode;
    }

    public void setParentElem(ElemTemplateElement elemTemplateElement) {
        this.m_parentNode = elemTemplateElement;
    }

    public Node getNextSibling() {
        return this.m_nextSibling;
    }

    public Node getPreviousSibling() {
        Node node = this.getParentNode();
        Node node2 = null;
        if (node != null) {
            for (node = node.getFirstChild(); node != null; node = node.getNextSibling()) {
                if (node == this) {
                    return node2;
                }
                node2 = node;
            }
        }
        return null;
    }

    public ElemTemplateElement getPreviousSiblingElem() {
        ElemTemplateElement elemTemplateElement = this.getParentNodeElem();
        ElemTemplateElement elemTemplateElement2 = null;
        if (elemTemplateElement != null) {
            for (elemTemplateElement = elemTemplateElement.getFirstChildElem(); elemTemplateElement != null; elemTemplateElement = elemTemplateElement.getNextSiblingElem()) {
                if (elemTemplateElement == this) {
                    return elemTemplateElement2;
                }
                elemTemplateElement2 = elemTemplateElement;
            }
        }
        return null;
    }

    public ElemTemplateElement getNextSiblingElem() {
        return this.m_nextSibling;
    }

    public ElemTemplateElement getParentNodeElem() {
        return this.m_parentNode;
    }

    public Node getFirstChild() {
        return this.m_firstChild;
    }

    public ElemTemplateElement getFirstChildElem() {
        return this.m_firstChild;
    }

    public Node getLastChild() {
        ElemTemplateElement elemTemplateElement = null;
        ElemTemplateElement elemTemplateElement2 = this.m_firstChild;
        while (elemTemplateElement2 != null) {
            elemTemplateElement = elemTemplateElement2;
            elemTemplateElement2 = elemTemplateElement2.m_nextSibling;
        }
        return elemTemplateElement;
    }

    public ElemTemplateElement getLastChildElem() {
        ElemTemplateElement elemTemplateElement = null;
        ElemTemplateElement elemTemplateElement2 = this.m_firstChild;
        while (elemTemplateElement2 != null) {
            elemTemplateElement = elemTemplateElement2;
            elemTemplateElement2 = elemTemplateElement2.m_nextSibling;
        }
        return elemTemplateElement;
    }

    public Node getDOMBackPointer() {
        return this.m_DOMBackPointer;
    }

    public void setDOMBackPointer(Node node) {
        this.m_DOMBackPointer = node;
    }

    public int compareTo(Object object) throws ClassCastException {
        ElemTemplateElement elemTemplateElement = (ElemTemplateElement)object;
        int n2 = elemTemplateElement.getStylesheetComposed().getImportCountComposed();
        int n3 = this.getStylesheetComposed().getImportCountComposed();
        if (n3 < n2) {
            return -1;
        }
        if (n3 > n2) {
            return 1;
        }
        return this.getUid() - elemTemplateElement.getUid();
    }

    public boolean shouldStripWhiteSpace(XPathContext xPathContext, Element element) throws TransformerException {
        StylesheetRoot stylesheetRoot = this.getStylesheetRoot();
        return null != stylesheetRoot ? stylesheetRoot.shouldStripWhiteSpace(xPathContext, element) : false;
    }

    public boolean canStripWhiteSpace() {
        StylesheetRoot stylesheetRoot = this.getStylesheetRoot();
        return null != stylesheetRoot ? stylesheetRoot.canStripWhiteSpace() : false;
    }

    public boolean canAcceptVariables() {
        return true;
    }

    public void exprSetParent(ExpressionNode expressionNode) {
        this.setParentElem((ElemTemplateElement)expressionNode);
    }

    public ExpressionNode exprGetParent() {
        return this.getParentElem();
    }

    public void exprAddChild(ExpressionNode expressionNode, int n2) {
        this.appendChild((ElemTemplateElement)expressionNode);
    }

    public ExpressionNode exprGetChild(int n2) {
        return (ExpressionNode)((Object)this.item(n2));
    }

    public int exprGetNumChildren() {
        return this.getLength();
    }

    protected boolean accept(XSLTVisitor xSLTVisitor) {
        return xSLTVisitor.visitInstruction(this);
    }

    public void callVisitors(XSLTVisitor xSLTVisitor) {
        if (this.accept(xSLTVisitor)) {
            this.callChildVisitors(xSLTVisitor);
        }
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        ElemTemplateElement elemTemplateElement = this.m_firstChild;
        while (elemTemplateElement != null) {
            elemTemplateElement.callVisitors(xSLTVisitor);
            elemTemplateElement = elemTemplateElement.m_nextSibling;
        }
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor) {
        this.callChildVisitors(xSLTVisitor, true);
    }

    public boolean handlesNullPrefixes() {
        return false;
    }
}

