/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMLocatorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DOMNormalizer;
import org.apache.xerces.dom.DOMStringListImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XML11Serializer;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSSerializerFilter;

public class DOMSerializerImpl
implements DOMConfiguration,
LSSerializer {
    private XMLSerializer serializer;
    private XML11Serializer xml11Serializer;
    private DOMStringList fRecognizedParameters;
    protected short features = 0;
    private DOMErrorHandler fErrorHandler = null;
    private final DOMErrorImpl fError = new DOMErrorImpl();
    private final DOMLocatorImpl fLocator = new DOMLocatorImpl();

    public DOMSerializerImpl() {
        this.features = (short)(this.features | 1);
        this.features = (short)(this.features | 4);
        this.features = (short)(this.features | 32);
        this.features = (short)(this.features | 8);
        this.features = (short)(this.features | 16);
        this.features = (short)(this.features | 2);
        this.features = (short)(this.features | 512);
        this.features = (short)(this.features | 1024);
        this.features = (short)(this.features | 64);
        this.features = (short)(this.features | 256);
        this.serializer = new XMLSerializer();
        this.initSerializer(this.serializer);
    }

    public DOMConfiguration getDomConfig() {
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setParameter(String string, Object object) throws DOMException {
        if (object instanceof Boolean) {
            boolean bl = (Boolean)object;
            if (string.equalsIgnoreCase("infoset")) {
                if (!bl) return;
                this.features = (short)(this.features & -5);
                this.features = (short)(this.features & -9);
                this.features = (short)(this.features | 1);
                this.features = (short)(this.features | 512);
                this.features = (short)(this.features | 2);
                this.features = (short)(this.features | 32);
                return;
            }
            if (string.equalsIgnoreCase("xml-declaration")) {
                this.features = bl ? (short)(this.features | 256) : (short)(this.features & -257);
                return;
            }
            if (string.equalsIgnoreCase("namespaces")) {
                this.features = bl ? (short)(this.features | 1) : (short)(this.features & -2);
                this.serializer.fNamespaces = bl;
                return;
            }
            if (string.equalsIgnoreCase("split-cdata-sections")) {
                this.features = bl ? (short)(this.features | 16) : (short)(this.features & -17);
                return;
            }
            if (string.equalsIgnoreCase("discard-default-content")) {
                this.features = bl ? (short)(this.features | 64) : (short)(this.features & -65);
                return;
            }
            if (string.equalsIgnoreCase("well-formed")) {
                this.features = bl ? (short)(this.features | 2) : (short)(this.features & -3);
                return;
            }
            if (string.equalsIgnoreCase("entities")) {
                this.features = bl ? (short)(this.features | 4) : (short)(this.features & -5);
                return;
            }
            if (string.equalsIgnoreCase("cdata-sections")) {
                this.features = bl ? (short)(this.features | 8) : (short)(this.features & -9);
                return;
            }
            if (string.equalsIgnoreCase("comments")) {
                this.features = bl ? (short)(this.features | 32) : (short)(this.features & -33);
                return;
            }
            if (string.equalsIgnoreCase("format-pretty-print")) {
                this.features = bl ? (short)(this.features | 2048) : (short)(this.features & -2049);
                return;
            }
            if (string.equalsIgnoreCase("canonical-form") || string.equalsIgnoreCase("validate-if-schema") || string.equalsIgnoreCase("validate") || string.equalsIgnoreCase("check-character-normalization") || string.equalsIgnoreCase("datatype-normalization") || string.equalsIgnoreCase("normalize-characters")) {
                if (!bl) return;
                String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{string});
                throw new DOMException(9, string2);
            }
            if (string.equalsIgnoreCase("namespace-declarations")) {
                this.features = bl ? (short)(this.features | 512) : (short)(this.features & -513);
                this.serializer.fNamespacePrefixes = bl;
                return;
            }
            if (!string.equalsIgnoreCase("element-content-whitespace") && !string.equalsIgnoreCase("ignore-unknown-character-denormalizations")) {
                String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{string});
                throw new DOMException(9, string3);
            }
            if (bl) return;
            String string4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{string});
            throw new DOMException(9, string4);
        }
        if (string.equalsIgnoreCase("error-handler")) {
            if (object != null && !(object instanceof DOMErrorHandler)) {
                String string5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{string});
                throw new DOMException(17, string5);
            }
            this.fErrorHandler = (DOMErrorHandler)object;
            return;
        }
        if (string.equalsIgnoreCase("resource-resolver") || string.equalsIgnoreCase("schema-location") || string.equalsIgnoreCase("schema-type") && object != null) {
            String string6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{string});
            throw new DOMException(9, string6);
        }
        String string7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{string});
        throw new DOMException(8, string7);
    }

    public boolean canSetParameter(String string, Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof Boolean) {
            boolean bl = (Boolean)object;
            if (string.equalsIgnoreCase("namespaces") || string.equalsIgnoreCase("split-cdata-sections") || string.equalsIgnoreCase("discard-default-content") || string.equalsIgnoreCase("xml-declaration") || string.equalsIgnoreCase("well-formed") || string.equalsIgnoreCase("infoset") || string.equalsIgnoreCase("entities") || string.equalsIgnoreCase("cdata-sections") || string.equalsIgnoreCase("comments") || string.equalsIgnoreCase("format-pretty-print") || string.equalsIgnoreCase("namespace-declarations")) {
                return true;
            }
            if (string.equalsIgnoreCase("canonical-form") || string.equalsIgnoreCase("validate-if-schema") || string.equalsIgnoreCase("validate") || string.equalsIgnoreCase("check-character-normalization") || string.equalsIgnoreCase("datatype-normalization") || string.equalsIgnoreCase("normalize-characters")) {
                return !bl;
            }
            if (string.equalsIgnoreCase("element-content-whitespace") || string.equalsIgnoreCase("ignore-unknown-character-denormalizations")) {
                return bl;
            }
        } else if (string.equalsIgnoreCase("error-handler") && object == null || object instanceof DOMErrorHandler) {
            return true;
        }
        return false;
    }

    public DOMStringList getParameterNames() {
        if (this.fRecognizedParameters == null) {
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("namespaces");
            arrayList.add("split-cdata-sections");
            arrayList.add("discard-default-content");
            arrayList.add("xml-declaration");
            arrayList.add("canonical-form");
            arrayList.add("validate-if-schema");
            arrayList.add("validate");
            arrayList.add("check-character-normalization");
            arrayList.add("datatype-normalization");
            arrayList.add("format-pretty-print");
            arrayList.add("normalize-characters");
            arrayList.add("well-formed");
            arrayList.add("infoset");
            arrayList.add("namespace-declarations");
            arrayList.add("element-content-whitespace");
            arrayList.add("entities");
            arrayList.add("cdata-sections");
            arrayList.add("comments");
            arrayList.add("ignore-unknown-character-denormalizations");
            arrayList.add("error-handler");
            this.fRecognizedParameters = new DOMStringListImpl(arrayList);
        }
        return this.fRecognizedParameters;
    }

    public Object getParameter(String string) throws DOMException {
        if (string.equalsIgnoreCase("comments")) {
            return (this.features & 32) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("namespaces")) {
            return (this.features & 1) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("xml-declaration")) {
            return (this.features & 256) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("cdata-sections")) {
            return (this.features & 8) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("entities")) {
            return (this.features & 4) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("split-cdata-sections")) {
            return (this.features & 16) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("well-formed")) {
            return (this.features & 2) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("namespace-declarations")) {
            return (this.features & 512) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("element-content-whitespace") || string.equalsIgnoreCase("ignore-unknown-character-denormalizations")) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase("discard-default-content")) {
            return (this.features & 64) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("format-pretty-print")) {
            return (this.features & 2048) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("infoset")) {
            if ((this.features & 4) == 0 && (this.features & 8) == 0 && (this.features & 1) != 0 && (this.features & 512) != 0 && (this.features & 2) != 0 && (this.features & 32) != 0) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("normalize-characters") || string.equalsIgnoreCase("canonical-form") || string.equalsIgnoreCase("validate-if-schema") || string.equalsIgnoreCase("check-character-normalization") || string.equalsIgnoreCase("validate") || string.equalsIgnoreCase("validate-if-schema") || string.equalsIgnoreCase("datatype-normalization")) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("error-handler")) {
            return this.fErrorHandler;
        }
        if (string.equalsIgnoreCase("resource-resolver") || string.equalsIgnoreCase("schema-location") || string.equalsIgnoreCase("schema-type")) {
            String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{string});
            throw new DOMException(9, string2);
        }
        String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{string});
        throw new DOMException(8, string3);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String writeToString(Node node) throws DOMException, LSException {
        XMLSerializer xMLSerializer = null;
        String string = this._getXmlVersion(node);
        if (string != null && string.equals("1.1")) {
            if (this.xml11Serializer == null) {
                this.xml11Serializer = new XML11Serializer();
                this.initSerializer(this.xml11Serializer);
            }
            this.copySettings(this.serializer, this.xml11Serializer);
            xMLSerializer = this.xml11Serializer;
        } else {
            xMLSerializer = this.serializer;
        }
        StringWriter stringWriter = new StringWriter();
        try {
            try {
                this.prepareForSerialization(xMLSerializer, node);
                xMLSerializer._format.setEncoding("UTF-16");
                xMLSerializer.setOutputCharStream(stringWriter);
                if (node.getNodeType() == 9) {
                    xMLSerializer.serialize((Document)node);
                } else if (node.getNodeType() == 11) {
                    xMLSerializer.serialize((DocumentFragment)node);
                } else {
                    if (node.getNodeType() != 1) {
                        String string2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "unable-to-serialize-node", null);
                        if (xMLSerializer.fDOMErrorHandler == null) throw new LSException(82, string2);
                        DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                        dOMErrorImpl.fType = "unable-to-serialize-node";
                        dOMErrorImpl.fMessage = string2;
                        dOMErrorImpl.fSeverity = 3;
                        xMLSerializer.fDOMErrorHandler.handleError(dOMErrorImpl);
                        throw new LSException(82, string2);
                    }
                    xMLSerializer.serialize((Element)node);
                }
                Object var10_9 = null;
            }
            catch (LSException lSException) {
                throw lSException;
            }
            catch (RuntimeException runtimeException) {
                if (runtimeException != DOMNormalizer.abort) throw (LSException)DOMUtil.createLSException(82, runtimeException).fillInStackTrace();
                String string3 = null;
                Object var10_10 = null;
                xMLSerializer.clearDocumentState();
                return string3;
            }
            catch (IOException iOException) {
                String string4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "STRING_TOO_LONG", new Object[]{iOException.getMessage()});
                throw new DOMException(2, string4);
            }
            xMLSerializer.clearDocumentState();
            return stringWriter.toString();
        }
        catch (Throwable throwable) {
            Object var10_11 = null;
            xMLSerializer.clearDocumentState();
            throw throwable;
        }
    }

    public void setNewLine(String string) {
        this.serializer._format.setLineSeparator(string);
    }

    public String getNewLine() {
        return this.serializer._format.getLineSeparator();
    }

    public LSSerializerFilter getFilter() {
        return this.serializer.fDOMFilter;
    }

    public void setFilter(LSSerializerFilter lSSerializerFilter) {
        this.serializer.fDOMFilter = lSSerializerFilter;
    }

    private void initSerializer(XMLSerializer xMLSerializer) {
        xMLSerializer.fNSBinder = new NamespaceSupport();
        xMLSerializer.fLocalNSBinder = new NamespaceSupport();
        xMLSerializer.fSymbolTable = new SymbolTable();
    }

    private void copySettings(XMLSerializer xMLSerializer, XMLSerializer xMLSerializer2) {
        xMLSerializer2.fDOMErrorHandler = this.fErrorHandler;
        xMLSerializer2._format.setEncoding(xMLSerializer._format.getEncoding());
        xMLSerializer2._format.setLineSeparator(xMLSerializer._format.getLineSeparator());
        xMLSerializer2.fDOMFilter = xMLSerializer.fDOMFilter;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean write(Node node, LSOutput lSOutput) throws LSException {
        if (node == null) {
            return false;
        }
        XMLSerializer xMLSerializer = null;
        String string = this._getXmlVersion(node);
        if (string != null && string.equals("1.1")) {
            if (this.xml11Serializer == null) {
                this.xml11Serializer = new XML11Serializer();
                this.initSerializer(this.xml11Serializer);
            }
            this.copySettings(this.serializer, this.xml11Serializer);
            xMLSerializer = this.xml11Serializer;
        } else {
            xMLSerializer = this.serializer;
        }
        String string2 = null;
        string2 = lSOutput.getEncoding();
        if (string2 == null && (string2 = this._getInputEncoding(node)) == null && (string2 = this._getXmlEncoding(node)) == null) {
            string2 = "UTF-8";
        }
        try {
            try {
                this.prepareForSerialization(xMLSerializer, node);
                xMLSerializer._format.setEncoding(string2);
                OutputStream outputStream = lSOutput.getByteStream();
                Writer writer = lSOutput.getCharacterStream();
                String string3 = lSOutput.getSystemId();
                if (writer == null) {
                    if (outputStream == null) {
                        if (string3 == null) {
                            String string4 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "no-output-specified", null);
                            if (xMLSerializer.fDOMErrorHandler == null) throw new LSException(82, string4);
                            DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                            dOMErrorImpl.fType = "no-output-specified";
                            dOMErrorImpl.fMessage = string4;
                            dOMErrorImpl.fSeverity = 3;
                            xMLSerializer.fDOMErrorHandler.handleError(dOMErrorImpl);
                            throw new LSException(82, string4);
                        }
                        xMLSerializer.setOutputByteStream(XMLEntityManager.createOutputStream(string3));
                    } else {
                        xMLSerializer.setOutputByteStream(outputStream);
                    }
                } else {
                    xMLSerializer.setOutputCharStream(writer);
                }
                if (node.getNodeType() == 9) {
                    xMLSerializer.serialize((Document)node);
                } else if (node.getNodeType() == 11) {
                    xMLSerializer.serialize((DocumentFragment)node);
                } else if (node.getNodeType() == 1) {
                    xMLSerializer.serialize((Element)node);
                } else {
                    boolean bl = false;
                    Object var12_19 = null;
                    xMLSerializer.clearDocumentState();
                    return bl;
                }
                Object var12_20 = null;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                if (xMLSerializer.fDOMErrorHandler == null) throw new LSException(82, DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "unsupported-encoding", null));
                DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                dOMErrorImpl.fException = unsupportedEncodingException;
                dOMErrorImpl.fType = "unsupported-encoding";
                dOMErrorImpl.fMessage = unsupportedEncodingException.getMessage();
                dOMErrorImpl.fSeverity = 3;
                xMLSerializer.fDOMErrorHandler.handleError(dOMErrorImpl);
                throw new LSException(82, DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "unsupported-encoding", null));
            }
            catch (LSException lSException) {
                throw lSException;
            }
            catch (RuntimeException runtimeException) {
                if (runtimeException != DOMNormalizer.abort) throw (LSException)DOMUtil.createLSException(82, runtimeException).fillInStackTrace();
                boolean bl = false;
                Object var12_21 = null;
                xMLSerializer.clearDocumentState();
                return bl;
            }
            catch (Exception exception) {
                if (xMLSerializer.fDOMErrorHandler == null) throw (LSException)DOMUtil.createLSException(82, exception).fillInStackTrace();
                DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                dOMErrorImpl.fException = exception;
                dOMErrorImpl.fMessage = exception.getMessage();
                dOMErrorImpl.fSeverity = 2;
                xMLSerializer.fDOMErrorHandler.handleError(dOMErrorImpl);
                throw (LSException)DOMUtil.createLSException(82, exception).fillInStackTrace();
            }
            xMLSerializer.clearDocumentState();
            return true;
        }
        catch (Throwable throwable) {
            Object var12_22 = null;
            xMLSerializer.clearDocumentState();
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean writeToURI(Node node, String string) throws LSException {
        if (node == null) {
            return false;
        }
        XMLSerializer xMLSerializer = null;
        String string2 = this._getXmlVersion(node);
        if (string2 != null && string2.equals("1.1")) {
            if (this.xml11Serializer == null) {
                this.xml11Serializer = new XML11Serializer();
                this.initSerializer(this.xml11Serializer);
            }
            this.copySettings(this.serializer, this.xml11Serializer);
            xMLSerializer = this.xml11Serializer;
        } else {
            xMLSerializer = this.serializer;
        }
        String string3 = this._getInputEncoding(node);
        if (string3 == null && (string3 = this._getXmlEncoding(node)) == null) {
            string3 = "UTF-8";
        }
        try {
            try {
                this.prepareForSerialization(xMLSerializer, node);
                xMLSerializer._format.setEncoding(string3);
                xMLSerializer.setOutputByteStream(XMLEntityManager.createOutputStream(string));
                if (node.getNodeType() == 9) {
                    xMLSerializer.serialize((Document)node);
                } else if (node.getNodeType() == 11) {
                    xMLSerializer.serialize((DocumentFragment)node);
                } else if (node.getNodeType() == 1) {
                    xMLSerializer.serialize((Element)node);
                } else {
                    boolean bl = false;
                    Object var11_8 = null;
                    xMLSerializer.clearDocumentState();
                    return bl;
                }
                Object var11_9 = null;
            }
            catch (LSException lSException) {
                throw lSException;
            }
            catch (RuntimeException runtimeException) {
                if (runtimeException != DOMNormalizer.abort) throw (LSException)DOMUtil.createLSException(82, runtimeException).fillInStackTrace();
                boolean bl = false;
                Object var11_10 = null;
                xMLSerializer.clearDocumentState();
                return bl;
            }
            catch (Exception exception) {
                if (xMLSerializer.fDOMErrorHandler == null) throw (LSException)DOMUtil.createLSException(82, exception).fillInStackTrace();
                DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                dOMErrorImpl.fException = exception;
                dOMErrorImpl.fMessage = exception.getMessage();
                dOMErrorImpl.fSeverity = 2;
                xMLSerializer.fDOMErrorHandler.handleError(dOMErrorImpl);
                throw (LSException)DOMUtil.createLSException(82, exception).fillInStackTrace();
            }
            xMLSerializer.clearDocumentState();
            return true;
        }
        catch (Throwable throwable) {
            Object var11_11 = null;
            xMLSerializer.clearDocumentState();
            throw throwable;
        }
    }

    private void prepareForSerialization(XMLSerializer xMLSerializer, Node node) {
        xMLSerializer.reset();
        xMLSerializer.features = this.features;
        xMLSerializer.fDOMErrorHandler = this.fErrorHandler;
        xMLSerializer.fNamespaces = (this.features & 1) != 0;
        xMLSerializer.fNamespacePrefixes = (this.features & 512) != 0;
        xMLSerializer._format.setIndenting((this.features & 2048) != 0);
        xMLSerializer._format.setOmitComments((this.features & 32) == 0);
        xMLSerializer._format.setOmitXMLDeclaration((this.features & 256) == 0);
        if ((this.features & 2) != 0) {
            Node node2 = node;
            boolean bl = true;
            Document document = node.getNodeType() == 9 ? (Document)node : node.getOwnerDocument();
            try {
                Method method = document.getClass().getMethod("isXMLVersionChanged()", new Class[0]);
                if (method != null) {
                    bl = (Boolean)method.invoke(document, null);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (node.getFirstChild() != null) {
                while (node != null) {
                    this.verify(node, bl, false);
                    Node node3 = node.getFirstChild();
                    while (node3 == null) {
                        node3 = node.getNextSibling();
                        if (node3 != null) continue;
                        if (node2 == (node = node.getParentNode())) {
                            node3 = null;
                            break;
                        }
                        node3 = node.getNextSibling();
                    }
                    node = node3;
                }
            } else {
                this.verify(node, bl, false);
            }
        }
    }

    private void verify(Node node, boolean bl, boolean bl2) {
        short s2 = node.getNodeType();
        this.fLocator.fRelatedNode = node;
        switch (s2) {
            case 9: {
                break;
            }
            case 10: {
                break;
            }
            case 1: {
                Object object;
                boolean bl3;
                if (bl) {
                    bl3 = (this.features & 1) != 0 ? CoreDocumentImpl.isValidQName(node.getPrefix(), node.getLocalName(), bl2) : CoreDocumentImpl.isXMLName(node.getNodeName(), bl2);
                    if (!bl3 && this.fErrorHandler != null) {
                        object = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Element", node.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, (String)object, 3, "wf-invalid-character-in-node-name");
                    }
                }
                Object object2 = object = node.hasAttributes() ? node.getAttributes() : null;
                if (object == null) break;
                int n2 = 0;
                while (n2 < object.getLength()) {
                    Attr attr = (Attr)object.item(n2);
                    this.fLocator.fRelatedNode = attr;
                    DOMNormalizer.isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, (NamedNodeMap)object, attr, attr.getValue(), bl2);
                    if (bl && !(bl3 = CoreDocumentImpl.isXMLName(attr.getNodeName(), bl2))) {
                        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Attr", node.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string, 3, "wf-invalid-character-in-node-name");
                    }
                    ++n2;
                }
                break;
            }
            case 8: {
                if ((this.features & 32) == 0) break;
                DOMNormalizer.isCommentWF(this.fErrorHandler, this.fError, this.fLocator, ((Comment)node).getData(), bl2);
                break;
            }
            case 5: {
                if (!bl || (this.features & 4) == 0) break;
                CoreDocumentImpl.isXMLName(node.getNodeName(), bl2);
                break;
            }
            case 4: {
                DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), bl2);
                break;
            }
            case 3: {
                DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), bl2);
                break;
            }
            case 7: {
                ProcessingInstruction processingInstruction = (ProcessingInstruction)node;
                String string = processingInstruction.getTarget();
                if (bl) {
                    boolean bl4 = bl2 ? XML11Char.isXML11ValidName(string) : XMLChar.isValidName(string);
                    if (!bl4) {
                        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Element", node.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, string2, 3, "wf-invalid-character-in-node-name");
                    }
                }
                DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, processingInstruction.getData(), bl2);
            }
        }
        this.fLocator.fRelatedNode = null;
    }

    private String _getXmlVersion(Node node) {
        Document document;
        Document document2 = document = node.getNodeType() == 9 ? (Document)node : node.getOwnerDocument();
        if (document != null && DocumentMethods.access$000()) {
            try {
                return (String)DocumentMethods.access$100().invoke(document, null);
            }
            catch (VirtualMachineError virtualMachineError) {
                throw virtualMachineError;
            }
            catch (ThreadDeath threadDeath) {
                throw threadDeath;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    private String _getInputEncoding(Node node) {
        Document document;
        Document document2 = document = node.getNodeType() == 9 ? (Document)node : node.getOwnerDocument();
        if (document != null && DocumentMethods.access$000()) {
            try {
                return (String)DocumentMethods.access$200().invoke(document, null);
            }
            catch (VirtualMachineError virtualMachineError) {
                throw virtualMachineError;
            }
            catch (ThreadDeath threadDeath) {
                throw threadDeath;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    private String _getXmlEncoding(Node node) {
        Document document;
        Document document2 = document = node.getNodeType() == 9 ? (Document)node : node.getOwnerDocument();
        if (document != null && DocumentMethods.access$000()) {
            try {
                return (String)DocumentMethods.access$300().invoke(document, null);
            }
            catch (VirtualMachineError virtualMachineError) {
                throw virtualMachineError;
            }
            catch (ThreadDeath threadDeath) {
                throw threadDeath;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    static class DocumentMethods {
        private static Method fgDocumentGetXmlVersionMethod = null;
        private static Method fgDocumentGetInputEncodingMethod = null;
        private static Method fgDocumentGetXmlEncodingMethod = null;
        private static boolean fgDocumentMethodsAvailable = false;
        static Class class$org$w3c$dom$Document;

        static boolean access$000() {
            return fgDocumentMethodsAvailable;
        }

        static Method access$100() {
            return fgDocumentGetXmlVersionMethod;
        }

        static Method access$200() {
            return fgDocumentGetInputEncodingMethod;
        }

        static Method access$300() {
            return fgDocumentGetXmlEncodingMethod;
        }

        static Class class$(String string) {
            try {
                return Class.forName(string);
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }

        static {
            try {
                Class class_ = class$org$w3c$dom$Document == null ? (DocumentMethods.class$org$w3c$dom$Document = DocumentMethods.class$("org.w3c.dom.Document")) : class$org$w3c$dom$Document;
                fgDocumentGetXmlVersionMethod = class_.getMethod("getXmlVersion", new Class[0]);
                fgDocumentGetInputEncodingMethod = (class$org$w3c$dom$Document == null ? (DocumentMethods.class$org$w3c$dom$Document = DocumentMethods.class$("org.w3c.dom.Document")) : class$org$w3c$dom$Document).getMethod("getInputEncoding", new Class[0]);
                fgDocumentGetXmlEncodingMethod = (class$org$w3c$dom$Document == null ? (DocumentMethods.class$org$w3c$dom$Document = DocumentMethods.class$("org.w3c.dom.Document")) : class$org$w3c$dom$Document).getMethod("getXmlEncoding", new Class[0]);
                fgDocumentMethodsAvailable = true;
            }
            catch (Exception exception) {
                fgDocumentGetXmlVersionMethod = null;
                fgDocumentGetInputEncodingMethod = null;
                fgDocumentGetXmlEncodingMethod = null;
                fgDocumentMethodsAvailable = false;
            }
        }
    }

}

