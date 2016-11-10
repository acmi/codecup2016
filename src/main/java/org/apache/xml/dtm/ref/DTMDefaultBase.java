/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;
import javax.xml.transform.Source;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class DTMDefaultBase
implements DTM {
    static final boolean JJK_DEBUG = false;
    public static final int ROOTNODE = 0;
    protected int m_size = 0;
    protected SuballocatedIntVector m_exptype;
    protected SuballocatedIntVector m_firstch;
    protected SuballocatedIntVector m_nextsib;
    protected SuballocatedIntVector m_prevsib;
    protected SuballocatedIntVector m_parent;
    protected Vector m_namespaceDeclSets = null;
    protected SuballocatedIntVector m_namespaceDeclSetElements = null;
    protected int[][][] m_elemIndexes;
    public static final int DEFAULT_BLOCKSIZE = 512;
    public static final int DEFAULT_NUMBLOCKS = 32;
    public static final int DEFAULT_NUMBLOCKS_SMALL = 4;
    protected static final int NOTPROCESSED = -2;
    public DTMManager m_mgr;
    protected DTMManagerDefault m_mgrDefault = null;
    protected SuballocatedIntVector m_dtmIdent;
    protected String m_documentBaseURI;
    protected DTMWSFilter m_wsfilter;
    protected boolean m_shouldStripWS = false;
    protected BoolStack m_shouldStripWhitespaceStack;
    protected XMLStringFactory m_xstrf;
    protected ExpandedNameTable m_expandedNameTable;
    protected boolean m_indexing;
    protected DTMAxisTraverser[] m_traversers;
    private Vector m_namespaceLists = null;

    public DTMDefaultBase(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl) {
        this(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, 512, true, false);
    }

    public DTMDefaultBase(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl, int n3, boolean bl2, boolean bl3) {
        int n4;
        if (n3 <= 64) {
            n4 = 4;
            this.m_dtmIdent = new SuballocatedIntVector(4, 1);
        } else {
            n4 = 32;
            this.m_dtmIdent = new SuballocatedIntVector(32);
        }
        this.m_exptype = new SuballocatedIntVector(n3, n4);
        this.m_firstch = new SuballocatedIntVector(n3, n4);
        this.m_nextsib = new SuballocatedIntVector(n3, n4);
        this.m_parent = new SuballocatedIntVector(n3, n4);
        if (bl2) {
            this.m_prevsib = new SuballocatedIntVector(n3, n4);
        }
        this.m_mgr = dTMManager;
        if (dTMManager instanceof DTMManagerDefault) {
            this.m_mgrDefault = (DTMManagerDefault)dTMManager;
        }
        this.m_documentBaseURI = null != source ? source.getSystemId() : null;
        this.m_dtmIdent.setElementAt(n2, 0);
        this.m_wsfilter = dTMWSFilter;
        this.m_xstrf = xMLStringFactory;
        this.m_indexing = bl;
        this.m_expandedNameTable = bl ? new ExpandedNameTable() : this.m_mgrDefault.getExpandedNameTable(this);
        if (null != dTMWSFilter) {
            this.m_shouldStripWhitespaceStack = new BoolStack();
            this.pushShouldStripWhitespace(false);
        }
    }

    protected void ensureSizeOfIndex(int n2, int n3) {
        int[][][] arrn;
        int[][][] arrn2;
        if (null == this.m_elemIndexes) {
            this.m_elemIndexes = new int[n2 + 20][][];
        } else if (this.m_elemIndexes.length <= n2) {
            arrn = this.m_elemIndexes;
            this.m_elemIndexes = new int[n2 + 20][][];
            System.arraycopy(arrn, 0, this.m_elemIndexes, 0, arrn.length);
        }
        arrn = this.m_elemIndexes[n2];
        if (null == arrn) {
            arrn = new int[n3 + 100][];
            this.m_elemIndexes[n2] = arrn;
        } else if (arrn.length <= n3) {
            arrn2 = arrn;
            arrn = new int[n3 + 100][];
            System.arraycopy(arrn2, 0, arrn, 0, arrn2.length);
            this.m_elemIndexes[n2] = arrn;
        }
        arrn2 = arrn[n3];
        if (null == arrn2) {
            arrn2 = new int[128];
            arrn[n3] = arrn2;
            arrn2[0] = true;
        } else if (arrn2.length <= arrn2[0] + true) {
            int[][][] arrn3 = arrn2;
            arrn2 = new int[arrn2[0] + 1024];
            System.arraycopy(arrn3, 0, arrn2, 0, arrn3.length);
            arrn[n3] = arrn2;
        }
    }

    protected void indexNode(int n2, int n3) {
        ExpandedNameTable expandedNameTable = this.m_expandedNameTable;
        short s2 = expandedNameTable.getType(n2);
        if (1 == s2) {
            int n4 = expandedNameTable.getNamespaceID(n2);
            int n5 = expandedNameTable.getLocalNameID(n2);
            this.ensureSizeOfIndex(n4, n5);
            int[] arrn = this.m_elemIndexes[n4][n5];
            arrn[arrn[0]] = n3;
            int[] arrn2 = arrn;
            arrn2[0] = arrn2[0] + 1;
        }
    }

    protected int findGTE(int[] arrn, int n2, int n3, int n4) {
        int n5;
        int n6 = n2;
        int n7 = n5 = n2 + (n3 - 1);
        while (n6 <= n5) {
            int n8 = n6 + n5 >>> 1;
            int n9 = arrn[n8];
            if (n9 > n4) {
                n5 = n8 - 1;
                continue;
            }
            if (n9 < n4) {
                n6 = n8 + 1;
                continue;
            }
            return n8;
        }
        return n6 <= n7 && arrn[n6] > n4 ? n6 : -1;
    }

    int findElementFromIndex(int n2, int n3, int n4) {
        int[] arrn;
        int n5;
        int[][] arrn2;
        int[][][] arrn3 = this.m_elemIndexes;
        if (null != arrn3 && n2 < arrn3.length && null != (arrn2 = arrn3[n2]) && n3 < arrn2.length && null != (arrn = arrn2[n3]) && (n5 = this.findGTE(arrn, 1, arrn[0], n4)) > -1) {
            return arrn[n5];
        }
        return -2;
    }

    protected abstract int getNextNodeIdentity(int var1);

    protected abstract boolean nextNode();

    protected abstract int getNumberOfNodes();

    protected short _type(int n2) {
        int n3 = this._exptype(n2);
        if (-1 != n3) {
            return this.m_expandedNameTable.getType(n3);
        }
        return -1;
    }

    protected int _exptype(int n2) {
        if (n2 == -1) {
            return -1;
        }
        while (n2 >= this.m_size) {
            if (this.nextNode() || n2 < this.m_size) continue;
            return -1;
        }
        return this.m_exptype.elementAt(n2);
    }

    protected int _level(int n2) {
        int n3;
        while (n2 >= this.m_size) {
            n3 = this.nextNode();
            if (n3 != 0 || n2 < this.m_size) continue;
            return -1;
        }
        n3 = 0;
        while (-1 != (n2 = this._parent(n2))) {
            ++n3;
        }
        return n3;
    }

    protected int _firstch(int n2) {
        int n3;
        int n4 = n3 = n2 >= this.m_size ? -2 : this.m_firstch.elementAt(n2);
        while (n3 == -2) {
            boolean bl = this.nextNode();
            if (n2 >= this.m_size && !bl) {
                return -1;
            }
            n3 = this.m_firstch.elementAt(n2);
            if (n3 != -2 || bl) continue;
            return -1;
        }
        return n3;
    }

    protected int _nextsib(int n2) {
        int n3;
        int n4 = n3 = n2 >= this.m_size ? -2 : this.m_nextsib.elementAt(n2);
        while (n3 == -2) {
            boolean bl = this.nextNode();
            if (n2 >= this.m_size && !bl) {
                return -1;
            }
            n3 = this.m_nextsib.elementAt(n2);
            if (n3 != -2 || bl) continue;
            return -1;
        }
        return n3;
    }

    protected int _prevsib(int n2) {
        if (n2 < this.m_size) {
            return this.m_prevsib.elementAt(n2);
        }
        do {
            boolean bl = this.nextNode();
            if (n2 < this.m_size || bl) continue;
            return -1;
        } while (n2 >= this.m_size);
        return this.m_prevsib.elementAt(n2);
    }

    protected int _parent(int n2) {
        if (n2 < this.m_size) {
            return this.m_parent.elementAt(n2);
        }
        do {
            boolean bl = this.nextNode();
            if (n2 < this.m_size || bl) continue;
            return -1;
        } while (n2 >= this.m_size);
        return this.m_parent.elementAt(n2);
    }

    public void dumpDTM(OutputStream outputStream) {
        try {
            Object object;
            if (outputStream == null) {
                object = new File("DTMDump" + this.hashCode() + ".txt");
                System.err.println("Dumping... " + object.getAbsolutePath());
                outputStream = new FileOutputStream((File)object);
            }
            object = new PrintStream(outputStream);
            while (this.nextNode()) {
            }
            int n2 = this.m_size;
            object.println("Total nodes: " + n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                int n3;
                String string;
                int n4 = this.makeNodeHandle(i2);
                object.println("=========== index=" + i2 + " handle=" + n4 + " ===========");
                object.println("NodeName: " + this.getNodeName(n4));
                object.println("NodeNameX: " + this.getNodeNameX(n4));
                object.println("LocalName: " + this.getLocalName(n4));
                object.println("NamespaceURI: " + this.getNamespaceURI(n4));
                object.println("Prefix: " + this.getPrefix(n4));
                int n5 = this._exptype(i2);
                object.println("Expanded Type ID: " + Integer.toHexString(n5));
                short s2 = this._type(i2);
                switch (s2) {
                    case 2: {
                        string = "ATTRIBUTE_NODE";
                        break;
                    }
                    case 4: {
                        string = "CDATA_SECTION_NODE";
                        break;
                    }
                    case 8: {
                        string = "COMMENT_NODE";
                        break;
                    }
                    case 11: {
                        string = "DOCUMENT_FRAGMENT_NODE";
                        break;
                    }
                    case 9: {
                        string = "DOCUMENT_NODE";
                        break;
                    }
                    case 10: {
                        string = "DOCUMENT_NODE";
                        break;
                    }
                    case 1: {
                        string = "ELEMENT_NODE";
                        break;
                    }
                    case 6: {
                        string = "ENTITY_NODE";
                        break;
                    }
                    case 5: {
                        string = "ENTITY_REFERENCE_NODE";
                        break;
                    }
                    case 13: {
                        string = "NAMESPACE_NODE";
                        break;
                    }
                    case 12: {
                        string = "NOTATION_NODE";
                        break;
                    }
                    case -1: {
                        string = "NULL";
                        break;
                    }
                    case 7: {
                        string = "PROCESSING_INSTRUCTION_NODE";
                        break;
                    }
                    case 3: {
                        string = "TEXT_NODE";
                        break;
                    }
                    default: {
                        string = "Unknown!";
                    }
                }
                object.println("Type: " + string);
                int n6 = this._firstch(i2);
                if (-1 == n6) {
                    object.println("First child: DTM.NULL");
                } else if (-2 == n6) {
                    object.println("First child: NOTPROCESSED");
                } else {
                    object.println("First child: " + n6);
                }
                if (this.m_prevsib != null) {
                    n3 = this._prevsib(i2);
                    if (-1 == n3) {
                        object.println("Prev sibling: DTM.NULL");
                    } else if (-2 == n3) {
                        object.println("Prev sibling: NOTPROCESSED");
                    } else {
                        object.println("Prev sibling: " + n3);
                    }
                }
                if (-1 == (n3 = this._nextsib(i2))) {
                    object.println("Next sibling: DTM.NULL");
                } else if (-2 == n3) {
                    object.println("Next sibling: NOTPROCESSED");
                } else {
                    object.println("Next sibling: " + n3);
                }
                int n7 = this._parent(i2);
                if (-1 == n7) {
                    object.println("Parent: DTM.NULL");
                } else if (-2 == n7) {
                    object.println("Parent: NOTPROCESSED");
                } else {
                    object.println("Parent: " + n7);
                }
                int n8 = this._level(i2);
                object.println("Level: " + n8);
                object.println("Node Value: " + this.getNodeValue(n4));
                object.println("String Value: " + this.getStringValue(n4));
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace(System.err);
            throw new RuntimeException(iOException.getMessage());
        }
    }

    public String dumpNode(int n2) {
        String string;
        if (n2 == -1) {
            return "[null]";
        }
        switch (this.getNodeType(n2)) {
            case 2: {
                string = "ATTR";
                break;
            }
            case 4: {
                string = "CDATA";
                break;
            }
            case 8: {
                string = "COMMENT";
                break;
            }
            case 11: {
                string = "DOC_FRAG";
                break;
            }
            case 9: {
                string = "DOC";
                break;
            }
            case 10: {
                string = "DOC_TYPE";
                break;
            }
            case 1: {
                string = "ELEMENT";
                break;
            }
            case 6: {
                string = "ENTITY";
                break;
            }
            case 5: {
                string = "ENT_REF";
                break;
            }
            case 13: {
                string = "NAMESPACE";
                break;
            }
            case 12: {
                string = "NOTATION";
                break;
            }
            case -1: {
                string = "null";
                break;
            }
            case 7: {
                string = "PI";
                break;
            }
            case 3: {
                string = "TEXT";
                break;
            }
            default: {
                string = "Unknown!";
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[" + n2 + ": " + string + "(0x" + Integer.toHexString(this.getExpandedTypeID(n2)) + ") " + this.getNodeNameX(n2) + " {" + this.getNamespaceURI(n2) + "}" + "=\"" + this.getNodeValue(n2) + "\"]");
        return stringBuffer.toString();
    }

    public void setFeature(String string, boolean bl) {
    }

    public boolean hasChildNodes(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._firstch(n3);
        return n4 != -1;
    }

    public final int makeNodeHandle(int n2) {
        if (-1 == n2) {
            return -1;
        }
        return this.m_dtmIdent.elementAt(n2 >>> 16) + (n2 & 65535);
    }

    public final int makeNodeIdentity(int n2) {
        if (-1 == n2) {
            return -1;
        }
        if (this.m_mgrDefault != null) {
            int n3 = n2 >>> 16;
            if (this.m_mgrDefault.m_dtms[n3] != this) {
                return -1;
            }
            return this.m_mgrDefault.m_dtm_offsets[n3] | n2 & 65535;
        }
        int n4 = this.m_dtmIdent.indexOf(n2 & -65536);
        return n4 == -1 ? -1 : (n4 << 16) + (n2 & 65535);
    }

    public int getFirstChild(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._firstch(n3);
        return this.makeNodeHandle(n4);
    }

    public int getTypedFirstChild(int n2, int n3) {
        if (n3 < 14) {
            int n4 = this._firstch(this.makeNodeIdentity(n2));
            while (n4 != -1) {
                int n5 = this._exptype(n4);
                if (n5 == n3 || n5 >= 14 && this.m_expandedNameTable.getType(n5) == n3) {
                    return this.makeNodeHandle(n4);
                }
                n4 = this._nextsib(n4);
            }
        } else {
            int n6 = this._firstch(this.makeNodeIdentity(n2));
            while (n6 != -1) {
                if (this._exptype(n6) == n3) {
                    return this.makeNodeHandle(n6);
                }
                n6 = this._nextsib(n6);
            }
        }
        return -1;
    }

    public int getLastChild(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._firstch(n3);
        int n5 = -1;
        while (n4 != -1) {
            n5 = n4;
            n4 = this._nextsib(n4);
        }
        return this.makeNodeHandle(n5);
    }

    public abstract int getAttributeNode(int var1, String var2, String var3);

    public int getFirstAttribute(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        return this.makeNodeHandle(this.getFirstAttributeIdentity(n3));
    }

    protected int getFirstAttributeIdentity(int n2) {
        short s2 = this._type(n2);
        if (1 == s2) {
            while (-1 != (n2 = this.getNextNodeIdentity(n2))) {
                s2 = this._type(n2);
                if (s2 == 2) {
                    return n2;
                }
                if (13 == s2) continue;
            }
        }
        return -1;
    }

    protected int getTypedAttribute(int n2, int n3) {
        short s2 = this.getNodeType(n2);
        if (1 == s2) {
            int n4 = this.makeNodeIdentity(n2);
            while (-1 != (n4 = this.getNextNodeIdentity(n4))) {
                s2 = this._type(n4);
                if (s2 == 2) {
                    if (this._exptype(n4) != n3) continue;
                    return this.makeNodeHandle(n4);
                }
                if (13 == s2) continue;
            }
        }
        return -1;
    }

    public int getNextSibling(int n2) {
        if (n2 == -1) {
            return -1;
        }
        return this.makeNodeHandle(this._nextsib(this.makeNodeIdentity(n2)));
    }

    public int getTypedNextSibling(int n2, int n3) {
        int n4;
        if (n2 == -1) {
            return -1;
        }
        int n5 = this.makeNodeIdentity(n2);
        while ((n5 = this._nextsib(n5)) != -1 && (n4 = this._exptype(n5)) != n3 && this.m_expandedNameTable.getType(n4) != n3) {
        }
        return n5 == -1 ? -1 : this.makeNodeHandle(n5);
    }

    public int getPreviousSibling(int n2) {
        if (n2 == -1) {
            return -1;
        }
        if (this.m_prevsib != null) {
            return this.makeNodeHandle(this._prevsib(this.makeNodeIdentity(n2)));
        }
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._parent(n3);
        int n5 = this._firstch(n4);
        int n6 = -1;
        while (n5 != n3) {
            n6 = n5;
            n5 = this._nextsib(n5);
        }
        return this.makeNodeHandle(n6);
    }

    public int getNextAttribute(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (this._type(n3) == 2) {
            return this.makeNodeHandle(this.getNextAttributeIdentity(n3));
        }
        return -1;
    }

    protected int getNextAttributeIdentity(int n2) {
        while (-1 != (n2 = this.getNextNodeIdentity(n2))) {
            short s2 = this._type(n2);
            if (s2 == 2) {
                return n2;
            }
            if (s2 == 13) continue;
            break;
        }
        return -1;
    }

    protected void declareNamespaceInContext(int n2, int n3) {
        int n4;
        int n5;
        SuballocatedIntVector suballocatedIntVector = null;
        if (this.m_namespaceDeclSets == null) {
            this.m_namespaceDeclSetElements = new SuballocatedIntVector(32);
            this.m_namespaceDeclSetElements.addElement(n2);
            this.m_namespaceDeclSets = new Vector<E>();
            suballocatedIntVector = new SuballocatedIntVector(32);
            this.m_namespaceDeclSets.addElement(suballocatedIntVector);
        } else {
            n4 = this.m_namespaceDeclSetElements.size() - 1;
            if (n4 >= 0 && n2 == this.m_namespaceDeclSetElements.elementAt(n4)) {
                suballocatedIntVector = (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(n4);
            }
        }
        if (suballocatedIntVector == null) {
            this.m_namespaceDeclSetElements.addElement(n2);
            SuballocatedIntVector suballocatedIntVector2 = this.findNamespaceContext(this._parent(n2));
            if (suballocatedIntVector2 != null) {
                n5 = suballocatedIntVector2.size();
                suballocatedIntVector = new SuballocatedIntVector(Math.max(Math.min(n5 + 16, 2048), 32));
                for (int i2 = 0; i2 < n5; ++i2) {
                    suballocatedIntVector.addElement(suballocatedIntVector2.elementAt(i2));
                }
            } else {
                suballocatedIntVector = new SuballocatedIntVector(32);
            }
            this.m_namespaceDeclSets.addElement(suballocatedIntVector);
        }
        n4 = this._exptype(n3);
        for (n5 = suballocatedIntVector.size() - 1; n5 >= 0; --n5) {
            if (n4 != this.getExpandedTypeID(suballocatedIntVector.elementAt(n5))) continue;
            suballocatedIntVector.setElementAt(this.makeNodeHandle(n3), n5);
            return;
        }
        suballocatedIntVector.addElement(this.makeNodeHandle(n3));
    }

    protected SuballocatedIntVector findNamespaceContext(int n2) {
        if (null != this.m_namespaceDeclSetElements) {
            int n3 = this.findInSortedSuballocatedIntVector(this.m_namespaceDeclSetElements, n2);
            if (n3 >= 0) {
                return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(n3);
            }
            if (n3 == -1) {
                return null;
            }
            n3 = -1 - n3;
            int n4 = this.m_namespaceDeclSetElements.elementAt(--n3);
            int n5 = this._parent(n2);
            if (n3 == 0 && n4 < n5) {
                int n6;
                int n7 = this.getDocumentRoot(this.makeNodeHandle(n2));
                int n8 = this.makeNodeIdentity(n7);
                int n9 = this.getNodeType(n7) == 9 ? ((n6 = this._firstch(n8)) != -1 ? n6 : n8) : n8;
                if (n4 == n9) {
                    return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(n3);
                }
            }
            while (n3 >= 0 && n5 > 0) {
                if (n4 == n5) {
                    return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(n3);
                }
                if (n4 < n5) {
                    while (n4 < (n5 = this._parent(n5))) {
                    }
                    continue;
                }
                if (n3 <= 0) break;
                n4 = this.m_namespaceDeclSetElements.elementAt(--n3);
            }
        }
        return null;
    }

    protected int findInSortedSuballocatedIntVector(SuballocatedIntVector suballocatedIntVector, int n2) {
        int n3 = 0;
        if (suballocatedIntVector != null) {
            int n4 = 0;
            int n5 = suballocatedIntVector.size() - 1;
            while (n4 <= n5) {
                n3 = (n4 + n5) / 2;
                int n6 = n2 - suballocatedIntVector.elementAt(n3);
                if (n6 == 0) {
                    return n3;
                }
                if (n6 < 0) {
                    n5 = n3 - 1;
                    continue;
                }
                n4 = n3 + 1;
            }
            if (n4 > n3) {
                n3 = n4;
            }
        }
        return -1 - n3;
    }

    public int getFirstNamespaceNode(int n2, boolean bl) {
        if (bl) {
            int n3 = this.makeNodeIdentity(n2);
            if (this._type(n3) == 1) {
                SuballocatedIntVector suballocatedIntVector = this.findNamespaceContext(n3);
                if (suballocatedIntVector == null || suballocatedIntVector.size() < 1) {
                    return -1;
                }
                return suballocatedIntVector.elementAt(0);
            }
            return -1;
        }
        int n4 = this.makeNodeIdentity(n2);
        if (this._type(n4) == 1) {
            while (-1 != (n4 = this.getNextNodeIdentity(n4))) {
                short s2 = this._type(n4);
                if (s2 == 13) {
                    return this.makeNodeHandle(n4);
                }
                if (2 == s2) continue;
                break;
            }
            return -1;
        }
        return -1;
    }

    public int getNextNamespaceNode(int n2, int n3, boolean bl) {
        if (bl) {
            SuballocatedIntVector suballocatedIntVector = this.findNamespaceContext(this.makeNodeIdentity(n2));
            if (suballocatedIntVector == null) {
                return -1;
            }
            int n4 = 1 + suballocatedIntVector.indexOf(n3);
            if (n4 <= 0 || n4 == suballocatedIntVector.size()) {
                return -1;
            }
            return suballocatedIntVector.elementAt(n4);
        }
        int n5 = this.makeNodeIdentity(n3);
        while (-1 != (n5 = this.getNextNodeIdentity(n5))) {
            short s2 = this._type(n5);
            if (s2 == 13) {
                return this.makeNodeHandle(n5);
            }
            if (s2 == 2) continue;
            break;
        }
        return -1;
    }

    public int getParent(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 > 0) {
            return this.makeNodeHandle(this._parent(n3));
        }
        return -1;
    }

    public int getDocument() {
        return this.m_dtmIdent.elementAt(0);
    }

    public int getOwnerDocument(int n2) {
        if (9 == this.getNodeType(n2)) {
            return -1;
        }
        return this.getDocumentRoot(n2);
    }

    public int getDocumentRoot(int n2) {
        return this.getManager().getDTM(n2).getDocument();
    }

    public abstract XMLString getStringValue(int var1);

    public int getStringValueChunkCount(int n2) {
        this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return 0;
    }

    public char[] getStringValueChunk(int n2, int n3, int[] arrn) {
        this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return null;
    }

    public int getExpandedTypeID(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 == -1) {
            return -1;
        }
        return this._exptype(n3);
    }

    public int getExpandedTypeID(String string, String string2, int n2) {
        ExpandedNameTable expandedNameTable = this.m_expandedNameTable;
        return expandedNameTable.getExpandedTypeID(string, string2, n2);
    }

    public String getLocalNameFromExpandedNameID(int n2) {
        return this.m_expandedNameTable.getLocalName(n2);
    }

    public String getNamespaceFromExpandedNameID(int n2) {
        return this.m_expandedNameTable.getNamespace(n2);
    }

    public int getNamespaceType(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._exptype(n3);
        return this.m_expandedNameTable.getNamespaceID(n4);
    }

    public abstract String getNodeName(int var1);

    public String getNodeNameX(int n2) {
        this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return null;
    }

    public abstract String getLocalName(int var1);

    public abstract String getPrefix(int var1);

    public abstract String getNamespaceURI(int var1);

    public abstract String getNodeValue(int var1);

    public short getNodeType(int n2) {
        if (n2 == -1) {
            return -1;
        }
        return this.m_expandedNameTable.getType(this._exptype(this.makeNodeIdentity(n2)));
    }

    public short getLevel(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        return (short)(this._level(n3) + 1);
    }

    public int getNodeIdent(int n2) {
        return this.makeNodeIdentity(n2);
    }

    public int getNodeHandle(int n2) {
        return this.makeNodeHandle(n2);
    }

    public boolean isSupported(String string, String string2) {
        return false;
    }

    public String getDocumentBaseURI() {
        return this.m_documentBaseURI;
    }

    public void setDocumentBaseURI(String string) {
        this.m_documentBaseURI = string;
    }

    public String getDocumentSystemIdentifier(int n2) {
        return this.m_documentBaseURI;
    }

    public String getDocumentEncoding(int n2) {
        return "UTF-8";
    }

    public String getDocumentStandalone(int n2) {
        return null;
    }

    public String getDocumentVersion(int n2) {
        return null;
    }

    public boolean getDocumentAllDeclarationsProcessed() {
        return true;
    }

    public abstract String getDocumentTypeDeclarationSystemIdentifier();

    public abstract String getDocumentTypeDeclarationPublicIdentifier();

    public abstract int getElementById(String var1);

    public abstract String getUnparsedEntityURI(String var1);

    public boolean supportsPreStripping() {
        return true;
    }

    public boolean isNodeAfter(int n2, int n3) {
        int n4 = this.makeNodeIdentity(n2);
        int n5 = this.makeNodeIdentity(n3);
        return n4 != -1 && n5 != -1 && n4 <= n5;
    }

    public boolean isCharacterElementContentWhitespace(int n2) {
        return false;
    }

    public boolean isDocumentAllDeclarationsProcessed(int n2) {
        return true;
    }

    public abstract boolean isAttributeSpecified(int var1);

    public abstract void dispatchCharactersEvents(int var1, ContentHandler var2, boolean var3) throws SAXException;

    public abstract void dispatchToEvents(int var1, ContentHandler var2) throws SAXException;

    public Node getNode(int n2) {
        return new DTMNodeProxy(this, n2);
    }

    public void appendChild(int n2, boolean bl, boolean bl2) {
        this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    }

    public void appendTextChild(String string) {
        this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    }

    protected void error(String string) {
        throw new DTMException(string);
    }

    protected boolean getShouldStripWhitespace() {
        return this.m_shouldStripWS;
    }

    protected void pushShouldStripWhitespace(boolean bl) {
        this.m_shouldStripWS = bl;
        if (null != this.m_shouldStripWhitespaceStack) {
            this.m_shouldStripWhitespaceStack.push(bl);
        }
    }

    protected void popShouldStripWhitespace() {
        if (null != this.m_shouldStripWhitespaceStack) {
            this.m_shouldStripWS = this.m_shouldStripWhitespaceStack.popAndTop();
        }
    }

    protected void setShouldStripWhitespace(boolean bl) {
        this.m_shouldStripWS = bl;
        if (null != this.m_shouldStripWhitespaceStack) {
            this.m_shouldStripWhitespaceStack.setTop(bl);
        }
    }

    public void documentRegistration() {
    }

    public void documentRelease() {
    }

    public void migrateTo(DTMManager dTMManager) {
        this.m_mgr = dTMManager;
        if (dTMManager instanceof DTMManagerDefault) {
            this.m_mgrDefault = (DTMManagerDefault)dTMManager;
        }
    }

    public DTMManager getManager() {
        return this.m_mgr;
    }

    public SuballocatedIntVector getDTMIDs() {
        if (this.m_mgr == null) {
            return null;
        }
        return this.m_dtmIdent;
    }
}

