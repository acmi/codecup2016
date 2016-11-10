/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.xml.validation.Schema;
import javax.xml.validation.ValidatorHandler;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.jaxp.DefaultValidationErrorHandler;
import org.apache.xerces.jaxp.JAXPConstants;
import org.apache.xerces.jaxp.JAXPValidatorComponent;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xerces.jaxp.SchemaValidatorConfiguration;
import org.apache.xerces.jaxp.UnparsedEntityHandler;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDSource;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserImpl
extends javax.xml.parsers.SAXParser
implements JAXPConstants,
PSVIProvider {
    private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
    private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    private static final String XMLSCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
    private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private final JAXPSAXParser xmlReader;
    private String schemaLanguage = null;
    private final Schema grammar;
    private final XMLComponent fSchemaValidator;
    private final XMLComponentManager fSchemaValidatorComponentManager;
    private final ValidationManager fSchemaValidationManager;
    private final UnparsedEntityHandler fUnparsedEntityHandler;
    private final ErrorHandler fInitErrorHandler;
    private final EntityResolver fInitEntityResolver;

    SAXParserImpl(SAXParserFactoryImpl sAXParserFactoryImpl, Hashtable hashtable) throws SAXException {
        this(sAXParserFactoryImpl, hashtable, false);
    }

    SAXParserImpl(SAXParserFactoryImpl sAXParserFactoryImpl, Hashtable hashtable, boolean bl) throws SAXException {
        this.xmlReader = new JAXPSAXParser(this);
        this.xmlReader.setFeature0("http://xml.org/sax/features/namespaces", sAXParserFactoryImpl.isNamespaceAware());
        this.xmlReader.setFeature0("http://xml.org/sax/features/namespace-prefixes", !sAXParserFactoryImpl.isNamespaceAware());
        if (sAXParserFactoryImpl.isXIncludeAware()) {
            this.xmlReader.setFeature0("http://apache.org/xml/features/xinclude", true);
        }
        if (bl) {
            this.xmlReader.setProperty0("http://apache.org/xml/properties/security-manager", new SecurityManager());
        }
        this.setFeatures(hashtable);
        if (sAXParserFactoryImpl.isValidating()) {
            this.fInitErrorHandler = new DefaultValidationErrorHandler();
            this.xmlReader.setErrorHandler(this.fInitErrorHandler);
        } else {
            this.fInitErrorHandler = this.xmlReader.getErrorHandler();
        }
        this.xmlReader.setFeature0("http://xml.org/sax/features/validation", sAXParserFactoryImpl.isValidating());
        this.grammar = sAXParserFactoryImpl.getSchema();
        if (this.grammar != null) {
            XMLParserConfiguration xMLParserConfiguration = this.xmlReader.getXMLParserConfiguration();
            XMLComponent xMLComponent = null;
            if (this.grammar instanceof XSGrammarPoolContainer) {
                xMLComponent = new XMLSchemaValidator();
                this.fSchemaValidationManager = new ValidationManager();
                this.fUnparsedEntityHandler = new UnparsedEntityHandler(this.fSchemaValidationManager);
                xMLParserConfiguration.setDTDHandler(this.fUnparsedEntityHandler);
                this.fUnparsedEntityHandler.setDTDHandler(this.xmlReader);
                this.xmlReader.setDTDSource(this.fUnparsedEntityHandler);
                this.fSchemaValidatorComponentManager = new SchemaValidatorConfiguration(xMLParserConfiguration, (XSGrammarPoolContainer)((Object)this.grammar), this.fSchemaValidationManager);
            } else {
                xMLComponent = new JAXPValidatorComponent(this.grammar.newValidatorHandler());
                this.fSchemaValidationManager = null;
                this.fUnparsedEntityHandler = null;
                this.fSchemaValidatorComponentManager = xMLParserConfiguration;
            }
            xMLParserConfiguration.addRecognizedFeatures(xMLComponent.getRecognizedFeatures());
            xMLParserConfiguration.addRecognizedProperties(xMLComponent.getRecognizedProperties());
            xMLParserConfiguration.setDocumentHandler((XMLDocumentHandler)((Object)xMLComponent));
            ((XMLDocumentSource)((Object)xMLComponent)).setDocumentHandler(this.xmlReader);
            this.xmlReader.setDocumentSource((XMLDocumentSource)((Object)xMLComponent));
            this.fSchemaValidator = xMLComponent;
        } else {
            this.fSchemaValidationManager = null;
            this.fUnparsedEntityHandler = null;
            this.fSchemaValidatorComponentManager = null;
            this.fSchemaValidator = null;
        }
        this.fInitEntityResolver = this.xmlReader.getEntityResolver();
    }

    private void setFeatures(Hashtable hashtable) throws SAXNotSupportedException, SAXNotRecognizedException {
        if (hashtable != null) {
            Iterator iterator = hashtable.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                String string = (String)entry.getKey();
                boolean bl = (Boolean)entry.getValue();
                this.xmlReader.setFeature0(string, bl);
            }
        }
    }

    public Parser getParser() throws SAXException {
        return this.xmlReader;
    }

    public XMLReader getXMLReader() {
        return this.xmlReader;
    }

    public boolean isNamespaceAware() {
        try {
            return this.xmlReader.getFeature("http://xml.org/sax/features/namespaces");
        }
        catch (SAXException sAXException) {
            throw new IllegalStateException(sAXException.getMessage());
        }
    }

    public boolean isValidating() {
        try {
            return this.xmlReader.getFeature("http://xml.org/sax/features/validation");
        }
        catch (SAXException sAXException) {
            throw new IllegalStateException(sAXException.getMessage());
        }
    }

    public boolean isXIncludeAware() {
        try {
            return this.xmlReader.getFeature("http://apache.org/xml/features/xinclude");
        }
        catch (SAXException sAXException) {
            return false;
        }
    }

    public void setProperty(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        this.xmlReader.setProperty(string, object);
    }

    public Object getProperty(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.xmlReader.getProperty(string);
    }

    public void parse(InputSource inputSource, DefaultHandler defaultHandler) throws SAXException, IOException {
        if (inputSource == null) {
            throw new IllegalArgumentException();
        }
        if (defaultHandler != null) {
            this.xmlReader.setContentHandler(defaultHandler);
            this.xmlReader.setEntityResolver(defaultHandler);
            this.xmlReader.setErrorHandler(defaultHandler);
            this.xmlReader.setDTDHandler(defaultHandler);
            this.xmlReader.setDocumentHandler(null);
        }
        this.xmlReader.parse(inputSource);
    }

    public void parse(InputSource inputSource, HandlerBase handlerBase) throws SAXException, IOException {
        if (inputSource == null) {
            throw new IllegalArgumentException();
        }
        if (handlerBase != null) {
            this.xmlReader.setDocumentHandler(handlerBase);
            this.xmlReader.setEntityResolver(handlerBase);
            this.xmlReader.setErrorHandler(handlerBase);
            this.xmlReader.setDTDHandler(handlerBase);
            this.xmlReader.setContentHandler(null);
        }
        this.xmlReader.parse(inputSource);
    }

    public Schema getSchema() {
        return this.grammar;
    }

    public void reset() {
        try {
            this.xmlReader.restoreInitState();
        }
        catch (SAXException sAXException) {
            // empty catch block
        }
        this.xmlReader.setContentHandler(null);
        this.xmlReader.setDTDHandler(null);
        if (this.xmlReader.getErrorHandler() != this.fInitErrorHandler) {
            this.xmlReader.setErrorHandler(this.fInitErrorHandler);
        }
        if (this.xmlReader.getEntityResolver() != this.fInitEntityResolver) {
            this.xmlReader.setEntityResolver(this.fInitEntityResolver);
        }
    }

    public ElementPSVI getElementPSVI() {
        return this.xmlReader.getElementPSVI();
    }

    public AttributePSVI getAttributePSVI(int n2) {
        return this.xmlReader.getAttributePSVI(n2);
    }

    public AttributePSVI getAttributePSVIByName(String string, String string2) {
        return this.xmlReader.getAttributePSVIByName(string, string2);
    }

    static XMLComponent access$000(SAXParserImpl sAXParserImpl) {
        return sAXParserImpl.fSchemaValidator;
    }

    static Schema access$100(SAXParserImpl sAXParserImpl) {
        return sAXParserImpl.grammar;
    }

    static String access$202(SAXParserImpl sAXParserImpl, String string) {
        sAXParserImpl.schemaLanguage = string;
        return sAXParserImpl.schemaLanguage;
    }

    static String access$200(SAXParserImpl sAXParserImpl) {
        return sAXParserImpl.schemaLanguage;
    }

    static ValidationManager access$300(SAXParserImpl sAXParserImpl) {
        return sAXParserImpl.fSchemaValidationManager;
    }

    static UnparsedEntityHandler access$400(SAXParserImpl sAXParserImpl) {
        return sAXParserImpl.fUnparsedEntityHandler;
    }

    static XMLComponentManager access$500(SAXParserImpl sAXParserImpl) {
        return sAXParserImpl.fSchemaValidatorComponentManager;
    }

    public static class JAXPSAXParser
    extends SAXParser {
        private final HashMap fInitFeatures = new HashMap();
        private final HashMap fInitProperties = new HashMap();
        private final SAXParserImpl fSAXParser;

        public JAXPSAXParser() {
            this(null);
        }

        JAXPSAXParser(SAXParserImpl sAXParserImpl) {
            this.fSAXParser = sAXParserImpl;
        }

        public synchronized void setFeature(String string, boolean bl) throws SAXNotRecognizedException, SAXNotSupportedException {
            if (string == null) {
                throw new NullPointerException();
            }
            if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
                block8 : {
                    try {
                        this.setProperty("http://apache.org/xml/properties/security-manager", bl ? new SecurityManager() : null);
                    }
                    catch (SAXNotRecognizedException sAXNotRecognizedException) {
                        if (bl) {
                            throw sAXNotRecognizedException;
                        }
                    }
                    catch (SAXNotSupportedException sAXNotSupportedException) {
                        if (!bl) break block8;
                        throw sAXNotSupportedException;
                    }
                }
                return;
            }
            if (!this.fInitFeatures.containsKey(string)) {
                boolean bl2 = super.getFeature(string);
                this.fInitFeatures.put(string, bl2 ? Boolean.TRUE : Boolean.FALSE);
            }
            if (this.fSAXParser != null && SAXParserImpl.access$000(this.fSAXParser) != null) {
                this.setSchemaValidatorFeature(string, bl);
            }
            super.setFeature(string, bl);
        }

        public synchronized boolean getFeature(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
            if (string == null) {
                throw new NullPointerException();
            }
            if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
                try {
                    return super.getProperty("http://apache.org/xml/properties/security-manager") != null;
                }
                catch (SAXException sAXException) {
                    return false;
                }
            }
            return super.getFeature(string);
        }

        public synchronized void setProperty(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
            if (string == null) {
                throw new NullPointerException();
            }
            if (this.fSAXParser != null) {
                if ("http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(string)) {
                    if (SAXParserImpl.access$100(this.fSAXParser) != null) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-already-specified", new Object[]{string}));
                    }
                    if ("http://www.w3.org/2001/XMLSchema".equals(object)) {
                        if (this.fSAXParser.isValidating()) {
                            SAXParserImpl.access$202(this.fSAXParser, "http://www.w3.org/2001/XMLSchema");
                            this.setFeature("http://apache.org/xml/features/validation/schema", true);
                            if (!this.fInitProperties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaLanguage")) {
                                this.fInitProperties.put("http://java.sun.com/xml/jaxp/properties/schemaLanguage", super.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage"));
                            }
                            super.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
                        }
                    } else if (object == null) {
                        SAXParserImpl.access$202(this.fSAXParser, null);
                        this.setFeature("http://apache.org/xml/features/validation/schema", false);
                    } else {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-not-supported", null));
                    }
                    return;
                }
                if ("http://java.sun.com/xml/jaxp/properties/schemaSource".equals(string)) {
                    if (SAXParserImpl.access$100(this.fSAXParser) != null) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-already-specified", new Object[]{string}));
                    }
                    String string2 = (String)this.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
                    if (string2 != null && "http://www.w3.org/2001/XMLSchema".equals(string2)) {
                        if (!this.fInitProperties.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
                            this.fInitProperties.put("http://java.sun.com/xml/jaxp/properties/schemaSource", super.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource"));
                        }
                    } else {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "jaxp-order-not-supported", new Object[]{"http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://java.sun.com/xml/jaxp/properties/schemaSource"}));
                    }
                    super.setProperty(string, object);
                    return;
                }
            }
            if (!this.fInitProperties.containsKey(string)) {
                this.fInitProperties.put(string, super.getProperty(string));
            }
            if (this.fSAXParser != null && SAXParserImpl.access$000(this.fSAXParser) != null) {
                this.setSchemaValidatorProperty(string, object);
            }
            super.setProperty(string, object);
        }

        public synchronized Object getProperty(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
            if (string == null) {
                throw new NullPointerException();
            }
            if (this.fSAXParser != null && "http://java.sun.com/xml/jaxp/properties/schemaLanguage".equals(string)) {
                return SAXParserImpl.access$200(this.fSAXParser);
            }
            return super.getProperty(string);
        }

        synchronized void restoreInitState() throws SAXNotRecognizedException, SAXNotSupportedException {
            String string;
            Iterator iterator;
            Map.Entry entry;
            if (!this.fInitFeatures.isEmpty()) {
                iterator = this.fInitFeatures.entrySet().iterator();
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    string = (String)entry.getKey();
                    boolean bl = (Boolean)entry.getValue();
                    super.setFeature(string, bl);
                }
                this.fInitFeatures.clear();
            }
            if (!this.fInitProperties.isEmpty()) {
                iterator = this.fInitProperties.entrySet().iterator();
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    string = (String)entry.getKey();
                    Object v2 = entry.getValue();
                    super.setProperty(string, v2);
                }
                this.fInitProperties.clear();
            }
        }

        public void parse(InputSource inputSource) throws SAXException, IOException {
            if (this.fSAXParser != null && SAXParserImpl.access$000(this.fSAXParser) != null) {
                if (SAXParserImpl.access$300(this.fSAXParser) != null) {
                    SAXParserImpl.access$300(this.fSAXParser).reset();
                    SAXParserImpl.access$400(this.fSAXParser).reset();
                }
                this.resetSchemaValidator();
            }
            super.parse(inputSource);
        }

        public void parse(String string) throws SAXException, IOException {
            if (this.fSAXParser != null && SAXParserImpl.access$000(this.fSAXParser) != null) {
                if (SAXParserImpl.access$300(this.fSAXParser) != null) {
                    SAXParserImpl.access$300(this.fSAXParser).reset();
                    SAXParserImpl.access$400(this.fSAXParser).reset();
                }
                this.resetSchemaValidator();
            }
            super.parse(string);
        }

        XMLParserConfiguration getXMLParserConfiguration() {
            return this.fConfiguration;
        }

        void setFeature0(String string, boolean bl) throws SAXNotRecognizedException, SAXNotSupportedException {
            super.setFeature(string, bl);
        }

        boolean getFeature0(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
            return super.getFeature(string);
        }

        void setProperty0(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
            super.setProperty(string, object);
        }

        Object getProperty0(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
            return super.getProperty(string);
        }

        private void setSchemaValidatorFeature(String string, boolean bl) throws SAXNotRecognizedException, SAXNotSupportedException {
            try {
                SAXParserImpl.access$000(this.fSAXParser).setFeature(string, bl);
            }
            catch (XMLConfigurationException xMLConfigurationException) {
                String string2 = xMLConfigurationException.getIdentifier();
                if (xMLConfigurationException.getType() == 0) {
                    throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{string2}));
                }
                throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[]{string2}));
            }
        }

        private void setSchemaValidatorProperty(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
            try {
                SAXParserImpl.access$000(this.fSAXParser).setProperty(string, object);
            }
            catch (XMLConfigurationException xMLConfigurationException) {
                String string2 = xMLConfigurationException.getIdentifier();
                if (xMLConfigurationException.getType() == 0) {
                    throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{string2}));
                }
                throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[]{string2}));
            }
        }

        private void resetSchemaValidator() throws SAXException {
            try {
                SAXParserImpl.access$000(this.fSAXParser).reset(SAXParserImpl.access$500(this.fSAXParser));
            }
            catch (XMLConfigurationException xMLConfigurationException) {
                throw new SAXException(xMLConfigurationException);
            }
        }
    }

}

