/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.ChildNode;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.NamedNodeMapImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public abstract class NodeImpl
implements Serializable,
Cloneable,
Node,
NodeList,
EventTarget {
    public static final short TREE_POSITION_PRECEDING = 1;
    public static final short TREE_POSITION_FOLLOWING = 2;
    public static final short TREE_POSITION_ANCESTOR = 4;
    public static final short TREE_POSITION_DESCENDANT = 8;
    public static final short TREE_POSITION_EQUIVALENT = 16;
    public static final short TREE_POSITION_SAME_NODE = 32;
    public static final short TREE_POSITION_DISCONNECTED = 0;
    public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
    public static final short DOCUMENT_POSITION_PRECEDING = 2;
    public static final short DOCUMENT_POSITION_FOLLOWING = 4;
    public static final short DOCUMENT_POSITION_CONTAINS = 8;
    public static final short DOCUMENT_POSITION_IS_CONTAINED = 16;
    public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
    static final long serialVersionUID = -6316591992167219696L;
    public static final short ELEMENT_DEFINITION_NODE = 21;
    protected NodeImpl ownerNode;
    protected short flags;
    protected static final short READONLY = 1;
    protected static final short SYNCDATA = 2;
    protected static final short SYNCCHILDREN = 4;
    protected static final short OWNED = 8;
    protected static final short FIRSTCHILD = 16;
    protected static final short SPECIFIED = 32;
    protected static final short IGNORABLEWS = 64;
    protected static final short HASSTRING = 128;
    protected static final short NORMALIZED = 256;
    protected static final short ID = 512;

    protected NodeImpl(CoreDocumentImpl coreDocumentImpl) {
        this.ownerNode = coreDocumentImpl;
    }

    public NodeImpl() {
    }

    public abstract short getNodeType();

    public abstract String getNodeName();

    public String getNodeValue() throws DOMException {
        return null;
    }

    public void setNodeValue(String string) throws DOMException {
    }

    public Node appendChild(Node node) throws DOMException {
        return this.insertBefore(node, null);
    }

    public Node cloneNode(boolean bl) {
        NodeImpl nodeImpl;
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        try {
            nodeImpl = (NodeImpl)this.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new RuntimeException("**Internal Error**" + cloneNotSupportedException);
        }
        nodeImpl.ownerNode = this.ownerDocument();
        nodeImpl.isOwned(false);
        nodeImpl.isReadOnly(false);
        this.ownerDocument().callUserDataHandlers(this, nodeImpl, 1);
        return nodeImpl;
    }

    public Document getOwnerDocument() {
        if (this.isOwned()) {
            return this.ownerNode.ownerDocument();
        }
        return (Document)((Object)this.ownerNode);
    }

    CoreDocumentImpl ownerDocument() {
        if (this.isOwned()) {
            return this.ownerNode.ownerDocument();
        }
        return (CoreDocumentImpl)this.ownerNode;
    }

    protected void setOwnerDocument(CoreDocumentImpl coreDocumentImpl) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        if (!this.isOwned()) {
            this.ownerNode = coreDocumentImpl;
        }
    }

    protected int getNodeNumber() {
        CoreDocumentImpl coreDocumentImpl = (CoreDocumentImpl)this.getOwnerDocument();
        int n2 = coreDocumentImpl.getNodeNumber(this);
        return n2;
    }

    public Node getParentNode() {
        return null;
    }

    NodeImpl parentNode() {
        return null;
    }

    public Node getNextSibling() {
        return null;
    }

    public Node getPreviousSibling() {
        return null;
    }

    ChildNode previousSibling() {
        return null;
    }

    public NamedNodeMap getAttributes() {
        return null;
    }

    public boolean hasAttributes() {
        return false;
    }

    public boolean hasChildNodes() {
        return false;
    }

    public NodeList getChildNodes() {
        return this;
    }

    public Node getFirstChild() {
        return null;
    }

    public Node getLastChild() {
        return null;
    }

    public Node insertBefore(Node node, Node node2) throws DOMException {
        throw new DOMException(3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
    }

    public Node removeChild(Node node) throws DOMException {
        throw new DOMException(8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
    }

    public Node replaceChild(Node node, Node node2) throws DOMException {
        throw new DOMException(3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
    }

    public int getLength() {
        return 0;
    }

    public Node item(int n2) {
        return null;
    }

    public void normalize() {
    }

    public boolean isSupported(String string, String string2) {
        return this.ownerDocument().getImplementation().hasFeature(string, string2);
    }

    public String getNamespaceURI() {
        return null;
    }

    public String getPrefix() {
        return null;
    }

    public void setPrefix(String string) throws DOMException {
        throw new DOMException(14, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null));
    }

    public String getLocalName() {
        return null;
    }

    public void addEventListener(String string, EventListener eventListener, boolean bl) {
        this.ownerDocument().addEventListener(this, string, eventListener, bl);
    }

    public void removeEventListener(String string, EventListener eventListener, boolean bl) {
        this.ownerDocument().removeEventListener(this, string, eventListener, bl);
    }

    public boolean dispatchEvent(Event event) {
        return this.ownerDocument().dispatchEvent(this, event);
    }

    public String getBaseURI() {
        return null;
    }

    public short compareTreePosition(Node node) {
        int n2;
        if (this == node) {
            return 48;
        }
        short s2 = this.getNodeType();
        short s3 = node.getNodeType();
        if (s2 == 6 || s2 == 12 || s3 == 6 || s3 == 12) {
            return 0;
        }
        Node node2 = this;
        Node node3 = node;
        int n3 = 0;
        int n4 = 0;
        Node node4 = this;
        while (node4 != null) {
            ++n3;
            if (node4 == node) {
                return 5;
            }
            node2 = node4;
            node4 = node4.getParentNode();
        }
        node4 = node;
        while (node4 != null) {
            ++n4;
            if (node4 == this) {
                return 10;
            }
            node3 = node4;
            node4 = node4.getParentNode();
        }
        Node node5 = this;
        Node node6 = node;
        short s4 = node2.getNodeType();
        short s5 = node3.getNodeType();
        if (s4 == 2) {
            node5 = ((AttrImpl)node2).getOwnerElement();
        }
        if (s5 == 2) {
            node6 = ((AttrImpl)node3).getOwnerElement();
        }
        if (s4 == 2 && s5 == 2 && node5 == node6) {
            return 16;
        }
        if (s4 == 2) {
            n3 = 0;
            node4 = node5;
            while (node4 != null) {
                ++n3;
                if (node4 == node6) {
                    return 1;
                }
                node2 = node4;
                node4 = node4.getParentNode();
            }
        }
        if (s5 == 2) {
            n4 = 0;
            node4 = node6;
            while (node4 != null) {
                ++n4;
                if (node4 == node5) {
                    return 2;
                }
                node3 = node4;
                node4 = node4.getParentNode();
            }
        }
        if (node2 != node3) {
            return 0;
        }
        if (n3 > n4) {
            n2 = 0;
            while (n2 < n3 - n4) {
                node5 = node5.getParentNode();
                ++n2;
            }
            if (node5 == node6) {
                return 1;
            }
        } else {
            n2 = 0;
            while (n2 < n4 - n3) {
                node6 = node6.getParentNode();
                ++n2;
            }
            if (node6 == node5) {
                return 2;
            }
        }
        Node node7 = node5.getParentNode();
        Node node8 = node6.getParentNode();
        while (node7 != node8) {
            node5 = node7;
            node6 = node8;
            node7 = node7.getParentNode();
            node8 = node8.getParentNode();
        }
        Node node9 = node7.getFirstChild();
        while (node9 != null) {
            if (node9 == node6) {
                return 1;
            }
            if (node9 == node5) {
                return 2;
            }
            node9 = node9.getNextSibling();
        }
        return 0;
    }

    public short compareDocumentPosition(Node node) throws DOMException {
        DocumentType documentType;
        int n2;
        if (this == node) {
            return 0;
        }
        if (node != null && !(node instanceof NodeImpl)) {
            String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
            throw new DOMException(9, string);
        }
        Document document = this.getNodeType() == 9 ? (Document)((Object)this) : this.getOwnerDocument();
        Document document2 = node.getNodeType() == 9 ? (Document)node : node.getOwnerDocument();
        if (document != document2 && document != null && document2 != null) {
            int n3;
            int n4 = ((CoreDocumentImpl)document2).getNodeNumber();
            if (n4 > (n3 = ((CoreDocumentImpl)document).getNodeNumber())) {
                return 37;
            }
            return 35;
        }
        Node node2 = this;
        Node node3 = node;
        int n5 = 0;
        int n6 = 0;
        Node node4 = this;
        while (node4 != null) {
            ++n5;
            if (node4 == node) {
                return 10;
            }
            node2 = node4;
            node4 = node4.getParentNode();
        }
        node4 = node;
        while (node4 != null) {
            ++n6;
            if (node4 == this) {
                return 20;
            }
            node3 = node4;
            node4 = node4.getParentNode();
        }
        short s2 = node2.getNodeType();
        short s3 = node3.getNodeType();
        Node node5 = this;
        Node node6 = node;
        switch (s2) {
            case 6: 
            case 12: {
                documentType = document.getDoctype();
                if (documentType == node3) {
                    return 10;
                }
                switch (s3) {
                    case 6: 
                    case 12: {
                        if (s2 != s3) {
                            return s2 > s3 ? 2 : 4;
                        }
                        if (s2 == 12) {
                            if (((NamedNodeMapImpl)documentType.getNotations()).precedes(node3, node2)) {
                                return 34;
                            }
                            return 36;
                        }
                        if (((NamedNodeMapImpl)documentType.getEntities()).precedes(node3, node2)) {
                            return 34;
                        }
                        return 36;
                    }
                }
                node2 = document;
                node5 = node2;
                break;
            }
            case 10: {
                if (node6 == document) {
                    return 10;
                }
                if (document == null || document != document2) break;
                return 4;
            }
            case 2: {
                node5 = ((AttrImpl)node2).getOwnerElement();
                if (s3 == 2 && (node6 = ((AttrImpl)node3).getOwnerElement()) == node5) {
                    if (((NamedNodeMapImpl)node5.getAttributes()).precedes(node, this)) {
                        return 34;
                    }
                    return 36;
                }
                n5 = 0;
                node4 = node5;
                while (node4 != null) {
                    ++n5;
                    if (node4 == node6) {
                        return 10;
                    }
                    node2 = node4;
                    node4 = node4.getParentNode();
                }
                break block0;
            }
        }
        switch (s3) {
            case 6: 
            case 12: {
                documentType = document.getDoctype();
                if (documentType == this) {
                    return 20;
                }
                node6 = node3 = document;
                break;
            }
            case 10: {
                if (node5 == document2) {
                    return 20;
                }
                if (document2 == null || document != document2) break;
                return 2;
            }
            case 2: {
                n6 = 0;
                node4 = node6 = ((AttrImpl)node3).getOwnerElement();
                while (node4 != null) {
                    ++n6;
                    if (node4 == node5) {
                        return 20;
                    }
                    node3 = node4;
                    node4 = node4.getParentNode();
                }
                break block8;
            }
        }
        if (node2 != node3) {
            int n7;
            int n8 = node2.getNodeNumber();
            if (n8 > (n7 = ((NodeImpl)node3).getNodeNumber())) {
                return 37;
            }
            return 35;
        }
        if (n5 > n6) {
            n2 = 0;
            while (n2 < n5 - n6) {
                node5 = node5.getParentNode();
                ++n2;
            }
            if (node5 == node6) {
                return 2;
            }
        } else {
            n2 = 0;
            while (n2 < n6 - n5) {
                node6 = node6.getParentNode();
                ++n2;
            }
            if (node6 == node5) {
                return 4;
            }
        }
        Node node7 = node5.getParentNode();
        Node node8 = node6.getParentNode();
        while (node7 != node8) {
            node5 = node7;
            node6 = node8;
            node7 = node7.getParentNode();
            node8 = node8.getParentNode();
        }
        Node node9 = node7.getFirstChild();
        while (node9 != null) {
            if (node9 == node6) {
                return 2;
            }
            if (node9 == node5) {
                return 4;
            }
            node9 = node9.getNextSibling();
        }
        return 0;
    }

    public String getTextContent() throws DOMException {
        return this.getNodeValue();
    }

    void getTextContent(StringBuffer stringBuffer) throws DOMException {
        String string = this.getNodeValue();
        if (string != null) {
            stringBuffer.append(string);
        }
    }

    public void setTextContent(String string) throws DOMException {
        this.setNodeValue(string);
    }

    public boolean isSameNode(Node node) {
        return this == node;
    }

    public boolean isDefaultNamespace(String string) {
        short s2 = this.getNodeType();
        switch (s2) {
            case 1: {
                NodeImpl nodeImpl;
                NodeImpl nodeImpl2;
                String string2 = this.getNamespaceURI();
                String string3 = this.getPrefix();
                if (string3 == null || string3.length() == 0) {
                    if (string == null) {
                        return string2 == string;
                    }
                    return string.equals(string2);
                }
                if (this.hasAttributes() && (nodeImpl2 = (NodeImpl)((Object)(nodeImpl = (ElementImpl)this).getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns"))) != null) {
                    String string4 = nodeImpl2.getNodeValue();
                    if (string == null) {
                        return string2 == string4;
                    }
                    return string.equals(string4);
                }
                nodeImpl = (NodeImpl)this.getElementAncestor(this);
                if (nodeImpl != null) {
                    return nodeImpl.isDefaultNamespace(string);
                }
                return false;
            }
            case 9: {
                return ((NodeImpl)((Object)((Document)((Object)this)).getDocumentElement())).isDefaultNamespace(string);
            }
            case 6: 
            case 10: 
            case 11: 
            case 12: {
                return false;
            }
            case 2: {
                if (this.ownerNode.getNodeType() == 1) {
                    return this.ownerNode.isDefaultNamespace(string);
                }
                return false;
            }
        }
        NodeImpl nodeImpl = (NodeImpl)this.getElementAncestor(this);
        if (nodeImpl != null) {
            return nodeImpl.isDefaultNamespace(string);
        }
        return false;
    }

    public String lookupPrefix(String string) {
        if (string == null) {
            return null;
        }
        short s2 = this.getNodeType();
        switch (s2) {
            case 1: {
                this.getNamespaceURI();
                return this.lookupNamespacePrefix(string, (ElementImpl)this);
            }
            case 9: {
                return ((NodeImpl)((Object)((Document)((Object)this)).getDocumentElement())).lookupPrefix(string);
            }
            case 6: 
            case 10: 
            case 11: 
            case 12: {
                return null;
            }
            case 2: {
                if (this.ownerNode.getNodeType() == 1) {
                    return this.ownerNode.lookupPrefix(string);
                }
                return null;
            }
        }
        NodeImpl nodeImpl = (NodeImpl)this.getElementAncestor(this);
        if (nodeImpl != null) {
            return nodeImpl.lookupPrefix(string);
        }
        return null;
    }

    public String lookupNamespaceURI(String string) {
        short s2 = this.getNodeType();
        switch (s2) {
            case 1: {
                Object object;
                String string2 = this.getNamespaceURI();
                String string3 = this.getPrefix();
                if (string2 != null) {
                    if (string == null && string3 == string) {
                        return string2;
                    }
                    if (string3 != null && string3.equals(string)) {
                        return string2;
                    }
                }
                if (this.hasAttributes()) {
                    object = this.getAttributes();
                    int n2 = object.getLength();
                    int n3 = 0;
                    while (n3 < n2) {
                        Node node = object.item(n3);
                        String string4 = node.getPrefix();
                        String string5 = node.getNodeValue();
                        string2 = node.getNamespaceURI();
                        if (string2 != null && string2.equals("http://www.w3.org/2000/xmlns/")) {
                            if (string == null && node.getNodeName().equals("xmlns")) {
                                return string5.length() > 0 ? string5 : null;
                            }
                            if (string4 != null && string4.equals("xmlns") && node.getLocalName().equals(string)) {
                                return string5.length() > 0 ? string5 : null;
                            }
                        }
                        ++n3;
                    }
                }
                if ((object = (NodeImpl)this.getElementAncestor(this)) != null) {
                    return object.lookupNamespaceURI(string);
                }
                return null;
            }
            case 9: {
                return ((NodeImpl)((Object)((Document)((Object)this)).getDocumentElement())).lookupNamespaceURI(string);
            }
            case 6: 
            case 10: 
            case 11: 
            case 12: {
                return null;
            }
            case 2: {
                if (this.ownerNode.getNodeType() == 1) {
                    return this.ownerNode.lookupNamespaceURI(string);
                }
                return null;
            }
        }
        NodeImpl nodeImpl = (NodeImpl)this.getElementAncestor(this);
        if (nodeImpl != null) {
            return nodeImpl.lookupNamespaceURI(string);
        }
        return null;
    }

    Node getElementAncestor(Node node) {
        Node node2 = node.getParentNode();
        while (node2 != null) {
            short s2 = node2.getNodeType();
            if (s2 == 1) {
                return node2;
            }
            node2 = node2.getParentNode();
        }
        return null;
    }

    String lookupNamespacePrefix(String string, ElementImpl elementImpl) {
        Object object;
        String string2 = this.getNamespaceURI();
        String string3 = this.getPrefix();
        if (string2 != null && string2.equals(string) && string3 != null && (object = elementImpl.lookupNamespaceURI(string3)) != null && object.equals(string)) {
            return string3;
        }
        if (this.hasAttributes()) {
            object = this.getAttributes();
            int n2 = object.getLength();
            int n3 = 0;
            while (n3 < n2) {
                String string4;
                String string5;
                Node node = object.item(n3);
                String string6 = node.getPrefix();
                String string7 = node.getNodeValue();
                string2 = node.getNamespaceURI();
                if (string2 != null && string2.equals("http://www.w3.org/2000/xmlns/") && (node.getNodeName().equals("xmlns") || string6 != null && string6.equals("xmlns") && string7.equals(string)) && (string4 = elementImpl.lookupNamespaceURI(string5 = node.getLocalName())) != null && string4.equals(string)) {
                    return string5;
                }
                ++n3;
            }
        }
        if ((object = (NodeImpl)this.getElementAncestor(this)) != null) {
            return object.lookupNamespacePrefix(string, elementImpl);
        }
        return null;
    }

    public boolean isEqualNode(Node node) {
        if (node == this) {
            return true;
        }
        if (node.getNodeType() != this.getNodeType()) {
            return false;
        }
        if (this.getNodeName() == null ? node.getNodeName() != null : !this.getNodeName().equals(node.getNodeName())) {
            return false;
        }
        if (this.getLocalName() == null ? node.getLocalName() != null : !this.getLocalName().equals(node.getLocalName())) {
            return false;
        }
        if (this.getNamespaceURI() == null ? node.getNamespaceURI() != null : !this.getNamespaceURI().equals(node.getNamespaceURI())) {
            return false;
        }
        if (this.getPrefix() == null ? node.getPrefix() != null : !this.getPrefix().equals(node.getPrefix())) {
            return false;
        }
        if (this.getNodeValue() == null ? node.getNodeValue() != null : !this.getNodeValue().equals(node.getNodeValue())) {
            return false;
        }
        return true;
    }

    public Object getFeature(String string, String string2) {
        return this.isSupported(string, string2) ? this : null;
    }

    public Object setUserData(String string, Object object, UserDataHandler userDataHandler) {
        return this.ownerDocument().setUserData(this, string, object, userDataHandler);
    }

    public Object getUserData(String string) {
        return this.ownerDocument().getUserData(this, string);
    }

    protected Hashtable getUserDataRecord() {
        return this.ownerDocument().getUserDataRecord(this);
    }

    public void setReadOnly(boolean bl, boolean bl2) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.isReadOnly(bl);
    }

    public boolean getReadOnly() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.isReadOnly();
    }

    public void setUserData(Object object) {
        this.ownerDocument().setUserData(this, object);
    }

    public Object getUserData() {
        return this.ownerDocument().getUserData(this);
    }

    protected void changed() {
        this.ownerDocument().changed();
    }

    protected int changes() {
        return this.ownerDocument().changes();
    }

    protected void synchronizeData() {
        this.needsSyncData(false);
    }

    protected Node getContainer() {
        return null;
    }

    final boolean isReadOnly() {
        return (this.flags & 1) != 0;
    }

    final void isReadOnly(boolean bl) {
        this.flags = bl ? (short)(this.flags | 1) : (short)(this.flags & -2);
    }

    final boolean needsSyncData() {
        return (this.flags & 2) != 0;
    }

    final void needsSyncData(boolean bl) {
        this.flags = bl ? (short)(this.flags | 2) : (short)(this.flags & -3);
    }

    final boolean needsSyncChildren() {
        return (this.flags & 4) != 0;
    }

    public final void needsSyncChildren(boolean bl) {
        this.flags = bl ? (short)(this.flags | 4) : (short)(this.flags & -5);
    }

    final boolean isOwned() {
        return (this.flags & 8) != 0;
    }

    final void isOwned(boolean bl) {
        this.flags = bl ? (short)(this.flags | 8) : (short)(this.flags & -9);
    }

    final boolean isFirstChild() {
        return (this.flags & 16) != 0;
    }

    final void isFirstChild(boolean bl) {
        this.flags = bl ? (short)(this.flags | 16) : (short)(this.flags & -17);
    }

    final boolean isSpecified() {
        return (this.flags & 32) != 0;
    }

    final void isSpecified(boolean bl) {
        this.flags = bl ? (short)(this.flags | 32) : (short)(this.flags & -33);
    }

    final boolean internalIsIgnorableWhitespace() {
        return (this.flags & 64) != 0;
    }

    final void isIgnorableWhitespace(boolean bl) {
        this.flags = bl ? (short)(this.flags | 64) : (short)(this.flags & -65);
    }

    final boolean hasStringValue() {
        return (this.flags & 128) != 0;
    }

    final void hasStringValue(boolean bl) {
        this.flags = bl ? (short)(this.flags | 128) : (short)(this.flags & -129);
    }

    final boolean isNormalized() {
        return (this.flags & 256) != 0;
    }

    final void isNormalized(boolean bl) {
        if (!bl && this.isNormalized() && this.ownerNode != null) {
            this.ownerNode.isNormalized(false);
        }
        this.flags = bl ? (short)(this.flags | 256) : (short)(this.flags & -257);
    }

    final boolean isIdAttribute() {
        return (this.flags & 512) != 0;
    }

    final void isIdAttribute(boolean bl) {
        this.flags = bl ? (short)(this.flags | 512) : (short)(this.flags & -513);
    }

    public String toString() {
        return "[" + this.getNodeName() + ": " + this.getNodeValue() + "]";
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        objectOutputStream.defaultWriteObject();
    }
}

