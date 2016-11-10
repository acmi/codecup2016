/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.AttributesImplSerializer;
import org.apache.xml.serializer.CharInfo;
import org.apache.xml.serializer.ElemContext;
import org.apache.xml.serializer.EncodingInfo;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SecuritySupport;
import org.apache.xml.serializer.SerializerBase;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.SerializerTraceWriter;
import org.apache.xml.serializer.TreeWalker;
import org.apache.xml.serializer.WriterChain;
import org.apache.xml.serializer.WriterToASCI;
import org.apache.xml.serializer.WriterToUTF8Buffered;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class ToStream
extends SerializerBase {
    protected BoolStack m_disableOutputEscapingStates = new BoolStack();
    EncodingInfo m_encodingInfo = new EncodingInfo(null, null, '\u0000');
    protected BoolStack m_preserves = new BoolStack();
    protected boolean m_ispreserve = false;
    protected boolean m_isprevtext = false;
    private static final char[] s_systemLineSep = SecuritySupport.getSystemProperty("line.separator").toCharArray();
    protected char[] m_lineSep = s_systemLineSep;
    protected boolean m_lineSepUse = true;
    protected int m_lineSepLen = this.m_lineSep.length;
    protected CharInfo m_charInfo;
    boolean m_shouldFlush = true;
    protected boolean m_spaceBeforeClose = false;
    boolean m_startNewLine;
    protected boolean m_inDoctype = false;
    boolean m_isUTF8 = false;
    protected boolean m_cdataStartCalled = false;
    private boolean m_expandDTDEntities = true;
    protected boolean m_escaping = true;
    OutputStream m_outputStream;
    private boolean m_writer_set_by_user;

    protected void closeCDATA() throws SAXException {
        try {
            this.m_writer.write("]]>");
            this.m_cdataTagOpen = false;
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void serialize(Node node) throws IOException {
        try {
            TreeWalker treeWalker = new TreeWalker(this);
            treeWalker.traverse(node);
        }
        catch (SAXException sAXException) {
            throw new WrappedRuntimeException(sAXException);
        }
    }

    protected final void flushWriter() throws SAXException {
        Writer writer = this.m_writer;
        if (null != writer) {
            try {
                if (writer instanceof WriterToUTF8Buffered) {
                    if (this.m_shouldFlush) {
                        ((WriterToUTF8Buffered)writer).flush();
                    } else {
                        ((WriterToUTF8Buffered)writer).flushBuffer();
                    }
                }
                if (writer instanceof WriterToASCI) {
                    if (this.m_shouldFlush) {
                        writer.flush();
                    }
                } else {
                    writer.flush();
                }
            }
            catch (IOException iOException) {
                throw new SAXException(iOException);
            }
        }
    }

    public OutputStream getOutputStream() {
        return this.m_outputStream;
    }

    public void elementDecl(String string, String string2) throws SAXException {
        if (this.m_inExternalDTD) {
            return;
        }
        try {
            Writer writer = this.m_writer;
            this.DTDprolog();
            writer.write("<!ELEMENT ");
            writer.write(string);
            writer.write(32);
            writer.write(string2);
            writer.write(62);
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
        if (this.m_inExternalDTD) {
            return;
        }
        try {
            this.DTDprolog();
            this.outputEntityDecl(string, string2);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    void outputEntityDecl(String string, String string2) throws IOException {
        Writer writer = this.m_writer;
        writer.write("<!ENTITY ");
        writer.write(string);
        writer.write(" \"");
        writer.write(string2);
        writer.write("\">");
        writer.write(this.m_lineSep, 0, this.m_lineSepLen);
    }

    protected final void outputLineSep() throws IOException {
        this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
    }

    void setProp(String string, String string2, boolean bl) {
        if (string2 != null) {
            char c2 = ToStream.getFirstCharLocName(string);
            switch (c2) {
                case 'c': {
                    if (!"cdata-section-elements".equals(string)) break;
                    String string3 = string2;
                    this.addCdataSectionElements(string3);
                    break;
                }
                case 'd': {
                    if ("doctype-system".equals(string)) {
                        this.m_doctypeSystem = string2;
                        break;
                    }
                    if (!"doctype-public".equals(string)) break;
                    this.m_doctypePublic = string2;
                    if (!string2.startsWith("-//W3C//DTD XHTML")) break;
                    this.m_spaceBeforeClose = true;
                    break;
                }
                case 'e': {
                    Object object;
                    Object object2;
                    Object object3;
                    String string4 = string2;
                    if (!"encoding".equals(string)) break;
                    String string5 = Encodings.getMimeEncoding(string2);
                    if (string5 != null) {
                        super.setProp("mime-name", string5, bl);
                    }
                    String string6 = this.getOutputPropertyNonDefault("encoding");
                    String string7 = this.getOutputPropertyDefault("encoding");
                    if ((!bl || string7 != null && string7.equalsIgnoreCase(string4)) && (bl || string6 != null && string6.equalsIgnoreCase(string4))) break;
                    EncodingInfo encodingInfo = Encodings.getEncodingInfo(string4);
                    if (string4 != null && encodingInfo.name == null) {
                        object2 = Utils.messages.createMessage("ER_ENCODING_NOT_SUPPORTED", new Object[]{string4});
                        object3 = "Warning: encoding \"" + string4 + "\" not supported, using " + "UTF-8";
                        try {
                            object = super.getTransformer();
                            if (object != null) {
                                ErrorListener errorListener = object.getErrorListener();
                                if (null != errorListener && this.m_sourceLocator != null) {
                                    errorListener.warning(new TransformerException((String)object2, this.m_sourceLocator));
                                    errorListener.warning(new TransformerException((String)object3, this.m_sourceLocator));
                                } else {
                                    System.out.println((String)object2);
                                    System.out.println((String)object3);
                                }
                            } else {
                                System.out.println((String)object2);
                                System.out.println((String)object3);
                            }
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                        string4 = "UTF-8";
                        string2 = "UTF-8";
                        encodingInfo = Encodings.getEncodingInfo(string4);
                    }
                    if (bl && string6 != null) break;
                    this.m_encodingInfo = encodingInfo;
                    if (string4 != null) {
                        this.m_isUTF8 = string4.equals("UTF-8");
                    }
                    if ((object2 = this.getOutputStream()) == null) break;
                    object3 = this.getWriter();
                    object = this.getOutputProperty("encoding");
                    if (object3 != null && this.m_writer_set_by_user || string4.equalsIgnoreCase((String)object)) break;
                    super.setProp(string, string2, bl);
                    this.setOutputStreamInternal((OutputStream)object2, false);
                    break;
                }
                case 'i': {
                    boolean bl2;
                    if ("{http://xml.apache.org/xalan}indent-amount".equals(string)) {
                        this.setIndentAmount(Integer.parseInt(string2));
                        break;
                    }
                    if (!"indent".equals(string)) break;
                    this.m_doIndent = bl2 = "yes".equals(string2);
                    break;
                }
                case 'l': {
                    if (!"{http://xml.apache.org/xalan}line-separator".equals(string)) break;
                    this.m_lineSep = string2.toCharArray();
                    this.m_lineSepLen = this.m_lineSep.length;
                    break;
                }
                case 'm': {
                    if (!"media-type".equals(string)) break;
                    this.m_mediatype = string2;
                    break;
                }
                case 'o': {
                    boolean bl3;
                    if (!"omit-xml-declaration".equals(string)) break;
                    this.m_shouldNotWriteXMLHeader = bl3 = "yes".equals(string2);
                    break;
                }
                case 's': {
                    if (!"standalone".equals(string)) break;
                    if (bl) {
                        this.setStandaloneInternal(string2);
                        break;
                    }
                    this.m_standaloneWasSpecified = true;
                    this.setStandaloneInternal(string2);
                    break;
                }
                case 'v': {
                    if (!"version".equals(string)) break;
                    this.m_version = string2;
                    break;
                }
            }
            super.setProp(string, string2, bl);
        }
    }

    public void setOutputFormat(Properties properties) {
        String string;
        Object object;
        boolean bl = this.m_shouldFlush;
        if (properties != null) {
            object = properties.propertyNames();
            while (object.hasMoreElements()) {
                string = (String)object.nextElement();
                String string2 = properties.getProperty(string);
                String string3 = (String)properties.get(string);
                if (string3 == null && string2 != null) {
                    this.setOutputPropertyDefault(string, string2);
                }
                if (string3 == null) continue;
                this.setOutputProperty(string, string3);
            }
        }
        if (null != (object = (String)properties.get("{http://xml.apache.org/xalan}entities"))) {
            string = (String)properties.get("method");
            this.m_charInfo = CharInfo.getCharInfo((String)object, string);
        }
        this.m_shouldFlush = bl;
    }

    public Properties getOutputFormat() {
        Object object;
        String string;
        Properties properties = new Properties();
        Object object2 = this.getOutputPropDefaultKeys();
        Object object3 = object2.iterator();
        while (object3.hasNext()) {
            object = (String)object3.next();
            string = this.getOutputPropertyDefault((String)object);
            properties.put(object, string);
        }
        object2 = new Properties(properties);
        object3 = this.getOutputPropKeys();
        object = object3.iterator();
        while (object.hasNext()) {
            string = (String)object.next();
            String string2 = this.getOutputPropertyNonDefault(string);
            if (string2 == null) continue;
            object2.put(string, string2);
        }
        return object2;
    }

    public void setWriter(Writer writer) {
        this.setWriterInternal(writer, true);
    }

    private void setWriterInternal(Writer writer, boolean bl) {
        this.m_writer_set_by_user = bl;
        this.m_writer = writer;
        if (this.m_tracer != null) {
            boolean bl2 = true;
            Writer writer2 = this.m_writer;
            while (writer2 instanceof WriterChain) {
                if (writer2 instanceof SerializerTraceWriter) {
                    bl2 = false;
                    break;
                }
                writer2 = ((WriterChain)((Object)writer2)).getWriter();
            }
            if (bl2) {
                this.m_writer = new SerializerTraceWriter(this.m_writer, this.m_tracer);
            }
        }
    }

    public void setOutputStream(OutputStream outputStream) {
        this.setOutputStreamInternal(outputStream, true);
    }

    private void setOutputStreamInternal(OutputStream outputStream, boolean bl) {
        this.m_outputStream = outputStream;
        String string = this.getOutputProperty("encoding");
        if ("UTF-8".equalsIgnoreCase(string)) {
            this.setWriterInternal(new WriterToUTF8Buffered(outputStream), false);
        } else if ("WINDOWS-1250".equals(string) || "US-ASCII".equals(string) || "ASCII".equals(string)) {
            this.setWriterInternal(new WriterToASCI(outputStream), false);
        } else if (string != null) {
            Writer writer = null;
            try {
                writer = Encodings.getWriter(outputStream, string);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                writer = null;
            }
            if (writer == null) {
                System.out.println("Warning: encoding \"" + string + "\" not supported" + ", using " + "UTF-8");
                string = "UTF-8";
                this.setEncoding(string);
                try {
                    writer = Encodings.getWriter(outputStream, string);
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                }
            }
            this.setWriterInternal(writer, false);
        } else {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            this.setWriterInternal(outputStreamWriter, false);
        }
    }

    public boolean setEscaping(boolean bl) {
        boolean bl2 = this.m_escaping;
        this.m_escaping = bl;
        return bl2;
    }

    protected void indent(int n2) throws IOException {
        if (this.m_startNewLine) {
            this.outputLineSep();
        }
        if (this.m_indentAmount > 0) {
            this.printSpace(n2 * this.m_indentAmount);
        }
    }

    protected void indent() throws IOException {
        this.indent(this.m_elemContext.m_currentElemDepth);
    }

    private void printSpace(int n2) throws IOException {
        Writer writer = this.m_writer;
        for (int i2 = 0; i2 < n2; ++i2) {
            writer.write(32);
        }
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
        if (this.m_inExternalDTD) {
            return;
        }
        try {
            Writer writer = this.m_writer;
            this.DTDprolog();
            writer.write("<!ATTLIST ");
            writer.write(string);
            writer.write(32);
            writer.write(string2);
            writer.write(32);
            writer.write(string3);
            if (string4 != null) {
                writer.write(32);
                writer.write(string4);
            }
            writer.write(62);
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public Writer getWriter() {
        return this.m_writer;
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
        try {
            this.DTDprolog();
            this.m_writer.write("<!ENTITY ");
            this.m_writer.write(string);
            if (string2 != null) {
                this.m_writer.write(" PUBLIC \"");
                this.m_writer.write(string2);
            } else {
                this.m_writer.write(" SYSTEM \"");
                this.m_writer.write(string3);
            }
            this.m_writer.write("\" >");
            this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    protected boolean escapingNotNeeded(char c2) {
        boolean bl = c2 < '' ? c2 >= ' ' || '\n' == c2 || '\r' == c2 || '\t' == c2 : this.m_encodingInfo.isInEncoding(c2);
        return bl;
    }

    protected int writeUTF16Surrogate(char c2, char[] arrc, int n2, int n3) throws IOException {
        int n4 = 0;
        if (n2 + 1 >= n3) {
            throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(c2)}));
        }
        char c3 = c2;
        char c4 = arrc[n2 + 1];
        if (!Encodings.isLowUTF16Surrogate(c4)) {
            throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(c2) + " " + Integer.toHexString(c4)}));
        }
        Writer writer = this.m_writer;
        if (this.m_encodingInfo.isInEncoding(c2, c4)) {
            writer.write(arrc, n2, 2);
        } else {
            String string = this.getEncoding();
            if (string != null) {
                n4 = Encodings.toCodePoint(c3, c4);
                writer.write(38);
                writer.write(35);
                writer.write(Integer.toString(n4));
                writer.write(59);
            } else {
                writer.write(arrc, n2, 2);
            }
        }
        return n4;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    int accumDefaultEntity(Writer writer, char c2, int n2, char[] arrc, int n3, boolean bl, boolean bl2) throws IOException {
        if (!bl2 && '\n' == c2) {
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
            return n2 + 1;
        } else {
            if ((!bl || !this.m_charInfo.shouldMapTextChar(c2)) && (bl || !this.m_charInfo.shouldMapAttrChar(c2))) return n2;
            String string = this.m_charInfo.getOutputStringForChar(c2);
            if (null == string) return n2;
            writer.write(string);
        }
        return n2 + 1;
    }

    void writeNormalizedChars(char[] arrc, int n2, int n3, boolean bl, boolean bl2) throws IOException, SAXException {
        Writer writer = this.m_writer;
        int n4 = n2 + n3;
        for (int i2 = n2; i2 < n4; ++i2) {
            String string;
            char c2 = arrc[i2];
            if ('\n' == c2 && bl2) {
                writer.write(this.m_lineSep, 0, this.m_lineSepLen);
                continue;
            }
            if (bl && !this.escapingNotNeeded(c2)) {
                if (this.m_cdataTagOpen) {
                    this.closeCDATA();
                }
                if (Encodings.isHighUTF16Surrogate(c2)) {
                    this.writeUTF16Surrogate(c2, arrc, i2, n4);
                    ++i2;
                    continue;
                }
                writer.write("&#");
                string = Integer.toString(c2);
                writer.write(string);
                writer.write(59);
                continue;
            }
            if (bl && i2 < n4 - 2 && ']' == c2 && ']' == arrc[i2 + 1] && '>' == arrc[i2 + 2]) {
                writer.write("]]]]><![CDATA[>");
                i2 += 2;
                continue;
            }
            if (this.escapingNotNeeded(c2)) {
                if (bl && !this.m_cdataTagOpen) {
                    writer.write("<![CDATA[");
                    this.m_cdataTagOpen = true;
                }
                writer.write(c2);
                continue;
            }
            if (Encodings.isHighUTF16Surrogate(c2)) {
                if (this.m_cdataTagOpen) {
                    this.closeCDATA();
                }
                this.writeUTF16Surrogate(c2, arrc, i2, n4);
                ++i2;
                continue;
            }
            if (this.m_cdataTagOpen) {
                this.closeCDATA();
            }
            writer.write("&#");
            string = Integer.toString(c2);
            writer.write(string);
            writer.write(59);
        }
    }

    public void endNonEscaping() throws SAXException {
        this.m_disableOutputEscapingStates.pop();
    }

    public void startNonEscaping() throws SAXException {
        this.m_disableOutputEscapingStates.push(true);
    }

    protected void cdata(char[] arrc, int n2, int n3) throws SAXException {
        try {
            boolean bl;
            int n4 = n2;
            if (this.m_elemContext.m_startTagOpen) {
                this.closeStartTag();
                this.m_elemContext.m_startTagOpen = false;
            }
            this.m_ispreserve = true;
            if (this.shouldIndent()) {
                this.indent();
            }
            boolean bl2 = bl = n3 >= 1 && this.escapingNotNeeded(arrc[n2]);
            if (bl && !this.m_cdataTagOpen) {
                this.m_writer.write("<![CDATA[");
                this.m_cdataTagOpen = true;
            }
            if (this.isEscapingDisabled()) {
                this.charactersRaw(arrc, n2, n3);
            } else {
                this.writeNormalizedChars(arrc, n2, n3, true, this.m_lineSepUse);
            }
            if (bl && arrc[n2 + n3 - 1] == ']') {
                this.closeCDATA();
            }
            if (this.m_tracer != null) {
                super.fireCDATAEvent(arrc, n4, n3);
            }
        }
        catch (IOException iOException) {
            throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), iOException);
        }
    }

    private boolean isEscapingDisabled() {
        return this.m_disableOutputEscapingStates.peekOrFalse();
    }

    protected void charactersRaw(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        try {
            if (this.m_elemContext.m_startTagOpen) {
                this.closeStartTag();
                this.m_elemContext.m_startTagOpen = false;
            }
            this.m_ispreserve = true;
            this.m_writer.write(arrc, n2, n3);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (n3 == 0 || this.m_inEntityRef && !this.m_expandDTDEntities) {
            return;
        }
        this.m_docIsEmpty = false;
        if (this.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        } else if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
        }
        if (this.m_cdataStartCalled || this.m_elemContext.m_isCdataSection) {
            this.cdata(arrc, n2, n3);
            return;
        }
        if (this.m_cdataTagOpen) {
            this.closeCDATA();
        }
        if (this.m_disableOutputEscapingStates.peekOrFalse() || !this.m_escaping) {
            this.charactersRaw(arrc, n2, n3);
            if (this.m_tracer != null) {
                super.fireCharEvent(arrc, n2, n3);
            }
            return;
        }
        if (this.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        try {
            char c2;
            String string;
            int n4 = n2 + n3;
            int n5 = n2 - 1;
            Writer writer = this.m_writer;
            boolean bl = true;
            int n6 = n2;
            block13 : while (n6 < n4 && bl) {
                c2 = arrc[n6];
                if (this.m_charInfo.shouldMapTextChar(c2)) {
                    this.writeOutCleanChars(arrc, n6, n5);
                    string = this.m_charInfo.getOutputStringForChar(c2);
                    writer.write(string);
                    bl = false;
                    n5 = n6++;
                    continue;
                }
                switch (c2) {
                    case ' ': {
                        ++n6;
                        continue block13;
                    }
                    case '\n': {
                        n5 = this.processLineFeed(arrc, n6, n5, writer);
                        ++n6;
                        continue block13;
                    }
                    case '\r': {
                        this.writeOutCleanChars(arrc, n6, n5);
                        writer.write("&#13;");
                        n5 = n6++;
                        continue block13;
                    }
                    case '\t': {
                        ++n6;
                        continue block13;
                    }
                }
                bl = false;
            }
            if (n6 < n4 || !bl) {
                this.m_ispreserve = true;
            }
            while (n6 < n4) {
                c2 = arrc[n6];
                if (this.m_charInfo.shouldMapTextChar(c2)) {
                    this.writeOutCleanChars(arrc, n6, n5);
                    string = this.m_charInfo.getOutputStringForChar(c2);
                    writer.write(string);
                    n5 = n6;
                } else if (c2 <= '\u001f') {
                    switch (c2) {
                        case '\t': {
                            break;
                        }
                        case '\n': {
                            n5 = this.processLineFeed(arrc, n6, n5, writer);
                            break;
                        }
                        case '\r': {
                            this.writeOutCleanChars(arrc, n6, n5);
                            writer.write("&#13;");
                            n5 = n6;
                            break;
                        }
                        default: {
                            this.writeOutCleanChars(arrc, n6, n5);
                            writer.write("&#");
                            writer.write(Integer.toString(c2));
                            writer.write(59);
                            n5 = n6;
                            break;
                        }
                    }
                } else if (c2 >= '') {
                    if (c2 <= '\u009f') {
                        this.writeOutCleanChars(arrc, n6, n5);
                        writer.write("&#");
                        writer.write(Integer.toString(c2));
                        writer.write(59);
                        n5 = n6;
                    } else if (c2 == '\u2028') {
                        this.writeOutCleanChars(arrc, n6, n5);
                        writer.write("&#8232;");
                        n5 = n6;
                    } else if (!this.m_encodingInfo.isInEncoding(c2)) {
                        this.writeOutCleanChars(arrc, n6, n5);
                        writer.write("&#");
                        writer.write(Integer.toString(c2));
                        writer.write(59);
                        n5 = n6;
                    }
                }
                ++n6;
            }
            int n7 = n5 + 1;
            if (n6 > n7) {
                c2 = n6 - n7;
                this.m_writer.write(arrc, n7, (int)c2);
            }
            this.m_isprevtext = true;
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
        if (this.m_tracer != null) {
            super.fireCharEvent(arrc, n2, n3);
        }
    }

    private int processLineFeed(char[] arrc, int n2, int n3, Writer writer) throws IOException {
        if (this.m_lineSepUse && (this.m_lineSepLen != 1 || this.m_lineSep[0] != '\n')) {
            this.writeOutCleanChars(arrc, n2, n3);
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
            n3 = n2;
        }
        return n3;
    }

    private void writeOutCleanChars(char[] arrc, int n2, int n3) throws IOException {
        int n4 = n3 + 1;
        if (n4 < n2) {
            int n5 = n2 - n4;
            this.m_writer.write(arrc, n4, n5);
        }
    }

    private static boolean isCharacterInC0orC1Range(char c2) {
        if (c2 == '\t' || c2 == '\n' || c2 == '\r') {
            return false;
        }
        return c2 >= '' && c2 <= '\u009f' || c2 >= '\u0001' && c2 <= '\u001f';
    }

    private static boolean isNELorLSEPCharacter(char c2) {
        return c2 == '\u0085' || c2 == '\u2028';
    }

    public void characters(String string) throws SAXException {
        if (this.m_inEntityRef && !this.m_expandDTDEntities) {
            return;
        }
        int n2 = string.length();
        if (n2 > this.m_charsBuff.length) {
            this.m_charsBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_charsBuff, 0);
        this.characters(this.m_charsBuff, 0, n2);
    }

    private int accumDefaultEscape(Writer writer, char c2, int n2, char[] arrc, int n3, boolean bl, boolean bl2) throws IOException {
        int n4 = this.accumDefaultEntity(writer, c2, n2, arrc, n3, bl, bl2);
        if (n2 == n4) {
            if (Encodings.isHighUTF16Surrogate(c2)) {
                char c3;
                int n5 = 0;
                if (n2 + 1 >= n3) {
                    throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(c2)}));
                }
                if (!Encodings.isLowUTF16Surrogate(c3 = arrc[++n2])) {
                    throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(c2) + " " + Integer.toHexString(c3)}));
                }
                n5 = Encodings.toCodePoint(c2, c3);
                writer.write("&#");
                writer.write(Integer.toString(n5));
                writer.write(59);
                n4 += 2;
            } else {
                if (ToStream.isCharacterInC0orC1Range(c2) || ToStream.isNELorLSEPCharacter(c2)) {
                    writer.write("&#");
                    writer.write(Integer.toString(c2));
                    writer.write(59);
                } else if ((!this.escapingNotNeeded(c2) || bl && this.m_charInfo.shouldMapTextChar(c2) || !bl && this.m_charInfo.shouldMapAttrChar(c2)) && this.m_elemContext.m_currentElemDepth > 0) {
                    writer.write("&#");
                    writer.write(Integer.toString(c2));
                    writer.write(59);
                } else {
                    writer.write(c2);
                }
                ++n4;
            }
        }
        return n4;
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
            this.m_needToCallStartDocument = false;
            this.m_docIsEmpty = false;
        } else if (this.m_cdataTagOpen) {
            this.closeCDATA();
        }
        try {
            if (this.m_needToOutputDocTypeDecl) {
                if (null != this.getDoctypeSystem()) {
                    this.outputDocTypeDecl(string3, true);
                }
                this.m_needToOutputDocTypeDecl = false;
            }
            if (this.m_elemContext.m_startTagOpen) {
                this.closeStartTag();
                this.m_elemContext.m_startTagOpen = false;
            }
            if (string != null) {
                this.ensurePrefixIsDeclared(string, string3);
            }
            this.m_ispreserve = false;
            if (this.shouldIndent() && this.m_startNewLine) {
                this.indent();
            }
            this.m_startNewLine = true;
            Writer writer = this.m_writer;
            writer.write(60);
            writer.write(string3);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
        if (attributes != null) {
            this.addAttributes(attributes);
        }
        this.m_elemContext = this.m_elemContext.push(string, string2, string3);
        this.m_isprevtext = false;
        if (this.m_tracer != null) {
            this.firePseudoAttributes();
        }
    }

    public void startElement(String string, String string2, String string3) throws SAXException {
        this.startElement(string, string2, string3, null);
    }

    public void startElement(String string) throws SAXException {
        this.startElement(null, null, string, null);
    }

    void outputDocTypeDecl(String string, boolean bl) throws SAXException {
        if (this.m_cdataTagOpen) {
            this.closeCDATA();
        }
        try {
            String string2;
            Writer writer = this.m_writer;
            writer.write("<!DOCTYPE ");
            writer.write(string);
            String string3 = this.getDoctypePublic();
            if (null != string3) {
                writer.write(" PUBLIC \"");
                writer.write(string3);
                writer.write(34);
            }
            if (null != (string2 = this.getDoctypeSystem())) {
                if (null == string3) {
                    writer.write(" SYSTEM \"");
                } else {
                    writer.write(" \"");
                }
                writer.write(string2);
                if (bl) {
                    writer.write("\">");
                    writer.write(this.m_lineSep, 0, this.m_lineSepLen);
                    bl = false;
                } else {
                    writer.write(34);
                }
            }
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void processAttributes(Writer writer, int n2) throws IOException, SAXException {
        String string = this.getEncoding();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string2 = this.m_attributes.getQName(i2);
            String string3 = this.m_attributes.getValue(i2);
            writer.write(32);
            writer.write(string2);
            writer.write("=\"");
            this.writeAttrString(writer, string3, string);
            writer.write(34);
        }
    }

    public void writeAttrString(Writer writer, String string, String string2) throws IOException {
        int n2 = string.length();
        if (n2 > this.m_attrBuff.length) {
            this.m_attrBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_attrBuff, 0);
        char[] arrc = this.m_attrBuff;
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = arrc[i2];
            if (this.m_charInfo.shouldMapAttrChar(c2)) {
                this.accumDefaultEscape(writer, c2, i2, arrc, n2, false, true);
                continue;
            }
            if ('\u0000' <= c2 && c2 <= '\u001f') {
                switch (c2) {
                    case '\t': {
                        writer.write("&#9;");
                        break;
                    }
                    case '\n': {
                        writer.write("&#10;");
                        break;
                    }
                    case '\r': {
                        writer.write("&#13;");
                        break;
                    }
                    default: {
                        writer.write("&#");
                        writer.write(Integer.toString(c2));
                        writer.write(59);
                        break;
                    }
                }
                continue;
            }
            if (c2 < '') {
                writer.write(c2);
                continue;
            }
            if (c2 <= '\u009f') {
                writer.write("&#");
                writer.write(Integer.toString(c2));
                writer.write(59);
                continue;
            }
            if (c2 == '\u2028') {
                writer.write("&#8232;");
                continue;
            }
            if (this.m_encodingInfo.isInEncoding(c2)) {
                writer.write(c2);
                continue;
            }
            writer.write("&#");
            writer.write(Integer.toString(c2));
            writer.write(59);
        }
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        this.m_prefixMap.popNamespaces(this.m_elemContext.m_currentElemDepth, null);
        try {
            Writer writer = this.m_writer;
            if (this.m_elemContext.m_startTagOpen) {
                int n2;
                if (this.m_tracer != null) {
                    super.fireStartElem(this.m_elemContext.m_elementName);
                }
                if ((n2 = this.m_attributes.getLength()) > 0) {
                    this.processAttributes(this.m_writer, n2);
                    this.m_attributes.clear();
                }
                if (this.m_spaceBeforeClose) {
                    writer.write(" />");
                } else {
                    writer.write("/>");
                }
            } else {
                if (this.m_cdataTagOpen) {
                    this.closeCDATA();
                }
                if (this.shouldIndent()) {
                    this.indent(this.m_elemContext.m_currentElemDepth - 1);
                }
                writer.write(60);
                writer.write(47);
                writer.write(string3);
                writer.write(62);
            }
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
        if (!this.m_elemContext.m_startTagOpen && this.m_doIndent) {
            this.m_ispreserve = this.m_preserves.isEmpty() ? false : this.m_preserves.pop();
        }
        this.m_isprevtext = false;
        if (this.m_tracer != null) {
            super.fireEndElem(string3);
        }
        this.m_elemContext = this.m_elemContext.m_prev;
    }

    public void endElement(String string) throws SAXException {
        this.endElement(null, null, string);
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        this.startPrefixMapping(string, string2, true);
    }

    public boolean startPrefixMapping(String string, String string2, boolean bl) throws SAXException {
        int n2;
        if (bl) {
            this.flushPending();
            n2 = this.m_elemContext.m_currentElemDepth + 1;
        } else {
            n2 = this.m_elemContext.m_currentElemDepth;
        }
        boolean bl2 = this.m_prefixMap.pushNamespace(string, string2, n2);
        if (bl2) {
            if ("".equals(string)) {
                String string3 = "xmlns";
                this.addAttributeAlways("http://www.w3.org/2000/xmlns/", string3, string3, "CDATA", string2, false);
            } else if (!"".equals(string2)) {
                String string4 = "xmlns:" + string;
                this.addAttributeAlways("http://www.w3.org/2000/xmlns/", string, string4, "CDATA", string2, false);
            }
        }
        return bl2;
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        int n4 = n2;
        if (this.m_inEntityRef) {
            return;
        }
        if (this.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        } else if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        try {
            int n5;
            int n6 = n2 + n3;
            boolean bl = false;
            if (this.m_cdataTagOpen) {
                this.closeCDATA();
            }
            if (this.shouldIndent()) {
                this.indent();
            }
            Writer writer = this.m_writer;
            writer.write("<!--");
            for (n5 = n2; n5 < n6; ++n5) {
                if (bl && arrc[n5] == '-') {
                    writer.write(arrc, n2, n5 - n2);
                    writer.write(" -");
                    n2 = n5 + 1;
                }
                bl = arrc[n5] == '-';
            }
            if (n3 > 0) {
                n5 = n6 - n2;
                if (n5 > 0) {
                    writer.write(arrc, n2, n5);
                }
                if (arrc[n6 - 1] == '-') {
                    writer.write(32);
                }
            }
            writer.write("-->");
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
        this.m_startNewLine = true;
        if (this.m_tracer != null) {
            super.fireCommentEvent(arrc, n4, n3);
        }
    }

    public void endCDATA() throws SAXException {
        if (this.m_cdataTagOpen) {
            this.closeCDATA();
        }
        this.m_cdataStartCalled = false;
    }

    public void endDTD() throws SAXException {
        try {
            if (this.m_needToOutputDocTypeDecl) {
                this.outputDocTypeDecl(this.m_elemContext.m_elementName, false);
                this.m_needToOutputDocTypeDecl = false;
            }
            Writer writer = this.m_writer;
            if (!this.m_inDoctype) {
                writer.write("]>");
            } else {
                writer.write(62);
            }
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        }
        catch (IOException iOException) {
            throw new SAXException(iOException);
        }
    }

    public void endPrefixMapping(String string) throws SAXException {
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        if (0 == n3) {
            return;
        }
        this.characters(arrc, n2, n3);
    }

    public void skippedEntity(String string) throws SAXException {
    }

    public void startCDATA() throws SAXException {
        this.m_cdataStartCalled = true;
    }

    public void startEntity(String string) throws SAXException {
        if (string.equals("[dtd]")) {
            this.m_inExternalDTD = true;
        }
        if (!this.m_expandDTDEntities && !this.m_inExternalDTD) {
            this.startNonEscaping();
            this.characters("&" + string + ';');
            this.endNonEscaping();
        }
        this.m_inEntityRef = true;
    }

    protected void closeStartTag() throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
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
            }
            catch (IOException iOException) {
                throw new SAXException(iOException);
            }
            if (this.m_CdataElems != null) {
                this.m_elemContext.m_isCdataSection = this.isCdataSection();
            }
            if (this.m_doIndent) {
                this.m_isprevtext = false;
                this.m_preserves.push(this.m_ispreserve);
            }
        }
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        this.setDoctypeSystem(string3);
        this.setDoctypePublic(string2);
        this.m_elemContext.m_elementName = string;
        this.m_inDoctype = true;
    }

    public void setIndentAmount(int n2) {
        this.m_indentAmount = n2;
    }

    protected boolean shouldIndent() {
        return this.m_doIndent && !this.m_ispreserve && !this.m_isprevtext && this.m_elemContext.m_currentElemDepth > 0;
    }

    public void setCdataSectionElements(Vector vector) {
        int n2;
        if (vector != null && (n2 = vector.size() - 1) > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < n2; i2 += 2) {
                if (i2 != 0) {
                    stringBuffer.append(' ');
                }
                String string = (String)vector.elementAt(i2);
                String string2 = (String)vector.elementAt(i2 + 1);
                if (string != null) {
                    stringBuffer.append('{');
                    stringBuffer.append(string);
                    stringBuffer.append('}');
                }
                stringBuffer.append(string2);
            }
            this.m_StringOfCDATASections = stringBuffer.toString();
        }
        this.initCdataElems(this.m_StringOfCDATASections);
    }

    protected String ensureAttributesNamespaceIsDeclared(String string, String string2, String string3) throws SAXException {
        if (string != null && string.length() > 0) {
            String string4;
            int n2 = 0;
            n2 = string3.indexOf(":");
            String string5 = string4 = n2 < 0 ? "" : string3.substring(0, n2);
            if (n2 > 0) {
                String string6 = this.m_prefixMap.lookupNamespace(string4);
                if (string6 != null && string6.equals(string)) {
                    return null;
                }
                this.startPrefixMapping(string4, string, false);
                this.addAttribute("http://www.w3.org/2000/xmlns/", string4, "xmlns:" + string4, "CDATA", string, false);
                return string4;
            }
            String string7 = this.m_prefixMap.lookupPrefix(string);
            if (string7 == null) {
                string7 = this.m_prefixMap.generateNextPrefix();
                this.startPrefixMapping(string7, string, false);
                this.addAttribute("http://www.w3.org/2000/xmlns/", string7, "xmlns:" + string7, "CDATA", string, false);
            }
            return string7;
        }
        return null;
    }

    void ensurePrefixIsDeclared(String string, String string2) throws SAXException {
        if (string != null && string.length() > 0) {
            String string3;
            String string4;
            int n2 = string2.indexOf(":");
            boolean bl = n2 < 0;
            String string5 = string4 = bl ? "" : string2.substring(0, n2);
            if (!(null == string4 || null != (string3 = this.m_prefixMap.lookupNamespace(string4)) && string3.equals(string))) {
                this.startPrefixMapping(string4, string);
                this.addAttributeAlways("http://www.w3.org/2000/xmlns/", bl ? "xmlns" : string4, bl ? "xmlns" : "xmlns:" + string4, "CDATA", string, false);
            }
        }
    }

    public void flushPending() throws SAXException {
        if (this.m_needToCallStartDocument) {
            this.startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (this.m_elemContext.m_startTagOpen) {
            this.closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        if (this.m_cdataTagOpen) {
            this.closeCDATA();
            this.m_cdataTagOpen = false;
        }
        if (this.m_writer != null) {
            try {
                this.m_writer.flush();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public void setContentHandler(ContentHandler contentHandler) {
    }

    public boolean addAttributeAlways(String string, String string2, String string3, String string4, String string5, boolean bl) {
        boolean bl2;
        int n2 = string == null || string2 == null || string.length() == 0 ? this.m_attributes.getIndex(string3) : this.m_attributes.getIndex(string, string2);
        if (n2 >= 0) {
            String string6 = null;
            if (this.m_tracer != null && string5.equals(string6 = this.m_attributes.getValue(n2))) {
                string6 = null;
            }
            this.m_attributes.setValue(n2, string5);
            bl2 = false;
            if (string6 != null) {
                this.firePseudoAttributes();
            }
        } else {
            if (bl) {
                NamespaceMappings.MappingRecord mappingRecord;
                String string7;
                int n3 = string3.indexOf(58);
                if (n3 > 0 && (mappingRecord = this.m_prefixMap.getMappingFromPrefix(string7 = string3.substring(0, n3))) != null && mappingRecord.m_declarationDepth == this.m_elemContext.m_currentElemDepth && !mappingRecord.m_uri.equals(string)) {
                    string7 = this.m_prefixMap.lookupPrefix(string);
                    if (string7 == null) {
                        string7 = this.m_prefixMap.generateNextPrefix();
                    }
                    string3 = string7 + ':' + string2;
                }
                try {
                    string7 = this.ensureAttributesNamespaceIsDeclared(string, string2, string3);
                }
                catch (SAXException sAXException) {
                    sAXException.printStackTrace();
                }
            }
            this.m_attributes.addAttribute(string, string2, string3, string4, string5);
            bl2 = true;
            if (this.m_tracer != null) {
                this.firePseudoAttributes();
            }
        }
        return bl2;
    }

    protected void firePseudoAttributes() {
        if (this.m_tracer != null) {
            try {
                char[] arrc;
                this.m_writer.flush();
                StringBuffer stringBuffer = new StringBuffer();
                int n2 = this.m_attributes.getLength();
                if (n2 > 0) {
                    arrc = new char[](stringBuffer);
                    this.processAttributes((Writer)arrc, n2);
                }
                stringBuffer.append('>');
                arrc = stringBuffer.toString().toCharArray();
                this.m_tracer.fireGenerateEvent(11, arrc, 0, arrc.length);
            }
            catch (IOException iOException) {
            }
            catch (SAXException sAXException) {
                // empty catch block
            }
        }
    }

    public void setTransformer(Transformer transformer) {
        super.setTransformer(transformer);
        if (this.m_tracer != null && !(this.m_writer instanceof SerializerTraceWriter)) {
            this.setWriterInternal(new SerializerTraceWriter(this.m_writer, this.m_tracer), false);
        }
    }

    public boolean reset() {
        boolean bl = false;
        if (super.reset()) {
            this.resetToStream();
            bl = true;
        }
        return bl;
    }

    private void resetToStream() {
        this.m_cdataStartCalled = false;
        this.m_disableOutputEscapingStates.clear();
        this.m_escaping = true;
        this.m_expandDTDEntities = true;
        this.m_inDoctype = false;
        this.m_ispreserve = false;
        this.m_isprevtext = false;
        this.m_isUTF8 = false;
        this.m_lineSep = s_systemLineSep;
        this.m_lineSepLen = s_systemLineSep.length;
        this.m_lineSepUse = true;
        this.m_preserves.clear();
        this.m_shouldFlush = true;
        this.m_spaceBeforeClose = false;
        this.m_startNewLine = false;
        this.m_writer_set_by_user = false;
    }

    public void setEncoding(String string) {
        this.setOutputProperty("encoding", string);
    }

    public void notationDecl(String string, String string2, String string3) throws SAXException {
        try {
            this.DTDprolog();
            this.m_writer.write("<!NOTATION ");
            this.m_writer.write(string);
            if (string2 != null) {
                this.m_writer.write(" PUBLIC \"");
                this.m_writer.write(string2);
            } else {
                this.m_writer.write(" SYSTEM \"");
                this.m_writer.write(string3);
            }
            this.m_writer.write("\" >");
            this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) throws SAXException {
        try {
            this.DTDprolog();
            this.m_writer.write("<!ENTITY ");
            this.m_writer.write(string);
            if (string2 != null) {
                this.m_writer.write(" PUBLIC \"");
                this.m_writer.write(string2);
            } else {
                this.m_writer.write(" SYSTEM \"");
                this.m_writer.write(string3);
            }
            this.m_writer.write("\" NDATA ");
            this.m_writer.write(string4);
            this.m_writer.write(" >");
            this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private void DTDprolog() throws SAXException, IOException {
        Writer writer = this.m_writer;
        if (this.m_needToOutputDocTypeDecl) {
            this.outputDocTypeDecl(this.m_elemContext.m_elementName, false);
            this.m_needToOutputDocTypeDecl = false;
        }
        if (this.m_inDoctype) {
            writer.write(" [");
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
            this.m_inDoctype = false;
        }
    }

    public void addCdataSectionElements(String string) {
        if (string != null) {
            this.initCdataElems(string);
        }
        this.m_StringOfCDATASections = this.m_StringOfCDATASections == null ? string : this.m_StringOfCDATASections + " " + string;
    }

    static final class BoolStack {
        private boolean[] m_values;
        private int m_allocatedSize;
        private int m_index;

        public BoolStack() {
            this(32);
        }

        public BoolStack(int n2) {
            this.m_allocatedSize = n2;
            this.m_values = new boolean[n2];
            this.m_index = -1;
        }

        public final void clear() {
            this.m_index = -1;
        }

        public final boolean push(boolean bl) {
            if (this.m_index == this.m_allocatedSize - 1) {
                this.grow();
            }
            boolean bl2 = bl;
            this.m_values[++this.m_index] = bl2;
            return bl2;
        }

        public final boolean pop() {
            return this.m_values[this.m_index--];
        }

        public final boolean peekOrFalse() {
            return this.m_index > -1 ? this.m_values[this.m_index] : false;
        }

        public boolean isEmpty() {
            return this.m_index == -1;
        }

        private void grow() {
            this.m_allocatedSize *= 2;
            boolean[] arrbl = new boolean[this.m_allocatedSize];
            System.arraycopy(this.m_values, 0, arrbl, 0, this.m_index + 1);
            this.m_values = arrbl;
        }
    }

    private static class WritertoStringBuffer
    extends Writer {
        private final StringBuffer m_stringbuf;

        WritertoStringBuffer(StringBuffer stringBuffer) {
            this.m_stringbuf = stringBuffer;
        }

        public void write(char[] arrc, int n2, int n3) throws IOException {
            this.m_stringbuf.append(arrc, n2, n3);
        }

        public void flush() throws IOException {
        }

        public void close() throws IOException {
        }

        public void write(int n2) {
            this.m_stringbuf.append((char)n2);
        }

        public void write(String string) {
            this.m_stringbuf.append(string);
        }
    }

}

