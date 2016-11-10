/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.parsers;

import java.util.Locale;
import java.util.Stack;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.DocumentTypeImpl;
import org.apache.xerces.dom.ElementDefinitionImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.EntityImpl;
import org.apache.xerces.dom.EntityReferenceImpl;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.NotationImpl;
import org.apache.xerces.dom.PSVIAttrNSImpl;
import org.apache.xerces.dom.PSVIDocumentImpl;
import org.apache.xerces.dom.PSVIElementNSImpl;
import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.parsers.AbstractXMLDocumentParser;
import org.apache.xerces.parsers.ObjectFactory;
import org.apache.xerces.util.DOMErrorHandlerWrapper;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
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
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.ls.LSParserFilter;

public class AbstractDOMParser
extends AbstractXMLDocumentParser {
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String CREATE_ENTITY_REF_NODES = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
    protected static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
    protected static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
    protected static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
    protected static final String DEFER_NODE_EXPANSION = "http://apache.org/xml/features/dom/defer-node-expansion";
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces", "http://apache.org/xml/features/dom/create-entity-ref-nodes", "http://apache.org/xml/features/include-comments", "http://apache.org/xml/features/create-cdata-nodes", "http://apache.org/xml/features/dom/include-ignorable-whitespace", "http://apache.org/xml/features/dom/defer-node-expansion"};
    protected static final String DOCUMENT_CLASS_NAME = "http://apache.org/xml/properties/dom/document-class-name";
    protected static final String CURRENT_ELEMENT_NODE = "http://apache.org/xml/properties/dom/current-element-node";
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/dom/document-class-name", "http://apache.org/xml/properties/dom/current-element-node"};
    protected static final String DEFAULT_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.DocumentImpl";
    protected static final String CORE_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.CoreDocumentImpl";
    protected static final String PSVI_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.PSVIDocumentImpl";
    private static final boolean DEBUG_EVENTS = false;
    private static final boolean DEBUG_BASEURI = false;
    protected DOMErrorHandlerWrapper fErrorHandler = null;
    protected boolean fInDTD;
    protected boolean fCreateEntityRefNodes;
    protected boolean fIncludeIgnorableWhitespace;
    protected boolean fIncludeComments;
    protected boolean fCreateCDATANodes;
    protected Document fDocument;
    protected CoreDocumentImpl fDocumentImpl;
    protected boolean fStorePSVI;
    protected String fDocumentClassName;
    protected DocumentType fDocumentType;
    protected Node fCurrentNode;
    protected CDATASection fCurrentCDATASection;
    protected EntityImpl fCurrentEntityDecl;
    protected int fDeferredEntityDecl;
    protected final StringBuffer fStringBuffer = new StringBuffer(50);
    protected StringBuffer fInternalSubset;
    protected boolean fDeferNodeExpansion;
    protected boolean fNamespaceAware;
    protected DeferredDocumentImpl fDeferredDocumentImpl;
    protected int fDocumentIndex;
    protected int fDocumentTypeIndex;
    protected int fCurrentNodeIndex;
    protected int fCurrentCDATASectionIndex;
    protected boolean fInDTDExternalSubset;
    protected Node fRoot;
    protected boolean fInCDATASection;
    protected boolean fFirstChunk = false;
    protected boolean fFilterReject = false;
    protected final Stack fBaseURIStack = new Stack();
    protected int fRejectedElementDepth = 0;
    protected Stack fSkippedElemStack = null;
    protected boolean fInEntityRef = false;
    private final QName fAttrQName = new QName();
    private XMLLocator fLocator;
    protected LSParserFilter fDOMFilter = null;
    static Class class$org$w3c$dom$Document;

    protected AbstractDOMParser(XMLParserConfiguration xMLParserConfiguration) {
        super(xMLParserConfiguration);
        this.fConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", true);
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", true);
        this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
        this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", true);
        this.fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
        this.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", "org.apache.xerces.dom.DocumentImpl");
    }

    protected String getDocumentClassName() {
        return this.fDocumentClassName;
    }

    protected void setDocumentClassName(String string) {
        if (string == null) {
            string = "org.apache.xerces.dom.DocumentImpl";
        }
        if (!string.equals("org.apache.xerces.dom.DocumentImpl") && !string.equals("org.apache.xerces.dom.PSVIDocumentImpl")) {
            try {
                Class class_ = ObjectFactory.findProviderClass(string, ObjectFactory.findClassLoader(), true);
                Class class_2 = class$org$w3c$dom$Document == null ? (AbstractDOMParser.class$org$w3c$dom$Document = AbstractDOMParser.class$("org.w3c.dom.Document")) : class$org$w3c$dom$Document;
                if (!class_2.isAssignableFrom(class_)) {
                    throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidDocumentClassName", new Object[]{string}));
                }
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "MissingDocumentClassName", new Object[]{string}));
            }
        }
        this.fDocumentClassName = string;
        if (!string.equals("org.apache.xerces.dom.DocumentImpl")) {
            this.fDeferNodeExpansion = false;
        }
    }

    public Document getDocument() {
        return this.fDocument;
    }

    public final void dropDocumentReferences() {
        this.fDocument = null;
        this.fDocumentImpl = null;
        this.fDeferredDocumentImpl = null;
        this.fDocumentType = null;
        this.fCurrentNode = null;
        this.fCurrentCDATASection = null;
        this.fCurrentEntityDecl = null;
        this.fRoot = null;
    }

    public void reset() throws XNIException {
        super.reset();
        this.fCreateEntityRefNodes = this.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes");
        this.fIncludeIgnorableWhitespace = this.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace");
        this.fDeferNodeExpansion = this.fConfiguration.getFeature("http://apache.org/xml/features/dom/defer-node-expansion");
        this.fNamespaceAware = this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
        this.fIncludeComments = this.fConfiguration.getFeature("http://apache.org/xml/features/include-comments");
        this.fCreateCDATANodes = this.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes");
        this.setDocumentClassName((String)this.fConfiguration.getProperty("http://apache.org/xml/properties/dom/document-class-name"));
        this.fDocument = null;
        this.fDocumentImpl = null;
        this.fStorePSVI = false;
        this.fDocumentType = null;
        this.fDocumentTypeIndex = -1;
        this.fDeferredDocumentImpl = null;
        this.fCurrentNode = null;
        this.fStringBuffer.setLength(0);
        this.fRoot = null;
        this.fInDTD = false;
        this.fInDTDExternalSubset = false;
        this.fInCDATASection = false;
        this.fFirstChunk = false;
        this.fCurrentCDATASection = null;
        this.fCurrentCDATASectionIndex = -1;
        this.fBaseURIStack.removeAllElements();
    }

    public void setLocale(Locale locale) {
        this.fConfiguration.setLocale(locale);
    }

    public void startGeneralEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        if (!this.fDeferNodeExpansion) {
            if (this.fFilterReject) {
                return;
            }
            this.setCharacterData(true);
            EntityReference entityReference = this.fDocument.createEntityReference(string);
            if (this.fDocumentImpl != null) {
                EntityReferenceImpl entityReferenceImpl = (EntityReferenceImpl)entityReference;
                entityReferenceImpl.setBaseURI(xMLResourceIdentifier.getExpandedSystemId());
                if (this.fDocumentType != null) {
                    NamedNodeMap namedNodeMap = this.fDocumentType.getEntities();
                    this.fCurrentEntityDecl = (EntityImpl)namedNodeMap.getNamedItem(string);
                    if (this.fCurrentEntityDecl != null) {
                        this.fCurrentEntityDecl.setInputEncoding(string2);
                    }
                }
                entityReferenceImpl.needsSyncChildren(false);
            }
            this.fInEntityRef = true;
            this.fCurrentNode.appendChild(entityReference);
            this.fCurrentNode = entityReference;
        } else {
            int n2 = this.fDeferredDocumentImpl.createDeferredEntityReference(string, xMLResourceIdentifier.getExpandedSystemId());
            if (this.fDocumentTypeIndex != -1) {
                int n3 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
                while (n3 != -1) {
                    String string3;
                    short s2 = this.fDeferredDocumentImpl.getNodeType(n3, false);
                    if (s2 == 6 && (string3 = this.fDeferredDocumentImpl.getNodeName(n3, false)).equals(string)) {
                        this.fDeferredEntityDecl = n3;
                        this.fDeferredDocumentImpl.setInputEncoding(n3, string2);
                        break;
                    }
                    n3 = this.fDeferredDocumentImpl.getRealPrevSibling(n3, false);
                }
            }
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, n2);
            this.fCurrentNodeIndex = n2;
        }
    }

    public void textDecl(String string, String string2, Augmentations augmentations) throws XNIException {
        if (this.fInDTD) {
            return;
        }
        if (!this.fDeferNodeExpansion) {
            if (this.fCurrentEntityDecl != null && !this.fFilterReject) {
                this.fCurrentEntityDecl.setXmlEncoding(string2);
                if (string != null) {
                    this.fCurrentEntityDecl.setXmlVersion(string);
                }
            }
        } else if (this.fDeferredEntityDecl != -1) {
            this.fDeferredDocumentImpl.setEntityInfo(this.fDeferredEntityDecl, string, string2);
        }
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fInDTD) {
            if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
                this.fInternalSubset.append("<!--");
                if (xMLString.length > 0) {
                    this.fInternalSubset.append(xMLString.ch, xMLString.offset, xMLString.length);
                }
                this.fInternalSubset.append("-->");
            }
            return;
        }
        if (!this.fIncludeComments || this.fFilterReject) {
            return;
        }
        if (!this.fDeferNodeExpansion) {
            Comment comment = this.fDocument.createComment(xMLString.toString());
            this.setCharacterData(false);
            this.fCurrentNode.appendChild(comment);
            if (this.fDOMFilter != null && !this.fInEntityRef && (this.fDOMFilter.getWhatToShow() & 128) != 0) {
                short s2 = this.fDOMFilter.acceptNode(comment);
                switch (s2) {
                    case 4: {
                        throw Abort.INSTANCE;
                    }
                    case 2: 
                    case 3: {
                        this.fCurrentNode.removeChild(comment);
                        this.fFirstChunk = true;
                        return;
                    }
                }
            }
        } else {
            int n2 = this.fDeferredDocumentImpl.createDeferredComment(xMLString.toString());
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, n2);
        }
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (this.fInDTD) {
            if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
                this.fInternalSubset.append("<?");
                this.fInternalSubset.append(string);
                if (xMLString.length > 0) {
                    this.fInternalSubset.append(' ').append(xMLString.ch, xMLString.offset, xMLString.length);
                }
                this.fInternalSubset.append("?>");
            }
            return;
        }
        if (!this.fDeferNodeExpansion) {
            if (this.fFilterReject) {
                return;
            }
            ProcessingInstruction processingInstruction = this.fDocument.createProcessingInstruction(string, xMLString.toString());
            this.setCharacterData(false);
            this.fCurrentNode.appendChild(processingInstruction);
            if (this.fDOMFilter != null && !this.fInEntityRef && (this.fDOMFilter.getWhatToShow() & 64) != 0) {
                short s2 = this.fDOMFilter.acceptNode(processingInstruction);
                switch (s2) {
                    case 4: {
                        throw Abort.INSTANCE;
                    }
                    case 2: 
                    case 3: {
                        this.fCurrentNode.removeChild(processingInstruction);
                        this.fFirstChunk = true;
                        return;
                    }
                }
            }
        } else {
            int n2 = this.fDeferredDocumentImpl.createDeferredProcessingInstruction(string, xMLString.toString());
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, n2);
        }
    }

    public void startDocument(XMLLocator xMLLocator, String string, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
        this.fLocator = xMLLocator;
        if (!this.fDeferNodeExpansion) {
            if (this.fDocumentClassName.equals("org.apache.xerces.dom.DocumentImpl")) {
                this.fDocument = new DocumentImpl();
                this.fDocumentImpl = (CoreDocumentImpl)this.fDocument;
                this.fDocumentImpl.setStrictErrorChecking(false);
                this.fDocumentImpl.setInputEncoding(string);
                this.fDocumentImpl.setDocumentURI(xMLLocator.getExpandedSystemId());
            } else if (this.fDocumentClassName.equals("org.apache.xerces.dom.PSVIDocumentImpl")) {
                this.fDocument = new PSVIDocumentImpl();
                this.fDocumentImpl = (CoreDocumentImpl)this.fDocument;
                this.fStorePSVI = true;
                this.fDocumentImpl.setStrictErrorChecking(false);
                this.fDocumentImpl.setInputEncoding(string);
                this.fDocumentImpl.setDocumentURI(xMLLocator.getExpandedSystemId());
            } else {
                try {
                    ClassLoader classLoader = ObjectFactory.findClassLoader();
                    Class class_ = ObjectFactory.findProviderClass(this.fDocumentClassName, classLoader, true);
                    this.fDocument = (Document)class_.newInstance();
                    Class class_2 = ObjectFactory.findProviderClass("org.apache.xerces.dom.CoreDocumentImpl", classLoader, true);
                    if (class_2.isAssignableFrom(class_)) {
                        this.fDocumentImpl = (CoreDocumentImpl)this.fDocument;
                        Class class_3 = ObjectFactory.findProviderClass("org.apache.xerces.dom.PSVIDocumentImpl", classLoader, true);
                        if (class_3.isAssignableFrom(class_)) {
                            this.fStorePSVI = true;
                        }
                        this.fDocumentImpl.setStrictErrorChecking(false);
                        this.fDocumentImpl.setInputEncoding(string);
                        if (xMLLocator != null) {
                            this.fDocumentImpl.setDocumentURI(xMLLocator.getExpandedSystemId());
                        }
                    }
                }
                catch (ClassNotFoundException classNotFoundException) {
                }
                catch (Exception exception) {
                    throw new RuntimeException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "CannotCreateDocumentClass", new Object[]{this.fDocumentClassName}));
                }
            }
            this.fCurrentNode = this.fDocument;
        } else {
            this.fDeferredDocumentImpl = new DeferredDocumentImpl(this.fNamespaceAware);
            this.fDocument = this.fDeferredDocumentImpl;
            this.fDocumentIndex = this.fDeferredDocumentImpl.createDeferredDocument();
            this.fDeferredDocumentImpl.setInputEncoding(string);
            this.fDeferredDocumentImpl.setDocumentURI(xMLLocator.getExpandedSystemId());
            this.fCurrentNodeIndex = this.fDocumentIndex;
        }
    }

    public void xmlDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        if (!this.fDeferNodeExpansion) {
            if (this.fDocumentImpl != null) {
                if (string != null) {
                    this.fDocumentImpl.setXmlVersion(string);
                }
                this.fDocumentImpl.setXmlEncoding(string2);
                this.fDocumentImpl.setXmlStandalone("yes".equals(string3));
            }
        } else {
            if (string != null) {
                this.fDeferredDocumentImpl.setXmlVersion(string);
            }
            this.fDeferredDocumentImpl.setXmlEncoding(string2);
            this.fDeferredDocumentImpl.setXmlStandalone("yes".equals(string3));
        }
    }

    public void doctypeDecl(String string, String string2, String string3, Augmentations augmentations) throws XNIException {
        if (!this.fDeferNodeExpansion) {
            if (this.fDocumentImpl != null) {
                this.fDocumentType = this.fDocumentImpl.createDocumentType(string, string2, string3);
                this.fCurrentNode.appendChild(this.fDocumentType);
            }
        } else {
            this.fDocumentTypeIndex = this.fDeferredDocumentImpl.createDeferredDocumentType(string, string2, string3);
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, this.fDocumentTypeIndex);
        }
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        if (!this.fDeferNodeExpansion) {
            Object object;
            Object object2;
            if (this.fFilterReject) {
                ++this.fRejectedElementDepth;
                return;
            }
            Element element = this.createElementNode(qName);
            int n2 = xMLAttributes.getLength();
            boolean bl = false;
            int n3 = 0;
            while (n3 < n2) {
                xMLAttributes.getName(n3, this.fAttrQName);
                object = this.createAttrNode(this.fAttrQName);
                object2 = xMLAttributes.getValue(n3);
                AttributePSVI attributePSVI = (AttributePSVI)xMLAttributes.getAugmentations(n3).getItem("ATTRIBUTE_PSVI");
                if (this.fStorePSVI && attributePSVI != null) {
                    ((PSVIAttrNSImpl)object).setPSVI(attributePSVI);
                }
                object.setValue((String)object2);
                boolean bl2 = xMLAttributes.isSpecified(n3);
                if (!bl2 && (bl || this.fAttrQName.uri != null && this.fAttrQName.uri != NamespaceContext.XMLNS_URI && this.fAttrQName.prefix == null)) {
                    element.setAttributeNodeNS((Attr)object);
                    bl = true;
                } else {
                    element.setAttributeNode((Attr)object);
                }
                if (this.fDocumentImpl != null) {
                    AttrImpl attrImpl = (AttrImpl)object;
                    Object object3 = null;
                    boolean bl3 = false;
                    if (attributePSVI != null && this.fNamespaceAware) {
                        object3 = attributePSVI.getMemberTypeDefinition();
                        if (object3 == null) {
                            object3 = attributePSVI.getTypeDefinition();
                            if (object3 != null) {
                                bl3 = ((XSSimpleType)object3).isIDType();
                                attrImpl.setType(object3);
                            }
                        } else {
                            bl3 = ((XSSimpleType)object3).isIDType();
                            attrImpl.setType(object3);
                        }
                    } else {
                        boolean bl4 = Boolean.TRUE.equals(xMLAttributes.getAugmentations(n3).getItem("ATTRIBUTE_DECLARED"));
                        if (bl4) {
                            object3 = xMLAttributes.getType(n3);
                            bl3 = "ID".equals(object3);
                        }
                        attrImpl.setType(object3);
                    }
                    if (bl3) {
                        ((ElementImpl)element).setIdAttributeNode((Attr)object, true);
                    }
                    attrImpl.setSpecified(bl2);
                }
                ++n3;
            }
            this.setCharacterData(false);
            if (augmentations != null && (object = (ElementPSVI)augmentations.getItem("ELEMENT_PSVI")) != null && this.fNamespaceAware) {
                object2 = object.getMemberTypeDefinition();
                if (object2 == null) {
                    object2 = object.getTypeDefinition();
                }
                ((ElementNSImpl)element).setType((XSTypeDefinition)object2);
            }
            if (this.fDOMFilter != null && !this.fInEntityRef) {
                if (this.fRoot == null) {
                    this.fRoot = element;
                } else {
                    short s2 = this.fDOMFilter.startElement(element);
                    switch (s2) {
                        case 4: {
                            throw Abort.INSTANCE;
                        }
                        case 2: {
                            this.fFilterReject = true;
                            this.fRejectedElementDepth = 0;
                            return;
                        }
                        case 3: {
                            this.fFirstChunk = true;
                            this.fSkippedElemStack.push(Boolean.TRUE);
                            return;
                        }
                    }
                    if (!this.fSkippedElemStack.isEmpty()) {
                        this.fSkippedElemStack.push(Boolean.FALSE);
                    }
                }
            }
            this.fCurrentNode.appendChild(element);
            this.fCurrentNode = element;
        } else {
            int n4 = this.fDeferredDocumentImpl.createDeferredElement(this.fNamespaceAware ? qName.uri : null, qName.rawname);
            Object object = null;
            int n5 = xMLAttributes.getLength();
            int n6 = n5 - 1;
            while (n6 >= 0) {
                AttributePSVI attributePSVI = (AttributePSVI)xMLAttributes.getAugmentations(n6).getItem("ATTRIBUTE_PSVI");
                boolean bl = false;
                if (attributePSVI != null && this.fNamespaceAware) {
                    object = attributePSVI.getMemberTypeDefinition();
                    if (object == null) {
                        object = attributePSVI.getTypeDefinition();
                        if (object != null) {
                            bl = ((XSSimpleType)object).isIDType();
                        }
                    } else {
                        bl = ((XSSimpleType)object).isIDType();
                    }
                } else {
                    boolean bl5 = Boolean.TRUE.equals(xMLAttributes.getAugmentations(n6).getItem("ATTRIBUTE_DECLARED"));
                    if (bl5) {
                        object = xMLAttributes.getType(n6);
                        bl = "ID".equals(object);
                    }
                }
                this.fDeferredDocumentImpl.setDeferredAttribute(n4, xMLAttributes.getQName(n6), xMLAttributes.getURI(n6), xMLAttributes.getValue(n6), xMLAttributes.isSpecified(n6), bl, object);
                --n6;
            }
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, n4);
            this.fCurrentNodeIndex = n4;
        }
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        this.startElement(qName, xMLAttributes, augmentations);
        this.endElement(qName, augmentations);
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (!this.fDeferNodeExpansion) {
            if (this.fFilterReject) {
                return;
            }
            if (this.fInCDATASection && this.fCreateCDATANodes) {
                if (this.fCurrentCDATASection == null) {
                    this.fCurrentCDATASection = this.fDocument.createCDATASection(xMLString.toString());
                    this.fCurrentNode.appendChild(this.fCurrentCDATASection);
                    this.fCurrentNode = this.fCurrentCDATASection;
                } else {
                    this.fCurrentCDATASection.appendData(xMLString.toString());
                }
            } else if (!this.fInDTD) {
                if (xMLString.length == 0) {
                    return;
                }
                Node node = this.fCurrentNode.getLastChild();
                if (node != null && node.getNodeType() == 3) {
                    if (this.fFirstChunk) {
                        if (this.fDocumentImpl != null) {
                            this.fStringBuffer.append(((TextImpl)node).removeData());
                        } else {
                            this.fStringBuffer.append(((Text)node).getData());
                            ((Text)node).setNodeValue(null);
                        }
                        this.fFirstChunk = false;
                    }
                    if (xMLString.length > 0) {
                        this.fStringBuffer.append(xMLString.ch, xMLString.offset, xMLString.length);
                    }
                } else {
                    this.fFirstChunk = true;
                    Text text = this.fDocument.createTextNode(xMLString.toString());
                    this.fCurrentNode.appendChild(text);
                }
            }
        } else if (this.fInCDATASection && this.fCreateCDATANodes) {
            if (this.fCurrentCDATASectionIndex == -1) {
                int n2 = this.fDeferredDocumentImpl.createDeferredCDATASection(xMLString.toString());
                this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, n2);
                this.fCurrentCDATASectionIndex = n2;
                this.fCurrentNodeIndex = n2;
            } else {
                int n3 = this.fDeferredDocumentImpl.createDeferredTextNode(xMLString.toString(), false);
                this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, n3);
            }
        } else if (!this.fInDTD) {
            if (xMLString.length == 0) {
                return;
            }
            String string = xMLString.toString();
            int n4 = this.fDeferredDocumentImpl.createDeferredTextNode(string, false);
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, n4);
        }
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (!this.fIncludeIgnorableWhitespace || this.fFilterReject) {
            return;
        }
        if (!this.fDeferNodeExpansion) {
            Node node = this.fCurrentNode.getLastChild();
            if (node != null && node.getNodeType() == 3) {
                Text text = (Text)node;
                text.appendData(xMLString.toString());
            } else {
                Text text = this.fDocument.createTextNode(xMLString.toString());
                if (this.fDocumentImpl != null) {
                    TextImpl textImpl = (TextImpl)text;
                    textImpl.setIgnorableWhitespace(true);
                }
                this.fCurrentNode.appendChild(text);
            }
        } else {
            int n2 = this.fDeferredDocumentImpl.createDeferredTextNode(xMLString.toString(), true);
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, n2);
        }
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        if (!this.fDeferNodeExpansion) {
            Object object;
            ElementPSVI elementPSVI;
            if (augmentations != null && this.fDocumentImpl != null && (this.fNamespaceAware || this.fStorePSVI) && (elementPSVI = (ElementPSVI)augmentations.getItem("ELEMENT_PSVI")) != null) {
                if (this.fNamespaceAware) {
                    object = elementPSVI.getMemberTypeDefinition();
                    if (object == null) {
                        object = elementPSVI.getTypeDefinition();
                    }
                    ((ElementNSImpl)this.fCurrentNode).setType((XSTypeDefinition)object);
                }
                if (this.fStorePSVI) {
                    ((PSVIElementNSImpl)this.fCurrentNode).setPSVI(elementPSVI);
                }
            }
            if (this.fDOMFilter != null) {
                if (this.fFilterReject) {
                    if (this.fRejectedElementDepth-- == 0) {
                        this.fFilterReject = false;
                    }
                    return;
                }
                if (!this.fSkippedElemStack.isEmpty() && this.fSkippedElemStack.pop() == Boolean.TRUE) {
                    return;
                }
                this.setCharacterData(false);
                if (this.fCurrentNode != this.fRoot && !this.fInEntityRef && (this.fDOMFilter.getWhatToShow() & 1) != 0) {
                    short s2 = this.fDOMFilter.acceptNode(this.fCurrentNode);
                    switch (s2) {
                        case 4: {
                            throw Abort.INSTANCE;
                        }
                        case 2: {
                            object = this.fCurrentNode.getParentNode();
                            object.removeChild(this.fCurrentNode);
                            this.fCurrentNode = object;
                            return;
                        }
                        case 3: {
                            this.fFirstChunk = true;
                            object = this.fCurrentNode.getParentNode();
                            NodeList nodeList = this.fCurrentNode.getChildNodes();
                            int n2 = nodeList.getLength();
                            int n3 = 0;
                            while (n3 < n2) {
                                object.appendChild(nodeList.item(0));
                                ++n3;
                            }
                            object.removeChild(this.fCurrentNode);
                            this.fCurrentNode = object;
                            return;
                        }
                    }
                }
                this.fCurrentNode = this.fCurrentNode.getParentNode();
            } else {
                this.setCharacterData(false);
                this.fCurrentNode = this.fCurrentNode.getParentNode();
            }
        } else {
            ElementPSVI elementPSVI;
            if (augmentations != null && (elementPSVI = (ElementPSVI)augmentations.getItem("ELEMENT_PSVI")) != null) {
                XSTypeDefinition xSTypeDefinition = elementPSVI.getMemberTypeDefinition();
                if (xSTypeDefinition == null) {
                    xSTypeDefinition = elementPSVI.getTypeDefinition();
                }
                this.fDeferredDocumentImpl.setTypeInfo(this.fCurrentNodeIndex, xSTypeDefinition);
            }
            this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
        }
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATASection = true;
        if (!this.fDeferNodeExpansion) {
            if (this.fFilterReject) {
                return;
            }
            if (this.fCreateCDATANodes) {
                this.setCharacterData(false);
            }
        }
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATASection = false;
        if (!this.fDeferNodeExpansion) {
            if (this.fFilterReject) {
                return;
            }
            if (this.fCurrentCDATASection != null) {
                if (this.fDOMFilter != null && !this.fInEntityRef && (this.fDOMFilter.getWhatToShow() & 8) != 0) {
                    short s2 = this.fDOMFilter.acceptNode(this.fCurrentCDATASection);
                    switch (s2) {
                        case 4: {
                            throw Abort.INSTANCE;
                        }
                        case 2: 
                        case 3: {
                            Node node = this.fCurrentNode.getParentNode();
                            node.removeChild(this.fCurrentCDATASection);
                            this.fCurrentNode = node;
                            return;
                        }
                    }
                }
                this.fCurrentNode = this.fCurrentNode.getParentNode();
                this.fCurrentCDATASection = null;
            }
        } else if (this.fCurrentCDATASectionIndex != -1) {
            this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
            this.fCurrentCDATASectionIndex = -1;
        }
    }

    public void endDocument(Augmentations augmentations) throws XNIException {
        if (!this.fDeferNodeExpansion) {
            if (this.fDocumentImpl != null) {
                if (this.fLocator != null) {
                    this.fDocumentImpl.setInputEncoding(this.fLocator.getEncoding());
                }
                this.fDocumentImpl.setStrictErrorChecking(true);
            }
            this.fCurrentNode = null;
        } else {
            if (this.fLocator != null) {
                this.fDeferredDocumentImpl.setInputEncoding(this.fLocator.getEncoding());
            }
            this.fCurrentNodeIndex = -1;
        }
    }

    public void endGeneralEntity(String string, Augmentations augmentations) throws XNIException {
        if (!this.fDeferNodeExpansion) {
            Object object;
            Node node;
            if (this.fFilterReject) {
                return;
            }
            this.setCharacterData(true);
            if (this.fDocumentType != null) {
                NamedNodeMap namedNodeMap = this.fDocumentType.getEntities();
                this.fCurrentEntityDecl = (EntityImpl)namedNodeMap.getNamedItem(string);
                if (this.fCurrentEntityDecl != null) {
                    if (this.fCurrentEntityDecl != null && this.fCurrentEntityDecl.getFirstChild() == null) {
                        this.fCurrentEntityDecl.setReadOnly(false, true);
                        object = this.fCurrentNode.getFirstChild();
                        while (object != null) {
                            node = object.cloneNode(true);
                            this.fCurrentEntityDecl.appendChild(node);
                            object = object.getNextSibling();
                        }
                        this.fCurrentEntityDecl.setReadOnly(true, true);
                    }
                    this.fCurrentEntityDecl = null;
                }
            }
            this.fInEntityRef = false;
            boolean bl = false;
            if (this.fCreateEntityRefNodes) {
                if (this.fDocumentImpl != null) {
                    ((NodeImpl)this.fCurrentNode).setReadOnly(true, true);
                }
                if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 16) != 0) {
                    short s2 = this.fDOMFilter.acceptNode(this.fCurrentNode);
                    switch (s2) {
                        case 4: {
                            throw Abort.INSTANCE;
                        }
                        case 2: {
                            node = this.fCurrentNode.getParentNode();
                            node.removeChild(this.fCurrentNode);
                            this.fCurrentNode = node;
                            return;
                        }
                        case 3: {
                            this.fFirstChunk = true;
                            bl = true;
                            break;
                        }
                        default: {
                            this.fCurrentNode = this.fCurrentNode.getParentNode();
                            break;
                        }
                    }
                } else {
                    this.fCurrentNode = this.fCurrentNode.getParentNode();
                }
            }
            if (!this.fCreateEntityRefNodes || bl) {
                object = this.fCurrentNode.getChildNodes();
                node = this.fCurrentNode.getParentNode();
                int n2 = object.getLength();
                if (n2 > 0) {
                    Node node2 = this.fCurrentNode.getPreviousSibling();
                    Node node3 = object.item(0);
                    if (node2 != null && node2.getNodeType() == 3 && node3.getNodeType() == 3) {
                        ((Text)node2).appendData(node3.getNodeValue());
                        this.fCurrentNode.removeChild(node3);
                    } else {
                        node2 = node.insertBefore(node3, this.fCurrentNode);
                        this.handleBaseURI(node2);
                    }
                    int n3 = 1;
                    while (n3 < n2) {
                        node2 = node.insertBefore(object.item(0), this.fCurrentNode);
                        this.handleBaseURI(node2);
                        ++n3;
                    }
                }
                node.removeChild(this.fCurrentNode);
                this.fCurrentNode = node;
            }
        } else {
            int n4;
            int n5;
            if (this.fDocumentTypeIndex != -1) {
                n4 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
                while (n4 != -1) {
                    String string2;
                    n5 = this.fDeferredDocumentImpl.getNodeType(n4, false);
                    if (n5 == 6 && (string2 = this.fDeferredDocumentImpl.getNodeName(n4, false)).equals(string)) {
                        this.fDeferredEntityDecl = n4;
                        break;
                    }
                    n4 = this.fDeferredDocumentImpl.getRealPrevSibling(n4, false);
                }
            }
            if (this.fDeferredEntityDecl != -1 && this.fDeferredDocumentImpl.getLastChild(this.fDeferredEntityDecl, false) == -1) {
                n4 = -1;
                n5 = this.fDeferredDocumentImpl.getLastChild(this.fCurrentNodeIndex, false);
                while (n5 != -1) {
                    int n6 = this.fDeferredDocumentImpl.cloneNode(n5, true);
                    this.fDeferredDocumentImpl.insertBefore(this.fDeferredEntityDecl, n6, n4);
                    n4 = n6;
                    n5 = this.fDeferredDocumentImpl.getRealPrevSibling(n5, false);
                }
            }
            if (this.fCreateEntityRefNodes) {
                this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
            } else {
                n4 = this.fDeferredDocumentImpl.getLastChild(this.fCurrentNodeIndex, false);
                n5 = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
                int n7 = this.fCurrentNodeIndex;
                int n8 = n4;
                int n9 = -1;
                while (n4 != -1) {
                    this.handleBaseURI(n4);
                    n9 = this.fDeferredDocumentImpl.getRealPrevSibling(n4, false);
                    this.fDeferredDocumentImpl.insertBefore(n5, n4, n7);
                    n7 = n4;
                    n4 = n9;
                }
                if (n8 != -1) {
                    this.fDeferredDocumentImpl.setAsLastChild(n5, n8);
                } else {
                    n9 = this.fDeferredDocumentImpl.getRealPrevSibling(n7, false);
                    this.fDeferredDocumentImpl.setAsLastChild(n5, n9);
                }
                this.fCurrentNodeIndex = n5;
            }
            this.fDeferredEntityDecl = -1;
        }
    }

    protected final void handleBaseURI(Node node) {
        if (this.fDocumentImpl != null) {
            String string = null;
            short s2 = node.getNodeType();
            if (s2 == 1) {
                if (this.fNamespaceAware ? ((Element)node).getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "base") != null : ((Element)node).getAttributeNode("xml:base") != null) {
                    return;
                }
                string = ((EntityReferenceImpl)this.fCurrentNode).getBaseURI();
                if (string != null && !string.equals(this.fDocumentImpl.getDocumentURI())) {
                    if (this.fNamespaceAware) {
                        ((Element)node).setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base", string);
                    } else {
                        ((Element)node).setAttribute("xml:base", string);
                    }
                }
            } else if (s2 == 7 && (string = ((EntityReferenceImpl)this.fCurrentNode).getBaseURI()) != null && this.fErrorHandler != null) {
                DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                dOMErrorImpl.fType = "pi-base-uri-not-preserved";
                dOMErrorImpl.fRelatedData = string;
                dOMErrorImpl.fSeverity = 1;
                this.fErrorHandler.getErrorHandler().handleError(dOMErrorImpl);
            }
        }
    }

    protected final void handleBaseURI(int n2) {
        short s2 = this.fDeferredDocumentImpl.getNodeType(n2, false);
        if (s2 == 1) {
            String string = this.fDeferredDocumentImpl.getNodeValueString(this.fCurrentNodeIndex, false);
            if (string == null) {
                string = this.fDeferredDocumentImpl.getDeferredEntityBaseURI(this.fDeferredEntityDecl);
            }
            if (string != null && !string.equals(this.fDeferredDocumentImpl.getDocumentURI())) {
                this.fDeferredDocumentImpl.setDeferredAttribute(n2, "xml:base", "http://www.w3.org/XML/1998/namespace", string, true);
            }
        } else if (s2 == 7) {
            String string = this.fDeferredDocumentImpl.getNodeValueString(this.fCurrentNodeIndex, false);
            if (string == null) {
                string = this.fDeferredDocumentImpl.getDeferredEntityBaseURI(this.fDeferredEntityDecl);
            }
            if (string != null && this.fErrorHandler != null) {
                DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                dOMErrorImpl.fType = "pi-base-uri-not-preserved";
                dOMErrorImpl.fRelatedData = string;
                dOMErrorImpl.fSeverity = 1;
                this.fErrorHandler.getErrorHandler().handleError(dOMErrorImpl);
            }
        }
    }

    public void startDTD(XMLLocator xMLLocator, Augmentations augmentations) throws XNIException {
        this.fInDTD = true;
        if (xMLLocator != null) {
            this.fBaseURIStack.push(xMLLocator.getBaseSystemId());
        }
        if (this.fDeferNodeExpansion || this.fDocumentImpl != null) {
            this.fInternalSubset = new StringBuffer(1024);
        }
    }

    public void endDTD(Augmentations augmentations) throws XNIException {
        String string;
        this.fInDTD = false;
        if (!this.fBaseURIStack.isEmpty()) {
            this.fBaseURIStack.pop();
        }
        String string2 = string = this.fInternalSubset != null && this.fInternalSubset.length() > 0 ? this.fInternalSubset.toString() : null;
        if (this.fDeferNodeExpansion) {
            if (string != null) {
                this.fDeferredDocumentImpl.setInternalSubset(this.fDocumentTypeIndex, string);
            }
        } else if (this.fDocumentImpl != null && string != null) {
            ((DocumentTypeImpl)this.fDocumentType).setInternalSubset(string);
        }
    }

    public void startConditional(short s2, Augmentations augmentations) throws XNIException {
    }

    public void endConditional(Augmentations augmentations) throws XNIException {
    }

    public void startExternalSubset(XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        this.fBaseURIStack.push(xMLResourceIdentifier.getBaseSystemId());
        this.fInDTDExternalSubset = true;
    }

    public void endExternalSubset(Augmentations augmentations) throws XNIException {
        this.fInDTDExternalSubset = false;
        this.fBaseURIStack.pop();
    }

    public void internalEntityDecl(String string, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
        int n2;
        EntityImpl entityImpl;
        Object object;
        if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
            this.fInternalSubset.append("<!ENTITY ");
            if (string.startsWith("%")) {
                this.fInternalSubset.append("% ");
                this.fInternalSubset.append(string.substring(1));
            } else {
                this.fInternalSubset.append(string);
            }
            this.fInternalSubset.append(' ');
            object = xMLString2.toString();
            n2 = object.indexOf(39) == -1 ? 1 : 0;
            this.fInternalSubset.append(n2 != 0 ? '\'' : '\"');
            this.fInternalSubset.append((String)object);
            this.fInternalSubset.append(n2 != 0 ? '\'' : '\"');
            this.fInternalSubset.append(">\n");
        }
        if (string.startsWith("%")) {
            return;
        }
        if (this.fDocumentType != null && (entityImpl = (EntityImpl)(object = this.fDocumentType.getEntities()).getNamedItem(string)) == null) {
            entityImpl = (EntityImpl)this.fDocumentImpl.createEntity(string);
            entityImpl.setBaseURI((String)this.fBaseURIStack.peek());
            object.setNamedItem(entityImpl);
        }
        if (this.fDocumentTypeIndex != -1) {
            int n3;
            boolean bl = false;
            n2 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
            while (n2 != -1) {
                String string2;
                n3 = this.fDeferredDocumentImpl.getNodeType(n2, false);
                if (n3 == 6 && (string2 = this.fDeferredDocumentImpl.getNodeName(n2, false)).equals(string)) {
                    bl = true;
                    break;
                }
                n2 = this.fDeferredDocumentImpl.getRealPrevSibling(n2, false);
            }
            if (!bl) {
                n3 = this.fDeferredDocumentImpl.createDeferredEntity(string, null, null, null, (String)this.fBaseURIStack.peek());
                this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, n3);
            }
        }
    }

    public void externalEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        NamedNodeMap namedNodeMap;
        EntityImpl entityImpl;
        String string2 = xMLResourceIdentifier.getPublicId();
        String string3 = xMLResourceIdentifier.getLiteralSystemId();
        if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
            this.fInternalSubset.append("<!ENTITY ");
            if (string.startsWith("%")) {
                this.fInternalSubset.append("% ");
                this.fInternalSubset.append(string.substring(1));
            } else {
                this.fInternalSubset.append(string);
            }
            this.fInternalSubset.append(' ');
            if (string2 != null) {
                this.fInternalSubset.append("PUBLIC '");
                this.fInternalSubset.append(string2);
                this.fInternalSubset.append("' '");
            } else {
                this.fInternalSubset.append("SYSTEM '");
            }
            this.fInternalSubset.append(string3);
            this.fInternalSubset.append("'>\n");
        }
        if (string.startsWith("%")) {
            return;
        }
        if (this.fDocumentType != null && (entityImpl = (EntityImpl)(namedNodeMap = this.fDocumentType.getEntities()).getNamedItem(string)) == null) {
            entityImpl = (EntityImpl)this.fDocumentImpl.createEntity(string);
            entityImpl.setPublicId(string2);
            entityImpl.setSystemId(string3);
            entityImpl.setBaseURI(xMLResourceIdentifier.getBaseSystemId());
            namedNodeMap.setNamedItem(entityImpl);
        }
        if (this.fDocumentTypeIndex != -1) {
            int n2;
            boolean bl = false;
            int n3 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
            while (n3 != -1) {
                String string4;
                n2 = this.fDeferredDocumentImpl.getNodeType(n3, false);
                if (n2 == 6 && (string4 = this.fDeferredDocumentImpl.getNodeName(n3, false)).equals(string)) {
                    bl = true;
                    break;
                }
                n3 = this.fDeferredDocumentImpl.getRealPrevSibling(n3, false);
            }
            if (!bl) {
                n2 = this.fDeferredDocumentImpl.createDeferredEntity(string, string2, string3, null, xMLResourceIdentifier.getBaseSystemId());
                this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, n2);
            }
        }
    }

    public void startParameterEntity(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        if (augmentations != null && this.fInternalSubset != null && !this.fInDTDExternalSubset && Boolean.TRUE.equals(augmentations.getItem("ENTITY_SKIPPED"))) {
            this.fInternalSubset.append(string).append(";\n");
        }
        this.fBaseURIStack.push(xMLResourceIdentifier.getExpandedSystemId());
    }

    public void endParameterEntity(String string, Augmentations augmentations) throws XNIException {
        this.fBaseURIStack.pop();
    }

    public void unparsedEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
        NamedNodeMap namedNodeMap;
        EntityImpl entityImpl;
        String string3 = xMLResourceIdentifier.getPublicId();
        String string4 = xMLResourceIdentifier.getLiteralSystemId();
        if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
            this.fInternalSubset.append("<!ENTITY ");
            this.fInternalSubset.append(string);
            this.fInternalSubset.append(' ');
            if (string3 != null) {
                this.fInternalSubset.append("PUBLIC '");
                this.fInternalSubset.append(string3);
                if (string4 != null) {
                    this.fInternalSubset.append("' '");
                    this.fInternalSubset.append(string4);
                }
            } else {
                this.fInternalSubset.append("SYSTEM '");
                this.fInternalSubset.append(string4);
            }
            this.fInternalSubset.append("' NDATA ");
            this.fInternalSubset.append(string2);
            this.fInternalSubset.append(">\n");
        }
        if (this.fDocumentType != null && (entityImpl = (EntityImpl)(namedNodeMap = this.fDocumentType.getEntities()).getNamedItem(string)) == null) {
            entityImpl = (EntityImpl)this.fDocumentImpl.createEntity(string);
            entityImpl.setPublicId(string3);
            entityImpl.setSystemId(string4);
            entityImpl.setNotationName(string2);
            entityImpl.setBaseURI(xMLResourceIdentifier.getBaseSystemId());
            namedNodeMap.setNamedItem(entityImpl);
        }
        if (this.fDocumentTypeIndex != -1) {
            int n2;
            boolean bl = false;
            int n3 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
            while (n3 != -1) {
                String string5;
                n2 = this.fDeferredDocumentImpl.getNodeType(n3, false);
                if (n2 == 6 && (string5 = this.fDeferredDocumentImpl.getNodeName(n3, false)).equals(string)) {
                    bl = true;
                    break;
                }
                n3 = this.fDeferredDocumentImpl.getRealPrevSibling(n3, false);
            }
            if (!bl) {
                n2 = this.fDeferredDocumentImpl.createDeferredEntity(string, string3, string4, string2, xMLResourceIdentifier.getBaseSystemId());
                this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, n2);
            }
        }
    }

    public void notationDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        NamedNodeMap namedNodeMap;
        String string2 = xMLResourceIdentifier.getPublicId();
        String string3 = xMLResourceIdentifier.getLiteralSystemId();
        if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
            this.fInternalSubset.append("<!NOTATION ");
            this.fInternalSubset.append(string);
            if (string2 != null) {
                this.fInternalSubset.append(" PUBLIC '");
                this.fInternalSubset.append(string2);
                if (string3 != null) {
                    this.fInternalSubset.append("' '");
                    this.fInternalSubset.append(string3);
                }
            } else {
                this.fInternalSubset.append(" SYSTEM '");
                this.fInternalSubset.append(string3);
            }
            this.fInternalSubset.append("'>\n");
        }
        if (this.fDocumentImpl != null && this.fDocumentType != null && (namedNodeMap = this.fDocumentType.getNotations()).getNamedItem(string) == null) {
            NotationImpl notationImpl = (NotationImpl)this.fDocumentImpl.createNotation(string);
            notationImpl.setPublicId(string2);
            notationImpl.setSystemId(string3);
            notationImpl.setBaseURI(xMLResourceIdentifier.getBaseSystemId());
            namedNodeMap.setNamedItem(notationImpl);
        }
        if (this.fDocumentTypeIndex != -1) {
            int n2;
            boolean bl = false;
            int n3 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false);
            while (n3 != -1) {
                String string4;
                n2 = this.fDeferredDocumentImpl.getNodeType(n3, false);
                if (n2 == 12 && (string4 = this.fDeferredDocumentImpl.getNodeName(n3, false)).equals(string)) {
                    bl = true;
                    break;
                }
                n3 = this.fDeferredDocumentImpl.getPrevSibling(n3, false);
            }
            if (!bl) {
                n2 = this.fDeferredDocumentImpl.createDeferredNotation(string, string2, string3, xMLResourceIdentifier.getBaseSystemId());
                this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, n2);
            }
        }
    }

    public void ignoredCharacters(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void elementDecl(String string, String string2, Augmentations augmentations) throws XNIException {
        if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
            this.fInternalSubset.append("<!ELEMENT ");
            this.fInternalSubset.append(string);
            this.fInternalSubset.append(' ');
            this.fInternalSubset.append(string2);
            this.fInternalSubset.append(">\n");
        }
    }

    public void attributeDecl(String string, String string2, String string3, String[] arrstring, String string4, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
        int n2;
        char c2;
        if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
            this.fInternalSubset.append("<!ATTLIST ");
            this.fInternalSubset.append(string);
            this.fInternalSubset.append(' ');
            this.fInternalSubset.append(string2);
            this.fInternalSubset.append(' ');
            if (string3.equals("ENUMERATION")) {
                this.fInternalSubset.append('(');
                n2 = 0;
                while (n2 < arrstring.length) {
                    if (n2 > 0) {
                        this.fInternalSubset.append('|');
                    }
                    this.fInternalSubset.append(arrstring[n2]);
                    ++n2;
                }
                this.fInternalSubset.append(')');
            } else {
                this.fInternalSubset.append(string3);
            }
            if (string4 != null) {
                this.fInternalSubset.append(' ');
                this.fInternalSubset.append(string4);
            }
            if (xMLString != null) {
                this.fInternalSubset.append(" '");
                n2 = 0;
                while (n2 < xMLString.length) {
                    c2 = xMLString.ch[xMLString.offset + n2];
                    if (c2 == '\'') {
                        this.fInternalSubset.append("&apos;");
                    } else {
                        this.fInternalSubset.append(c2);
                    }
                    ++n2;
                }
                this.fInternalSubset.append('\'');
            }
            this.fInternalSubset.append(">\n");
        }
        if (this.fDeferredDocumentImpl != null) {
            if (xMLString != null) {
                n2 = this.fDeferredDocumentImpl.lookupElementDefinition(string);
                if (n2 == -1) {
                    n2 = this.fDeferredDocumentImpl.createDeferredElementDefinition(string);
                    this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, n2);
                }
                c2 = this.fNamespaceAware;
                String string5 = null;
                if (c2 != '\u0000') {
                    if (string2.startsWith("xmlns:") || string2.equals("xmlns")) {
                        string5 = NamespaceContext.XMLNS_URI;
                    } else if (string2.startsWith("xml:")) {
                        string5 = NamespaceContext.XML_URI;
                    }
                }
                int n3 = this.fDeferredDocumentImpl.createDeferredAttribute(string2, string5, xMLString.toString(), false);
                if ("ID".equals(string3)) {
                    this.fDeferredDocumentImpl.setIdAttribute(n3);
                }
                this.fDeferredDocumentImpl.appendChild(n2, n3);
            }
        } else if (this.fDocumentImpl != null && xMLString != null) {
            AttrImpl attrImpl;
            boolean bl;
            NamedNodeMap namedNodeMap = ((DocumentTypeImpl)this.fDocumentType).getElements();
            ElementDefinitionImpl elementDefinitionImpl = (ElementDefinitionImpl)namedNodeMap.getNamedItem(string);
            if (elementDefinitionImpl == null) {
                elementDefinitionImpl = this.fDocumentImpl.createElementDefinition(string);
                ((DocumentTypeImpl)this.fDocumentType).getElements().setNamedItem(elementDefinitionImpl);
            }
            if (bl = this.fNamespaceAware) {
                String string6 = null;
                if (string2.startsWith("xmlns:") || string2.equals("xmlns")) {
                    string6 = NamespaceContext.XMLNS_URI;
                } else if (string2.startsWith("xml:")) {
                    string6 = NamespaceContext.XML_URI;
                }
                attrImpl = (AttrImpl)this.fDocumentImpl.createAttributeNS(string6, string2);
            } else {
                attrImpl = (AttrImpl)this.fDocumentImpl.createAttribute(string2);
            }
            attrImpl.setValue(xMLString.toString());
            attrImpl.setSpecified(false);
            attrImpl.setIdAttribute("ID".equals(string3));
            if (bl) {
                elementDefinitionImpl.getAttributes().setNamedItemNS(attrImpl);
            } else {
                elementDefinitionImpl.getAttributes().setNamedItem(attrImpl);
            }
        }
    }

    public void startAttlist(String string, Augmentations augmentations) throws XNIException {
    }

    public void endAttlist(Augmentations augmentations) throws XNIException {
    }

    protected Element createElementNode(QName qName) {
        Element element = null;
        element = this.fNamespaceAware ? (this.fDocumentImpl != null ? this.fDocumentImpl.createElementNS(qName.uri, qName.rawname, qName.localpart) : this.fDocument.createElementNS(qName.uri, qName.rawname)) : this.fDocument.createElement(qName.rawname);
        return element;
    }

    protected Attr createAttrNode(QName qName) {
        Attr attr = null;
        attr = this.fNamespaceAware ? (this.fDocumentImpl != null ? this.fDocumentImpl.createAttributeNS(qName.uri, qName.rawname, qName.localpart) : this.fDocument.createAttributeNS(qName.uri, qName.rawname)) : this.fDocument.createAttribute(qName.rawname);
        return attr;
    }

    protected void setCharacterData(boolean bl) {
        this.fFirstChunk = bl;
        Node node = this.fCurrentNode.getLastChild();
        if (node != null) {
            if (this.fStringBuffer.length() > 0) {
                if (node.getNodeType() == 3) {
                    if (this.fDocumentImpl != null) {
                        ((TextImpl)node).replaceData(this.fStringBuffer.toString());
                    } else {
                        ((Text)node).setData(this.fStringBuffer.toString());
                    }
                }
                this.fStringBuffer.setLength(0);
            }
            if (this.fDOMFilter != null && !this.fInEntityRef && node.getNodeType() == 3 && (this.fDOMFilter.getWhatToShow() & 4) != 0) {
                short s2 = this.fDOMFilter.acceptNode(node);
                switch (s2) {
                    case 4: {
                        throw Abort.INSTANCE;
                    }
                    case 2: 
                    case 3: {
                        this.fCurrentNode.removeChild(node);
                        return;
                    }
                }
            }
        }
    }

    public void abort() {
        throw Abort.INSTANCE;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static final class Abort
    extends RuntimeException {
        private static final long serialVersionUID = 1687848994976808490L;
        static final Abort INSTANCE = new Abort();

        private Abort() {
        }

        public Throwable fillInStackTrace() {
            return this;
        }
    }

}

