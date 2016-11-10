/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.xml.serializer.AttributesImplSerializer;
import org.apache.xml.serializer.CharInfo;
import org.apache.xml.serializer.ElemContext;
import org.apache.xml.serializer.ElemDesc;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.OutputPropertyUtils;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.ToStream;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ToHTMLStream
extends ToStream {
    protected boolean m_inDTD = false;
    private boolean m_inBlockElem = false;
    private final CharInfo m_htmlcharInfo = CharInfo.getCharInfo(CharInfo.HTML_ENTITIES_RESOURCE, "html");
    static final Trie m_elementFlags = new Trie();
    private static final ElemDesc m_dummy;
    private boolean m_specialEscapeURLs = true;
    private boolean m_omitMetaTag = false;
    private Trie m_htmlInfo = new Trie(m_elementFlags);

    static void initTagReference(Trie trie) {
        trie.put("BASEFONT", new ElemDesc(2));
        trie.put("FRAME", new ElemDesc(10));
        trie.put("FRAMESET", new ElemDesc(8));
        trie.put("NOFRAMES", new ElemDesc(8));
        trie.put("ISINDEX", new ElemDesc(10));
        trie.put("APPLET", new ElemDesc(2097152));
        trie.put("CENTER", new ElemDesc(8));
        trie.put("DIR", new ElemDesc(8));
        trie.put("MENU", new ElemDesc(8));
        trie.put("TT", new ElemDesc(4096));
        trie.put("I", new ElemDesc(4096));
        trie.put("B", new ElemDesc(4096));
        trie.put("BIG", new ElemDesc(4096));
        trie.put("SMALL", new ElemDesc(4096));
        trie.put("EM", new ElemDesc(8192));
        trie.put("STRONG", new ElemDesc(8192));
        trie.put("DFN", new ElemDesc(8192));
        trie.put("CODE", new ElemDesc(8192));
        trie.put("SAMP", new ElemDesc(8192));
        trie.put("KBD", new ElemDesc(8192));
        trie.put("VAR", new ElemDesc(8192));
        trie.put("CITE", new ElemDesc(8192));
        trie.put("ABBR", new ElemDesc(8192));
        trie.put("ACRONYM", new ElemDesc(8192));
        trie.put("SUP", new ElemDesc(98304));
        trie.put("SUB", new ElemDesc(98304));
        trie.put("SPAN", new ElemDesc(98304));
        trie.put("BDO", new ElemDesc(98304));
        trie.put("BR", new ElemDesc(98314));
        trie.put("BODY", new ElemDesc(8));
        trie.put("ADDRESS", new ElemDesc(56));
        trie.put("DIV", new ElemDesc(56));
        trie.put("A", new ElemDesc(32768));
        trie.put("MAP", new ElemDesc(98312));
        trie.put("AREA", new ElemDesc(10));
        trie.put("LINK", new ElemDesc(131082));
        trie.put("IMG", new ElemDesc(2195458));
        trie.put("OBJECT", new ElemDesc(2326528));
        trie.put("PARAM", new ElemDesc(2));
        trie.put("HR", new ElemDesc(58));
        trie.put("P", new ElemDesc(56));
        trie.put("H1", new ElemDesc(262152));
        trie.put("H2", new ElemDesc(262152));
        trie.put("H3", new ElemDesc(262152));
        trie.put("H4", new ElemDesc(262152));
        trie.put("H5", new ElemDesc(262152));
        trie.put("H6", new ElemDesc(262152));
        trie.put("PRE", new ElemDesc(1048584));
        trie.put("Q", new ElemDesc(98304));
        trie.put("BLOCKQUOTE", new ElemDesc(56));
        trie.put("INS", new ElemDesc(0));
        trie.put("DEL", new ElemDesc(0));
        trie.put("DL", new ElemDesc(56));
        trie.put("DT", new ElemDesc(8));
        trie.put("DD", new ElemDesc(8));
        trie.put("OL", new ElemDesc(524296));
        trie.put("UL", new ElemDesc(524296));
        trie.put("LI", new ElemDesc(8));
        trie.put("FORM", new ElemDesc(8));
        trie.put("LABEL", new ElemDesc(16384));
        trie.put("INPUT", new ElemDesc(18434));
        trie.put("SELECT", new ElemDesc(18432));
        trie.put("OPTGROUP", new ElemDesc(0));
        trie.put("OPTION", new ElemDesc(0));
        trie.put("TEXTAREA", new ElemDesc(18432));
        trie.put("FIELDSET", new ElemDesc(24));
        trie.put("LEGEND", new ElemDesc(0));
        trie.put("BUTTON", new ElemDesc(18432));
        trie.put("TABLE", new ElemDesc(56));
        trie.put("CAPTION", new ElemDesc(8));
        trie.put("THEAD", new ElemDesc(8));
        trie.put("TFOOT", new ElemDesc(8));
        trie.put("TBODY", new ElemDesc(8));
        trie.put("COLGROUP", new ElemDesc(8));
        trie.put("COL", new ElemDesc(10));
        trie.put("TR", new ElemDesc(8));
        trie.put("TH", new ElemDesc(0));
        trie.put("TD", new ElemDesc(0));
        trie.put("HEAD", new ElemDesc(4194312));
        trie.put("TITLE", new ElemDesc(8));
        trie.put("BASE", new ElemDesc(10));
        trie.put("META", new ElemDesc(131082));
        trie.put("STYLE", new ElemDesc(131336));
        trie.put("SCRIPT", new ElemDesc(229632));
        trie.put("NOSCRIPT", new ElemDesc(56));
        trie.put("HTML", new ElemDesc(8388616));
        trie.put("FONT", new ElemDesc(4096));
        trie.put("S", new ElemDesc(4096));
        trie.put("STRIKE", new ElemDesc(4096));
        trie.put("U", new ElemDesc(4096));
        trie.put("NOBR", new ElemDesc(4096));
        trie.put("IFRAME", new ElemDesc(56));
        trie.put("LAYER", new ElemDesc(56));
        trie.put("ILAYER", new ElemDesc(56));
        ElemDesc elemDesc = (ElemDesc)trie.get("a");
        elemDesc.setAttr("HREF", 2);
        elemDesc.setAttr("NAME", 2);
        elemDesc = (ElemDesc)trie.get("area");
        elemDesc.setAttr("HREF", 2);
        elemDesc.setAttr("NOHREF", 4);
        elemDesc = (ElemDesc)trie.get("base");
        elemDesc.setAttr("HREF", 2);
        elemDesc = (ElemDesc)trie.get("button");
        elemDesc.setAttr("DISABLED", 4);
        elemDesc = (ElemDesc)trie.get("blockquote");
        elemDesc.setAttr("CITE", 2);
        elemDesc = (ElemDesc)trie.get("del");
        elemDesc.setAttr("CITE", 2);
        elemDesc = (ElemDesc)trie.get("dir");
        elemDesc.setAttr("COMPACT", 4);
        elemDesc = (ElemDesc)trie.get("div");
        elemDesc.setAttr("SRC", 2);
        elemDesc.setAttr("NOWRAP", 4);
        elemDesc = (ElemDesc)trie.get("dl");
        elemDesc.setAttr("COMPACT", 4);
        elemDesc = (ElemDesc)trie.get("form");
        elemDesc.setAttr("ACTION", 2);
        elemDesc = (ElemDesc)trie.get("frame");
        elemDesc.setAttr("SRC", 2);
        elemDesc.setAttr("LONGDESC", 2);
        elemDesc.setAttr("NORESIZE", 4);
        elemDesc = (ElemDesc)trie.get("head");
        elemDesc.setAttr("PROFILE", 2);
        elemDesc = (ElemDesc)trie.get("hr");
        elemDesc.setAttr("NOSHADE", 4);
        elemDesc = (ElemDesc)trie.get("iframe");
        elemDesc.setAttr("SRC", 2);
        elemDesc.setAttr("LONGDESC", 2);
        elemDesc = (ElemDesc)trie.get("ilayer");
        elemDesc.setAttr("SRC", 2);
        elemDesc = (ElemDesc)trie.get("img");
        elemDesc.setAttr("SRC", 2);
        elemDesc.setAttr("LONGDESC", 2);
        elemDesc.setAttr("USEMAP", 2);
        elemDesc.setAttr("ISMAP", 4);
        elemDesc = (ElemDesc)trie.get("input");
        elemDesc.setAttr("SRC", 2);
        elemDesc.setAttr("USEMAP", 2);
        elemDesc.setAttr("CHECKED", 4);
        elemDesc.setAttr("DISABLED", 4);
        elemDesc.setAttr("ISMAP", 4);
        elemDesc.setAttr("READONLY", 4);
        elemDesc = (ElemDesc)trie.get("ins");
        elemDesc.setAttr("CITE", 2);
        elemDesc = (ElemDesc)trie.get("layer");
        elemDesc.setAttr("SRC", 2);
        elemDesc = (ElemDesc)trie.get("link");
        elemDesc.setAttr("HREF", 2);
        elemDesc = (ElemDesc)trie.get("menu");
        elemDesc.setAttr("COMPACT", 4);
        elemDesc = (ElemDesc)trie.get("object");
        elemDesc.setAttr("CLASSID", 2);
        elemDesc.setAttr("CODEBASE", 2);
        elemDesc.setAttr("DATA", 2);
        elemDesc.setAttr("ARCHIVE", 2);
        elemDesc.setAttr("USEMAP", 2);
        elemDesc.setAttr("DECLARE", 4);
        elemDesc = (ElemDesc)trie.get("ol");
        elemDesc.setAttr("COMPACT", 4);
        elemDesc = (ElemDesc)trie.get("optgroup");
        elemDesc.setAttr("DISABLED", 4);
        elemDesc = (ElemDesc)trie.get("option");
        elemDesc.setAttr("SELECTED", 4);
        elemDesc.setAttr("DISABLED", 4);
        elemDesc = (ElemDesc)trie.get("q");
        elemDesc.setAttr("CITE", 2);
        elemDesc = (ElemDesc)trie.get("script");
        elemDesc.setAttr("SRC", 2);
        elemDesc.setAttr("FOR", 2);
        elemDesc.setAttr("DEFER", 4);
        elemDesc = (ElemDesc)trie.get("select");
        elemDesc.setAttr("DISABLED", 4);
        elemDesc.setAttr("MULTIPLE", 4);
        elemDesc = (ElemDesc)trie.get("table");
        elemDesc.setAttr("NOWRAP", 4);
        elemDesc = (ElemDesc)trie.get("td");
        elemDesc.setAttr("NOWRAP", 4);
        elemDesc = (ElemDesc)trie.get("textarea");
        elemDesc.setAttr("DISABLED", 4);
        elemDesc.setAttr("READONLY", 4);
        elemDesc = (ElemDesc)trie.get("th");
        elemDesc.setAttr("NOWRAP", 4);
        elemDesc = (ElemDesc)trie.get("tr");
        elemDesc.setAttr("NOWRAP", 4);
        elemDesc = (ElemDesc)trie.get("ul");
        elemDesc.setAttr("COMPACT", 4);
    }

    public void setOutputFormat(Properties properties) {
        String string = properties.getProperty("{http://xml.apache.org/xalan}use-url-escaping");
        if (string != null) {
            this.m_specialEscapeURLs = OutputPropertyUtils.getBooleanProperty("{http://xml.apache.org/xalan}use-url-escaping", properties);
        }
        if ((string = properties.getProperty("{http://xml.apache.org/xalan}omit-meta-tag")) != null) {
            this.m_omitMetaTag = OutputPropertyUtils.getBooleanProperty("{http://xml.apache.org/xalan}omit-meta-tag", properties);
        }
        super.setOutputFormat(properties);
    }

    public static final ElemDesc getElemDesc(String string) {
        Object object = m_elementFlags.get(string);
        if (null != object) {
            return (ElemDesc)object;
        }
        return m_dummy;
    }

    private ElemDesc getElemDesc2(String string) {
        Object object = this.m_htmlInfo.get2(string);
        if (null != object) {
            return (ElemDesc)object;
        }
        return m_dummy;
    }

    public ToHTMLStream() {
        this.m_doIndent = true;
        this.m_charInfo = this.m_htmlcharInfo;
        this.m_prefixMap = new NamespaceMappings();
    }

    protected void startDocumentInternal() throws SAXException {
        super.startDocumentInternal();
        this.m_needToCallStartDocument = false;
        this.m_needToOutputDocTypeDecl = true;
        this.m_startNewLine = false;
        this.setOmitXMLDeclaration(true);
    }

    private void outputDocTypeDecl(String string) throws SAXException {
        if (this.m_needToOutputDocTypeDecl) {
            String string2 = this.getDoctypeSystem();
            String string3 = this.getDoctypePublic();
            if (null != string2 || null != string3) {
                Writer writer = this.m_writer;
                try {
                    writer.write("<!DOCTYPE ");
                    writer.write(string);
                    if (null != string3) {
                        writer.write(" PUBLIC \"");
                        writer.write(string3);
                        writer.write(34);
                    }
                    if (null != string2) {
                        if (null == string3) {
                            writer.write(" SYSTEM \"");
                        } else {
                            writer.write(" \"");
                        }
                        writer.write(string2);
                        writer.write(34);
                    }
                    writer.write(62);
                    this.outputLineSep();
                }
                catch (IOException iOException) {
                    throw new SAXException(iOException);
                }
            }
        }
        this.m_needToOutputDocTypeDecl = false;
    }

    public final void endDocument() throws SAXException {
        this.flushPending();
        if (this.m_doIndent && !this.m_isprevtext) {
            try {
                this.outputLineSep();
            }
            catch (IOException iOException) {
                throw new SAXException(iOException);
            }
        }
        this.flushWriter();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        Object object;
        ElemContext elemContext = this.m_elemContext;
        if (elemContext.m_startTagOpen) {
            this.closeStartTag();
            elemContext.m_startTagOpen = false;
        } else if (this.m_cdataTagOpen) {
            this.closeCDATA();
            this.m_cdataTagOpen = false;
        } else if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (this.m_needToOutputDocTypeDecl) {
            object = string3;
            if (object == null || object.length() == 0) {
                object = string2;
            }
            this.outputDocTypeDecl((String)object);
        }
        if (null != string && string.length() > 0) {
            super.startElement(string, string2, string3, attributes);
            return;
        }
        try {
            object = this.getElemDesc2(string3);
            int n2 = object.getFlags();
            if (this.m_doIndent) {
                boolean bl;
                boolean bl2 = bl = (n2 & 8) != 0;
                if (this.m_ispreserve) {
                    this.m_ispreserve = false;
                } else if (null != elemContext.m_elementName && (!this.m_inBlockElem || bl)) {
                    this.m_startNewLine = true;
                    this.indent();
                }
                boolean bl3 = this.m_inBlockElem = !bl;
            }
            if (attributes != null) {
                this.addAttributes(attributes);
            }
            this.m_isprevtext = false;
            Writer writer = this.m_writer;
            writer.write(60);
            writer.write(string3);
            if (this.m_tracer != null) {
                this.firePseudoAttributes();
            }
            if ((n2 & 2) != 0) {
                this.m_elemContext = elemContext.push();
                this.m_elemContext.m_elementName = string3;
                this.m_elemContext.m_elementDesc = object;
                return;
            }
            this.m_elemContext = elemContext = elemContext.push(string, string2, string3);
            elemContext.m_elementDesc = object;
            boolean bl = elemContext.m_isRaw = (n2 & 256) != 0;
            if ((n2 & 4194304) != 0) {
                this.closeStartTag();
                elemContext.m_startTagOpen = false;
                if (!this.m_omitMetaTag) {
                    if (this.m_doIndent) {
                        this.indent();
                    }
                    writer.write("<META http-equiv=\"Content-Type\" content=\"text/html; charset=");
                    String string4 = this.getEncoding();
                    String string5 = Encodings.getMimeEncoding(string4);
                    writer.write(string5);
                    writer.write("\">");
                }
            }
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public final void endElement(String string, String string2, String string3) throws SAXException {
        if (this.m_cdataTagOpen) {
            this.closeCDATA();
        }
        if (null != string && string.length() > 0) {
            super.endElement(string, string2, string3);
            return;
        }
        try {
            int n2;
            boolean bl;
            ElemContext elemContext = this.m_elemContext;
            ElemDesc elemDesc = elemContext.m_elementDesc;
            int n3 = elemDesc.getFlags();
            boolean bl2 = bl = (n3 & 2) != 0;
            if (this.m_doIndent) {
                boolean bl3 = (n3 & 8) != 0;
                n2 = 0;
                if (this.m_ispreserve) {
                    this.m_ispreserve = false;
                } else if (this.m_doIndent && (!this.m_inBlockElem || bl3)) {
                    this.m_startNewLine = true;
                    n2 = 1;
                }
                if (!elemContext.m_startTagOpen && n2 != 0) {
                    this.indent(elemContext.m_currentElemDepth - 1);
                }
                this.m_inBlockElem = !bl3;
            }
            Writer writer = this.m_writer;
            if (!elemContext.m_startTagOpen) {
                writer.write("</");
                writer.write(string3);
                writer.write(62);
            } else {
                if (this.m_tracer != null) {
                    super.fireStartElem(string3);
                }
                if ((n2 = this.m_attributes.getLength()) > 0) {
                    this.processAttributes(this.m_writer, n2);
                    this.m_attributes.clear();
                }
                if (!bl) {
                    writer.write("></");
                    writer.write(string3);
                    writer.write(62);
                } else {
                    writer.write(62);
                }
            }
            if ((n3 & 2097152) != 0) {
                this.m_ispreserve = true;
            }
            this.m_isprevtext = false;
            if (this.m_tracer != null) {
                super.fireEndElem(string3);
            }
            if (bl) {
                this.m_elemContext = elemContext.m_prev;
                return;
            }
            if (!elemContext.m_startTagOpen && this.m_doIndent && !this.m_preserves.isEmpty()) {
                this.m_preserves.pop();
            }
            this.m_elemContext = elemContext.m_prev;
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    protected void processAttribute(Writer writer, String string, String string2, ElemDesc elemDesc) throws IOException {
        writer.write(32);
        if ((string2.length() == 0 || string2.equalsIgnoreCase(string)) && elemDesc != null && elemDesc.isAttrFlagSet(string, 4)) {
            writer.write(string);
        } else {
            writer.write(string);
            writer.write("=\"");
            if (elemDesc != null && elemDesc.isAttrFlagSet(string, 2)) {
                this.writeAttrURI(writer, string2, this.m_specialEscapeURLs);
            } else {
                this.writeAttrString(writer, string2, this.getEncoding());
            }
            writer.write(34);
        }
    }

    private static String makeHHString(int n2) {
        String string = Integer.toHexString(n2).toUpperCase();
        if (string.length() == 1) {
            string = "0" + string;
        }
        return string;
    }

    public void writeAttrURI(Writer writer, String string, boolean bl) throws IOException {
        int n2 = string.length();
        if (n2 > this.m_attrBuff.length) {
            this.m_attrBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_attrBuff, 0);
        char[] arrc = this.m_attrBuff;
        int n3 = 0;
        int n4 = 0;
        char c2 = '\u0000';
        for (int i2 = 0; i2 < n2; ++i2) {
            c2 = arrc[i2];
            if (c2 < ' ' || c2 > '~') {
                if (n4 > 0) {
                    writer.write(arrc, n3, n4);
                    n4 = 0;
                }
                if (bl) {
                    int n5;
                    int n6;
                    int n7;
                    if (c2 <= '') {
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(c2));
                    } else if (c2 <= '\u07ff') {
                        n7 = c2 >> 6 | 192;
                        n5 = c2 & 63 | 128;
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n7));
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n5));
                    } else if (Encodings.isHighUTF16Surrogate(c2)) {
                        n7 = c2 & 1023;
                        n5 = (n7 & 960) >> 6;
                        n6 = n5 + 1;
                        int n8 = (n7 & 60) >> 2;
                        int n9 = (n7 & 3) << 4 & 48;
                        c2 = arrc[++i2];
                        int n10 = c2 & 1023;
                        int n11 = n10 & 63;
                        int n12 = 240 | n6 >> 2;
                        int n13 = 128 | (n6 & 3) << 4 & 48 | n8;
                        int n14 = 128 | (n9 |= (n10 & 960) >> 6);
                        int n15 = 128 | n11;
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n12));
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n13));
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n14));
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n15));
                    } else {
                        n7 = c2 >> 12 | 224;
                        n5 = (c2 & 4032) >> 6 | 128;
                        n6 = c2 & 63 | 128;
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n7));
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n5));
                        writer.write(37);
                        writer.write(ToHTMLStream.makeHHString(n6));
                    }
                } else if (this.escapingNotNeeded(c2)) {
                    writer.write(c2);
                } else {
                    writer.write("&#");
                    writer.write(Integer.toString(c2));
                    writer.write(59);
                }
                n3 = i2 + 1;
                continue;
            }
            if (c2 == '\"') {
                if (n4 > 0) {
                    writer.write(arrc, n3, n4);
                    n4 = 0;
                }
                if (bl) {
                    writer.write("%22");
                } else {
                    writer.write("&quot;");
                }
                n3 = i2 + 1;
                continue;
            }
            if (c2 == '&') {
                if (n4 > 0) {
                    writer.write(arrc, n3, n4);
                    n4 = 0;
                }
                writer.write("&amp;");
                n3 = i2 + 1;
                continue;
            }
            ++n4;
        }
        if (n4 > 1) {
            if (n3 == 0) {
                writer.write(string);
            } else {
                writer.write(arrc, n3, n4);
            }
        } else if (n4 == 1) {
            writer.write(c2);
        }
    }

    public void writeAttrString(Writer writer, String string, String string2) throws IOException {
        int n2 = string.length();
        if (n2 > this.m_attrBuff.length) {
            this.m_attrBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_attrBuff, 0);
        char[] arrc = this.m_attrBuff;
        int n3 = 0;
        int n4 = 0;
        char c2 = '\u0000';
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5;
            c2 = arrc[i2];
            if (this.escapingNotNeeded(c2) && !this.m_charInfo.shouldMapAttrChar(c2)) {
                ++n4;
                continue;
            }
            if ('<' == c2 || '>' == c2) {
                ++n4;
                continue;
            }
            if ('&' == c2 && i2 + 1 < n2 && '{' == arrc[i2 + 1]) {
                ++n4;
                continue;
            }
            if (n4 > 0) {
                writer.write(arrc, n3, n4);
                n4 = 0;
            }
            if (i2 != (n5 = this.accumDefaultEntity(writer, c2, i2, arrc, n2, false, true))) {
                i2 = n5 - 1;
            } else {
                String string3;
                if (Encodings.isHighUTF16Surrogate(c2)) {
                    this.writeUTF16Surrogate(c2, arrc, i2, n2);
                    ++i2;
                }
                if (null != (string3 = this.m_charInfo.getOutputStringForChar(c2))) {
                    writer.write(string3);
                } else if (this.escapingNotNeeded(c2)) {
                    writer.write(c2);
                } else {
                    writer.write("&#");
                    writer.write(Integer.toString(c2));
                    writer.write(59);
                }
            }
            n3 = i2 + 1;
        }
        if (n4 > 1) {
            if (n3 == 0) {
                writer.write(string);
            } else {
                writer.write(arrc, n3, n4);
            }
        } else if (n4 == 1) {
            writer.write(c2);
        }
    }

    public final void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_elemContext.m_isRaw) {
            try {
                if (this.m_elemContext.m_startTagOpen) {
                    this.closeStartTag();
                    this.m_elemContext.m_startTagOpen = false;
                }
                this.m_ispreserve = true;
                this.writeNormalizedChars(arrc, n2, n3, false, this.m_lineSepUse);
                if (this.m_tracer != null) {
                    super.fireCharEvent(arrc, n2, n3);
                }
                return;
            }
            catch (IOException iOException) {
                throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), iOException);
            }
        }
        super.characters(arrc, n2, n3);
    }

    public final void cdata(char[] arrc, int n2, int n3) throws SAXException {
        if (null != this.m_elemContext.m_elementName && (this.m_elemContext.m_elementName.equalsIgnoreCase("SCRIPT") || this.m_elemContext.m_elementName.equalsIgnoreCase("STYLE"))) {
            try {
                if (this.m_elemContext.m_startTagOpen) {
                    this.closeStartTag();
                    this.m_elemContext.m_startTagOpen = false;
                }
                this.m_ispreserve = true;
                if (this.shouldIndent()) {
                    this.indent();
                }
                this.writeNormalizedChars(arrc, n2, n3, true, this.m_lineSepUse);
            }
            catch (IOException iOException) {
                throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), iOException);
            }
        } else {
            super.cdata(arrc, n2, n3);
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this.flushPending();
        if (string.equals("javax.xml.transform.disable-output-escaping")) {
            this.startNonEscaping();
        } else if (string.equals("javax.xml.transform.enable-output-escaping")) {
            this.endNonEscaping();
        } else {
            try {
                if (this.m_elemContext.m_startTagOpen) {
                    this.closeStartTag();
                    this.m_elemContext.m_startTagOpen = false;
                } else if (this.m_cdataTagOpen) {
                    this.closeCDATA();
                } else if (this.m_needToCallStartDocument) {
                    this.startDocumentInternal();
                }
                if (this.m_needToOutputDocTypeDecl) {
                    this.outputDocTypeDecl("html");
                }
                if (this.shouldIndent()) {
                    this.indent();
                }
                Writer writer = this.m_writer;
                writer.write("<?");
                writer.write(string);
                if (string2.length() > 0 && !Character.isSpaceChar(string2.charAt(0))) {
                    writer.write(32);
                }
                writer.write(string2);
                writer.write(62);
                if (this.m_elemContext.m_currentElemDepth <= 0) {
                    this.outputLineSep();
                }
                this.m_startNewLine = true;
            }
            catch (IOException iOException) {
                throw new SAXException(iOException);
            }
        }
        if (this.m_tracer != null) {
            super.fireEscapingEvent(string, string2);
        }
    }

    public final void entityReference(String string) throws SAXException {
        try {
            Writer writer = this.m_writer;
            writer.write(38);
            writer.write(string);
            writer.write(59);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public final void endElement(String string) throws SAXException {
        this.endElement(null, null, string);
    }

    public void processAttributes(Writer writer, int n2) throws IOException, SAXException {
        for (int i2 = 0; i2 < n2; ++i2) {
            this.processAttribute(writer, this.m_attributes.getQName(i2), this.m_attributes.getValue(i2), this.m_elemContext.m_elementDesc);
        }
    }

    protected void closeStartTag() throws SAXException {
        try {
            int n2;
            if (this.m_tracer != null) {
                super.fireStartElem(this.m_elemContext.m_elementName);
            }
            if ((n2 = this.m_attributes.getLength()) > 0) {
                this.processAttributes(this.m_writer, n2);
                this.m_attributes.clear();
            }
            this.m_writer.write(62);
            if (this.m_CdataElems != null) {
                this.m_elemContext.m_isCdataSection = this.isCdataSection();
            }
            if (this.m_doIndent) {
                this.m_isprevtext = false;
                this.m_preserves.push(this.m_ispreserve);
            }
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void namespaceAfterStartElement(String string, String string2) throws SAXException {
        String string3;
        if (this.m_elemContext.m_elementURI == null && (string3 = ToHTMLStream.getPrefixPart(this.m_elemContext.m_elementName)) == null && "".equals(string)) {
            this.m_elemContext.m_elementURI = string2;
        }
        this.startPrefixMapping(string, string2, false);
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        this.m_inDTD = true;
        super.startDTD(string, string2, string3);
    }

    public void endDTD() throws SAXException {
        this.m_inDTD = false;
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
    }

    public void elementDecl(String string, String string2) throws SAXException {
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_inDTD) {
            return;
        }
        if (this.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        } else if (this.m_cdataTagOpen) {
            this.closeCDATA();
        } else if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
        }
        if (this.m_needToOutputDocTypeDecl) {
            this.outputDocTypeDecl("html");
        }
        super.comment(arrc, n2, n3);
    }

    public boolean reset() {
        boolean bl = super.reset();
        if (!bl) {
            return false;
        }
        this.resetToHTMLStream();
        return true;
    }

    private void resetToHTMLStream() {
        this.m_inBlockElem = false;
        this.m_inDTD = false;
        this.m_omitMetaTag = false;
        this.m_specialEscapeURLs = true;
    }

    static {
        ToHTMLStream.initTagReference(m_elementFlags);
        m_dummy = new ElemDesc(8);
    }

    static class Trie {
        final Node m_Root;
        private char[] m_charBuffer = new char[0];
        private final boolean m_lowerCaseOnly;

        public Trie() {
            this.m_Root = new Node();
            this.m_lowerCaseOnly = false;
        }

        public Object put(String string, Object object) {
            int n2 = string.length();
            if (n2 > this.m_charBuffer.length) {
                this.m_charBuffer = new char[n2];
            }
            Node node = this.m_Root;
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node2 = node.m_nextChar[Character.toLowerCase(string.charAt(i2))];
                if (node2 != null) {
                    node = node2;
                    continue;
                }
                while (i2 < n2) {
                    Node node3 = new Node();
                    if (this.m_lowerCaseOnly) {
                        node.m_nextChar[Character.toLowerCase((char)string.charAt((int)i2))] = node3;
                    } else {
                        node.m_nextChar[Character.toUpperCase((char)string.charAt((int)i2))] = node3;
                        node.m_nextChar[Character.toLowerCase((char)string.charAt((int)i2))] = node3;
                    }
                    node = node3;
                    ++i2;
                }
                break;
            }
            Object object2 = node.m_Value;
            node.m_Value = object;
            return object2;
        }

        public Object get(String string) {
            int n2 = string.length();
            if (this.m_charBuffer.length < n2) {
                return null;
            }
            Node node = this.m_Root;
            switch (n2) {
                case 0: {
                    return null;
                }
                case 1: {
                    char c2 = string.charAt(0);
                    if (c2 < '' && (node = node.m_nextChar[c2]) != null) {
                        return node.m_Value;
                    }
                    return null;
                }
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                char c3 = string.charAt(i2);
                if ('' <= c3) {
                    return null;
                }
                node = node.m_nextChar[c3];
                if (node != null) continue;
                return null;
            }
            return node.m_Value;
        }

        public Trie(Trie trie) {
            this.m_Root = trie.m_Root;
            this.m_lowerCaseOnly = trie.m_lowerCaseOnly;
            int n2 = trie.getLongestKeyLength();
            this.m_charBuffer = new char[n2];
        }

        public Object get2(String string) {
            int n2 = string.length();
            if (this.m_charBuffer.length < n2) {
                return null;
            }
            Node node = this.m_Root;
            switch (n2) {
                case 0: {
                    return null;
                }
                case 1: {
                    char c2 = string.charAt(0);
                    if (c2 < '' && (node = node.m_nextChar[c2]) != null) {
                        return node.m_Value;
                    }
                    return null;
                }
            }
            string.getChars(0, n2, this.m_charBuffer, 0);
            for (int i2 = 0; i2 < n2; ++i2) {
                char c3 = this.m_charBuffer[i2];
                if ('' <= c3) {
                    return null;
                }
                node = node.m_nextChar[c3];
                if (node != null) continue;
                return null;
            }
            return node.m_Value;
        }

        public int getLongestKeyLength() {
            return this.m_charBuffer.length;
        }

        private static class Node {
            final Node[] m_nextChar = new Node[128];
            Object m_Value = null;

            Node() {
            }
        }

    }

}

