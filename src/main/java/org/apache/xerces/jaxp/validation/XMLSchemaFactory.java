/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.jaxp.validation.AbstractXMLSchema;
import org.apache.xerces.jaxp.validation.DraconianErrorHandler;
import org.apache.xerces.jaxp.validation.EmptyXMLSchema;
import org.apache.xerces.jaxp.validation.JAXPValidationMessageFormatter;
import org.apache.xerces.jaxp.validation.ReadOnlyGrammarPool;
import org.apache.xerces.jaxp.validation.SimpleXMLSchema;
import org.apache.xerces.jaxp.validation.Util;
import org.apache.xerces.jaxp.validation.WeakReferenceXMLSchema;
import org.apache.xerces.jaxp.validation.XMLSchema;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.DOMInputSource;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXInputSource;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.StAXInputSource;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public final class XMLSchemaFactory
extends SchemaFactory {
    private static final String JAXP_SOURCE_FEATURE_PREFIX = "http://javax.xml.transform";
    private static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
    private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private final XMLSchemaLoader fXMLSchemaLoader = new XMLSchemaLoader();
    private ErrorHandler fErrorHandler;
    private LSResourceResolver fLSResourceResolver;
    private final DOMEntityResolverWrapper fDOMEntityResolverWrapper = new DOMEntityResolverWrapper();
    private final ErrorHandlerWrapper fErrorHandlerWrapper = new ErrorHandlerWrapper(DraconianErrorHandler.getInstance());
    private SecurityManager fSecurityManager;
    private final XMLGrammarPoolWrapper fXMLGrammarPoolWrapper = new XMLGrammarPoolWrapper();
    private boolean fUseGrammarPoolOnly;

    public XMLSchemaFactory() {
        this.fXMLSchemaLoader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
        this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fXMLGrammarPoolWrapper);
        this.fXMLSchemaLoader.setEntityResolver(this.fDOMEntityResolverWrapper);
        this.fXMLSchemaLoader.setErrorHandler(this.fErrorHandlerWrapper);
        this.fUseGrammarPoolOnly = true;
    }

    public boolean isSchemaLanguageSupported(String string) {
        if (string == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaLanguageNull", null));
        }
        if (string.length() == 0) {
            throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaLanguageLengthZero", null));
        }
        return string.equals("http://www.w3.org/2001/XMLSchema");
    }

    public LSResourceResolver getResourceResolver() {
        return this.fLSResourceResolver;
    }

    public void setResourceResolver(LSResourceResolver lSResourceResolver) {
        this.fLSResourceResolver = lSResourceResolver;
        this.fDOMEntityResolverWrapper.setEntityResolver(lSResourceResolver);
        this.fXMLSchemaLoader.setEntityResolver(this.fDOMEntityResolverWrapper);
    }

    public ErrorHandler getErrorHandler() {
        return this.fErrorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.fErrorHandler = errorHandler;
        this.fErrorHandlerWrapper.setErrorHandler(errorHandler != null ? errorHandler : DraconianErrorHandler.getInstance());
        this.fXMLSchemaLoader.setErrorHandler(this.fErrorHandlerWrapper);
    }

    public Schema newSchema(Source[] arrsource) throws SAXException {
        Object object;
        Object object2;
        XMLGrammarPoolImplExtension xMLGrammarPoolImplExtension = new XMLGrammarPoolImplExtension();
        this.fXMLGrammarPoolWrapper.setGrammarPool(xMLGrammarPoolImplExtension);
        XMLInputSource[] arrxMLInputSource = new XMLInputSource[arrsource.length];
        int n2 = 0;
        while (n2 < arrsource.length) {
            String string;
            Source source = arrsource[n2];
            if (source instanceof StreamSource) {
                object2 = (StreamSource)source;
                object = object2.getPublicId();
                string = object2.getSystemId();
                InputStream inputStream = object2.getInputStream();
                Reader reader = object2.getReader();
                arrxMLInputSource[n2] = new XMLInputSource((String)object, string, null);
                arrxMLInputSource[n2].setByteStream(inputStream);
                arrxMLInputSource[n2].setCharacterStream(reader);
            } else if (source instanceof SAXSource) {
                object2 = (SAXSource)source;
                object = object2.getInputSource();
                if (object == null) {
                    throw new SAXException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SAXSourceNullInputSource", null));
                }
                arrxMLInputSource[n2] = new SAXInputSource(object2.getXMLReader(), (InputSource)object);
            } else if (source instanceof DOMSource) {
                object2 = (DOMSource)source;
                object = object2.getNode();
                string = object2.getSystemId();
                arrxMLInputSource[n2] = new DOMInputSource((Node)object, string);
            } else if (source instanceof StAXSource) {
                object2 = (StAXSource)source;
                object = object2.getXMLEventReader();
                arrxMLInputSource[n2] = object != null ? new StAXInputSource((XMLEventReader)object) : new StAXInputSource(object2.getXMLStreamReader());
            } else {
                if (source == null) {
                    throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaSourceArrayMemberNull", null));
                }
                throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaFactorySourceUnrecognized", new Object[]{source.getClass().getName()}));
            }
            ++n2;
        }
        try {
            this.fXMLSchemaLoader.loadGrammar(arrxMLInputSource);
        }
        catch (XNIException xNIException) {
            throw Util.toSAXException(xNIException);
        }
        catch (IOException iOException) {
            object = new SAXParseException(iOException.getMessage(), null, iOException);
            if (this.fErrorHandler != null) {
                this.fErrorHandler.error((SAXParseException)object);
            }
            throw object;
        }
        this.fXMLGrammarPoolWrapper.setGrammarPool(null);
        int n3 = xMLGrammarPoolImplExtension.getGrammarCount();
        object2 = null;
        if (this.fUseGrammarPoolOnly) {
            if (n3 > 1) {
                object2 = new XMLSchema(new ReadOnlyGrammarPool(xMLGrammarPoolImplExtension));
            } else if (n3 == 1) {
                object = xMLGrammarPoolImplExtension.retrieveInitialGrammarSet("http://www.w3.org/2001/XMLSchema");
                object2 = new SimpleXMLSchema(object[0]);
            } else {
                object2 = new EmptyXMLSchema();
            }
        } else {
            object2 = new XMLSchema(new ReadOnlyGrammarPool(xMLGrammarPoolImplExtension), false);
        }
        this.propagateFeatures((AbstractXMLSchema)object2);
        return object2;
    }

    public Schema newSchema() throws SAXException {
        WeakReferenceXMLSchema weakReferenceXMLSchema = new WeakReferenceXMLSchema();
        this.propagateFeatures(weakReferenceXMLSchema);
        return weakReferenceXMLSchema;
    }

    public Schema newSchema(XMLGrammarPool xMLGrammarPool) throws SAXException {
        XMLSchema xMLSchema = this.fUseGrammarPoolOnly ? new XMLSchema(new ReadOnlyGrammarPool(xMLGrammarPool)) : new XMLSchema(xMLGrammarPool, false);
        this.propagateFeatures(xMLSchema);
        return xMLSchema;
    }

    public boolean getFeature(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (string == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "FeatureNameNull", null));
        }
        if (string.startsWith("http://javax.xml.transform") && (string.equals("http://javax.xml.transform.stream.StreamSource/feature") || string.equals("http://javax.xml.transform.sax.SAXSource/feature") || string.equals("http://javax.xml.transform.dom.DOMSource/feature") || string.equals("http://javax.xml.transform.stax.StAXSource/feature"))) {
            return true;
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this.fSecurityManager != null;
        }
        if (string.equals("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only")) {
            return this.fUseGrammarPoolOnly;
        }
        try {
            return this.fXMLSchemaLoader.getFeature(string);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            String string2 = xMLConfigurationException.getIdentifier();
            if (xMLConfigurationException.getType() == 0) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-recognized", new Object[]{string2}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-supported", new Object[]{string2}));
        }
    }

    public Object getProperty(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (string == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "ProperyNameNull", null));
        }
        if (string.equals("http://apache.org/xml/properties/security-manager")) {
            return this.fSecurityManager;
        }
        if (string.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[]{string}));
        }
        try {
            return this.fXMLSchemaLoader.getProperty(string);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            String string2 = xMLConfigurationException.getIdentifier();
            if (xMLConfigurationException.getType() == 0) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-recognized", new Object[]{string2}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[]{string2}));
        }
    }

    public void setFeature(String string, boolean bl) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (string == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "FeatureNameNull", null));
        }
        if (string.startsWith("http://javax.xml.transform") && (string.equals("http://javax.xml.transform.stream.StreamSource/feature") || string.equals("http://javax.xml.transform.sax.SAXSource/feature") || string.equals("http://javax.xml.transform.dom.DOMSource/feature") || string.equals("http://javax.xml.transform.stax.StAXSource/feature"))) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-read-only", new Object[]{string}));
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            this.fSecurityManager = bl ? new SecurityManager() : null;
            this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
            return;
        }
        if (string.equals("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only")) {
            this.fUseGrammarPoolOnly = bl;
            return;
        }
        try {
            this.fXMLSchemaLoader.setFeature(string, bl);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            String string2 = xMLConfigurationException.getIdentifier();
            if (xMLConfigurationException.getType() == 0) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-recognized", new Object[]{string2}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-supported", new Object[]{string2}));
        }
    }

    public void setProperty(String string, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (string == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "ProperyNameNull", null));
        }
        if (string.equals("http://apache.org/xml/properties/security-manager")) {
            this.fSecurityManager = (SecurityManager)object;
            this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
            return;
        }
        if (string.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[]{string}));
        }
        try {
            this.fXMLSchemaLoader.setProperty(string, object);
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            String string2 = xMLConfigurationException.getIdentifier();
            if (xMLConfigurationException.getType() == 0) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-recognized", new Object[]{string2}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[]{string2}));
        }
    }

    private void propagateFeatures(AbstractXMLSchema abstractXMLSchema) {
        abstractXMLSchema.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", this.fSecurityManager != null);
        String[] arrstring = this.fXMLSchemaLoader.getRecognizedFeatures();
        int n2 = 0;
        while (n2 < arrstring.length) {
            boolean bl = this.fXMLSchemaLoader.getFeature(arrstring[n2]);
            abstractXMLSchema.setFeature(arrstring[n2], bl);
            ++n2;
        }
    }

    static class XMLGrammarPoolWrapper
    implements XMLGrammarPool {
        private XMLGrammarPool fGrammarPool;

        XMLGrammarPoolWrapper() {
        }

        public Grammar[] retrieveInitialGrammarSet(String string) {
            return this.fGrammarPool.retrieveInitialGrammarSet(string);
        }

        public void cacheGrammars(String string, Grammar[] arrgrammar) {
            this.fGrammarPool.cacheGrammars(string, arrgrammar);
        }

        public Grammar retrieveGrammar(XMLGrammarDescription xMLGrammarDescription) {
            return this.fGrammarPool.retrieveGrammar(xMLGrammarDescription);
        }

        public void lockPool() {
            this.fGrammarPool.lockPool();
        }

        public void unlockPool() {
            this.fGrammarPool.unlockPool();
        }

        public void clear() {
            this.fGrammarPool.clear();
        }

        void setGrammarPool(XMLGrammarPool xMLGrammarPool) {
            this.fGrammarPool = xMLGrammarPool;
        }

        XMLGrammarPool getGrammarPool() {
            return this.fGrammarPool;
        }
    }

    static class XMLGrammarPoolImplExtension
    extends XMLGrammarPoolImpl {
        public XMLGrammarPoolImplExtension() {
        }

        public XMLGrammarPoolImplExtension(int n2) {
            super(n2);
        }

        int getGrammarCount() {
            return this.fGrammarCount;
        }
    }

}

