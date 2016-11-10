/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMWSFilter;

public class DOMWSFilter
implements DTMWSFilter {
    private AbstractTranslet m_translet;
    private StripFilter m_filter;
    private Hashtable m_mappings;
    private DTM m_currentDTM;
    private short[] m_currentMapping;

    public DOMWSFilter(AbstractTranslet abstractTranslet) {
        this.m_translet = abstractTranslet;
        this.m_mappings = new Hashtable();
        if (abstractTranslet instanceof StripFilter) {
            this.m_filter = (StripFilter)((Object)abstractTranslet);
        }
    }

    public short getShouldStripSpace(int n2, DTM dTM) {
        if (this.m_filter != null && dTM instanceof DOM) {
            DOM dOM = (DOM)((Object)dTM);
            int n3 = 0;
            if (dTM instanceof DOMEnhancedForDTM) {
                short[] arrs;
                DOMEnhancedForDTM dOMEnhancedForDTM = (DOMEnhancedForDTM)((Object)dTM);
                if (dTM == this.m_currentDTM) {
                    arrs = this.m_currentMapping;
                } else {
                    arrs = (short[])this.m_mappings.get(dTM);
                    if (arrs == null) {
                        arrs = dOMEnhancedForDTM.getMapping(this.m_translet.getNamesArray(), this.m_translet.getUrisArray(), this.m_translet.getTypesArray());
                        this.m_mappings.put(dTM, arrs);
                        this.m_currentDTM = dTM;
                        this.m_currentMapping = arrs;
                    }
                }
                int n4 = dOMEnhancedForDTM.getExpandedTypeID(n2);
                n3 = n4 >= 0 && n4 < arrs.length ? arrs[n4] : -1;
            } else {
                return 3;
            }
            if (this.m_filter.stripSpace(dOM, n2, n3)) {
                return 2;
            }
            return 1;
        }
        return 1;
    }
}

