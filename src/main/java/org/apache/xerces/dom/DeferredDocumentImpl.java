/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.ChildNode;
import org.apache.xerces.dom.DeferredAttrImpl;
import org.apache.xerces.dom.DeferredAttrNSImpl;
import org.apache.xerces.dom.DeferredCDATASectionImpl;
import org.apache.xerces.dom.DeferredCommentImpl;
import org.apache.xerces.dom.DeferredDOMImplementationImpl;
import org.apache.xerces.dom.DeferredDocumentTypeImpl;
import org.apache.xerces.dom.DeferredElementDefinitionImpl;
import org.apache.xerces.dom.DeferredElementImpl;
import org.apache.xerces.dom.DeferredElementNSImpl;
import org.apache.xerces.dom.DeferredEntityImpl;
import org.apache.xerces.dom.DeferredEntityReferenceImpl;
import org.apache.xerces.dom.DeferredNode;
import org.apache.xerces.dom.DeferredNotationImpl;
import org.apache.xerces.dom.DeferredProcessingInstructionImpl;
import org.apache.xerces.dom.DeferredTextImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.DocumentTypeImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.ParentNode;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DeferredDocumentImpl
extends DocumentImpl
implements DeferredNode {
    static final long serialVersionUID = 5186323580749626857L;
    private static final boolean DEBUG_PRINT_REF_COUNTS = false;
    private static final boolean DEBUG_PRINT_TABLES = false;
    private static final boolean DEBUG_IDS = false;
    protected static final int CHUNK_SHIFT = 11;
    protected static final int CHUNK_SIZE = 2048;
    protected static final int CHUNK_MASK = 2047;
    protected static final int INITIAL_CHUNK_COUNT = 32;
    protected transient int fNodeCount = 0;
    protected transient int[][] fNodeType;
    protected transient Object[][] fNodeName;
    protected transient Object[][] fNodeValue;
    protected transient int[][] fNodeParent;
    protected transient int[][] fNodeLastChild;
    protected transient int[][] fNodePrevSib;
    protected transient Object[][] fNodeURI;
    protected transient int[][] fNodeExtra;
    protected transient int fIdCount;
    protected transient String[] fIdName;
    protected transient int[] fIdElement;
    protected boolean fNamespacesEnabled = false;
    private final transient StringBuffer fBufferStr = new StringBuffer();
    private final transient ArrayList fStrChunks = new ArrayList();
    private static final int[] INIT_ARRAY = new int[2049];

    public DeferredDocumentImpl() {
        this(false);
    }

    public DeferredDocumentImpl(boolean bl) {
        this(bl, false);
    }

    public DeferredDocumentImpl(boolean bl, boolean bl2) {
        super(bl2);
        this.needsSyncData(true);
        this.needsSyncChildren(true);
        this.fNamespacesEnabled = bl;
    }

    public DOMImplementation getImplementation() {
        return DeferredDOMImplementationImpl.getDOMImplementation();
    }

    boolean getNamespacesEnabled() {
        return this.fNamespacesEnabled;
    }

    void setNamespacesEnabled(boolean bl) {
        this.fNamespacesEnabled = bl;
    }

    public int createDeferredDocument() {
        int n2 = this.createNode(9);
        return n2;
    }

    public int createDeferredDocumentType(String string, String string2, String string3) {
        int n2 = this.createNode(10);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeName, string, n3, n4);
        this.setChunkValue(this.fNodeValue, string2, n3, n4);
        this.setChunkValue(this.fNodeURI, string3, n3, n4);
        return n2;
    }

    public void setInternalSubset(int n2, String string) {
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        int n5 = this.createNode(10);
        int n6 = n5 >> 11;
        int n7 = n5 & 2047;
        this.setChunkIndex(this.fNodeExtra, n5, n3, n4);
        this.setChunkValue(this.fNodeValue, string, n6, n7);
    }

    public int createDeferredNotation(String string, String string2, String string3, String string4) {
        int n2 = this.createNode(12);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        int n5 = this.createNode(12);
        int n6 = n5 >> 11;
        int n7 = n5 & 2047;
        this.setChunkValue(this.fNodeName, string, n3, n4);
        this.setChunkValue(this.fNodeValue, string2, n3, n4);
        this.setChunkValue(this.fNodeURI, string3, n3, n4);
        this.setChunkIndex(this.fNodeExtra, n5, n3, n4);
        this.setChunkValue(this.fNodeName, string4, n6, n7);
        return n2;
    }

    public int createDeferredEntity(String string, String string2, String string3, String string4, String string5) {
        int n2 = this.createNode(6);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        int n5 = this.createNode(6);
        int n6 = n5 >> 11;
        int n7 = n5 & 2047;
        this.setChunkValue(this.fNodeName, string, n3, n4);
        this.setChunkValue(this.fNodeValue, string2, n3, n4);
        this.setChunkValue(this.fNodeURI, string3, n3, n4);
        this.setChunkIndex(this.fNodeExtra, n5, n3, n4);
        this.setChunkValue(this.fNodeName, string4, n6, n7);
        this.setChunkValue(this.fNodeValue, null, n6, n7);
        this.setChunkValue(this.fNodeURI, null, n6, n7);
        int n8 = this.createNode(6);
        int n9 = n8 >> 11;
        int n10 = n8 & 2047;
        this.setChunkIndex(this.fNodeExtra, n8, n6, n7);
        this.setChunkValue(this.fNodeName, string5, n9, n10);
        return n2;
    }

    public String getDeferredEntityBaseURI(int n2) {
        if (n2 != -1) {
            int n3 = this.getNodeExtra(n2, false);
            n3 = this.getNodeExtra(n3, false);
            return this.getNodeName(n3, false);
        }
        return null;
    }

    public void setEntityInfo(int n2, String string, String string2) {
        int n3 = this.getNodeExtra(n2, false);
        if (n3 != -1) {
            int n4 = n3 >> 11;
            int n5 = n3 & 2047;
            this.setChunkValue(this.fNodeValue, string, n4, n5);
            this.setChunkValue(this.fNodeURI, string2, n4, n5);
        }
    }

    public void setTypeInfo(int n2, Object object) {
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeValue, object, n3, n4);
    }

    public void setInputEncoding(int n2, String string) {
        int n3 = this.getNodeExtra(n2, false);
        int n4 = this.getNodeExtra(n3, false);
        int n5 = n4 >> 11;
        int n6 = n4 & 2047;
        this.setChunkValue(this.fNodeValue, string, n5, n6);
    }

    public int createDeferredEntityReference(String string, String string2) {
        int n2 = this.createNode(5);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeName, string, n3, n4);
        this.setChunkValue(this.fNodeValue, string2, n3, n4);
        return n2;
    }

    public int createDeferredElement(String string, String string2, Object object) {
        int n2 = this.createNode(1);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeName, string2, n3, n4);
        this.setChunkValue(this.fNodeURI, string, n3, n4);
        this.setChunkValue(this.fNodeValue, object, n3, n4);
        return n2;
    }

    public int createDeferredElement(String string) {
        return this.createDeferredElement(null, string);
    }

    public int createDeferredElement(String string, String string2) {
        int n2 = this.createNode(1);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeName, string2, n3, n4);
        this.setChunkValue(this.fNodeURI, string, n3, n4);
        return n2;
    }

    public int setDeferredAttribute(int n2, String string, String string2, String string3, boolean bl, boolean bl2, Object object) {
        int n3 = this.createDeferredAttribute(string, string2, string3, bl);
        int n4 = n3 >> 11;
        int n5 = n3 & 2047;
        this.setChunkIndex(this.fNodeParent, n2, n4, n5);
        int n6 = n2 >> 11;
        int n7 = n2 & 2047;
        int n8 = this.getChunkIndex(this.fNodeExtra, n6, n7);
        if (n8 != 0) {
            this.setChunkIndex(this.fNodePrevSib, n8, n4, n5);
        }
        this.setChunkIndex(this.fNodeExtra, n3, n6, n7);
        int n9 = this.getChunkIndex(this.fNodeExtra, n4, n5);
        if (bl2) {
            this.setChunkIndex(this.fNodeExtra, n9 |= 512, n4, n5);
            String string4 = this.getChunkValue(this.fNodeValue, n4, n5);
            this.putIdentifier(string4, n2);
        }
        if (object != null) {
            int n10 = this.createNode(20);
            int n11 = n10 >> 11;
            int n12 = n10 & 2047;
            this.setChunkIndex(this.fNodeLastChild, n10, n4, n5);
            this.setChunkValue(this.fNodeValue, object, n11, n12);
        }
        return n3;
    }

    public int setDeferredAttribute(int n2, String string, String string2, String string3, boolean bl) {
        int n3 = this.createDeferredAttribute(string, string2, string3, bl);
        int n4 = n3 >> 11;
        int n5 = n3 & 2047;
        this.setChunkIndex(this.fNodeParent, n2, n4, n5);
        int n6 = n2 >> 11;
        int n7 = n2 & 2047;
        int n8 = this.getChunkIndex(this.fNodeExtra, n6, n7);
        if (n8 != 0) {
            this.setChunkIndex(this.fNodePrevSib, n8, n4, n5);
        }
        this.setChunkIndex(this.fNodeExtra, n3, n6, n7);
        return n3;
    }

    public int createDeferredAttribute(String string, String string2, boolean bl) {
        return this.createDeferredAttribute(string, null, string2, bl);
    }

    public int createDeferredAttribute(String string, String string2, String string3, boolean bl) {
        int n2 = this.createNode(2);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeName, string, n3, n4);
        this.setChunkValue(this.fNodeURI, string2, n3, n4);
        this.setChunkValue(this.fNodeValue, string3, n3, n4);
        int n5 = bl ? 32 : 0;
        this.setChunkIndex(this.fNodeExtra, n5, n3, n4);
        return n2;
    }

    public int createDeferredElementDefinition(String string) {
        int n2 = this.createNode(21);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeName, string, n3, n4);
        return n2;
    }

    public int createDeferredTextNode(String string, boolean bl) {
        int n2 = this.createNode(3);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeValue, string, n3, n4);
        this.setChunkIndex(this.fNodeExtra, bl ? 1 : 0, n3, n4);
        return n2;
    }

    public int createDeferredCDATASection(String string) {
        int n2 = this.createNode(4);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeValue, string, n3, n4);
        return n2;
    }

    public int createDeferredProcessingInstruction(String string, String string2) {
        int n2 = this.createNode(7);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeName, string, n3, n4);
        this.setChunkValue(this.fNodeValue, string2, n3, n4);
        return n2;
    }

    public int createDeferredComment(String string) {
        int n2 = this.createNode(8);
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        this.setChunkValue(this.fNodeValue, string, n3, n4);
        return n2;
    }

    public int cloneNode(int n2, boolean bl) {
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        int n5 = this.fNodeType[n3][n4];
        int n6 = this.createNode((short)n5);
        int n7 = n6 >> 11;
        int n8 = n6 & 2047;
        this.setChunkValue(this.fNodeName, this.fNodeName[n3][n4], n7, n8);
        this.setChunkValue(this.fNodeValue, this.fNodeValue[n3][n4], n7, n8);
        this.setChunkValue(this.fNodeURI, this.fNodeURI[n3][n4], n7, n8);
        int n9 = this.fNodeExtra[n3][n4];
        if (n9 != -1) {
            if (n5 != 2 && n5 != 3) {
                n9 = this.cloneNode(n9, false);
            }
            this.setChunkIndex(this.fNodeExtra, n9, n7, n8);
        }
        if (bl) {
            int n10 = -1;
            int n11 = this.getLastChild(n2, false);
            while (n11 != -1) {
                int n12 = this.cloneNode(n11, bl);
                this.insertBefore(n6, n12, n10);
                n10 = n12;
                n11 = this.getRealPrevSibling(n11, false);
            }
        }
        return n6;
    }

    public void appendChild(int n2, int n3) {
        int n4 = n2 >> 11;
        int n5 = n2 & 2047;
        int n6 = n3 >> 11;
        int n7 = n3 & 2047;
        this.setChunkIndex(this.fNodeParent, n2, n6, n7);
        int n8 = this.getChunkIndex(this.fNodeLastChild, n4, n5);
        this.setChunkIndex(this.fNodePrevSib, n8, n6, n7);
        this.setChunkIndex(this.fNodeLastChild, n3, n4, n5);
    }

    public int setAttributeNode(int n2, int n3) {
        int n4 = n2 >> 11;
        int n5 = n2 & 2047;
        int n6 = n3 >> 11;
        int n7 = n3 & 2047;
        String string = this.getChunkValue(this.fNodeName, n6, n7);
        int n8 = this.getChunkIndex(this.fNodeExtra, n4, n5);
        int n9 = -1;
        int n10 = -1;
        int n11 = -1;
        while (n8 != -1) {
            n10 = n8 >> 11;
            n11 = n8 & 2047;
            String string2 = this.getChunkValue(this.fNodeName, n10, n11);
            if (string2.equals(string)) break;
            n9 = n8;
            n8 = this.getChunkIndex(this.fNodePrevSib, n10, n11);
        }
        if (n8 != -1) {
            int n12;
            int n13;
            int n14 = this.getChunkIndex(this.fNodePrevSib, n10, n11);
            if (n9 == -1) {
                this.setChunkIndex(this.fNodeExtra, n14, n4, n5);
            } else {
                n13 = n9 >> 11;
                n12 = n9 & 2047;
                this.setChunkIndex(this.fNodePrevSib, n14, n13, n12);
            }
            this.clearChunkIndex(this.fNodeType, n10, n11);
            this.clearChunkValue(this.fNodeName, n10, n11);
            this.clearChunkValue(this.fNodeValue, n10, n11);
            this.clearChunkIndex(this.fNodeParent, n10, n11);
            this.clearChunkIndex(this.fNodePrevSib, n10, n11);
            n13 = this.clearChunkIndex(this.fNodeLastChild, n10, n11);
            n12 = n13 >> 11;
            int n15 = n13 & 2047;
            this.clearChunkIndex(this.fNodeType, n12, n15);
            this.clearChunkValue(this.fNodeValue, n12, n15);
            this.clearChunkIndex(this.fNodeParent, n12, n15);
            this.clearChunkIndex(this.fNodeLastChild, n12, n15);
        }
        int n16 = this.getChunkIndex(this.fNodeExtra, n4, n5);
        this.setChunkIndex(this.fNodeExtra, n3, n4, n5);
        this.setChunkIndex(this.fNodePrevSib, n16, n6, n7);
        return n8;
    }

    public void setIdAttributeNode(int n2, int n3) {
        int n4 = n3 >> 11;
        int n5 = n3 & 2047;
        int n6 = this.getChunkIndex(this.fNodeExtra, n4, n5);
        this.setChunkIndex(this.fNodeExtra, n6 |= 512, n4, n5);
        String string = this.getChunkValue(this.fNodeValue, n4, n5);
        this.putIdentifier(string, n2);
    }

    public void setIdAttribute(int n2) {
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        int n5 = this.getChunkIndex(this.fNodeExtra, n3, n4);
        this.setChunkIndex(this.fNodeExtra, n5 |= 512, n3, n4);
    }

    public int insertBefore(int n2, int n3, int n4) {
        if (n4 == -1) {
            this.appendChild(n2, n3);
            return n3;
        }
        int n5 = n3 >> 11;
        int n6 = n3 & 2047;
        int n7 = n4 >> 11;
        int n8 = n4 & 2047;
        int n9 = this.getChunkIndex(this.fNodePrevSib, n7, n8);
        this.setChunkIndex(this.fNodePrevSib, n3, n7, n8);
        this.setChunkIndex(this.fNodePrevSib, n9, n5, n6);
        return n3;
    }

    public void setAsLastChild(int n2, int n3) {
        int n4 = n2 >> 11;
        int n5 = n2 & 2047;
        this.setChunkIndex(this.fNodeLastChild, n3, n4, n5);
    }

    public int getParentNode(int n2) {
        return this.getParentNode(n2, false);
    }

    public int getParentNode(int n2, boolean bl) {
        if (n2 == -1) {
            return -1;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        return bl ? this.clearChunkIndex(this.fNodeParent, n3, n4) : this.getChunkIndex(this.fNodeParent, n3, n4);
    }

    public int getLastChild(int n2) {
        return this.getLastChild(n2, true);
    }

    public int getLastChild(int n2, boolean bl) {
        if (n2 == -1) {
            return -1;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        return bl ? this.clearChunkIndex(this.fNodeLastChild, n3, n4) : this.getChunkIndex(this.fNodeLastChild, n3, n4);
    }

    public int getPrevSibling(int n2) {
        return this.getPrevSibling(n2, true);
    }

    public int getPrevSibling(int n2, boolean bl) {
        if (n2 == -1) {
            return -1;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        int n5 = this.getChunkIndex(this.fNodeType, n3, n4);
        if (n5 == 3) {
            while ((n2 = this.getChunkIndex(this.fNodePrevSib, n3, n4)) != -1 && (n5 = this.getChunkIndex(this.fNodeType, n3 = n2 >> 11, n4 = n2 & 2047)) == 3) {
            }
        } else {
            n2 = this.getChunkIndex(this.fNodePrevSib, n3, n4);
        }
        return n2;
    }

    public int getRealPrevSibling(int n2) {
        return this.getRealPrevSibling(n2, true);
    }

    public int getRealPrevSibling(int n2, boolean bl) {
        if (n2 == -1) {
            return -1;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        return bl ? this.clearChunkIndex(this.fNodePrevSib, n3, n4) : this.getChunkIndex(this.fNodePrevSib, n3, n4);
    }

    public int lookupElementDefinition(String string) {
        if (this.fNodeCount > 1) {
            int n2 = -1;
            int n3 = 0;
            int n4 = 0;
            int n5 = this.getChunkIndex(this.fNodeLastChild, n3, n4);
            while (n5 != -1) {
                n3 = n5 >> 11;
                n4 = n5 & 2047;
                if (this.getChunkIndex(this.fNodeType, n3, n4) == 10) {
                    n2 = n5;
                    break;
                }
                n5 = this.getChunkIndex(this.fNodePrevSib, n3, n4);
            }
            if (n2 == -1) {
                return -1;
            }
            n3 = n2 >> 11;
            n4 = n2 & 2047;
            int n6 = this.getChunkIndex(this.fNodeLastChild, n3, n4);
            while (n6 != -1) {
                n3 = n6 >> 11;
                n4 = n6 & 2047;
                if (this.getChunkIndex(this.fNodeType, n3, n4) == 21 && this.getChunkValue(this.fNodeName, n3, n4) == string) {
                    return n6;
                }
                n6 = this.getChunkIndex(this.fNodePrevSib, n3, n4);
            }
        }
        return -1;
    }

    public DeferredNode getNodeObject(int n2) {
        if (n2 == -1) {
            return null;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        int n5 = this.getChunkIndex(this.fNodeType, n3, n4);
        if (n5 != 3 && n5 != 4) {
            this.clearChunkIndex(this.fNodeType, n3, n4);
        }
        NodeImpl nodeImpl = null;
        switch (n5) {
            case 2: {
                if (this.fNamespacesEnabled) {
                    nodeImpl = new DeferredAttrNSImpl(this, n2);
                    break;
                }
                nodeImpl = new DeferredAttrImpl(this, n2);
                break;
            }
            case 4: {
                nodeImpl = new DeferredCDATASectionImpl(this, n2);
                break;
            }
            case 8: {
                nodeImpl = new DeferredCommentImpl(this, n2);
                break;
            }
            case 9: {
                nodeImpl = this;
                break;
            }
            case 10: {
                nodeImpl = new DeferredDocumentTypeImpl(this, n2);
                this.docType = (DocumentTypeImpl)nodeImpl;
                break;
            }
            case 1: {
                nodeImpl = this.fNamespacesEnabled ? new DeferredElementNSImpl(this, n2) : new DeferredElementImpl(this, n2);
                if (this.fIdElement == null) break;
                int n6 = DeferredDocumentImpl.binarySearch(this.fIdElement, 0, this.fIdCount - 1, n2);
                while (n6 != -1) {
                    String string = this.fIdName[n6];
                    if (string != null) {
                        this.putIdentifier0(string, (Element)((Object)nodeImpl));
                        this.fIdName[n6] = null;
                    }
                    if (n6 + 1 < this.fIdCount && this.fIdElement[n6 + 1] == n2) {
                        ++n6;
                        continue;
                    }
                    n6 = -1;
                }
                break;
            }
            case 6: {
                nodeImpl = new DeferredEntityImpl(this, n2);
                break;
            }
            case 5: {
                nodeImpl = new DeferredEntityReferenceImpl(this, n2);
                break;
            }
            case 12: {
                nodeImpl = new DeferredNotationImpl(this, n2);
                break;
            }
            case 7: {
                nodeImpl = new DeferredProcessingInstructionImpl(this, n2);
                break;
            }
            case 3: {
                nodeImpl = new DeferredTextImpl(this, n2);
                break;
            }
            case 21: {
                nodeImpl = new DeferredElementDefinitionImpl(this, n2);
                break;
            }
            default: {
                throw new IllegalArgumentException("type: " + n5);
            }
        }
        if (nodeImpl != null) {
            return nodeImpl;
        }
        throw new IllegalArgumentException();
    }

    public String getNodeName(int n2) {
        return this.getNodeName(n2, true);
    }

    public String getNodeName(int n2, boolean bl) {
        if (n2 == -1) {
            return null;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        return bl ? this.clearChunkValue(this.fNodeName, n3, n4) : this.getChunkValue(this.fNodeName, n3, n4);
    }

    public String getNodeValueString(int n2) {
        return this.getNodeValueString(n2, true);
    }

    public String getNodeValueString(int n2, boolean bl) {
        int n3;
        String string;
        if (n2 == -1) {
            return null;
        }
        int n4 = n2 >> 11;
        int n5 = n2 & 2047;
        String string2 = string = bl ? this.clearChunkValue(this.fNodeValue, n4, n5) : this.getChunkValue(this.fNodeValue, n4, n5);
        if (string == null) {
            return null;
        }
        int n6 = this.getChunkIndex(this.fNodeType, n4, n5);
        if (n6 == 3) {
            int n7 = this.getRealPrevSibling(n2);
            if (n7 != -1 && this.getNodeType(n7, false) == 3) {
                this.fStrChunks.add(string);
                do {
                    n4 = n7 >> 11;
                    n5 = n7 & 2047;
                    string = this.getChunkValue(this.fNodeValue, n4, n5);
                    this.fStrChunks.add(string);
                } while ((n7 = this.getChunkIndex(this.fNodePrevSib, n4, n5)) != -1 && this.getNodeType(n7, false) == 3);
                int n8 = this.fStrChunks.size();
                int n9 = n8 - 1;
                while (n9 >= 0) {
                    this.fBufferStr.append((String)this.fStrChunks.get(n9));
                    --n9;
                }
                string = this.fBufferStr.toString();
                this.fStrChunks.clear();
                this.fBufferStr.setLength(0);
                return string;
            }
        } else if (n6 == 4 && (n3 = this.getLastChild(n2, false)) != -1) {
            this.fBufferStr.append(string);
            while (n3 != -1) {
                n4 = n3 >> 11;
                n5 = n3 & 2047;
                string = this.getChunkValue(this.fNodeValue, n4, n5);
                this.fStrChunks.add(string);
                n3 = this.getChunkIndex(this.fNodePrevSib, n4, n5);
            }
            int n10 = this.fStrChunks.size() - 1;
            while (n10 >= 0) {
                this.fBufferStr.append((String)this.fStrChunks.get(n10));
                --n10;
            }
            string = this.fBufferStr.toString();
            this.fStrChunks.clear();
            this.fBufferStr.setLength(0);
            return string;
        }
        return string;
    }

    public String getNodeValue(int n2) {
        return this.getNodeValue(n2, true);
    }

    public Object getTypeInfo(int n2) {
        Object object;
        if (n2 == -1) {
            return null;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        Object object2 = object = this.fNodeValue[n3] != null ? this.fNodeValue[n3][n4] : null;
        if (object != null) {
            this.fNodeValue[n3][n4] = null;
            RefCount refCount = (RefCount)this.fNodeValue[n3][2048];
            --refCount.fCount;
            if (refCount.fCount == 0) {
                this.fNodeValue[n3] = null;
            }
        }
        return object;
    }

    public String getNodeValue(int n2, boolean bl) {
        if (n2 == -1) {
            return null;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        return bl ? this.clearChunkValue(this.fNodeValue, n3, n4) : this.getChunkValue(this.fNodeValue, n3, n4);
    }

    public int getNodeExtra(int n2) {
        return this.getNodeExtra(n2, true);
    }

    public int getNodeExtra(int n2, boolean bl) {
        if (n2 == -1) {
            return -1;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        return bl ? this.clearChunkIndex(this.fNodeExtra, n3, n4) : this.getChunkIndex(this.fNodeExtra, n3, n4);
    }

    public short getNodeType(int n2) {
        return this.getNodeType(n2, true);
    }

    public short getNodeType(int n2, boolean bl) {
        if (n2 == -1) {
            return -1;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        return bl ? (short)this.clearChunkIndex(this.fNodeType, n3, n4) : (short)this.getChunkIndex(this.fNodeType, n3, n4);
    }

    public String getAttribute(int n2, String string) {
        if (n2 == -1 || string == null) {
            return null;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        int n5 = this.getChunkIndex(this.fNodeExtra, n3, n4);
        while (n5 != -1) {
            int n6 = n5 >> 11;
            int n7 = n5 & 2047;
            if (this.getChunkValue(this.fNodeName, n6, n7) == string) {
                return this.getChunkValue(this.fNodeValue, n6, n7);
            }
            n5 = this.getChunkIndex(this.fNodePrevSib, n6, n7);
        }
        return null;
    }

    public String getNodeURI(int n2) {
        return this.getNodeURI(n2, true);
    }

    public String getNodeURI(int n2, boolean bl) {
        if (n2 == -1) {
            return null;
        }
        int n3 = n2 >> 11;
        int n4 = n2 & 2047;
        return bl ? this.clearChunkValue(this.fNodeURI, n3, n4) : this.getChunkValue(this.fNodeURI, n3, n4);
    }

    public void putIdentifier(String string, int n2) {
        if (this.fIdName == null) {
            this.fIdName = new String[64];
            this.fIdElement = new int[64];
        }
        if (this.fIdCount == this.fIdName.length) {
            String[] arrstring = new String[this.fIdCount * 2];
            System.arraycopy(this.fIdName, 0, arrstring, 0, this.fIdCount);
            this.fIdName = arrstring;
            int[] arrn = new int[arrstring.length];
            System.arraycopy(this.fIdElement, 0, arrn, 0, this.fIdCount);
            this.fIdElement = arrn;
        }
        this.fIdName[this.fIdCount] = string;
        this.fIdElement[this.fIdCount] = n2;
        ++this.fIdCount;
    }

    public void print() {
    }

    public int getNodeIndex() {
        return 0;
    }

    protected void synchronizeData() {
        this.needsSyncData(false);
        if (this.fIdElement != null) {
            IntVector intVector = new IntVector();
            int n2 = 0;
            while (n2 < this.fIdCount) {
                int n3 = this.fIdElement[n2];
                String string = this.fIdName[n2];
                if (string != null) {
                    int n4;
                    Node node;
                    int n5;
                    intVector.removeAllElements();
                    int n6 = n3;
                    do {
                        intVector.addElement(n6);
                        n4 = n6 >> 11;
                        n5 = n6 & 2047;
                    } while ((n6 = this.getChunkIndex(this.fNodeParent, n4, n5)) != -1);
                    Node node2 = this;
                    n5 = intVector.size() - 2;
                    while (n5 >= 0) {
                        n6 = intVector.elementAt(n5);
                        node = node2.getLastChild();
                        while (node != null) {
                            int n7;
                            if (node instanceof DeferredNode && (n7 = ((DeferredNode)node).getNodeIndex()) == n6) {
                                node2 = node;
                                break;
                            }
                            node = node.getPreviousSibling();
                        }
                        --n5;
                    }
                    node = (Element)node2;
                    this.putIdentifier0(string, (Element)node);
                    this.fIdName[n2] = null;
                    while (n2 + 1 < this.fIdCount && this.fIdElement[n2 + 1] == n3) {
                        if ((string = this.fIdName[++n2]) == null) continue;
                        this.putIdentifier0(string, (Element)node);
                    }
                }
                ++n2;
            }
        }
    }

    protected void synchronizeChildren() {
        if (this.needsSyncData()) {
            this.synchronizeData();
            if (!this.needsSyncChildren()) {
                return;
            }
        }
        boolean bl = this.mutationEvents;
        this.mutationEvents = false;
        this.needsSyncChildren(false);
        this.getNodeType(0);
        NodeImpl nodeImpl = null;
        ChildNode childNode = null;
        int n2 = this.getLastChild(0);
        while (n2 != -1) {
            ChildNode childNode2 = (ChildNode)((Object)this.getNodeObject(n2));
            if (childNode == null) {
                childNode = childNode2;
            } else {
                nodeImpl.previousSibling = childNode2;
            }
            childNode2.ownerNode = this;
            childNode2.isOwned(true);
            childNode2.nextSibling = nodeImpl;
            nodeImpl = childNode2;
            short s2 = childNode2.getNodeType();
            if (s2 == 1) {
                this.docElement = (ElementImpl)childNode2;
            } else if (s2 == 10) {
                this.docType = (DocumentTypeImpl)childNode2;
            }
            n2 = this.getPrevSibling(n2);
        }
        if (nodeImpl != null) {
            this.firstChild = nodeImpl;
            nodeImpl.isFirstChild(true);
            this.lastChild(childNode);
        }
        this.mutationEvents = bl;
    }

    protected final void synchronizeChildren(AttrImpl attrImpl, int n2) {
        boolean bl = this.getMutationEvents();
        this.setMutationEvents(false);
        attrImpl.needsSyncChildren(false);
        int n3 = this.getLastChild(n2);
        int n4 = this.getPrevSibling(n3);
        if (n4 == -1) {
            attrImpl.value = this.getNodeValueString(n2);
            attrImpl.hasStringValue(true);
        } else {
            NodeImpl nodeImpl = null;
            ChildNode childNode = null;
            int n5 = n3;
            while (n5 != -1) {
                ChildNode childNode2 = (ChildNode)((Object)this.getNodeObject(n5));
                if (childNode == null) {
                    childNode = childNode2;
                } else {
                    nodeImpl.previousSibling = childNode2;
                }
                childNode2.ownerNode = attrImpl;
                childNode2.isOwned(true);
                childNode2.nextSibling = nodeImpl;
                nodeImpl = childNode2;
                n5 = this.getPrevSibling(n5);
            }
            if (childNode != null) {
                attrImpl.value = nodeImpl;
                nodeImpl.isFirstChild(true);
                attrImpl.lastChild(childNode);
            }
            attrImpl.hasStringValue(false);
        }
        this.setMutationEvents(bl);
    }

    protected final void synchronizeChildren(ParentNode parentNode, int n2) {
        boolean bl = this.getMutationEvents();
        this.setMutationEvents(false);
        parentNode.needsSyncChildren(false);
        NodeImpl nodeImpl = null;
        ChildNode childNode = null;
        int n3 = this.getLastChild(n2);
        while (n3 != -1) {
            ChildNode childNode2 = (ChildNode)((Object)this.getNodeObject(n3));
            if (childNode == null) {
                childNode = childNode2;
            } else {
                nodeImpl.previousSibling = childNode2;
            }
            childNode2.ownerNode = parentNode;
            childNode2.isOwned(true);
            childNode2.nextSibling = nodeImpl;
            nodeImpl = childNode2;
            n3 = this.getPrevSibling(n3);
        }
        if (childNode != null) {
            parentNode.firstChild = nodeImpl;
            nodeImpl.isFirstChild(true);
            parentNode.lastChild(childNode);
        }
        this.setMutationEvents(bl);
    }

    protected void ensureCapacity(int n2) {
        if (this.fNodeType == null) {
            this.fNodeType = new int[32][];
            this.fNodeName = new Object[32][];
            this.fNodeValue = new Object[32][];
            this.fNodeParent = new int[32][];
            this.fNodeLastChild = new int[32][];
            this.fNodePrevSib = new int[32][];
            this.fNodeURI = new Object[32][];
            this.fNodeExtra = new int[32][];
        } else if (this.fNodeType.length <= n2) {
            int n3 = n2 * 2;
            int[][] arrarrn = new int[n3][];
            System.arraycopy(this.fNodeType, 0, arrarrn, 0, n2);
            this.fNodeType = arrarrn;
            Object[][] arrarrobject = new Object[n3][];
            System.arraycopy(this.fNodeName, 0, arrarrobject, 0, n2);
            this.fNodeName = arrarrobject;
            arrarrobject = new Object[n3][];
            System.arraycopy(this.fNodeValue, 0, arrarrobject, 0, n2);
            this.fNodeValue = arrarrobject;
            arrarrn = new int[n3][];
            System.arraycopy(this.fNodeParent, 0, arrarrn, 0, n2);
            this.fNodeParent = arrarrn;
            arrarrn = new int[n3][];
            System.arraycopy(this.fNodeLastChild, 0, arrarrn, 0, n2);
            this.fNodeLastChild = arrarrn;
            arrarrn = new int[n3][];
            System.arraycopy(this.fNodePrevSib, 0, arrarrn, 0, n2);
            this.fNodePrevSib = arrarrn;
            arrarrobject = new Object[n3][];
            System.arraycopy(this.fNodeURI, 0, arrarrobject, 0, n2);
            this.fNodeURI = arrarrobject;
            arrarrn = new int[n3][];
            System.arraycopy(this.fNodeExtra, 0, arrarrn, 0, n2);
            this.fNodeExtra = arrarrn;
        } else if (this.fNodeType[n2] != null) {
            return;
        }
        this.createChunk(this.fNodeType, n2);
        this.createChunk(this.fNodeName, n2);
        this.createChunk(this.fNodeValue, n2);
        this.createChunk(this.fNodeParent, n2);
        this.createChunk(this.fNodeLastChild, n2);
        this.createChunk(this.fNodePrevSib, n2);
        this.createChunk(this.fNodeURI, n2);
        this.createChunk(this.fNodeExtra, n2);
    }

    protected int createNode(short s2) {
        int n2 = this.fNodeCount >> 11;
        int n3 = this.fNodeCount & 2047;
        this.ensureCapacity(n2);
        this.setChunkIndex(this.fNodeType, s2, n2, n3);
        return this.fNodeCount++;
    }

    protected static int binarySearch(int[] arrn, int n2, int n3, int n4) {
        while (n2 <= n3) {
            int n5 = (n2 + n3) / 2;
            int n6 = arrn[n5];
            if (n6 == n4) {
                while (n5 > 0 && arrn[n5 - 1] == n4) {
                    --n5;
                }
                return n5;
            }
            if (n6 > n4) {
                n3 = n5 - 1;
                continue;
            }
            n2 = n5 + 1;
        }
        return -1;
    }

    private final void createChunk(int[][] arrn, int n2) {
        arrn[n2] = new int[2049];
        System.arraycopy(INIT_ARRAY, 0, arrn[n2], 0, 2048);
    }

    private final void createChunk(Object[][] arrobject, int n2) {
        arrobject[n2] = new Object[2049];
        arrobject[n2][2048] = new RefCount();
    }

    private final int setChunkIndex(int[][] arrn, int n2, int n3, int n4) {
        int n5;
        if (n2 == -1) {
            return this.clearChunkIndex(arrn, n3, n4);
        }
        int[] arrn2 = arrn[n3];
        if (arrn2 == null) {
            this.createChunk(arrn, n3);
            arrn2 = arrn[n3];
        }
        if ((n5 = arrn2[n4]) == -1) {
            int[] arrn3 = arrn2;
            arrn3[2048] = arrn3[2048] + 1;
        }
        arrn2[n4] = n2;
        return n5;
    }

    private final String setChunkValue(Object[][] arrobject, Object object, int n2, int n3) {
        String string;
        if (object == null) {
            return this.clearChunkValue(arrobject, n2, n3);
        }
        Object[] arrobject2 = arrobject[n2];
        if (arrobject2 == null) {
            this.createChunk(arrobject, n2);
            arrobject2 = arrobject[n2];
        }
        if ((string = (String)arrobject2[n3]) == null) {
            RefCount refCount = (RefCount)arrobject2[2048];
            ++refCount.fCount;
        }
        arrobject2[n3] = object;
        return string;
    }

    private final int getChunkIndex(int[][] arrn, int n2, int n3) {
        return arrn[n2] != null ? arrn[n2][n3] : -1;
    }

    private final String getChunkValue(Object[][] arrobject, int n2, int n3) {
        return arrobject[n2] != null ? (String)arrobject[n2][n3] : null;
    }

    private final String getNodeValue(int n2, int n3) {
        Object object = this.fNodeValue[n2][n3];
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String)object;
        }
        return object.toString();
    }

    private final int clearChunkIndex(int[][] arrn, int n2, int n3) {
        int n4;
        int n5 = n4 = arrn[n2] != null ? arrn[n2][n3] : -1;
        if (n4 != -1) {
            int[] arrn2 = arrn[n2];
            arrn2[2048] = arrn2[2048] - 1;
            arrn[n2][n3] = -1;
            if (arrn[n2][2048] == 0) {
                arrn[n2] = null;
            }
        }
        return n4;
    }

    private final String clearChunkValue(Object[][] arrobject, int n2, int n3) {
        String string;
        String string2 = string = arrobject[n2] != null ? (String)arrobject[n2][n3] : null;
        if (string != null) {
            arrobject[n2][n3] = null;
            RefCount refCount = (RefCount)arrobject[n2][2048];
            --refCount.fCount;
            if (refCount.fCount == 0) {
                arrobject[n2] = null;
            }
        }
        return string;
    }

    private final void putIdentifier0(String string, Element element) {
        if (this.identifiers == null) {
            this.identifiers = new Hashtable();
        }
        this.identifiers.put(string, element);
    }

    private static void print(int[] arrn, int n2, int n3, int n4, int n5) {
    }

    static {
        int n2 = 0;
        while (n2 < 2048) {
            DeferredDocumentImpl.INIT_ARRAY[n2] = -1;
            ++n2;
        }
    }

    static final class IntVector {
        private int[] data;
        private int size;

        IntVector() {
        }

        public int size() {
            return this.size;
        }

        public int elementAt(int n2) {
            return this.data[n2];
        }

        public void addElement(int n2) {
            this.ensureCapacity(this.size + 1);
            this.data[this.size++] = n2;
        }

        public void removeAllElements() {
            this.size = 0;
        }

        private void ensureCapacity(int n2) {
            if (this.data == null) {
                this.data = new int[n2 + 15];
            } else if (n2 > this.data.length) {
                int[] arrn = new int[n2 + 15];
                System.arraycopy(this.data, 0, arrn, 0, this.data.length);
                this.data = arrn;
            }
        }
    }

    static final class RefCount {
        int fCount;

        RefCount() {
        }
    }

}

