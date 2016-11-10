/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDSource;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class DefaultXMLDocumentHandler
implements XMLDTDContentModelHandler,
XMLDTDHandler,
XMLDocumentHandler {
    private XMLDocumentSource fDocumentSource;
    private XMLDTDSource fDTDSource;
    private XMLDTDContentModelSource fCMSource;

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

    public void startPrefixMapping(String string, String string2, Augmentations augmentations) throws XNIException {
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
    }

    public void startGeneralEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
    }

    public void textDecl(String string, String string2, Augmentations augmentations) throws XNIException {
    }

    public void endGeneralEntity(String string, Augmentations augmentations) throws XNIException {
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
    }

    public void endPrefixMapping(String string, Augmentations augmentations) throws XNIException {
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
    }

    public void startDTD(XMLLocator xMLLocator, Augmentations augmentations) throws XNIException {
    }

    public void startParameterEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
    }

    public void endParameterEntity(String string, Augmentations augmentations) throws XNIException {
    }

    public void startExternalSubset(XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
    }

    public void endExternalSubset(Augmentations augmentations) throws XNIException {
    }

    public void elementDecl(String string, String string2, Augmentations augmentations) throws XNIException {
    }

    public void startAttlist(String string, Augmentations augmentations) throws XNIException {
    }

    public void attributeDecl(String string, String string2, String string3, String[] arrstring, String string4, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
    }

    public void endAttlist(Augmentations augmentations) throws XNIException {
    }

    public void internalEntityDecl(String string, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
    }

    public void externalEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
    }

    public void unparsedEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
    }

    public void notationDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
    }

    public void startConditional(short s2, Augmentations augmentations) throws XNIException {
    }

    public void ignoredCharacters(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void endConditional(Augmentations augmentations) throws XNIException {
    }

    public void endDTD(Augmentations augmentations) throws XNIException {
    }

    public void startContentModel(String string, Augmentations augmentations) throws XNIException {
    }

    public void any(Augmentations augmentations) throws XNIException {
    }

    public void empty(Augmentations augmentations) throws XNIException {
    }

    public void startGroup(Augmentations augmentations) throws XNIException {
    }

    public void pcdata(Augmentations augmentations) throws XNIException {
    }

    public void element(String string, Augmentations augmentations) throws XNIException {
    }

    public void separator(short s2, Augmentations augmentations) throws XNIException {
    }

    public void occurrence(short s2, Augmentations augmentations) throws XNIException {
    }

    public void endGroup(Augmentations augmentations) throws XNIException {
    }

    public void endContentModel(Augmentations augmentations) throws XNIException {
    }

    public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
        this.fDocumentSource = xMLDocumentSource;
    }

    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    public void setDTDSource(XMLDTDSource xMLDTDSource) {
        this.fDTDSource = xMLDTDSource;
    }

    public XMLDTDSource getDTDSource() {
        return this.fDTDSource;
    }

    public void setDTDContentModelSource(XMLDTDContentModelSource xMLDTDContentModelSource) {
        this.fCMSource = xMLDTDContentModelSource;
    }

    public XMLDTDContentModelSource getDTDContentModelSource() {
        return this.fCMSource;
    }
}

