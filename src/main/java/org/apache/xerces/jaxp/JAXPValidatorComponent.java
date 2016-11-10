/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.opti.DefaultXMLDocumentHandler;
import org.apache.xerces.jaxp.TeeXMLDocumentFilterImpl;
import org.apache.xerces.util.AttributesProxy;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.ErrorHandlerProxy;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.LocatorProxy;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

final class JAXPValidatorComponent
extends TeeXMLDocumentFilterImpl
implements XMLComponent {
    private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private final ValidatorHandler validator;
    private final XNI2SAX xni2sax = new XNI2SAX(null);
    private final SAX2XNI sax2xni;
    private final TypeInfoProvider typeInfoProvider;
    private Augmentations fCurrentAug;
    private XMLAttributes fCurrentAttributes;
    private SymbolTable fSymbolTable;
    private XMLErrorReporter fErrorReporter;
    private XMLEntityResolver fEntityResolver;
    private static final TypeInfoProvider noInfoProvider = new TypeInfoProvider(){

        public TypeInfo getElementTypeInfo() {
            return null;
        }

        public TypeInfo getAttributeTypeInfo(int n2) {
            return null;
        }

        public TypeInfo getAttributeTypeInfo(String string) {
            return null;
        }

        public TypeInfo getAttributeTypeInfo(String string, String string2) {
            return null;
        }

        public boolean isIdAttribute(int n2) {
            return false;
        }

        public boolean isSpecified(int n2) {
            return false;
        }
    };

    public JAXPValidatorComponent(ValidatorHandler validatorHandler) {
        this.sax2xni = new SAX2XNI(this, null);
        this.validator = validatorHandler;
        TypeInfoProvider typeInfoProvider = validatorHandler.getTypeInfoProvider();
        if (typeInfoProvider == null) {
            typeInfoProvider = noInfoProvider;
        }
        this.typeInfoProvider = typeInfoProvider;
        this.xni2sax.setContentHandler(this.validator);
        this.validator.setContentHandler(this.sax2xni);
        this.setSide(this.xni2sax);
        this.validator.setErrorHandler(new ErrorHandlerProxy(this){
            private final JAXPValidatorComponent this$0;

            protected XMLErrorHandler getErrorHandler() {
                XMLErrorHandler xMLErrorHandler = JAXPValidatorComponent.access$200(this.this$0).getErrorHandler();
                if (xMLErrorHandler != null) {
                    return xMLErrorHandler;
                }
                return new ErrorHandlerWrapper(DraconianErrorHandler.getInstance());
            }
        });
        this.validator.setResourceResolver(new LSResourceResolver(this){
            private final JAXPValidatorComponent this$0;

            public LSInput resolveResource(String string, String string2, String string3, String string4, String string5) {
                if (JAXPValidatorComponent.access$300(this.this$0) == null) {
                    return null;
                }
                try {
                    XMLInputSource xMLInputSource = JAXPValidatorComponent.access$300(this.this$0).resolveEntity(new XMLResourceIdentifierImpl(string3, string4, string5, null));
                    if (xMLInputSource == null) {
                        return null;
                    }
                    DOMInputImpl dOMInputImpl = new DOMInputImpl();
                    dOMInputImpl.setBaseURI(xMLInputSource.getBaseSystemId());
                    dOMInputImpl.setByteStream(xMLInputSource.getByteStream());
                    dOMInputImpl.setCharacterStream(xMLInputSource.getCharacterStream());
                    dOMInputImpl.setEncoding(xMLInputSource.getEncoding());
                    dOMInputImpl.setPublicId(xMLInputSource.getPublicId());
                    dOMInputImpl.setSystemId(xMLInputSource.getSystemId());
                    return dOMInputImpl;
                }
                catch (IOException iOException) {
                    throw new XNIException(iOException);
                }
            }
        });
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        this.fCurrentAttributes = xMLAttributes;
        this.fCurrentAug = augmentations;
        this.xni2sax.startElement(qName, xMLAttributes, null);
        this.fCurrentAttributes = null;
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        this.fCurrentAug = augmentations;
        this.xni2sax.endElement(qName, null);
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        this.startElement(qName, xMLAttributes, augmentations);
        this.endElement(qName, augmentations);
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        this.fCurrentAug = augmentations;
        this.xni2sax.characters(xMLString, null);
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        this.fCurrentAug = augmentations;
        this.xni2sax.ignorableWhitespace(xMLString, null);
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        this.fSymbolTable = (SymbolTable)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        try {
            this.fEntityResolver = (XMLEntityResolver)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager");
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fEntityResolver = null;
        }
    }

    private void updateAttributes(Attributes attributes) {
        int n2 = attributes.getLength();
        int n3 = 0;
        while (n3 < n2) {
            String string = attributes.getQName(n3);
            int n4 = this.fCurrentAttributes.getIndex(string);
            String string2 = attributes.getValue(n3);
            if (n4 == -1) {
                int n5 = string.indexOf(58);
                String string3 = n5 < 0 ? null : this.symbolize(string.substring(0, n5));
                n4 = this.fCurrentAttributes.addAttribute(new QName(string3, this.symbolize(attributes.getLocalName(n3)), this.symbolize(string), this.symbolize(attributes.getURI(n3))), attributes.getType(n3), string2);
            } else if (!string2.equals(this.fCurrentAttributes.getValue(n4))) {
                this.fCurrentAttributes.setValue(n4, string2);
            }
            ++n3;
        }
    }

    private String symbolize(String string) {
        return this.fSymbolTable.addSymbol(string);
    }

    public String[] getRecognizedFeatures() {
        return null;
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
    }

    public String[] getRecognizedProperties() {
        return new String[]{"http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/symbol-table"};
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
    }

    public Boolean getFeatureDefault(String string) {
        return null;
    }

    public Object getPropertyDefault(String string) {
        return null;
    }

    static XMLErrorReporter access$200(JAXPValidatorComponent jAXPValidatorComponent) {
        return jAXPValidatorComponent.fErrorReporter;
    }

    static XMLEntityResolver access$300(JAXPValidatorComponent jAXPValidatorComponent) {
        return jAXPValidatorComponent.fEntityResolver;
    }

    static void access$400(JAXPValidatorComponent jAXPValidatorComponent, Attributes attributes) {
        jAXPValidatorComponent.updateAttributes(attributes);
    }

    static XMLAttributes access$500(JAXPValidatorComponent jAXPValidatorComponent) {
        return jAXPValidatorComponent.fCurrentAttributes;
    }

    static Augmentations access$600(JAXPValidatorComponent jAXPValidatorComponent) {
        return jAXPValidatorComponent.fCurrentAug;
    }

    static Augmentations access$602(JAXPValidatorComponent jAXPValidatorComponent, Augmentations augmentations) {
        jAXPValidatorComponent.fCurrentAug = augmentations;
        return jAXPValidatorComponent.fCurrentAug;
    }

    static String access$700(JAXPValidatorComponent jAXPValidatorComponent, String string) {
        return jAXPValidatorComponent.symbolize(string);
    }

    private static final class DraconianErrorHandler
    implements ErrorHandler {
        private static final DraconianErrorHandler ERROR_HANDLER_INSTANCE = new DraconianErrorHandler();

        private DraconianErrorHandler() {
        }

        public static DraconianErrorHandler getInstance() {
            return ERROR_HANDLER_INSTANCE;
        }

        public void warning(SAXParseException sAXParseException) throws SAXException {
        }

        public void error(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }

        public void fatalError(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }
    }

    private static final class XNI2SAX
    extends DefaultXMLDocumentHandler {
        private ContentHandler fContentHandler;
        private String fVersion;
        protected NamespaceContext fNamespaceContext;
        private final AttributesProxy fAttributesProxy = new AttributesProxy(null);

        private XNI2SAX() {
        }

        public void setContentHandler(ContentHandler contentHandler) {
            this.fContentHandler = contentHandler;
        }

        public ContentHandler getContentHandler() {
            return this.fContentHandler;
        }

        public void xmlDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
            this.fVersion = string;
        }

        public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
            this.fNamespaceContext = namespaceContext;
            this.fContentHandler.setDocumentLocator(new LocatorProxy(xMLLocator));
            try {
                this.fContentHandler.startDocument();
            }
            catch (SAXException sAXException) {
                throw new XNIException(sAXException);
            }
        }

        public void endDocument(Augmentations augmentations) throws XNIException {
            try {
                this.fContentHandler.endDocument();
            }
            catch (SAXException sAXException) {
                throw new XNIException(sAXException);
            }
        }

        public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
            try {
                this.fContentHandler.processingInstruction(string, xMLString.toString());
            }
            catch (SAXException sAXException) {
                throw new XNIException(sAXException);
            }
        }

        public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
            try {
                String string;
                String string2;
                int n2 = this.fNamespaceContext.getDeclaredPrefixCount();
                if (n2 > 0) {
                    string2 = null;
                    string = null;
                    int n3 = 0;
                    while (n3 < n2) {
                        string = this.fNamespaceContext.getURI(string2 = this.fNamespaceContext.getDeclaredPrefixAt(n3));
                        this.fContentHandler.startPrefixMapping(string2, string == null ? "" : string);
                        ++n3;
                    }
                }
                string2 = qName.uri != null ? qName.uri : "";
                string = qName.localpart;
                this.fAttributesProxy.setAttributes(xMLAttributes);
                this.fContentHandler.startElement(string2, string, qName.rawname, this.fAttributesProxy);
            }
            catch (SAXException sAXException) {
                throw new XNIException(sAXException);
            }
        }

        public void endElement(QName qName, Augmentations augmentations) throws XNIException {
            try {
                String string = qName.uri != null ? qName.uri : "";
                String string2 = qName.localpart;
                this.fContentHandler.endElement(string, string2, qName.rawname);
                int n2 = this.fNamespaceContext.getDeclaredPrefixCount();
                if (n2 > 0) {
                    int n3 = 0;
                    while (n3 < n2) {
                        this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(n3));
                        ++n3;
                    }
                }
            }
            catch (SAXException sAXException) {
                throw new XNIException(sAXException);
            }
        }

        public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
            this.startElement(qName, xMLAttributes, augmentations);
            this.endElement(qName, augmentations);
        }

        public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
            try {
                this.fContentHandler.characters(xMLString.ch, xMLString.offset, xMLString.length);
            }
            catch (SAXException sAXException) {
                throw new XNIException(sAXException);
            }
        }

        public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
            try {
                this.fContentHandler.ignorableWhitespace(xMLString.ch, xMLString.offset, xMLString.length);
            }
            catch (SAXException sAXException) {
                throw new XNIException(sAXException);
            }
        }

        XNI2SAX( var1_1) {
            this();
        }
    }

    private final class SAX2XNI
    extends DefaultHandler {
        private final Augmentations fAugmentations;
        private final QName fQName;
        private final JAXPValidatorComponent this$0;

        private SAX2XNI(JAXPValidatorComponent jAXPValidatorComponent) {
            this.this$0 = jAXPValidatorComponent;
            this.fAugmentations = new AugmentationsImpl();
            this.fQName = new QName();
        }

        public void characters(char[] arrc, int n2, int n3) throws SAXException {
            try {
                this.handler().characters(new XMLString(arrc, n2, n3), this.aug());
            }
            catch (XNIException xNIException) {
                throw this.toSAXException(xNIException);
            }
        }

        public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
            try {
                this.handler().ignorableWhitespace(new XMLString(arrc, n2, n3), this.aug());
            }
            catch (XNIException xNIException) {
                throw this.toSAXException(xNIException);
            }
        }

        public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
            try {
                JAXPValidatorComponent.access$400(this.this$0, attributes);
                this.handler().startElement(this.toQName(string, string2, string3), JAXPValidatorComponent.access$500(this.this$0), this.elementAug());
            }
            catch (XNIException xNIException) {
                throw this.toSAXException(xNIException);
            }
        }

        public void endElement(String string, String string2, String string3) throws SAXException {
            try {
                this.handler().endElement(this.toQName(string, string2, string3), this.aug());
            }
            catch (XNIException xNIException) {
                throw this.toSAXException(xNIException);
            }
        }

        private Augmentations elementAug() {
            Augmentations augmentations = this.aug();
            return augmentations;
        }

        private Augmentations aug() {
            if (JAXPValidatorComponent.access$600(this.this$0) != null) {
                Augmentations augmentations = JAXPValidatorComponent.access$600(this.this$0);
                JAXPValidatorComponent.access$602(this.this$0, null);
                return augmentations;
            }
            this.fAugmentations.removeAllItems();
            return this.fAugmentations;
        }

        private XMLDocumentHandler handler() {
            return this.this$0.getDocumentHandler();
        }

        private SAXException toSAXException(XNIException xNIException) {
            Exception exception = xNIException.getException();
            if (exception == null) {
                exception = xNIException;
            }
            if (exception instanceof SAXException) {
                return (SAXException)exception;
            }
            return new SAXException(exception);
        }

        private QName toQName(String string, String string2, String string3) {
            String string4 = null;
            int n2 = string3.indexOf(58);
            if (n2 > 0) {
                string4 = JAXPValidatorComponent.access$700(this.this$0, string3.substring(0, n2));
            }
            string2 = JAXPValidatorComponent.access$700(this.this$0, string2);
            string3 = JAXPValidatorComponent.access$700(this.this$0, string3);
            string = JAXPValidatorComponent.access$700(this.this$0, string);
            this.fQName.setValues(string4, string2, string3, string);
            return this.fQName;
        }

        SAX2XNI(JAXPValidatorComponent jAXPValidatorComponent,  var2_2) {
            this(jAXPValidatorComponent);
        }
    }

}

