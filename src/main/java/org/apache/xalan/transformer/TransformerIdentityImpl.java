/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.transformer.SerializerSwitcher;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serializer.TreeWalker;
import org.apache.xml.utils.DOMBuilder;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLReaderManager;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class TransformerIdentityImpl
extends Transformer
implements TransformerHandler,
DeclHandler {
    boolean m_flushedStartDoc = false;
    private FileOutputStream m_outputStream = null;
    private ContentHandler m_resultContentHandler;
    private LexicalHandler m_resultLexicalHandler;
    private DTDHandler m_resultDTDHandler;
    private DeclHandler m_resultDeclHandler;
    private Serializer m_serializer;
    private Result m_result;
    private String m_systemID;
    private Hashtable m_params;
    private ErrorListener m_errorListener = new DefaultErrorHandler(false);
    URIResolver m_URIResolver;
    private OutputProperties m_outputFormat = new OutputProperties("xml");
    boolean m_foundFirstElement;
    private boolean m_isSecureProcessing = false;

    public TransformerIdentityImpl(boolean bl) {
        this.m_isSecureProcessing = bl;
    }

    public TransformerIdentityImpl() {
        this(false);
    }

    public void setResult(Result result) throws IllegalArgumentException {
        if (null == result) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_RESULT_NULL", null));
        }
        this.m_result = result;
    }

    public void setSystemId(String string) {
        this.m_systemID = string;
    }

    public String getSystemId() {
        return this.m_systemID;
    }

    public Transformer getTransformer() {
        return this;
    }

    public void reset() {
        this.m_flushedStartDoc = false;
        this.m_foundFirstElement = false;
        this.m_outputStream = null;
        this.clearParameters();
        this.m_result = null;
        this.m_resultContentHandler = null;
        this.m_resultDeclHandler = null;
        this.m_resultDTDHandler = null;
        this.m_resultLexicalHandler = null;
        this.m_serializer = null;
        this.m_systemID = null;
        this.m_URIResolver = null;
        this.m_outputFormat = new OutputProperties("xml");
    }

    private void createResultContentHandler(Result result) throws TransformerException {
        if (result instanceof SAXResult) {
            SAXResult sAXResult = (SAXResult)result;
            this.m_resultContentHandler = sAXResult.getHandler();
            this.m_resultLexicalHandler = sAXResult.getLexicalHandler();
            if (this.m_resultContentHandler instanceof Serializer) {
                this.m_serializer = (Serializer)((Object)this.m_resultContentHandler);
            }
        } else if (result instanceof DOMResult) {
            short s2;
            Object object;
            Document document;
            DOMResult dOMResult = (DOMResult)result;
            Node node = dOMResult.getNode();
            Node node2 = dOMResult.getNextSibling();
            if (null != node) {
                s2 = node.getNodeType();
                document = 9 == s2 ? (Document)node : node.getOwnerDocument();
            } else {
                try {
                    object = DocumentBuilderFactory.newInstance();
                    object.setNamespaceAware(true);
                    if (this.m_isSecureProcessing) {
                        try {
                            object.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                        }
                        catch (ParserConfigurationException parserConfigurationException) {
                            // empty catch block
                        }
                    }
                    DocumentBuilder documentBuilder = object.newDocumentBuilder();
                    document = documentBuilder.newDocument();
                }
                catch (ParserConfigurationException parserConfigurationException) {
                    throw new TransformerException(parserConfigurationException);
                }
                node = document;
                s2 = node.getNodeType();
                ((DOMResult)result).setNode(node);
            }
            Object object2 = object = 11 == s2 ? new DOMBuilder(document, (DocumentFragment)node) : new DOMBuilder(document, node);
            if (node2 != null) {
                object.setNextSibling(node2);
            }
            this.m_resultContentHandler = object;
            this.m_resultLexicalHandler = object;
        } else if (result instanceof StreamResult) {
            StreamResult streamResult = (StreamResult)result;
            try {
                Serializer serializer;
                this.m_serializer = serializer = SerializerFactory.getSerializer(this.m_outputFormat.getProperties());
                if (null != streamResult.getWriter()) {
                    serializer.setWriter(streamResult.getWriter());
                } else if (null != streamResult.getOutputStream()) {
                    serializer.setOutputStream(streamResult.getOutputStream());
                } else if (null != streamResult.getSystemId()) {
                    String string = streamResult.getSystemId();
                    if (string.startsWith("file:///")) {
                        string = string.substring(8).indexOf(":") > 0 ? string.substring(8) : string.substring(7);
                    } else if (string.startsWith("file:/")) {
                        string = string.substring(6).indexOf(":") > 0 ? string.substring(6) : string.substring(5);
                    }
                    this.m_outputStream = new FileOutputStream(string);
                    serializer.setOutputStream(this.m_outputStream);
                } else {
                    throw new TransformerException(XSLMessages.createMessage("ER_NO_OUTPUT_SPECIFIED", null));
                }
                this.m_resultContentHandler = serializer.asContentHandler();
            }
            catch (IOException iOException) {
                throw new TransformerException(iOException);
            }
        } else {
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_TRANSFORM_TO_RESULT_TYPE", new Object[]{result.getClass().getName()}));
        }
        if (this.m_resultContentHandler instanceof DTDHandler) {
            this.m_resultDTDHandler = (DTDHandler)((Object)this.m_resultContentHandler);
        }
        if (this.m_resultContentHandler instanceof DeclHandler) {
            this.m_resultDeclHandler = (DeclHandler)((Object)this.m_resultContentHandler);
        }
        if (this.m_resultContentHandler instanceof LexicalHandler) {
            this.m_resultLexicalHandler = (LexicalHandler)((Object)this.m_resultContentHandler);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void transform(Source var1_1, Result var2_2) throws TransformerException {
        this.createResultContentHandler(var2_2);
        if (var1_1 instanceof StreamSource != false && var1_1.getSystemId() == null && ((StreamSource)var1_1).getInputStream() == null && ((StreamSource)var1_1).getReader() == null || var1_1 instanceof SAXSource != false && ((SAXSource)var1_1).getInputSource() == null && ((SAXSource)var1_1).getXMLReader() == null || var1_1 instanceof DOMSource && ((DOMSource)var1_1).getNode() == null) {
            try {
                var3_3 = DocumentBuilderFactory.newInstance();
                var4_5 = var3_3.newDocumentBuilder();
                var5_6 = var1_1.getSystemId();
                var1_1 = new DOMSource(var4_5.newDocument());
                if (var5_6 != null) {
                    var1_1.setSystemId((String)var5_6);
                }
            }
            catch (ParserConfigurationException var3_4) {
                throw new TransformerException(var3_4.getMessage());
            }
        }
        try {
            if (var1_1 instanceof DOMSource) {
                var3_3 = (DOMSource)var1_1;
                this.m_systemID = var3_3.getSystemId();
                var4_5 = var3_3.getNode();
                if (null == var4_5) {
                    var5_6 = XSLMessages.createMessage("ER_ILLEGAL_DOMSOURCE_INPUT", null);
                    throw new IllegalArgumentException((String)var5_6);
                }
                try {
                    if (var4_5.getNodeType() == 2) {
                        this.startDocument();
                    }
                    try {
                        if (var4_5.getNodeType() == 2) {
                            var5_6 = var4_5.getNodeValue();
                            var6_9 = var5_6.toCharArray();
                            this.characters(var6_9, 0, var6_9.length);
                        } else {
                            var5_6 = new TreeWalker(this, this.m_systemID);
                            var5_6.traverse((Node)var4_5);
                        }
                        var8_16 = null;
                        if (var4_5.getNodeType() == 2) {
                            this.endDocument();
                        }
                    }
                    catch (Throwable var7_18) {
                        var8_17 = null;
                        if (var4_5.getNodeType() != 2) throw var7_18;
                        this.endDocument();
                        throw var7_18;
                    }
                }
                catch (SAXException var5_7) {
                    throw new TransformerException(var5_7);
                }
                var12_22 = null;
                if (null == this.m_outputStream) return;
                try {
                    this.m_outputStream.close();
                }
                catch (IOException var13_25) {
                    // empty catch block
                }
                this.m_outputStream = null;
                return;
            }
            var3_3 = SAXSource.sourceToInputSource(var1_1);
            if (null == var3_3) {
                throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_TRANSFORM_SOURCE_TYPE", new Object[]{var1_1.getClass().getName()}));
            }
            if (null != var3_3.getSystemId()) {
                this.m_systemID = var3_3.getSystemId();
            }
            var4_5 = null;
            var5_8 = false;
            try {
                try {
                    if (var1_1 instanceof SAXSource) {
                        var4_5 = ((SAXSource)var1_1).getXMLReader();
                    }
                    if (null == var4_5) {
                        try {
                            var4_5 = XMLReaderManager.getInstance().getXMLReader();
                            var5_8 = true;
                        }
                        catch (SAXException var6_10) {
                            throw new TransformerException(var6_10);
                        }
                    }
                    try {
                        var4_5.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                    }
                    catch (SAXException var6_11) {
                        // empty catch block
                    }
                    var6_12 = this;
                    var4_5.setContentHandler(var6_12);
                    if (var6_12 instanceof DTDHandler) {
                        var4_5.setDTDHandler(var6_12);
                    }
                    try {
                        if (var6_12 instanceof LexicalHandler) {
                            var4_5.setProperty("http://xml.org/sax/properties/lexical-handler", var6_12);
                        }
                        if (var6_12 instanceof DeclHandler) {
                            var4_5.setProperty("http://xml.org/sax/properties/declaration-handler", var6_12);
                        }
                    }
                    catch (SAXException var7_19) {
                        // empty catch block
                    }
                    try {
                        if (var6_12 instanceof LexicalHandler) {
                            var4_5.setProperty("http://xml.org/sax/handlers/LexicalHandler", var6_12);
                        }
                        if (var6_12 instanceof DeclHandler) {
                            var4_5.setProperty("http://xml.org/sax/handlers/DeclHandler", var6_12);
                        }
                    }
                    catch (SAXNotRecognizedException var7_20) {
                        // empty catch block
                    }
                    var4_5.parse((InputSource)var3_3);
                }
                catch (WrappedRuntimeException var6_13) {
                    var7_21 = var6_13.getException();
                    while (var7_21 instanceof WrappedRuntimeException != false) {
                        var7_21 = ((WrappedRuntimeException)var7_21).getException();
                    }
                    throw new TransformerException(var6_13.getException());
                }
                catch (SAXException var6_14) {
                    throw new TransformerException(var6_14);
                }
                catch (IOException var6_15) {
                    throw new TransformerException(var6_15);
                }
                var10_28 = null;
                if (var5_8) {
                    XMLReaderManager.getInstance().releaseXMLReader((XMLReader)var4_5);
                }
            }
            catch (Throwable var9_30) {
                var10_29 = null;
                if (var5_8 == false) throw var9_30;
                XMLReaderManager.getInstance().releaseXMLReader((XMLReader)var4_5);
                throw var9_30;
            }
            var12_23 = null;
            if (null == this.m_outputStream) return;
            ** try [egrp 10[TRYBLOCK] [16 : 693->703)] { 
lbl125: // 1 sources:
            this.m_outputStream.close();
            ** GOTO lbl129
lbl127: // 1 sources:
            catch (IOException var13_26) {
                // empty catch block
            }
lbl129: // 2 sources:
            this.m_outputStream = null;
            return;
        }
        catch (Throwable var11_31) {
            var12_24 = null;
            if (null == this.m_outputStream) throw var11_31;
            ** try [egrp 10[TRYBLOCK] [16 : 693->703)] { 
lbl136: // 1 sources:
            this.m_outputStream.close();
            ** GOTO lbl140
lbl138: // 1 sources:
            catch (IOException var13_27) {
                // empty catch block
            }
lbl140: // 2 sources:
            this.m_outputStream = null;
            throw var11_31;
        }
    }

    public void setParameter(String string, Object object) {
        if (object == null) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_SET_PARAM_VALUE", new Object[]{string}));
        }
        if (null == this.m_params) {
            this.m_params = new Hashtable();
        }
        this.m_params.put(string, object);
    }

    public Object getParameter(String string) {
        if (null == this.m_params) {
            return null;
        }
        return this.m_params.get(string);
    }

    public void clearParameters() {
        if (null == this.m_params) {
            return;
        }
        this.m_params.clear();
    }

    public void setURIResolver(URIResolver uRIResolver) {
        this.m_URIResolver = uRIResolver;
    }

    public URIResolver getURIResolver() {
        return this.m_URIResolver;
    }

    public void setOutputProperties(Properties properties) throws IllegalArgumentException {
        if (null != properties) {
            String string = (String)properties.get("method");
            this.m_outputFormat = null != string ? new OutputProperties(string) : new OutputProperties();
            this.m_outputFormat.copyFrom(properties);
        } else {
            this.m_outputFormat = null;
        }
    }

    public Properties getOutputProperties() {
        return (Properties)this.m_outputFormat.getProperties().clone();
    }

    public void setOutputProperty(String string, String string2) throws IllegalArgumentException {
        if (!OutputProperties.isLegalPropertyKey(string)) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{string}));
        }
        this.m_outputFormat.setProperty(string, string2);
    }

    public String getOutputProperty(String string) throws IllegalArgumentException {
        String string2 = null;
        OutputProperties outputProperties = this.m_outputFormat;
        string2 = outputProperties.getProperty(string);
        if (null == string2 && !OutputProperties.isLegalPropertyKey(string)) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{string}));
        }
        return string2;
    }

    public void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException {
        if (errorListener == null) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_NULL_ERROR_HANDLER", null));
        }
        this.m_errorListener = errorListener;
    }

    public ErrorListener getErrorListener() {
        return this.m_errorListener;
    }

    public void notationDecl(String string, String string2, String string3) throws SAXException {
        if (null != this.m_resultDTDHandler) {
            this.m_resultDTDHandler.notationDecl(string, string2, string3);
        }
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) throws SAXException {
        if (null != this.m_resultDTDHandler) {
            this.m_resultDTDHandler.unparsedEntityDecl(string, string2, string3, string4);
        }
    }

    public void setDocumentLocator(Locator locator) {
        try {
            if (null == this.m_resultContentHandler) {
                this.createResultContentHandler(this.m_result);
            }
        }
        catch (TransformerException transformerException) {
            throw new WrappedRuntimeException(transformerException);
        }
        this.m_resultContentHandler.setDocumentLocator(locator);
    }

    public void startDocument() throws SAXException {
        try {
            if (null == this.m_resultContentHandler) {
                this.createResultContentHandler(this.m_result);
            }
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException.getMessage(), transformerException);
        }
        this.m_flushedStartDoc = false;
        this.m_foundFirstElement = false;
    }

    protected final void flushStartDoc() throws SAXException {
        if (!this.m_flushedStartDoc) {
            if (this.m_resultContentHandler == null) {
                try {
                    this.createResultContentHandler(this.m_result);
                }
                catch (TransformerException transformerException) {
                    throw new SAXException(transformerException);
                }
            }
            this.m_resultContentHandler.startDocument();
            this.m_flushedStartDoc = true;
        }
    }

    public void endDocument() throws SAXException {
        this.flushStartDoc();
        this.m_resultContentHandler.endDocument();
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        this.flushStartDoc();
        this.m_resultContentHandler.startPrefixMapping(string, string2);
    }

    public void endPrefixMapping(String string) throws SAXException {
        this.flushStartDoc();
        this.m_resultContentHandler.endPrefixMapping(string);
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        if (!this.m_foundFirstElement && null != this.m_serializer) {
            Serializer serializer;
            this.m_foundFirstElement = true;
            try {
                serializer = SerializerSwitcher.switchSerializerIfHTML(string, string2, this.m_outputFormat.getProperties(), this.m_serializer);
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
            if (serializer != this.m_serializer) {
                try {
                    this.m_resultContentHandler = serializer.asContentHandler();
                }
                catch (IOException iOException) {
                    throw new SAXException(iOException);
                }
                if (this.m_resultContentHandler instanceof DTDHandler) {
                    this.m_resultDTDHandler = (DTDHandler)((Object)this.m_resultContentHandler);
                }
                if (this.m_resultContentHandler instanceof LexicalHandler) {
                    this.m_resultLexicalHandler = (LexicalHandler)((Object)this.m_resultContentHandler);
                }
                this.m_serializer = serializer;
            }
        }
        this.flushStartDoc();
        this.m_resultContentHandler.startElement(string, string2, string3, attributes);
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        this.m_resultContentHandler.endElement(string, string2, string3);
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        this.flushStartDoc();
        this.m_resultContentHandler.characters(arrc, n2, n3);
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        this.m_resultContentHandler.ignorableWhitespace(arrc, n2, n3);
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this.flushStartDoc();
        this.m_resultContentHandler.processingInstruction(string, string2);
    }

    public void skippedEntity(String string) throws SAXException {
        this.flushStartDoc();
        this.m_resultContentHandler.skippedEntity(string);
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        this.flushStartDoc();
        if (null != this.m_resultLexicalHandler) {
            this.m_resultLexicalHandler.startDTD(string, string2, string3);
        }
    }

    public void endDTD() throws SAXException {
        if (null != this.m_resultLexicalHandler) {
            this.m_resultLexicalHandler.endDTD();
        }
    }

    public void startEntity(String string) throws SAXException {
        if (null != this.m_resultLexicalHandler) {
            this.m_resultLexicalHandler.startEntity(string);
        }
    }

    public void endEntity(String string) throws SAXException {
        if (null != this.m_resultLexicalHandler) {
            this.m_resultLexicalHandler.endEntity(string);
        }
    }

    public void startCDATA() throws SAXException {
        if (null != this.m_resultLexicalHandler) {
            this.m_resultLexicalHandler.startCDATA();
        }
    }

    public void endCDATA() throws SAXException {
        if (null != this.m_resultLexicalHandler) {
            this.m_resultLexicalHandler.endCDATA();
        }
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        this.flushStartDoc();
        if (null != this.m_resultLexicalHandler) {
            this.m_resultLexicalHandler.comment(arrc, n2, n3);
        }
    }

    public void elementDecl(String string, String string2) throws SAXException {
        if (null != this.m_resultDeclHandler) {
            this.m_resultDeclHandler.elementDecl(string, string2);
        }
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
        if (null != this.m_resultDeclHandler) {
            this.m_resultDeclHandler.attributeDecl(string, string2, string3, string4, string5);
        }
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
        if (null != this.m_resultDeclHandler) {
            this.m_resultDeclHandler.internalEntityDecl(string, string2);
        }
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
        if (null != this.m_resultDeclHandler) {
            this.m_resultDeclHandler.externalEntityDecl(string, string2, string3);
        }
    }
}

