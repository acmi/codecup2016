/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import java.util.BitSet;
import org.apache.xml.res.XMLMessages;

public class CoroutineManager {
    BitSet m_activeIDs = new BitSet();
    Object m_yield = null;
    int m_nextCoroutine = -1;

    public synchronized int co_joinCoroutineSet(int n2) {
        if (n2 >= 0) {
            if (n2 >= 1024 || this.m_activeIDs.get(n2)) {
                return -1;
            }
        } else {
            for (n2 = 0; n2 < 1024 && this.m_activeIDs.get(n2); ++n2) {
            }
            if (n2 >= 1024) {
                return -1;
            }
        }
        this.m_activeIDs.set(n2);
        return n2;
    }

    public synchronized Object co_entry_pause(int n2) throws NoSuchMethodException {
        if (!this.m_activeIDs.get(n2)) {
            throw new NoSuchMethodException();
        }
        while (this.m_nextCoroutine != n2) {
            try {
                this.wait();
            }
            catch (InterruptedException interruptedException) {}
        }
        return this.m_yield;
    }

    public synchronized Object co_resume(Object object, int n2, int n3) throws NoSuchMethodException {
        if (!this.m_activeIDs.get(n3)) {
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[]{Integer.toString(n3)}));
        }
        this.m_yield = object;
        this.m_nextCoroutine = n3;
        this.notify();
        while (this.m_nextCoroutine != n2 || this.m_nextCoroutine == -1 || this.m_nextCoroutine == -1) {
            try {
                this.wait();
            }
            catch (InterruptedException interruptedException) {}
        }
        if (this.m_nextCoroutine == -1) {
            this.co_exit(n2);
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_CO_EXIT", null));
        }
        return this.m_yield;
    }

    public synchronized void co_exit(int n2) {
        this.m_activeIDs.clear(n2);
        this.m_nextCoroutine = -1;
        this.notify();
    }

    public synchronized void co_exit_to(Object object, int n2, int n3) throws NoSuchMethodException {
        if (!this.m_activeIDs.get(n3)) {
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[]{Integer.toString(n3)}));
        }
        this.m_yield = object;
        this.m_nextCoroutine = n3;
        this.m_activeIDs.clear(n2);
        this.notify();
    }
}

