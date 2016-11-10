/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import org.apache.xalan.res.XSLMessages;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeSet
implements Cloneable,
NodeList,
NodeIterator {
    protected transient int m_next = 0;
    protected transient boolean m_mutable = true;
    protected transient boolean m_cacheNodes = true;
    private transient int m_last = 0;
    private int m_blocksize;
    Node[] m_map;
    protected int m_firstFree = 0;
    private int m_mapSize;

    public NodeSet() {
        this.m_blocksize = 32;
        this.m_mapSize = 0;
    }

    public NodeSet(int n2) {
        this.m_blocksize = n2;
        this.m_mapSize = 0;
    }

    public NodeSet(NodeList nodeList) {
        this(32);
        this.addNodes(nodeList);
    }

    public NodeSet(NodeIterator nodeIterator) {
        this(32);
        this.addNodes(nodeIterator);
    }

    public NodeSet(Node node) {
        this(32);
        this.addNode(node);
    }

    public Node getRoot() {
        return null;
    }

    public int getWhatToShow() {
        return -17;
    }

    public NodeFilter getFilter() {
        return null;
    }

    public boolean getExpandEntityReferences() {
        return true;
    }

    public Node nextNode() throws DOMException {
        if (this.m_next < this.size()) {
            Node node = this.elementAt(this.m_next);
            ++this.m_next;
            return node;
        }
        return null;
    }

    public Node previousNode() throws DOMException {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_ITERATE", null));
        }
        if (this.m_next - 1 > 0) {
            --this.m_next;
            return this.elementAt(this.m_next);
        }
        return null;
    }

    public void detach() {
    }

    public boolean isFresh() {
        return this.m_next == 0;
    }

    public void runTo(int n2) {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", null));
        }
        this.m_next = n2 >= 0 && this.m_next < this.m_firstFree ? n2 : this.m_firstFree - 1;
    }

    public Node item(int n2) {
        this.runTo(n2);
        return this.elementAt(n2);
    }

    public int getLength() {
        this.runTo(-1);
        return this.size();
    }

    public void addNode(Node node) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        this.addElement(node);
    }

    public void addNodes(NodeList nodeList) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (null != nodeList) {
            int n2 = nodeList.getLength();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node = nodeList.item(i2);
                if (null == node) continue;
                this.addElement(node);
            }
        }
    }

    public void addNodes(NodeIterator nodeIterator) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (null != nodeIterator) {
            Node node;
            while (null != (node = nodeIterator.nextNode())) {
                this.addElement(node);
            }
        }
    }

    public void setShouldCacheNodes(boolean bl) {
        if (!this.isFresh()) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_CALL_SETSHOULDCACHENODE", null));
        }
        this.m_cacheNodes = bl;
        this.m_mutable = true;
    }

    public Object clone() throws CloneNotSupportedException {
        NodeSet nodeSet = (NodeSet)super.clone();
        if (null != this.m_map && this.m_map == nodeSet.m_map) {
            nodeSet.m_map = new Node[this.m_map.length];
            System.arraycopy(this.m_map, 0, nodeSet.m_map, 0, this.m_map.length);
        }
        return nodeSet;
    }

    public int size() {
        return this.m_firstFree;
    }

    public void addElement(Node node) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            if (null == this.m_map) {
                this.m_map = new Node[this.m_blocksize];
                this.m_mapSize = this.m_blocksize;
            } else {
                this.m_mapSize += this.m_blocksize;
                Node[] arrnode = new Node[this.m_mapSize];
                System.arraycopy(this.m_map, 0, arrnode, 0, this.m_firstFree + 1);
                this.m_map = arrnode;
            }
        }
        this.m_map[this.m_firstFree] = node;
        ++this.m_firstFree;
    }

    public Node elementAt(int n2) {
        if (null == this.m_map) {
            return null;
        }
        return this.m_map[n2];
    }

    public boolean contains(Node node) {
        this.runTo(-1);
        if (null == this.m_map) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; ++i2) {
            Node node2 = this.m_map[i2];
            if (null == node2 || !node2.equals(node)) continue;
            return true;
        }
        return false;
    }
}

