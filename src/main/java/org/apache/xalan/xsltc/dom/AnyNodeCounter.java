/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.dom.NodeCounter;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract class AnyNodeCounter
extends NodeCounter {
    public AnyNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
        super(translet, dOM, dTMAxisIterator);
    }

    public NodeCounter setStartNode(int n2) {
        this._node = n2;
        this._nodeType = this._document.getExpandedTypeID(n2);
        return this;
    }

    public String getCounter() {
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
        int n2 = this._document.getDocument();
        int n3 = 0;
        for (int i2 = this._node; i2 >= n2 && !this.matchesFrom(i2); --i2) {
            if (!this.matchesCount(i2)) continue;
            ++n3;
        }
        return this.formatNumbers(n3);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
        return new DefaultAnyNodeCounter(translet, dOM, dTMAxisIterator);
    }

    static class DefaultAnyNodeCounter
    extends AnyNodeCounter {
        public DefaultAnyNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
            super(translet, dOM, dTMAxisIterator);
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
                n2 = (int)this._value;
            } else {
                n2 = 0;
                int n3 = this._document.getExpandedTypeID(this._node);
                int n4 = this._document.getDocument();
                for (int i2 = this._node; i2 >= 0; --i2) {
                    if (n3 == this._document.getExpandedTypeID(i2)) {
                        ++n2;
                    }
                    if (i2 == n4) break;
                }
            }
            return this.formatNumbers(n2);
        }
    }

}

