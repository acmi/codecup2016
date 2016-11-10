/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.NodeVector;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public class NodeSetDTM
extends NodeVector
implements Cloneable,
DTMIterator {
    DTMManager m_manager;
    protected transient int m_next = 0;
    protected transient boolean m_mutable = true;
    protected transient boolean m_cacheNodes = true;
    protected int m_root = -1;
    private transient int m_last = 0;

    public NodeSetDTM(DTMManager dTMManager) {
        this.m_manager = dTMManager;
    }

    public NodeSetDTM(DTMIterator dTMIterator) {
        this.m_manager = dTMIterator.getDTMManager();
        this.m_root = dTMIterator.getRoot();
        this.addNodes(dTMIterator);
    }

    public NodeSetDTM(NodeIterator nodeIterator, XPathContext xPathContext) {
        Node node;
        this.m_manager = xPathContext.getDTMManager();
        while (null != (node = nodeIterator.nextNode())) {
            int n2 = xPathContext.getDTMHandleFromNode(node);
            this.addNodeInDocOrder(n2, xPathContext);
        }
    }

    public NodeSetDTM(NodeList nodeList, XPathContext xPathContext) {
        this.m_manager = xPathContext.getDTMManager();
        int n2 = nodeList.getLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = nodeList.item(i2);
            int n3 = xPathContext.getDTMHandleFromNode(node);
            this.addNode(n3);
        }
    }

    public NodeSetDTM(int n2, DTMManager dTMManager) {
        this.m_manager = dTMManager;
        this.addNode(n2);
    }

    public int getRoot() {
        if (-1 == this.m_root) {
            if (this.size() > 0) {
                return this.item(0);
            }
            return -1;
        }
        return this.m_root;
    }

    public void setRoot(int n2, Object object) {
    }

    public Object clone() throws CloneNotSupportedException {
        NodeSetDTM nodeSetDTM = (NodeSetDTM)super.clone();
        return nodeSetDTM;
    }

    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        NodeSetDTM nodeSetDTM = (NodeSetDTM)this.clone();
        nodeSetDTM.reset();
        return nodeSetDTM;
    }

    public void reset() {
        this.m_next = 0;
    }

    public int getWhatToShow() {
        return -17;
    }

    public boolean getExpandEntityReferences() {
        return true;
    }

    public DTM getDTM(int n2) {
        return this.m_manager.getDTM(n2);
    }

    public DTMManager getDTMManager() {
        return this.m_manager;
    }

    public int nextNode() {
        if (this.m_next < this.size()) {
            int n2 = this.elementAt(this.m_next);
            ++this.m_next;
            return n2;
        }
        return -1;
    }

    public int previousNode() {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_ITERATE", null));
        }
        if (this.m_next - 1 > 0) {
            --this.m_next;
            return this.elementAt(this.m_next);
        }
        return -1;
    }

    public void detach() {
    }

    public void allowDetachToRelease(boolean bl) {
    }

    public boolean isFresh() {
        return this.m_next == 0;
    }

    public void runTo(int n2) {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", null));
        }
        this.m_next = n2 >= 0 && this.m_next < this.m_firstFree ? n2 : this.m_firstFree - 1;
    }

    public int item(int n2) {
        this.runTo(n2);
        return this.elementAt(n2);
    }

    public int getLength() {
        this.runTo(-1);
        return this.size();
    }

    public void addNode(int n2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        this.addElement(n2);
    }

    public void addNodes(DTMIterator dTMIterator) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        if (null != dTMIterator) {
            int n2;
            while (-1 != (n2 = dTMIterator.nextNode())) {
                this.addElement(n2);
            }
        }
    }

    public int addNodeInDocOrder(int n2, boolean bl, XPathContext xPathContext) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        int n3 = -1;
        if (bl) {
            int n4;
            int n5 = this.size();
            for (n4 = n5 - 1; n4 >= 0; --n4) {
                int n6 = this.elementAt(n4);
                if (n6 == n2) {
                    n4 = -2;
                    break;
                }
                DTM dTM = xPathContext.getDTM(n2);
                if (!dTM.isNodeAfter(n2, n6)) break;
            }
            if (n4 != -2) {
                n3 = n4 + 1;
                this.insertElementAt(n2, n3);
            }
        } else {
            n3 = this.size();
            boolean bl2 = false;
            for (int i2 = 0; i2 < n3; ++i2) {
                if (i2 != n2) continue;
                bl2 = true;
                break;
            }
            if (!bl2) {
                this.addElement(n2);
            }
        }
        return n3;
    }

    public int addNodeInDocOrder(int n2, XPathContext xPathContext) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        return this.addNodeInDocOrder(n2, true, xPathContext);
    }

    public int size() {
        return super.size();
    }

    public void addElement(int n2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.addElement(n2);
    }

    public void insertElementAt(int n2, int n3) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.insertElementAt(n2, n3);
    }

    public void removeAllElements() {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.removeAllElements();
    }

    public void setElementAt(int n2, int n3) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.setElementAt(n2, n3);
    }

    public void setItem(int n2, int n3) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
        }
        super.setElementAt(n2, n3);
    }

    public int elementAt(int n2) {
        this.runTo(n2);
        return super.elementAt(n2);
    }

    public boolean contains(int n2) {
        this.runTo(-1);
        return super.contains(n2);
    }

    public int getCurrentPos() {
        return this.m_next;
    }

    public void setCurrentPos(int n2) {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", null));
        }
        this.m_next = n2;
    }

    public int getCurrentNode() {
        if (!this.m_cacheNodes) {
            throw new RuntimeException("This NodeSetDTM can not do indexing or counting functions!");
        }
        int n2 = this.m_next;
        int n3 = this.m_next > 0 ? this.m_next - 1 : this.m_next;
        int n4 = n3 < this.m_firstFree ? this.elementAt(n3) : -1;
        this.m_next = n2;
        return n4;
    }

    public void setShouldCacheNodes(boolean bl) {
        if (!this.isFresh()) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_CALL_SETSHOULDCACHENODE", null));
        }
        this.m_cacheNodes = bl;
        this.m_mutable = true;
    }

    public boolean isDocOrdered() {
        return true;
    }

    public int getAxis() {
        return -1;
    }
}

