/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.dom.NodeCounter;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract class SingleNodeCounter
extends NodeCounter {
    private static final int[] EmptyArray = new int[0];
    DTMAxisIterator _countSiblings = null;

    public SingleNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
        super(translet, dOM, dTMAxisIterator);
    }

    public NodeCounter setStartNode(int n2) {
        this._node = n2;
        this._nodeType = this._document.getExpandedTypeID(n2);
        this._countSiblings = this._document.getAxisIterator(12);
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
            n2 = (int)this._value;
        } else {
            int n3 = this._node;
            n2 = 0;
            if (!this.matchesCount(n3)) {
                while ((n3 = this._document.getParent(n3)) > -1 && !this.matchesCount(n3)) {
                    if (!this.matchesFrom(n3)) continue;
                    n3 = -1;
                    break;
                }
            }
            if (n3 != -1) {
                this._countSiblings.setStartNode(n3);
                do {
                    if (!this.matchesCount(n3)) continue;
                    ++n2;
                } while ((n3 = this._countSiblings.next()) != -1);
            } else {
                return this.formatNumbers(EmptyArray);
            }
        }
        return this.formatNumbers(n2);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
        return new DefaultSingleNodeCounter(translet, dOM, dTMAxisIterator);
    }

    static class DefaultSingleNodeCounter
    extends SingleNodeCounter {
        public DefaultSingleNodeCounter(Translet translet, DOM dOM, DTMAxisIterator dTMAxisIterator) {
            super(translet, dOM, dTMAxisIterator);
        }

        public NodeCounter setStartNode(int n2) {
            this._node = n2;
            this._nodeType = this._document.getExpandedTypeID(n2);
            this._countSiblings = this._document.getTypedAxisIterator(12, this._document.getExpandedTypeID(n2));
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
                n2 = (int)this._value;
            } else {
                int n3;
                n2 = 1;
                this._countSiblings.setStartNode(this._node);
                while ((n3 = this._countSiblings.next()) != -1) {
                    ++n2;
                }
            }
            return this.formatNumbers(n2);
        }
    }

}

