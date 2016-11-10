/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.AxesWalker;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.WalkingIterator;

public class ReverseAxesWalker
extends AxesWalker {
    protected DTMAxisIterator m_iterator;

    ReverseAxesWalker(LocPathIterator locPathIterator, int n2) {
        super(locPathIterator, n2);
    }

    public void setRoot(int n2) {
        super.setRoot(n2);
        this.m_iterator = this.getDTM(n2).getAxisIterator(this.m_axis);
        this.m_iterator.setStartNode(n2);
    }

    public void detach() {
        this.m_iterator = null;
        super.detach();
    }

    protected int getNextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        int n2 = this.m_iterator.next();
        if (this.m_isFresh) {
            this.m_isFresh = false;
        }
        if (-1 == n2) {
            this.m_foundLast = true;
        }
        return n2;
    }

    public boolean isReverseAxes() {
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected int getProximityPosition(int n2) {
        int n3;
        if (n2 < 0) {
            return -1;
        }
        n3 = this.m_proximityPositions[n2];
        if (n3 <= 0) {
            AxesWalker axesWalker = this.wi().getLastUsedWalker();
            try {
                try {
                    int n4;
                    ReverseAxesWalker reverseAxesWalker = (ReverseAxesWalker)this.clone();
                    reverseAxesWalker.setRoot(this.getRoot());
                    reverseAxesWalker.setPredicateCount(n2);
                    reverseAxesWalker.setPrevWalker(null);
                    reverseAxesWalker.setNextWalker(null);
                    this.wi().setLastUsedWalker(reverseAxesWalker);
                    ++n3;
                    while (-1 != (n4 = reverseAxesWalker.nextNode())) {
                        ++n3;
                    }
                    this.m_proximityPositions[n2] = n3;
                }
                catch (CloneNotSupportedException cloneNotSupportedException) {
                    Object var7_8 = null;
                    this.wi().setLastUsedWalker(axesWalker);
                }
                Object var7_7 = null;
                this.wi().setLastUsedWalker(axesWalker);
            }
            catch (Throwable throwable) {
                Object var7_9 = null;
                this.wi().setLastUsedWalker(axesWalker);
                throw throwable;
            }
        }
        return n3;
    }

    protected void countProximityPosition(int n2) {
        if (n2 < this.m_proximityPositions.length) {
            int[] arrn = this.m_proximityPositions;
            int n3 = n2;
            arrn[n3] = arrn[n3] - 1;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getLastPos(XPathContext xPathContext) {
        int n2 = 0;
        AxesWalker axesWalker = this.wi().getLastUsedWalker();
        try {
            try {
                int n3;
                ReverseAxesWalker reverseAxesWalker = (ReverseAxesWalker)this.clone();
                reverseAxesWalker.setRoot(this.getRoot());
                reverseAxesWalker.setPredicateCount(this.m_predicateIndex);
                reverseAxesWalker.setPrevWalker(null);
                reverseAxesWalker.setNextWalker(null);
                this.wi().setLastUsedWalker(reverseAxesWalker);
                while (-1 != (n3 = reverseAxesWalker.nextNode())) {
                    ++n2;
                }
                Object var7_7 = null;
                this.wi().setLastUsedWalker(axesWalker);
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                Object var7_8 = null;
                this.wi().setLastUsedWalker(axesWalker);
            }
        }
        catch (Throwable throwable) {
            Object var7_9 = null;
            this.wi().setLastUsedWalker(axesWalker);
            throw throwable;
        }
        return n2;
    }
}

