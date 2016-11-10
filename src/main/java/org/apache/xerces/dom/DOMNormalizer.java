/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.AttributeMap;
import org.apache.xerces.dom.CoreDOMImplementationImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMConfigurationImpl;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMLocatorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.EntityReferenceImpl;
import org.apache.xerces.dom.PSVIAttrNSImpl;
import org.apache.xerces.dom.PSVIElementNSImpl;
import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.impl.dtd.XMLDTDValidator;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.DOMErrorHandlerWrapper;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
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
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DOMNormalizer
implements XMLDocumentHandler {
    protected static final boolean DEBUG_ND = false;
    protected static final boolean DEBUG = false;
    protected static final boolean DEBUG_EVENTS = false;
    protected static final String PREFIX = "NS";
    protected DOMConfigurationImpl fConfiguration = null;
    protected CoreDocumentImpl fDocument = null;
    protected final XMLAttributesProxy fAttrProxy;
    protected final QName fQName;
    protected RevalidationHandler fValidationHandler;
    protected SymbolTable fSymbolTable;
    protected DOMErrorHandler fErrorHandler;
    private final DOMErrorImpl fError;
    protected boolean fNamespaceValidation;
    protected boolean fPSVI;
    protected final NamespaceContext fNamespaceContext;
    protected final NamespaceContext fLocalNSBinder;
    protected final ArrayList fAttributeList;
    protected final DOMLocatorImpl fLocator;
    protected Node fCurrentNode;
    private final QName fAttrQName;
    final XMLString fNormalizedValue;
    public static final RuntimeException abort = new RuntimeException();
    public static final XMLString EMPTY_STRING = new XMLString();
    private boolean fAllWhitespace;

    public DOMNormalizer() {
        this.fAttrProxy = new XMLAttributesProxy(this);
        this.fQName = new QName();
        this.fError = new DOMErrorImpl();
        this.fNamespaceValidation = false;
        this.fPSVI = false;
        this.fNamespaceContext = new NamespaceSupport();
        this.fLocalNSBinder = new NamespaceSupport();
        this.fAttributeList = new ArrayList(5);
        this.fLocator = new DOMLocatorImpl();
        this.fCurrentNode = null;
        this.fAttrQName = new QName();
        this.fNormalizedValue = new XMLString(new char[16], 0, 0);
        this.fAllWhitespace = false;
    }

    protected void normalizeDocument(CoreDocumentImpl coreDocumentImpl, DOMConfigurationImpl dOMConfigurationImpl) {
        Object object;
        this.fDocument = coreDocumentImpl;
        this.fConfiguration = dOMConfigurationImpl;
        this.fAllWhitespace = false;
        this.fNamespaceValidation = false;
        String string = this.fDocument.getXmlVersion();
        String string2 = null;
        String[] arrstring = null;
        this.fSymbolTable = (SymbolTable)this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fNamespaceContext.reset();
        this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, null);
        if ((this.fConfiguration.features & 64) != 0) {
            object = (String)this.fConfiguration.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
            if (object != null && object.equals(Constants.NS_XMLSCHEMA)) {
                string2 = "http://www.w3.org/2001/XMLSchema";
                this.fValidationHandler = CoreDOMImplementationImpl.singleton.getValidator(string2, string);
                this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", true);
                this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
                this.fNamespaceValidation = true;
                this.fPSVI = (this.fConfiguration.features & 128) != 0;
            } else {
                string2 = "http://www.w3.org/TR/REC-xml";
                if (object != null) {
                    arrstring = (String[])this.fConfiguration.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
                }
                this.fConfiguration.setDTDValidatorFactory(string);
                this.fValidationHandler = CoreDOMImplementationImpl.singleton.getValidator(string2, string);
                this.fPSVI = false;
            }
            this.fConfiguration.setFeature("http://xml.org/sax/features/validation", true);
            this.fDocument.clearIdentifiers();
            if (this.fValidationHandler != null) {
                ((XMLComponent)((Object)this.fValidationHandler)).reset(this.fConfiguration);
            }
        } else {
            this.fValidationHandler = null;
        }
        this.fErrorHandler = (DOMErrorHandler)this.fConfiguration.getParameter("error-handler");
        if (this.fValidationHandler != null) {
            this.fValidationHandler.setDocumentHandler(this);
            this.fValidationHandler.startDocument(new SimpleLocator(this.fDocument.fDocumentURI, this.fDocument.fDocumentURI, -1, -1), this.fDocument.encoding, this.fNamespaceContext, null);
            this.fValidationHandler.xmlDecl(this.fDocument.getXmlVersion(), this.fDocument.getXmlEncoding(), this.fDocument.getXmlStandalone() ? "yes" : "no", null);
        }
        try {
            if (string2 == "http://www.w3.org/TR/REC-xml") {
                this.processDTD(string, arrstring != null ? arrstring[0] : null);
            }
            object = this.fDocument.getFirstChild();
            while (object != null) {
                Object object2 = object.getNextSibling();
                if ((object = this.normalizeNode((Node)object)) != null) {
                    object2 = object;
                }
                object = object2;
            }
            if (this.fValidationHandler != null) {
                this.fValidationHandler.endDocument(null);
                this.fValidationHandler.setDocumentHandler(null);
                CoreDOMImplementationImpl.singleton.releaseValidator(string2, string, this.fValidationHandler);
                this.fValidationHandler = null;
            }
        }
        catch (RuntimeException runtimeException) {
            if (this.fValidationHandler != null) {
                this.fValidationHandler.setDocumentHandler(null);
                CoreDOMImplementationImpl.singleton.releaseValidator(string2, string, this.fValidationHandler);
                this.fValidationHandler = null;
            }
            if (runtimeException == abort) {
                return;
            }
            throw runtimeException;
        }
    }

    protected Node normalizeNode(Node node) {
        short s2 = node.getNodeType();
        this.fLocator.fRelatedNode = node;
        switch (s2) {
            case 10: {
                break;
            }
            case 1: {
                Node node2;
                boolean bl;
                AttributeMap attributeMap;
                Object object;
                int n2;
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0 && this.fDocument.isXMLVersionChanged()) {
                    bl = this.fNamespaceValidation ? CoreDocumentImpl.isValidQName(node.getPrefix(), node.getLocalName(), this.fDocument.isXML11Version()) : CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version());
                    if (!bl) {
                        object = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Element", node.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, (String)object, 2, "wf-invalid-character-in-node-name");
                    }
                }
                this.fNamespaceContext.pushContext();
                this.fLocalNSBinder.reset();
                object = (ElementImpl)node;
                if (object.needsSyncChildren()) {
                    object.synchronizeChildren();
                }
                AttributeMap attributeMap2 = attributeMap = object.hasAttributes() ? (AttributeMap)object.getAttributes() : null;
                if ((this.fConfiguration.features & 1) != 0) {
                    this.namespaceFixUp((ElementImpl)object, attributeMap);
                    if ((this.fConfiguration.features & 512) == 0 && attributeMap != null) {
                        n2 = 0;
                        while (n2 < attributeMap.getLength()) {
                            node2 = (Attr)attributeMap.getItem(n2);
                            if (XMLSymbols.PREFIX_XMLNS.equals(node2.getPrefix()) || XMLSymbols.PREFIX_XMLNS.equals(node2.getName())) {
                                object.removeAttributeNode((Attr)node2);
                                --n2;
                            }
                            ++n2;
                        }
                    }
                } else if (attributeMap != null) {
                    n2 = 0;
                    while (n2 < attributeMap.getLength()) {
                        node2 = (Attr)attributeMap.item(n2);
                        node2.normalize();
                        if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                            DOMNormalizer.isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, attributeMap, (Attr)node2, node2.getValue(), this.fDocument.isXML11Version());
                            if (this.fDocument.isXMLVersionChanged()) {
                                bl = this.fNamespaceValidation ? CoreDocumentImpl.isValidQName(node.getPrefix(), node.getLocalName(), this.fDocument.isXML11Version()) : CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version());
                                if (!bl) {
                                    String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Attr", node.getNodeName()});
                                    DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string, 2, "wf-invalid-character-in-node-name");
                                }
                            }
                        }
                        ++n2;
                    }
                }
                if (this.fValidationHandler != null) {
                    this.fAttrProxy.setAttributes(attributeMap, this.fDocument, (ElementImpl)object);
                    this.updateQName((Node)object, this.fQName);
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fValidationHandler.startElement(this.fQName, this.fAttrProxy, null);
                }
                Node node3 = object.getFirstChild();
                while (node3 != null) {
                    node2 = node3.getNextSibling();
                    if ((node3 = this.normalizeNode(node3)) != null) {
                        node2 = node3;
                    }
                    node3 = node2;
                }
                if (this.fValidationHandler != null) {
                    this.updateQName((Node)object, this.fQName);
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fValidationHandler.endElement(this.fQName, null);
                }
                this.fNamespaceContext.popContext();
                break;
            }
            case 8: {
                if ((this.fConfiguration.features & 32) == 0) {
                    Node node4;
                    Node node5 = node.getPreviousSibling();
                    Node node6 = node.getParentNode();
                    node6.removeChild(node);
                    if (node5 == null || node5.getNodeType() != 3 || (node4 = node5.getNextSibling()) == null || node4.getNodeType() != 3) break;
                    ((TextImpl)node4).insertData(0, node5.getNodeValue());
                    node6.removeChild(node5);
                    return node4;
                }
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                    String string = ((Comment)node).getData();
                    DOMNormalizer.isCommentWF(this.fErrorHandler, this.fError, this.fLocator, string, this.fDocument.isXML11Version());
                }
                if (this.fValidationHandler == null) break;
                this.fValidationHandler.comment(EMPTY_STRING, null);
                break;
            }
            case 5: {
                if ((this.fConfiguration.features & 4) == 0) {
                    Node node7;
                    Node node8 = node.getPreviousSibling();
                    Node node9 = node.getParentNode();
                    ((EntityReferenceImpl)node).setReadOnly(false, true);
                    this.expandEntityRef(node9, node);
                    node9.removeChild(node);
                    Node node10 = node7 = node8 != null ? node8.getNextSibling() : node9.getFirstChild();
                    if (node8 != null && node7 != null && node8.getNodeType() == 3 && node7.getNodeType() == 3) {
                        return node8;
                    }
                    return node7;
                }
                if (!this.fDocument.errorChecking || (this.fConfiguration.features & 256) == 0 || !this.fDocument.isXMLVersionChanged()) break;
                CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version());
                break;
            }
            case 4: {
                if ((this.fConfiguration.features & 8) == 0) {
                    Node node11 = node.getPreviousSibling();
                    if (node11 != null && node11.getNodeType() == 3) {
                        ((Text)node11).appendData(node.getNodeValue());
                        node.getParentNode().removeChild(node);
                        return node11;
                    }
                    Text text = this.fDocument.createTextNode(node.getNodeValue());
                    Node node12 = node.getParentNode();
                    node = node12.replaceChild(text, node);
                    return text;
                }
                if (this.fValidationHandler != null) {
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fValidationHandler.startCDATA(null);
                    this.fValidationHandler.characterData(node.getNodeValue(), null);
                    this.fValidationHandler.endCDATA(null);
                }
                String string = node.getNodeValue();
                if ((this.fConfiguration.features & 16) != 0) {
                    int n3;
                    Node node13 = node.getParentNode();
                    if (this.fDocument.errorChecking) {
                        DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), this.fDocument.isXML11Version());
                    }
                    while ((n3 = string.indexOf("]]>")) >= 0) {
                        node.setNodeValue(string.substring(0, n3 + 2));
                        string = string.substring(n3 + 2);
                        Node node14 = node;
                        CDATASection cDATASection = this.fDocument.createCDATASection(string);
                        node13.insertBefore(cDATASection, node.getNextSibling());
                        node = cDATASection;
                        this.fLocator.fRelatedNode = node14;
                        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "cdata-sections-splitted", null);
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string2, 1, "cdata-sections-splitted");
                    }
                    break;
                }
                if (!this.fDocument.errorChecking) break;
                DOMNormalizer.isCDataWF(this.fErrorHandler, this.fError, this.fLocator, string, this.fDocument.isXML11Version());
                break;
            }
            case 3: {
                int n4;
                Node node15 = node.getNextSibling();
                if (node15 != null && node15.getNodeType() == 3) {
                    ((Text)node).appendData(node15.getNodeValue());
                    node.getParentNode().removeChild(node15);
                    return node;
                }
                if (node.getNodeValue().length() == 0) {
                    node.getParentNode().removeChild(node);
                    break;
                }
                int n5 = n4 = node15 != null ? (int)node15.getNodeType() : -1;
                if (n4 != -1 && ((this.fConfiguration.features & 4) == 0 && n4 == 6 || (this.fConfiguration.features & 32) == 0 && n4 == 8 || (this.fConfiguration.features & 8) == 0 && n4 == 4)) break;
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                    DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), this.fDocument.isXML11Version());
                }
                if (this.fValidationHandler == null) break;
                this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                this.fCurrentNode = node;
                this.fValidationHandler.characterData(node.getNodeValue(), null);
                if (this.fNamespaceValidation) break;
                if (this.fAllWhitespace) {
                    this.fAllWhitespace = false;
                    ((TextImpl)node).setIgnorableWhitespace(true);
                    break;
                }
                ((TextImpl)node).setIgnorableWhitespace(false);
                break;
            }
            case 7: {
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                    ProcessingInstruction processingInstruction = (ProcessingInstruction)node;
                    String string = processingInstruction.getTarget();
                    boolean bl = this.fDocument.isXML11Version() ? XML11Char.isXML11ValidName(string) : XMLChar.isValidName(string);
                    if (!bl) {
                        String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Element", node.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string3, 2, "wf-invalid-character-in-node-name");
                    }
                    DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, processingInstruction.getData(), this.fDocument.isXML11Version());
                }
                if (this.fValidationHandler == null) break;
                this.fValidationHandler.processingInstruction(((ProcessingInstruction)node).getTarget(), EMPTY_STRING, null);
            }
        }
        return null;
    }

    private void processDTD(String string, String string2) {
        Object object;
        String string3 = null;
        String string4 = null;
        String string5 = string2;
        String string6 = this.fDocument.getDocumentURI();
        String string7 = null;
        DocumentType documentType = this.fDocument.getDoctype();
        if (documentType != null) {
            string3 = documentType.getName();
            string4 = documentType.getPublicId();
            if (string5 == null || string5.length() == 0) {
                string5 = documentType.getSystemId();
            }
            string7 = documentType.getInternalSubset();
        } else {
            object = this.fDocument.getDocumentElement();
            if (object == null) {
                return;
            }
            string3 = object.getNodeName();
            if (string5 == null || string5.length() == 0) {
                return;
            }
        }
        object = null;
        try {
            try {
                this.fValidationHandler.doctypeDecl(string3, string4, string5, null);
                object = CoreDOMImplementationImpl.singleton.getDTDLoader(string);
                object.setFeature("http://xml.org/sax/features/validation", true);
                object.setEntityResolver(this.fConfiguration.getEntityResolver());
                object.setErrorHandler(this.fConfiguration.getErrorHandler());
                object.loadGrammarWithContext((XMLDTDValidator)this.fValidationHandler, string3, string4, string5, string6, string7);
            }
            catch (IOException iOException) {
                Object var12_11 = null;
                if (object != null) {
                    CoreDOMImplementationImpl.singleton.releaseDTDLoader(string, (XMLDTDLoader)object);
                }
            }
            Object var12_10 = null;
            if (object != null) {
                CoreDOMImplementationImpl.singleton.releaseDTDLoader(string, (XMLDTDLoader)object);
            }
        }
        catch (Throwable throwable) {
            Object var12_12 = null;
            if (object != null) {
                CoreDOMImplementationImpl.singleton.releaseDTDLoader(string, (XMLDTDLoader)object);
            }
            throw throwable;
        }
    }

    protected final void expandEntityRef(Node node, Node node2) {
        Node node3 = node2.getFirstChild();
        while (node3 != null) {
            Node node4 = node3.getNextSibling();
            node.insertBefore(node3, node2);
            node3 = node4;
        }
    }

    protected final void namespaceFixUp(ElementImpl elementImpl, AttributeMap attributeMap) {
        String string;
        Attr attr;
        String string2;
        int n2;
        String string3;
        if (attributeMap != null) {
            n2 = 0;
            while (n2 < attributeMap.getLength()) {
                attr = (Attr)attributeMap.getItem(n2);
                string3 = attr.getNamespaceURI();
                if (string3 != null && string3.equals(NamespaceContext.XMLNS_URI)) {
                    String string4;
                    string = attr.getNodeValue();
                    if (string == null) {
                        string = XMLSymbols.EMPTY_STRING;
                    }
                    if (this.fDocument.errorChecking && string.equals(NamespaceContext.XMLNS_URI)) {
                        this.fLocator.fRelatedNode = attr;
                        string4 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", null);
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string4, 2, "CantBindXMLNS");
                    } else {
                        string2 = attr.getPrefix();
                        string2 = string2 == null || string2.length() == 0 ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string2);
                        string4 = this.fSymbolTable.addSymbol(attr.getLocalName());
                        if (string2 == XMLSymbols.PREFIX_XMLNS) {
                            if ((string = this.fSymbolTable.addSymbol(string)).length() != 0) {
                                this.fNamespaceContext.declarePrefix(string4, string);
                            }
                        } else {
                            this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, (string = this.fSymbolTable.addSymbol(string)).length() != 0 ? string : null);
                        }
                    }
                }
                ++n2;
            }
        }
        string3 = elementImpl.getNamespaceURI();
        string2 = elementImpl.getPrefix();
        if (string3 != null) {
            string3 = this.fSymbolTable.addSymbol(string3);
            String string5 = string2 = string2 == null || string2.length() == 0 ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string2);
            if (this.fNamespaceContext.getURI(string2) != string3) {
                this.addNamespaceDecl(string2, string3, elementImpl);
                this.fLocalNSBinder.declarePrefix(string2, string3);
                this.fNamespaceContext.declarePrefix(string2, string3);
            }
        } else if (elementImpl.getLocalName() == null) {
            if (this.fNamespaceValidation) {
                String string6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[]{elementImpl.getNodeName()});
                DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string6, 3, "NullLocalElementName");
            } else {
                String string7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[]{elementImpl.getNodeName()});
                DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string7, 2, "NullLocalElementName");
            }
        } else {
            string3 = this.fNamespaceContext.getURI(XMLSymbols.EMPTY_STRING);
            if (string3 != null && string3.length() > 0) {
                this.addNamespaceDecl(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING, elementImpl);
                this.fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, null);
                this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, null);
            }
        }
        if (attributeMap != null) {
            attributeMap.cloneMap(this.fAttributeList);
            n2 = 0;
            while (n2 < this.fAttributeList.size()) {
                String string8;
                attr = (Attr)this.fAttributeList.get(n2);
                this.fLocator.fRelatedNode = attr;
                attr.normalize();
                string = attr.getValue();
                string3 = attr.getNamespaceURI();
                if (string == null) {
                    string = XMLSymbols.EMPTY_STRING;
                }
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                    boolean bl;
                    DOMNormalizer.isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, attributeMap, attr, string, this.fDocument.isXML11Version());
                    if (this.fDocument.isXMLVersionChanged() && !(bl = this.fNamespaceValidation ? CoreDocumentImpl.isValidQName(attr.getPrefix(), attr.getLocalName(), this.fDocument.isXML11Version()) : CoreDocumentImpl.isXMLName(attr.getNodeName(), this.fDocument.isXML11Version()))) {
                        string8 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Attr", attr.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string8, 2, "wf-invalid-character-in-node-name");
                    }
                }
                if (string3 != null) {
                    string2 = attr.getPrefix();
                    string2 = string2 == null || string2.length() == 0 ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(string2);
                    this.fSymbolTable.addSymbol(attr.getLocalName());
                    if (string3 == null || !string3.equals(NamespaceContext.XMLNS_URI)) {
                        ((AttrImpl)attr).setIdAttribute(false);
                        string3 = this.fSymbolTable.addSymbol(string3);
                        String string9 = this.fNamespaceContext.getURI(string2);
                        if (string2 == XMLSymbols.EMPTY_STRING || string9 != string3) {
                            string8 = this.fNamespaceContext.getPrefix(string3);
                            if (string8 != null && string8 != XMLSymbols.EMPTY_STRING) {
                                string2 = string8;
                            } else {
                                if (string2 == XMLSymbols.EMPTY_STRING || this.fLocalNSBinder.getURI(string2) != null) {
                                    int n3 = 1;
                                    string2 = this.fSymbolTable.addSymbol("NS" + n3++);
                                    while (this.fLocalNSBinder.getURI(string2) != null) {
                                        string2 = this.fSymbolTable.addSymbol("NS" + n3++);
                                    }
                                }
                                this.addNamespaceDecl(string2, string3, elementImpl);
                                string = this.fSymbolTable.addSymbol(string);
                                this.fLocalNSBinder.declarePrefix(string2, string);
                                this.fNamespaceContext.declarePrefix(string2, string3);
                            }
                            attr.setPrefix(string2);
                        }
                    }
                } else {
                    ((AttrImpl)attr).setIdAttribute(false);
                    if (attr.getLocalName() == null) {
                        if (this.fNamespaceValidation) {
                            String string10 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[]{attr.getNodeName()});
                            DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string10, 3, "NullLocalAttrName");
                        } else {
                            String string11 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[]{attr.getNodeName()});
                            DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string11, 2, "NullLocalAttrName");
                        }
                    }
                }
                ++n2;
            }
        }
    }

    protected final void addNamespaceDecl(String string, String string2, ElementImpl elementImpl) {
        if (string == XMLSymbols.EMPTY_STRING) {
            elementImpl.setAttributeNS(NamespaceContext.XMLNS_URI, XMLSymbols.PREFIX_XMLNS, string2);
        } else {
            elementImpl.setAttributeNS(NamespaceContext.XMLNS_URI, "xmlns:" + string, string2);
        }
    }

    public static final void isCDataWF(DOMErrorHandler dOMErrorHandler, DOMErrorImpl dOMErrorImpl, DOMLocatorImpl dOMLocatorImpl, String string, boolean bl) {
        char c2;
        if (string == null || string.length() == 0) {
            return;
        }
        char[] arrc = string.toCharArray();
        char c3 = arrc.length;
        if (bl) {
            char c4 = '\u0000';
            while (c4 < c3) {
                char c5;
                if (XML11Char.isXML11Invalid(c5 = arrc[c4++])) {
                    if (XMLChar.isHighSurrogate(c5) && c4 < c3 && XMLChar.isLowSurrogate(c2 = arrc[c4++]) && XMLChar.isSupplemental(XMLChar.supplemental(c5, c2))) continue;
                    String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInCDSect", new Object[]{Integer.toString(c5, 16)});
                    DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string2, 2, "wf-invalid-character");
                    continue;
                }
                if (c5 != ']' || (c2 = c4) >= c3 || arrc[c2] != ']') continue;
                while (++c2 < c3 && arrc[c2] == ']') {
                }
                if (c2 >= c3 || arrc[c2] != '>') continue;
                String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CDEndInContent", null);
                DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string3, 2, "wf-invalid-character");
            }
        } else {
            char c6 = '\u0000';
            while (c6 < c3) {
                char c7;
                if (XMLChar.isInvalid(c7 = arrc[c6++])) {
                    if (XMLChar.isHighSurrogate(c7) && c6 < c3 && XMLChar.isLowSurrogate(c2 = arrc[c6++]) && XMLChar.isSupplemental(XMLChar.supplemental(c7, c2))) continue;
                    String string4 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInCDSect", new Object[]{Integer.toString(c7, 16)});
                    DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string4, 2, "wf-invalid-character");
                    continue;
                }
                if (c7 != ']' || (c2 = c6) >= c3 || arrc[c2] != ']') continue;
                while (++c2 < c3 && arrc[c2] == ']') {
                }
                if (c2 >= c3 || arrc[c2] != '>') continue;
                String string5 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CDEndInContent", null);
                DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string5, 2, "wf-invalid-character");
            }
        }
    }

    public static final void isXMLCharWF(DOMErrorHandler dOMErrorHandler, DOMErrorImpl dOMErrorImpl, DOMLocatorImpl dOMLocatorImpl, String string, boolean bl) {
        char c2;
        if (string == null || string.length() == 0) {
            return;
        }
        char[] arrc = string.toCharArray();
        int n2 = arrc.length;
        if (bl) {
            int n3 = 0;
            while (n3 < n2) {
                char c3;
                if (!XML11Char.isXML11Invalid(arrc[n3++]) || XMLChar.isHighSurrogate(c3 = arrc[n3 - 1]) && n3 < n2 && XMLChar.isLowSurrogate(c2 = arrc[n3++]) && XMLChar.isSupplemental(XMLChar.supplemental(c3, c2))) continue;
                String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidXMLCharInDOM", new Object[]{Integer.toString(arrc[n3 - 1], 16)});
                DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string2, 2, "wf-invalid-character");
            }
        } else {
            int n4 = 0;
            while (n4 < n2) {
                char c4;
                if (!XMLChar.isInvalid(arrc[n4++]) || XMLChar.isHighSurrogate(c4 = arrc[n4 - 1]) && n4 < n2 && XMLChar.isLowSurrogate(c2 = arrc[n4++]) && XMLChar.isSupplemental(XMLChar.supplemental(c4, c2))) continue;
                String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidXMLCharInDOM", new Object[]{Integer.toString(arrc[n4 - 1], 16)});
                DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string3, 2, "wf-invalid-character");
            }
        }
    }

    public static final void isCommentWF(DOMErrorHandler dOMErrorHandler, DOMErrorImpl dOMErrorImpl, DOMLocatorImpl dOMLocatorImpl, String string, boolean bl) {
        char c2;
        if (string == null || string.length() == 0) {
            return;
        }
        char[] arrc = string.toCharArray();
        int n2 = arrc.length;
        if (bl) {
            int n3 = 0;
            while (n3 < n2) {
                char c3;
                if (XML11Char.isXML11Invalid(c3 = arrc[n3++])) {
                    if (XMLChar.isHighSurrogate(c3) && n3 < n2 && XMLChar.isLowSurrogate(c2 = arrc[n3++]) && XMLChar.isSupplemental(XMLChar.supplemental(c3, c2))) continue;
                    String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInComment", new Object[]{Integer.toString(arrc[n3 - 1], 16)});
                    DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string2, 2, "wf-invalid-character");
                    continue;
                }
                if (c3 != '-' || n3 >= n2 || arrc[n3] != '-') continue;
                String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "DashDashInComment", null);
                DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string3, 2, "wf-invalid-character");
            }
        } else {
            int n4 = 0;
            while (n4 < n2) {
                char c4;
                if (XMLChar.isInvalid(c4 = arrc[n4++])) {
                    if (XMLChar.isHighSurrogate(c4) && n4 < n2 && XMLChar.isLowSurrogate(c2 = arrc[n4++]) && XMLChar.isSupplemental(XMLChar.supplemental(c4, c2))) continue;
                    String string4 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInComment", new Object[]{Integer.toString(arrc[n4 - 1], 16)});
                    DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string4, 2, "wf-invalid-character");
                    continue;
                }
                if (c4 != '-' || n4 >= n2 || arrc[n4] != '-') continue;
                String string5 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "DashDashInComment", null);
                DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string5, 2, "wf-invalid-character");
            }
        }
    }

    public static final void isAttrValueWF(DOMErrorHandler dOMErrorHandler, DOMErrorImpl dOMErrorImpl, DOMLocatorImpl dOMLocatorImpl, NamedNodeMap namedNodeMap, Attr attr, String string, boolean bl) {
        if (attr instanceof AttrImpl && ((AttrImpl)attr).hasStringValue()) {
            DOMNormalizer.isXMLCharWF(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, string, bl);
        } else {
            NodeList nodeList = attr.getChildNodes();
            int n2 = 0;
            while (n2 < nodeList.getLength()) {
                Node node = nodeList.item(n2);
                if (node.getNodeType() == 5) {
                    Object object;
                    Document document = attr.getOwnerDocument();
                    Entity entity = null;
                    if (document != null && (object = document.getDoctype()) != null) {
                        NamedNodeMap namedNodeMap2 = object.getEntities();
                        entity = (Entity)namedNodeMap2.getNamedItemNS("*", node.getNodeName());
                    }
                    if (entity == null) {
                        object = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "UndeclaredEntRefInAttrValue", new Object[]{attr.getNodeName()});
                        DOMNormalizer.reportDOMError(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, (String)object, 2, "UndeclaredEntRefInAttrValue");
                    }
                } else {
                    DOMNormalizer.isXMLCharWF(dOMErrorHandler, dOMErrorImpl, dOMLocatorImpl, node.getNodeValue(), bl);
                }
                ++n2;
            }
        }
    }

    public static final void reportDOMError(DOMErrorHandler dOMErrorHandler, DOMErrorImpl dOMErrorImpl, DOMLocatorImpl dOMLocatorImpl, String string, short s2, String string2) {
        if (dOMErrorHandler != null) {
            dOMErrorImpl.reset();
            dOMErrorImpl.fMessage = string;
            dOMErrorImpl.fSeverity = s2;
            dOMErrorImpl.fLocator = dOMLocatorImpl;
            dOMErrorImpl.fType = string2;
            dOMErrorImpl.fRelatedData = dOMLocatorImpl.fRelatedNode;
            if (!dOMErrorHandler.handleError(dOMErrorImpl)) {
                throw abort;
            }
        }
        if (s2 == 3) {
            throw abort;
        }
    }

    protected final void updateQName(Node node, QName qName) {
        String string = node.getPrefix();
        String string2 = node.getNamespaceURI();
        String string3 = node.getLocalName();
        qName.prefix = string != null && string.length() != 0 ? this.fSymbolTable.addSymbol(string) : null;
        qName.localpart = string3 != null ? this.fSymbolTable.addSymbol(string3) : null;
        qName.rawname = this.fSymbolTable.addSymbol(node.getNodeName());
        qName.uri = string2 != null ? this.fSymbolTable.addSymbol(string2) : null;
    }

    final String normalizeAttributeValue(String string, Attr attr) {
        if (!attr.getSpecified()) {
            return string;
        }
        int n2 = string.length();
        if (this.fNormalizedValue.ch.length < n2) {
            this.fNormalizedValue.ch = new char[n2];
        }
        this.fNormalizedValue.length = 0;
        boolean bl = false;
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (c2 == '\t' || c2 == '\n') {
                this.fNormalizedValue.ch[this.fNormalizedValue.length++] = 32;
                bl = true;
            } else if (c2 == '\r') {
                bl = true;
                this.fNormalizedValue.ch[this.fNormalizedValue.length++] = 32;
                int n4 = n3 + 1;
                if (n4 < n2 && string.charAt(n4) == '\n') {
                    n3 = n4;
                }
            } else {
                this.fNormalizedValue.ch[this.fNormalizedValue.length++] = c2;
            }
            ++n3;
        }
        if (bl) {
            string = this.fNormalizedValue.toString();
            attr.setValue(string);
        }
        return string;
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
        Element element = (Element)this.fCurrentNode;
        int n2 = xMLAttributes.getLength();
        int n3 = 0;
        while (n3 < n2) {
            boolean bl;
            Object object;
            AttributePSVI attributePSVI;
            xMLAttributes.getName(n3, this.fAttrQName);
            Attr attr = null;
            attr = element.getAttributeNodeNS(this.fAttrQName.uri, this.fAttrQName.localpart);
            if (attr == null) {
                attr = element.getAttributeNode(this.fAttrQName.rawname);
            }
            if ((attributePSVI = (AttributePSVI)xMLAttributes.getAugmentations(n3).getItem("ATTRIBUTE_PSVI")) != null) {
                String string;
                object = attributePSVI.getMemberTypeDefinition();
                bl = false;
                if (object != null) {
                    bl = ((XSSimpleType)object).isIDType();
                } else {
                    object = attributePSVI.getTypeDefinition();
                    if (object != null) {
                        bl = ((XSSimpleType)object).isIDType();
                    }
                }
                if (bl) {
                    ((ElementImpl)element).setIdAttributeNode(attr, true);
                }
                if (this.fPSVI) {
                    ((PSVIAttrNSImpl)attr).setPSVI(attributePSVI);
                }
                ((AttrImpl)attr).setType(object);
                if ((this.fConfiguration.features & 2) != 0 && (string = attributePSVI.getSchemaNormalizedValue()) != null) {
                    boolean bl2 = attr.getSpecified();
                    attr.setValue(string);
                    if (!bl2) {
                        ((AttrImpl)attr).setSpecified(bl2);
                    }
                }
            } else {
                object = null;
                bl = Boolean.TRUE.equals(xMLAttributes.getAugmentations(n3).getItem("ATTRIBUTE_DECLARED"));
                if (bl && "ID".equals(object = xMLAttributes.getType(n3))) {
                    ((ElementImpl)element).setIdAttributeNode(attr, true);
                }
                ((AttrImpl)attr).setType(object);
            }
            ++n3;
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
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        this.fAllWhitespace = true;
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        ElementPSVI elementPSVI;
        if (augmentations != null && (elementPSVI = (ElementPSVI)augmentations.getItem("ELEMENT_PSVI")) != null) {
            Object object;
            ElementImpl elementImpl = (ElementImpl)this.fCurrentNode;
            if (this.fPSVI) {
                ((PSVIElementNSImpl)this.fCurrentNode).setPSVI(elementPSVI);
            }
            if (elementImpl instanceof ElementNSImpl) {
                object = elementPSVI.getMemberTypeDefinition();
                if (object == null) {
                    object = elementPSVI.getTypeDefinition();
                }
                ((ElementNSImpl)elementImpl).setType((XSTypeDefinition)object);
            }
            object = elementPSVI.getSchemaNormalizedValue();
            if ((this.fConfiguration.features & 2) != 0) {
                if (object != null) {
                    elementImpl.setTextContent((String)object);
                }
            } else {
                String string = elementImpl.getTextContent();
                if (string.length() == 0 && object != null) {
                    elementImpl.setTextContent((String)object);
                }
            }
            return;
        }
        if (this.fCurrentNode instanceof ElementNSImpl) {
            ((ElementNSImpl)this.fCurrentNode).setType(null);
        }
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
    }

    public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
    }

    public XMLDocumentSource getDocumentSource() {
        return null;
    }

    protected final class XMLAttributesProxy
    implements XMLAttributes {
        protected AttributeMap fAttributes;
        protected CoreDocumentImpl fDocument;
        protected ElementImpl fElement;
        protected final Vector fDTDTypes;
        protected final Vector fAugmentations;
        private final DOMNormalizer this$0;

        protected XMLAttributesProxy(DOMNormalizer dOMNormalizer) {
            this.this$0 = dOMNormalizer;
            this.fDTDTypes = new Vector(5);
            this.fAugmentations = new Vector(5);
        }

        public void setAttributes(AttributeMap attributeMap, CoreDocumentImpl coreDocumentImpl, ElementImpl elementImpl) {
            this.fDocument = coreDocumentImpl;
            this.fAttributes = attributeMap;
            this.fElement = elementImpl;
            if (attributeMap != null) {
                int n2 = attributeMap.getLength();
                this.fDTDTypes.setSize(n2);
                this.fAugmentations.setSize(n2);
                int n3 = 0;
                while (n3 < n2) {
                    this.fAugmentations.setElementAt(new AugmentationsImpl(), n3);
                    ++n3;
                }
            } else {
                this.fDTDTypes.setSize(0);
                this.fAugmentations.setSize(0);
            }
        }

        public int addAttribute(QName qName, String string, String string2) {
            int n2 = this.fElement.getXercesAttribute(qName.uri, qName.localpart);
            if (n2 < 0) {
                AttrImpl attrImpl = (AttrImpl)((CoreDocumentImpl)this.fElement.getOwnerDocument()).createAttributeNS(qName.uri, qName.rawname, qName.localpart);
                attrImpl.setNodeValue(string2);
                n2 = this.fElement.setXercesAttributeNode(attrImpl);
                this.fDTDTypes.insertElementAt(string, n2);
                this.fAugmentations.insertElementAt(new AugmentationsImpl(), n2);
                attrImpl.setSpecified(false);
            }
            return n2;
        }

        public void removeAllAttributes() {
        }

        public void removeAttributeAt(int n2) {
        }

        public int getLength() {
            return this.fAttributes != null ? this.fAttributes.getLength() : 0;
        }

        public int getIndex(String string) {
            return -1;
        }

        public int getIndex(String string, String string2) {
            return -1;
        }

        public void setName(int n2, QName qName) {
        }

        public void getName(int n2, QName qName) {
            if (this.fAttributes != null) {
                this.this$0.updateQName((Node)this.fAttributes.getItem(n2), qName);
            }
        }

        public String getPrefix(int n2) {
            if (this.fAttributes != null) {
                Node node = (Node)this.fAttributes.getItem(n2);
                String string = node.getPrefix();
                string = string != null && string.length() != 0 ? this.this$0.fSymbolTable.addSymbol(string) : null;
                return string;
            }
            return null;
        }

        public String getURI(int n2) {
            if (this.fAttributes != null) {
                Node node = (Node)this.fAttributes.getItem(n2);
                String string = node.getNamespaceURI();
                string = string != null ? this.this$0.fSymbolTable.addSymbol(string) : null;
                return string;
            }
            return null;
        }

        public String getLocalName(int n2) {
            if (this.fAttributes != null) {
                Node node = (Node)this.fAttributes.getItem(n2);
                String string = node.getLocalName();
                string = string != null ? this.this$0.fSymbolTable.addSymbol(string) : null;
                return string;
            }
            return null;
        }

        public String getQName(int n2) {
            if (this.fAttributes != null) {
                Node node = (Node)this.fAttributes.getItem(n2);
                String string = this.this$0.fSymbolTable.addSymbol(node.getNodeName());
                return string;
            }
            return null;
        }

        public void setType(int n2, String string) {
            this.fDTDTypes.setElementAt(string, n2);
        }

        public String getType(int n2) {
            String string = (String)this.fDTDTypes.elementAt(n2);
            return string != null ? this.getReportableType(string) : "CDATA";
        }

        public String getType(String string) {
            return "CDATA";
        }

        public String getType(String string, String string2) {
            return "CDATA";
        }

        private String getReportableType(String string) {
            if (string.charAt(0) == '(') {
                return "NMTOKEN";
            }
            return string;
        }

        public void setValue(int n2, String string) {
            if (this.fAttributes != null) {
                AttrImpl attrImpl = (AttrImpl)this.fAttributes.getItem(n2);
                boolean bl = attrImpl.getSpecified();
                attrImpl.setValue(string);
                attrImpl.setSpecified(bl);
            }
        }

        public String getValue(int n2) {
            return this.fAttributes != null ? this.fAttributes.item(n2).getNodeValue() : "";
        }

        public String getValue(String string) {
            return null;
        }

        public String getValue(String string, String string2) {
            if (this.fAttributes != null) {
                Node node = this.fAttributes.getNamedItemNS(string, string2);
                return node != null ? node.getNodeValue() : null;
            }
            return null;
        }

        public void setNonNormalizedValue(int n2, String string) {
        }

        public String getNonNormalizedValue(int n2) {
            return null;
        }

        public void setSpecified(int n2, boolean bl) {
            AttrImpl attrImpl = (AttrImpl)this.fAttributes.getItem(n2);
            attrImpl.setSpecified(bl);
        }

        public boolean isSpecified(int n2) {
            return ((Attr)this.fAttributes.getItem(n2)).getSpecified();
        }

        public Augmentations getAugmentations(int n2) {
            return (Augmentations)this.fAugmentations.elementAt(n2);
        }

        public Augmentations getAugmentations(String string, String string2) {
            return null;
        }

        public Augmentations getAugmentations(String string) {
            return null;
        }

        public void setAugmentations(int n2, Augmentations augmentations) {
            this.fAugmentations.setElementAt(augmentations, n2);
        }
    }

}

