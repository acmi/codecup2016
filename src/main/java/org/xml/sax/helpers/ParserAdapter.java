/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax.helpers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.ParserFactory;

public class ParserAdapter
implements DocumentHandler,
XMLReader {
    private NamespaceSupport nsSupport;
    private AttributeListAdapter attAdapter;
    private boolean parsing = false;
    private String[] nameParts = new String[3];
    private Parser parser = null;
    private AttributesImpl atts = null;
    private boolean namespaces = true;
    private boolean prefixes = false;
    private boolean uris = false;
    Locator locator;
    EntityResolver entityResolver = null;
    DTDHandler dtdHandler = null;
    ContentHandler contentHandler = null;
    ErrorHandler errorHandler = null;

    public ParserAdapter() throws SAXException {
        String string = System.getProperty("org.xml.sax.parser");
        try {
            this.setup(ParserFactory.makeParser());
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new SAXException("Cannot find SAX1 driver class " + string, classNotFoundException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new SAXException("SAX1 driver class " + string + " found but cannot be loaded", illegalAccessException);
        }
        catch (InstantiationException instantiationException) {
            throw new SAXException("SAX1 driver class " + string + " loaded but cannot be instantiated", instantiationException);
        }
        catch (ClassCastException classCastException) {
            throw new SAXException("SAX1 driver class " + string + " does not implement org.xml.sax.Parser");
        }
        catch (NullPointerException nullPointerException) {
            throw new SAXException("System property org.xml.sax.parser not specified");
        }
    }

    public ParserAdapter(Parser parser) {
        this.setup(parser);
    }

    private void setup(Parser parser) {
        if (parser == null) {
            throw new NullPointerException("Parser argument must not be null");
        }
        this.parser = parser;
        this.atts = new AttributesImpl();
        this.nsSupport = new NamespaceSupport();
        this.attAdapter = new AttributeListAdapter(this);
    }

    public void setFeature(String string, boolean bl) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (string.equals("http://xml.org/sax/features/namespaces")) {
            this.checkNotParsing("feature", string);
            this.namespaces = bl;
            if (!this.namespaces && !this.prefixes) {
                this.prefixes = true;
            }
        } else if (string.equals("http://xml.org/sax/features/namespace-prefixes")) {
            this.checkNotParsing("feature", string);
            this.prefixes = bl;
            if (!this.prefixes && !this.namespaces) {
                this.namespaces = true;
            }
        } else if (string.equals("http://xml.org/sax/features/xmlns-uris")) {
            this.checkNotParsing("feature", string);
            this.uris = bl;
        } else {
            throw new SAXNotRecognizedException("Feature: " + string);
        }
    }

    public boolean getFeature(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (string.equals("http://xml.org/sax/features/namespaces")) {
            return this.namespaces;
        }
        if (string.equals("http://xml.org/sax/features/namespace-prefixes")) {
            return this.prefixes;
        }
        if (string.equals("http://xml.org/sax/features/xmlns-uris")) {
            return this.uris;
        }
        throw new SAXNotRecognizedException("Feature: " + string);
    }

    public void setProperty(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("Property: " + string);
    }

    public Object getProperty(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("Property: " + string);
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public void setDTDHandler(DTDHandler dTDHandler) {
        this.dtdHandler = dTDHandler;
    }

    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public void parse(String string) throws IOException, SAXException {
        this.parse(new InputSource(string));
    }

    public void parse(InputSource inputSource) throws IOException, SAXException {
        if (this.parsing) {
            throw new SAXException("Parser is already in use");
        }
        this.setupParser();
        this.parsing = true;
        try {
            this.parser.parse(inputSource);
            Object var3_2 = null;
            this.parsing = false;
        }
        catch (Throwable throwable) {
            Object var3_3 = null;
            this.parsing = false;
            throw throwable;
        }
        this.parsing = false;
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
        if (this.contentHandler != null) {
            this.contentHandler.setDocumentLocator(locator);
        }
    }

    public void startDocument() throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.startDocument();
        }
    }

    public void endDocument() throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.endDocument();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void startElement(String var1_1, AttributeList var2_2) throws SAXException {
        var3_3 = null;
        if (!this.namespaces) {
            if (this.contentHandler == null) return;
            this.attAdapter.setAttributeList(var2_2);
            this.contentHandler.startElement("", "", var1_1.intern(), this.attAdapter);
            return;
        }
        this.nsSupport.pushContext();
        var4_4 = var2_2.getLength();
        var5_5 = 0;
        while (var5_5 < var4_4) {
            var6_6 = var2_2.getName(var5_5);
            if (!var6_6.startsWith("xmlns")) ** GOTO lbl25
            var8_11 = var6_6.indexOf(58);
            if (var8_11 != -1 || var6_6.length() != 5) ** GOTO lbl17
            var7_8 = "";
            ** GOTO lbl19
lbl17: // 1 sources:
            if (var8_11 == 5) {
                var7_8 = var6_6.substring(var8_11 + 1);
lbl19: // 2 sources:
                var9_13 = var2_2.getValue(var5_5);
                if (!this.nsSupport.declarePrefix(var7_8, var9_13)) {
                    this.reportError("Illegal Namespace prefix: " + var7_8);
                } else if (this.contentHandler != null) {
                    this.contentHandler.startPrefixMapping(var7_8, var9_13);
                }
            }
lbl25: // 7 sources:
            ++var5_5;
        }
        this.atts.clear();
        var6_7 = 0;
        while (var6_7 < var4_4) {
            var7_8 = var2_2.getName(var6_7);
            var8_12 = var2_2.getType(var6_7);
            var9_13 = var2_2.getValue(var6_7);
            if (!var7_8.startsWith("xmlns")) ** GOTO lbl-1000
            var11_16 = var7_8.indexOf(58);
            var10_14 = var11_16 == -1 && var7_8.length() == 5 ? "" : (var11_16 != 5 ? null : var7_8.substring(6));
            if (var10_14 != null) {
                if (this.prefixes) {
                    if (this.uris) {
                        this.atts.addAttribute("http://www.w3.org/XML/1998/namespace", (String)var10_14, var7_8.intern(), var8_12, var9_13);
                    } else {
                        this.atts.addAttribute("", "", var7_8.intern(), var8_12, var9_13);
                    }
                }
            } else lbl-1000: // 2 sources:
            {
                try {
                    var10_14 = this.processName(var7_8, true, true);
                    this.atts.addAttribute(var10_14[0], (String)var10_14[1], (String)var10_14[2], var8_12, var9_13);
                }
                catch (SAXException var10_15) {
                    if (var3_3 == null) {
                        var3_3 = new Vector<SAXException>();
                    }
                    var3_3.addElement(var10_15);
                    this.atts.addAttribute("", var7_8, var7_8, var8_12, var9_13);
                }
            }
            ++var6_7;
        }
        if (var3_3 != null && this.errorHandler != null) {
            var7_9 = 0;
            while (var7_9 < var3_3.size()) {
                this.errorHandler.error((SAXParseException)var3_3.elementAt(var7_9));
                ++var7_9;
            }
        }
        if (this.contentHandler == null) return;
        var7_10 = this.processName(var1_1, false, false);
        this.contentHandler.startElement(var7_10[0], var7_10[1], var7_10[2], this.atts);
    }

    public void endElement(String string) throws SAXException {
        if (!this.namespaces) {
            if (this.contentHandler != null) {
                this.contentHandler.endElement("", "", string.intern());
            }
            return;
        }
        String[] arrstring = this.processName(string, false, false);
        if (this.contentHandler != null) {
            this.contentHandler.endElement(arrstring[0], arrstring[1], arrstring[2]);
            Enumeration enumeration = this.nsSupport.getDeclaredPrefixes();
            while (enumeration.hasMoreElements()) {
                String string2 = (String)enumeration.nextElement();
                this.contentHandler.endPrefixMapping(string2);
            }
        }
        this.nsSupport.popContext();
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.characters(arrc, n2, n3);
        }
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.ignorableWhitespace(arrc, n2, n3);
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.processingInstruction(string, string2);
        }
    }

    private void setupParser() {
        if (!this.prefixes && !this.namespaces) {
            throw new IllegalStateException();
        }
        this.nsSupport.reset();
        if (this.uris) {
            this.nsSupport.setNamespaceDeclUris(true);
        }
        if (this.entityResolver != null) {
            this.parser.setEntityResolver(this.entityResolver);
        }
        if (this.dtdHandler != null) {
            this.parser.setDTDHandler(this.dtdHandler);
        }
        if (this.errorHandler != null) {
            this.parser.setErrorHandler(this.errorHandler);
        }
        this.parser.setDocumentHandler(this);
        this.locator = null;
    }

    private String[] processName(String string, boolean bl, boolean bl2) throws SAXException {
        String[] arrstring = this.nsSupport.processName(string, this.nameParts, bl);
        if (arrstring == null) {
            if (bl2) {
                throw this.makeException("Undeclared prefix: " + string);
            }
            this.reportError("Undeclared prefix: " + string);
            arrstring = new String[3];
            arrstring[1] = "";
            arrstring[0] = "";
            arrstring[2] = string.intern();
        }
        return arrstring;
    }

    void reportError(String string) throws SAXException {
        if (this.errorHandler != null) {
            this.errorHandler.error(this.makeException(string));
        }
    }

    private SAXParseException makeException(String string) {
        if (this.locator != null) {
            return new SAXParseException(string, this.locator);
        }
        return new SAXParseException(string, null, null, -1, -1);
    }

    private void checkNotParsing(String string, String string2) throws SAXNotSupportedException {
        if (this.parsing) {
            throw new SAXNotSupportedException("Cannot change " + string + ' ' + string2 + " while parsing");
        }
    }

    static AttributesImpl access$000(ParserAdapter parserAdapter) {
        return parserAdapter.atts;
    }

    final class AttributeListAdapter
    implements Attributes {
        private AttributeList qAtts;
        private final ParserAdapter this$0;

        AttributeListAdapter(ParserAdapter parserAdapter) {
            this.this$0 = parserAdapter;
        }

        void setAttributeList(AttributeList attributeList) {
            this.qAtts = attributeList;
        }

        public int getLength() {
            return this.qAtts.getLength();
        }

        public String getURI(int n2) {
            return "";
        }

        public String getLocalName(int n2) {
            return "";
        }

        public String getQName(int n2) {
            return this.qAtts.getName(n2).intern();
        }

        public String getType(int n2) {
            return this.qAtts.getType(n2).intern();
        }

        public String getValue(int n2) {
            return this.qAtts.getValue(n2);
        }

        public int getIndex(String string) {
            int n2 = ParserAdapter.access$000(this.this$0).getLength();
            int n3 = 0;
            while (n3 < n2) {
                if (this.qAtts.getName(n3).equals(string)) {
                    return n3;
                }
                ++n3;
            }
            return -1;
        }

        public String getValue(String string) {
            return this.qAtts.getValue(string);
        }
    }

}

