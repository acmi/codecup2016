/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.axes.WalkingIterator;
import org.apache.xpath.compiler.Compiler;

public class WalkingIteratorSorted
extends WalkingIterator {
    protected boolean m_inNaturalOrderStatic = false;

    WalkingIteratorSorted(Compiler compiler, int n2, int n3, boolean bl) throws TransformerException {
        super(compiler, n2, n3, bl);
    }

    public boolean isDocOrdered() {
        return this.m_inNaturalOrderStatic;
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        int n3 = this.getAnalysisBits();
        this.m_inNaturalOrderStatic = WalkerFactory.isNaturalDocOrder(n3);
    }
}

