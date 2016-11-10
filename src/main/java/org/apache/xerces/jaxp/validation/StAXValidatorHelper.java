/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.jaxp.validation.JAXPValidationMessageFormatter;
import org.apache.xerces.jaxp.validation.StAXDocumentHandler;
import org.apache.xerces.jaxp.validation.StAXEventResultBuilder;
import org.apache.xerces.jaxp.validation.StAXStreamResultBuilder;
import org.apache.xerces.jaxp.validation.Util;
import org.apache.xerces.jaxp.validation.ValidatorHelper;
import org.apache.xerces.jaxp.validation.XMLSchemaValidatorComponentManager;
import org.apache.xerces.util.JAXPNamespaceContextWrapper;
import org.apache.xerces.util.StAXLocationWrapper;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.xml.sax.SAXException;

final class StAXValidatorHelper
implements EntityState,
ValidatorHelper {
    private static final String STRING_INTERNING = "javax.xml.stream.isInterning";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private final XMLErrorReporter fErrorReporter;
    private final XMLSchemaValidator fSchemaValidator;
    private final SymbolTable fSymbolTable;
    private final ValidationManager fValidationManager;
    private final XMLSchemaValidatorComponentManager fComponentManager;
    private final JAXPNamespaceContextWrapper fNamespaceContext;
    private final StAXLocationWrapper fStAXLocationWrapper = new StAXLocationWrapper();
    private final XMLStreamReaderLocation fXMLStreamReaderLocation = new XMLStreamReaderLocation();
    private HashMap fEntities = null;
    private boolean fStringsInternalized = false;
    private StreamHelper fStreamHelper;
    private EventHelper fEventHelper;
    private StAXDocumentHandler fStAXValidatorHandler;
    private StAXStreamResultBuilder fStAXStreamResultBuilder;
    private StAXEventResultBuilder fStAXEventResultBuilder;
    private int fDepth = 0;
    private XMLEvent fCurrentEvent = null;
    final org.apache.xerces.xni.QName fElementQName = new org.apache.xerces.xni.QName();
    final org.apache.xerces.xni.QName fAttributeQName = new org.apache.xerces.xni.QName();
    final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
    final ArrayList fDeclaredPrefixes = new ArrayList();
    final XMLString fTempString = new XMLString();
    final XMLStringBuffer fStringBuffer = new XMLStringBuffer();

    public StAXValidatorHelper(XMLSchemaValidatorComponentManager xMLSchemaValidatorComponentManager) {
        this.fComponentManager = xMLSchemaValidatorComponentManager;
        this.fErrorReporter = (XMLErrorReporter)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fSchemaValidator = (XMLSchemaValidator)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema");
        this.fSymbolTable = (SymbolTable)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fValidationManager = (ValidationManager)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager");
        this.fNamespaceContext = new JAXPNamespaceContextWrapper(this.fSymbolTable);
        this.fNamespaceContext.setDeclaredPrefixes(this.fDeclaredPrefixes);
    }

    public void validate(Source source, Result result) throws SAXException, IOException {
        if (result instanceof StAXResult || result == null) {
            StAXSource stAXSource = (StAXSource)source;
            StAXResult stAXResult = (StAXResult)result;
            try {
                block12 : {
                    try {
                        XMLStreamReader xMLStreamReader = stAXSource.getXMLStreamReader();
                        if (xMLStreamReader != null) {
                            if (this.fStreamHelper == null) {
                                this.fStreamHelper = new StreamHelper(this);
                            }
                            this.fStreamHelper.validate(xMLStreamReader, stAXResult);
                            break block12;
                        }
                        if (this.fEventHelper == null) {
                            this.fEventHelper = new EventHelper(this);
                        }
                        this.fEventHelper.validate(stAXSource.getXMLEventReader(), stAXResult);
                    }
                    catch (XMLStreamException xMLStreamException) {
                        throw new SAXException(xMLStreamException);
                    }
                    catch (XMLParseException xMLParseException) {
                        throw Util.toSAXParseException(xMLParseException);
                    }
                    catch (XNIException xNIException) {
                        throw Util.toSAXException(xNIException);
                    }
                }
                Object var9_7 = null;
                this.fCurrentEvent = null;
                this.fStAXLocationWrapper.setLocation(null);
                this.fXMLStreamReaderLocation.setXMLStreamReader(null);
                if (this.fStAXValidatorHandler != null) {
                    this.fStAXValidatorHandler.setStAXResult(null);
                }
            }
            catch (Throwable throwable) {
                Object var9_8 = null;
                this.fCurrentEvent = null;
                this.fStAXLocationWrapper.setLocation(null);
                this.fXMLStreamReaderLocation.setXMLStreamReader(null);
                if (this.fStAXValidatorHandler != null) {
                    this.fStAXValidatorHandler.setStAXResult(null);
                }
                throw throwable;
            }
            return;
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[]{source.getClass().getName(), result.getClass().getName()}));
    }

    public boolean isEntityDeclared(String string) {
        if (this.fEntities != null) {
            return this.fEntities.containsKey(string);
        }
        return false;
    }

    public boolean isEntityUnparsed(String string) {
        EntityDeclaration entityDeclaration;
        if (this.fEntities != null && (entityDeclaration = (EntityDeclaration)this.fEntities.get(string)) != null) {
            return entityDeclaration.getNotationName() != null;
        }
        return false;
    }

    final EntityDeclaration getEntityDeclaration(String string) {
        return this.fEntities != null ? (EntityDeclaration)this.fEntities.get(string) : null;
    }

    final XMLEvent getCurrentEvent() {
        return this.fCurrentEvent;
    }

    final void fillQName(org.apache.xerces.xni.QName qName, String string, String string2, String string3) {
        if (!this.fStringsInternalized) {
            string = string != null && string.length() > 0 ? this.fSymbolTable.addSymbol(string) : null;
            string2 = string2 != null ? this.fSymbolTable.addSymbol(string2) : XMLSymbols.EMPTY_STRING;
            string3 = string3 != null && string3.length() > 0 ? this.fSymbolTable.addSymbol(string3) : XMLSymbols.EMPTY_STRING;
        } else {
            if (string != null && string.length() == 0) {
                string = null;
            }
            if (string2 == null) {
                string2 = XMLSymbols.EMPTY_STRING;
            }
            if (string3 == null) {
                string3 = XMLSymbols.EMPTY_STRING;
            }
        }
        String string4 = string2;
        if (string3 != XMLSymbols.EMPTY_STRING) {
            this.fStringBuffer.clear();
            this.fStringBuffer.append(string3);
            this.fStringBuffer.append(':');
            this.fStringBuffer.append(string2);
            string4 = this.fSymbolTable.addSymbol(this.fStringBuffer.ch, this.fStringBuffer.offset, this.fStringBuffer.length);
        }
        qName.setValues(string3, string2, string4, string);
    }

    final void setup(Location location, StAXResult stAXResult, boolean bl) {
        this.fDepth = 0;
        this.fComponentManager.reset();
        this.setupStAXResultHandler(stAXResult);
        this.fValidationManager.setEntityState(this);
        if (this.fEntities != null && !this.fEntities.isEmpty()) {
            this.fEntities.clear();
        }
        this.fStAXLocationWrapper.setLocation(location);
        this.fErrorReporter.setDocumentLocator(this.fStAXLocationWrapper);
        this.fStringsInternalized = bl;
    }

    final void processEntityDeclarations(List list) {
        int n2;
        int n3 = n2 = list != null ? list.size() : 0;
        if (n2 > 0) {
            if (this.fEntities == null) {
                this.fEntities = new HashMap();
            }
            int n4 = 0;
            while (n4 < n2) {
                EntityDeclaration entityDeclaration = (EntityDeclaration)list.get(n4);
                this.fEntities.put(entityDeclaration.getName(), entityDeclaration);
                ++n4;
            }
        }
    }

    private void setupStAXResultHandler(StAXResult stAXResult) {
        if (stAXResult == null) {
            this.fStAXValidatorHandler = null;
            this.fSchemaValidator.setDocumentHandler(null);
            return;
        }
        XMLStreamWriter xMLStreamWriter = stAXResult.getXMLStreamWriter();
        if (xMLStreamWriter != null) {
            if (this.fStAXStreamResultBuilder == null) {
                this.fStAXStreamResultBuilder = new StAXStreamResultBuilder(this.fNamespaceContext);
            }
            this.fStAXValidatorHandler = this.fStAXStreamResultBuilder;
            this.fStAXStreamResultBuilder.setStAXResult(stAXResult);
        } else {
            if (this.fStAXEventResultBuilder == null) {
                this.fStAXEventResultBuilder = new StAXEventResultBuilder(this, this.fNamespaceContext);
            }
            this.fStAXValidatorHandler = this.fStAXEventResultBuilder;
            this.fStAXEventResultBuilder.setStAXResult(stAXResult);
        }
        this.fSchemaValidator.setDocumentHandler(this.fStAXValidatorHandler);
    }

    static XMLSchemaValidatorComponentManager access$000(StAXValidatorHelper stAXValidatorHelper) {
        return stAXValidatorHelper.fComponentManager;
    }

    static XMLStreamReaderLocation access$100(StAXValidatorHelper stAXValidatorHelper) {
        return stAXValidatorHelper.fXMLStreamReaderLocation;
    }

    static StAXLocationWrapper access$200(StAXValidatorHelper stAXValidatorHelper) {
        return stAXValidatorHelper.fStAXLocationWrapper;
    }

    static JAXPNamespaceContextWrapper access$300(StAXValidatorHelper stAXValidatorHelper) {
        return stAXValidatorHelper.fNamespaceContext;
    }

    static XMLSchemaValidator access$400(StAXValidatorHelper stAXValidatorHelper) {
        return stAXValidatorHelper.fSchemaValidator;
    }

    static int access$504(StAXValidatorHelper stAXValidatorHelper) {
        return ++stAXValidatorHelper.fDepth;
    }

    static int access$506(StAXValidatorHelper stAXValidatorHelper) {
        return --stAXValidatorHelper.fDepth;
    }

    static StAXDocumentHandler access$600(StAXValidatorHelper stAXValidatorHelper) {
        return stAXValidatorHelper.fStAXValidatorHandler;
    }

    static int access$500(StAXValidatorHelper stAXValidatorHelper) {
        return stAXValidatorHelper.fDepth;
    }

    static XMLEvent access$702(StAXValidatorHelper stAXValidatorHelper, XMLEvent xMLEvent) {
        stAXValidatorHelper.fCurrentEvent = xMLEvent;
        return stAXValidatorHelper.fCurrentEvent;
    }

    static XMLEvent access$700(StAXValidatorHelper stAXValidatorHelper) {
        return stAXValidatorHelper.fCurrentEvent;
    }

    static final class XMLStreamReaderLocation
    implements Location {
        private XMLStreamReader reader;

        public int getCharacterOffset() {
            Location location = this.getLocation();
            if (location != null) {
                return location.getCharacterOffset();
            }
            return -1;
        }

        public int getColumnNumber() {
            Location location = this.getLocation();
            if (location != null) {
                return location.getColumnNumber();
            }
            return -1;
        }

        public int getLineNumber() {
            Location location = this.getLocation();
            if (location != null) {
                return location.getLineNumber();
            }
            return -1;
        }

        public String getPublicId() {
            Location location = this.getLocation();
            if (location != null) {
                return location.getPublicId();
            }
            return null;
        }

        public String getSystemId() {
            Location location = this.getLocation();
            if (location != null) {
                return location.getSystemId();
            }
            return null;
        }

        public void setXMLStreamReader(XMLStreamReader xMLStreamReader) {
            this.reader = xMLStreamReader;
        }

        private Location getLocation() {
            return this.reader != null ? this.reader.getLocation() : null;
        }
    }

    final class EventHelper {
        private static final int CHUNK_SIZE = 1024;
        private static final int CHUNK_MASK = 1023;
        private final char[] fCharBuffer;
        private final StAXValidatorHelper this$0;

        EventHelper(StAXValidatorHelper stAXValidatorHelper) {
            this.this$0 = stAXValidatorHelper;
            this.fCharBuffer = new char[1024];
        }

        /*
         * Enabled aggressive block sorting
         */
        final void validate(XMLEventReader xMLEventReader, StAXResult stAXResult) throws SAXException, XMLStreamException {
            StAXValidatorHelper.access$702(this.this$0, xMLEventReader.peek());
            if (StAXValidatorHelper.access$700(this.this$0) != null) {
                int n2 = StAXValidatorHelper.access$700(this.this$0).getEventType();
                if (n2 != 7 && n2 != 1) {
                    throw new SAXException(JAXPValidationMessageFormatter.formatMessage(StAXValidatorHelper.access$000(this.this$0).getLocale(), "StAXIllegalInitialState", null));
                }
                this.this$0.setup(null, stAXResult, false);
                StAXValidatorHelper.access$400(this.this$0).startDocument(StAXValidatorHelper.access$200(this.this$0), null, StAXValidatorHelper.access$300(this.this$0), null);
                block12 : while (xMLEventReader.hasNext()) {
                    StAXValidatorHelper.access$702(this.this$0, xMLEventReader.nextEvent());
                    n2 = StAXValidatorHelper.access$700(this.this$0).getEventType();
                    switch (n2) {
                        case 1: {
                            StAXValidatorHelper.access$504(this.this$0);
                            StartElement startElement = StAXValidatorHelper.access$700(this.this$0).asStartElement();
                            this.fillQName(this.this$0.fElementQName, startElement.getName());
                            this.fillXMLAttributes(startElement);
                            this.fillDeclaredPrefixes(startElement);
                            StAXValidatorHelper.access$300(this.this$0).setNamespaceContext(startElement.getNamespaceContext());
                            StAXValidatorHelper.access$200(this.this$0).setLocation(startElement.getLocation());
                            StAXValidatorHelper.access$400(this.this$0).startElement(this.this$0.fElementQName, this.this$0.fAttributes, null);
                            break;
                        }
                        case 2: {
                            EndElement endElement = StAXValidatorHelper.access$700(this.this$0).asEndElement();
                            this.fillQName(this.this$0.fElementQName, endElement.getName());
                            this.fillDeclaredPrefixes(endElement);
                            StAXValidatorHelper.access$200(this.this$0).setLocation(endElement.getLocation());
                            StAXValidatorHelper.access$400(this.this$0).endElement(this.this$0.fElementQName, null);
                            if (StAXValidatorHelper.access$506(this.this$0) > 0) break;
                            break block12;
                        }
                        case 4: 
                        case 6: {
                            if (StAXValidatorHelper.access$600(this.this$0) != null) {
                                Characters characters = StAXValidatorHelper.access$700(this.this$0).asCharacters();
                                StAXValidatorHelper.access$600(this.this$0).setIgnoringCharacters(true);
                                this.sendCharactersToValidator(characters.getData());
                                StAXValidatorHelper.access$600(this.this$0).setIgnoringCharacters(false);
                                StAXValidatorHelper.access$600(this.this$0).characters(characters);
                                break;
                            }
                            this.sendCharactersToValidator(StAXValidatorHelper.access$700(this.this$0).asCharacters().getData());
                            break;
                        }
                        case 12: {
                            if (StAXValidatorHelper.access$600(this.this$0) != null) {
                                Characters characters = StAXValidatorHelper.access$700(this.this$0).asCharacters();
                                StAXValidatorHelper.access$600(this.this$0).setIgnoringCharacters(true);
                                StAXValidatorHelper.access$400(this.this$0).startCDATA(null);
                                this.sendCharactersToValidator(StAXValidatorHelper.access$700(this.this$0).asCharacters().getData());
                                StAXValidatorHelper.access$400(this.this$0).endCDATA(null);
                                StAXValidatorHelper.access$600(this.this$0).setIgnoringCharacters(false);
                                StAXValidatorHelper.access$600(this.this$0).cdata(characters);
                                break;
                            }
                            StAXValidatorHelper.access$400(this.this$0).startCDATA(null);
                            this.sendCharactersToValidator(StAXValidatorHelper.access$700(this.this$0).asCharacters().getData());
                            StAXValidatorHelper.access$400(this.this$0).endCDATA(null);
                            break;
                        }
                        case 7: {
                            StAXValidatorHelper.access$504(this.this$0);
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).startDocument((StartDocument)StAXValidatorHelper.access$700(this.this$0));
                            break;
                        }
                        case 8: {
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).endDocument((EndDocument)StAXValidatorHelper.access$700(this.this$0));
                            break;
                        }
                        case 3: {
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).processingInstruction((ProcessingInstruction)StAXValidatorHelper.access$700(this.this$0));
                            break;
                        }
                        case 5: {
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).comment((Comment)StAXValidatorHelper.access$700(this.this$0));
                            break;
                        }
                        case 9: {
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).entityReference((EntityReference)StAXValidatorHelper.access$700(this.this$0));
                            break;
                        }
                        case 11: {
                            DTD dTD = (DTD)StAXValidatorHelper.access$700(this.this$0);
                            this.this$0.processEntityDeclarations(dTD.getEntities());
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).doctypeDecl(dTD);
                        }
                    }
                }
                StAXValidatorHelper.access$400(this.this$0).endDocument(null);
            }
        }

        private void fillQName(org.apache.xerces.xni.QName qName, QName qName2) {
            this.this$0.fillQName(qName, qName2.getNamespaceURI(), qName2.getLocalPart(), qName2.getPrefix());
        }

        private void fillXMLAttributes(StartElement startElement) {
            this.this$0.fAttributes.removeAllAttributes();
            Iterator iterator = startElement.getAttributes();
            while (iterator.hasNext()) {
                Attribute attribute = (Attribute)iterator.next();
                this.fillQName(this.this$0.fAttributeQName, attribute.getName());
                String string = attribute.getDTDType();
                int n2 = this.this$0.fAttributes.getLength();
                this.this$0.fAttributes.addAttributeNS(this.this$0.fAttributeQName, string != null ? string : XMLSymbols.fCDATASymbol, attribute.getValue());
                this.this$0.fAttributes.setSpecified(n2, attribute.isSpecified());
            }
        }

        private void fillDeclaredPrefixes(StartElement startElement) {
            this.fillDeclaredPrefixes(startElement.getNamespaces());
        }

        private void fillDeclaredPrefixes(EndElement endElement) {
            this.fillDeclaredPrefixes(endElement.getNamespaces());
        }

        private void fillDeclaredPrefixes(Iterator iterator) {
            this.this$0.fDeclaredPrefixes.clear();
            while (iterator.hasNext()) {
                Namespace namespace = (Namespace)iterator.next();
                String string = namespace.getPrefix();
                this.this$0.fDeclaredPrefixes.add(string != null ? string : "");
            }
        }

        private void sendCharactersToValidator(String string) {
            if (string != null) {
                int n2 = string.length();
                int n3 = n2 & 1023;
                if (n3 > 0) {
                    string.getChars(0, n3, this.fCharBuffer, 0);
                    this.this$0.fTempString.setValues(this.fCharBuffer, 0, n3);
                    StAXValidatorHelper.access$400(this.this$0).characters(this.this$0.fTempString, null);
                }
                int n4 = n3;
                while (n4 < n2) {
                    string.getChars(n4, n4 += 1024, this.fCharBuffer, 0);
                    this.this$0.fTempString.setValues(this.fCharBuffer, 0, 1024);
                    StAXValidatorHelper.access$400(this.this$0).characters(this.this$0.fTempString, null);
                }
            }
        }
    }

    final class StreamHelper {
        private final StAXValidatorHelper this$0;

        StreamHelper(StAXValidatorHelper stAXValidatorHelper) {
            this.this$0 = stAXValidatorHelper;
        }

        final void validate(XMLStreamReader xMLStreamReader, StAXResult stAXResult) throws SAXException, XMLStreamException {
            if (xMLStreamReader.hasNext()) {
                int n2 = xMLStreamReader.getEventType();
                if (n2 != 7 && n2 != 1) {
                    throw new SAXException(JAXPValidationMessageFormatter.formatMessage(StAXValidatorHelper.access$000(this.this$0).getLocale(), "StAXIllegalInitialState", null));
                }
                StAXValidatorHelper.access$100(this.this$0).setXMLStreamReader(xMLStreamReader);
                this.this$0.setup(StAXValidatorHelper.access$100(this.this$0), stAXResult, Boolean.TRUE.equals(xMLStreamReader.getProperty("javax.xml.stream.isInterning")));
                StAXValidatorHelper.access$400(this.this$0).startDocument(StAXValidatorHelper.access$200(this.this$0), null, StAXValidatorHelper.access$300(this.this$0), null);
                do {
                    switch (n2) {
                        case 1: {
                            StAXValidatorHelper.access$504(this.this$0);
                            this.this$0.fillQName(this.this$0.fElementQName, xMLStreamReader.getNamespaceURI(), xMLStreamReader.getLocalName(), xMLStreamReader.getPrefix());
                            this.fillXMLAttributes(xMLStreamReader);
                            this.fillDeclaredPrefixes(xMLStreamReader);
                            StAXValidatorHelper.access$300(this.this$0).setNamespaceContext(xMLStreamReader.getNamespaceContext());
                            StAXValidatorHelper.access$400(this.this$0).startElement(this.this$0.fElementQName, this.this$0.fAttributes, null);
                            break;
                        }
                        case 2: {
                            this.this$0.fillQName(this.this$0.fElementQName, xMLStreamReader.getNamespaceURI(), xMLStreamReader.getLocalName(), xMLStreamReader.getPrefix());
                            this.fillDeclaredPrefixes(xMLStreamReader);
                            StAXValidatorHelper.access$300(this.this$0).setNamespaceContext(xMLStreamReader.getNamespaceContext());
                            StAXValidatorHelper.access$400(this.this$0).endElement(this.this$0.fElementQName, null);
                            StAXValidatorHelper.access$506(this.this$0);
                            break;
                        }
                        case 4: 
                        case 6: {
                            this.this$0.fTempString.setValues(xMLStreamReader.getTextCharacters(), xMLStreamReader.getTextStart(), xMLStreamReader.getTextLength());
                            StAXValidatorHelper.access$400(this.this$0).characters(this.this$0.fTempString, null);
                            break;
                        }
                        case 12: {
                            StAXValidatorHelper.access$400(this.this$0).startCDATA(null);
                            this.this$0.fTempString.setValues(xMLStreamReader.getTextCharacters(), xMLStreamReader.getTextStart(), xMLStreamReader.getTextLength());
                            StAXValidatorHelper.access$400(this.this$0).characters(this.this$0.fTempString, null);
                            StAXValidatorHelper.access$400(this.this$0).endCDATA(null);
                            break;
                        }
                        case 7: {
                            StAXValidatorHelper.access$504(this.this$0);
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).startDocument(xMLStreamReader);
                            break;
                        }
                        case 3: {
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).processingInstruction(xMLStreamReader);
                            break;
                        }
                        case 5: {
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).comment(xMLStreamReader);
                            break;
                        }
                        case 9: {
                            if (StAXValidatorHelper.access$600(this.this$0) == null) break;
                            StAXValidatorHelper.access$600(this.this$0).entityReference(xMLStreamReader);
                            break;
                        }
                        case 11: {
                            this.this$0.processEntityDeclarations((List)xMLStreamReader.getProperty("javax.xml.stream.entities"));
                        }
                    }
                    n2 = xMLStreamReader.next();
                } while (xMLStreamReader.hasNext() && StAXValidatorHelper.access$500(this.this$0) > 0);
                StAXValidatorHelper.access$400(this.this$0).endDocument(null);
                if (n2 == 8 && StAXValidatorHelper.access$600(this.this$0) != null) {
                    StAXValidatorHelper.access$600(this.this$0).endDocument(xMLStreamReader);
                }
            }
        }

        private void fillXMLAttributes(XMLStreamReader xMLStreamReader) {
            this.this$0.fAttributes.removeAllAttributes();
            int n2 = xMLStreamReader.getAttributeCount();
            int n3 = 0;
            while (n3 < n2) {
                this.this$0.fillQName(this.this$0.fAttributeQName, xMLStreamReader.getAttributeNamespace(n3), xMLStreamReader.getAttributeLocalName(n3), xMLStreamReader.getAttributePrefix(n3));
                String string = xMLStreamReader.getAttributeType(n3);
                this.this$0.fAttributes.addAttributeNS(this.this$0.fAttributeQName, string != null ? string : XMLSymbols.fCDATASymbol, xMLStreamReader.getAttributeValue(n3));
                this.this$0.fAttributes.setSpecified(n3, xMLStreamReader.isAttributeSpecified(n3));
                ++n3;
            }
        }

        private void fillDeclaredPrefixes(XMLStreamReader xMLStreamReader) {
            this.this$0.fDeclaredPrefixes.clear();
            int n2 = xMLStreamReader.getNamespaceCount();
            int n3 = 0;
            while (n3 < n2) {
                String string = xMLStreamReader.getNamespacePrefix(n3);
                this.this$0.fDeclaredPrefixes.add(string != null ? string : "");
                ++n3;
            }
        }
    }

}

