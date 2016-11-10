/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;

public final class IteratorPool
implements Serializable {
    private final DTMIterator m_orig;
    private final ArrayList m_freeStack;

    public IteratorPool(DTMIterator dTMIterator) {
        this.m_orig = dTMIterator;
        this.m_freeStack = new ArrayList();
    }

    public synchronized DTMIterator getInstanceOrThrow() throws CloneNotSupportedException {
        if (this.m_freeStack.isEmpty()) {
            return (DTMIterator)this.m_orig.clone();
        }
        DTMIterator dTMIterator = (DTMIterator)this.m_freeStack.remove(this.m_freeStack.size() - 1);
        return dTMIterator;
    }

    public synchronized DTMIterator getInstance() {
        if (this.m_freeStack.isEmpty()) {
            try {
                return (DTMIterator)this.m_orig.clone();
            }
            catch (Exception exception) {
                throw new WrappedRuntimeException(exception);
            }
        }
        DTMIterator dTMIterator = (DTMIterator)this.m_freeStack.remove(this.m_freeStack.size() - 1);
        return dTMIterator;
    }

    public synchronized void freeInstance(DTMIterator dTMIterator) {
        this.m_freeStack.add(dTMIterator);
    }
}

