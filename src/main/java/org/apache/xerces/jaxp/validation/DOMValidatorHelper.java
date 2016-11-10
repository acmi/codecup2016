/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.jaxp.validation.DOMDocumentHandler;
import org.apache.xerces.jaxp.validation.DOMResultAugmentor;
import org.apache.xerces.jaxp.validation.DOMResultBuilder;
import org.apache.xerces.jaxp.validation.JAXPValidationMessageFormatter;
import org.apache.xerces.jaxp.validation.Util;
import org.apache.xerces.jaxp.validation.ValidatorHelper;
import org.apache.xerces.jaxp.validation.XMLSchemaValidatorComponentManager;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

final class DOMValidatorHelper
implements EntityState,
ValidatorHelper {
    private static final int CHUNK_SIZE = 1024;
    private static final int CHUNK_MASK = 1023;
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private final XMLErrorReporter fErrorReporter;
    private final NamespaceSupport fNamespaceContext;
    private final DOMNamespaceContext fDOMNamespaceContext;
    private final XMLSchemaValidator fSchemaValidator;
    private final SymbolTable fSymbolTable;
    private final ValidationManager fValidationManager;
    private final XMLSchemaValidatorComponentManager fComponentManager;
    private final SimpleLocator fXMLLocator;
    private DOMDocumentHandler fDOMValidatorHandler;
    private final DOMResultAugmentor fDOMResultAugmentor;
    private final DOMResultBuilder fDOMResultBuilder;
    private NamedNodeMap fEntities;
    private final char[] fCharBuffer;
    private Node fRoot;
    private Node fCurrentElement;
    final QName fElementQName;
    final QName fAttributeQName;
    final XMLAttributesImpl fAttributes;
    final XMLString fTempString;

    public DOMValidatorHelper(XMLSchemaValidatorComponentManager xMLSchemaValidatorComponentManager) {
        this.fDOMNamespaceContext = new DOMNamespaceContext(this);
        this.fXMLLocator = new SimpleLocator(null, null, -1, -1, -1);
        this.fDOMResultAugmentor = new DOMResultAugmentor(this);
        this.fDOMResultBuilder = new DOMResultBuilder();
        this.fEntities = null;
        this.fCharBuffer = new char[1024];
        this.fElementQName = new QName();
        this.fAttributeQName = new QName();
        this.fAttributes = new XMLAttributesImpl();
        this.fTempString = new XMLString();
        this.fComponentManager = xMLSchemaValidatorComponentManager;
        this.fErrorReporter = (XMLErrorReporter)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fNamespaceContext = (NamespaceSupport)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context");
        this.fSchemaValidator = (XMLSchemaValidator)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema");
        this.fSymbolTable = (SymbolTable)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fValidationManager = (ValidationManager)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager");
    }

    public void validate(Source source, Result result) throws SAXException, IOException {
        if (result instanceof DOMResult || result == null) {
            Node node;
            DOMSource dOMSource = (DOMSource)source;
            DOMResult dOMResult = (DOMResult)result;
            this.fRoot = node = dOMSource.getNode();
            if (node != null) {
                this.fComponentManager.reset();
                this.fValidationManager.setEntityState(this);
                this.fDOMNamespaceContext.reset();
                String string = dOMSource.getSystemId();
                this.fXMLLocator.setLiteralSystemId(string);
                this.fXMLLocator.setExpandedSystemId(string);
                this.fErrorReporter.setDocumentLocator(this.fXMLLocator);
                try {
                    try {
                        this.setupEntityMap(node.getNodeType() == 9 ? (Document)node : node.getOwnerDocument());
                        this.setupDOMResultHandler(dOMSource, dOMResult);
                        this.fSchemaValidator.startDocument(this.fXMLLocator, null, this.fDOMNamespaceContext, null);
                        this.validate(node);
                        this.fSchemaValidator.endDocument(null);
                    }
                    catch (XMLParseException xMLParseException) {
                        throw Util.toSAXParseException(xMLParseException);
                    }
                    catch (XNIException xNIException) {
                        throw Util.toSAXException(xNIException);
                    }
                    Object var10_7 = null;
                    this.fRoot = null;
                    this.fCurrentElement = null;
                    this.fEntities = null;
                    if (this.fDOMValidatorHandler != null) {
                        this.fDOMValidatorHandler.setDOMResult(null);
                    }
                }
                catch (Throwable throwable) {
                    Object var10_8 = null;
                    this.fRoot = null;
                    this.fCurrentElement = null;
                    this.fEntities = null;
                    if (this.fDOMValidatorHandler != null) {
                        this.fDOMValidatorHandler.setDOMResult(null);
                    }
                    throw throwable;
                }
            }
            return;
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[]{source.getClass().getName(), result.getClass().getName()}));
    }

    public boolean isEntityDeclared(String string) {
        return false;
    }

    public boolean isEntityUnparsed(String string) {
        Entity entity;
        if (this.fEntities != null && (entity = (Entity)this.fEntities.getNamedItem(string)) != null) {
            return entity.getNotationName() != null;
        }
        return false;
    }

    private void validate(Node node) {
        Node node2 = node;
        boolean bl = this.useIsSameNode(node2);
        while (node != null) {
            this.beginNode(node);
            Node node3 = node.getFirstChild();
            while (node3 == null) {
                this.finishNode(node);
                if (node2 == node) break;
                node3 = node.getNextSibling();
                if (node3 != null || (node = node.getParentNode()) != null && !(bl ? node2.isSameNode(node) : node2 == node)) continue;
                if (node != null) {
                    this.finishNode(node);
                }
                node3 = null;
                break;
            }
            node = node3;
        }
    }

    private void beginNode(Node node) {
        switch (node.getNodeType()) {
            case 1: {
                this.fCurrentElement = node;
                this.fNamespaceContext.pushContext();
                this.fillQName(this.fElementQName, node);
                this.processAttributes(node.getAttributes());
                this.fSchemaValidator.startElement(this.fElementQName, this.fAttributes, null);
                break;
            }
            case 3: {
                if (this.fDOMValidatorHandler != null) {
                    this.fDOMValidatorHandler.setIgnoringCharacters(true);
                    this.sendCharactersToValidator(node.getNodeValue());
                    this.fDOMValidatorHandler.setIgnoringCharacters(false);
                    this.fDOMValidatorHandler.characters((Text)node);
                    break;
                }
                this.sendCharactersToValidator(node.getNodeValue());
                break;
            }
            case 4: {
                if (this.fDOMValidatorHandler != null) {
                    this.fDOMValidatorHandler.setIgnoringCharacters(true);
                    this.fSchemaValidator.startCDATA(null);
                    this.sendCharactersToValidator(node.getNodeValue());
                    this.fSchemaValidator.endCDATA(null);
                    this.fDOMValidatorHandler.setIgnoringCharacters(false);
                    this.fDOMValidatorHandler.cdata((CDATASection)node);
                    break;
                }
                this.fSchemaValidator.startCDATA(null);
                this.sendCharactersToValidator(node.getNodeValue());
                this.fSchemaValidator.endCDATA(null);
                break;
            }
            case 7: {
                if (this.fDOMValidatorHandler == null) break;
                this.fDOMValidatorHandler.processingInstruction((ProcessingInstruction)node);
                break;
            }
            case 8: {
                if (this.fDOMValidatorHandler == null) break;
                this.fDOMValidatorHandler.comment((Comment)node);
                break;
            }
            case 10: {
                if (this.fDOMValidatorHandler == null) break;
                this.fDOMValidatorHandler.doctypeDecl((DocumentType)node);
                break;
            }
        }
    }

    private void finishNode(Node node) {
        if (node.getNodeType() == 1) {
            this.fCurrentElement = node;
            this.fillQName(this.fElementQName, node);
            this.fSchemaValidator.endElement(this.fElementQName, null);
            this.fNamespaceContext.popContext();
        }
    }

    private void setupEntityMap(Document document) {
        DocumentType documentType;
        if (document != null && (documentType = document.getDoctype()) != null) {
            this.fEntities = documentType.getEntities();
            return;
        }
        this.fEntities = null;
    }

    private void setupDOMResultHandler(DOMSource dOMSource, DOMResult dOMResult) throws SAXException {
        if (dOMResult == null) {
            this.fDOMValidatorHandler = null;
            this.fSchemaValidator.setDocumentHandler(null);
            return;
        }
        Node node = dOMResult.getNode();
        if (dOMSource.getNode() == node) {
            this.fDOMValidatorHandler = this.fDOMResultAugmentor;
            this.fDOMResultAugmentor.setDOMResult(dOMResult);
            this.fSchemaValidator.setDocumentHandler(this.fDOMResultAugmentor);
            return;
        }
        if (dOMResult.getNode() == null) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setNamespaceAware(true);
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                dOMResult.setNode(documentBuilder.newDocument());
            }
            catch (ParserConfigurationException parserConfigurationException) {
                throw new SAXException(parserConfigurationException);
            }
        }
        this.fDOMValidatorHandler = this.fDOMResultBuilder;
        this.fDOMResultBuilder.setDOMResult(dOMResult);
        this.fSchemaValidator.setDocumentHandler(this.fDOMResultBuilder);
    }

    private void fillQName(QName qName, Node node) {
        String string = node.getPrefix();
        String string2 = node.getLocalName();
        String string3 = node.getNodeName();
        String string4 = node.getNamespaceURI();
        qName.prefix = string != null ? this.fSymbolTable.addSymbol(string) : XMLSymbols.EMPTY_STRING;
        qName.localpart = string2 != null ? this.fSymbolTable.addSymbol(string2) : XMLSymbols.EMPTY_STRING;
        qName.rawname = string3 != null ? this.fSymbolTable.addSymbol(string3) : XMLSymbols.EMPTY_STRING;
        qName.uri = string4 != null && string4.length() > 0 ? this.fSymbolTable.addSymbol(string4) : null;
    }

    private void processAttributes(NamedNodeMap namedNodeMap) {
        int n2 = namedNodeMap.getLength();
        this.fAttributes.removeAllAttributes();
        int n3 = 0;
        while (n3 < n2) {
            Attr attr = (Attr)namedNodeMap.item(n3);
            String string = attr.getValue();
            if (string == null) {
                string = XMLSymbols.EMPTY_STRING;
            }
            this.fillQName(this.fAttributeQName, attr);
            this.fAttributes.addAttributeNS(this.fAttributeQName, XMLSymbols.fCDATASymbol, string);
            this.fAttributes.setSpecified(n3, attr.getSpecified());
            if (this.fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
                if (this.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                    this.fNamespaceContext.declarePrefix(this.fAttributeQName.localpart, string.length() != 0 ? this.fSymbolTable.addSymbol(string) : null);
                } else {
                    this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, string.length() != 0 ? this.fSymbolTable.addSymbol(string) : null);
                }
            }
            ++n3;
        }
    }

    private void sendCharactersToValidator(String string) {
        if (string != null) {
            int n2 = string.length();
            int n3 = n2 & 1023;
            if (n3 > 0) {
                string.getChars(0, n3, this.fCharBuffer, 0);
                this.fTempString.setValues(this.fCharBuffer, 0, n3);
                this.fSchemaValidator.characters(this.fTempString, null);
            }
            int n4 = n3;
            while (n4 < n2) {
                string.getChars(n4, n4 += 1024, this.fCharBuffer, 0);
                this.fTempString.setValues(this.fCharBuffer, 0, 1024);
                this.fSchemaValidator.characters(this.fTempString, null);
            }
        }
    }

    private boolean useIsSameNode(Node node) {
        if (node instanceof NodeImpl) {
            return false;
        }
        Document document = node.getNodeType() == 9 ? (Document)node : node.getOwnerDocument();
        return document != null && document.getImplementation().hasFeature("Core", "3.0");
    }

    Node getCurrentElement() {
        return this.fCurrentElement;
    }

    static NamespaceSupport access$000(DOMValidatorHelper dOMValidatorHelper) {
        return dOMValidatorHelper.fNamespaceContext;
    }

    static Node access$100(DOMValidatorHelper dOMValidatorHelper) {
        return dOMValidatorHelper.fRoot;
    }

    static void access$200(DOMValidatorHelper dOMValidatorHelper, QName qName, Node node) {
        dOMValidatorHelper.fillQName(qName, node);
    }

    static SymbolTable access$300(DOMValidatorHelper dOMValidatorHelper) {
        return dOMValidatorHelper.fSymbolTable;
    }

    final class DOMNamespaceContext
    implements NamespaceContext {
        protected String[] fNamespace;
        protected int fNamespaceSize;
        protected boolean fDOMContextBuilt;
        private final DOMValidatorHelper this$0;

        DOMNamespaceContext(DOMValidatorHelper dOMValidatorHelper) {
            this.this$0 = dOMValidatorHelper;
            this.fNamespace = new String[32];
            this.fNamespaceSize = 0;
            this.fDOMContextBuilt = false;
        }

        public void pushContext() {
            DOMValidatorHelper.access$000(this.this$0).pushContext();
        }

        public void popContext() {
            DOMValidatorHelper.access$000(this.this$0).popContext();
        }

        public boolean declarePrefix(String string, String string2) {
            return DOMValidatorHelper.access$000(this.this$0).declarePrefix(string, string2);
        }

        public String getURI(String string) {
            String string2 = DOMValidatorHelper.access$000(this.this$0).getURI(string);
            if (string2 == null) {
                if (!this.fDOMContextBuilt) {
                    this.fillNamespaceContext();
                    this.fDOMContextBuilt = true;
                }
                if (this.fNamespaceSize > 0 && !DOMValidatorHelper.access$000(this.this$0).containsPrefix(string)) {
                    string2 = this.getURI0(string);
                }
            }
            return string2;
        }

        public String getPrefix(String string) {
            return DOMValidatorHelper.access$000(this.this$0).getPrefix(string);
        }

        public int getDeclaredPrefixCount() {
            return DOMValidatorHelper.access$000(this.this$0).getDeclaredPrefixCount();
        }

        public String getDeclaredPrefixAt(int n2) {
            return DOMValidatorHelper.access$000(this.this$0).getDeclaredPrefixAt(n2);
        }

        public Enumeration getAllPrefixes() {
            return DOMValidatorHelper.access$000(this.this$0).getAllPrefixes();
        }

        public void reset() {
            this.fDOMContextBuilt = false;
            this.fNamespaceSize = 0;
        }

        private void fillNamespaceContext() {
            if (DOMValidatorHelper.access$100(this.this$0) != null) {
                Node node = DOMValidatorHelper.access$100(this.this$0).getParentNode();
                while (node != null) {
                    if (1 == node.getNodeType()) {
                        NamedNodeMap namedNodeMap = node.getAttributes();
                        int n2 = namedNodeMap.getLength();
                        int n3 = 0;
                        while (n3 < n2) {
                            Attr attr = (Attr)namedNodeMap.item(n3);
                            String string = attr.getValue();
                            if (string == null) {
                                string = XMLSymbols.EMPTY_STRING;
                            }
                            DOMValidatorHelper.access$200(this.this$0, this.this$0.fAttributeQName, attr);
                            if (this.this$0.fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
                                if (this.this$0.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                                    this.declarePrefix0(this.this$0.fAttributeQName.localpart, string.length() != 0 ? DOMValidatorHelper.access$300(this.this$0).addSymbol(string) : null);
                                } else {
                                    this.declarePrefix0(XMLSymbols.EMPTY_STRING, string.length() != 0 ? DOMValidatorHelper.access$300(this.this$0).addSymbol(string) : null);
                                }
                            }
                            ++n3;
                        }
                    }
                    node = node.getParentNode();
                }
            }
        }

        private void declarePrefix0(String string, String string2) {
            if (this.fNamespaceSize == this.fNamespace.length) {
                String[] arrstring = new String[this.fNamespaceSize * 2];
                System.arraycopy(this.fNamespace, 0, arrstring, 0, this.fNamespaceSize);
                this.fNamespace = arrstring;
            }
            this.fNamespace[this.fNamespaceSize++] = string;
            this.fNamespace[this.fNamespaceSize++] = string2;
        }

        private String getURI0(String string) {
            int n2 = 0;
            while (n2 < this.fNamespaceSize) {
                if (this.fNamespace[n2] == string) {
                    return this.fNamespace[n2 + 1];
                }
                n2 += 2;
            }
            return null;
        }
    }

}

