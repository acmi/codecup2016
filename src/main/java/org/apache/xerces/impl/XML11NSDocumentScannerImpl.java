/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.impl.ExternalSubsetResolver;
import org.apache.xerces.impl.XML11DocumentScannerImpl;
import org.apache.xerces.impl.XMLDocumentFragmentScannerImpl;
import org.apache.xerces.impl.XMLDocumentScannerImpl;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.XMLDTDValidatorFilter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XML11NSDocumentScannerImpl
extends XML11DocumentScannerImpl {
    protected boolean fBindNamespaces;
    protected boolean fPerformValidation;
    private XMLDTDValidatorFilter fDTDValidator;
    private boolean fSawSpace;

    public void setDTDValidator(XMLDTDValidatorFilter xMLDTDValidatorFilter) {
        this.fDTDValidator = xMLDTDValidatorFilter;
    }

    protected boolean scanStartElement() throws IOException, XNIException {
        int n2;
        this.fEntityScanner.scanQName(this.fElementQName);
        String string = this.fElementQName.rawname;
        if (this.fBindNamespaces) {
            this.fNamespaceContext.pushContext();
            if (this.fScannerState == 6 && this.fPerformValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[]{string}, 1);
                if (this.fDoctypeName == null || !this.fDoctypeName.equals(string)) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[]{this.fDoctypeName, string}, 1);
                }
            }
        }
        this.fCurrentElement = this.fElementStack.pushElement(this.fElementQName);
        boolean bl = false;
        this.fAttributes.removeAllAttributes();
        do {
            boolean bl2 = this.fEntityScanner.skipSpaces();
            n2 = this.fEntityScanner.peekChar();
            if (n2 == 62) {
                this.fEntityScanner.scanChar();
                break;
            }
            if (n2 == 47) {
                this.fEntityScanner.scanChar();
                if (!this.fEntityScanner.skipChar(62)) {
                    this.reportFatalError("ElementUnterminated", new Object[]{string});
                }
                bl = true;
                break;
            }
            if (!(this.isValidNameStartChar(n2) && bl2 || this.isValidNameStartHighSurrogate(n2) && bl2)) {
                this.reportFatalError("ElementUnterminated", new Object[]{string});
            }
            this.scanAttribute(this.fAttributes);
        } while (true);
        if (this.fBindNamespaces) {
            Object object;
            if (this.fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{this.fElementQName.rawname}, 2);
            }
            String string2 = this.fElementQName.prefix != null ? this.fElementQName.prefix : XMLSymbols.EMPTY_STRING;
            this.fCurrentElement.uri = this.fElementQName.uri = this.fNamespaceContext.getURI(string2);
            if (this.fElementQName.prefix == null && this.fElementQName.uri != null) {
                this.fElementQName.prefix = XMLSymbols.EMPTY_STRING;
                this.fCurrentElement.prefix = XMLSymbols.EMPTY_STRING;
            }
            if (this.fElementQName.prefix != null && this.fElementQName.uri == null) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{this.fElementQName.prefix, this.fElementQName.rawname}, 2);
            }
            n2 = this.fAttributes.getLength();
            int n3 = 0;
            while (n3 < n2) {
                this.fAttributes.getName(n3, this.fAttributeQName);
                object = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
                String string3 = this.fNamespaceContext.getURI((String)object);
                if ((this.fAttributeQName.uri == null || this.fAttributeQName.uri != string3) && object != XMLSymbols.EMPTY_STRING) {
                    this.fAttributeQName.uri = string3;
                    if (string3 == null) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{this.fElementQName.rawname, this.fAttributeQName.rawname, object}, 2);
                    }
                    this.fAttributes.setURI(n3, string3);
                }
                ++n3;
            }
            if (n2 > 1 && (object = this.fAttributes.checkDuplicatesNS()) != null) {
                if (object.uri != null) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{this.fElementQName.rawname, object.localpart, object.uri}, 2);
                } else {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[]{this.fElementQName.rawname, object.rawname}, 2);
                }
            }
        }
        if (this.fDocumentHandler != null) {
            if (bl) {
                --this.fMarkupDepth;
                if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
                    this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
                }
                this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
                if (this.fBindNamespaces) {
                    this.fNamespaceContext.popContext();
                }
                this.fElementStack.popElement(this.fElementQName);
            } else {
                this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
            }
        }
        return bl;
    }

    protected void scanStartElementName() throws IOException, XNIException {
        this.fEntityScanner.scanQName(this.fElementQName);
        this.fSawSpace = this.fEntityScanner.skipSpaces();
    }

    protected boolean scanStartElementAfterName() throws IOException, XNIException {
        String string = this.fElementQName.rawname;
        if (this.fBindNamespaces) {
            this.fNamespaceContext.pushContext();
            if (this.fScannerState == 6 && this.fPerformValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[]{string}, 1);
                if (this.fDoctypeName == null || !this.fDoctypeName.equals(string)) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[]{this.fDoctypeName, string}, 1);
                }
            }
        }
        this.fCurrentElement = this.fElementStack.pushElement(this.fElementQName);
        boolean bl = false;
        this.fAttributes.removeAllAttributes();
        do {
            int n2;
            if ((n2 = this.fEntityScanner.peekChar()) == 62) {
                this.fEntityScanner.scanChar();
                break;
            }
            if (n2 == 47) {
                this.fEntityScanner.scanChar();
                if (!this.fEntityScanner.skipChar(62)) {
                    this.reportFatalError("ElementUnterminated", new Object[]{string});
                }
                bl = true;
                break;
            }
            if (!(this.isValidNameStartChar(n2) && this.fSawSpace || this.isValidNameStartHighSurrogate(n2) && this.fSawSpace)) {
                this.reportFatalError("ElementUnterminated", new Object[]{string});
            }
            this.scanAttribute(this.fAttributes);
            this.fSawSpace = this.fEntityScanner.skipSpaces();
        } while (true);
        if (this.fBindNamespaces) {
            Object object;
            if (this.fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{this.fElementQName.rawname}, 2);
            }
            String string2 = this.fElementQName.prefix != null ? this.fElementQName.prefix : XMLSymbols.EMPTY_STRING;
            this.fCurrentElement.uri = this.fElementQName.uri = this.fNamespaceContext.getURI(string2);
            if (this.fElementQName.prefix == null && this.fElementQName.uri != null) {
                this.fElementQName.prefix = XMLSymbols.EMPTY_STRING;
                this.fCurrentElement.prefix = XMLSymbols.EMPTY_STRING;
            }
            if (this.fElementQName.prefix != null && this.fElementQName.uri == null) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{this.fElementQName.prefix, this.fElementQName.rawname}, 2);
            }
            int n3 = this.fAttributes.getLength();
            int n4 = 0;
            while (n4 < n3) {
                this.fAttributes.getName(n4, this.fAttributeQName);
                object = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
                String string3 = this.fNamespaceContext.getURI((String)object);
                if ((this.fAttributeQName.uri == null || this.fAttributeQName.uri != string3) && object != XMLSymbols.EMPTY_STRING) {
                    this.fAttributeQName.uri = string3;
                    if (string3 == null) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{this.fElementQName.rawname, this.fAttributeQName.rawname, object}, 2);
                    }
                    this.fAttributes.setURI(n4, string3);
                }
                ++n4;
            }
            if (n3 > 1 && (object = this.fAttributes.checkDuplicatesNS()) != null) {
                if (object.uri != null) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{this.fElementQName.rawname, object.localpart, object.uri}, 2);
                } else {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[]{this.fElementQName.rawname, object.rawname}, 2);
                }
            }
        }
        if (this.fDocumentHandler != null) {
            if (bl) {
                --this.fMarkupDepth;
                if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
                    this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
                }
                this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
                if (this.fBindNamespaces) {
                    this.fNamespaceContext.popContext();
                }
                this.fElementStack.popElement(this.fElementQName);
            } else {
                this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
            }
        }
        return bl;
    }

    protected void scanAttribute(XMLAttributesImpl xMLAttributesImpl) throws IOException, XNIException {
        int n2;
        int n3;
        this.fEntityScanner.scanQName(this.fAttributeQName);
        this.fEntityScanner.skipSpaces();
        if (!this.fEntityScanner.skipChar(61)) {
            this.reportFatalError("EqRequiredInAttribute", new Object[]{this.fCurrentElement.rawname, this.fAttributeQName.rawname});
        }
        this.fEntityScanner.skipSpaces();
        if (this.fBindNamespaces) {
            n2 = xMLAttributesImpl.getLength();
            xMLAttributesImpl.addAttributeNS(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
        } else {
            n3 = xMLAttributesImpl.getLength();
            n2 = xMLAttributesImpl.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
            if (n3 == xMLAttributesImpl.getLength()) {
                this.reportFatalError("AttributeNotUnique", new Object[]{this.fCurrentElement.rawname, this.fAttributeQName.rawname});
            }
        }
        n3 = (int)this.scanAttributeValue(this.fTempString, this.fTempString2, this.fAttributeQName.rawname, this.fIsEntityDeclaredVC, this.fCurrentElement.rawname) ? 1 : 0;
        String string = this.fTempString.toString();
        xMLAttributesImpl.setValue(n2, string);
        if (n3 == 0) {
            xMLAttributesImpl.setNonNormalizedValue(n2, this.fTempString2.toString());
        }
        xMLAttributesImpl.setSpecified(n2, true);
        if (this.fBindNamespaces) {
            String string2;
            String string3 = this.fAttributeQName.localpart;
            String string4 = string2 = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            if (string2 == XMLSymbols.PREFIX_XMLNS || string2 == XMLSymbols.EMPTY_STRING && string3 == XMLSymbols.PREFIX_XMLNS) {
                String string5 = this.fSymbolTable.addSymbol(string);
                if (string2 == XMLSymbols.PREFIX_XMLNS && string3 == XMLSymbols.PREFIX_XMLNS) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{this.fAttributeQName}, 2);
                }
                if (string5 == NamespaceContext.XMLNS_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{this.fAttributeQName}, 2);
                }
                if (string3 == XMLSymbols.PREFIX_XML) {
                    if (string5 != NamespaceContext.XML_URI) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{this.fAttributeQName}, 2);
                    }
                } else if (string5 == NamespaceContext.XML_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{this.fAttributeQName}, 2);
                }
                string2 = string3 != XMLSymbols.PREFIX_XMLNS ? string3 : XMLSymbols.EMPTY_STRING;
                this.fNamespaceContext.declarePrefix(string2, string5.length() != 0 ? string5 : null);
                xMLAttributesImpl.setURI(n2, this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS));
            } else if (this.fAttributeQName.prefix != null) {
                xMLAttributesImpl.setURI(n2, this.fNamespaceContext.getURI(this.fAttributeQName.prefix));
            }
        }
    }

    protected int scanEndElement() throws IOException, XNIException {
        this.fElementStack.popElement(this.fElementQName);
        if (!this.fEntityScanner.skipString(this.fElementQName.rawname)) {
            this.reportFatalError("ETagRequired", new Object[]{this.fElementQName.rawname});
        }
        this.fEntityScanner.skipSpaces();
        if (!this.fEntityScanner.skipChar(62)) {
            this.reportFatalError("ETagUnterminated", new Object[]{this.fElementQName.rawname});
        }
        --this.fMarkupDepth;
        --this.fMarkupDepth;
        if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
            this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endElement(this.fElementQName, null);
            if (this.fBindNamespaces) {
                this.fNamespaceContext.popContext();
            }
        }
        return this.fMarkupDepth;
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        super.reset(xMLComponentManager);
        this.fPerformValidation = false;
        this.fBindNamespaces = false;
    }

    protected XMLDocumentFragmentScannerImpl.Dispatcher createContentDispatcher() {
        return new NS11ContentDispatcher(this);
    }

    static XMLDTDValidatorFilter access$000(XML11NSDocumentScannerImpl xML11NSDocumentScannerImpl) {
        return xML11NSDocumentScannerImpl.fDTDValidator;
    }

    protected final class NS11ContentDispatcher
    extends XMLDocumentScannerImpl.ContentDispatcher {
        private final XML11NSDocumentScannerImpl this$0;

        protected NS11ContentDispatcher(XML11NSDocumentScannerImpl xML11NSDocumentScannerImpl) {
            super(xML11NSDocumentScannerImpl);
            this.this$0 = xML11NSDocumentScannerImpl;
        }

        protected boolean scanRootElementHook() throws IOException, XNIException {
            if (this.this$0.fExternalSubsetResolver != null && !this.this$0.fSeenDoctypeDecl && !this.this$0.fDisallowDoctype && (this.this$0.fValidation || this.this$0.fLoadExternalDTD)) {
                this.this$0.scanStartElementName();
                this.resolveExternalSubsetAndRead();
                this.reconfigurePipeline();
                if (this.this$0.scanStartElementAfterName()) {
                    this.this$0.setScannerState(12);
                    this.this$0.setDispatcher(this.this$0.fTrailingMiscDispatcher);
                    return true;
                }
            } else {
                this.reconfigurePipeline();
                if (this.this$0.scanStartElement()) {
                    this.this$0.setScannerState(12);
                    this.this$0.setDispatcher(this.this$0.fTrailingMiscDispatcher);
                    return true;
                }
            }
            return false;
        }

        private void reconfigurePipeline() {
            if (XML11NSDocumentScannerImpl.access$000(this.this$0) == null) {
                this.this$0.fBindNamespaces = true;
            } else if (!XML11NSDocumentScannerImpl.access$000(this.this$0).hasGrammar()) {
                this.this$0.fBindNamespaces = true;
                this.this$0.fPerformValidation = XML11NSDocumentScannerImpl.access$000(this.this$0).validate();
                XMLDocumentSource xMLDocumentSource = XML11NSDocumentScannerImpl.access$000(this.this$0).getDocumentSource();
                XMLDocumentHandler xMLDocumentHandler = XML11NSDocumentScannerImpl.access$000(this.this$0).getDocumentHandler();
                xMLDocumentSource.setDocumentHandler(xMLDocumentHandler);
                if (xMLDocumentHandler != null) {
                    xMLDocumentHandler.setDocumentSource(xMLDocumentSource);
                }
                XML11NSDocumentScannerImpl.access$000(this.this$0).setDocumentSource(null);
                XML11NSDocumentScannerImpl.access$000(this.this$0).setDocumentHandler(null);
            }
        }
    }

}

