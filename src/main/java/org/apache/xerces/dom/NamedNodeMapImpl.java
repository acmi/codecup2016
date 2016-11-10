/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamedNodeMapImpl
implements Serializable,
NamedNodeMap {
    static final long serialVersionUID = -7039242451046758020L;
    protected short flags;
    protected static final short READONLY = 1;
    protected static final short CHANGED = 2;
    protected static final short HASDEFAULTS = 4;
    protected List nodes;
    protected NodeImpl ownerNode;

    protected NamedNodeMapImpl(NodeImpl nodeImpl) {
        this.ownerNode = nodeImpl;
    }

    public int getLength() {
        return this.nodes != null ? this.nodes.size() : 0;
    }

    public Node item(int n2) {
        return this.nodes != null && n2 < this.nodes.size() ? (Node)this.nodes.get(n2) : null;
    }

    public Node getNamedItem(String string) {
        int n2 = this.findNamePoint(string, 0);
        return n2 < 0 ? null : (Node)this.nodes.get(n2);
    }

    public Node getNamedItemNS(String string, String string2) {
        int n2 = this.findNamePoint(string, string2);
        return n2 < 0 ? null : (Node)this.nodes.get(n2);
    }

    public Node setNamedItem(Node node) throws DOMException {
        CoreDocumentImpl coreDocumentImpl = this.ownerNode.ownerDocument();
        if (coreDocumentImpl.errorChecking) {
            if (this.isReadOnly()) {
                String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException(7, string);
            }
            if (node.getOwnerDocument() != coreDocumentImpl) {
                String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
                throw new DOMException(4, string);
            }
        }
        int n2 = this.findNamePoint(node.getNodeName(), 0);
        NodeImpl nodeImpl = null;
        if (n2 >= 0) {
            nodeImpl = (NodeImpl)this.nodes.get(n2);
            this.nodes.set(n2, node);
        } else {
            n2 = -1 - n2;
            if (null == this.nodes) {
                this.nodes = new ArrayList(5);
            }
            this.nodes.add(n2, node);
        }
        return nodeImpl;
    }

    public Node setNamedItemNS(Node node) throws DOMException {
        CoreDocumentImpl coreDocumentImpl = this.ownerNode.ownerDocument();
        if (coreDocumentImpl.errorChecking) {
            if (this.isReadOnly()) {
                String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException(7, string);
            }
            if (node.getOwnerDocument() != coreDocumentImpl) {
                String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
                throw new DOMException(4, string);
            }
        }
        int n2 = this.findNamePoint(node.getNamespaceURI(), node.getLocalName());
        NodeImpl nodeImpl = null;
        if (n2 >= 0) {
            nodeImpl = (NodeImpl)this.nodes.get(n2);
            this.nodes.set(n2, node);
        } else {
            n2 = this.findNamePoint(node.getNodeName(), 0);
            if (n2 >= 0) {
                nodeImpl = (NodeImpl)this.nodes.get(n2);
                this.nodes.add(n2, node);
            } else {
                n2 = -1 - n2;
                if (null == this.nodes) {
                    this.nodes = new ArrayList(5);
                }
                this.nodes.add(n2, node);
            }
        }
        return nodeImpl;
    }

    public Node removeNamedItem(String string) throws DOMException {
        if (this.isReadOnly()) {
            String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException(7, string2);
        }
        int n2 = this.findNamePoint(string, 0);
        if (n2 < 0) {
            String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
            throw new DOMException(8, string3);
        }
        NodeImpl nodeImpl = (NodeImpl)this.nodes.get(n2);
        this.nodes.remove(n2);
        return nodeImpl;
    }

    public Node removeNamedItemNS(String string, String string2) throws DOMException {
        if (this.isReadOnly()) {
            String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException(7, string3);
        }
        int n2 = this.findNamePoint(string, string2);
        if (n2 < 0) {
            String string4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
            throw new DOMException(8, string4);
        }
        NodeImpl nodeImpl = (NodeImpl)this.nodes.get(n2);
        this.nodes.remove(n2);
        return nodeImpl;
    }

    public NamedNodeMapImpl cloneMap(NodeImpl nodeImpl) {
        NamedNodeMapImpl namedNodeMapImpl = new NamedNodeMapImpl(nodeImpl);
        namedNodeMapImpl.cloneContent(this);
        return namedNodeMapImpl;
    }

    protected void cloneContent(NamedNodeMapImpl namedNodeMapImpl) {
        int n2;
        List list = namedNodeMapImpl.nodes;
        if (list != null && (n2 = list.size()) != 0) {
            if (this.nodes == null) {
                this.nodes = new ArrayList(n2);
            } else {
                this.nodes.clear();
            }
            int n3 = 0;
            while (n3 < n2) {
                NodeImpl nodeImpl = (NodeImpl)namedNodeMapImpl.nodes.get(n3);
                NodeImpl nodeImpl2 = (NodeImpl)nodeImpl.cloneNode(true);
                nodeImpl2.isSpecified(nodeImpl.isSpecified());
                this.nodes.add(nodeImpl2);
                ++n3;
            }
        }
    }

    void setReadOnly(boolean bl, boolean bl2) {
        this.isReadOnly(bl);
        if (bl2 && this.nodes != null) {
            int n2 = this.nodes.size() - 1;
            while (n2 >= 0) {
                ((NodeImpl)this.nodes.get(n2)).setReadOnly(bl, bl2);
                --n2;
            }
        }
    }

    boolean getReadOnly() {
        return this.isReadOnly();
    }

    protected void setOwnerDocument(CoreDocumentImpl coreDocumentImpl) {
        if (this.nodes != null) {
            int n2 = this.nodes.size();
            int n3 = 0;
            while (n3 < n2) {
                ((NodeImpl)this.item(n3)).setOwnerDocument(coreDocumentImpl);
                ++n3;
            }
        }
    }

    final boolean isReadOnly() {
        return (this.flags & 1) != 0;
    }

    final void isReadOnly(boolean bl) {
        this.flags = bl ? (short)(this.flags | 1) : (short)(this.flags & -2);
    }

    final boolean changed() {
        return (this.flags & 2) != 0;
    }

    final void changed(boolean bl) {
        this.flags = bl ? (short)(this.flags | 2) : (short)(this.flags & -3);
    }

    final boolean hasDefaults() {
        return (this.flags & 4) != 0;
    }

    final void hasDefaults(boolean bl) {
        this.flags = bl ? (short)(this.flags | 4) : (short)(this.flags & -5);
    }

    protected int findNamePoint(String string, int n2) {
        int n3 = 0;
        if (this.nodes != null) {
            int n4 = n2;
            int n5 = this.nodes.size() - 1;
            while (n4 <= n5) {
                n3 = (n4 + n5) / 2;
                int n6 = string.compareTo(((Node)this.nodes.get(n3)).getNodeName());
                if (n6 == 0) {
                    return n3;
                }
                if (n6 < 0) {
                    n5 = n3 - 1;
                    continue;
                }
                n4 = n3 + 1;
            }
            if (n4 > n3) {
                n3 = n4;
            }
        }
        return -1 - n3;
    }

    protected int findNamePoint(String string, String string2) {
        if (this.nodes == null) {
            return -1;
        }
        if (string2 == null) {
            return -1;
        }
        int n2 = this.nodes.size();
        int n3 = 0;
        while (n3 < n2) {
            NodeImpl nodeImpl = (NodeImpl)this.nodes.get(n3);
            String string3 = nodeImpl.getNamespaceURI();
            String string4 = nodeImpl.getLocalName();
            if (string == null ? string3 == null && (string2.equals(string4) || string4 == null && string2.equals(nodeImpl.getNodeName())) : string.equals(string3) && string2.equals(string4)) {
                return n3;
            }
            ++n3;
        }
        return -1;
    }

    protected boolean precedes(Node node, Node node2) {
        if (this.nodes != null) {
            int n2 = this.nodes.size();
            int n3 = 0;
            while (n3 < n2) {
                Node node3 = (Node)this.nodes.get(n3);
                if (node3 == node) {
                    return true;
                }
                if (node3 == node2) {
                    return false;
                }
                ++n3;
            }
        }
        return false;
    }

    protected void removeItem(int n2) {
        if (this.nodes != null && n2 < this.nodes.size()) {
            this.nodes.remove(n2);
        }
    }

    protected Object getItem(int n2) {
        if (this.nodes != null) {
            return this.nodes.get(n2);
        }
        return null;
    }

    protected int addItem(Node node) {
        int n2 = this.findNamePoint(node.getNamespaceURI(), node.getLocalName());
        if (n2 >= 0) {
            this.nodes.set(n2, node);
        } else {
            n2 = this.findNamePoint(node.getNodeName(), 0);
            if (n2 >= 0) {
                this.nodes.add(n2, node);
            } else {
                n2 = -1 - n2;
                if (null == this.nodes) {
                    this.nodes = new ArrayList(5);
                }
                this.nodes.add(n2, node);
            }
        }
        return n2;
    }

    protected ArrayList cloneMap(ArrayList arrayList) {
        if (arrayList == null) {
            arrayList = new ArrayList(5);
        }
        arrayList.clear();
        if (this.nodes != null) {
            int n2 = this.nodes.size();
            int n3 = 0;
            while (n3 < n2) {
                arrayList.add(this.nodes.get(n3));
                ++n3;
            }
        }
        return arrayList;
    }

    protected int getNamedItemIndex(String string, String string2) {
        return this.findNamePoint(string, string2);
    }

    public void removeAll() {
        if (this.nodes != null) {
            this.nodes.clear();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.nodes != null) {
            this.nodes = new ArrayList(this.nodes);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        List list = this.nodes;
        try {
            if (list != null) {
                this.nodes = new Vector(list);
            }
            objectOutputStream.defaultWriteObject();
            Object var4_3 = null;
            this.nodes = list;
        }
        catch (Throwable throwable) {
            Object var4_4 = null;
            this.nodes = list;
            throw throwable;
        }
    }
}

