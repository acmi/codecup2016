/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.util.StringTokenizer;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.MultiValuedNodeHeapIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public class KeyIndex
extends DTMAxisIteratorBase {
    private Hashtable _index;
    private int _currentDocumentNode = -1;
    private Hashtable _rootToIndexMap = new Hashtable();
    private IntegerArray _nodes = null;
    private DOM _dom;
    private DOMEnhancedForDTM _enhancedDOM;
    private int _markedPosition = 0;
    private static final IntegerArray EMPTY_NODES = new IntegerArray(0);

    public KeyIndex(int n2) {
    }

    public void setRestartable(boolean bl) {
    }

    public void add(Object object, int n2, int n3) {
        IntegerArray integerArray;
        if (this._currentDocumentNode != n3) {
            this._currentDocumentNode = n3;
            this._index = new Hashtable();
            this._rootToIndexMap.put(new Integer(n3), this._index);
        }
        if ((integerArray = (IntegerArray)this._index.get(object)) == null) {
            integerArray = new IntegerArray();
            this._index.put(object, integerArray);
            integerArray.add(n2);
        } else if (n2 != integerArray.at(integerArray.cardinality() - 1)) {
            integerArray.add(n2);
        }
    }

    public void merge(KeyIndex keyIndex) {
        if (keyIndex == null) {
            return;
        }
        if (keyIndex._nodes != null) {
            if (this._nodes == null) {
                this._nodes = (IntegerArray)keyIndex._nodes.clone();
            } else {
                this._nodes.merge(keyIndex._nodes);
            }
        }
    }

    public void lookupId(Object object) {
        this._nodes = null;
        StringTokenizer stringTokenizer = new StringTokenizer((String)object, " \n\t");
        while (stringTokenizer.hasMoreElements()) {
            String string = (String)stringTokenizer.nextElement();
            IntegerArray integerArray = (IntegerArray)this._index.get(string);
            if (integerArray == null && this._enhancedDOM != null && this._enhancedDOM.hasDOMSource()) {
                integerArray = this.getDOMNodeById(string);
            }
            if (integerArray == null) continue;
            if (this._nodes == null) {
                this._nodes = integerArray = (IntegerArray)integerArray.clone();
                continue;
            }
            this._nodes.merge(integerArray);
        }
    }

    public IntegerArray getDOMNodeById(String string) {
        int n2;
        IntegerArray integerArray = null;
        if (this._enhancedDOM != null && (n2 = this._enhancedDOM.getElementById(string)) != -1) {
            Integer n3 = new Integer(this._enhancedDOM.getDocument());
            Hashtable hashtable = (Hashtable)this._rootToIndexMap.get(n3);
            if (hashtable == null) {
                hashtable = new Hashtable();
                this._rootToIndexMap.put(n3, hashtable);
            } else {
                integerArray = (IntegerArray)hashtable.get(string);
            }
            if (integerArray == null) {
                integerArray = new IntegerArray();
                hashtable.put(string, integerArray);
            }
            integerArray.add(this._enhancedDOM.getNodeHandle(n2));
        }
        return integerArray;
    }

    public void lookupKey(Object object) {
        IntegerArray integerArray = (IntegerArray)this._index.get(object);
        this._nodes = integerArray != null ? (IntegerArray)integerArray.clone() : null;
        this._position = 0;
    }

    public int next() {
        if (this._nodes == null) {
            return -1;
        }
        int n2 = this._position < this._nodes.cardinality() ? this._dom.getNodeHandle(this._nodes.at(this._position++)) : -1;
        return n2;
    }

    public int containsID(int n2, Object object) {
        String string = (String)object;
        int n3 = this._dom.getAxisIterator(19).setStartNode(n2).next();
        Hashtable hashtable = (Hashtable)this._rootToIndexMap.get(new Integer(n3));
        StringTokenizer stringTokenizer = new StringTokenizer(string, " \n\t");
        while (stringTokenizer.hasMoreElements()) {
            String string2 = (String)stringTokenizer.nextElement();
            IntegerArray integerArray = null;
            if (hashtable != null) {
                integerArray = (IntegerArray)hashtable.get(string2);
            }
            if (integerArray == null && this._enhancedDOM != null && this._enhancedDOM.hasDOMSource()) {
                integerArray = this.getDOMNodeById(string2);
            }
            if (integerArray == null || integerArray.indexOf(n2) < 0) continue;
            return 1;
        }
        return 0;
    }

    public int containsKey(int n2, Object object) {
        int n3 = this._dom.getAxisIterator(19).setStartNode(n2).next();
        Hashtable hashtable = (Hashtable)this._rootToIndexMap.get(new Integer(n3));
        if (hashtable != null) {
            IntegerArray integerArray = (IntegerArray)hashtable.get(object);
            return integerArray != null && integerArray.indexOf(n2) >= 0 ? 1 : 0;
        }
        return 0;
    }

    public DTMAxisIterator reset() {
        this._position = 0;
        return this;
    }

    public int getLast() {
        return this._nodes == null ? 0 : this._nodes.cardinality();
    }

    public int getPosition() {
        return this._position;
    }

    public void setMark() {
        this._markedPosition = this._position;
    }

    public void gotoMark() {
        this._position = this._markedPosition;
    }

    public DTMAxisIterator setStartNode(int n2) {
        if (n2 == -1) {
            this._nodes = null;
        } else if (this._nodes != null) {
            this._position = 0;
        }
        return this;
    }

    public int getStartNode() {
        return 0;
    }

    public boolean isReverse() {
        return false;
    }

    public DTMAxisIterator cloneIterator() {
        KeyIndex keyIndex = new KeyIndex(0);
        keyIndex._index = this._index;
        keyIndex._rootToIndexMap = this._rootToIndexMap;
        keyIndex._nodes = this._nodes;
        keyIndex._position = this._position;
        return keyIndex;
    }

    public void setDom(DOM dOM) {
        DOM dOM2;
        this._dom = dOM;
        if (dOM instanceof DOMEnhancedForDTM) {
            this._enhancedDOM = (DOMEnhancedForDTM)dOM;
        } else if (dOM instanceof DOMAdapter && (dOM2 = ((DOMAdapter)dOM).getDOMImpl()) instanceof DOMEnhancedForDTM) {
            this._enhancedDOM = (DOMEnhancedForDTM)dOM2;
        }
    }

    public KeyIndexIterator getKeyIndexIterator(Object object, boolean bl) {
        if (object instanceof DTMAxisIterator) {
            return this.getKeyIndexIterator((DTMAxisIterator)object, bl);
        }
        return this.getKeyIndexIterator(BasisLibrary.stringF(object, this._dom), bl);
    }

    public KeyIndexIterator getKeyIndexIterator(String string, boolean bl) {
        return new KeyIndexIterator(this, string, bl);
    }

    public KeyIndexIterator getKeyIndexIterator(DTMAxisIterator dTMAxisIterator, boolean bl) {
        return new KeyIndexIterator(this, dTMAxisIterator, bl);
    }

    static Hashtable access$000(KeyIndex keyIndex) {
        return keyIndex._rootToIndexMap;
    }

    static DOMEnhancedForDTM access$100(KeyIndex keyIndex) {
        return keyIndex._enhancedDOM;
    }

    static DOM access$200(KeyIndex keyIndex) {
        return keyIndex._dom;
    }

    static IntegerArray access$300() {
        return EMPTY_NODES;
    }

    public class KeyIndexIterator
    extends MultiValuedNodeHeapIterator {
        private IntegerArray _nodes;
        private DTMAxisIterator _keyValueIterator;
        private String _keyValue;
        private boolean _isKeyIterator;
        private final KeyIndex this$0;

        KeyIndexIterator(KeyIndex keyIndex, String string, boolean bl) {
            this.this$0 = keyIndex;
            this._isKeyIterator = bl;
            this._keyValue = string;
        }

        KeyIndexIterator(KeyIndex keyIndex, DTMAxisIterator dTMAxisIterator, boolean bl) {
            this.this$0 = keyIndex;
            this._keyValueIterator = dTMAxisIterator;
            this._isKeyIterator = bl;
        }

        protected IntegerArray lookupNodes(int n2, String string) {
            IntegerArray integerArray = null;
            Hashtable hashtable = (Hashtable)KeyIndex.access$000(this.this$0).get(new Integer(n2));
            if (!this._isKeyIterator) {
                StringTokenizer stringTokenizer = new StringTokenizer(string, " \n\t");
                while (stringTokenizer.hasMoreElements()) {
                    String string2 = (String)stringTokenizer.nextElement();
                    IntegerArray integerArray2 = null;
                    if (hashtable != null) {
                        integerArray2 = (IntegerArray)hashtable.get(string2);
                    }
                    if (integerArray2 == null && KeyIndex.access$100(this.this$0) != null && KeyIndex.access$100(this.this$0).hasDOMSource()) {
                        integerArray2 = this.this$0.getDOMNodeById(string2);
                    }
                    if (integerArray2 == null) continue;
                    if (integerArray == null) {
                        integerArray = (IntegerArray)integerArray2.clone();
                        continue;
                    }
                    integerArray.merge(integerArray2);
                }
            } else if (hashtable != null) {
                integerArray = (IntegerArray)hashtable.get(string);
            }
            return integerArray;
        }

        public DTMAxisIterator setStartNode(int n2) {
            this._startNode = n2;
            if (this._keyValueIterator != null) {
                this._keyValueIterator = this._keyValueIterator.setStartNode(n2);
            }
            this.init();
            return super.setStartNode(n2);
        }

        public int next() {
            int n2 = this._nodes != null ? (this._position < this._nodes.cardinality() ? this.returnNode(this._nodes.at(this._position)) : -1) : super.next();
            return n2;
        }

        public DTMAxisIterator reset() {
            if (this._nodes == null) {
                this.init();
            } else {
                super.reset();
            }
            return this.resetPosition();
        }

        protected void init() {
            super.init();
            this._position = 0;
            int n2 = KeyIndex.access$200(this.this$0).getAxisIterator(19).setStartNode(this._startNode).next();
            if (this._keyValueIterator == null) {
                this._nodes = this.lookupNodes(n2, this._keyValue);
                if (this._nodes == null) {
                    this._nodes = KeyIndex.access$300();
                }
            } else {
                DTMAxisIterator dTMAxisIterator = this._keyValueIterator.reset();
                boolean bl = false;
                boolean bl2 = false;
                this._nodes = null;
                int n3 = dTMAxisIterator.next();
                while (n3 != -1) {
                    String string = BasisLibrary.stringF(n3, KeyIndex.access$200(this.this$0));
                    IntegerArray integerArray = this.lookupNodes(n2, string);
                    if (integerArray != null) {
                        if (!bl2) {
                            this._nodes = integerArray;
                            bl2 = true;
                        } else {
                            if (this._nodes != null) {
                                this.addHeapNode(new KeyIndexHeapNode(this, this._nodes));
                                this._nodes = null;
                            }
                            this.addHeapNode(new KeyIndexHeapNode(this, integerArray));
                        }
                    }
                    n3 = dTMAxisIterator.next();
                }
                if (!bl2) {
                    this._nodes = KeyIndex.access$300();
                }
            }
        }

        public int getLast() {
            return this._nodes != null ? this._nodes.cardinality() : super.getLast();
        }

        public int getNodeByPosition(int n2) {
            int n3 = -1;
            if (this._nodes != null) {
                if (n2 > 0) {
                    if (n2 <= this._nodes.cardinality()) {
                        this._position = n2;
                        n3 = this._nodes.at(n2 - 1);
                    } else {
                        this._position = this._nodes.cardinality();
                    }
                }
            } else {
                n3 = super.getNodeByPosition(n2);
            }
            return n3;
        }

        protected class KeyIndexHeapNode
        extends MultiValuedNodeHeapIterator.HeapNode {
            private IntegerArray _nodes;
            private int _position;
            private int _markPosition;
            private final KeyIndexIterator this$1;

            KeyIndexHeapNode(KeyIndexIterator keyIndexIterator, IntegerArray integerArray) {
                super(keyIndexIterator);
                this.this$1 = keyIndexIterator;
                this._position = 0;
                this._markPosition = -1;
                this._nodes = integerArray;
            }

            public int step() {
                if (this._position < this._nodes.cardinality()) {
                    this._node = this._nodes.at(this._position);
                    ++this._position;
                } else {
                    this._node = -1;
                }
                return this._node;
            }

            public MultiValuedNodeHeapIterator.HeapNode cloneHeapNode() {
                KeyIndexHeapNode keyIndexHeapNode = (KeyIndexHeapNode)super.cloneHeapNode();
                keyIndexHeapNode._nodes = this._nodes;
                keyIndexHeapNode._position = this._position;
                keyIndexHeapNode._markPosition = this._markPosition;
                return keyIndexHeapNode;
            }

            public void setMark() {
                this._markPosition = this._position;
            }

            public void gotoMark() {
                this._position = this._markPosition;
            }

            public boolean isLessThan(MultiValuedNodeHeapIterator.HeapNode heapNode) {
                return this._node < heapNode._node;
            }

            public MultiValuedNodeHeapIterator.HeapNode setStartNode(int n2) {
                return this;
            }

            public MultiValuedNodeHeapIterator.HeapNode reset() {
                this._position = 0;
                return this;
            }
        }

    }

}

