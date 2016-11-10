/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.XML11DTDValidator;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;

public class XML11NSDTDValidator
extends XML11DTDValidator {
    private final QName fAttributeQName = new QName();

    protected final void startNamespaceScope(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        String string;
        String string2;
        this.fNamespaceContext.pushContext();
        if (qName.prefix == XMLSymbols.PREFIX_XMLNS) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{qName.rawname}, 2);
        }
        int n2 = xMLAttributes.getLength();
        int n3 = 0;
        while (n3 < n2) {
            string = xMLAttributes.getLocalName(n3);
            String string3 = xMLAttributes.getPrefix(n3);
            if (string3 == XMLSymbols.PREFIX_XMLNS || string3 == XMLSymbols.EMPTY_STRING && string == XMLSymbols.PREFIX_XMLNS) {
                string2 = this.fSymbolTable.addSymbol(xMLAttributes.getValue(n3));
                if (string3 == XMLSymbols.PREFIX_XMLNS && string == XMLSymbols.PREFIX_XMLNS) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{xMLAttributes.getQName(n3)}, 2);
                }
                if (string2 == NamespaceContext.XMLNS_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{xMLAttributes.getQName(n3)}, 2);
                }
                if (string == XMLSymbols.PREFIX_XML) {
                    if (string2 != NamespaceContext.XML_URI) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{xMLAttributes.getQName(n3)}, 2);
                    }
                } else if (string2 == NamespaceContext.XML_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{xMLAttributes.getQName(n3)}, 2);
                }
                string3 = string != XMLSymbols.PREFIX_XMLNS ? string : XMLSymbols.EMPTY_STRING;
                this.fNamespaceContext.declarePrefix(string3, string2.length() != 0 ? string2 : null);
            }
            ++n3;
        }
        string = qName.prefix != null ? qName.prefix : XMLSymbols.EMPTY_STRING;
        qName.uri = this.fNamespaceContext.getURI(string);
        if (qName.prefix == null && qName.uri != null) {
            qName.prefix = XMLSymbols.EMPTY_STRING;
        }
        if (qName.prefix != null && qName.uri == null) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{qName.prefix, qName.rawname}, 2);
        }
        int n4 = 0;
        while (n4 < n2) {
            xMLAttributes.getName(n4, this.fAttributeQName);
            string2 = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String string4 = this.fAttributeQName.rawname;
            if (string4 == XMLSymbols.PREFIX_XMLNS) {
                this.fAttributeQName.uri = this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
                xMLAttributes.setName(n4, this.fAttributeQName);
            } else if (string2 != XMLSymbols.EMPTY_STRING) {
                this.fAttributeQName.uri = this.fNamespaceContext.getURI(string2);
                if (this.fAttributeQName.uri == null) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{qName.rawname, string4, string2}, 2);
                }
                xMLAttributes.setName(n4, this.fAttributeQName);
            }
            ++n4;
        }
        int n5 = xMLAttributes.getLength();
        int n6 = 0;
        while (n6 < n5 - 1) {
            String string5 = xMLAttributes.getURI(n6);
            if (string5 != null && string5 != NamespaceContext.XMLNS_URI) {
                String string6 = xMLAttributes.getLocalName(n6);
                int n7 = n6 + 1;
                while (n7 < n5) {
                    String string7 = xMLAttributes.getLocalName(n7);
                    String string8 = xMLAttributes.getURI(n7);
                    if (string6 == string7 && string5 == string8) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{qName.rawname, string6, string5}, 2);
                    }
                    ++n7;
                }
            }
            ++n6;
        }
    }

    protected void endNamespaceScope(QName qName, Augmentations augmentations, boolean bl) throws XNIException {
        String string = qName.prefix != null ? qName.prefix : XMLSymbols.EMPTY_STRING;
        qName.uri = this.fNamespaceContext.getURI(string);
        if (qName.uri != null) {
            qName.prefix = string;
        }
        if (this.fDocumentHandler != null && !bl) {
            this.fDocumentHandler.endElement(qName, augmentations);
        }
        this.fNamespaceContext.popContext();
    }
}

