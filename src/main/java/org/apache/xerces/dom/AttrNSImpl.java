/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.xni.NamespaceContext;
import org.w3c.dom.DOMException;

public class AttrNSImpl
extends AttrImpl {
    static final long serialVersionUID = -781906615369795414L;
    static final String xmlnsURI = "http://www.w3.org/2000/xmlns/";
    static final String xmlURI = "http://www.w3.org/XML/1998/namespace";
    protected String namespaceURI;
    protected String localName;

    public AttrNSImpl() {
    }

    protected AttrNSImpl(CoreDocumentImpl coreDocumentImpl, String string, String string2) {
        super(coreDocumentImpl, string2);
        this.setName(string, string2);
    }

    private void setName(String string, String string2) {
        CoreDocumentImpl coreDocumentImpl = this.ownerDocument();
        this.namespaceURI = string;
        if (string != null) {
            this.namespaceURI = string.length() == 0 ? null : string;
        }
        int n2 = string2.indexOf(58);
        int n3 = string2.lastIndexOf(58);
        coreDocumentImpl.checkNamespaceWF(string2, n2, n3);
        if (n2 < 0) {
            this.localName = string2;
            if (coreDocumentImpl.errorChecking) {
                coreDocumentImpl.checkQName(null, this.localName);
                if (string2.equals("xmlns") && (string == null || !string.equals(NamespaceContext.XMLNS_URI)) || string != null && string.equals(NamespaceContext.XMLNS_URI) && !string2.equals("xmlns")) {
                    String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
                    throw new DOMException(14, string3);
                }
            }
        } else {
            String string4 = string2.substring(0, n2);
            this.localName = string2.substring(n3 + 1);
            coreDocumentImpl.checkQName(string4, this.localName);
            coreDocumentImpl.checkDOMNSErr(string4, string);
        }
    }

    public AttrNSImpl(CoreDocumentImpl coreDocumentImpl, String string, String string2, String string3) {
        super(coreDocumentImpl, string2);
        this.localName = string3;
        this.namespaceURI = string;
    }

    protected AttrNSImpl(CoreDocumentImpl coreDocumentImpl, String string) {
        super(coreDocumentImpl, string);
    }

    void rename(String string, String string2) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.name = string2;
        this.setName(string, string2);
    }

    public String getNamespaceURI() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.namespaceURI;
    }

    public String getPrefix() {
        int n2;
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return (n2 = this.name.indexOf(58)) < 0 ? null : this.name.substring(0, n2);
    }

    public void setPrefix(String string) throws DOMException {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        if (this.ownerDocument().errorChecking) {
            if (this.isReadOnly()) {
                String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException(7, string2);
            }
            if (string != null && string.length() != 0) {
                if (!CoreDocumentImpl.isXMLName(string, this.ownerDocument().isXML11Version())) {
                    String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
                    throw new DOMException(5, string3);
                }
                if (this.namespaceURI == null || string.indexOf(58) >= 0) {
                    String string4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
                    throw new DOMException(14, string4);
                }
                if (string.equals("xmlns")) {
                    if (!this.namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
                        String string5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
                        throw new DOMException(14, string5);
                    }
                } else if (string.equals("xml")) {
                    if (!this.namespaceURI.equals("http://www.w3.org/XML/1998/namespace")) {
                        String string6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
                        throw new DOMException(14, string6);
                    }
                } else if (this.name.equals("xmlns")) {
                    String string7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
                    throw new DOMException(14, string7);
                }
            }
        }
        this.name = string != null && string.length() != 0 ? string + ":" + this.localName : this.localName;
    }

    public String getLocalName() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.localName;
    }

    public String getTypeName() {
        if (this.type != null) {
            if (this.type instanceof XSSimpleTypeDecl) {
                return ((XSSimpleTypeDecl)this.type).getName();
            }
            return (String)this.type;
        }
        return null;
    }

    public boolean isDerivedFrom(String string, String string2, int n2) {
        if (this.type != null && this.type instanceof XSSimpleTypeDecl) {
            return ((XSSimpleTypeDecl)this.type).isDOMDerivedFrom(string, string2, n2);
        }
        return false;
    }

    public String getTypeNamespace() {
        if (this.type != null) {
            if (this.type instanceof XSSimpleTypeDecl) {
                return ((XSSimpleTypeDecl)this.type).getNamespace();
            }
            return "http://www.w3.org/TR/REC-xml";
        }
        return null;
    }
}

