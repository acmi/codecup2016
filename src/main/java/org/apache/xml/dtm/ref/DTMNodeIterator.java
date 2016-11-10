/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMDOMException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class DTMNodeIterator
implements NodeIterator {
    private DTMIterator dtm_iter;
    private boolean valid = true;

    public DTMNodeIterator(DTMIterator dTMIterator) {
        try {
            this.dtm_iter = (DTMIterator)dTMIterator.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new WrappedRuntimeException(cloneNotSupportedException);
        }
    }

    public DTMIterator getDTMIterator() {
        return this.dtm_iter;
    }

    public void detach() {
        this.valid = false;
    }

    public boolean getExpandEntityReferences() {
        return false;
    }

    public NodeFilter getFilter() {
        throw new DTMDOMException(9);
    }

    public Node getRoot() {
        int n2 = this.dtm_iter.getRoot();
        return this.dtm_iter.getDTM(n2).getNode(n2);
    }

    public int getWhatToShow() {
        return this.dtm_iter.getWhatToShow();
    }

    public Node nextNode() throws DOMException {
        if (!this.valid) {
            throw new DTMDOMException(11);
        }
        int n2 = this.dtm_iter.nextNode();
        if (n2 == -1) {
            return null;
        }
        return this.dtm_iter.getDTM(n2).getNode(n2);
    }

    public Node previousNode() {
        if (!this.valid) {
            throw new DTMDOMException(11);
        }
        int n2 = this.dtm_iter.previousNode();
        if (n2 == -1) {
            return null;
        }
        return this.dtm_iter.getDTM(n2).getNode(n2);
    }
}

