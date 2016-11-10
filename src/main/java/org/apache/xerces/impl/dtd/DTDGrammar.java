/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.xerces.impl.dtd.XMLAttributeDecl;
import org.apache.xerces.impl.dtd.XMLContentSpec;
import org.apache.xerces.impl.dtd.XMLDTDDescription;
import org.apache.xerces.impl.dtd.XMLElementDecl;
import org.apache.xerces.impl.dtd.XMLEntityDecl;
import org.apache.xerces.impl.dtd.XMLNotationDecl;
import org.apache.xerces.impl.dtd.XMLSimpleType;
import org.apache.xerces.impl.dtd.models.CMAny;
import org.apache.xerces.impl.dtd.models.CMBinOp;
import org.apache.xerces.impl.dtd.models.CMLeaf;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMUniOp;
import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.impl.dtd.models.DFAContentModel;
import org.apache.xerces.impl.dtd.models.MixedContentModel;
import org.apache.xerces.impl.dtd.models.SimpleContentModel;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDSource;

public class DTDGrammar
implements EntityState,
XMLDTDContentModelHandler,
XMLDTDHandler,
Grammar {
    public static final int TOP_LEVEL_SCOPE = -1;
    private static final int CHUNK_SHIFT = 8;
    private static final int CHUNK_SIZE = 256;
    private static final int CHUNK_MASK = 255;
    private static final int INITIAL_CHUNK_COUNT = 4;
    private static final short LIST_FLAG = 128;
    private static final short LIST_MASK = -129;
    private static final boolean DEBUG = false;
    protected XMLDTDSource fDTDSource = null;
    protected XMLDTDContentModelSource fDTDContentModelSource = null;
    protected int fCurrentElementIndex;
    protected int fCurrentAttributeIndex;
    protected boolean fReadingExternalDTD = false;
    private SymbolTable fSymbolTable;
    protected XMLDTDDescription fGrammarDescription = null;
    private int fElementDeclCount = 0;
    private QName[][] fElementDeclName = new QName[4][];
    private short[][] fElementDeclType = new short[4][];
    private int[][] fElementDeclContentSpecIndex = new int[4][];
    private ContentModelValidator[][] fElementDeclContentModelValidator = new ContentModelValidator[4][];
    private int[][] fElementDeclFirstAttributeDeclIndex = new int[4][];
    private int[][] fElementDeclLastAttributeDeclIndex = new int[4][];
    private int fAttributeDeclCount = 0;
    private QName[][] fAttributeDeclName = new QName[4][];
    private boolean fIsImmutable = false;
    private short[][] fAttributeDeclType = new short[4][];
    private String[][][] fAttributeDeclEnumeration = new String[4][][];
    private short[][] fAttributeDeclDefaultType = new short[4][];
    private DatatypeValidator[][] fAttributeDeclDatatypeValidator = new DatatypeValidator[4][];
    private String[][] fAttributeDeclDefaultValue = new String[4][];
    private String[][] fAttributeDeclNonNormalizedDefaultValue = new String[4][];
    private int[][] fAttributeDeclNextAttributeDeclIndex = new int[4][];
    private int fContentSpecCount = 0;
    private short[][] fContentSpecType = new short[4][];
    private Object[][] fContentSpecValue = new Object[4][];
    private Object[][] fContentSpecOtherValue = new Object[4][];
    private int fEntityCount = 0;
    private String[][] fEntityName = new String[4][];
    private String[][] fEntityValue = new String[4][];
    private String[][] fEntityPublicId = new String[4][];
    private String[][] fEntitySystemId = new String[4][];
    private String[][] fEntityBaseSystemId = new String[4][];
    private String[][] fEntityNotation = new String[4][];
    private byte[][] fEntityIsPE = new byte[4][];
    private byte[][] fEntityInExternal = new byte[4][];
    private int fNotationCount = 0;
    private String[][] fNotationName = new String[4][];
    private String[][] fNotationPublicId = new String[4][];
    private String[][] fNotationSystemId = new String[4][];
    private String[][] fNotationBaseSystemId = new String[4][];
    private QNameHashtable fElementIndexMap = new QNameHashtable();
    private QNameHashtable fEntityIndexMap = new QNameHashtable();
    private QNameHashtable fNotationIndexMap = new QNameHashtable();
    private boolean fMixed;
    private final QName fQName = new QName();
    private final QName fQName2 = new QName();
    protected final XMLAttributeDecl fAttributeDecl = new XMLAttributeDecl();
    private int fLeafCount = 0;
    private int fEpsilonIndex = -1;
    private XMLElementDecl fElementDecl = new XMLElementDecl();
    private XMLEntityDecl fEntityDecl = new XMLEntityDecl();
    private XMLSimpleType fSimpleType = new XMLSimpleType();
    private XMLContentSpec fContentSpec = new XMLContentSpec();
    Hashtable fElementDeclTab = new Hashtable();
    private short[] fOpStack = null;
    private int[] fNodeIndexStack = null;
    private int[] fPrevNodeIndexStack = null;
    private int fDepth = 0;
    private boolean[] fPEntityStack = new boolean[4];
    private int fPEDepth = 0;
    private int[][] fElementDeclIsExternal = new int[4][];
    private int[][] fAttributeDeclIsExternal = new int[4][];
    int valueIndex = -1;
    int prevNodeIndex = -1;
    int nodeIndex = -1;

    public DTDGrammar(SymbolTable symbolTable, XMLDTDDescription xMLDTDDescription) {
        this.fSymbolTable = symbolTable;
        this.fGrammarDescription = xMLDTDDescription;
    }

    public XMLGrammarDescription getGrammarDescription() {
        return this.fGrammarDescription;
    }

    public boolean getElementDeclIsExternal(int n2) {
        if (n2 < 0) {
            return false;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        return this.fElementDeclIsExternal[n3][n4] != 0;
    }

    public boolean getAttributeDeclIsExternal(int n2) {
        if (n2 < 0) {
            return false;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        return this.fAttributeDeclIsExternal[n3][n4] != 0;
    }

    public int getAttributeDeclIndex(int n2, String string) {
        if (n2 == -1) {
            return -1;
        }
        int n3 = this.getFirstAttributeDeclIndex(n2);
        while (n3 != -1) {
            this.getAttributeDecl(n3, this.fAttributeDecl);
            if (this.fAttributeDecl.name.rawname == string || string.equals(this.fAttributeDecl.name.rawname)) {
                return n3;
            }
            n3 = this.getNextAttributeDeclIndex(n3);
        }
        return -1;
    }

    public void startDTD(XMLLocator xMLLocator, Augmentations augmentations) throws XNIException {
        this.fOpStack = null;
        this.fNodeIndexStack = null;
        this.fPrevNodeIndexStack = null;
    }

    public void startParameterEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        if (this.fPEDepth == this.fPEntityStack.length) {
            boolean[] arrbl = new boolean[this.fPEntityStack.length * 2];
            System.arraycopy(this.fPEntityStack, 0, arrbl, 0, this.fPEntityStack.length);
            this.fPEntityStack = arrbl;
        }
        this.fPEntityStack[this.fPEDepth] = this.fReadingExternalDTD;
        ++this.fPEDepth;
    }

    public void startExternalSubset(XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        this.fReadingExternalDTD = true;
    }

    public void endParameterEntity(String string, Augmentations augmentations) throws XNIException {
        --this.fPEDepth;
        this.fReadingExternalDTD = this.fPEntityStack[this.fPEDepth];
    }

    public void endExternalSubset(Augmentations augmentations) throws XNIException {
        this.fReadingExternalDTD = false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void elementDecl(String string, String string2, Augmentations augmentations) throws XNIException {
        XMLElementDecl xMLElementDecl = (XMLElementDecl)this.fElementDeclTab.get(string);
        if (xMLElementDecl != null) {
            if (xMLElementDecl.type != -1) return;
            this.fCurrentElementIndex = this.getElementDeclIndex(string);
        } else {
            this.fCurrentElementIndex = this.createElementDecl();
        }
        XMLElementDecl xMLElementDecl2 = new XMLElementDecl();
        this.fQName.setValues(null, string, string, null);
        xMLElementDecl2.name.setValues(this.fQName);
        xMLElementDecl2.contentModelValidator = null;
        xMLElementDecl2.scope = -1;
        if (string2.equals("EMPTY")) {
            xMLElementDecl2.type = 1;
        } else if (string2.equals("ANY")) {
            xMLElementDecl2.type = 0;
        } else if (string2.startsWith("(")) {
            xMLElementDecl2.type = string2.indexOf("#PCDATA") > 0 ? 2 : 3;
        }
        this.fElementDeclTab.put(string, xMLElementDecl2);
        this.fElementDecl = xMLElementDecl2;
        this.addContentSpecToElement(xMLElementDecl2);
        this.setElementDecl(this.fCurrentElementIndex, this.fElementDecl);
        int n2 = this.fCurrentElementIndex >> 8;
        int n3 = this.fCurrentElementIndex & 255;
        this.ensureElementDeclCapacity(n2);
        this.fElementDeclIsExternal[n2][n3] = this.fReadingExternalDTD || this.fPEDepth > 0 ? 1 : 0;
    }

    public void attributeDecl(String string, String string2, String string3, String[] arrstring, String string4, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
        if (!this.fElementDeclTab.containsKey(string)) {
            this.fCurrentElementIndex = this.createElementDecl();
            XMLElementDecl xMLElementDecl = new XMLElementDecl();
            xMLElementDecl.name.setValues(null, string, string, null);
            xMLElementDecl.scope = -1;
            this.fElementDeclTab.put(string, xMLElementDecl);
            this.setElementDecl(this.fCurrentElementIndex, xMLElementDecl);
        }
        int n2 = this.getElementDeclIndex(string);
        if (this.getAttributeDeclIndex(n2, string2) != -1) {
            return;
        }
        this.fCurrentAttributeIndex = this.createAttributeDecl();
        this.fSimpleType.clear();
        if (string4 != null) {
            if (string4.equals("#FIXED")) {
                this.fSimpleType.defaultType = 1;
            } else if (string4.equals("#IMPLIED")) {
                this.fSimpleType.defaultType = 0;
            } else if (string4.equals("#REQUIRED")) {
                this.fSimpleType.defaultType = 2;
            }
        }
        this.fSimpleType.defaultValue = xMLString != null ? xMLString.toString() : null;
        this.fSimpleType.nonNormalizedDefaultValue = xMLString2 != null ? xMLString2.toString() : null;
        this.fSimpleType.enumeration = arrstring;
        if (string3.equals("CDATA")) {
            this.fSimpleType.type = 0;
        } else if (string3.equals("ID")) {
            this.fSimpleType.type = 3;
        } else if (string3.startsWith("IDREF")) {
            this.fSimpleType.type = 4;
            if (string3.indexOf("S") > 0) {
                this.fSimpleType.list = true;
            }
        } else if (string3.equals("ENTITIES")) {
            this.fSimpleType.type = 1;
            this.fSimpleType.list = true;
        } else if (string3.equals("ENTITY")) {
            this.fSimpleType.type = 1;
        } else if (string3.equals("NMTOKENS")) {
            this.fSimpleType.type = 5;
            this.fSimpleType.list = true;
        } else if (string3.equals("NMTOKEN")) {
            this.fSimpleType.type = 5;
        } else if (string3.startsWith("NOTATION")) {
            this.fSimpleType.type = 6;
        } else if (string3.startsWith("ENUMERATION")) {
            this.fSimpleType.type = 2;
        } else {
            System.err.println("!!! unknown attribute type " + string3);
        }
        this.fQName.setValues(null, string2, string2, null);
        this.fAttributeDecl.setValues(this.fQName, this.fSimpleType, false);
        this.setAttributeDecl(n2, this.fCurrentAttributeIndex, this.fAttributeDecl);
        int n3 = this.fCurrentAttributeIndex >> 8;
        int n4 = this.fCurrentAttributeIndex & 255;
        this.ensureAttributeDeclCapacity(n3);
        this.fAttributeDeclIsExternal[n3][n4] = this.fReadingExternalDTD || this.fPEDepth > 0 ? 1 : 0;
    }

    public void internalEntityDecl(String string, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
        int n2 = this.getEntityDeclIndex(string);
        if (n2 == -1) {
            n2 = this.createEntityDecl();
            boolean bl = string.startsWith("%");
            boolean bl2 = this.fReadingExternalDTD || this.fPEDepth > 0;
            XMLEntityDecl xMLEntityDecl = new XMLEntityDecl();
            xMLEntityDecl.setValues(string, null, null, null, null, xMLString.toString(), bl, bl2);
            this.setEntityDecl(n2, xMLEntityDecl);
        }
    }

    public void externalEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        int n2 = this.getEntityDeclIndex(string);
        if (n2 == -1) {
            n2 = this.createEntityDecl();
            boolean bl = string.startsWith("%");
            boolean bl2 = this.fReadingExternalDTD || this.fPEDepth > 0;
            XMLEntityDecl xMLEntityDecl = new XMLEntityDecl();
            xMLEntityDecl.setValues(string, xMLResourceIdentifier.getPublicId(), xMLResourceIdentifier.getLiteralSystemId(), xMLResourceIdentifier.getBaseSystemId(), null, null, bl, bl2);
            this.setEntityDecl(n2, xMLEntityDecl);
        }
    }

    public void unparsedEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        XMLEntityDecl xMLEntityDecl = new XMLEntityDecl();
        boolean bl = string.startsWith("%");
        boolean bl2 = this.fReadingExternalDTD || this.fPEDepth > 0;
        xMLEntityDecl.setValues(string, xMLResourceIdentifier.getPublicId(), xMLResourceIdentifier.getLiteralSystemId(), xMLResourceIdentifier.getBaseSystemId(), string2, null, bl, bl2);
        int n2 = this.getEntityDeclIndex(string);
        if (n2 == -1) {
            n2 = this.createEntityDecl();
            this.setEntityDecl(n2, xMLEntityDecl);
        }
    }

    public void notationDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        XMLNotationDecl xMLNotationDecl = new XMLNotationDecl();
        xMLNotationDecl.setValues(string, xMLResourceIdentifier.getPublicId(), xMLResourceIdentifier.getLiteralSystemId(), xMLResourceIdentifier.getBaseSystemId());
        int n2 = this.getNotationDeclIndex(string);
        if (n2 == -1) {
            n2 = this.createNotationDecl();
            this.setNotationDecl(n2, xMLNotationDecl);
        }
    }

    public void endDTD(Augmentations augmentations) throws XNIException {
        this.fIsImmutable = true;
        if (this.fGrammarDescription.getRootName() == null) {
            int n2 = 0;
            String string = null;
            int n3 = this.fElementDeclCount;
            ArrayList<String> arrayList = new ArrayList<String>(n3);
            int n4 = 0;
            while (n4 < n3) {
                int n5 = n4 >> 8;
                n2 = n4 & 255;
                string = this.fElementDeclName[n5][n2].rawname;
                arrayList.add(string);
                ++n4;
            }
            this.fGrammarDescription.setPossibleRoots(arrayList);
        }
    }

    public void setDTDSource(XMLDTDSource xMLDTDSource) {
        this.fDTDSource = xMLDTDSource;
    }

    public XMLDTDSource getDTDSource() {
        return this.fDTDSource;
    }

    public void textDecl(String string, String string2, Augmentations augmentations) throws XNIException {
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void startAttlist(String string, Augmentations augmentations) throws XNIException {
    }

    public void endAttlist(Augmentations augmentations) throws XNIException {
    }

    public void startConditional(short s2, Augmentations augmentations) throws XNIException {
    }

    public void ignoredCharacters(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void endConditional(Augmentations augmentations) throws XNIException {
    }

    public void setDTDContentModelSource(XMLDTDContentModelSource xMLDTDContentModelSource) {
        this.fDTDContentModelSource = xMLDTDContentModelSource;
    }

    public XMLDTDContentModelSource getDTDContentModelSource() {
        return this.fDTDContentModelSource;
    }

    public void startContentModel(String string, Augmentations augmentations) throws XNIException {
        XMLElementDecl xMLElementDecl = (XMLElementDecl)this.fElementDeclTab.get(string);
        if (xMLElementDecl != null) {
            this.fElementDecl = xMLElementDecl;
        }
        this.fDepth = 0;
        this.initializeContentModelStack();
    }

    public void startGroup(Augmentations augmentations) throws XNIException {
        ++this.fDepth;
        this.initializeContentModelStack();
        this.fMixed = false;
    }

    public void pcdata(Augmentations augmentations) throws XNIException {
        this.fMixed = true;
    }

    public void element(String string, Augmentations augmentations) throws XNIException {
        this.fNodeIndexStack[this.fDepth] = this.fMixed ? (this.fNodeIndexStack[this.fDepth] == -1 ? this.addUniqueLeafNode(string) : this.addContentSpecNode(4, this.fNodeIndexStack[this.fDepth], this.addUniqueLeafNode(string))) : this.addContentSpecNode(0, string);
    }

    public void separator(short s2, Augmentations augmentations) throws XNIException {
        if (!this.fMixed) {
            if (this.fOpStack[this.fDepth] != 5 && s2 == 0) {
                if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
                    this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
                }
                this.fPrevNodeIndexStack[this.fDepth] = this.fNodeIndexStack[this.fDepth];
                this.fOpStack[this.fDepth] = 4;
            } else if (this.fOpStack[this.fDepth] != 4 && s2 == 1) {
                if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
                    this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
                }
                this.fPrevNodeIndexStack[this.fDepth] = this.fNodeIndexStack[this.fDepth];
                this.fOpStack[this.fDepth] = 5;
            }
        }
    }

    public void occurrence(short s2, Augmentations augmentations) throws XNIException {
        if (!this.fMixed) {
            if (s2 == 2) {
                this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(1, this.fNodeIndexStack[this.fDepth], -1);
            } else if (s2 == 3) {
                this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(2, this.fNodeIndexStack[this.fDepth], -1);
            } else if (s2 == 4) {
                this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(3, this.fNodeIndexStack[this.fDepth], -1);
            }
        }
    }

    public void endGroup(Augmentations augmentations) throws XNIException {
        if (!this.fMixed) {
            int n2;
            if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
                this.fNodeIndexStack[this.fDepth] = this.addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
            }
            this.fNodeIndexStack[this.fDepth] = n2 = this.fNodeIndexStack[this.fDepth--];
        }
    }

    public void any(Augmentations augmentations) throws XNIException {
    }

    public void empty(Augmentations augmentations) throws XNIException {
    }

    public void endContentModel(Augmentations augmentations) throws XNIException {
    }

    public boolean isNamespaceAware() {
        return false;
    }

    public SymbolTable getSymbolTable() {
        return this.fSymbolTable;
    }

    public int getFirstElementDeclIndex() {
        return this.fElementDeclCount >= 0 ? 0 : -1;
    }

    public int getNextElementDeclIndex(int n2) {
        return n2 < this.fElementDeclCount - 1 ? n2 + 1 : -1;
    }

    public int getElementDeclIndex(String string) {
        int n2 = this.fElementIndexMap.get(string);
        return n2;
    }

    public int getElementDeclIndex(QName qName) {
        return this.getElementDeclIndex(qName.rawname);
    }

    public short getContentSpecType(int n2) {
        if (n2 < 0 || n2 >= this.fElementDeclCount) {
            return -1;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        if (this.fElementDeclType[n3][n4] == -1) {
            return -1;
        }
        return (short)(this.fElementDeclType[n3][n4] & -129);
    }

    public boolean getElementDecl(int n2, XMLElementDecl xMLElementDecl) {
        if (n2 < 0 || n2 >= this.fElementDeclCount) {
            return false;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        xMLElementDecl.name.setValues(this.fElementDeclName[n3][n4]);
        if (this.fElementDeclType[n3][n4] == -1) {
            xMLElementDecl.type = -1;
            xMLElementDecl.simpleType.list = false;
        } else {
            xMLElementDecl.type = (short)(this.fElementDeclType[n3][n4] & -129);
            boolean bl = xMLElementDecl.simpleType.list = (this.fElementDeclType[n3][n4] & 128) != 0;
        }
        if (xMLElementDecl.type == 3 || xMLElementDecl.type == 2) {
            xMLElementDecl.contentModelValidator = this.getElementContentModelValidator(n2);
        }
        xMLElementDecl.simpleType.datatypeValidator = null;
        xMLElementDecl.simpleType.defaultType = -1;
        xMLElementDecl.simpleType.defaultValue = null;
        return true;
    }

    QName getElementDeclName(int n2) {
        if (n2 < 0 || n2 >= this.fElementDeclCount) {
            return null;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        return this.fElementDeclName[n3][n4];
    }

    public int getFirstAttributeDeclIndex(int n2) {
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        return this.fElementDeclFirstAttributeDeclIndex[n3][n4];
    }

    public int getNextAttributeDeclIndex(int n2) {
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        return this.fAttributeDeclNextAttributeDeclIndex[n3][n4];
    }

    public boolean getAttributeDecl(int n2, XMLAttributeDecl xMLAttributeDecl) {
        boolean bl;
        short s2;
        if (n2 < 0 || n2 >= this.fAttributeDeclCount) {
            return false;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        xMLAttributeDecl.name.setValues(this.fAttributeDeclName[n3][n4]);
        if (this.fAttributeDeclType[n3][n4] == -1) {
            s2 = -1;
            bl = false;
        } else {
            s2 = (short)(this.fAttributeDeclType[n3][n4] & -129);
            bl = (this.fAttributeDeclType[n3][n4] & 128) != 0;
        }
        xMLAttributeDecl.simpleType.setValues(s2, this.fAttributeDeclName[n3][n4].localpart, this.fAttributeDeclEnumeration[n3][n4], bl, this.fAttributeDeclDefaultType[n3][n4], this.fAttributeDeclDefaultValue[n3][n4], this.fAttributeDeclNonNormalizedDefaultValue[n3][n4], this.fAttributeDeclDatatypeValidator[n3][n4]);
        return true;
    }

    public boolean isCDATAAttribute(QName qName, QName qName2) {
        int n2 = this.getElementDeclIndex(qName);
        if (this.getAttributeDecl(n2, this.fAttributeDecl) && this.fAttributeDecl.simpleType.type != 0) {
            return false;
        }
        return true;
    }

    public int getEntityDeclIndex(String string) {
        if (string == null) {
            return -1;
        }
        return this.fEntityIndexMap.get(string);
    }

    public boolean getEntityDecl(int n2, XMLEntityDecl xMLEntityDecl) {
        int n3;
        int n4;
        if (n2 < 0 || n2 >= this.fEntityCount) {
            return false;
        }
        xMLEntityDecl.setValues(this.fEntityName[n4][n3], this.fEntityPublicId[n4][n3], this.fEntitySystemId[n4][n3], this.fEntityBaseSystemId[n4][n3], this.fEntityNotation[n4][n3], this.fEntityValue[n4][n3], this.fEntityIsPE[n4 = n2 >> 8][n3 = n2 & 255] != 0, this.fEntityInExternal[n4][n3] != 0);
        return true;
    }

    public int getNotationDeclIndex(String string) {
        if (string == null) {
            return -1;
        }
        return this.fNotationIndexMap.get(string);
    }

    public boolean getNotationDecl(int n2, XMLNotationDecl xMLNotationDecl) {
        if (n2 < 0 || n2 >= this.fNotationCount) {
            return false;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        xMLNotationDecl.setValues(this.fNotationName[n3][n4], this.fNotationPublicId[n3][n4], this.fNotationSystemId[n3][n4], this.fNotationBaseSystemId[n3][n4]);
        return true;
    }

    public boolean getContentSpec(int n2, XMLContentSpec xMLContentSpec) {
        if (n2 < 0 || n2 >= this.fContentSpecCount) {
            return false;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        xMLContentSpec.type = this.fContentSpecType[n3][n4];
        xMLContentSpec.value = this.fContentSpecValue[n3][n4];
        xMLContentSpec.otherValue = this.fContentSpecOtherValue[n3][n4];
        return true;
    }

    public int getContentSpecIndex(int n2) {
        if (n2 < 0 || n2 >= this.fElementDeclCount) {
            return -1;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        return this.fElementDeclContentSpecIndex[n3][n4];
    }

    public String getContentSpecAsString(int n2) {
        if (n2 < 0 || n2 >= this.fElementDeclCount) {
            return null;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        int n5 = this.fElementDeclContentSpecIndex[n3][n4];
        XMLContentSpec xMLContentSpec = new XMLContentSpec();
        if (this.getContentSpec(n5, xMLContentSpec)) {
            StringBuffer stringBuffer = new StringBuffer();
            int n6 = xMLContentSpec.type & 15;
            switch (n6) {
                case 0: {
                    stringBuffer.append('(');
                    if (xMLContentSpec.value == null && xMLContentSpec.otherValue == null) {
                        stringBuffer.append("#PCDATA");
                    } else {
                        stringBuffer.append(xMLContentSpec.value);
                    }
                    stringBuffer.append(')');
                    break;
                }
                case 1: {
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    short s2 = xMLContentSpec.type;
                    if (s2 == 0) {
                        stringBuffer.append('(');
                        stringBuffer.append(xMLContentSpec.value);
                        stringBuffer.append(')');
                    } else if (s2 == 3 || s2 == 2 || s2 == 1) {
                        stringBuffer.append('(');
                        this.appendContentSpec(xMLContentSpec, stringBuffer, true, n6);
                        stringBuffer.append(')');
                    } else {
                        this.appendContentSpec(xMLContentSpec, stringBuffer, true, n6);
                    }
                    stringBuffer.append('?');
                    break;
                }
                case 2: {
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    short s3 = xMLContentSpec.type;
                    if (s3 == 0) {
                        stringBuffer.append('(');
                        if (xMLContentSpec.value == null && xMLContentSpec.otherValue == null) {
                            stringBuffer.append("#PCDATA");
                        } else if (xMLContentSpec.otherValue != null) {
                            stringBuffer.append("##any:uri=").append(xMLContentSpec.otherValue);
                        } else if (xMLContentSpec.value == null) {
                            stringBuffer.append("##any");
                        } else {
                            this.appendContentSpec(xMLContentSpec, stringBuffer, true, n6);
                        }
                        stringBuffer.append(')');
                    } else if (s3 == 3 || s3 == 2 || s3 == 1) {
                        stringBuffer.append('(');
                        this.appendContentSpec(xMLContentSpec, stringBuffer, true, n6);
                        stringBuffer.append(')');
                    } else {
                        this.appendContentSpec(xMLContentSpec, stringBuffer, true, n6);
                    }
                    stringBuffer.append('*');
                    break;
                }
                case 3: {
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    short s4 = xMLContentSpec.type;
                    if (s4 == 0) {
                        stringBuffer.append('(');
                        if (xMLContentSpec.value == null && xMLContentSpec.otherValue == null) {
                            stringBuffer.append("#PCDATA");
                        } else if (xMLContentSpec.otherValue != null) {
                            stringBuffer.append("##any:uri=").append(xMLContentSpec.otherValue);
                        } else if (xMLContentSpec.value == null) {
                            stringBuffer.append("##any");
                        } else {
                            stringBuffer.append(xMLContentSpec.value);
                        }
                        stringBuffer.append(')');
                    } else if (s4 == 3 || s4 == 2 || s4 == 1) {
                        stringBuffer.append('(');
                        this.appendContentSpec(xMLContentSpec, stringBuffer, true, n6);
                        stringBuffer.append(')');
                    } else {
                        this.appendContentSpec(xMLContentSpec, stringBuffer, true, n6);
                    }
                    stringBuffer.append('+');
                    break;
                }
                case 4: 
                case 5: {
                    this.appendContentSpec(xMLContentSpec, stringBuffer, true, n6);
                    break;
                }
                case 6: {
                    stringBuffer.append("##any");
                    if (xMLContentSpec.otherValue == null) break;
                    stringBuffer.append(":uri=");
                    stringBuffer.append(xMLContentSpec.otherValue);
                    break;
                }
                case 7: {
                    stringBuffer.append("##other:uri=");
                    stringBuffer.append(xMLContentSpec.otherValue);
                    break;
                }
                case 8: {
                    stringBuffer.append("##local");
                    break;
                }
                default: {
                    stringBuffer.append("???");
                }
            }
            return stringBuffer.toString();
        }
        return null;
    }

    public void printElements() {
        int n2 = 0;
        XMLElementDecl xMLElementDecl = new XMLElementDecl();
        while (this.getElementDecl(n2++, xMLElementDecl)) {
            System.out.println("element decl: " + xMLElementDecl.name + ", " + xMLElementDecl.name.rawname);
        }
    }

    public void printAttributes(int n2) {
        int n3 = this.getFirstAttributeDeclIndex(n2);
        System.out.print(n2);
        System.out.print(" [");
        while (n3 != -1) {
            System.out.print(' ');
            System.out.print(n3);
            this.printAttribute(n3);
            n3 = this.getNextAttributeDeclIndex(n3);
            if (n3 == -1) continue;
            System.out.print(",");
        }
        System.out.println(" ]");
    }

    protected void addContentSpecToElement(XMLElementDecl xMLElementDecl) {
        if ((this.fDepth == 0 || this.fDepth == 1 && xMLElementDecl.type == 2) && this.fNodeIndexStack != null) {
            if (xMLElementDecl.type == 2) {
                int n2 = this.addUniqueLeafNode(null);
                this.fNodeIndexStack[0] = this.fNodeIndexStack[0] == -1 ? n2 : this.addContentSpecNode(4, n2, this.fNodeIndexStack[0]);
            }
            this.setContentSpecIndex(this.fCurrentElementIndex, this.fNodeIndexStack[this.fDepth]);
        }
    }

    protected ContentModelValidator getElementContentModelValidator(int n2) {
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        ContentModelValidator contentModelValidator = this.fElementDeclContentModelValidator[n3][n4];
        if (contentModelValidator != null) {
            return contentModelValidator;
        }
        short s2 = this.fElementDeclType[n3][n4];
        if (s2 == 4) {
            return null;
        }
        int n5 = this.fElementDeclContentSpecIndex[n3][n4];
        XMLContentSpec xMLContentSpec = new XMLContentSpec();
        this.getContentSpec(n5, xMLContentSpec);
        if (s2 == 2) {
            ChildrenList childrenList = new ChildrenList();
            this.contentSpecTree(n5, xMLContentSpec, childrenList);
            contentModelValidator = new MixedContentModel(childrenList.qname, childrenList.type, 0, childrenList.length, false);
        } else if (s2 == 3) {
            contentModelValidator = this.createChildModel(n5);
        } else {
            throw new RuntimeException("Unknown content type for a element decl in getElementContentModelValidator() in AbstractDTDGrammar class");
        }
        this.fElementDeclContentModelValidator[n3][n4] = contentModelValidator;
        return contentModelValidator;
    }

    protected int createElementDecl() {
        int n2 = this.fElementDeclCount >> 8;
        int n3 = this.fElementDeclCount & 255;
        this.ensureElementDeclCapacity(n2);
        this.fElementDeclName[n2][n3] = new QName();
        this.fElementDeclType[n2][n3] = -1;
        this.fElementDeclContentModelValidator[n2][n3] = null;
        this.fElementDeclFirstAttributeDeclIndex[n2][n3] = -1;
        this.fElementDeclLastAttributeDeclIndex[n2][n3] = -1;
        return this.fElementDeclCount++;
    }

    protected void setElementDecl(int n2, XMLElementDecl xMLElementDecl) {
        if (n2 < 0 || n2 >= this.fElementDeclCount) {
            return;
        }
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        this.fElementDeclName[n3][n4].setValues(xMLElementDecl.name);
        this.fElementDeclType[n3][n4] = xMLElementDecl.type;
        this.fElementDeclContentModelValidator[n3][n4] = xMLElementDecl.contentModelValidator;
        if (xMLElementDecl.simpleType.list) {
            short[] arrs = this.fElementDeclType[n3];
            int n5 = n4;
            arrs[n5] = (short)(arrs[n5] | 128);
        }
        this.fElementIndexMap.put(xMLElementDecl.name.rawname, n2);
    }

    protected void putElementNameMapping(QName qName, int n2, int n3) {
    }

    protected void setFirstAttributeDeclIndex(int n2, int n3) {
        if (n2 < 0 || n2 >= this.fElementDeclCount) {
            return;
        }
        int n4 = n2 >> 8;
        int n5 = n2 & 255;
        this.fElementDeclFirstAttributeDeclIndex[n4][n5] = n3;
    }

    protected void setContentSpecIndex(int n2, int n3) {
        if (n2 < 0 || n2 >= this.fElementDeclCount) {
            return;
        }
        int n4 = n2 >> 8;
        int n5 = n2 & 255;
        this.fElementDeclContentSpecIndex[n4][n5] = n3;
    }

    protected int createAttributeDecl() {
        int n2 = this.fAttributeDeclCount >> 8;
        int n3 = this.fAttributeDeclCount & 255;
        this.ensureAttributeDeclCapacity(n2);
        this.fAttributeDeclName[n2][n3] = new QName();
        this.fAttributeDeclType[n2][n3] = -1;
        this.fAttributeDeclDatatypeValidator[n2][n3] = null;
        this.fAttributeDeclEnumeration[n2][n3] = null;
        this.fAttributeDeclDefaultType[n2][n3] = 0;
        this.fAttributeDeclDefaultValue[n2][n3] = null;
        this.fAttributeDeclNonNormalizedDefaultValue[n2][n3] = null;
        this.fAttributeDeclNextAttributeDeclIndex[n2][n3] = -1;
        return this.fAttributeDeclCount++;
    }

    protected void setAttributeDecl(int n2, int n3, XMLAttributeDecl xMLAttributeDecl) {
        int n4 = n3 >> 8;
        int n5 = n3 & 255;
        this.fAttributeDeclName[n4][n5].setValues(xMLAttributeDecl.name);
        this.fAttributeDeclType[n4][n5] = xMLAttributeDecl.simpleType.type;
        if (xMLAttributeDecl.simpleType.list) {
            short[] arrs = this.fAttributeDeclType[n4];
            int n6 = n5;
            arrs[n6] = (short)(arrs[n6] | 128);
        }
        this.fAttributeDeclEnumeration[n4][n5] = xMLAttributeDecl.simpleType.enumeration;
        this.fAttributeDeclDefaultType[n4][n5] = xMLAttributeDecl.simpleType.defaultType;
        this.fAttributeDeclDatatypeValidator[n4][n5] = xMLAttributeDecl.simpleType.datatypeValidator;
        this.fAttributeDeclDefaultValue[n4][n5] = xMLAttributeDecl.simpleType.defaultValue;
        this.fAttributeDeclNonNormalizedDefaultValue[n4][n5] = xMLAttributeDecl.simpleType.nonNormalizedDefaultValue;
        int n7 = n2 >> 8;
        int n8 = n2 & 255;
        int n9 = this.fElementDeclFirstAttributeDeclIndex[n7][n8];
        while (n9 != -1) {
            if (n9 == n3) break;
            n4 = n9 >> 8;
            n5 = n9 & 255;
            n9 = this.fAttributeDeclNextAttributeDeclIndex[n4][n5];
        }
        if (n9 == -1) {
            if (this.fElementDeclFirstAttributeDeclIndex[n7][n8] == -1) {
                this.fElementDeclFirstAttributeDeclIndex[n7][n8] = n3;
            } else {
                n9 = this.fElementDeclLastAttributeDeclIndex[n7][n8];
                n4 = n9 >> 8;
                n5 = n9 & 255;
                this.fAttributeDeclNextAttributeDeclIndex[n4][n5] = n3;
            }
            this.fElementDeclLastAttributeDeclIndex[n7][n8] = n3;
        }
    }

    protected int createContentSpec() {
        int n2 = this.fContentSpecCount >> 8;
        int n3 = this.fContentSpecCount & 255;
        this.ensureContentSpecCapacity(n2);
        this.fContentSpecType[n2][n3] = -1;
        this.fContentSpecValue[n2][n3] = null;
        this.fContentSpecOtherValue[n2][n3] = null;
        return this.fContentSpecCount++;
    }

    protected void setContentSpec(int n2, XMLContentSpec xMLContentSpec) {
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        this.fContentSpecType[n3][n4] = xMLContentSpec.type;
        this.fContentSpecValue[n3][n4] = xMLContentSpec.value;
        this.fContentSpecOtherValue[n3][n4] = xMLContentSpec.otherValue;
    }

    protected int createEntityDecl() {
        int n2 = this.fEntityCount >> 8;
        int n3 = this.fEntityCount & 255;
        this.ensureEntityDeclCapacity(n2);
        this.fEntityIsPE[n2][n3] = 0;
        this.fEntityInExternal[n2][n3] = 0;
        return this.fEntityCount++;
    }

    protected void setEntityDecl(int n2, XMLEntityDecl xMLEntityDecl) {
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        this.fEntityName[n3][n4] = xMLEntityDecl.name;
        this.fEntityValue[n3][n4] = xMLEntityDecl.value;
        this.fEntityPublicId[n3][n4] = xMLEntityDecl.publicId;
        this.fEntitySystemId[n3][n4] = xMLEntityDecl.systemId;
        this.fEntityBaseSystemId[n3][n4] = xMLEntityDecl.baseSystemId;
        this.fEntityNotation[n3][n4] = xMLEntityDecl.notation;
        this.fEntityIsPE[n3][n4] = xMLEntityDecl.isPE ? 1 : 0;
        this.fEntityInExternal[n3][n4] = xMLEntityDecl.inExternal ? 1 : 0;
        this.fEntityIndexMap.put(xMLEntityDecl.name, n2);
    }

    protected int createNotationDecl() {
        int n2 = this.fNotationCount >> 8;
        this.ensureNotationDeclCapacity(n2);
        return this.fNotationCount++;
    }

    protected void setNotationDecl(int n2, XMLNotationDecl xMLNotationDecl) {
        int n3 = n2 >> 8;
        int n4 = n2 & 255;
        this.fNotationName[n3][n4] = xMLNotationDecl.name;
        this.fNotationPublicId[n3][n4] = xMLNotationDecl.publicId;
        this.fNotationSystemId[n3][n4] = xMLNotationDecl.systemId;
        this.fNotationBaseSystemId[n3][n4] = xMLNotationDecl.baseSystemId;
        this.fNotationIndexMap.put(xMLNotationDecl.name, n2);
    }

    protected int addContentSpecNode(short s2, String string) {
        int n2 = this.createContentSpec();
        this.fContentSpec.setValues(s2, string, null);
        this.setContentSpec(n2, this.fContentSpec);
        return n2;
    }

    protected int addUniqueLeafNode(String string) {
        int n2 = this.createContentSpec();
        this.fContentSpec.setValues(0, string, null);
        this.setContentSpec(n2, this.fContentSpec);
        return n2;
    }

    protected int addContentSpecNode(short s2, int n2, int n3) {
        int n4 = this.createContentSpec();
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        arrn[0] = n2;
        arrn2[0] = n3;
        this.fContentSpec.setValues(s2, arrn, arrn2);
        this.setContentSpec(n4, this.fContentSpec);
        return n4;
    }

    protected void initializeContentModelStack() {
        if (this.fOpStack == null) {
            this.fOpStack = new short[8];
            this.fNodeIndexStack = new int[8];
            this.fPrevNodeIndexStack = new int[8];
        } else if (this.fDepth == this.fOpStack.length) {
            short[] arrs = new short[this.fDepth * 2];
            System.arraycopy(this.fOpStack, 0, arrs, 0, this.fDepth);
            this.fOpStack = arrs;
            int[] arrn = new int[this.fDepth * 2];
            System.arraycopy(this.fNodeIndexStack, 0, arrn, 0, this.fDepth);
            this.fNodeIndexStack = arrn;
            arrn = new int[this.fDepth * 2];
            System.arraycopy(this.fPrevNodeIndexStack, 0, arrn, 0, this.fDepth);
            this.fPrevNodeIndexStack = arrn;
        }
        this.fOpStack[this.fDepth] = -1;
        this.fNodeIndexStack[this.fDepth] = -1;
        this.fPrevNodeIndexStack[this.fDepth] = -1;
    }

    boolean isImmutable() {
        return this.fIsImmutable;
    }

    private void appendContentSpec(XMLContentSpec xMLContentSpec, StringBuffer stringBuffer, boolean bl, int n2) {
        int n3 = xMLContentSpec.type & 15;
        switch (n3) {
            case 0: {
                if (xMLContentSpec.value == null && xMLContentSpec.otherValue == null) {
                    stringBuffer.append("#PCDATA");
                    break;
                }
                if (xMLContentSpec.value == null && xMLContentSpec.otherValue != null) {
                    stringBuffer.append("##any:uri=").append(xMLContentSpec.otherValue);
                    break;
                }
                if (xMLContentSpec.value == null) {
                    stringBuffer.append("##any");
                    break;
                }
                stringBuffer.append(xMLContentSpec.value);
                break;
            }
            case 1: {
                if (n2 == 3 || n2 == 2 || n2 == 1) {
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    stringBuffer.append('(');
                    this.appendContentSpec(xMLContentSpec, stringBuffer, true, n3);
                    stringBuffer.append(')');
                } else {
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    this.appendContentSpec(xMLContentSpec, stringBuffer, true, n3);
                }
                stringBuffer.append('?');
                break;
            }
            case 2: {
                if (n2 == 3 || n2 == 2 || n2 == 1) {
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    stringBuffer.append('(');
                    this.appendContentSpec(xMLContentSpec, stringBuffer, true, n3);
                    stringBuffer.append(')');
                } else {
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    this.appendContentSpec(xMLContentSpec, stringBuffer, true, n3);
                }
                stringBuffer.append('*');
                break;
            }
            case 3: {
                if (n2 == 3 || n2 == 2 || n2 == 1) {
                    stringBuffer.append('(');
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    this.appendContentSpec(xMLContentSpec, stringBuffer, true, n3);
                    stringBuffer.append(')');
                } else {
                    this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                    this.appendContentSpec(xMLContentSpec, stringBuffer, true, n3);
                }
                stringBuffer.append('+');
                break;
            }
            case 4: 
            case 5: {
                if (bl) {
                    stringBuffer.append('(');
                }
                short s2 = xMLContentSpec.type;
                int n4 = ((int[])xMLContentSpec.otherValue)[0];
                this.getContentSpec(((int[])xMLContentSpec.value)[0], xMLContentSpec);
                this.appendContentSpec(xMLContentSpec, stringBuffer, xMLContentSpec.type != s2, n3);
                if (s2 == 4) {
                    stringBuffer.append('|');
                } else {
                    stringBuffer.append(',');
                }
                this.getContentSpec(n4, xMLContentSpec);
                this.appendContentSpec(xMLContentSpec, stringBuffer, true, n3);
                if (!bl) break;
                stringBuffer.append(')');
                break;
            }
            case 6: {
                stringBuffer.append("##any");
                if (xMLContentSpec.otherValue == null) break;
                stringBuffer.append(":uri=");
                stringBuffer.append(xMLContentSpec.otherValue);
                break;
            }
            case 7: {
                stringBuffer.append("##other:uri=");
                stringBuffer.append(xMLContentSpec.otherValue);
                break;
            }
            case 8: {
                stringBuffer.append("##local");
                break;
            }
            default: {
                stringBuffer.append("???");
            }
        }
    }

    private void printAttribute(int n2) {
        XMLAttributeDecl xMLAttributeDecl = new XMLAttributeDecl();
        if (this.getAttributeDecl(n2, xMLAttributeDecl)) {
            System.out.print(" { ");
            System.out.print(xMLAttributeDecl.name.localpart);
            System.out.print(" }");
        }
    }

    private synchronized ContentModelValidator createChildModel(int n2) {
        Object object;
        XMLContentSpec xMLContentSpec = new XMLContentSpec();
        this.getContentSpec(n2, xMLContentSpec);
        if ((xMLContentSpec.type & 15) != 6 && (xMLContentSpec.type & 15) != 7 && (xMLContentSpec.type & 15) != 8) {
            if (xMLContentSpec.type == 0) {
                if (xMLContentSpec.value == null && xMLContentSpec.otherValue == null) {
                    throw new RuntimeException("ImplementationMessages.VAL_NPCD");
                }
                this.fQName.setValues(null, (String)xMLContentSpec.value, (String)xMLContentSpec.value, (String)xMLContentSpec.otherValue);
                return new SimpleContentModel(xMLContentSpec.type, this.fQName, null);
            }
            if (xMLContentSpec.type == 4 || xMLContentSpec.type == 5) {
                object = new XMLContentSpec();
                XMLContentSpec xMLContentSpec2 = new XMLContentSpec();
                this.getContentSpec(((int[])xMLContentSpec.value)[0], (XMLContentSpec)object);
                this.getContentSpec(((int[])xMLContentSpec.otherValue)[0], xMLContentSpec2);
                if (object.type == 0 && xMLContentSpec2.type == 0) {
                    this.fQName.setValues(null, (String)object.value, (String)object.value, (String)object.otherValue);
                    this.fQName2.setValues(null, (String)xMLContentSpec2.value, (String)xMLContentSpec2.value, (String)xMLContentSpec2.otherValue);
                    return new SimpleContentModel(xMLContentSpec.type, this.fQName, this.fQName2);
                }
            } else if (xMLContentSpec.type == 1 || xMLContentSpec.type == 2 || xMLContentSpec.type == 3) {
                object = new XMLContentSpec();
                this.getContentSpec(((int[])xMLContentSpec.value)[0], (XMLContentSpec)object);
                if (object.type == 0) {
                    this.fQName.setValues(null, (String)object.value, (String)object.value, (String)object.otherValue);
                    return new SimpleContentModel(xMLContentSpec.type, this.fQName, null);
                }
            } else {
                throw new RuntimeException("ImplementationMessages.VAL_CST");
            }
        }
        this.fLeafCount = 0;
        this.fLeafCount = 0;
        object = this.buildSyntaxTree(n2, xMLContentSpec);
        return new DFAContentModel((CMNode)object, this.fLeafCount, false);
    }

    private final CMNode buildSyntaxTree(int n2, XMLContentSpec xMLContentSpec) {
        CMNode cMNode = null;
        this.getContentSpec(n2, xMLContentSpec);
        if ((xMLContentSpec.type & 15) == 6) {
            cMNode = new CMAny(xMLContentSpec.type, (String)xMLContentSpec.otherValue, this.fLeafCount++);
        } else if ((xMLContentSpec.type & 15) == 7) {
            cMNode = new CMAny(xMLContentSpec.type, (String)xMLContentSpec.otherValue, this.fLeafCount++);
        } else if ((xMLContentSpec.type & 15) == 8) {
            cMNode = new CMAny(xMLContentSpec.type, null, this.fLeafCount++);
        } else if (xMLContentSpec.type == 0) {
            this.fQName.setValues(null, (String)xMLContentSpec.value, (String)xMLContentSpec.value, (String)xMLContentSpec.otherValue);
            cMNode = new CMLeaf(this.fQName, this.fLeafCount++);
        } else {
            int n3 = ((int[])xMLContentSpec.value)[0];
            int n4 = ((int[])xMLContentSpec.otherValue)[0];
            if (xMLContentSpec.type == 4 || xMLContentSpec.type == 5) {
                cMNode = new CMBinOp(xMLContentSpec.type, this.buildSyntaxTree(n3, xMLContentSpec), this.buildSyntaxTree(n4, xMLContentSpec));
            } else if (xMLContentSpec.type == 2) {
                cMNode = new CMUniOp(xMLContentSpec.type, this.buildSyntaxTree(n3, xMLContentSpec));
            } else if (xMLContentSpec.type == 2 || xMLContentSpec.type == 1 || xMLContentSpec.type == 3) {
                cMNode = new CMUniOp(xMLContentSpec.type, this.buildSyntaxTree(n3, xMLContentSpec));
            } else {
                throw new RuntimeException("ImplementationMessages.VAL_CST");
            }
        }
        return cMNode;
    }

    private void contentSpecTree(int n2, XMLContentSpec xMLContentSpec, ChildrenList childrenList) {
        this.getContentSpec(n2, xMLContentSpec);
        if (xMLContentSpec.type == 0 || (xMLContentSpec.type & 15) == 6 || (xMLContentSpec.type & 15) == 8 || (xMLContentSpec.type & 15) == 7) {
            if (childrenList.length == childrenList.qname.length) {
                QName[] arrqName = new QName[childrenList.length * 2];
                System.arraycopy(childrenList.qname, 0, arrqName, 0, childrenList.length);
                childrenList.qname = arrqName;
                int[] arrn = new int[childrenList.length * 2];
                System.arraycopy(childrenList.type, 0, arrn, 0, childrenList.length);
                childrenList.type = arrn;
            }
            childrenList.qname[childrenList.length] = new QName(null, (String)xMLContentSpec.value, (String)xMLContentSpec.value, (String)xMLContentSpec.otherValue);
            childrenList.type[childrenList.length] = xMLContentSpec.type;
            ++childrenList.length;
            return;
        }
        int n3 = xMLContentSpec.value != null ? ((int[])xMLContentSpec.value)[0] : -1;
        int n4 = -1;
        if (xMLContentSpec.otherValue == null) {
            return;
        }
        n4 = ((int[])xMLContentSpec.otherValue)[0];
        if (xMLContentSpec.type == 4 || xMLContentSpec.type == 5) {
            this.contentSpecTree(n3, xMLContentSpec, childrenList);
            this.contentSpecTree(n4, xMLContentSpec, childrenList);
            return;
        }
        if (xMLContentSpec.type == 1 || xMLContentSpec.type == 2 || xMLContentSpec.type == 3) {
            this.contentSpecTree(n3, xMLContentSpec, childrenList);
            return;
        }
        throw new RuntimeException("Invalid content spec type seen in contentSpecTree() method of AbstractDTDGrammar class : " + xMLContentSpec.type);
    }

    private void ensureElementDeclCapacity(int n2) {
        if (n2 >= this.fElementDeclName.length) {
            this.fElementDeclIsExternal = DTDGrammar.resize(this.fElementDeclIsExternal, this.fElementDeclIsExternal.length * 2);
            this.fElementDeclName = DTDGrammar.resize(this.fElementDeclName, this.fElementDeclName.length * 2);
            this.fElementDeclType = DTDGrammar.resize(this.fElementDeclType, this.fElementDeclType.length * 2);
            this.fElementDeclContentModelValidator = DTDGrammar.resize(this.fElementDeclContentModelValidator, this.fElementDeclContentModelValidator.length * 2);
            this.fElementDeclContentSpecIndex = DTDGrammar.resize(this.fElementDeclContentSpecIndex, this.fElementDeclContentSpecIndex.length * 2);
            this.fElementDeclFirstAttributeDeclIndex = DTDGrammar.resize(this.fElementDeclFirstAttributeDeclIndex, this.fElementDeclFirstAttributeDeclIndex.length * 2);
            this.fElementDeclLastAttributeDeclIndex = DTDGrammar.resize(this.fElementDeclLastAttributeDeclIndex, this.fElementDeclLastAttributeDeclIndex.length * 2);
        } else if (this.fElementDeclName[n2] != null) {
            return;
        }
        this.fElementDeclIsExternal[n2] = new int[256];
        this.fElementDeclName[n2] = new QName[256];
        this.fElementDeclType[n2] = new short[256];
        this.fElementDeclContentModelValidator[n2] = new ContentModelValidator[256];
        this.fElementDeclContentSpecIndex[n2] = new int[256];
        this.fElementDeclFirstAttributeDeclIndex[n2] = new int[256];
        this.fElementDeclLastAttributeDeclIndex[n2] = new int[256];
    }

    private void ensureAttributeDeclCapacity(int n2) {
        if (n2 >= this.fAttributeDeclName.length) {
            this.fAttributeDeclIsExternal = DTDGrammar.resize(this.fAttributeDeclIsExternal, this.fAttributeDeclIsExternal.length * 2);
            this.fAttributeDeclName = DTDGrammar.resize(this.fAttributeDeclName, this.fAttributeDeclName.length * 2);
            this.fAttributeDeclType = DTDGrammar.resize(this.fAttributeDeclType, this.fAttributeDeclType.length * 2);
            this.fAttributeDeclEnumeration = DTDGrammar.resize(this.fAttributeDeclEnumeration, this.fAttributeDeclEnumeration.length * 2);
            this.fAttributeDeclDefaultType = DTDGrammar.resize(this.fAttributeDeclDefaultType, this.fAttributeDeclDefaultType.length * 2);
            this.fAttributeDeclDatatypeValidator = DTDGrammar.resize(this.fAttributeDeclDatatypeValidator, this.fAttributeDeclDatatypeValidator.length * 2);
            this.fAttributeDeclDefaultValue = DTDGrammar.resize(this.fAttributeDeclDefaultValue, this.fAttributeDeclDefaultValue.length * 2);
            this.fAttributeDeclNonNormalizedDefaultValue = DTDGrammar.resize(this.fAttributeDeclNonNormalizedDefaultValue, this.fAttributeDeclNonNormalizedDefaultValue.length * 2);
            this.fAttributeDeclNextAttributeDeclIndex = DTDGrammar.resize(this.fAttributeDeclNextAttributeDeclIndex, this.fAttributeDeclNextAttributeDeclIndex.length * 2);
        } else if (this.fAttributeDeclName[n2] != null) {
            return;
        }
        this.fAttributeDeclIsExternal[n2] = new int[256];
        this.fAttributeDeclName[n2] = new QName[256];
        this.fAttributeDeclType[n2] = new short[256];
        this.fAttributeDeclEnumeration[n2] = new String[256][];
        this.fAttributeDeclDefaultType[n2] = new short[256];
        this.fAttributeDeclDatatypeValidator[n2] = new DatatypeValidator[256];
        this.fAttributeDeclDefaultValue[n2] = new String[256];
        this.fAttributeDeclNonNormalizedDefaultValue[n2] = new String[256];
        this.fAttributeDeclNextAttributeDeclIndex[n2] = new int[256];
    }

    private void ensureEntityDeclCapacity(int n2) {
        if (n2 >= this.fEntityName.length) {
            this.fEntityName = DTDGrammar.resize(this.fEntityName, this.fEntityName.length * 2);
            this.fEntityValue = DTDGrammar.resize(this.fEntityValue, this.fEntityValue.length * 2);
            this.fEntityPublicId = DTDGrammar.resize(this.fEntityPublicId, this.fEntityPublicId.length * 2);
            this.fEntitySystemId = DTDGrammar.resize(this.fEntitySystemId, this.fEntitySystemId.length * 2);
            this.fEntityBaseSystemId = DTDGrammar.resize(this.fEntityBaseSystemId, this.fEntityBaseSystemId.length * 2);
            this.fEntityNotation = DTDGrammar.resize(this.fEntityNotation, this.fEntityNotation.length * 2);
            this.fEntityIsPE = DTDGrammar.resize(this.fEntityIsPE, this.fEntityIsPE.length * 2);
            this.fEntityInExternal = DTDGrammar.resize(this.fEntityInExternal, this.fEntityInExternal.length * 2);
        } else if (this.fEntityName[n2] != null) {
            return;
        }
        this.fEntityName[n2] = new String[256];
        this.fEntityValue[n2] = new String[256];
        this.fEntityPublicId[n2] = new String[256];
        this.fEntitySystemId[n2] = new String[256];
        this.fEntityBaseSystemId[n2] = new String[256];
        this.fEntityNotation[n2] = new String[256];
        this.fEntityIsPE[n2] = new byte[256];
        this.fEntityInExternal[n2] = new byte[256];
    }

    private void ensureNotationDeclCapacity(int n2) {
        if (n2 >= this.fNotationName.length) {
            this.fNotationName = DTDGrammar.resize(this.fNotationName, this.fNotationName.length * 2);
            this.fNotationPublicId = DTDGrammar.resize(this.fNotationPublicId, this.fNotationPublicId.length * 2);
            this.fNotationSystemId = DTDGrammar.resize(this.fNotationSystemId, this.fNotationSystemId.length * 2);
            this.fNotationBaseSystemId = DTDGrammar.resize(this.fNotationBaseSystemId, this.fNotationBaseSystemId.length * 2);
        } else if (this.fNotationName[n2] != null) {
            return;
        }
        this.fNotationName[n2] = new String[256];
        this.fNotationPublicId[n2] = new String[256];
        this.fNotationSystemId[n2] = new String[256];
        this.fNotationBaseSystemId[n2] = new String[256];
    }

    private void ensureContentSpecCapacity(int n2) {
        if (n2 >= this.fContentSpecType.length) {
            this.fContentSpecType = DTDGrammar.resize(this.fContentSpecType, this.fContentSpecType.length * 2);
            this.fContentSpecValue = DTDGrammar.resize(this.fContentSpecValue, this.fContentSpecValue.length * 2);
            this.fContentSpecOtherValue = DTDGrammar.resize(this.fContentSpecOtherValue, this.fContentSpecOtherValue.length * 2);
        } else if (this.fContentSpecType[n2] != null) {
            return;
        }
        this.fContentSpecType[n2] = new short[256];
        this.fContentSpecValue[n2] = new Object[256];
        this.fContentSpecOtherValue[n2] = new Object[256];
    }

    private static byte[][] resize(byte[][] arrby, int n2) {
        byte[][] arrarrby = new byte[n2][];
        System.arraycopy(arrby, 0, arrarrby, 0, arrby.length);
        return arrarrby;
    }

    private static short[][] resize(short[][] arrs, int n2) {
        short[][] arrarrs = new short[n2][];
        System.arraycopy(arrs, 0, arrarrs, 0, arrs.length);
        return arrarrs;
    }

    private static int[][] resize(int[][] arrn, int n2) {
        int[][] arrarrn = new int[n2][];
        System.arraycopy(arrn, 0, arrarrn, 0, arrn.length);
        return arrarrn;
    }

    private static DatatypeValidator[][] resize(DatatypeValidator[][] arrdatatypeValidator, int n2) {
        DatatypeValidator[][] arrdatatypeValidator2 = new DatatypeValidator[n2][];
        System.arraycopy(arrdatatypeValidator, 0, arrdatatypeValidator2, 0, arrdatatypeValidator.length);
        return arrdatatypeValidator2;
    }

    private static ContentModelValidator[][] resize(ContentModelValidator[][] arrcontentModelValidator, int n2) {
        ContentModelValidator[][] arrcontentModelValidator2 = new ContentModelValidator[n2][];
        System.arraycopy(arrcontentModelValidator, 0, arrcontentModelValidator2, 0, arrcontentModelValidator.length);
        return arrcontentModelValidator2;
    }

    private static Object[][] resize(Object[][] arrobject, int n2) {
        Object[][] arrobject2 = new Object[n2][];
        System.arraycopy(arrobject, 0, arrobject2, 0, arrobject.length);
        return arrobject2;
    }

    private static QName[][] resize(QName[][] arrqName, int n2) {
        QName[][] arrqName2 = new QName[n2][];
        System.arraycopy(arrqName, 0, arrqName2, 0, arrqName.length);
        return arrqName2;
    }

    private static String[][] resize(String[][] arrstring, int n2) {
        String[][] arrstring2 = new String[n2][];
        System.arraycopy(arrstring, 0, arrstring2, 0, arrstring.length);
        return arrstring2;
    }

    private static String[][][] resize(String[][][] arrstring, int n2) {
        String[][][] arrstring2 = new String[n2][][];
        System.arraycopy(arrstring, 0, arrstring2, 0, arrstring.length);
        return arrstring2;
    }

    public boolean isEntityDeclared(String string) {
        return this.getEntityDeclIndex(string) != -1;
    }

    public boolean isEntityUnparsed(String string) {
        int n2 = this.getEntityDeclIndex(string);
        if (n2 > -1) {
            int n3 = n2 >> 8;
            int n4 = n2 & 255;
            return this.fEntityNotation[n3][n4] != null;
        }
        return false;
    }

    protected static final class QNameHashtable {
        private static final int INITIAL_BUCKET_SIZE = 4;
        private static final int HASHTABLE_SIZE = 101;
        private Object[][] fHashTable = new Object[101][];

        protected QNameHashtable() {
        }

        public void put(String string, int n2) {
            int n3 = (string.hashCode() & Integer.MAX_VALUE) % 101;
            Object[] arrobject = this.fHashTable[n3];
            if (arrobject == null) {
                arrobject = new Object[9];
                arrobject[0] = new int[]{1};
                arrobject[1] = string;
                arrobject[2] = new int[]{n2};
                this.fHashTable[n3] = arrobject;
            } else {
                int n4;
                int n5 = ((int[])arrobject[0])[0];
                int n6 = 1 + 2 * n5;
                if (n6 == arrobject.length) {
                    n4 = n5 + 4;
                    Object[] arrobject2 = new Object[1 + 2 * n4];
                    System.arraycopy(arrobject, 0, arrobject2, 0, n6);
                    arrobject = arrobject2;
                    this.fHashTable[n3] = arrobject;
                }
                n4 = 0;
                int n7 = 1;
                int n8 = 0;
                while (n8 < n5) {
                    if ((String)arrobject[n7] == string) {
                        ((int[])arrobject[n7 + 1])[0] = n2;
                        n4 = 1;
                        break;
                    }
                    n7 += 2;
                    ++n8;
                }
                if (n4 == 0) {
                    arrobject[n6++] = string;
                    arrobject[n6] = new int[]{n2};
                    ((int[])arrobject[0])[0] = ++n5;
                }
            }
        }

        public int get(String string) {
            int n2 = (string.hashCode() & Integer.MAX_VALUE) % 101;
            Object[] arrobject = this.fHashTable[n2];
            if (arrobject == null) {
                return -1;
            }
            int n3 = ((int[])arrobject[0])[0];
            int n4 = 1;
            int n5 = 0;
            while (n5 < n3) {
                if ((String)arrobject[n4] == string) {
                    return ((int[])arrobject[n4 + 1])[0];
                }
                n4 += 2;
                ++n5;
            }
            return -1;
        }
    }

    private static class ChildrenList {
        public int length = 0;
        public QName[] qname = new QName[2];
        public int[] type = new int[2];
    }

}

