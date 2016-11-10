/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.parsers;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.parsers.AbstractXMLDocumentParser;
import org.apache.xerces.parsers.XML11Configurable;
import org.apache.xerces.util.EntityResolver2Wrapper;
import org.apache.xerces.util.EntityResolverWrapper;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
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
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;
import org.xml.sax.ext.Locator2Impl;

public abstract class AbstractSAXParser
extends AbstractXMLDocumentParser
implements PSVIProvider,
Parser,
XMLReader {
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/string-interning"};
    protected static final String LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
    protected static final String DECLARATION_HANDLER = "http://xml.org/sax/properties/declaration-handler";
    protected static final String DOM_NODE = "http://xml.org/sax/properties/dom-node";
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://xml.org/sax/properties/lexical-handler", "http://xml.org/sax/properties/declaration-handler", "http://xml.org/sax/properties/dom-node"};
    protected boolean fNamespaces;
    protected boolean fNamespacePrefixes = false;
    protected boolean fLexicalHandlerParameterEntities = true;
    protected boolean fStandalone;
    protected boolean fResolveDTDURIs = true;
    protected boolean fUseEntityResolver2 = true;
    protected boolean fXMLNSURIs = false;
    protected ContentHandler fContentHandler;
    protected DocumentHandler fDocumentHandler;
    protected NamespaceContext fNamespaceContext;
    protected DTDHandler fDTDHandler;
    protected DeclHandler fDeclHandler;
    protected LexicalHandler fLexicalHandler;
    protected final QName fQName = new QName();
    protected boolean fParseInProgress = false;
    protected String fVersion;
    private final AttributesProxy fAttributesProxy = new AttributesProxy();
    private Augmentations fAugmentations = null;
    protected SymbolHash fDeclaredAttrs = null;

    protected AbstractSAXParser(XMLParserConfiguration xMLParserConfiguration) {
        super(xMLParserConfiguration);
        xMLParserConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
        xMLParserConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
        try {
            xMLParserConfiguration.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", false);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
    }

    public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
        this.fNamespaceContext = namespaceContext;
        try {
            if (this.fDocumentHandler != null) {
                if (xMLLocator != null) {
                    this.fDocumentHandler.setDocumentLocator(new LocatorProxy(xMLLocator));
                }
                this.fDocumentHandler.startDocument();
            }
            if (this.fContentHandler != null) {
                if (xMLLocator != null) {
                    this.fContentHandler.setDocumentLocator(new LocatorProxy(xMLLocator));
                }
                this.fContentHandler.startDocument();
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void xmlDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        this.fVersion = string;
        this.fStandalone = "yes".equals(string3);
    }

    public void doctypeDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        this.fInDTD = true;
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.startDTD(string, string2, string3);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
        if (this.fDeclHandler != null) {
            this.fDeclaredAttrs = new SymbolHash();
        }
    }

    public void startGeneralEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        try {
            if (augmentations != null && Boolean.TRUE.equals(augmentations.getItem("ENTITY_SKIPPED"))) {
                if (this.fContentHandler != null) {
                    this.fContentHandler.skippedEntity(string);
                }
            } else if (this.fLexicalHandler != null) {
                this.fLexicalHandler.startEntity(string);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void endGeneralEntity(String string, Augmentations augmentations) throws XNIException {
        try {
            if (!(augmentations != null && Boolean.TRUE.equals(augmentations.getItem("ENTITY_SKIPPED")) || this.fLexicalHandler == null)) {
                this.fLexicalHandler.endEntity(string);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        block11 : {
            try {
                if (this.fDocumentHandler != null) {
                    this.fAttributesProxy.setAttributes(xMLAttributes);
                    this.fDocumentHandler.startElement(qName.rawname, this.fAttributesProxy);
                }
                if (this.fContentHandler == null) break block11;
                if (this.fNamespaces) {
                    int n2;
                    this.startNamespaceMapping();
                    int n3 = xMLAttributes.getLength();
                    if (!this.fNamespacePrefixes) {
                        n2 = n3 - 1;
                        while (n2 >= 0) {
                            xMLAttributes.getName(n2, this.fQName);
                            if (this.fQName.prefix == XMLSymbols.PREFIX_XMLNS || this.fQName.rawname == XMLSymbols.PREFIX_XMLNS) {
                                xMLAttributes.removeAttributeAt(n2);
                            }
                            --n2;
                        }
                    } else if (!this.fXMLNSURIs) {
                        n2 = n3 - 1;
                        while (n2 >= 0) {
                            xMLAttributes.getName(n2, this.fQName);
                            if (this.fQName.prefix == XMLSymbols.PREFIX_XMLNS || this.fQName.rawname == XMLSymbols.PREFIX_XMLNS) {
                                this.fQName.prefix = "";
                                this.fQName.uri = "";
                                this.fQName.localpart = "";
                                xMLAttributes.setName(n2, this.fQName);
                            }
                            --n2;
                        }
                    }
                }
                this.fAugmentations = augmentations;
                String string = qName.uri != null ? qName.uri : "";
                String string2 = this.fNamespaces ? qName.localpart : "";
                this.fAttributesProxy.setAttributes(xMLAttributes);
                this.fContentHandler.startElement(string, string2, qName.rawname, this.fAttributesProxy);
            }
            catch (SAXException sAXException) {
                throw new XNIException(sAXException);
            }
        }
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (xMLString.length == 0) {
            return;
        }
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.characters(xMLString.ch, xMLString.offset, xMLString.length);
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.characters(xMLString.ch, xMLString.offset, xMLString.length);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.ignorableWhitespace(xMLString.ch, xMLString.offset, xMLString.length);
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.ignorableWhitespace(xMLString.ch, xMLString.offset, xMLString.length);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.endElement(qName.rawname);
            }
            if (this.fContentHandler != null) {
                this.fAugmentations = augmentations;
                String string = qName.uri != null ? qName.uri : "";
                String string2 = this.fNamespaces ? qName.localpart : "";
                this.fContentHandler.endElement(string, string2, qName.rawname);
                if (this.fNamespaces) {
                    this.endNamespaceMapping();
                }
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.startCDATA();
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.endCDATA();
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.comment(xMLString.ch, 0, xMLString.length);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.processingInstruction(string, xMLString.toString());
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.processingInstruction(string, xMLString.toString());
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.endDocument();
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.endDocument();
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void startExternalSubset(XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        this.startParameterEntity("[dtd]", null, null, augmentations);
    }

    public void endExternalSubset(Augmentations augmentations) throws XNIException {
        this.endParameterEntity("[dtd]", augmentations);
    }

    public void startParameterEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        try {
            if (augmentations != null && Boolean.TRUE.equals(augmentations.getItem("ENTITY_SKIPPED"))) {
                if (this.fContentHandler != null) {
                    this.fContentHandler.skippedEntity(string);
                }
            } else if (this.fLexicalHandler != null && this.fLexicalHandlerParameterEntities) {
                this.fLexicalHandler.startEntity(string);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void endParameterEntity(String string, Augmentations augmentations) throws XNIException {
        try {
            if ((augmentations == null || !Boolean.TRUE.equals(augmentations.getItem("ENTITY_SKIPPED"))) && this.fLexicalHandler != null && this.fLexicalHandlerParameterEntities) {
                this.fLexicalHandler.endEntity(string);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void elementDecl(String string, String string2, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                this.fDeclHandler.elementDecl(string, string2);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void attributeDecl(String string, String string2, String string3, String[] arrstring, String string4, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                StringBuffer stringBuffer;
                String string5 = string + "<" + string2;
                if (this.fDeclaredAttrs.get(string5) != null) {
                    return;
                }
                this.fDeclaredAttrs.put(string5, Boolean.TRUE);
                if (string3.equals("NOTATION") || string3.equals("ENUMERATION")) {
                    stringBuffer = new StringBuffer();
                    if (string3.equals("NOTATION")) {
                        stringBuffer.append(string3);
                        stringBuffer.append(" (");
                    } else {
                        stringBuffer.append("(");
                    }
                    int n2 = 0;
                    while (n2 < arrstring.length) {
                        stringBuffer.append(arrstring[n2]);
                        if (n2 < arrstring.length - 1) {
                            stringBuffer.append('|');
                        }
                        ++n2;
                    }
                    stringBuffer.append(')');
                    string3 = stringBuffer.toString();
                }
                stringBuffer = xMLString == null ? null : xMLString.toString();
                this.fDeclHandler.attributeDecl(string, string2, string3, string4, (String)((Object)stringBuffer));
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void internalEntityDecl(String string, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                this.fDeclHandler.internalEntityDecl(string, xMLString.toString());
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void externalEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                String string2 = xMLResourceIdentifier.getPublicId();
                String string3 = this.fResolveDTDURIs ? xMLResourceIdentifier.getExpandedSystemId() : xMLResourceIdentifier.getLiteralSystemId();
                this.fDeclHandler.externalEntityDecl(string, string2, string3);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void unparsedEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDTDHandler != null) {
                String string3 = xMLResourceIdentifier.getPublicId();
                String string4 = this.fResolveDTDURIs ? xMLResourceIdentifier.getExpandedSystemId() : xMLResourceIdentifier.getLiteralSystemId();
                this.fDTDHandler.unparsedEntityDecl(string, string3, string4, string2);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void notationDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDTDHandler != null) {
                String string2 = xMLResourceIdentifier.getPublicId();
                String string3 = this.fResolveDTDURIs ? xMLResourceIdentifier.getExpandedSystemId() : xMLResourceIdentifier.getLiteralSystemId();
                this.fDTDHandler.notationDecl(string, string2, string3);
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
    }

    public void endDTD(Augmentations augmentations) throws XNIException {
        this.fInDTD = false;
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.endDTD();
            }
        }
        catch (SAXException sAXException) {
            throw new XNIException(sAXException);
        }
        if (this.fDeclaredAttrs != null) {
            this.fDeclaredAttrs.clear();
        }
    }

    public void parse(String string) throws SAXException, IOException {
        XMLInputSource xMLInputSource = new XMLInputSource(null, string, null);
        try {
            this.parse(xMLInputSource);
        }
        catch (XMLParseException xMLParseException) {
            Exception exception = xMLParseException.getException();
            if (exception == null || exception instanceof CharConversionException) {
                Locator2Impl locator2Impl = new Locator2Impl();
                locator2Impl.setXMLVersion(this.fVersion);
                locator2Impl.setPublicId(xMLParseException.getPublicId());
                locator2Impl.setSystemId(xMLParseException.getExpandedSystemId());
                locator2Impl.setLineNumber(xMLParseException.getLineNumber());
                locator2Impl.setColumnNumber(xMLParseException.getColumnNumber());
                throw exception == null ? new SAXParseException(xMLParseException.getMessage(), locator2Impl) : new SAXParseException(xMLParseException.getMessage(), locator2Impl, exception);
            }
            if (exception instanceof SAXException) {
                throw (SAXException)exception;
            }
            if (exception instanceof IOException) {
                throw (IOException)exception;
            }
            throw new SAXException(exception);
        }
        catch (XNIException xNIException) {
            Exception exception = xNIException.getException();
            if (exception == null) {
                throw new SAXException(xNIException.getMessage());
            }
            if (exception instanceof SAXException) {
                throw (SAXException)exception;
            }
            if (exception instanceof IOException) {
                throw (IOException)exception;
            }
            throw new SAXException(exception);
        }
    }

    public void parse(InputSource inputSource) throws SAXException, IOException {
        try {
            XMLInputSource xMLInputSource = new XMLInputSource(inputSource.getPublicId(), inputSource.getSystemId(), null);
            xMLInputSource.setByteStream(inputSource.getByteStream());
            xMLInputSource.setCharacterStream(inputSource.getCharacterStream());
            xMLInputSource.setEncoding(inputSource.getEncoding());
            this.parse(xMLInputSource);
        }
        catch (XMLParseException xMLParseException) {
            Exception exception = xMLParseException.getException();
            if (exception == null || exception instanceof CharConversionException) {
                Locator2Impl locator2Impl = new Locator2Impl();
                locator2Impl.setXMLVersion(this.fVersion);
                locator2Impl.setPublicId(xMLParseException.getPublicId());
                locator2Impl.setSystemId(xMLParseException.getExpandedSystemId());
                locator2Impl.setLineNumber(xMLParseException.getLineNumber());
                locator2Impl.setColumnNumber(xMLParseException.getColumnNumber());
                throw exception == null ? new SAXParseException(xMLParseException.getMessage(), locator2Impl) : new SAXParseException(xMLParseException.getMessage(), locator2Impl, exception);
            }
            if (exception instanceof SAXException) {
                throw (SAXException)exception;
            }
            if (exception instanceof IOException) {
                throw (IOException)exception;
            }
            throw new SAXException(exception);
        }
        catch (XNIException xNIException) {
            Exception exception = xNIException.getException();
            if (exception == null) {
                throw new SAXException(xNIException.getMessage());
            }
            if (exception instanceof SAXException) {
                throw (SAXException)exception;
            }
            if (exception instanceof IOException) {
                throw (IOException)exception;
            }
            throw new SAXException(exception);
        }
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        try {
            XMLEntityResolver xMLEntityResolver = (XMLEntityResolver)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
            if (this.fUseEntityResolver2 && entityResolver instanceof EntityResolver2) {
                if (xMLEntityResolver instanceof EntityResolver2Wrapper) {
                    EntityResolver2Wrapper entityResolver2Wrapper = (EntityResolver2Wrapper)xMLEntityResolver;
                    entityResolver2Wrapper.setEntityResolver((EntityResolver2)entityResolver);
                } else {
                    this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2)entityResolver));
                }
            } else if (xMLEntityResolver instanceof EntityResolverWrapper) {
                EntityResolverWrapper entityResolverWrapper = (EntityResolverWrapper)xMLEntityResolver;
                entityResolverWrapper.setEntityResolver(entityResolver);
            } else {
                this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(entityResolver));
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
    }

    public EntityResolver getEntityResolver() {
        EntityResolver entityResolver = null;
        try {
            XMLEntityResolver xMLEntityResolver = (XMLEntityResolver)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
            if (xMLEntityResolver != null) {
                if (xMLEntityResolver instanceof EntityResolverWrapper) {
                    entityResolver = ((EntityResolverWrapper)xMLEntityResolver).getEntityResolver();
                } else if (xMLEntityResolver instanceof EntityResolver2Wrapper) {
                    entityResolver = ((EntityResolver2Wrapper)xMLEntityResolver).getEntityResolver();
                }
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
        return entityResolver;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        try {
            XMLErrorHandler xMLErrorHandler = (XMLErrorHandler)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
            if (xMLErrorHandler instanceof ErrorHandlerWrapper) {
                ErrorHandlerWrapper errorHandlerWrapper = (ErrorHandlerWrapper)xMLErrorHandler;
                errorHandlerWrapper.setErrorHandler(errorHandler);
            } else {
                this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(errorHandler));
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
    }

    public ErrorHandler getErrorHandler() {
        ErrorHandler errorHandler = null;
        try {
            XMLErrorHandler xMLErrorHandler = (XMLErrorHandler)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
            if (xMLErrorHandler != null && xMLErrorHandler instanceof ErrorHandlerWrapper) {
                errorHandler = ((ErrorHandlerWrapper)xMLErrorHandler).getErrorHandler();
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
        return errorHandler;
    }

    public void setLocale(Locale locale) throws SAXException {
        this.fConfiguration.setLocale(locale);
    }

    public void setDTDHandler(DTDHandler dTDHandler) {
        this.fDTDHandler = dTDHandler;
    }

    public void setDocumentHandler(DocumentHandler documentHandler) {
        this.fDocumentHandler = documentHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.fContentHandler = contentHandler;
    }

    public ContentHandler getContentHandler() {
        return this.fContentHandler;
    }

    public DTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    public void setFeature(String string, boolean bl) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (string.startsWith("http://xml.org/sax/features/")) {
                int n2 = string.length() - "http://xml.org/sax/features/".length();
                if (n2 == "namespaces".length() && string.endsWith("namespaces")) {
                    this.fConfiguration.setFeature(string, bl);
                    this.fNamespaces = bl;
                    return;
                }
                if (n2 == "namespace-prefixes".length() && string.endsWith("namespace-prefixes")) {
                    this.fNamespacePrefixes = bl;
                    return;
                }
                if (n2 == "string-interning".length() && string.endsWith("string-interning")) {
                    if (!bl) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "false-not-supported", new Object[]{string}));
                    }
                    return;
                }
                if (n2 == "lexical-handler/parameter-entities".length() && string.endsWith("lexical-handler/parameter-entities")) {
                    this.fLexicalHandlerParameterEntities = bl;
                    return;
                }
                if (n2 == "resolve-dtd-uris".length() && string.endsWith("resolve-dtd-uris")) {
                    this.fResolveDTDURIs = bl;
                    return;
                }
                if (n2 == "unicode-normalization-checking".length() && string.endsWith("unicode-normalization-checking")) {
                    if (bl) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "true-not-supported", new Object[]{string}));
                    }
                    return;
                }
                if (n2 == "xmlns-uris".length() && string.endsWith("xmlns-uris")) {
                    this.fXMLNSURIs = bl;
                    return;
                }
                if (n2 == "use-entity-resolver2".length() && string.endsWith("use-entity-resolver2")) {
                    if (bl != this.fUseEntityResolver2) {
                        this.fUseEntityResolver2 = bl;
                        this.setEntityResolver(this.getEntityResolver());
                    }
                    return;
                }
                if (n2 == "is-standalone".length() && string.endsWith("is-standalone") || n2 == "use-attributes2".length() && string.endsWith("use-attributes2") || n2 == "use-locator2".length() && string.endsWith("use-locator2") || n2 == "xml-1.1".length() && string.endsWith("xml-1.1")) {
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-read-only", new Object[]{string}));
                }
            }
            this.fConfiguration.setFeature(string, bl);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            String string2 = xMLConfigurationException.getIdentifier();
            if (xMLConfigurationException.getType() == 0) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{string2}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[]{string2}));
        }
    }

    public boolean getFeature(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (string.startsWith("http://xml.org/sax/features/")) {
                int n2 = string.length() - "http://xml.org/sax/features/".length();
                if (n2 == "namespace-prefixes".length() && string.endsWith("namespace-prefixes")) {
                    return this.fNamespacePrefixes;
                }
                if (n2 == "string-interning".length() && string.endsWith("string-interning")) {
                    return true;
                }
                if (n2 == "is-standalone".length() && string.endsWith("is-standalone")) {
                    return this.fStandalone;
                }
                if (n2 == "xml-1.1".length() && string.endsWith("xml-1.1")) {
                    return this.fConfiguration instanceof XML11Configurable;
                }
                if (n2 == "lexical-handler/parameter-entities".length() && string.endsWith("lexical-handler/parameter-entities")) {
                    return this.fLexicalHandlerParameterEntities;
                }
                if (n2 == "resolve-dtd-uris".length() && string.endsWith("resolve-dtd-uris")) {
                    return this.fResolveDTDURIs;
                }
                if (n2 == "xmlns-uris".length() && string.endsWith("xmlns-uris")) {
                    return this.fXMLNSURIs;
                }
                if (n2 == "unicode-normalization-checking".length() && string.endsWith("unicode-normalization-checking")) {
                    return false;
                }
                if (n2 == "use-entity-resolver2".length() && string.endsWith("use-entity-resolver2")) {
                    return this.fUseEntityResolver2;
                }
                if (n2 == "use-attributes2".length() && string.endsWith("use-attributes2") || n2 == "use-locator2".length() && string.endsWith("use-locator2")) {
                    return true;
                }
            }
            return this.fConfiguration.getFeature(string);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            String string2 = xMLConfigurationException.getIdentifier();
            if (xMLConfigurationException.getType() == 0) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{string2}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[]{string2}));
        }
    }

    public void setProperty(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (string.startsWith("http://xml.org/sax/properties/")) {
                int n2 = string.length() - "http://xml.org/sax/properties/".length();
                if (n2 == "lexical-handler".length() && string.endsWith("lexical-handler")) {
                    try {
                        this.setLexicalHandler((LexicalHandler)object);
                    }
                    catch (ClassCastException classCastException) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "incompatible-class", new Object[]{string, "org.xml.sax.ext.LexicalHandler"}));
                    }
                    return;
                }
                if (n2 == "declaration-handler".length() && string.endsWith("declaration-handler")) {
                    try {
                        this.setDeclHandler((DeclHandler)object);
                    }
                    catch (ClassCastException classCastException) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "incompatible-class", new Object[]{string, "org.xml.sax.ext.DeclHandler"}));
                    }
                    return;
                }
                if (n2 == "dom-node".length() && string.endsWith("dom-node") || n2 == "document-xml-version".length() && string.endsWith("document-xml-version")) {
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-read-only", new Object[]{string}));
                }
            }
            this.fConfiguration.setProperty(string, object);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            String string2 = xMLConfigurationException.getIdentifier();
            if (xMLConfigurationException.getType() == 0) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{string2}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[]{string2}));
        }
    }

    public Object getProperty(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (string.startsWith("http://xml.org/sax/properties/")) {
                int n2 = string.length() - "http://xml.org/sax/properties/".length();
                if (n2 == "document-xml-version".length() && string.endsWith("document-xml-version")) {
                    return this.fVersion;
                }
                if (n2 == "lexical-handler".length() && string.endsWith("lexical-handler")) {
                    return this.getLexicalHandler();
                }
                if (n2 == "declaration-handler".length() && string.endsWith("declaration-handler")) {
                    return this.getDeclHandler();
                }
                if (n2 == "dom-node".length() && string.endsWith("dom-node")) {
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "dom-node-read-not-supported", null));
                }
            }
            return this.fConfiguration.getProperty(string);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            String string2 = xMLConfigurationException.getIdentifier();
            if (xMLConfigurationException.getType() == 0) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{string2}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[]{string2}));
        }
    }

    protected void setDeclHandler(DeclHandler declHandler) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (this.fParseInProgress) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[]{"http://xml.org/sax/properties/declaration-handler"}));
        }
        this.fDeclHandler = declHandler;
    }

    protected DeclHandler getDeclHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.fDeclHandler;
    }

    protected void setLexicalHandler(LexicalHandler lexicalHandler) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (this.fParseInProgress) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[]{"http://xml.org/sax/properties/lexical-handler"}));
        }
        this.fLexicalHandler = lexicalHandler;
    }

    protected LexicalHandler getLexicalHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.fLexicalHandler;
    }

    protected final void startNamespaceMapping() throws SAXException {
        int n2 = this.fNamespaceContext.getDeclaredPrefixCount();
        if (n2 > 0) {
            String string = null;
            String string2 = null;
            int n3 = 0;
            while (n3 < n2) {
                string2 = this.fNamespaceContext.getURI(string = this.fNamespaceContext.getDeclaredPrefixAt(n3));
                this.fContentHandler.startPrefixMapping(string, string2 == null ? "" : string2);
                ++n3;
            }
        }
    }

    protected final void endNamespaceMapping() throws SAXException {
        int n2 = this.fNamespaceContext.getDeclaredPrefixCount();
        if (n2 > 0) {
            int n3 = 0;
            while (n3 < n2) {
                this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(n3));
                ++n3;
            }
        }
    }

    public void reset() throws XNIException {
        super.reset();
        this.fInDTD = false;
        this.fVersion = "1.0";
        this.fStandalone = false;
        this.fNamespaces = this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
        this.fAugmentations = null;
        this.fDeclaredAttrs = null;
    }

    public ElementPSVI getElementPSVI() {
        return this.fAugmentations != null ? (ElementPSVI)this.fAugmentations.getItem("ELEMENT_PSVI") : null;
    }

    public AttributePSVI getAttributePSVI(int n2) {
        return (AttributePSVI)this.fAttributesProxy.fAttributes.getAugmentations(n2).getItem("ATTRIBUTE_PSVI");
    }

    public AttributePSVI getAttributePSVIByName(String string, String string2) {
        return (AttributePSVI)this.fAttributesProxy.fAttributes.getAugmentations(string, string2).getItem("ATTRIBUTE_PSVI");
    }

    protected static final class AttributesProxy
    implements AttributeList,
    Attributes2 {
        protected XMLAttributes fAttributes;

        protected AttributesProxy() {
        }

        public void setAttributes(XMLAttributes xMLAttributes) {
            this.fAttributes = xMLAttributes;
        }

        public int getLength() {
            return this.fAttributes.getLength();
        }

        public String getName(int n2) {
            return this.fAttributes.getQName(n2);
        }

        public String getQName(int n2) {
            return this.fAttributes.getQName(n2);
        }

        public String getURI(int n2) {
            String string = this.fAttributes.getURI(n2);
            return string != null ? string : "";
        }

        public String getLocalName(int n2) {
            return this.fAttributes.getLocalName(n2);
        }

        public String getType(int n2) {
            return this.fAttributes.getType(n2);
        }

        public String getType(String string) {
            return this.fAttributes.getType(string);
        }

        public String getType(String string, String string2) {
            return string.length() == 0 ? this.fAttributes.getType(null, string2) : this.fAttributes.getType(string, string2);
        }

        public String getValue(int n2) {
            return this.fAttributes.getValue(n2);
        }

        public String getValue(String string) {
            return this.fAttributes.getValue(string);
        }

        public String getValue(String string, String string2) {
            return string.length() == 0 ? this.fAttributes.getValue(null, string2) : this.fAttributes.getValue(string, string2);
        }

        public int getIndex(String string) {
            return this.fAttributes.getIndex(string);
        }

        public int getIndex(String string, String string2) {
            return string.length() == 0 ? this.fAttributes.getIndex(null, string2) : this.fAttributes.getIndex(string, string2);
        }

        public boolean isDeclared(int n2) {
            if (n2 < 0 || n2 >= this.fAttributes.getLength()) {
                throw new ArrayIndexOutOfBoundsException(n2);
            }
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(n2).getItem("ATTRIBUTE_DECLARED"));
        }

        public boolean isDeclared(String string) {
            int n2 = this.getIndex(string);
            if (n2 == -1) {
                throw new IllegalArgumentException(string);
            }
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(n2).getItem("ATTRIBUTE_DECLARED"));
        }

        public boolean isDeclared(String string, String string2) {
            int n2 = this.getIndex(string, string2);
            if (n2 == -1) {
                throw new IllegalArgumentException(string2);
            }
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(n2).getItem("ATTRIBUTE_DECLARED"));
        }

        public boolean isSpecified(int n2) {
            if (n2 < 0 || n2 >= this.fAttributes.getLength()) {
                throw new ArrayIndexOutOfBoundsException(n2);
            }
            return this.fAttributes.isSpecified(n2);
        }

        public boolean isSpecified(String string) {
            int n2 = this.getIndex(string);
            if (n2 == -1) {
                throw new IllegalArgumentException(string);
            }
            return this.fAttributes.isSpecified(n2);
        }

        public boolean isSpecified(String string, String string2) {
            int n2 = this.getIndex(string, string2);
            if (n2 == -1) {
                throw new IllegalArgumentException(string2);
            }
            return this.fAttributes.isSpecified(n2);
        }
    }

    protected static final class LocatorProxy
    implements Locator2 {
        protected XMLLocator fLocator;

        public LocatorProxy(XMLLocator xMLLocator) {
            this.fLocator = xMLLocator;
        }

        public String getPublicId() {
            return this.fLocator.getPublicId();
        }

        public String getSystemId() {
            return this.fLocator.getExpandedSystemId();
        }

        public int getLineNumber() {
            return this.fLocator.getLineNumber();
        }

        public int getColumnNumber() {
            return this.fLocator.getColumnNumber();
        }

        public String getXMLVersion() {
            return this.fLocator.getXMLVersion();
        }

        public String getEncoding() {
            return this.fLocator.getEncoding();
        }
    }

}

