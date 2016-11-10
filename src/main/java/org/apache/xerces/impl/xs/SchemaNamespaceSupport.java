/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SchemaNamespaceSupport
extends NamespaceSupport {
    private SchemaRootContext fSchemaRootContext = null;

    public SchemaNamespaceSupport(Element element, SymbolTable symbolTable) {
        Document document;
        if (element != null && !(element instanceof ElementImpl) && (document = element.getOwnerDocument()) != null && element != document.getDocumentElement()) {
            this.fSchemaRootContext = new SchemaRootContext(element, symbolTable);
        }
    }

    public SchemaNamespaceSupport(SchemaNamespaceSupport schemaNamespaceSupport) {
        this.fSchemaRootContext = schemaNamespaceSupport.fSchemaRootContext;
        this.fNamespaceSize = schemaNamespaceSupport.fNamespaceSize;
        if (this.fNamespace.length < this.fNamespaceSize) {
            this.fNamespace = new String[this.fNamespaceSize];
        }
        System.arraycopy(schemaNamespaceSupport.fNamespace, 0, this.fNamespace, 0, this.fNamespaceSize);
        this.fCurrentContext = schemaNamespaceSupport.fCurrentContext;
        if (this.fContext.length <= this.fCurrentContext) {
            this.fContext = new int[this.fCurrentContext + 1];
        }
        System.arraycopy(schemaNamespaceSupport.fContext, 0, this.fContext, 0, this.fCurrentContext + 1);
    }

    public void setEffectiveContext(String[] arrstring) {
        if (arrstring == null || arrstring.length == 0) {
            return;
        }
        this.pushContext();
        int n2 = this.fNamespaceSize + arrstring.length;
        if (this.fNamespace.length < n2) {
            String[] arrstring2 = new String[n2];
            System.arraycopy(this.fNamespace, 0, arrstring2, 0, this.fNamespace.length);
            this.fNamespace = arrstring2;
        }
        System.arraycopy(arrstring, 0, this.fNamespace, this.fNamespaceSize, arrstring.length);
        this.fNamespaceSize = n2;
    }

    public String[] getEffectiveLocalContext() {
        int n2;
        int n3;
        String[] arrstring = null;
        if (this.fCurrentContext >= 3 && (n3 = this.fNamespaceSize - (n2 = this.fContext[3])) > 0) {
            arrstring = new String[n3];
            System.arraycopy(this.fNamespace, n2, arrstring, 0, n3);
        }
        return arrstring;
    }

    public void makeGlobal() {
        if (this.fCurrentContext >= 3) {
            this.fCurrentContext = 3;
            this.fNamespaceSize = this.fContext[3];
        }
    }

    public String getURI(String string) {
        String string2 = super.getURI(string);
        if (string2 == null && this.fSchemaRootContext != null) {
            if (!this.fSchemaRootContext.fDOMContextBuilt) {
                this.fSchemaRootContext.fillNamespaceContext();
                this.fSchemaRootContext.fDOMContextBuilt = true;
            }
            if (this.fSchemaRootContext.fNamespaceSize > 0 && !this.containsPrefix(string)) {
                string2 = this.fSchemaRootContext.getURI(string);
            }
        }
        return string2;
    }

    static final class SchemaRootContext {
        String[] fNamespace = new String[32];
        int fNamespaceSize = 0;
        boolean fDOMContextBuilt = false;
        private final Element fSchemaRoot;
        private final SymbolTable fSymbolTable;
        private final QName fAttributeQName = new QName();

        SchemaRootContext(Element element, SymbolTable symbolTable) {
            this.fSchemaRoot = element;
            this.fSymbolTable = symbolTable;
        }

        void fillNamespaceContext() {
            if (this.fSchemaRoot != null) {
                Node node = this.fSchemaRoot.getParentNode();
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
                            this.fillQName(this.fAttributeQName, attr);
                            if (this.fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
                                if (this.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                                    this.declarePrefix(this.fAttributeQName.localpart, string.length() != 0 ? this.fSymbolTable.addSymbol(string) : null);
                                } else {
                                    this.declarePrefix(XMLSymbols.EMPTY_STRING, string.length() != 0 ? this.fSymbolTable.addSymbol(string) : null);
                                }
                            }
                            ++n3;
                        }
                    }
                    node = node.getParentNode();
                }
            }
        }

        String getURI(String string) {
            int n2 = 0;
            while (n2 < this.fNamespaceSize) {
                if (this.fNamespace[n2] == string) {
                    return this.fNamespace[n2 + 1];
                }
                n2 += 2;
            }
            return null;
        }

        private void declarePrefix(String string, String string2) {
            if (this.fNamespaceSize == this.fNamespace.length) {
                String[] arrstring = new String[this.fNamespaceSize * 2];
                System.arraycopy(this.fNamespace, 0, arrstring, 0, this.fNamespaceSize);
                this.fNamespace = arrstring;
            }
            this.fNamespace[this.fNamespaceSize++] = string;
            this.fNamespace[this.fNamespaceSize++] = string2;
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
    }

}

