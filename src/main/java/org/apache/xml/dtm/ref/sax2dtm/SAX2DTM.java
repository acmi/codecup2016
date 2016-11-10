/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref.sax2dtm;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.DTMStringPool;
import org.apache.xml.dtm.ref.DTMTreeWalker;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.IncrementalSAXSource;
import org.apache.xml.dtm.ref.IncrementalSAXSource_Filter;
import org.apache.xml.dtm.ref.NodeLocator;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.IntVector;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class SAX2DTM
extends DTMDefaultBaseIterators
implements ContentHandler,
DTDHandler,
EntityResolver,
ErrorHandler,
DeclHandler,
LexicalHandler {
    private static final boolean DEBUG = false;
    private IncrementalSAXSource m_incrementalSAXSource = null;
    protected FastStringBuffer m_chars;
    protected SuballocatedIntVector m_data;
    protected transient IntStack m_parents;
    protected transient int m_previous = 0;
    protected transient Vector m_prefixMappings = new Vector();
    protected transient IntStack m_contextIndexes;
    protected transient int m_textType = 3;
    protected transient int m_coalescedTextType = 3;
    protected transient Locator m_locator = null;
    private transient String m_systemId = null;
    protected transient boolean m_insideDTD = false;
    protected DTMTreeWalker m_walker = new DTMTreeWalker();
    protected DTMStringPool m_valuesOrPrefixes;
    protected boolean m_endDocumentOccured = false;
    protected SuballocatedIntVector m_dataOrQName;
    protected Hashtable m_idAttributes = new Hashtable();
    private static final String[] m_fixednames = new String[]{null, null, null, "#text", "#cdata_section", null, null, null, "#comment", "#document", null, "#document-fragment", null};
    private Vector m_entities = null;
    private static final int ENTITY_FIELD_PUBLICID = 0;
    private static final int ENTITY_FIELD_SYSTEMID = 1;
    private static final int ENTITY_FIELD_NOTATIONNAME = 2;
    private static final int ENTITY_FIELD_NAME = 3;
    private static final int ENTITY_FIELDS_PER = 4;
    protected int m_textPendingStart = -1;
    protected boolean m_useSourceLocationProperty = false;
    protected StringVector m_sourceSystemId;
    protected IntVector m_sourceLine;
    protected IntVector m_sourceColumn;
    boolean m_pastFirstElement = false;

    public SAX2DTM(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl) {
        this(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, 512, true, false);
    }

    public SAX2DTM(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl, int n3, boolean bl2, boolean bl3) {
        super(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, n3, bl2, bl3);
        if (n3 <= 64) {
            this.m_data = new SuballocatedIntVector(n3, 4);
            this.m_dataOrQName = new SuballocatedIntVector(n3, 4);
            this.m_valuesOrPrefixes = new DTMStringPool(16);
            this.m_chars = new FastStringBuffer(7, 10);
            this.m_contextIndexes = new IntStack(4);
            this.m_parents = new IntStack(4);
        } else {
            this.m_data = new SuballocatedIntVector(n3, 32);
            this.m_dataOrQName = new SuballocatedIntVector(n3, 32);
            this.m_valuesOrPrefixes = new DTMStringPool();
            this.m_chars = new FastStringBuffer(10, 13);
            this.m_contextIndexes = new IntStack();
            this.m_parents = new IntStack();
        }
        this.m_data.addElement(0);
        this.m_useSourceLocationProperty = dTMManager.getSource_location();
        this.m_sourceSystemId = this.m_useSourceLocationProperty ? new StringVector() : null;
        this.m_sourceLine = this.m_useSourceLocationProperty ? new IntVector() : null;
        this.m_sourceColumn = this.m_useSourceLocationProperty ? new IntVector() : null;
    }

    public void setUseSourceLocation(boolean bl) {
        this.m_useSourceLocationProperty = bl;
    }

    protected int _dataOrQName(int n2) {
        if (n2 < this.m_size) {
            return this.m_dataOrQName.elementAt(n2);
        }
        do {
            boolean bl;
            if (bl = this.nextNode()) continue;
            return -1;
        } while (n2 >= this.m_size);
        return this.m_dataOrQName.elementAt(n2);
    }

    public void clearCoRoutine() {
        this.clearCoRoutine(true);
    }

    public void clearCoRoutine(boolean bl) {
        if (null != this.m_incrementalSAXSource) {
            if (bl) {
                this.m_incrementalSAXSource.deliverMoreNodes(false);
            }
            this.m_incrementalSAXSource = null;
        }
    }

    public void setIncrementalSAXSource(IncrementalSAXSource incrementalSAXSource) {
        this.m_incrementalSAXSource = incrementalSAXSource;
        incrementalSAXSource.setContentHandler(this);
        incrementalSAXSource.setLexicalHandler(this);
        incrementalSAXSource.setDTDHandler(this);
    }

    public ContentHandler getContentHandler() {
        if (this.m_incrementalSAXSource instanceof IncrementalSAXSource_Filter) {
            return (ContentHandler)((Object)this.m_incrementalSAXSource);
        }
        return this;
    }

    public LexicalHandler getLexicalHandler() {
        if (this.m_incrementalSAXSource instanceof IncrementalSAXSource_Filter) {
            return (LexicalHandler)((Object)this.m_incrementalSAXSource);
        }
        return this;
    }

    public EntityResolver getEntityResolver() {
        return this;
    }

    public DTDHandler getDTDHandler() {
        return this;
    }

    public ErrorHandler getErrorHandler() {
        return this;
    }

    public DeclHandler getDeclHandler() {
        return this;
    }

    public boolean needsTwoThreads() {
        return null != this.m_incrementalSAXSource;
    }

    public void dispatchCharactersEvents(int n2, ContentHandler contentHandler, boolean bl) throws SAXException {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 == -1) {
            return;
        }
        short s2 = this._type(n3);
        if (this.isTextType(s2)) {
            int n4 = this.m_dataOrQName.elementAt(n3);
            int n5 = this.m_data.elementAt(n4);
            int n6 = this.m_data.elementAt(n4 + 1);
            if (bl) {
                this.m_chars.sendNormalizedSAXcharacters(contentHandler, n5, n6);
            } else {
                this.m_chars.sendSAXcharacters(contentHandler, n5, n6);
            }
        } else {
            int n7 = this._firstch(n3);
            if (-1 != n7) {
                int n8 = -1;
                int n9 = 0;
                int n10 = n3;
                n3 = n7;
                do {
                    if (!this.isTextType(s2 = this._type(n3))) continue;
                    int n11 = this._dataOrQName(n3);
                    if (-1 == n8) {
                        n8 = this.m_data.elementAt(n11);
                    }
                    n9 += this.m_data.elementAt(n11 + 1);
                } while (-1 != (n3 = this.getNextNodeIdentity(n3)) && this._parent(n3) >= n10);
                if (n9 > 0) {
                    if (bl) {
                        this.m_chars.sendNormalizedSAXcharacters(contentHandler, n8, n9);
                    } else {
                        this.m_chars.sendSAXcharacters(contentHandler, n8, n9);
                    }
                }
            } else if (s2 != 1) {
                int n12 = this._dataOrQName(n3);
                if (n12 < 0) {
                    n12 = - n12;
                    n12 = this.m_data.elementAt(n12 + 1);
                }
                String string = this.m_valuesOrPrefixes.indexToString(n12);
                if (bl) {
                    FastStringBuffer.sendNormalizedSAXcharacters(string.toCharArray(), 0, string.length(), contentHandler);
                } else {
                    contentHandler.characters(string.toCharArray(), 0, string.length());
                }
            }
        }
    }

    public String getNodeName(int n2) {
        int n3 = this.getExpandedTypeID(n2);
        int n4 = this.m_expandedNameTable.getNamespaceID(n3);
        if (0 == n4) {
            short s2 = this.getNodeType(n2);
            if (s2 == 13) {
                if (null == this.m_expandedNameTable.getLocalName(n3)) {
                    return "xmlns";
                }
                return "xmlns:" + this.m_expandedNameTable.getLocalName(n3);
            }
            if (0 == this.m_expandedNameTable.getLocalNameID(n3)) {
                return m_fixednames[s2];
            }
            return this.m_expandedNameTable.getLocalName(n3);
        }
        int n5 = this.m_dataOrQName.elementAt(this.makeNodeIdentity(n2));
        if (n5 < 0) {
            n5 = - n5;
            n5 = this.m_data.elementAt(n5);
        }
        return this.m_valuesOrPrefixes.indexToString(n5);
    }

    public String getNodeNameX(int n2) {
        int n3 = this.getExpandedTypeID(n2);
        int n4 = this.m_expandedNameTable.getNamespaceID(n3);
        if (0 == n4) {
            String string = this.m_expandedNameTable.getLocalName(n3);
            if (string == null) {
                return "";
            }
            return string;
        }
        int n5 = this.m_dataOrQName.elementAt(this.makeNodeIdentity(n2));
        if (n5 < 0) {
            n5 = - n5;
            n5 = this.m_data.elementAt(n5);
        }
        return this.m_valuesOrPrefixes.indexToString(n5);
    }

    public boolean isAttributeSpecified(int n2) {
        return true;
    }

    public String getDocumentTypeDeclarationSystemIdentifier() {
        this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return null;
    }

    protected int getNextNodeIdentity(int n2) {
        while (++n2 >= this.m_size) {
            if (null == this.m_incrementalSAXSource) {
                return -1;
            }
            this.nextNode();
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void dispatchToEvents(int n2, ContentHandler contentHandler) throws SAXException {
        DTMTreeWalker dTMTreeWalker = this.m_walker;
        ContentHandler contentHandler2 = dTMTreeWalker.getcontentHandler();
        if (null != contentHandler2) {
            dTMTreeWalker = new DTMTreeWalker();
        }
        dTMTreeWalker.setcontentHandler(contentHandler);
        dTMTreeWalker.setDTM(this);
        try {
            dTMTreeWalker.traverse(n2);
        }
        finally {
            dTMTreeWalker.setcontentHandler(null);
        }
    }

    public int getNumberOfNodes() {
        return this.m_size;
    }

    protected boolean nextNode() {
        if (null == this.m_incrementalSAXSource) {
            return false;
        }
        if (this.m_endDocumentOccured) {
            this.clearCoRoutine();
            return false;
        }
        Object object = this.m_incrementalSAXSource.deliverMoreNodes(true);
        if (!(object instanceof Boolean)) {
            if (object instanceof RuntimeException) {
                throw (RuntimeException)object;
            }
            if (object instanceof Exception) {
                throw new WrappedRuntimeException((Exception)object);
            }
            this.clearCoRoutine();
            return false;
        }
        if (object != Boolean.TRUE) {
            this.clearCoRoutine();
        }
        return true;
    }

    private final boolean isTextType(int n2) {
        return 3 == n2 || 4 == n2;
    }

    protected int addNode(int n2, int n3, int n4, int n5, int n6, boolean bl) {
        int n7 = this.m_size++;
        if (this.m_dtmIdent.size() == n7 >>> 16) {
            this.addNewDTMID(n7);
        }
        this.m_firstch.addElement(bl ? -2 : -1);
        this.m_nextsib.addElement(-2);
        this.m_parent.addElement(n4);
        this.m_exptype.addElement(n3);
        this.m_dataOrQName.addElement(n6);
        if (this.m_prevsib != null) {
            this.m_prevsib.addElement(n5);
        }
        if (-1 != n5) {
            this.m_nextsib.setElementAt(n7, n5);
        }
        if (this.m_locator != null && this.m_useSourceLocationProperty) {
            this.setSourceLocation();
        }
        switch (n2) {
            case 13: {
                this.declareNamespaceInContext(n4, n7);
                break;
            }
            case 2: {
                break;
            }
            default: {
                if (-1 != n5 || -1 == n4) break;
                this.m_firstch.setElementAt(n7, n4);
            }
        }
        return n7;
    }

    protected void addNewDTMID(int n2) {
        try {
            if (this.m_mgr == null) {
                throw new ClassCastException();
            }
            DTMManagerDefault dTMManagerDefault = (DTMManagerDefault)this.m_mgr;
            int n3 = dTMManagerDefault.getFirstFreeDTMID();
            dTMManagerDefault.addDTM(this, n3, n2);
            this.m_dtmIdent.addElement(n3 << 16);
        }
        catch (ClassCastException classCastException) {
            this.error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
        }
    }

    public void migrateTo(DTMManager dTMManager) {
        super.migrateTo(dTMManager);
        int n2 = this.m_dtmIdent.size();
        int n3 = this.m_mgrDefault.getFirstFreeDTMID();
        int n4 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            this.m_dtmIdent.setElementAt(n3 << 16, i2);
            this.m_mgrDefault.addDTM(this, n3, n4);
            ++n3;
            n4 += 65536;
        }
    }

    protected void setSourceLocation() {
        this.m_sourceSystemId.addElement(this.m_locator.getSystemId());
        this.m_sourceLine.addElement(this.m_locator.getLineNumber());
        this.m_sourceColumn.addElement(this.m_locator.getColumnNumber());
        if (this.m_sourceSystemId.size() != this.m_size) {
            String string = "CODING ERROR in Source Location: " + this.m_size + " != " + this.m_sourceSystemId.size();
            System.err.println(string);
            throw new RuntimeException(string);
        }
    }

    public String getNodeValue(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        short s2 = this._type(n3);
        if (this.isTextType(s2)) {
            int n4 = this._dataOrQName(n3);
            int n5 = this.m_data.elementAt(n4);
            int n6 = this.m_data.elementAt(n4 + 1);
            return this.m_chars.getString(n5, n6);
        }
        if (1 == s2 || 11 == s2 || 9 == s2) {
            return null;
        }
        int n7 = this._dataOrQName(n3);
        if (n7 < 0) {
            n7 = - n7;
            n7 = this.m_data.elementAt(n7 + 1);
        }
        return this.m_valuesOrPrefixes.indexToString(n7);
    }

    public String getLocalName(int n2) {
        return this.m_expandedNameTable.getLocalName(this._exptype(this.makeNodeIdentity(n2)));
    }

    public String getUnparsedEntityURI(String string) {
        String string2 = "";
        if (null == this.m_entities) {
            return string2;
        }
        int n2 = this.m_entities.size();
        for (int i2 = 0; i2 < n2; i2 += 4) {
            String string3 = (String)this.m_entities.elementAt(i2 + 3);
            if (null == string3 || !string3.equals(string)) continue;
            String string4 = (String)this.m_entities.elementAt(i2 + 2);
            if (null == string4 || null != (string2 = (String)this.m_entities.elementAt(i2 + 1))) break;
            string2 = (String)this.m_entities.elementAt(i2 + 0);
            break;
        }
        return string2;
    }

    public String getPrefix(int n2) {
        int n3;
        int n4 = this.makeNodeIdentity(n2);
        short s2 = this._type(n4);
        if (1 == s2) {
            int n5 = this._dataOrQName(n4);
            if (0 == n5) {
                return "";
            }
            String string = this.m_valuesOrPrefixes.indexToString(n5);
            return this.getPrefix(string, null);
        }
        if (2 == s2 && (n3 = this._dataOrQName(n4)) < 0) {
            n3 = this.m_data.elementAt(- n3);
            String string = this.m_valuesOrPrefixes.indexToString(n3);
            return this.getPrefix(string, null);
        }
        return "";
    }

    public int getAttributeNode(int n2, String string, String string2) {
        int n3 = this.getFirstAttribute(n2);
        while (-1 != n3) {
            boolean bl;
            String string3 = this.getNamespaceURI(n3);
            String string4 = this.getLocalName(n3);
            boolean bl2 = bl = string == string3 || string != null && string.equals(string3);
            if (bl && string2.equals(string4)) {
                return n3;
            }
            n3 = this.getNextAttribute(n3);
        }
        return -1;
    }

    public String getDocumentTypeDeclarationPublicIdentifier() {
        this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
        return null;
    }

    public String getNamespaceURI(int n2) {
        return this.m_expandedNameTable.getNamespace(this._exptype(this.makeNodeIdentity(n2)));
    }

    public XMLString getStringValue(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = n3 == -1 ? -1 : (int)this._type(n3);
        if (this.isTextType(n4)) {
            int n5 = this._dataOrQName(n3);
            int n6 = this.m_data.elementAt(n5);
            int n7 = this.m_data.elementAt(n5 + 1);
            return this.m_xstrf.newstr(this.m_chars, n6, n7);
        }
        int n8 = this._firstch(n3);
        if (-1 != n8) {
            int n9 = -1;
            int n10 = 0;
            int n11 = n3;
            n3 = n8;
            do {
                if (!this.isTextType(n4 = (int)this._type(n3))) continue;
                int n12 = this._dataOrQName(n3);
                if (-1 == n9) {
                    n9 = this.m_data.elementAt(n12);
                }
                n10 += this.m_data.elementAt(n12 + 1);
            } while (-1 != (n3 = this.getNextNodeIdentity(n3)) && this._parent(n3) >= n11);
            if (n10 > 0) {
                return this.m_xstrf.newstr(this.m_chars, n9, n10);
            }
        } else if (n4 != 1) {
            int n13 = this._dataOrQName(n3);
            if (n13 < 0) {
                n13 = - n13;
                n13 = this.m_data.elementAt(n13 + 1);
            }
            return this.m_xstrf.newstr(this.m_valuesOrPrefixes.indexToString(n13));
        }
        return this.m_xstrf.emptystr();
    }

    public boolean isWhitespace(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = n3 == -1 ? -1 : (int)this._type(n3);
        if (this.isTextType(n4)) {
            int n5 = this._dataOrQName(n3);
            int n6 = this.m_data.elementAt(n5);
            int n7 = this.m_data.elementAt(n5 + 1);
            return this.m_chars.isWhitespace(n6, n7);
        }
        return false;
    }

    public int getElementById(String string) {
        Integer n2;
        boolean bl = true;
        do {
            if (null != (n2 = (Integer)this.m_idAttributes.get(string))) {
                return this.makeNodeHandle(n2);
            }
            if (!bl || this.m_endDocumentOccured) break;
            bl = this.nextNode();
        } while (null == n2);
        return -1;
    }

    public String getPrefix(String string, String string2) {
        String string3;
        int n2 = -1;
        if (null != string2 && string2.length() > 0) {
            do {
                ++n2;
            } while (((n2 = this.m_prefixMappings.indexOf(string2, n2)) & 1) == 0);
            if (n2 >= 0) {
                string3 = (String)this.m_prefixMappings.elementAt(n2 - 1);
            } else if (null != string) {
                int n3 = string.indexOf(58);
                string3 = string.equals("xmlns") ? "" : (string.startsWith("xmlns:") ? string.substring(n3 + 1) : (n3 > 0 ? string.substring(0, n3) : null));
            } else {
                string3 = null;
            }
        } else {
            int n4;
            string3 = null != string ? ((n4 = string.indexOf(58)) > 0 ? (string.startsWith("xmlns:") ? string.substring(n4 + 1) : string.substring(0, n4)) : (string.equals("xmlns") ? "" : null)) : null;
        }
        return string3;
    }

    public int getIdForNamespace(String string) {
        return this.m_valuesOrPrefixes.stringToIndex(string);
    }

    public String getNamespaceURI(String string) {
        String string2 = "";
        int n2 = this.m_contextIndexes.peek() - 1;
        if (null == string) {
            string = "";
        }
        do {
            ++n2;
        } while ((n2 = this.m_prefixMappings.indexOf(string, n2)) >= 0 && (n2 & 1) == 1);
        if (n2 > -1) {
            string2 = (String)this.m_prefixMappings.elementAt(n2 + 1);
        }
        return string2;
    }

    public void setIDAttribute(String string, int n2) {
        this.m_idAttributes.put(string, new Integer(n2));
    }

    protected void charactersFlush() {
        if (this.m_textPendingStart >= 0) {
            int n2 = this.m_chars.size() - this.m_textPendingStart;
            boolean bl = false;
            if (this.getShouldStripWhitespace()) {
                bl = this.m_chars.isWhitespace(this.m_textPendingStart, n2);
            }
            if (bl) {
                this.m_chars.setLength(this.m_textPendingStart);
            } else if (n2 > 0) {
                int n3 = this.m_expandedNameTable.getExpandedTypeID(3);
                int n4 = this.m_data.size();
                this.m_previous = this.addNode(this.m_coalescedTextType, n3, this.m_parents.peek(), this.m_previous, n4, false);
                this.m_data.addElement(this.m_textPendingStart);
                this.m_data.addElement(n2);
            }
            this.m_textPendingStart = -1;
            this.m_coalescedTextType = 3;
            this.m_textType = 3;
        }
    }

    public InputSource resolveEntity(String string, String string2) throws SAXException {
        return null;
    }

    public void notationDecl(String string, String string2, String string3) throws SAXException {
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) throws SAXException {
        if (null == this.m_entities) {
            this.m_entities = new Vector();
        }
        try {
            string3 = SystemIDResolver.getAbsoluteURI(string3, this.getDocumentBaseURI());
        }
        catch (Exception exception) {
            throw new SAXException(exception);
        }
        this.m_entities.addElement(string2);
        this.m_entities.addElement(string3);
        this.m_entities.addElement(string4);
        this.m_entities.addElement(string);
    }

    public void setDocumentLocator(Locator locator) {
        this.m_locator = locator;
        this.m_systemId = locator.getSystemId();
    }

    public void startDocument() throws SAXException {
        int n2 = this.addNode(9, this.m_expandedNameTable.getExpandedTypeID(9), -1, -1, 0, true);
        this.m_parents.push(n2);
        this.m_previous = -1;
        this.m_contextIndexes.push(this.m_prefixMappings.size());
    }

    public void endDocument() throws SAXException {
        this.charactersFlush();
        this.m_nextsib.setElementAt(-1, 0);
        if (this.m_firstch.elementAt(0) == -2) {
            this.m_firstch.setElementAt(-1, 0);
        }
        if (-1 != this.m_previous) {
            this.m_nextsib.setElementAt(-1, this.m_previous);
        }
        this.m_parents = null;
        this.m_prefixMappings = null;
        this.m_contextIndexes = null;
        this.m_endDocumentOccured = true;
        this.m_locator = null;
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        if (null == string) {
            string = "";
        }
        this.m_prefixMappings.addElement(string);
        this.m_prefixMappings.addElement(string2);
    }

    public void endPrefixMapping(String string) throws SAXException {
        if (null == string) {
            string = "";
        }
        int n2 = this.m_contextIndexes.peek() - 1;
        do {
            ++n2;
        } while ((n2 = this.m_prefixMappings.indexOf(string, n2)) >= 0 && (n2 & 1) == 1);
        if (n2 > -1) {
            this.m_prefixMappings.setElementAt("%@$#^@#", n2);
            this.m_prefixMappings.setElementAt("%@$#^@#", n2 + 1);
        }
    }

    protected boolean declAlreadyDeclared(String string) {
        int n2 = this.m_contextIndexes.peek();
        Vector vector = this.m_prefixMappings;
        int n3 = vector.size();
        for (int i2 = n2; i2 < n3; i2 += 2) {
            String string2 = (String)vector.elementAt(i2);
            if (string2 == null || !string2.equals(string)) continue;
            return true;
        }
        return false;
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        int n2;
        int n3;
        int n4;
        this.charactersFlush();
        int n5 = this.m_expandedNameTable.getExpandedTypeID(string, string2, 1);
        String string4 = this.getPrefix(string3, string);
        int n6 = null != string4 ? this.m_valuesOrPrefixes.stringToIndex(string3) : 0;
        int n7 = this.addNode(1, n5, this.m_parents.peek(), this.m_previous, n6, true);
        if (this.m_indexing) {
            this.indexNode(n5, n7);
        }
        this.m_parents.push(n7);
        int n8 = this.m_contextIndexes.peek();
        int n9 = this.m_prefixMappings.size();
        int n10 = -1;
        if (!this.m_pastFirstElement) {
            string4 = "xml";
            String string5 = "http://www.w3.org/XML/1998/namespace";
            n5 = this.m_expandedNameTable.getExpandedTypeID(null, string4, 13);
            n3 = this.m_valuesOrPrefixes.stringToIndex(string5);
            n10 = this.addNode(13, n5, n7, n10, n3, false);
            this.m_pastFirstElement = true;
        }
        for (n2 = n8; n2 < n9; n2 += 2) {
            string4 = (String)this.m_prefixMappings.elementAt(n2);
            if (string4 == null) continue;
            String string6 = (String)this.m_prefixMappings.elementAt(n2 + 1);
            n5 = this.m_expandedNameTable.getExpandedTypeID(null, string4, 13);
            n4 = this.m_valuesOrPrefixes.stringToIndex(string6);
            n10 = this.addNode(13, n5, n7, n10, n4, false);
        }
        n2 = attributes.getLength();
        for (n3 = 0; n3 < n2; ++n3) {
            int n11;
            String string7 = attributes.getURI(n3);
            String string8 = attributes.getQName(n3);
            String string9 = attributes.getValue(n3);
            string4 = this.getPrefix(string8, string7);
            String string10 = attributes.getLocalName(n3);
            if (null != string8 && (string8.equals("xmlns") || string8.startsWith("xmlns:"))) {
                if (this.declAlreadyDeclared(string4)) continue;
                n11 = 13;
            } else {
                n11 = 2;
                if (attributes.getType(n3).equalsIgnoreCase("ID")) {
                    this.setIDAttribute(string9, n7);
                }
            }
            if (null == string9) {
                string9 = "";
            }
            int n12 = this.m_valuesOrPrefixes.stringToIndex(string9);
            if (null != string4) {
                n6 = this.m_valuesOrPrefixes.stringToIndex(string8);
                int n13 = this.m_data.size();
                this.m_data.addElement(n6);
                this.m_data.addElement(n12);
                n12 = - n13;
            }
            n5 = this.m_expandedNameTable.getExpandedTypeID(string7, string10, n11);
            n10 = this.addNode(n11, n5, n7, n10, n12, false);
        }
        if (-1 != n10) {
            this.m_nextsib.setElementAt(-1, n10);
        }
        if (null != this.m_wsfilter) {
            n3 = this.m_wsfilter.getShouldStripSpace(this.makeNodeHandle(n7), this);
            n4 = (int)(3 == n3 ? this.getShouldStripWhitespace() : 2 == n3) ? 1 : 0;
            this.pushShouldStripWhitespace((boolean)n4);
        }
        this.m_previous = -1;
        this.m_contextIndexes.push(this.m_prefixMappings.size());
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        this.charactersFlush();
        this.m_contextIndexes.quickPop(1);
        int n2 = this.m_contextIndexes.peek();
        if (n2 != this.m_prefixMappings.size()) {
            this.m_prefixMappings.setSize(n2);
        }
        int n3 = this.m_previous;
        this.m_previous = this.m_parents.pop();
        if (-1 == n3) {
            this.m_firstch.setElementAt(-1, this.m_previous);
        } else {
            this.m_nextsib.setElementAt(-1, n3);
        }
        this.popShouldStripWhitespace();
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_textPendingStart == -1) {
            this.m_textPendingStart = this.m_chars.size();
            this.m_coalescedTextType = this.m_textType;
        } else if (this.m_textType == 3) {
            this.m_coalescedTextType = 3;
        }
        this.m_chars.append(arrc, n2, n3);
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        this.characters(arrc, n2, n3);
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this.charactersFlush();
        int n2 = this.m_expandedNameTable.getExpandedTypeID(null, string, 7);
        int n3 = this.m_valuesOrPrefixes.stringToIndex(string2);
        this.m_previous = this.addNode(7, n2, this.m_parents.peek(), this.m_previous, n3, false);
    }

    public void skippedEntity(String string) throws SAXException {
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
        System.err.println(sAXParseException.getMessage());
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    public void elementDecl(String string, String string2) throws SAXException {
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        this.m_insideDTD = true;
    }

    public void endDTD() throws SAXException {
        this.m_insideDTD = false;
    }

    public void startEntity(String string) throws SAXException {
    }

    public void endEntity(String string) throws SAXException {
    }

    public void startCDATA() throws SAXException {
        this.m_textType = 4;
    }

    public void endCDATA() throws SAXException {
        this.m_textType = 3;
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_insideDTD) {
            return;
        }
        this.charactersFlush();
        int n4 = this.m_expandedNameTable.getExpandedTypeID(8);
        int n5 = this.m_valuesOrPrefixes.stringToIndex(new String(arrc, n2, n3));
        this.m_previous = this.addNode(8, n4, this.m_parents.peek(), this.m_previous, n5, false);
    }

    public void setProperty(String string, Object object) {
    }

    public SourceLocator getSourceLocatorFor(int n2) {
        if (this.m_useSourceLocationProperty) {
            n2 = this.makeNodeIdentity(n2);
            return new NodeLocator(null, this.m_sourceSystemId.elementAt(n2), this.m_sourceLine.elementAt(n2), this.m_sourceColumn.elementAt(n2));
        }
        if (this.m_locator != null) {
            return new NodeLocator(null, this.m_locator.getSystemId(), -1, -1);
        }
        if (this.m_systemId != null) {
            return new NodeLocator(null, this.m_systemId, -1, -1);
        }
        return null;
    }

    public String getFixedNames(int n2) {
        return m_fixednames[n2];
    }
}

