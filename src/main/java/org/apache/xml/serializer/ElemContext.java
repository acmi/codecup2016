/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import org.apache.xml.serializer.ElemDesc;

final class ElemContext {
    final int m_currentElemDepth;
    ElemDesc m_elementDesc = null;
    String m_elementLocalName = null;
    String m_elementName = null;
    String m_elementURI = null;
    boolean m_isCdataSection;
    boolean m_isRaw = false;
    private ElemContext m_next;
    final ElemContext m_prev;
    boolean m_startTagOpen = false;

    ElemContext() {
        this.m_prev = this;
        this.m_currentElemDepth = 0;
    }

    private ElemContext(ElemContext elemContext) {
        this.m_prev = elemContext;
        this.m_currentElemDepth = elemContext.m_currentElemDepth + 1;
    }

    final ElemContext push() {
        ElemContext elemContext = this.m_next;
        if (elemContext == null) {
            this.m_next = elemContext = new ElemContext(this);
        }
        elemContext.m_startTagOpen = true;
        return elemContext;
    }

    final ElemContext push(String string, String string2, String string3) {
        ElemContext elemContext = this.m_next;
        if (elemContext == null) {
            this.m_next = elemContext = new ElemContext(this);
        }
        elemContext.m_elementName = string3;
        elemContext.m_elementLocalName = string2;
        elemContext.m_elementURI = string;
        elemContext.m_isCdataSection = false;
        elemContext.m_startTagOpen = true;
        return elemContext;
    }
}

