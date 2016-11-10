/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.apache.xml.serializer.AttributesImplSerializer;
import org.apache.xml.serializer.DOMSerializer;
import org.apache.xml.serializer.ElemContext;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.SerializerConstants;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class SerializerBase
implements SerializationHandler,
SerializerConstants {
    public static final String PKG_NAME;
    public static final String PKG_PATH;
    protected boolean m_needToCallStartDocument = true;
    protected boolean m_cdataTagOpen = false;
    protected AttributesImplSerializer m_attributes = new AttributesImplSerializer();
    protected boolean m_inEntityRef = false;
    protected boolean m_inExternalDTD = false;
    protected String m_doctypeSystem;
    protected String m_doctypePublic;
    boolean m_needToOutputDocTypeDecl = true;
    protected boolean m_shouldNotWriteXMLHeader = false;
    private String m_standalone;
    protected boolean m_standaloneWasSpecified = false;
    protected boolean m_doIndent = false;
    protected int m_indentAmount = 0;
    protected String m_version = null;
    protected String m_mediatype;
    private Transformer m_transformer;
    protected NamespaceMappings m_prefixMap;
    protected SerializerTrace m_tracer;
    protected SourceLocator m_sourceLocator;
    protected Writer m_writer = null;
    protected ElemContext m_elemContext = new ElemContext();
    protected char[] m_charsBuff = new char[60];
    protected char[] m_attrBuff = new char[30];
    protected String m_StringOfCDATASections = null;
    boolean m_docIsEmpty = true;
    protected Hashtable m_CdataElems = null;
    private HashMap m_OutputProps;
    private HashMap m_OutputPropsDefault;
    static Class class$org$apache$xml$serializer$SerializerBase;

    SerializerBase() {
    }

    protected void fireEndElem(String string) throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(4, string, (Attributes)null);
        }
    }

    protected void fireCharEvent(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(5, arrc, n2, n3);
        }
    }

    public void comment(String string) throws SAXException {
        this.m_docIsEmpty = false;
        int n2 = string.length();
        if (n2 > this.m_charsBuff.length) {
            this.m_charsBuff = new char[n2 * 2 + 1];
        }
        string.getChars(0, n2, this.m_charsBuff, 0);
        this.comment(this.m_charsBuff, 0, n2);
    }

    protected String patchName(String string) {
        int n2 = string.lastIndexOf(58);
        if (n2 > 0) {
            int n3 = string.indexOf(58);
            String string2 = string.substring(0, n3);
            String string3 = string.substring(n2 + 1);
            String string4 = this.m_prefixMap.lookupNamespace(string2);
            if (string4 != null && string4.length() == 0) {
                return string3;
            }
            if (n3 != n2) {
                return string2 + ':' + string3;
            }
        }
        return string;
    }

    protected static String getLocalName(String string) {
        int n2 = string.lastIndexOf(58);
        return n2 > 0 ? string.substring(n2 + 1) : string;
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void addAttribute(String string, String string2, String string3, String string4, String string5, boolean bl) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            this.addAttributeAlways(string, string2, string3, string4, string5, bl);
        }
    }

    public boolean addAttributeAlways(String string, String string2, String string3, String string4, String string5, boolean bl) {
        boolean bl2;
        int n2 = string2 == null || string == null || string.length() == 0 ? this.m_attributes.getIndex(string3) : this.m_attributes.getIndex(string, string2);
        if (n2 >= 0) {
            this.m_attributes.setValue(n2, string5);
            bl2 = false;
        } else {
            this.m_attributes.addAttribute(string, string2, string3, string4, string5);
            bl2 = true;
        }
        return bl2;
    }

    public void addAttribute(String string, String string2) {
        if (this.m_elemContext.m_startTagOpen) {
            String string3 = this.patchName(string);
            String string4 = SerializerBase.getLocalName(string3);
            String string5 = this.getNamespaceURI(string3, false);
            this.addAttributeAlways(string5, string4, string3, "CDATA", string2, false);
        }
    }

    public void addAttributes(Attributes attributes) throws SAXException {
        int n2 = attributes.getLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = attributes.getURI(i2);
            if (null == string) {
                string = "";
            }
            this.addAttributeAlways(string, attributes.getLocalName(i2), attributes.getQName(i2), attributes.getType(i2), attributes.getValue(i2), false);
        }
    }

    public ContentHandler asContentHandler() throws IOException {
        return this;
    }

    public void endEntity(String string) throws SAXException {
        if (string.equals("[dtd]")) {
            this.m_inExternalDTD = false;
        }
        this.m_inEntityRef = false;
        if (this.m_tracer != null) {
            this.fireEndEntity(string);
        }
    }

    public void close() {
    }

    protected void initCDATA() {
    }

    public String getEncoding() {
        return this.getOutputProperty("encoding");
    }

    public void setEncoding(String string) {
        this.setOutputProperty("encoding", string);
    }

    public void setOmitXMLDeclaration(boolean bl) {
        String string = bl ? "yes" : "no";
        this.setOutputProperty("omit-xml-declaration", string);
    }

    public boolean getOmitXMLDeclaration() {
        return this.m_shouldNotWriteXMLHeader;
    }

    public String getDoctypePublic() {
        return this.m_doctypePublic;
    }

    public void setDoctypePublic(String string) {
        this.setOutputProperty("doctype-public", string);
    }

    public String getDoctypeSystem() {
        return this.m_doctypeSystem;
    }

    public void setDoctypeSystem(String string) {
        this.setOutputProperty("doctype-system", string);
    }

    public void setDoctype(String string, String string2) {
        this.setOutputProperty("doctype-system", string);
        this.setOutputProperty("doctype-public", string2);
    }

    public void setStandalone(String string) {
        this.setOutputProperty("standalone", string);
    }

    protected void setStandaloneInternal(String string) {
        this.m_standalone = "yes".equals(string) ? "yes" : "no";
    }

    public String getStandalone() {
        return this.m_standalone;
    }

    public String getMediaType() {
        return this.m_mediatype;
    }

    public String getVersion() {
        return this.m_version;
    }

    public void setVersion(String string) {
        this.setOutputProperty("version", string);
    }

    public void setMediaType(String string) {
        this.setOutputProperty("media-type", string);
    }

    public void setIndentAmount(int n2) {
        this.m_indentAmount = n2;
    }

    public void setIndent(boolean bl) {
        String string = bl ? "yes" : "no";
        this.setOutputProperty("indent", string);
    }

    public void namespaceAfterStartElement(String string, String string2) throws SAXException {
    }

    public DOMSerializer asDOMSerializer() throws IOException {
        return this;
    }

    protected static final String getPrefixPart(String string) {
        int n2 = string.indexOf(58);
        return n2 > 0 ? string.substring(0, n2) : null;
    }

    public NamespaceMappings getNamespaceMappings() {
        return this.m_prefixMap;
    }

    public String getPrefix(String string) {
        String string2 = this.m_prefixMap.lookupPrefix(string);
        return string2;
    }

    public String getNamespaceURI(String string, boolean bl) {
        String string2;
        String string3 = "";
        int n2 = string.lastIndexOf(58);
        String string4 = string2 = n2 > 0 ? string.substring(0, n2) : "";
        if (!("".equals(string2) && !bl || this.m_prefixMap == null || (string3 = this.m_prefixMap.lookupNamespace(string2)) != null || string2.equals("xmlns"))) {
            throw new RuntimeException(Utils.messages.createMessage("ER_NAMESPACE_PREFIX", new Object[]{string.substring(0, n2)}));
        }
        return string3;
    }

    public String getNamespaceURIFromPrefix(String string) {
        String string2 = null;
        if (this.m_prefixMap != null) {
            string2 = this.m_prefixMap.lookupNamespace(string);
        }
        return string2;
    }

    public void entityReference(String string) throws SAXException {
        this.flushPending();
        this.startEntity(string);
        this.endEntity(string);
        if (this.m_tracer != null) {
            this.fireEntityReference(string);
        }
    }

    public void setTransformer(Transformer transformer) {
        this.m_transformer = transformer;
        this.m_tracer = this.m_transformer instanceof SerializerTrace && ((SerializerTrace)((Object)this.m_transformer)).hasTraceListeners() ? (SerializerTrace)((Object)this.m_transformer) : null;
    }

    public Transformer getTransformer() {
        return this.m_transformer;
    }

    public void characters(Node node) throws SAXException {
        this.flushPending();
        String string = node.getNodeValue();
        if (string != null) {
            int n2 = string.length();
            if (n2 > this.m_charsBuff.length) {
                this.m_charsBuff = new char[n2 * 2 + 1];
            }
            string.getChars(0, n2, this.m_charsBuff, 0);
            this.characters(this.m_charsBuff, 0, n2);
        }
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        this.m_elemContext.m_startTagOpen = false;
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
    }

    private void flushMyWriter() {
        if (this.m_writer != null) {
            try {
                this.m_writer.flush();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    protected void fireCDATAEvent(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(10, arrc, n2, n3);
        }
    }

    protected void fireCommentEvent(char[] arrc, int n2, int n3) throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(8, new String(arrc, n2, n3));
        }
    }

    public void fireEndEntity(String string) throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
        }
    }

    protected void fireStartDoc() throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(1);
        }
    }

    protected void fireEndDoc() throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(2);
        }
    }

    protected void fireStartElem(String string) throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(3, string, this.m_attributes);
        }
    }

    protected void fireEscapingEvent(String string, String string2) throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(7, string, string2);
        }
    }

    protected void fireEntityReference(String string) throws SAXException {
        if (this.m_tracer != null) {
            this.flushMyWriter();
            this.m_tracer.fireGenerateEvent(9, string, (Attributes)null);
        }
    }

    public void startDocument() throws SAXException {
        this.startDocumentInternal();
        this.m_needToCallStartDocument = false;
    }

    protected void startDocumentInternal() throws SAXException {
        if (this.m_tracer != null) {
            this.fireStartDoc();
        }
    }

    public void setSourceLocator(SourceLocator sourceLocator) {
        this.m_sourceLocator = sourceLocator;
    }

    public void setNamespaceMappings(NamespaceMappings namespaceMappings) {
        this.m_prefixMap = namespaceMappings;
    }

    public boolean reset() {
        this.resetSerializerBase();
        return true;
    }

    private void resetSerializerBase() {
        this.m_attributes.clear();
        this.m_CdataElems = null;
        this.m_cdataTagOpen = false;
        this.m_docIsEmpty = true;
        this.m_doctypePublic = null;
        this.m_doctypeSystem = null;
        this.m_doIndent = false;
        this.m_elemContext = new ElemContext();
        this.m_indentAmount = 0;
        this.m_inEntityRef = false;
        this.m_inExternalDTD = false;
        this.m_mediatype = null;
        this.m_needToCallStartDocument = true;
        this.m_needToOutputDocTypeDecl = false;
        if (this.m_OutputProps != null) {
            this.m_OutputProps.clear();
        }
        if (this.m_OutputPropsDefault != null) {
            this.m_OutputPropsDefault.clear();
        }
        if (this.m_prefixMap != null) {
            this.m_prefixMap.reset();
        }
        this.m_shouldNotWriteXMLHeader = false;
        this.m_sourceLocator = null;
        this.m_standalone = null;
        this.m_standaloneWasSpecified = false;
        this.m_StringOfCDATASections = null;
        this.m_tracer = null;
        this.m_transformer = null;
        this.m_version = null;
    }

    final boolean inTemporaryOutputState() {
        return this.getEncoding() == null;
    }

    public void notationDecl(String string, String string2, String string3) throws SAXException {
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) throws SAXException {
    }

    void initCdataElems(String string) {
        if (string != null) {
            int n2 = string.length();
            boolean bl = false;
            boolean bl2 = false;
            StringBuffer stringBuffer = new StringBuffer();
            String string2 = null;
            String string3 = null;
            for (int i2 = 0; i2 < n2; ++i2) {
                char c2 = string.charAt(i2);
                if (Character.isWhitespace(c2)) {
                    if (!bl) {
                        if (stringBuffer.length() <= 0) continue;
                        string3 = stringBuffer.toString();
                        if (!bl2) {
                            string2 = "";
                        }
                        this.addCDATAElement(string2, string3);
                        stringBuffer.setLength(0);
                        bl2 = false;
                        continue;
                    }
                    stringBuffer.append(c2);
                    continue;
                }
                if ('{' == c2) {
                    bl = true;
                    continue;
                }
                if ('}' == c2) {
                    bl2 = true;
                    string2 = stringBuffer.toString();
                    stringBuffer.setLength(0);
                    bl = false;
                    continue;
                }
                stringBuffer.append(c2);
            }
            if (stringBuffer.length() > 0) {
                string3 = stringBuffer.toString();
                if (!bl2) {
                    string2 = "";
                }
                this.addCDATAElement(string2, string3);
            }
        }
    }

    private void addCDATAElement(String string, String string2) {
        Hashtable<String, String> hashtable;
        if (this.m_CdataElems == null) {
            this.m_CdataElems = new Hashtable();
        }
        if ((hashtable = (Hashtable<String, String>)this.m_CdataElems.get(string2)) == null) {
            hashtable = new Hashtable<String, String>();
            this.m_CdataElems.put(string2, hashtable);
        }
        hashtable.put(string, string);
    }

    protected boolean isCdataSection() {
        boolean bl = false;
        if (null != this.m_StringOfCDATASections) {
            Object object;
            Object v2;
            if (this.m_elemContext.m_elementLocalName == null) {
                object = SerializerBase.getLocalName(this.m_elemContext.m_elementName);
                this.m_elemContext.m_elementLocalName = object;
            }
            if (this.m_elemContext.m_elementURI == null) {
                this.m_elemContext.m_elementURI = this.getElementURI();
            } else if (this.m_elemContext.m_elementURI.length() == 0) {
                if (this.m_elemContext.m_elementName == null) {
                    this.m_elemContext.m_elementName = this.m_elemContext.m_elementLocalName;
                } else if (this.m_elemContext.m_elementLocalName.length() < this.m_elemContext.m_elementName.length()) {
                    this.m_elemContext.m_elementURI = this.getElementURI();
                }
            }
            object = (Hashtable)this.m_CdataElems.get(this.m_elemContext.m_elementLocalName);
            if (object != null && (v2 = object.get(this.m_elemContext.m_elementURI)) != null) {
                bl = true;
            }
        }
        return bl;
    }

    private String getElementURI() {
        String string = null;
        String string2 = SerializerBase.getPrefixPart(this.m_elemContext.m_elementName);
        string = string2 == null ? this.m_prefixMap.lookupNamespace("") : this.m_prefixMap.lookupNamespace(string2);
        if (string == null) {
            string = "";
        }
        return string;
    }

    public String getOutputProperty(String string) {
        String string2 = this.getOutputPropertyNonDefault(string);
        if (string2 == null) {
            string2 = this.getOutputPropertyDefault(string);
        }
        return string2;
    }

    public String getOutputPropertyNonDefault(String string) {
        return this.getProp(string, false);
    }

    public String getOutputPropertyDefault(String string) {
        return this.getProp(string, true);
    }

    public void setOutputProperty(String string, String string2) {
        this.setProp(string, string2, false);
    }

    public void setOutputPropertyDefault(String string, String string2) {
        this.setProp(string, string2, true);
    }

    Set getOutputPropDefaultKeys() {
        return this.m_OutputPropsDefault.keySet();
    }

    Set getOutputPropKeys() {
        return this.m_OutputProps.keySet();
    }

    private String getProp(String string, boolean bl) {
        if (this.m_OutputProps == null) {
            this.m_OutputProps = new HashMap();
            this.m_OutputPropsDefault = new HashMap();
        }
        String string2 = bl ? (String)this.m_OutputPropsDefault.get(string) : (String)this.m_OutputProps.get(string);
        return string2;
    }

    void setProp(String string, String string2, boolean bl) {
        if (this.m_OutputProps == null) {
            this.m_OutputProps = new HashMap();
            this.m_OutputPropsDefault = new HashMap();
        }
        if (bl) {
            this.m_OutputPropsDefault.put(string, string2);
        } else if ("cdata-section-elements".equals(string) && string2 != null) {
            this.initCdataElems(string2);
            String string3 = (String)this.m_OutputProps.get(string);
            String string4 = string3 == null ? string3 + ' ' + string2 : string2;
            this.m_OutputProps.put(string, string4);
        } else {
            this.m_OutputProps.put(string, string2);
        }
    }

    static char getFirstCharLocName(String string) {
        int n2 = string.indexOf(125);
        char c2 = n2 < 0 ? string.charAt(0) : string.charAt(n2 + 1);
        return c2;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static {
        Class class_ = class$org$apache$xml$serializer$SerializerBase == null ? (SerializerBase.class$org$apache$xml$serializer$SerializerBase = SerializerBase.class$("org.apache.xml.serializer.SerializerBase")) : class$org$apache$xml$serializer$SerializerBase;
        String string = class_.getName();
        int n2 = string.lastIndexOf(46);
        PKG_NAME = n2 < 0 ? "" : string.substring(0, n2);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < PKG_NAME.length(); ++i2) {
            char c2 = PKG_NAME.charAt(i2);
            if (c2 == '.') {
                stringBuffer.append('/');
                continue;
            }
            stringBuffer.append(c2);
        }
        PKG_PATH = stringBuffer.toString();
    }
}

