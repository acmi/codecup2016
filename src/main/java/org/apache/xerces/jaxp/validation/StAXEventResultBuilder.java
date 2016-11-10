/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
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
import javax.xml.transform.stax.StAXResult;
import org.apache.xerces.jaxp.validation.StAXDocumentHandler;
import org.apache.xerces.jaxp.validation.StAXValidatorHelper;
import org.apache.xerces.util.JAXPNamespaceContextWrapper;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

final class StAXEventResultBuilder
implements StAXDocumentHandler {
    private XMLEventWriter fEventWriter;
    private final XMLEventFactory fEventFactory;
    private final StAXValidatorHelper fStAXValidatorHelper;
    private final JAXPNamespaceContextWrapper fNamespaceContext;
    private boolean fIgnoreChars;
    private boolean fInCDATA;
    private final QName fAttrName = new QName();
    private static final Iterator EMPTY_COLLECTION_ITERATOR = new Iterator(){

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    };

    public StAXEventResultBuilder(StAXValidatorHelper stAXValidatorHelper, JAXPNamespaceContextWrapper jAXPNamespaceContextWrapper) {
        this.fStAXValidatorHelper = stAXValidatorHelper;
        this.fNamespaceContext = jAXPNamespaceContextWrapper;
        this.fEventFactory = XMLEventFactory.newInstance();
    }

    public void setStAXResult(StAXResult stAXResult) {
        this.fIgnoreChars = false;
        this.fInCDATA = false;
        this.fEventWriter = stAXResult != null ? stAXResult.getXMLEventWriter() : null;
    }

    public void startDocument(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        String string = xMLStreamReader.getVersion();
        String string2 = xMLStreamReader.getCharacterEncodingScheme();
        boolean bl = xMLStreamReader.standaloneSet();
        this.fEventWriter.add(this.fEventFactory.createStartDocument(string2 != null ? string2 : "UTF-8", string != null ? string : "1.0", bl));
    }

    public void endDocument(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        this.fEventWriter.add(this.fEventFactory.createEndDocument());
        this.fEventWriter.flush();
    }

    public void comment(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        this.fEventWriter.add(this.fEventFactory.createComment(xMLStreamReader.getText()));
    }

    public void processingInstruction(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        String string;
        this.fEventWriter.add(this.fEventFactory.createProcessingInstruction(xMLStreamReader.getPITarget(), (string = xMLStreamReader.getPIData()) != null ? string : ""));
    }

    public void entityReference(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        String string = xMLStreamReader.getLocalName();
        this.fEventWriter.add(this.fEventFactory.createEntityReference(string, this.fStAXValidatorHelper.getEntityDeclaration(string)));
    }

    public void startDocument(StartDocument startDocument) throws XMLStreamException {
        this.fEventWriter.add(startDocument);
    }

    public void endDocument(EndDocument endDocument) throws XMLStreamException {
        this.fEventWriter.add(endDocument);
        this.fEventWriter.flush();
    }

    public void doctypeDecl(DTD dTD) throws XMLStreamException {
        this.fEventWriter.add(dTD);
    }

    public void characters(Characters characters) throws XMLStreamException {
        this.fEventWriter.add(characters);
    }

    public void cdata(Characters characters) throws XMLStreamException {
        this.fEventWriter.add(characters);
    }

    public void comment(Comment comment) throws XMLStreamException {
        this.fEventWriter.add(comment);
    }

    public void processingInstruction(ProcessingInstruction processingInstruction) throws XMLStreamException {
        this.fEventWriter.add(processingInstruction);
    }

    public void entityReference(EntityReference entityReference) throws XMLStreamException {
        this.fEventWriter.add(entityReference);
    }

    public void setIgnoringCharacters(boolean bl) {
        this.fIgnoreChars = bl;
    }

    public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
    }

    public void xmlDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
    }

    public void doctypeDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        try {
            XMLEvent xMLEvent;
            int n2 = xMLAttributes.getLength();
            if (n2 == 0 && (xMLEvent = this.fStAXValidatorHelper.getCurrentEvent()) != null) {
                this.fEventWriter.add(xMLEvent);
                return;
            }
            this.fEventWriter.add(this.fEventFactory.createStartElement(qName.prefix, qName.uri != null ? qName.uri : "", qName.localpart, this.getAttributeIterator(xMLAttributes, n2), this.getNamespaceIterator(), this.fNamespaceContext.getNamespaceContext()));
        }
        catch (XMLStreamException xMLStreamException) {
            throw new XNIException(xMLStreamException);
        }
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        this.startElement(qName, xMLAttributes, augmentations);
        this.endElement(qName, augmentations);
    }

    public void startGeneralEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
    }

    public void textDecl(String string, String string2, Augmentations augmentations) throws XNIException {
    }

    public void endGeneralEntity(String string, Augmentations augmentations) throws XNIException {
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (!this.fIgnoreChars) {
            try {
                if (!this.fInCDATA) {
                    this.fEventWriter.add(this.fEventFactory.createCharacters(xMLString.toString()));
                } else {
                    this.fEventWriter.add(this.fEventFactory.createCData(xMLString.toString()));
                }
            }
            catch (XMLStreamException xMLStreamException) {
                throw new XNIException(xMLStreamException);
            }
        }
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        this.characters(xMLString, augmentations);
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        try {
            XMLEvent xMLEvent = this.fStAXValidatorHelper.getCurrentEvent();
            if (xMLEvent != null) {
                this.fEventWriter.add(xMLEvent);
            } else {
                this.fEventWriter.add(this.fEventFactory.createEndElement(qName.prefix, qName.uri, qName.localpart, this.getNamespaceIterator()));
            }
        }
        catch (XMLStreamException xMLStreamException) {
            throw new XNIException(xMLStreamException);
        }
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATA = true;
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATA = false;
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
    }

    public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
    }

    public XMLDocumentSource getDocumentSource() {
        return null;
    }

    private Iterator getAttributeIterator(XMLAttributes xMLAttributes, int n2) {
        return n2 > 0 ? new AttributeIterator(this, xMLAttributes, n2) : EMPTY_COLLECTION_ITERATOR;
    }

    private Iterator getNamespaceIterator() {
        int n2 = this.fNamespaceContext.getDeclaredPrefixCount();
        return n2 > 0 ? new NamespaceIterator(this, n2) : EMPTY_COLLECTION_ITERATOR;
    }

    static QName access$000(StAXEventResultBuilder stAXEventResultBuilder) {
        return stAXEventResultBuilder.fAttrName;
    }

    static XMLEventFactory access$100(StAXEventResultBuilder stAXEventResultBuilder) {
        return stAXEventResultBuilder.fEventFactory;
    }

    static JAXPNamespaceContextWrapper access$200(StAXEventResultBuilder stAXEventResultBuilder) {
        return stAXEventResultBuilder.fNamespaceContext;
    }

    final class NamespaceIterator
    implements Iterator {
        javax.xml.namespace.NamespaceContext fNC;
        int fIndex;
        int fEnd;
        private final StAXEventResultBuilder this$0;

        NamespaceIterator(StAXEventResultBuilder stAXEventResultBuilder, int n2) {
            this.this$0 = stAXEventResultBuilder;
            this.fNC = StAXEventResultBuilder.access$200(stAXEventResultBuilder).getNamespaceContext();
            this.fIndex = 0;
            this.fEnd = n2;
        }

        public boolean hasNext() {
            if (this.fIndex < this.fEnd) {
                return true;
            }
            this.fNC = null;
            return false;
        }

        public Object next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            String string = StAXEventResultBuilder.access$200(this.this$0).getDeclaredPrefixAt(this.fIndex++);
            String string2 = this.fNC.getNamespaceURI(string);
            if (string.length() == 0) {
                return StAXEventResultBuilder.access$100(this.this$0).createNamespace(string2 != null ? string2 : "");
            }
            return StAXEventResultBuilder.access$100(this.this$0).createNamespace(string, string2 != null ? string2 : "");
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    final class AttributeIterator
    implements Iterator {
        XMLAttributes fAttributes;
        int fIndex;
        int fEnd;
        private final StAXEventResultBuilder this$0;

        AttributeIterator(StAXEventResultBuilder stAXEventResultBuilder, XMLAttributes xMLAttributes, int n2) {
            this.this$0 = stAXEventResultBuilder;
            this.fAttributes = xMLAttributes;
            this.fIndex = 0;
            this.fEnd = n2;
        }

        public boolean hasNext() {
            if (this.fIndex < this.fEnd) {
                return true;
            }
            this.fAttributes = null;
            return false;
        }

        public Object next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.fAttributes.getName(this.fIndex, StAXEventResultBuilder.access$000(this.this$0));
            return StAXEventResultBuilder.access$100(this.this$0).createAttribute(StAXEventResultBuilder.access$000((StAXEventResultBuilder)this.this$0).prefix, StAXEventResultBuilder.access$000((StAXEventResultBuilder)this.this$0).uri != null ? StAXEventResultBuilder.access$000((StAXEventResultBuilder)this.this$0).uri : "", StAXEventResultBuilder.access$000((StAXEventResultBuilder)this.this$0).localpart, this.fAttributes.getValue(this.fIndex++));
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}

