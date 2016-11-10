/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.dom.Filter;
import org.apache.xalan.xsltc.dom.StepIterator;
import org.apache.xml.dtm.DTMAxisIterator;

public final class FilteredStepIterator
extends StepIterator {
    private Filter _filter;

    public FilteredStepIterator(DTMAxisIterator dTMAxisIterator, DTMAxisIterator dTMAxisIterator2, Filter filter) {
        super(dTMAxisIterator, dTMAxisIterator2);
        this._filter = filter;
    }

    public int next() {
        int n2;
        while ((n2 = super.next()) != -1) {
            if (!this._filter.test(n2)) continue;
            return this.returnNode(n2);
        }
        return n2;
    }
}

