/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.util.Vector;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.NodeVector;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.objects.XObject;

public class NodeSequence
extends XObject
implements Cloneable,
DTMIterator,
PathComponent {
    static final long serialVersionUID = 3866261934726581044L;
    protected int m_last = -1;
    protected int m_next = 0;
    private IteratorCache m_cache;
    protected DTMIterator m_iter;
    protected DTMManager m_dtmMgr;

    protected NodeVector getVector() {
        NodeVector nodeVector = this.m_cache != null ? IteratorCache.access$000(this.m_cache) : null;
        return nodeVector;
    }

    private IteratorCache getCache() {
        return this.m_cache;
    }

    protected void SetVector(NodeVector nodeVector) {
        this.setObject(nodeVector);
    }

    public boolean hasCache() {
        NodeVector nodeVector = this.getVector();
        return nodeVector != null;
    }

    private boolean cacheComplete() {
        boolean bl = this.m_cache != null ? IteratorCache.access$100(this.m_cache) : false;
        return bl;
    }

    private void markCacheComplete() {
        NodeVector nodeVector = this.getVector();
        if (nodeVector != null) {
            IteratorCache.access$200(this.m_cache, true);
        }
    }

    public final void setIter(DTMIterator dTMIterator) {
        this.m_iter = dTMIterator;
    }

    public final DTMIterator getContainedIter() {
        return this.m_iter;
    }

    private NodeSequence(DTMIterator dTMIterator, int n2, XPathContext xPathContext, boolean bl) {
        this.setIter(dTMIterator);
        this.setRoot(n2, xPathContext);
        this.setShouldCacheNodes(bl);
    }

    public NodeSequence(Object object) {
        super(object);
        if (object instanceof NodeVector) {
            this.SetVector((NodeVector)object);
        }
        if (null != object) {
            this.assertion(object instanceof NodeVector, "Must have a NodeVector as the object for NodeSequence!");
            if (object instanceof DTMIterator) {
                this.setIter((DTMIterator)object);
                this.m_last = ((DTMIterator)object).getLength();
            }
        }
    }

    private NodeSequence(DTMManager dTMManager) {
        super(new NodeVector());
        this.m_last = 0;
        this.m_dtmMgr = dTMManager;
    }

    public NodeSequence() {
    }

    public DTM getDTM(int n2) {
        DTMManager dTMManager = this.getDTMManager();
        if (null != dTMManager) {
            return this.getDTMManager().getDTM(n2);
        }
        this.assertion(false, "Can not get a DTM Unless a DTMManager has been set!");
        return null;
    }

    public DTMManager getDTMManager() {
        return this.m_dtmMgr;
    }

    public int getRoot() {
        if (null != this.m_iter) {
            return this.m_iter.getRoot();
        }
        return -1;
    }

    public void setRoot(int n2, Object object) {
        if (null != this.m_iter) {
            XPathContext xPathContext = (XPathContext)object;
            this.m_dtmMgr = xPathContext.getDTMManager();
            this.m_iter.setRoot(n2, object);
            if (!this.m_iter.isDocOrdered()) {
                if (!this.hasCache()) {
                    this.setShouldCacheNodes(true);
                }
                this.runTo(-1);
                this.m_next = 0;
            }
        } else {
            this.assertion(false, "Can not setRoot on a non-iterated NodeSequence!");
        }
    }

    public void reset() {
        this.m_next = 0;
    }

    public int getWhatToShow() {
        return this.hasCache() ? -17 : this.m_iter.getWhatToShow();
    }

    public boolean getExpandEntityReferences() {
        if (null != this.m_iter) {
            return this.m_iter.getExpandEntityReferences();
        }
        return true;
    }

    public int nextNode() {
        NodeVector nodeVector = this.getVector();
        if (null != nodeVector) {
            if (this.m_next < nodeVector.size()) {
                int n2 = nodeVector.elementAt(this.m_next);
                ++this.m_next;
                return n2;
            }
            if (this.cacheComplete() || -1 != this.m_last || null == this.m_iter) {
                ++this.m_next;
                return -1;
            }
        }
        if (null == this.m_iter) {
            return -1;
        }
        int n3 = this.m_iter.nextNode();
        if (-1 != n3) {
            if (this.hasCache()) {
                if (this.m_iter.isDocOrdered()) {
                    this.getVector().addElement(n3);
                    ++this.m_next;
                } else {
                    int n4 = this.addNodeInDocOrder(n3);
                    if (n4 >= 0) {
                        ++this.m_next;
                    }
                }
            } else {
                ++this.m_next;
            }
        } else {
            this.markCacheComplete();
            this.m_last = this.m_next++;
        }
        return n3;
    }

    public int previousNode() {
        if (this.hasCache()) {
            if (this.m_next <= 0) {
                return -1;
            }
            --this.m_next;
            return this.item(this.m_next);
        }
        int n2 = this.m_iter.previousNode();
        this.m_next = this.m_iter.getCurrentPos();
        return this.m_next;
    }

    public void detach() {
        if (null != this.m_iter) {
            this.m_iter.detach();
        }
        super.detach();
    }

    public void allowDetachToRelease(boolean bl) {
        if (!bl && !this.hasCache()) {
            this.setShouldCacheNodes(true);
        }
        if (null != this.m_iter) {
            this.m_iter.allowDetachToRelease(bl);
        }
        super.allowDetachToRelease(bl);
    }

    public int getCurrentNode() {
        if (this.hasCache()) {
            int n2 = this.m_next - 1;
            NodeVector nodeVector = this.getVector();
            if (n2 >= 0 && n2 < nodeVector.size()) {
                return nodeVector.elementAt(n2);
            }
            return -1;
        }
        if (null != this.m_iter) {
            return this.m_iter.getCurrentNode();
        }
        return -1;
    }

    public boolean isFresh() {
        return 0 == this.m_next;
    }

    public void setShouldCacheNodes(boolean bl) {
        if (bl) {
            if (!this.hasCache()) {
                this.SetVector(new NodeVector());
            }
        } else {
            this.SetVector(null);
        }
    }

    public boolean isMutable() {
        return this.hasCache();
    }

    public int getCurrentPos() {
        return this.m_next;
    }

    public void runTo(int n2) {
        if (-1 == n2) {
            int n3;
            int n4 = this.m_next;
            while (-1 != (n3 = this.nextNode())) {
            }
            this.m_next = n4;
        } else {
            if (this.m_next == n2) {
                return;
            }
            if (this.hasCache() && this.m_next < this.getVector().size()) {
                this.m_next = n2;
            } else if (null == this.getVector() && n2 < this.m_next) {
                int n5;
                while (this.m_next >= n2 && -1 != (n5 = this.previousNode())) {
                }
            } else {
                int n6;
                while (this.m_next < n2 && -1 != (n6 = this.nextNode())) {
                }
            }
        }
    }

    public void setCurrentPos(int n2) {
        this.runTo(n2);
    }

    public int item(int n2) {
        this.setCurrentPos(n2);
        int n3 = this.nextNode();
        this.m_next = n2;
        return n3;
    }

    public void setItem(int n2, int n3) {
        NodeVector nodeVector = this.getVector();
        if (null != nodeVector) {
            int n4 = nodeVector.elementAt(n3);
            if (n4 != n2 && IteratorCache.access$300(this.m_cache) > 1) {
                NodeVector nodeVector2;
                IteratorCache iteratorCache = new IteratorCache();
                try {
                    nodeVector2 = (NodeVector)nodeVector.clone();
                }
                catch (CloneNotSupportedException cloneNotSupportedException) {
                    cloneNotSupportedException.printStackTrace();
                    RuntimeException runtimeException = new RuntimeException(cloneNotSupportedException.getMessage());
                    throw runtimeException;
                }
                IteratorCache.access$400(iteratorCache, nodeVector2);
                IteratorCache.access$200(iteratorCache, true);
                this.m_cache = iteratorCache;
                nodeVector = nodeVector2;
                super.setObject(nodeVector2);
            }
            nodeVector.setElementAt(n2, n3);
            this.m_last = nodeVector.size();
        } else {
            this.m_iter.setItem(n2, n3);
        }
    }

    public int getLength() {
        IteratorCache iteratorCache = this.getCache();
        if (iteratorCache != null) {
            if (IteratorCache.access$100(iteratorCache)) {
                NodeVector nodeVector = IteratorCache.access$000(iteratorCache);
                return nodeVector.size();
            }
            if (this.m_iter instanceof NodeSetDTM) {
                return this.m_iter.getLength();
            }
            if (-1 == this.m_last) {
                int n2 = this.m_next;
                this.runTo(-1);
                this.m_next = n2;
            }
            return this.m_last;
        }
        int n3 = -1 == this.m_last ? (this.m_last = this.m_iter.getLength()) : this.m_last;
        return n3;
    }

    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        NodeSequence nodeSequence = (NodeSequence)Object.super.clone();
        nodeSequence.m_next = 0;
        if (this.m_cache != null) {
            IteratorCache.access$500(this.m_cache);
        }
        return nodeSequence;
    }

    public Object clone() throws CloneNotSupportedException {
        NodeSequence nodeSequence = (NodeSequence)Object.super.clone();
        if (null != this.m_iter) {
            nodeSequence.m_iter = (DTMIterator)this.m_iter.clone();
        }
        if (this.m_cache != null) {
            IteratorCache.access$500(this.m_cache);
        }
        return nodeSequence;
    }

    public boolean isDocOrdered() {
        if (null != this.m_iter) {
            return this.m_iter.isDocOrdered();
        }
        return true;
    }

    public int getAxis() {
        if (null != this.m_iter) {
            return this.m_iter.getAxis();
        }
        this.assertion(false, "Can not getAxis from a non-iterated node sequence!");
        return 0;
    }

    public int getAnalysisBits() {
        if (null != this.m_iter && this.m_iter instanceof PathComponent) {
            return ((PathComponent)((Object)this.m_iter)).getAnalysisBits();
        }
        return 0;
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
    }

    protected int addNodeInDocOrder(int n2) {
        int n3;
        this.assertion(this.hasCache(), "addNodeInDocOrder must be done on a mutable sequence!");
        int n4 = -1;
        NodeVector nodeVector = this.getVector();
        int n5 = nodeVector.size();
        for (n3 = n5 - 1; n3 >= 0; --n3) {
            int n6 = nodeVector.elementAt(n3);
            if (n6 == n2) {
                n3 = -2;
                break;
            }
            DTM dTM = this.m_dtmMgr.getDTM(n2);
            if (!dTM.isNodeAfter(n2, n6)) break;
        }
        if (n3 != -2) {
            n4 = n3 + 1;
            nodeVector.insertElementAt(n2, n4);
        }
        return n4;
    }

    protected void setObject(Object object) {
        if (object instanceof NodeVector) {
            super.setObject(object);
            NodeVector nodeVector = (NodeVector)object;
            if (this.m_cache != null) {
                IteratorCache.access$400(this.m_cache, nodeVector);
            } else if (nodeVector != null) {
                this.m_cache = new IteratorCache();
                IteratorCache.access$400(this.m_cache, nodeVector);
            }
        } else if (object instanceof IteratorCache) {
            IteratorCache iteratorCache;
            this.m_cache = iteratorCache = (IteratorCache)object;
            IteratorCache.access$500(this.m_cache);
            super.setObject(IteratorCache.access$000(iteratorCache));
        } else {
            super.setObject(object);
        }
    }

    protected IteratorCache getIteratorCache() {
        return this.m_cache;
    }

    private static final class IteratorCache {
        private NodeVector m_vec2 = null;
        private boolean m_isComplete2 = false;
        private int m_useCount2 = 1;

        IteratorCache() {
        }

        private int useCount() {
            return this.m_useCount2;
        }

        private void increaseUseCount() {
            if (this.m_vec2 != null) {
                ++this.m_useCount2;
            }
        }

        private void setVector(NodeVector nodeVector) {
            this.m_vec2 = nodeVector;
            this.m_useCount2 = 1;
        }

        private NodeVector getVector() {
            return this.m_vec2;
        }

        private void setCacheComplete(boolean bl) {
            this.m_isComplete2 = bl;
        }

        private boolean isComplete() {
            return this.m_isComplete2;
        }

        static NodeVector access$000(IteratorCache iteratorCache) {
            return iteratorCache.getVector();
        }

        static boolean access$100(IteratorCache iteratorCache) {
            return iteratorCache.isComplete();
        }

        static void access$200(IteratorCache iteratorCache, boolean bl) {
            iteratorCache.setCacheComplete(bl);
        }

        static int access$300(IteratorCache iteratorCache) {
            return iteratorCache.useCount();
        }

        static void access$400(IteratorCache iteratorCache, NodeVector nodeVector) {
            iteratorCache.setVector(nodeVector);
        }

        static void access$500(IteratorCache iteratorCache) {
            iteratorCache.increaseUseCount();
        }
    }

}

