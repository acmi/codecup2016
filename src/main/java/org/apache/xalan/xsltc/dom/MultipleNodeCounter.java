/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.dom.NodeCounter;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract class MultipleNodeCounter
extends NodeCounter {
    private DTMAxisIterator _precSiblings = null;

    public MultipleNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
        super(translet, dOM, dTMAxisIterator);
    }

    public NodeCounter setStartNode(int n2) {
        this._node = n2;
        this._nodeType = this._document.getExpandedTypeID(n2);
        this._precSiblings = this._document.getAxisIterator(12);
        return this;
    }

    public String getCounter() {
        int n2;
        if (this._value != -2.147483648E9) {
            if (this._value == 0.0) {
                return "0";
            }
            if (Double.isNaN(this._value)) {
                return "NaN";
            }
            if (this._value < 0.0 && Double.isInfinite(this._value)) {
                return "-Infinity";
            }
            if (Double.isInfinite(this._value)) {
                return "Infinity";
            }
            return this.formatNumbers((int)this._value);
        }
        IntegerArray integerArray = new IntegerArray();
        int n3 = this._node;
        integerArray.add(n3);
        while ((n3 = this._document.getParent(n3)) > -1 && !this.matchesFrom(n3)) {
            integerArray.add(n3);
        }
        int n4 = integerArray.cardinality();
        int[] arrn = new int[n4];
        for (n2 = 0; n2 < n4; ++n2) {
            arrn[n2] = Integer.MIN_VALUE;
        }
        n2 = 0;
        int n5 = n4 - 1;
        while (n5 >= 0) {
            int n6 = arrn[n2];
            int n7 = integerArray.at(n5);
            if (this.matchesCount(n7)) {
                this._precSiblings.setStartNode(n7);
                while ((n3 = this._precSiblings.next()) != -1) {
                    if (!this.matchesCount(n3)) continue;
                    arrn[n2] = arrn[n2] == Integer.MIN_VALUE ? 1 : arrn[n2] + 1;
                }
                arrn[n2] = arrn[n2] == Integer.MIN_VALUE ? 1 : arrn[n2] + 1;
            }
            --n5;
            ++n2;
        }
        return this.formatNumbers(arrn);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
        return new DefaultMultipleNodeCounter(translet, dOM, dTMAxisIterator);
    }

    static class DefaultMultipleNodeCounter
    extends MultipleNodeCounter {
        public DefaultMultipleNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
            super(translet, dOM, dTMAxisIterator);
        }
    }

}

