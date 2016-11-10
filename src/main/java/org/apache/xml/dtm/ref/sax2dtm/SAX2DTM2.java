/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref.sax2dtm;

import java.util.Vector;
import javax.xml.transform.Source;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMStringPool;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.ExtendedType;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringDefault;
import org.apache.xml.utils.XMLStringFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class SAX2DTM2
extends SAX2DTM {
    private int[] m_exptype_map0;
    private int[] m_nextsib_map0;
    private int[] m_firstch_map0;
    private int[] m_parent_map0;
    private int[][] m_exptype_map;
    private int[][] m_nextsib_map;
    private int[][] m_firstch_map;
    private int[][] m_parent_map;
    protected ExtendedType[] m_extendedTypes;
    protected Vector m_values;
    private int m_valueIndex = 0;
    private int m_maxNodeIndex;
    protected int m_SHIFT;
    protected int m_MASK;
    protected int m_blocksize;
    protected static final int TEXT_LENGTH_BITS = 10;
    protected static final int TEXT_OFFSET_BITS = 21;
    protected static final int TEXT_LENGTH_MAX = 1023;
    protected static final int TEXT_OFFSET_MAX = 2097151;
    protected boolean m_buildIdIndex = true;
    private static final String EMPTY_STR = "";
    private static final XMLString EMPTY_XML_STR = new XMLStringDefault("");

    public SAX2DTM2(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl) {
        this(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, 512, true, true, false);
    }

    public SAX2DTM2(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl, int n3, boolean bl2, boolean bl3, boolean bl4) {
        super(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl, n3, bl2, bl4);
        int n4 = 0;
        while ((n3 >>>= 1) != 0) {
            ++n4;
        }
        this.m_blocksize = 1 << n4;
        this.m_SHIFT = n4;
        this.m_MASK = this.m_blocksize - 1;
        this.m_buildIdIndex = bl3;
        this.m_values = new Vector(32, 512);
        this.m_maxNodeIndex = 65536;
        this.m_exptype_map0 = this.m_exptype.getMap0();
        this.m_nextsib_map0 = this.m_nextsib.getMap0();
        this.m_firstch_map0 = this.m_firstch.getMap0();
        this.m_parent_map0 = this.m_parent.getMap0();
    }

    public final int _exptype(int n2) {
        return this.m_exptype.elementAt(n2);
    }

    public final int _exptype2(int n2) {
        if (n2 < this.m_blocksize) {
            return this.m_exptype_map0[n2];
        }
        return this.m_exptype_map[n2 >>> this.m_SHIFT][n2 & this.m_MASK];
    }

    public final int _nextsib2(int n2) {
        if (n2 < this.m_blocksize) {
            return this.m_nextsib_map0[n2];
        }
        return this.m_nextsib_map[n2 >>> this.m_SHIFT][n2 & this.m_MASK];
    }

    public final int _firstch2(int n2) {
        if (n2 < this.m_blocksize) {
            return this.m_firstch_map0[n2];
        }
        return this.m_firstch_map[n2 >>> this.m_SHIFT][n2 & this.m_MASK];
    }

    public final int _parent2(int n2) {
        if (n2 < this.m_blocksize) {
            return this.m_parent_map0[n2];
        }
        return this.m_parent_map[n2 >>> this.m_SHIFT][n2 & this.m_MASK];
    }

    public final int _type2(int n2) {
        int n3 = n2 < this.m_blocksize ? this.m_exptype_map0[n2] : this.m_exptype_map[n2 >>> this.m_SHIFT][n2 & this.m_MASK];
        if (-1 != n3) {
            return this.m_extendedTypes[n3].getNodeType();
        }
        return -1;
    }

    public final int getExpandedTypeID2(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 != -1) {
            if (n3 < this.m_blocksize) {
                return this.m_exptype_map0[n3];
            }
            return this.m_exptype_map[n3 >>> this.m_SHIFT][n3 & this.m_MASK];
        }
        return -1;
    }

    public final int _exptype2Type(int n2) {
        if (-1 != n2) {
            return this.m_extendedTypes[n2].getNodeType();
        }
        return -1;
    }

    public int getIdForNamespace(String string) {
        int n2 = this.m_values.indexOf(string);
        if (n2 < 0) {
            this.m_values.addElement(string);
            return this.m_valueIndex++;
        }
        return n2;
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        int n2;
        int n3;
        String string4;
        int n4;
        this.charactersFlush();
        int n5 = this.m_expandedNameTable.getExpandedTypeID(string, string2, 1);
        int n6 = string3.length() != string2.length() ? this.m_valuesOrPrefixes.stringToIndex(string3) : 0;
        int n7 = this.addNode(1, n5, this.m_parents.peek(), this.m_previous, n6, true);
        if (this.m_indexing) {
            this.indexNode(n5, n7);
        }
        this.m_parents.push(n7);
        int n8 = this.m_contextIndexes.peek();
        int n9 = this.m_prefixMappings.size();
        if (!this.m_pastFirstElement) {
            string4 = "xml";
            String string5 = "http://www.w3.org/XML/1998/namespace";
            n5 = this.m_expandedNameTable.getExpandedTypeID(null, string4, 13);
            this.m_values.addElement(string5);
            n2 = this.m_valueIndex++;
            this.addNode(13, n5, n7, -1, n2, false);
            this.m_pastFirstElement = true;
        }
        for (n3 = n8; n3 < n9; n3 += 2) {
            string4 = (String)this.m_prefixMappings.elementAt(n3);
            if (string4 == null) continue;
            String string6 = (String)this.m_prefixMappings.elementAt(n3 + 1);
            n5 = this.m_expandedNameTable.getExpandedTypeID(null, string4, 13);
            this.m_values.addElement(string6);
            ++this.m_valueIndex;
            this.addNode(13, n5, n7, -1, n4, false);
        }
        n3 = attributes.getLength();
        for (n2 = 0; n2 < n3; ++n2) {
            int n10;
            int n11;
            String string7 = attributes.getURI(n2);
            String string8 = attributes.getQName(n2);
            String string9 = attributes.getValue(n2);
            String string10 = attributes.getLocalName(n2);
            if (null != string8 && (string8.equals("xmlns") || string8.startsWith("xmlns:"))) {
                string4 = this.getPrefix(string8, string7);
                if (this.declAlreadyDeclared(string4)) continue;
                n10 = 13;
            } else {
                n10 = 2;
                if (this.m_buildIdIndex && attributes.getType(n2).equalsIgnoreCase("ID")) {
                    this.setIDAttribute(string9, n7);
                }
            }
            if (null == string9) {
                string9 = "";
            }
            this.m_values.addElement(string9);
            ++this.m_valueIndex;
            if (string10.length() != string8.length()) {
                n6 = this.m_valuesOrPrefixes.stringToIndex(string8);
                int n12 = this.m_data.size();
                this.m_data.addElement(n6);
                this.m_data.addElement(n11);
                n11 = - n12;
            }
            n5 = this.m_expandedNameTable.getExpandedTypeID(string7, string10, n10);
            this.addNode(n10, n5, n7, -1, n11, false);
        }
        if (null != this.m_wsfilter) {
            n2 = this.m_wsfilter.getShouldStripSpace(this.makeNodeHandle(n7), this);
            n4 = 3 == n2 ? this.getShouldStripWhitespace() : (2 == n2 ? 1 : 0);
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
        this.m_previous = this.m_parents.pop();
        this.popShouldStripWhitespace();
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_insideDTD) {
            return;
        }
        this.charactersFlush();
        this.m_values.addElement(new String(arrc, n2, n3));
        int n4 = this.m_valueIndex++;
        this.m_previous = this.addNode(8, 8, this.m_parents.peek(), this.m_previous, n4, false);
    }

    public void startDocument() throws SAXException {
        int n2 = this.addNode(9, 9, -1, -1, 0, true);
        this.m_parents.push(n2);
        this.m_previous = -1;
        this.m_contextIndexes.push(this.m_prefixMappings.size());
    }

    public void endDocument() throws SAXException {
        super.endDocument();
        this.m_exptype.addElement(-1);
        this.m_parent.addElement(-1);
        this.m_nextsib.addElement(-1);
        this.m_firstch.addElement(-1);
        this.m_extendedTypes = this.m_expandedNameTable.getExtendedTypes();
        this.m_exptype_map = this.m_exptype.getMap();
        this.m_nextsib_map = this.m_nextsib.getMap();
        this.m_firstch_map = this.m_firstch.getMap();
        this.m_parent_map = this.m_parent.getMap();
    }

    protected final int addNode(int n2, int n3, int n4, int n5, int n6, boolean bl) {
        int n7;
        if ((n7 = this.m_size++) == this.m_maxNodeIndex) {
            this.addNewDTMID(n7);
            this.m_maxNodeIndex += 65536;
        }
        this.m_firstch.addElement(-1);
        this.m_nextsib.addElement(-1);
        this.m_parent.addElement(n4);
        this.m_exptype.addElement(n3);
        this.m_dataOrQName.addElement(n6);
        if (this.m_prevsib != null) {
            this.m_prevsib.addElement(n5);
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
                if (-1 != n5) {
                    this.m_nextsib.setElementAt(n7, n5);
                    break;
                }
                if (-1 == n4) break;
                this.m_firstch.setElementAt(n7, n4);
            }
        }
        return n7;
    }

    protected final void charactersFlush() {
        if (this.m_textPendingStart >= 0) {
            int n2 = this.m_chars.size() - this.m_textPendingStart;
            boolean bl = false;
            if (this.getShouldStripWhitespace()) {
                bl = this.m_chars.isWhitespace(this.m_textPendingStart, n2);
            }
            if (bl) {
                this.m_chars.setLength(this.m_textPendingStart);
            } else if (n2 > 0) {
                if (n2 <= 1023 && this.m_textPendingStart <= 2097151) {
                    this.m_previous = this.addNode(this.m_coalescedTextType, 3, this.m_parents.peek(), this.m_previous, n2 + (this.m_textPendingStart << 10), false);
                } else {
                    int n3 = this.m_data.size();
                    this.m_previous = this.addNode(this.m_coalescedTextType, 3, this.m_parents.peek(), this.m_previous, - n3, false);
                    this.m_data.addElement(this.m_textPendingStart);
                    this.m_data.addElement(n2);
                }
            }
            this.m_textPendingStart = -1;
            this.m_coalescedTextType = 3;
            this.m_textType = 3;
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this.charactersFlush();
        int n2 = this.m_data.size();
        this.m_previous = this.addNode(7, 7, this.m_parents.peek(), this.m_previous, - n2, false);
        this.m_data.addElement(this.m_valuesOrPrefixes.stringToIndex(string));
        this.m_values.addElement(string2);
        this.m_data.addElement(this.m_valueIndex++);
    }

    public final int getFirstAttribute(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 == -1) {
            return -1;
        }
        int n4 = this._type2(n3);
        if (1 == n4) {
            do {
                if ((n4 = this._type2(++n3)) != 2) continue;
                return this.makeNodeHandle(n3);
            } while (13 == n4);
        }
        return -1;
    }

    protected int getFirstAttributeIdentity(int n2) {
        if (n2 == -1) {
            return -1;
        }
        int n3 = this._type2(n2);
        if (1 == n3) {
            do {
                if ((n3 = this._type2(++n2)) != 2) continue;
                return n2;
            } while (13 == n3);
        }
        return -1;
    }

    protected int getNextAttributeIdentity(int n2) {
        int n3;
        do {
            if ((n3 = this._type2(++n2)) != 2) continue;
            return n2;
        } while (n3 == 13);
        return -1;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected final int getTypedAttribute(int var1_1, int var2_2) {
        var3_3 = this.makeNodeIdentity(var1_1);
        if (var3_3 == -1) {
            return -1;
        }
        var4_4 = this._type2(var3_3);
        if (1 != var4_4) return -1;
        do lbl-1000: // 3 sources:
        {
            if ((var5_5 = this._exptype2(++var3_3)) == -1) return -1;
            var4_4 = this.m_extendedTypes[var5_5].getNodeType();
            if (var4_4 != 2) continue;
            if (var5_5 != var2_2) ** GOTO lbl-1000
            return this.makeNodeHandle(var3_3);
        } while (13 == var4_4);
        return -1;
    }

    public String getLocalName(int n2) {
        int n3 = this._exptype(this.makeNodeIdentity(n2));
        if (n3 == 7) {
            int n4 = this._dataOrQName(this.makeNodeIdentity(n2));
            n4 = this.m_data.elementAt(- n4);
            return this.m_valuesOrPrefixes.indexToString(n4);
        }
        return this.m_expandedNameTable.getLocalName(n3);
    }

    public final String getNodeNameX(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._exptype2(n3);
        if (n4 == 7) {
            int n5 = this._dataOrQName(n3);
            n5 = this.m_data.elementAt(- n5);
            return this.m_valuesOrPrefixes.indexToString(n5);
        }
        ExtendedType extendedType = this.m_extendedTypes[n4];
        if (extendedType.getNamespace().length() == 0) {
            return extendedType.getLocalName();
        }
        int n6 = this.m_dataOrQName.elementAt(n3);
        if (n6 == 0) {
            return extendedType.getLocalName();
        }
        if (n6 < 0) {
            n6 = - n6;
            n6 = this.m_data.elementAt(n6);
        }
        return this.m_valuesOrPrefixes.indexToString(n6);
    }

    public String getNodeName(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._exptype2(n3);
        ExtendedType extendedType = this.m_extendedTypes[n4];
        if (extendedType.getNamespace().length() == 0) {
            int n5 = extendedType.getNodeType();
            String string = extendedType.getLocalName();
            if (n5 == 13) {
                if (string.length() == 0) {
                    return "xmlns";
                }
                return "xmlns:" + string;
            }
            if (n5 == 7) {
                int n6 = this._dataOrQName(n3);
                n6 = this.m_data.elementAt(- n6);
                return this.m_valuesOrPrefixes.indexToString(n6);
            }
            if (string.length() == 0) {
                return this.getFixedNames(n5);
            }
            return string;
        }
        int n7 = this.m_dataOrQName.elementAt(n3);
        if (n7 == 0) {
            return extendedType.getLocalName();
        }
        if (n7 < 0) {
            n7 = - n7;
            n7 = this.m_data.elementAt(n7);
        }
        return this.m_valuesOrPrefixes.indexToString(n7);
    }

    public XMLString getStringValue(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 == -1) {
            return EMPTY_XML_STR;
        }
        int n4 = this._type2(n3);
        if (n4 == 1 || n4 == 9) {
            int n5 = n3;
            if (-1 != (n3 = this._firstch2(n3))) {
                int n6 = -1;
                int n7 = 0;
                do {
                    if ((n4 = this._exptype2(n3)) != 3 && n4 != 4) continue;
                    int n8 = this.m_dataOrQName.elementAt(n3);
                    if (n8 >= 0) {
                        if (-1 == n6) {
                            n6 = n8 >>> 10;
                        }
                        n7 += n8 & 1023;
                        continue;
                    }
                    if (-1 == n6) {
                        n6 = this.m_data.elementAt(- n8);
                    }
                    n7 += this.m_data.elementAt(- n8 + 1);
                } while (this._parent2(++n3) >= n5);
                if (n7 > 0) {
                    if (this.m_xstrf != null) {
                        return this.m_xstrf.newstr(this.m_chars, n6, n7);
                    }
                    return new XMLStringDefault(this.m_chars.getString(n6, n7));
                }
                return EMPTY_XML_STR;
            }
            return EMPTY_XML_STR;
        }
        if (3 == n4 || 4 == n4) {
            int n9 = this.m_dataOrQName.elementAt(n3);
            if (n9 >= 0) {
                if (this.m_xstrf != null) {
                    return this.m_xstrf.newstr(this.m_chars, n9 >>> 10, n9 & 1023);
                }
                return new XMLStringDefault(this.m_chars.getString(n9 >>> 10, n9 & 1023));
            }
            if (this.m_xstrf != null) {
                return this.m_xstrf.newstr(this.m_chars, this.m_data.elementAt(- n9), this.m_data.elementAt(- n9 + 1));
            }
            return new XMLStringDefault(this.m_chars.getString(this.m_data.elementAt(- n9), this.m_data.elementAt(- n9 + 1)));
        }
        int n10 = this.m_dataOrQName.elementAt(n3);
        if (n10 < 0) {
            n10 = - n10;
            n10 = this.m_data.elementAt(n10 + 1);
        }
        if (this.m_xstrf != null) {
            return this.m_xstrf.newstr((String)this.m_values.elementAt(n10));
        }
        return new XMLStringDefault((String)this.m_values.elementAt(n10));
    }

    public final String getStringValueX(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 == -1) {
            return "";
        }
        int n4 = this._type2(n3);
        if (n4 == 1 || n4 == 9) {
            int n5 = n3;
            if (-1 != (n3 = this._firstch2(n3))) {
                int n6 = -1;
                int n7 = 0;
                do {
                    if ((n4 = this._exptype2(n3)) != 3 && n4 != 4) continue;
                    int n8 = this.m_dataOrQName.elementAt(n3);
                    if (n8 >= 0) {
                        if (-1 == n6) {
                            n6 = n8 >>> 10;
                        }
                        n7 += n8 & 1023;
                        continue;
                    }
                    if (-1 == n6) {
                        n6 = this.m_data.elementAt(- n8);
                    }
                    n7 += this.m_data.elementAt(- n8 + 1);
                } while (this._parent2(++n3) >= n5);
                if (n7 > 0) {
                    return this.m_chars.getString(n6, n7);
                }
                return "";
            }
            return "";
        }
        if (3 == n4 || 4 == n4) {
            int n9 = this.m_dataOrQName.elementAt(n3);
            if (n9 >= 0) {
                return this.m_chars.getString(n9 >>> 10, n9 & 1023);
            }
            return this.m_chars.getString(this.m_data.elementAt(- n9), this.m_data.elementAt(- n9 + 1));
        }
        int n10 = this.m_dataOrQName.elementAt(n3);
        if (n10 < 0) {
            n10 = - n10;
            n10 = this.m_data.elementAt(n10 + 1);
        }
        return (String)this.m_values.elementAt(n10);
    }

    public String getStringValue() {
        int n2 = this._firstch2(0);
        if (n2 == -1) {
            return "";
        }
        if (this._exptype2(n2) == 3 && this._nextsib2(n2) == -1) {
            int n3 = this.m_dataOrQName.elementAt(n2);
            if (n3 >= 0) {
                return this.m_chars.getString(n3 >>> 10, n3 & 1023);
            }
            return this.m_chars.getString(this.m_data.elementAt(- n3), this.m_data.elementAt(- n3 + 1));
        }
        return this.getStringValueX(this.getDocument());
    }

    public final void dispatchCharactersEvents(int n2, ContentHandler contentHandler, boolean bl) throws SAXException {
        int n3 = this.makeNodeIdentity(n2);
        if (n3 == -1) {
            return;
        }
        int n4 = this._type2(n3);
        if (n4 == 1 || n4 == 9) {
            int n5 = n3;
            if (-1 != (n3 = this._firstch2(n3))) {
                int n6 = -1;
                int n7 = 0;
                do {
                    if ((n4 = this._exptype2(n3)) != 3 && n4 != 4) continue;
                    int n8 = this.m_dataOrQName.elementAt(n3);
                    if (n8 >= 0) {
                        if (-1 == n6) {
                            n6 = n8 >>> 10;
                        }
                        n7 += n8 & 1023;
                        continue;
                    }
                    if (-1 == n6) {
                        n6 = this.m_data.elementAt(- n8);
                    }
                    n7 += this.m_data.elementAt(- n8 + 1);
                } while (this._parent2(++n3) >= n5);
                if (n7 > 0) {
                    if (bl) {
                        this.m_chars.sendNormalizedSAXcharacters(contentHandler, n6, n7);
                    } else {
                        this.m_chars.sendSAXcharacters(contentHandler, n6, n7);
                    }
                }
            }
        } else if (3 == n4 || 4 == n4) {
            int n9 = this.m_dataOrQName.elementAt(n3);
            if (n9 >= 0) {
                if (bl) {
                    this.m_chars.sendNormalizedSAXcharacters(contentHandler, n9 >>> 10, n9 & 1023);
                } else {
                    this.m_chars.sendSAXcharacters(contentHandler, n9 >>> 10, n9 & 1023);
                }
            } else if (bl) {
                this.m_chars.sendNormalizedSAXcharacters(contentHandler, this.m_data.elementAt(- n9), this.m_data.elementAt(- n9 + 1));
            } else {
                this.m_chars.sendSAXcharacters(contentHandler, this.m_data.elementAt(- n9), this.m_data.elementAt(- n9 + 1));
            }
        } else {
            int n10 = this.m_dataOrQName.elementAt(n3);
            if (n10 < 0) {
                n10 = - n10;
                n10 = this.m_data.elementAt(n10 + 1);
            }
            String string = (String)this.m_values.elementAt(n10);
            if (bl) {
                FastStringBuffer.sendNormalizedSAXcharacters(string.toCharArray(), 0, string.length(), contentHandler);
            } else {
                contentHandler.characters(string.toCharArray(), 0, string.length());
            }
        }
    }

    public String getNodeValue(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        int n4 = this._type2(n3);
        if (n4 == 3 || n4 == 4) {
            int n5 = this._dataOrQName(n3);
            if (n5 > 0) {
                return this.m_chars.getString(n5 >>> 10, n5 & 1023);
            }
            return this.m_chars.getString(this.m_data.elementAt(- n5), this.m_data.elementAt(- n5 + 1));
        }
        if (1 == n4 || 11 == n4 || 9 == n4) {
            return null;
        }
        int n6 = this.m_dataOrQName.elementAt(n3);
        if (n6 < 0) {
            n6 = - n6;
            n6 = this.m_data.elementAt(n6 + 1);
        }
        return (String)this.m_values.elementAt(n6);
    }

    protected final void copyTextNode(int n2, SerializationHandler serializationHandler) throws SAXException {
        if (n2 != -1) {
            int n3 = this.m_dataOrQName.elementAt(n2);
            if (n3 >= 0) {
                this.m_chars.sendSAXcharacters(serializationHandler, n3 >>> 10, n3 & 1023);
            } else {
                this.m_chars.sendSAXcharacters(serializationHandler, this.m_data.elementAt(- n3), this.m_data.elementAt(- n3 + 1));
            }
        }
    }

    protected final String copyElement(int n2, int n3, SerializationHandler serializationHandler) throws SAXException {
        ExtendedType extendedType = this.m_extendedTypes[n3];
        String string = extendedType.getNamespace();
        String string2 = extendedType.getLocalName();
        if (string.length() == 0) {
            serializationHandler.startElement(string2);
            return string2;
        }
        int n4 = this.m_dataOrQName.elementAt(n2);
        if (n4 == 0) {
            serializationHandler.startElement(string2);
            serializationHandler.namespaceAfterStartElement("", string);
            return string2;
        }
        if (n4 < 0) {
            n4 = - n4;
            n4 = this.m_data.elementAt(n4);
        }
        String string3 = this.m_valuesOrPrefixes.indexToString(n4);
        serializationHandler.startElement(string3);
        int n5 = string3.indexOf(58);
        String string4 = n5 > 0 ? string3.substring(0, n5) : null;
        serializationHandler.namespaceAfterStartElement(string4, string);
        return string3;
    }

    protected final void copyNS(int n2, SerializationHandler serializationHandler, boolean bl) throws SAXException {
        int n3;
        if (this.m_namespaceDeclSetElements != null && this.m_namespaceDeclSetElements.size() == 1 && this.m_namespaceDeclSets != null && ((SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(0)).size() == 1) {
            return;
        }
        SuballocatedIntVector suballocatedIntVector = null;
        if (bl) {
            suballocatedIntVector = this.findNamespaceContext(n2);
            if (suballocatedIntVector == null || suballocatedIntVector.size() < 1) {
                return;
            }
            n3 = this.makeNodeIdentity(suballocatedIntVector.elementAt(0));
        } else {
            n3 = this.getNextNamespaceNode2(n2);
        }
        int n4 = 1;
        while (n3 != -1) {
            int n5 = this._exptype2(n3);
            String string = this.m_extendedTypes[n5].getLocalName();
            int n6 = this.m_dataOrQName.elementAt(n3);
            if (n6 < 0) {
                n6 = - n6;
                n6 = this.m_data.elementAt(n6 + 1);
            }
            String string2 = (String)this.m_values.elementAt(n6);
            serializationHandler.namespaceAfterStartElement(string, string2);
            if (bl) {
                if (n4 < suballocatedIntVector.size()) {
                    n3 = this.makeNodeIdentity(suballocatedIntVector.elementAt(n4));
                    ++n4;
                    continue;
                }
                return;
            }
            n3 = this.getNextNamespaceNode2(n3);
        }
    }

    protected final int getNextNamespaceNode2(int n2) {
        int n3;
        while ((n3 = this._type2(++n2)) == 2) {
        }
        if (n3 == 13) {
            return n2;
        }
        return -1;
    }

    protected final void copyAttributes(int n2, SerializationHandler serializationHandler) throws SAXException {
        int n3 = this.getFirstAttributeIdentity(n2);
        while (n3 != -1) {
            int n4 = this._exptype2(n3);
            this.copyAttribute(n3, n4, serializationHandler);
            n3 = this.getNextAttributeIdentity(n3);
        }
    }

    protected final void copyAttribute(int n2, int n3, SerializationHandler serializationHandler) throws SAXException {
        int n4;
        ExtendedType extendedType = this.m_extendedTypes[n3];
        String string = extendedType.getNamespace();
        String string2 = extendedType.getLocalName();
        String string3 = null;
        String string4 = null;
        int n5 = n4 = this._dataOrQName(n2);
        if (n4 <= 0) {
            int n6 = this.m_data.elementAt(- n4);
            n5 = this.m_data.elementAt(- n4 + 1);
            string4 = this.m_valuesOrPrefixes.indexToString(n6);
            int n7 = string4.indexOf(58);
            if (n7 > 0) {
                string3 = string4.substring(0, n7);
            }
        }
        if (string.length() != 0) {
            serializationHandler.namespaceAfterStartElement(string3, string);
        }
        String string5 = string3 != null ? string4 : string2;
        String string6 = (String)this.m_values.elementAt(n5);
        serializationHandler.addAttribute(string5, string6);
    }

    public final class TypedSingletonIterator
    extends DTMDefaultBaseIterators.SingletonIterator {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedSingletonIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public int next() {
            int n2 = this._currentNode;
            if (n2 == -1) {
                return -1;
            }
            this._currentNode = -1;
            if (this._nodeType >= 14 ? this.this$0._exptype2(this.this$0.makeNodeIdentity(n2)) == this._nodeType : this.this$0._type2(this.this$0.makeNodeIdentity(n2)) == this._nodeType) {
                return this.returnNode(n2);
            }
            return -1;
        }
    }

    public final class TypedDescendantIterator
    extends DescendantIterator {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedDescendantIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public int next() {
            int n2 = this._startNode;
            if (this._startNode == -1) {
                return -1;
            }
            int n3 = this._currentNode;
            int n4 = this._nodeType;
            if (n4 != 1) {
                int n5;
                do {
                    if (-1 != (n5 = this.this$0._exptype2(++n3)) && (this.this$0._parent2(n3) >= n2 || n2 == n3)) continue;
                    this._currentNode = -1;
                    return -1;
                } while (n5 != n4);
            } else if (n2 == 0) {
                int n6;
                do {
                    if (-1 != (n6 = this.this$0._exptype2(++n3))) continue;
                    this._currentNode = -1;
                    return -1;
                } while (n6 < 14 || this.this$0.m_extendedTypes[n6].getNodeType() != 1);
            } else {
                int n7;
                do {
                    if (-1 != (n7 = this.this$0._exptype2(++n3)) && (this.this$0._parent2(n3) >= n2 || n2 == n3)) continue;
                    this._currentNode = -1;
                    return -1;
                } while (n7 < 14 || this.this$0.m_extendedTypes[n7].getNodeType() != 1);
            }
            this._currentNode = n3;
            return this.returnNode(this.this$0.makeNodeHandle(n3));
        }
    }

    public class DescendantIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final SAX2DTM2 this$0;

        public DescendantIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                n2 = this.this$0.makeNodeIdentity(n2);
                this._startNode = n2--;
                if (this._includeSelf) {
                    // empty if block
                }
                this._currentNode = n2;
                return this.resetPosition();
            }
            return this;
        }

        protected final boolean isDescendant(int n2) {
            return this.this$0._parent2(n2) >= this._startNode || this._startNode == n2;
        }

        public int next() {
            int n2 = this._startNode;
            if (n2 == -1) {
                return -1;
            }
            if (this._includeSelf && this._currentNode + 1 == n2) {
                return this.returnNode(this.this$0.makeNodeHandle(++this._currentNode));
            }
            int n3 = this._currentNode;
            if (n2 == 0) {
                int n4;
                int n5;
                do {
                    if (-1 != (n4 = this.this$0._exptype2(++n3))) continue;
                    this._currentNode = -1;
                    return -1;
                } while (n4 == 3 || (n5 = this.this$0.m_extendedTypes[n4].getNodeType()) == 2 || n5 == 13);
            } else {
                int n6;
                do {
                    if (-1 != (n6 = this.this$0._type2(++n3)) && this.isDescendant(n3)) continue;
                    this._currentNode = -1;
                    return -1;
                } while (2 == n6 || 3 == n6 || 13 == n6);
            }
            this._currentNode = n3;
            return this.returnNode(this.this$0.makeNodeHandle(n3));
        }

        public DTMAxisIterator reset() {
            boolean bl = this._isRestartable;
            this._isRestartable = true;
            this.setStartNode(this.this$0.makeNodeHandle(this._startNode));
            this._isRestartable = bl;
            return this;
        }
    }

    public final class TypedAncestorIterator
    extends AncestorIterator {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedAncestorIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            this.m_realStartNode = n2;
            if (this._isRestartable) {
                int n3 = this.this$0.makeNodeIdentity(n2);
                this.m_size = 0;
                if (n3 == -1) {
                    this._currentNode = -1;
                    this.m_ancestorsPos = 0;
                    return this;
                }
                int n4 = this._nodeType;
                if (!this._includeSelf) {
                    n3 = this.this$0._parent2(n3);
                    n2 = this.this$0.makeNodeHandle(n3);
                }
                this._startNode = n2;
                if (n4 >= 14) {
                    while (n3 != -1) {
                        int n5 = this.this$0._exptype2(n3);
                        if (n5 == n4) {
                            if (this.m_size >= this.m_ancestors.length) {
                                int[] arrn = new int[this.m_size * 2];
                                System.arraycopy(this.m_ancestors, 0, arrn, 0, this.m_ancestors.length);
                                this.m_ancestors = arrn;
                            }
                            this.m_ancestors[this.m_size++] = this.this$0.makeNodeHandle(n3);
                        }
                        n3 = this.this$0._parent2(n3);
                    }
                } else {
                    while (n3 != -1) {
                        int n6 = this.this$0._exptype2(n3);
                        if (n6 < 14 && n6 == n4 || n6 >= 14 && this.this$0.m_extendedTypes[n6].getNodeType() == n4) {
                            if (this.m_size >= this.m_ancestors.length) {
                                int[] arrn = new int[this.m_size * 2];
                                System.arraycopy(this.m_ancestors, 0, arrn, 0, this.m_ancestors.length);
                                this.m_ancestors = arrn;
                            }
                            this.m_ancestors[this.m_size++] = this.this$0.makeNodeHandle(n3);
                        }
                        n3 = this.this$0._parent2(n3);
                    }
                }
                this.m_ancestorsPos = this.m_size - 1;
                this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
                return this.resetPosition();
            }
            return this;
        }

        public int getNodeByPosition(int n2) {
            if (n2 > 0 && n2 <= this.m_size) {
                return this.m_ancestors[n2 - 1];
            }
            return -1;
        }

        public int getLast() {
            return this.m_size;
        }
    }

    public class AncestorIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        int[] m_ancestors;
        int m_size;
        int m_ancestorsPos;
        int m_markedPos;
        int m_realStartNode;
        private final SAX2DTM2 this$0;

        public AncestorIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this.m_ancestors = new int[32];
            this.m_size = 0;
        }

        public int getStartNode() {
            return this.m_realStartNode;
        }

        public final boolean isReverse() {
            return true;
        }

        public DTMAxisIterator cloneIterator() {
            this._isRestartable = false;
            try {
                AncestorIterator ancestorIterator = (AncestorIterator)Object.super.clone();
                ancestorIterator._startNode = this._startNode;
                return ancestorIterator;
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
            }
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            this.m_realStartNode = n2;
            if (this._isRestartable) {
                int n3 = this.this$0.makeNodeIdentity(n2);
                this.m_size = 0;
                if (n3 == -1) {
                    this._currentNode = -1;
                    this.m_ancestorsPos = 0;
                    return this;
                }
                if (!this._includeSelf) {
                    n3 = this.this$0._parent2(n3);
                    n2 = this.this$0.makeNodeHandle(n3);
                }
                this._startNode = n2;
                while (n3 != -1) {
                    if (this.m_size >= this.m_ancestors.length) {
                        int[] arrn = new int[this.m_size * 2];
                        System.arraycopy(this.m_ancestors, 0, arrn, 0, this.m_ancestors.length);
                        this.m_ancestors = arrn;
                    }
                    this.m_ancestors[this.m_size++] = n2;
                    n3 = this.this$0._parent2(n3);
                    n2 = this.this$0.makeNodeHandle(n3);
                }
                this.m_ancestorsPos = this.m_size - 1;
                this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
                return this.resetPosition();
            }
            return this;
        }

        public DTMAxisIterator reset() {
            this.m_ancestorsPos = this.m_size - 1;
            this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
            return this.resetPosition();
        }

        public int next() {
            int n2;
            int n3 = this._currentNode;
            this._currentNode = (n2 = --this.m_ancestorsPos) >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
            return this.returnNode(n3);
        }

        public void setMark() {
            this.m_markedPos = this.m_ancestorsPos;
        }

        public void gotoMark() {
            this.m_ancestorsPos = this.m_markedPos;
            this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
        }
    }

    public final class TypedFollowingIterator
    extends FollowingIterator {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedFollowingIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public int next() {
            int n2;
            int n3 = this._nodeType;
            int n4 = this.this$0.makeNodeIdentity(this._currentNode);
            if (n3 >= 14) {
                do {
                    int n5;
                    int n6 = n2 = n4;
                    while ((n5 = this.this$0._type2(++n6)) != -1 && (2 == n5 || 13 == n5)) {
                    }
                    int n7 = n4 = n5 != -1 ? n6 : -1;
                } while (n2 != -1 && this.this$0._exptype2(n2) != n3);
            } else {
                do {
                    int n8;
                    int n9 = n2 = n4;
                    while ((n8 = this.this$0._type2(++n9)) != -1 && (2 == n8 || 13 == n8)) {
                    }
                    int n10 = n4 = n8 != -1 ? n9 : -1;
                } while (n2 != -1 && this.this$0._exptype2(n2) != n3 && this.this$0._type2(n2) != n3);
            }
            this._currentNode = this.this$0.makeNodeHandle(n4);
            return n2 == -1 ? -1 : this.returnNode(this.this$0.makeNodeHandle(n2));
        }
    }

    public class FollowingIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final SAX2DTM2 this$0;

        public FollowingIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                int n3;
                this._startNode = n2;
                int n4 = this.this$0._type2(n2 = this.this$0.makeNodeIdentity(n2));
                if ((2 == n4 || 13 == n4) && -1 != (n3 = this.this$0._firstch2(n2 = this.this$0._parent2(n2)))) {
                    this._currentNode = this.this$0.makeNodeHandle(n3);
                    return this.resetPosition();
                }
                do {
                    if (-1 != (n3 = this.this$0._nextsib2(n2))) continue;
                    n2 = this.this$0._parent2(n2);
                } while (-1 == n3 && -1 != n2);
                this._currentNode = this.this$0.makeNodeHandle(n3);
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2;
            int n3 = this._currentNode;
            int n4 = this.this$0.makeNodeIdentity(n3);
            do {
                if (-1 != (n2 = this.this$0._type2(++n4))) continue;
                this._currentNode = -1;
                return this.returnNode(n3);
            } while (2 == n2 || 13 == n2);
            this._currentNode = this.this$0.makeNodeHandle(n4);
            return this.returnNode(n3);
        }
    }

    public final class TypedPrecedingIterator
    extends PrecedingIterator {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedPrecedingIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public int next() {
            block5 : {
                var1_1 = this._currentNode;
                var2_2 = this._nodeType;
                if (var2_2 < 14) ** GOTO lbl15
                do lbl-1000: // 3 sources:
                {
                    ++var1_1;
                    if (this._sp < 0) {
                        var1_1 = -1;
                        break block5;
                    }
                    if (var1_1 < this._stack[this._sp]) continue;
                    if (--this._sp >= 0) ** GOTO lbl-1000
                    var1_1 = -1;
                    break block5;
                } while (this.this$0._exptype2(var1_1) != var2_2);
                ** GOTO lbl25
lbl15: // 1 sources:
                do lbl-1000: // 3 sources:
                {
                    ++var1_1;
                    if (this._sp < 0) {
                        var1_1 = -1;
                        break;
                    }
                    if (var1_1 < this._stack[this._sp]) continue;
                    if (--this._sp >= 0) ** GOTO lbl-1000
                    var1_1 = -1;
                    break;
                } while (!((var3_3 = this.this$0._exptype2(var1_1)) < 14 ? var3_3 == var2_2 : this.this$0.m_extendedTypes[var3_3].getNodeType() == var2_2));
            }
            this._currentNode = var1_1;
            if (var1_1 == -1) {
                return -1;
            }
            v0 = this.returnNode(this.this$0.makeNodeHandle(var1_1));
            return v0;
        }
    }

    public class PrecedingIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _maxAncestors = 8;
        protected int[] _stack;
        protected int _sp;
        protected int _oldsp;
        protected int _markedsp;
        protected int _markedNode;
        protected int _markedDescendant;
        private final SAX2DTM2 this$0;

        public PrecedingIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._maxAncestors = 8;
            this._stack = new int[8];
        }

        public boolean isReverse() {
            return true;
        }

        public DTMAxisIterator cloneIterator() {
            this._isRestartable = false;
            try {
                PrecedingIterator precedingIterator = (PrecedingIterator)Object.super.clone();
                int[] arrn = new int[this._stack.length];
                System.arraycopy(this._stack, 0, arrn, 0, this._stack.length);
                precedingIterator._stack = arrn;
                return precedingIterator;
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
            }
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                if (this.this$0._type2(n2 = this.this$0.makeNodeIdentity(n2)) == 2) {
                    n2 = this.this$0._parent2(n2);
                }
                this._startNode = n2;
                int n3 = 0;
                this._stack[0] = n2;
                int n4 = n2;
                while ((n4 = this.this$0._parent2(n4)) != -1) {
                    if (++n3 == this._stack.length) {
                        int[] arrn = new int[n3 * 2];
                        System.arraycopy(this._stack, 0, arrn, 0, n3);
                        this._stack = arrn;
                    }
                    this._stack[n3] = n4;
                }
                if (n3 > 0) {
                    --n3;
                }
                this._currentNode = this._stack[n3];
                this._oldsp = this._sp = n3;
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            ++this._currentNode;
            while (this._sp >= 0) {
                if (this._currentNode < this._stack[this._sp]) {
                    int n2 = this.this$0._type2(this._currentNode);
                    if (n2 != 2 && n2 != 13) {
                        return this.returnNode(this.this$0.makeNodeHandle(this._currentNode));
                    }
                } else {
                    --this._sp;
                }
                ++this._currentNode;
            }
            return -1;
        }

        public DTMAxisIterator reset() {
            this._sp = this._oldsp;
            return this.resetPosition();
        }

        public void setMark() {
            this._markedsp = this._sp;
            this._markedNode = this._currentNode;
            this._markedDescendant = this._stack[0];
        }

        public void gotoMark() {
            this._sp = this._markedsp;
            this._currentNode = this._markedNode;
        }
    }

    public final class TypedPrecedingSiblingIterator
    extends PrecedingSiblingIterator {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedPrecedingSiblingIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public int next() {
            int n2 = this._currentNode;
            int n3 = this._nodeType;
            int n4 = this._startNodeID;
            if (n3 != 1) {
                while (n2 != -1 && n2 != n4 && this.this$0._exptype2(n2) != n3) {
                    n2 = this.this$0._nextsib2(n2);
                }
            } else {
                while (n2 != -1 && n2 != n4 && this.this$0._exptype2(n2) < 14) {
                    n2 = this.this$0._nextsib2(n2);
                }
            }
            if (n2 == -1 || n2 == n4) {
                this._currentNode = -1;
                return -1;
            }
            this._currentNode = this.this$0._nextsib2(n2);
            return this.returnNode(this.this$0.makeNodeHandle(n2));
        }

        public int getLast() {
            if (this._last != -1) {
                return this._last;
            }
            this.setMark();
            int n2 = this._currentNode;
            int n3 = this._nodeType;
            int n4 = this._startNodeID;
            int n5 = 0;
            if (n3 != 1) {
                while (n2 != -1 && n2 != n4) {
                    if (this.this$0._exptype2(n2) == n3) {
                        ++n5;
                    }
                    n2 = this.this$0._nextsib2(n2);
                }
            } else {
                while (n2 != -1 && n2 != n4) {
                    if (this.this$0._exptype2(n2) >= 14) {
                        ++n5;
                    }
                    n2 = this.this$0._nextsib2(n2);
                }
            }
            this.gotoMark();
            this._last = n5;
            return this._last;
        }
    }

    public class PrecedingSiblingIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        protected int _startNodeID;
        private final SAX2DTM2 this$0;

        public PrecedingSiblingIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
        }

        public boolean isReverse() {
            return true;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._startNodeID = this.this$0.makeNodeIdentity(n2);
                if ((n2 = this._startNodeID) == -1) {
                    this._currentNode = n2;
                    return this.resetPosition();
                }
                int n3 = this.this$0._type2(n2);
                if (2 == n3 || 13 == n3) {
                    this._currentNode = n2;
                } else {
                    this._currentNode = this.this$0._parent2(n2);
                    this._currentNode = -1 != this._currentNode ? this.this$0._firstch2(this._currentNode) : n2;
                }
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            if (this._currentNode == this._startNodeID || this._currentNode == -1) {
                return -1;
            }
            int n2 = this._currentNode;
            this._currentNode = this.this$0._nextsib2(n2);
            return this.returnNode(this.this$0.makeNodeHandle(n2));
        }
    }

    public final class TypedAttributeIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedAttributeIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.this$0.getTypedAttribute(n2, this._nodeType);
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            this._currentNode = -1;
            return this.returnNode(n2);
        }
    }

    public final class AttributeIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final SAX2DTM2 this$0;

        public AttributeIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.this$0.getFirstAttributeIdentity(this.this$0.makeNodeIdentity(n2));
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            if (n2 != -1) {
                this._currentNode = this.this$0.getNextAttributeIdentity(n2);
                return this.returnNode(this.this$0.makeNodeHandle(n2));
            }
            return -1;
        }
    }

    public final class TypedFollowingSiblingIterator
    extends FollowingSiblingIterator {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedFollowingSiblingIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public int next() {
            if (this._currentNode == -1) {
                return -1;
            }
            int n2 = this._currentNode;
            int n3 = this._nodeType;
            if (n3 != 1) {
                while ((n2 = this.this$0._nextsib2(n2)) != -1 && this.this$0._exptype2(n2) != n3) {
                }
            } else {
                while ((n2 = this.this$0._nextsib2(n2)) != -1 && this.this$0._exptype2(n2) < 14) {
                }
            }
            this._currentNode = n2;
            return n2 == -1 ? -1 : this.returnNode(this.this$0.makeNodeHandle(n2));
        }
    }

    public class FollowingSiblingIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final SAX2DTM2 this$0;

        public FollowingSiblingIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = this.this$0.makeNodeIdentity(n2);
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            this._currentNode = this._currentNode == -1 ? -1 : this.this$0._nextsib2(this._currentNode);
            return this.returnNode(this.this$0.makeNodeHandle(this._currentNode));
        }
    }

    public class TypedRootIterator
    extends DTMDefaultBaseIterators.RootIterator {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedRootIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public int next() {
            if (this._startNode == this._currentNode) {
                return -1;
            }
            int n2 = this._startNode;
            int n3 = this.this$0._exptype2(this.this$0.makeNodeIdentity(n2));
            this._currentNode = n2;
            if (this._nodeType >= 14 ? this._nodeType == n3 : (n3 < 14 ? n3 == this._nodeType : this.this$0.m_extendedTypes[n3].getNodeType() == this._nodeType)) {
                return this.returnNode(n2);
            }
            return -1;
        }
    }

    public final class TypedChildrenIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _nodeType;
        private final SAX2DTM2 this$0;

        public TypedChildrenIterator(SAX2DTM2 sAX2DTM2, int n2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = n2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = n2 == -1 ? -1 : this.this$0._firstch2(this.this$0.makeNodeIdentity(this._startNode));
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            if (n2 == -1) {
                return -1;
            }
            int n3 = this._nodeType;
            if (n3 != 1) {
                while (n2 != -1 && this.this$0._exptype2(n2) != n3) {
                    n2 = this.this$0._nextsib2(n2);
                }
            } else {
                int n4;
                while (n2 != -1 && (n4 = this.this$0._exptype2(n2)) < 14) {
                    n2 = this.this$0._nextsib2(n2);
                }
            }
            if (n2 == -1) {
                this._currentNode = -1;
                return -1;
            }
            this._currentNode = this.this$0._nextsib2(n2);
            return this.returnNode(this.this$0.makeNodeHandle(n2));
        }

        public int getNodeByPosition(int n2) {
            if (n2 <= 0) {
                return -1;
            }
            int n3 = this._currentNode;
            int n4 = 0;
            int n5 = this._nodeType;
            if (n5 != 1) {
                while (n3 != -1) {
                    if (this.this$0._exptype2(n3) == n5 && ++n4 == n2) {
                        return this.this$0.makeNodeHandle(n3);
                    }
                    n3 = this.this$0._nextsib2(n3);
                }
                return -1;
            }
            while (n3 != -1) {
                if (this.this$0._exptype2(n3) >= 14 && ++n4 == n2) {
                    return this.this$0.makeNodeHandle(n3);
                }
                n3 = this.this$0._nextsib2(n3);
            }
            return -1;
        }
    }

    public final class ParentIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private int _nodeType;
        private final SAX2DTM2 this$0;

        public ParentIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
            this._nodeType = -1;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = n2 != -1 ? this.this$0._parent2(this.this$0.makeNodeIdentity(n2)) : -1;
                return this.resetPosition();
            }
            return this;
        }

        public DTMAxisIterator setNodeType(int n2) {
            this._nodeType = n2;
            return this;
        }

        public int next() {
            int n2 = this._currentNode;
            if (n2 == -1) {
                return -1;
            }
            if (this._nodeType == -1) {
                this._currentNode = -1;
                return this.returnNode(this.this$0.makeNodeHandle(n2));
            }
            if (this._nodeType >= 14) {
                if (this._nodeType == this.this$0._exptype2(n2)) {
                    this._currentNode = -1;
                    return this.returnNode(this.this$0.makeNodeHandle(n2));
                }
            } else if (this._nodeType == this.this$0._type2(n2)) {
                this._currentNode = -1;
                return this.returnNode(this.this$0.makeNodeHandle(n2));
            }
            return -1;
        }
    }

    public final class ChildrenIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final SAX2DTM2 this$0;

        public ChildrenIterator(SAX2DTM2 sAX2DTM2) {
            super(sAX2DTM2);
            this.this$0 = sAX2DTM2;
        }

        public DTMAxisIterator setStartNode(int n2) {
            if (n2 == 0) {
                n2 = this.this$0.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = n2;
                this._currentNode = n2 == -1 ? -1 : this.this$0._firstch2(this.this$0.makeNodeIdentity(n2));
                return this.resetPosition();
            }
            return this;
        }

        public int next() {
            if (this._currentNode != -1) {
                int n2 = this._currentNode;
                this._currentNode = this.this$0._nextsib2(n2);
                return this.returnNode(this.this$0.makeNodeHandle(n2));
            }
            return -1;
        }
    }

}

