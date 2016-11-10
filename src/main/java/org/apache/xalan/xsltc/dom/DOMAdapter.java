/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class DOMAdapter
implements DOM {
    private DOMEnhancedForDTM _enhancedDOM;
    private DOM _dom;
    private String[] _namesArray;
    private String[] _urisArray;
    private int[] _typesArray;
    private String[] _namespaceArray;
    private short[] _mapping = null;
    private int[] _reverse = null;
    private short[] _NSmapping = null;
    private short[] _NSreverse = null;
    private StripFilter _filter = null;
    private int _multiDOMMask;

    public DOMAdapter(DOM dOM, String[] arrstring, String[] arrstring2, int[] arrn, String[] arrstring3) {
        if (dOM instanceof DOMEnhancedForDTM) {
            this._enhancedDOM = (DOMEnhancedForDTM)dOM;
        }
        this._dom = dOM;
        this._namesArray = arrstring;
        this._urisArray = arrstring2;
        this._typesArray = arrn;
        this._namespaceArray = arrstring3;
    }

    public void setupMapping(String[] arrstring, String[] arrstring2, int[] arrn, String[] arrstring3) {
        this._namesArray = arrstring;
        this._urisArray = arrstring2;
        this._typesArray = arrn;
        this._namespaceArray = arrstring3;
    }

    public String[] getNamesArray() {
        return this._namesArray;
    }

    public String[] getUrisArray() {
        return this._urisArray;
    }

    public int[] getTypesArray() {
        return this._typesArray;
    }

    public String[] getNamespaceArray() {
        return this._namespaceArray;
    }

    public DOM getDOMImpl() {
        return this._dom;
    }

    private short[] getMapping() {
        if (this._mapping == null && this._enhancedDOM != null) {
            this._mapping = this._enhancedDOM.getMapping(this._namesArray, this._urisArray, this._typesArray);
        }
        return this._mapping;
    }

    private int[] getReverse() {
        if (this._reverse == null && this._enhancedDOM != null) {
            this._reverse = this._enhancedDOM.getReverseMapping(this._namesArray, this._urisArray, this._typesArray);
        }
        return this._reverse;
    }

    private short[] getNSMapping() {
        if (this._NSmapping == null && this._enhancedDOM != null) {
            this._NSmapping = this._enhancedDOM.getNamespaceMapping(this._namespaceArray);
        }
        return this._NSmapping;
    }

    private short[] getNSReverse() {
        if (this._NSreverse == null && this._enhancedDOM != null) {
            this._NSreverse = this._enhancedDOM.getReverseNamespaceMapping(this._namespaceArray);
        }
        return this._NSreverse;
    }

    public DTMAxisIterator getIterator() {
        return this._dom.getIterator();
    }

    public String getStringValue() {
        return this._dom.getStringValue();
    }

    public DTMAxisIterator getChildren(int n2) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getChildren(n2);
        }
        DTMAxisIterator dTMAxisIterator = this._dom.getChildren(n2);
        return dTMAxisIterator.setStartNode(n2);
    }

    public void setFilter(StripFilter stripFilter) {
        this._filter = stripFilter;
    }

    public DTMAxisIterator getTypedChildren(int n2) {
        int[] arrn = this.getReverse();
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getTypedChildren(arrn[n2]);
        }
        return this._dom.getTypedChildren(n2);
    }

    public DTMAxisIterator getNamespaceAxisIterator(int n2, int n3) {
        return this._dom.getNamespaceAxisIterator(n2, this.getNSReverse()[n3]);
    }

    public DTMAxisIterator getAxisIterator(int n2) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getAxisIterator(n2);
        }
        return this._dom.getAxisIterator(n2);
    }

    public DTMAxisIterator getTypedAxisIterator(int n2, int n3) {
        int[] arrn = this.getReverse();
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getTypedAxisIterator(n2, arrn[n3]);
        }
        return this._dom.getTypedAxisIterator(n2, n3);
    }

    public int getMultiDOMMask() {
        return this._multiDOMMask;
    }

    public void setMultiDOMMask(int n2) {
        this._multiDOMMask = n2;
    }

    public DTMAxisIterator getNthDescendant(int n2, int n3, boolean bl) {
        return this._dom.getNthDescendant(this.getReverse()[n2], n3, bl);
    }

    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator dTMAxisIterator, int n2, String string, boolean bl) {
        return this._dom.getNodeValueIterator(dTMAxisIterator, n2, string, bl);
    }

    public DTMAxisIterator orderNodes(DTMAxisIterator dTMAxisIterator, int n2) {
        return this._dom.orderNodes(dTMAxisIterator, n2);
    }

    public int getExpandedTypeID(int n2) {
        short[] arrs = this.getMapping();
        int n3 = this._enhancedDOM != null ? arrs[this._enhancedDOM.getExpandedTypeID2(n2)] : (null != arrs ? arrs[this._dom.getExpandedTypeID(n2)] : this._dom.getExpandedTypeID(n2));
        return n3;
    }

    public int getNamespaceType(int n2) {
        return this.getNSMapping()[this._dom.getNSType(n2)];
    }

    public int getNSType(int n2) {
        return this._dom.getNSType(n2);
    }

    public int getParent(int n2) {
        return this._dom.getParent(n2);
    }

    public int getAttributeNode(int n2, int n3) {
        return this._dom.getAttributeNode(this.getReverse()[n2], n3);
    }

    public String getNodeName(int n2) {
        if (n2 == -1) {
            return "";
        }
        return this._dom.getNodeName(n2);
    }

    public String getNodeNameX(int n2) {
        if (n2 == -1) {
            return "";
        }
        return this._dom.getNodeNameX(n2);
    }

    public String getNamespaceName(int n2) {
        if (n2 == -1) {
            return "";
        }
        return this._dom.getNamespaceName(n2);
    }

    public String getStringValueX(int n2) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getStringValueX(n2);
        }
        if (n2 == -1) {
            return "";
        }
        return this._dom.getStringValueX(n2);
    }

    public void copy(int n2, SerializationHandler serializationHandler) throws TransletException {
        this._dom.copy(n2, serializationHandler);
    }

    public void copy(DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler) throws TransletException {
        this._dom.copy(dTMAxisIterator, serializationHandler);
    }

    public String shallowCopy(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.shallowCopy(n2, serializationHandler);
        }
        return this._dom.shallowCopy(n2, serializationHandler);
    }

    public boolean lessThan(int n2, int n3) {
        return this._dom.lessThan(n2, n3);
    }

    public void characters(int n2, SerializationHandler serializationHandler) throws TransletException {
        if (this._enhancedDOM != null) {
            this._enhancedDOM.characters(n2, serializationHandler);
        } else {
            this._dom.characters(n2, serializationHandler);
        }
    }

    public Node makeNode(int n2) {
        return this._dom.makeNode(n2);
    }

    public Node makeNode(DTMAxisIterator dTMAxisIterator) {
        return this._dom.makeNode(dTMAxisIterator);
    }

    public NodeList makeNodeList(int n2) {
        return this._dom.makeNodeList(n2);
    }

    public NodeList makeNodeList(DTMAxisIterator dTMAxisIterator) {
        return this._dom.makeNodeList(dTMAxisIterator);
    }

    public String getLanguage(int n2) {
        return this._dom.getLanguage(n2);
    }

    public int getSize() {
        return this._dom.getSize();
    }

    public void setDocumentURI(String string) {
        if (this._enhancedDOM != null) {
            this._enhancedDOM.setDocumentURI(string);
        }
    }

    public String getDocumentURI() {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getDocumentURI();
        }
        return "";
    }

    public String getDocumentURI(int n2) {
        return this._dom.getDocumentURI(n2);
    }

    public int getDocument() {
        return this._dom.getDocument();
    }

    public boolean isElement(int n2) {
        return this._dom.isElement(n2);
    }

    public boolean isAttribute(int n2) {
        return this._dom.isAttribute(n2);
    }

    public int getNodeIdent(int n2) {
        return this._dom.getNodeIdent(n2);
    }

    public int getNodeHandle(int n2) {
        return this._dom.getNodeHandle(n2);
    }

    public DOM getResultTreeFrag(int n2, int n3) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getResultTreeFrag(n2, n3);
        }
        return this._dom.getResultTreeFrag(n2, n3);
    }

    public DOM getResultTreeFrag(int n2, int n3, boolean bl) {
        if (this._enhancedDOM != null) {
            return this._enhancedDOM.getResultTreeFrag(n2, n3, bl);
        }
        return this._dom.getResultTreeFrag(n2, n3, bl);
    }

    public SerializationHandler getOutputDomBuilder() {
        return this._dom.getOutputDomBuilder();
    }

    public String lookupNamespace(int n2, String string) throws TransletException {
        return this._dom.lookupNamespace(n2, string);
    }

    public String getUnparsedEntityURI(String string) {
        return this._dom.getUnparsedEntityURI(string);
    }

    public Hashtable getElementsWithIDs() {
        return this._dom.getElementsWithIDs();
    }
}

