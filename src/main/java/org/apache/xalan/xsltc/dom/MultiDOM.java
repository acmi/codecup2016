/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.AdaptiveResultTreeImpl;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.SimpleResultTreeImpl;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMDefaultBase;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.SuballocatedIntVector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class MultiDOM
implements DOM {
    private static final int NO_TYPE = -2;
    private static final int INITIAL_SIZE = 4;
    private DOM[] _adapters = new DOM[4];
    private DOMAdapter _main;
    private DTMManager _dtmManager;
    private int _free = 1;
    private int _size = 4;
    private Hashtable _documents = new Hashtable();

    public MultiDOM(DOM dOM) {
        DOMAdapter dOMAdapter = (DOMAdapter)dOM;
        this._adapters[0] = dOMAdapter;
        this._main = dOMAdapter;
        DOM dOM2 = dOMAdapter.getDOMImpl();
        if (dOM2 instanceof DTMDefaultBase) {
            this._dtmManager = ((DTMDefaultBase)((Object)dOM2)).getManager();
        }
        this.addDOMAdapter(dOMAdapter, false);
    }

    public int nextMask() {
        return this._free;
    }

    public void setupMapping(String[] arrstring, String[] arrstring2, int[] arrn, String[] arrstring3) {
    }

    public int addDOMAdapter(DOMAdapter dOMAdapter) {
        return this.addDOMAdapter(dOMAdapter, true);
    }

    private int addDOMAdapter(DOMAdapter dOMAdapter, boolean bl) {
        Object object;
        Object object2;
        DOM dOM = dOMAdapter.getDOMImpl();
        int n2 = 1;
        int n3 = 1;
        SuballocatedIntVector suballocatedIntVector = null;
        if (dOM instanceof DTMDefaultBase) {
            object = (DTMDefaultBase)((Object)dOM);
            suballocatedIntVector = object.getDTMIDs();
            n3 = suballocatedIntVector.size();
            n2 = suballocatedIntVector.elementAt(n3 - 1) >>> 16;
        } else if (dOM instanceof SimpleResultTreeImpl) {
            object = (SimpleResultTreeImpl)dOM;
            n2 = object.getDocument() >>> 16;
        }
        if (n2 >= this._size) {
            int n4 = this._size;
            do {
                this._size *= 2;
            } while (this._size <= n2);
            object2 = new DOMAdapter[this._size];
            System.arraycopy(this._adapters, 0, object2, 0, n4);
            this._adapters = object2;
        }
        this._free = n2 + 1;
        if (n3 == 1) {
            this._adapters[n2] = dOMAdapter;
        } else if (suballocatedIntVector != null) {
            int n5 = 0;
            for (int i2 = n3 - 1; i2 >= 0; --i2) {
                n5 = suballocatedIntVector.elementAt(i2) >>> 16;
                this._adapters[n5] = dOMAdapter;
            }
            n2 = n5;
        }
        if (bl) {
            object = dOMAdapter.getDocumentURI(0);
            this._documents.put(object, new Integer(n2));
        }
        if (dOM instanceof AdaptiveResultTreeImpl && (object2 = (object = (AdaptiveResultTreeImpl)dOM).getNestedDOM()) != null) {
            DOMAdapter dOMAdapter2 = new DOMAdapter((DOM)object2, dOMAdapter.getNamesArray(), dOMAdapter.getUrisArray(), dOMAdapter.getTypesArray(), dOMAdapter.getNamespaceArray());
            this.addDOMAdapter(dOMAdapter2);
        }
        return n2;
    }

    public int getDocumentMask(String string) {
        Integer n2 = (Integer)this._documents.get(string);
        if (n2 == null) {
            return -1;
        }
        return n2;
    }

    public DOM getDOMAdapter(String string) {
        Integer n2 = (Integer)this._documents.get(string);
        if (n2 == null) {
            return null;
        }
        return this._adapters[n2];
    }

    public int getDocument() {
        return this._main.getDocument();
    }

    public DTMManager getDTMManager() {
        return this._dtmManager;
    }

    public DTMAxisIterator getIterator() {
        return this._main.getIterator();
    }

    public String getStringValue() {
        return this._main.getStringValue();
    }

    public DTMAxisIterator getChildren(int n2) {
        return this._adapters[this.getDTMId(n2)].getChildren(n2);
    }

    public DTMAxisIterator getTypedChildren(int n2) {
        return new AxisIterator(this, 3, n2);
    }

    public DTMAxisIterator getAxisIterator(int n2) {
        return new AxisIterator(this, n2, -2);
    }

    public DTMAxisIterator getTypedAxisIterator(int n2, int n3) {
        return new AxisIterator(this, n2, n3);
    }

    public DTMAxisIterator getNthDescendant(int n2, int n3, boolean bl) {
        return this._adapters[this.getDTMId(n2)].getNthDescendant(n2, n3, bl);
    }

    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator dTMAxisIterator, int n2, String string, boolean bl) {
        return new NodeValueIterator(this, dTMAxisIterator, n2, string, bl);
    }

    public DTMAxisIterator getNamespaceAxisIterator(int n2, int n3) {
        DTMAxisIterator dTMAxisIterator = this._main.getNamespaceAxisIterator(n2, n3);
        return dTMAxisIterator;
    }

    public DTMAxisIterator orderNodes(DTMAxisIterator dTMAxisIterator, int n2) {
        return this._adapters[this.getDTMId(n2)].orderNodes(dTMAxisIterator, n2);
    }

    public int getExpandedTypeID(int n2) {
        if (n2 != -1) {
            return this._adapters[n2 >>> 16].getExpandedTypeID(n2);
        }
        return -1;
    }

    public int getNamespaceType(int n2) {
        return this._adapters[this.getDTMId(n2)].getNamespaceType(n2);
    }

    public int getNSType(int n2) {
        return this._adapters[this.getDTMId(n2)].getNSType(n2);
    }

    public int getParent(int n2) {
        if (n2 == -1) {
            return -1;
        }
        return this._adapters[n2 >>> 16].getParent(n2);
    }

    public int getAttributeNode(int n2, int n3) {
        if (n3 == -1) {
            return -1;
        }
        return this._adapters[n3 >>> 16].getAttributeNode(n2, n3);
    }

    public String getNodeName(int n2) {
        if (n2 == -1) {
            return "";
        }
        return this._adapters[n2 >>> 16].getNodeName(n2);
    }

    public String getNodeNameX(int n2) {
        if (n2 == -1) {
            return "";
        }
        return this._adapters[n2 >>> 16].getNodeNameX(n2);
    }

    public String getNamespaceName(int n2) {
        if (n2 == -1) {
            return "";
        }
        return this._adapters[n2 >>> 16].getNamespaceName(n2);
    }

    public String getStringValueX(int n2) {
        if (n2 == -1) {
            return "";
        }
        return this._adapters[n2 >>> 16].getStringValueX(n2);
    }

    public void copy(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (n2 != -1) {
            this._adapters[n2 >>> 16].copy(n2, serializationHandler);
        }
    }

    public void copy(DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler) throws TransletException {
        int n2;
        while ((n2 = dTMAxisIterator.next()) != -1) {
            this._adapters[n2 >>> 16].copy(n2, serializationHandler);
        }
    }

    public String shallowCopy(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (n2 == -1) {
            return "";
        }
        return this._adapters[n2 >>> 16].shallowCopy(n2, serializationHandler);
    }

    public boolean lessThan(int n2, int n3) {
        int n4;
        if (n2 == -1) {
            return true;
        }
        if (n3 == -1) {
            return false;
        }
        int n5 = this.getDTMId(n2);
        return n5 == (n4 = this.getDTMId(n3)) ? this._adapters[n5].lessThan(n2, n3) : n5 < n4;
    }

    public void characters(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (n2 != -1) {
            this._adapters[n2 >>> 16].characters(n2, serializationHandler);
        }
    }

    public void setFilter(StripFilter stripFilter) {
        for (int i2 = 0; i2 < this._free; ++i2) {
            if (this._adapters[i2] == null) continue;
            this._adapters[i2].setFilter(stripFilter);
        }
    }

    public Node makeNode(int n2) {
        if (n2 == -1) {
            return null;
        }
        return this._adapters[this.getDTMId(n2)].makeNode(n2);
    }

    public Node makeNode(DTMAxisIterator dTMAxisIterator) {
        return this._main.makeNode(dTMAxisIterator);
    }

    public NodeList makeNodeList(int n2) {
        if (n2 == -1) {
            return null;
        }
        return this._adapters[this.getDTMId(n2)].makeNodeList(n2);
    }

    public NodeList makeNodeList(DTMAxisIterator dTMAxisIterator) {
        return this._main.makeNodeList(dTMAxisIterator);
    }

    public String getLanguage(int n2) {
        return this._adapters[this.getDTMId(n2)].getLanguage(n2);
    }

    public int getSize() {
        int n2 = 0;
        for (int i2 = 0; i2 < this._size; ++i2) {
            n2 += this._adapters[i2].getSize();
        }
        return n2;
    }

    public String getDocumentURI(int n2) {
        if (n2 == -1) {
            n2 = 0;
        }
        return this._adapters[n2 >>> 16].getDocumentURI(0);
    }

    public boolean isElement(int n2) {
        if (n2 == -1) {
            return false;
        }
        return this._adapters[n2 >>> 16].isElement(n2);
    }

    public boolean isAttribute(int n2) {
        if (n2 == -1) {
            return false;
        }
        return this._adapters[n2 >>> 16].isAttribute(n2);
    }

    public int getDTMId(int n2) {
        int n3;
        if (n2 == -1) {
            return 0;
        }
        for (n3 = n2 >>> 16; n3 >= 2 && this._adapters[n3] == this._adapters[n3 - 1]; --n3) {
        }
        return n3;
    }

    public int getNodeIdent(int n2) {
        return this._adapters[n2 >>> 16].getNodeIdent(n2);
    }

    public int getNodeHandle(int n2) {
        return this._main.getNodeHandle(n2);
    }

    public DOM getResultTreeFrag(int n2, int n3) {
        return this._main.getResultTreeFrag(n2, n3);
    }

    public DOM getResultTreeFrag(int n2, int n3, boolean bl) {
        return this._main.getResultTreeFrag(n2, n3, bl);
    }

    public DOM getMain() {
        return this._main;
    }

    public SerializationHandler getOutputDomBuilder() {
        return this._main.getOutputDomBuilder();
    }

    public String lookupNamespace(int n2, String string) throws TransletException {
        return this._main.lookupNamespace(n2, string);
    }

    public String getUnparsedEntityURI(String string) {
        return this._main.getUnparsedEntityURI(string);
    }

    public Hashtable getElementsWithIDs() {
        return this._main.getElementsWithIDs();
    }

    static DOM[] access$000(MultiDOM multiDOM) {
        return multiDOM._adapters;
    }

    private final class NodeValueIterator
    extends DTMAxisIteratorBase {
        private DTMAxisIterator _source;
        private String _value;
        private boolean _op;
        private final boolean _isReverse;
        private int _returnType;
        private final MultiDOM this$0;

        public NodeValueIterator(MultiDOM multiDOM, DTMAxisIterator dTMAxisIterator, int n2, String string, boolean bl) {
            this.this$0 = multiDOM;
            this._returnType = 1;
            this._source = dTMAxisIterator;
            this._returnType = n2;
            this._value = string;
            this._op = bl;
            this._isReverse = dTMAxisIterator.isReverse();
        }

        public boolean isReverse() {
            return this._isReverse;
        }

        public DTMAxisIterator cloneIterator() {
            try {
                NodeValueIterator nodeValueIterator = (NodeValueIterator)Object.super.clone();
                nodeValueIterator._source = this._source.cloneIterator();
                nodeValueIterator.setRestartable(false);
                return nodeValueIterator.reset();
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", cloneNotSupportedException.toString());
                return null;
            }
        }

        public void setRestartable(boolean bl) {
            this._isRestartable = bl;
            this._source.setRestartable(bl);
        }

        public DTMAxisIterator reset() {
            this._source.reset();
            return this.resetPosition();
        }

        public int next() {
            int n2;
            while ((n2 = this._source.next()) != -1) {
                String string = this.this$0.getStringValueX(n2);
                if (this._value.equals(string) != this._op) continue;
                if (this._returnType == 0) {
                    return this.returnNode(n2);
                }
                return this.returnNode(this.this$0.getParent(n2));
            }
            return -1;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (this._isRestartable) {
                this._startNode = n2;
                this._source.setStartNode(this._startNode);
                return this.resetPosition();
            }
            return this;
        }

        public void setMark() {
            this._source.setMark();
        }

        public void gotoMark() {
            this._source.gotoMark();
        }
    }

    private final class AxisIterator
    extends DTMAxisIteratorBase {
        private final int _axis;
        private final int _type;
        private DTMAxisIterator _source;
        private int _dtmId;
        private final MultiDOM this$0;

        public AxisIterator(MultiDOM multiDOM, int n2, int n3) {
            this.this$0 = multiDOM;
            this._dtmId = -1;
            this._axis = n2;
            this._type = n3;
        }

        public int next() {
            if (this._source == null) {
                return -1;
            }
            return this._source.next();
        }

        public void setRestartable(boolean bl) {
            if (this._source != null) {
                this._source.setRestartable(bl);
            }
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == -1) {
                return this;
            }
            int n3 = n2 >>> 16;
            if (this._source == null || this._dtmId != n3) {
                this._source = this._type == -2 ? MultiDOM.access$000(this.this$0)[n3].getAxisIterator(this._axis) : (this._axis == 3 ? MultiDOM.access$000(this.this$0)[n3].getTypedChildren(this._type) : MultiDOM.access$000(this.this$0)[n3].getTypedAxisIterator(this._axis, this._type));
            }
            this._dtmId = n3;
            this._source.setStartNode(n2);
            return this;
        }

        public DTMAxisIterator reset() {
            if (this._source != null) {
                this._source.reset();
            }
            return this;
        }

        public int getLast() {
            if (this._source != null) {
                return this._source.getLast();
            }
            return -1;
        }

        public int getPosition() {
            if (this._source != null) {
                return this._source.getPosition();
            }
            return -1;
        }

        public boolean isReverse() {
            return Axis.isReverse(this._axis);
        }

        public void setMark() {
            if (this._source != null) {
                this._source.setMark();
            }
        }

        public void gotoMark() {
            if (this._source != null) {
                this._source.gotoMark();
            }
        }

        public DTMAxisIterator cloneIterator() {
            AxisIterator axisIterator = new AxisIterator(this.this$0, this._axis, this._type);
            if (this._source != null) {
                axisIterator._source = this._source.cloneIterator();
            }
            axisIterator._dtmId = this._dtmId;
            return axisIterator;
        }
    }

}

