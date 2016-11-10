/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public class NSInfo {
    public String m_namespace;
    public boolean m_hasXMLNSAttrs;
    public boolean m_hasProcessedNS;
    public int m_ancestorHasXMLNSAttrs;

    public NSInfo(boolean bl, boolean bl2) {
        this.m_hasProcessedNS = bl;
        this.m_hasXMLNSAttrs = bl2;
        this.m_namespace = null;
        this.m_ancestorHasXMLNSAttrs = 0;
    }

    public NSInfo(boolean bl, boolean bl2, int n2) {
        this.m_hasProcessedNS = bl;
        this.m_hasXMLNSAttrs = bl2;
        this.m_ancestorHasXMLNSAttrs = n2;
        this.m_namespace = null;
    }

    public NSInfo(String string, boolean bl) {
        this.m_hasProcessedNS = true;
        this.m_hasXMLNSAttrs = bl;
        this.m_namespace = string;
        this.m_ancestorHasXMLNSAttrs = 0;
    }
}

