/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
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
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xalan.xsltc.trax.DOM2TO;
import org.apache.xalan.xsltc.trax.TransformerFactoryImpl;
import org.apache.xalan.xsltc.trax.XSLTCSource;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.XMLReaderManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

public final class TransformerImpl
extends Transformer
implements ErrorListener,
DOMCache {
    private static final String EMPTY_STRING = "";
    private static final String NO_STRING = "no";
    private static final String YES_STRING = "yes";
    private static final String XML_STRING = "xml";
    private static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
    private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";
    private AbstractTranslet _translet = null;
    private String _method = null;
    private String _encoding = null;
    private String _sourceSystemId = null;
    private ErrorListener _errorListener;
    private URIResolver _uriResolver;
    private Properties _properties;
    private Properties _propertiesClone;
    private TransletOutputHandlerFactory _tohFactory;
    private DOM _dom;
    private int _indentNumber;
    private TransformerFactoryImpl _tfactory;
    private OutputStream _ostream;
    private XSLTCDTMManager _dtmManager;
    private XMLReaderManager _readerManager;
    private boolean _isIdentity;
    private boolean _isSecureProcessing;
    private Hashtable _parameters;

    protected TransformerImpl(Properties properties, int n2, TransformerFactoryImpl transformerFactoryImpl) {
        this(null, properties, n2, transformerFactoryImpl);
        this._isIdentity = true;
    }

    protected TransformerImpl(Translet translet, Properties properties, int n2, TransformerFactoryImpl transformerFactoryImpl) {
        this._errorListener = this;
        this._uriResolver = null;
        this._tohFactory = null;
        this._dom = null;
        this._tfactory = null;
        this._ostream = null;
        this._dtmManager = null;
        this._readerManager = XMLReaderManager.getInstance();
        this._isIdentity = false;
        this._isSecureProcessing = false;
        this._parameters = null;
        this._translet = (AbstractTranslet)translet;
        this._properties = this.createOutputProperties(properties);
        this._propertiesClone = (Properties)this._properties.clone();
        this._indentNumber = n2;
        this._tfactory = transformerFactoryImpl;
    }

    public boolean isSecureProcessing() {
        return this._isSecureProcessing;
    }

    public void setSecureProcessing(boolean bl) {
        this._isSecureProcessing = bl;
    }

    protected AbstractTranslet getTranslet() {
        return this._translet;
    }

    public boolean isIdentity() {
        return this._isIdentity;
    }

    public void transform(Source source, Result result) throws TransformerException {
        SerializationHandler serializationHandler;
        if (!this._isIdentity) {
            if (this._translet == null) {
                ErrorMsg errorMsg = new ErrorMsg("JAXP_NO_TRANSLET_ERR");
                throw new TransformerException(errorMsg.toString());
            }
            this.transferOutputProperties(this._translet);
        }
        if ((serializationHandler = this.getOutputHandler(result)) == null) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_NO_HANDLER_ERR");
            throw new TransformerException(errorMsg.toString());
        }
        if (this._uriResolver != null && !this._isIdentity) {
            this._translet.setDOMCache(this);
        }
        if (this._isIdentity) {
            this.transferOutputProperties(serializationHandler);
        }
        this.transform(source, serializationHandler, this._encoding);
        if (result instanceof DOMResult) {
            ((DOMResult)result).setNode(this._tohFactory.getNode());
        }
    }

    public SerializationHandler getOutputHandler(Result result) throws TransformerException {
        this._method = (String)this._properties.get("method");
        this._encoding = this._properties.getProperty("encoding");
        this._tohFactory = TransletOutputHandlerFactory.newInstance();
        this._tohFactory.setEncoding(this._encoding);
        if (this._method != null) {
            this._tohFactory.setOutputMethod(this._method);
        }
        if (this._indentNumber >= 0) {
            this._tohFactory.setIndentNumber(this._indentNumber);
        }
        try {
            if (result instanceof SAXResult) {
                SAXResult sAXResult = (SAXResult)result;
                ContentHandler contentHandler = sAXResult.getHandler();
                this._tohFactory.setHandler(contentHandler);
                LexicalHandler lexicalHandler = sAXResult.getLexicalHandler();
                if (lexicalHandler != null) {
                    this._tohFactory.setLexicalHandler(lexicalHandler);
                }
                this._tohFactory.setOutputType(1);
                return this._tohFactory.getSerializationHandler();
            }
            if (result instanceof DOMResult) {
                this._tohFactory.setNode(((DOMResult)result).getNode());
                this._tohFactory.setNextSibling(((DOMResult)result).getNextSibling());
                this._tohFactory.setOutputType(2);
                return this._tohFactory.getSerializationHandler();
            }
            if (result instanceof StreamResult) {
                StreamResult streamResult = (StreamResult)result;
                this._tohFactory.setOutputType(0);
                Writer writer = streamResult.getWriter();
                if (writer != null) {
                    this._tohFactory.setWriter(writer);
                    return this._tohFactory.getSerializationHandler();
                }
                OutputStream outputStream = streamResult.getOutputStream();
                if (outputStream != null) {
                    this._tohFactory.setOutputStream(outputStream);
                    return this._tohFactory.getSerializationHandler();
                }
                String string = result.getSystemId();
                if (string == null) {
                    ErrorMsg errorMsg = new ErrorMsg("JAXP_NO_RESULT_ERR");
                    throw new TransformerException(errorMsg.toString());
                }
                URL uRL = null;
                if (string.startsWith("file:")) {
                    uRL = new URL(string);
                    this._ostream = new FileOutputStream(uRL.getFile());
                    this._tohFactory.setOutputStream(this._ostream);
                    return this._tohFactory.getSerializationHandler();
                }
                if (string.startsWith("http:")) {
                    uRL = new URL(string);
                    URLConnection uRLConnection = uRL.openConnection();
                    this._ostream = uRLConnection.getOutputStream();
                    this._tohFactory.setOutputStream(this._ostream);
                    return this._tohFactory.getSerializationHandler();
                }
                uRL = new File(string).toURL();
                this._ostream = new FileOutputStream(uRL.getFile());
                this._tohFactory.setOutputStream(this._ostream);
                return this._tohFactory.getSerializationHandler();
            }
        }
        catch (UnknownServiceException unknownServiceException) {
            throw new TransformerException(unknownServiceException);
        }
        catch (ParserConfigurationException parserConfigurationException) {
            throw new TransformerException(parserConfigurationException);
        }
        catch (IOException iOException) {
            throw new TransformerException(iOException);
        }
        return null;
    }

    protected void setDOM(DOM dOM) {
        this._dom = dOM;
    }

    private DOM getDOM(Source source) throws TransformerException {
        try {
            DOM dOM = null;
            if (source != null) {
                boolean bl;
                DOMWSFilter dOMWSFilter = this._translet != null && this._translet instanceof StripFilter ? new DOMWSFilter(this._translet) : null;
                boolean bl2 = bl = this._translet != null ? this._translet.hasIdCall() : false;
                if (this._dtmManager == null) {
                    this._dtmManager = (XSLTCDTMManager)this._tfactory.getDTMManagerClass().newInstance();
                }
                dOM = (DOM)((Object)this._dtmManager.getDTM(source, false, dOMWSFilter, true, false, false, 0, bl));
            } else if (this._dom != null) {
                dOM = this._dom;
                this._dom = null;
            } else {
                return null;
            }
            if (!this._isIdentity) {
                this._translet.prepassDocument(dOM);
            }
            return dOM;
        }
        catch (Exception exception) {
            if (this._errorListener != null) {
                this.postErrorToListener(exception.getMessage());
            }
            throw new TransformerException(exception);
        }
    }

    protected TransformerFactoryImpl getTransformerFactory() {
        return this._tfactory;
    }

    protected TransletOutputHandlerFactory getTransletOutputHandlerFactory() {
        return this._tohFactory;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void transformIdentity(Source source, SerializationHandler serializationHandler) throws Exception {
        if (source != null) {
            this._sourceSystemId = source.getSystemId();
        }
        if (source instanceof StreamSource) {
            StreamSource streamSource = (StreamSource)source;
            InputStream inputStream = streamSource.getInputStream();
            Reader reader = streamSource.getReader();
            XMLReader xMLReader = this._readerManager.getXMLReader();
            try {
                InputSource inputSource;
                try {
                    xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", serializationHandler);
                }
                catch (SAXException sAXException) {
                    // empty catch block
                }
                xMLReader.setContentHandler(serializationHandler);
                if (inputStream != null) {
                    inputSource = new InputSource(inputStream);
                    inputSource.setSystemId(this._sourceSystemId);
                } else if (reader != null) {
                    inputSource = new InputSource(reader);
                    inputSource.setSystemId(this._sourceSystemId);
                } else if (this._sourceSystemId != null) {
                    inputSource = new InputSource(this._sourceSystemId);
                } else {
                    ErrorMsg errorMsg = new ErrorMsg("JAXP_NO_SOURCE_ERR");
                    throw new TransformerException(errorMsg.toString());
                }
                xMLReader.parse(inputSource);
            }
            finally {
                this._readerManager.releaseXMLReader(xMLReader);
            }
        }
        if (source instanceof SAXSource) {
            SAXSource sAXSource = (SAXSource)source;
            XMLReader xMLReader = sAXSource.getXMLReader();
            InputSource inputSource = sAXSource.getInputSource();
            boolean bl = true;
            try {
                if (xMLReader == null) {
                    xMLReader = this._readerManager.getXMLReader();
                    bl = false;
                }
                try {
                    xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", serializationHandler);
                }
                catch (SAXException sAXException) {
                    // empty catch block
                }
                xMLReader.setContentHandler(serializationHandler);
                xMLReader.parse(inputSource);
            }
            finally {
                if (!bl) {
                    this._readerManager.releaseXMLReader(xMLReader);
                }
            }
        }
        if (source instanceof DOMSource) {
            DOMSource dOMSource = (DOMSource)source;
            new DOM2TO(dOMSource.getNode(), serializationHandler).parse();
        } else if (source instanceof XSLTCSource) {
            DOM dOM = ((XSLTCSource)source).getDOM(null, this._translet);
            ((SAXImpl)dOM).copy(serializationHandler);
        } else {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_NO_SOURCE_ERR");
            throw new TransformerException(errorMsg.toString());
        }
    }

    private void transform(Source source, SerializationHandler serializationHandler, String string) throws TransformerException {
        block16 : {
            try {
                if (source instanceof StreamSource && source.getSystemId() == null && ((StreamSource)source).getInputStream() == null && ((StreamSource)source).getReader() == null || source instanceof SAXSource && ((SAXSource)source).getInputSource() == null && ((SAXSource)source).getXMLReader() == null || source instanceof DOMSource && ((DOMSource)source).getNode() == null) {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    String string2 = source.getSystemId();
                    source = new DOMSource(documentBuilder.newDocument());
                    if (string2 != null) {
                        source.setSystemId(string2);
                    }
                }
                if (this._isIdentity) {
                    this.transformIdentity(source, serializationHandler);
                    break block16;
                }
                this._translet.transform(this.getDOM(source), serializationHandler);
            }
            catch (TransletException transletException) {
                if (this._errorListener != null) {
                    this.postErrorToListener(transletException.getMessage());
                }
                throw new TransformerException(transletException);
            }
            catch (RuntimeException runtimeException) {
                if (this._errorListener != null) {
                    this.postErrorToListener(runtimeException.getMessage());
                }
                throw new TransformerException(runtimeException);
            }
            catch (Exception exception) {
                if (this._errorListener != null) {
                    this.postErrorToListener(exception.getMessage());
                }
                throw new TransformerException(exception);
            }
            finally {
                this._dtmManager = null;
            }
        }
        if (this._ostream != null) {
            try {
                this._ostream.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            this._ostream = null;
        }
    }

    public ErrorListener getErrorListener() {
        return this._errorListener;
    }

    public void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException {
        if (errorListener == null) {
            ErrorMsg errorMsg = new ErrorMsg("ERROR_LISTENER_NULL_ERR", "Transformer");
            throw new IllegalArgumentException(errorMsg.toString());
        }
        this._errorListener = errorListener;
        if (this._translet != null) {
            this._translet.setMessageHandler(new MessageHandler(this._errorListener));
        }
    }

    private void postErrorToListener(String string) {
        try {
            this._errorListener.error(new TransformerException(string));
        }
        catch (TransformerException transformerException) {
            // empty catch block
        }
    }

    private void postWarningToListener(String string) {
        try {
            this._errorListener.warning(new TransformerException(string));
        }
        catch (TransformerException transformerException) {
            // empty catch block
        }
    }

    private String makeCDATAString(Hashtable hashtable) {
        if (hashtable == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        Enumeration enumeration = hashtable.keys();
        if (enumeration.hasMoreElements()) {
            stringBuffer.append((String)enumeration.nextElement());
            while (enumeration.hasMoreElements()) {
                String string = (String)enumeration.nextElement();
                stringBuffer.append(' ');
                stringBuffer.append(string);
            }
        }
        return stringBuffer.toString();
    }

    public Properties getOutputProperties() {
        return (Properties)this._properties.clone();
    }

    public String getOutputProperty(String string) throws IllegalArgumentException {
        if (!this.validOutputProperty(string)) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", string);
            throw new IllegalArgumentException(errorMsg.toString());
        }
        return this._properties.getProperty(string);
    }

    public void setOutputProperties(Properties properties) throws IllegalArgumentException {
        if (properties != null) {
            Enumeration enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String string = (String)enumeration.nextElement();
                if (this.isDefaultProperty(string, properties)) continue;
                if (this.validOutputProperty(string)) {
                    this._properties.setProperty(string, properties.getProperty(string));
                    continue;
                }
                ErrorMsg errorMsg = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", string);
                throw new IllegalArgumentException(errorMsg.toString());
            }
        } else {
            this._properties = this._propertiesClone;
        }
    }

    public void setOutputProperty(String string, String string2) throws IllegalArgumentException {
        if (!this.validOutputProperty(string)) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", string);
            throw new IllegalArgumentException(errorMsg.toString());
        }
        this._properties.setProperty(string, string2);
    }

    private void transferOutputProperties(AbstractTranslet abstractTranslet) {
        if (this._properties == null) {
            return;
        }
        Enumeration enumeration = this._properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            String string2 = (String)this._properties.get(string);
            if (string2 == null) continue;
            if (string.equals("encoding")) {
                abstractTranslet._encoding = string2;
                continue;
            }
            if (string.equals("method")) {
                abstractTranslet._method = string2;
                continue;
            }
            if (string.equals("doctype-public")) {
                abstractTranslet._doctypePublic = string2;
                continue;
            }
            if (string.equals("doctype-system")) {
                abstractTranslet._doctypeSystem = string2;
                continue;
            }
            if (string.equals("media-type")) {
                abstractTranslet._mediaType = string2;
                continue;
            }
            if (string.equals("standalone")) {
                abstractTranslet._standalone = string2;
                continue;
            }
            if (string.equals("version")) {
                abstractTranslet._version = string2;
                continue;
            }
            if (string.equals("omit-xml-declaration")) {
                abstractTranslet._omitHeader = string2 != null && string2.toLowerCase().equals("yes");
                continue;
            }
            if (string.equals("indent")) {
                abstractTranslet._indent = string2 != null && string2.toLowerCase().equals("yes");
                continue;
            }
            if (!string.equals("cdata-section-elements") || string2 == null) continue;
            abstractTranslet._cdata = null;
            StringTokenizer stringTokenizer = new StringTokenizer(string2);
            while (stringTokenizer.hasMoreTokens()) {
                abstractTranslet.addCdataElement(stringTokenizer.nextToken());
            }
        }
    }

    public void transferOutputProperties(SerializationHandler serializationHandler) {
        if (this._properties == null) {
            return;
        }
        String string = null;
        String string2 = null;
        Enumeration enumeration = this._properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String string3 = (String)enumeration.nextElement();
            String string4 = (String)this._properties.get(string3);
            if (string4 == null) continue;
            if (string3.equals("doctype-public")) {
                string = string4;
                continue;
            }
            if (string3.equals("doctype-system")) {
                string2 = string4;
                continue;
            }
            if (string3.equals("media-type")) {
                serializationHandler.setMediaType(string4);
                continue;
            }
            if (string3.equals("standalone")) {
                serializationHandler.setStandalone(string4);
                continue;
            }
            if (string3.equals("version")) {
                serializationHandler.setVersion(string4);
                continue;
            }
            if (string3.equals("omit-xml-declaration")) {
                serializationHandler.setOmitXMLDeclaration(string4 != null && string4.toLowerCase().equals("yes"));
                continue;
            }
            if (string3.equals("indent")) {
                serializationHandler.setIndent(string4 != null && string4.toLowerCase().equals("yes"));
                continue;
            }
            if (!string3.equals("cdata-section-elements") || string4 == null) continue;
            StringTokenizer stringTokenizer = new StringTokenizer(string4);
            Vector<String> vector = null;
            while (stringTokenizer.hasMoreTokens()) {
                String string5;
                String string6;
                String string7 = stringTokenizer.nextToken();
                int n2 = string7.lastIndexOf(58);
                if (n2 > 0) {
                    string6 = string7.substring(0, n2);
                    string5 = string7.substring(n2 + 1);
                } else {
                    string6 = null;
                    string5 = string7;
                }
                if (vector == null) {
                    vector = new Vector<String>();
                }
                vector.addElement(string6);
                vector.addElement(string5);
            }
            serializationHandler.setCdataSectionElements(vector);
        }
        if (string != null || string2 != null) {
            serializationHandler.setDoctype(string2, string);
        }
    }

    private Properties createOutputProperties(Properties properties) {
        Object object;
        Properties properties2 = new Properties();
        this.setDefaults(properties2, "xml");
        Properties properties3 = new Properties(properties2);
        if (properties != null) {
            object = properties.propertyNames();
            while (object.hasMoreElements()) {
                String string = (String)object.nextElement();
                properties3.setProperty(string, properties.getProperty(string));
            }
        } else {
            properties3.setProperty("encoding", this._translet._encoding);
            if (this._translet._method != null) {
                properties3.setProperty("method", this._translet._method);
            }
        }
        if ((object = properties3.getProperty("method")) != null) {
            if (object.equals("html")) {
                this.setDefaults(properties2, "html");
            } else if (object.equals("text")) {
                this.setDefaults(properties2, "text");
            }
        }
        return properties3;
    }

    private void setDefaults(Properties properties, String string) {
        Properties properties2 = OutputPropertiesFactory.getDefaultMethodProperties(string);
        Enumeration enumeration = properties2.propertyNames();
        while (enumeration.hasMoreElements()) {
            String string2 = (String)enumeration.nextElement();
            properties.setProperty(string2, properties2.getProperty(string2));
        }
    }

    private boolean validOutputProperty(String string) {
        return string.equals("encoding") || string.equals("method") || string.equals("indent") || string.equals("doctype-public") || string.equals("doctype-system") || string.equals("cdata-section-elements") || string.equals("media-type") || string.equals("omit-xml-declaration") || string.equals("standalone") || string.equals("version") || string.charAt(0) == '{';
    }

    private boolean isDefaultProperty(String string, Properties properties) {
        return properties.get(string) == null;
    }

    public void setParameter(String string, Object object) {
        if (object == null) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_INVALID_SET_PARAM_VALUE", string);
            throw new IllegalArgumentException(errorMsg.toString());
        }
        if (this._isIdentity) {
            if (this._parameters == null) {
                this._parameters = new Hashtable();
            }
            this._parameters.put(string, object);
        } else {
            this._translet.addParameter(string, object);
        }
    }

    public void clearParameters() {
        if (this._isIdentity && this._parameters != null) {
            this._parameters.clear();
        } else {
            this._translet.clearParameters();
        }
    }

    public final Object getParameter(String string) {
        if (this._isIdentity) {
            return this._parameters != null ? this._parameters.get(string) : null;
        }
        return this._translet.getParameter(string);
    }

    public URIResolver getURIResolver() {
        return this._uriResolver;
    }

    public void setURIResolver(URIResolver uRIResolver) {
        this._uriResolver = uRIResolver;
    }

    public DOM retrieveDocument(String string, String string2, Translet translet) {
        try {
            Source source;
            if (string2.length() == 0) {
                string2 = string;
            }
            if ((source = this._uriResolver.resolve(string2, string)) == null) {
                StreamSource streamSource = new StreamSource(SystemIDResolver.getAbsoluteURI(string2, string));
                return this.getDOM(streamSource);
            }
            return this.getDOM(source);
        }
        catch (TransformerException transformerException) {
            if (this._errorListener != null) {
                this.postErrorToListener("File not found: " + transformerException.getMessage());
            }
            return null;
        }
    }

    public void error(TransformerException transformerException) throws TransformerException {
        Throwable throwable = transformerException.getException();
        if (throwable != null) {
            System.err.println(new ErrorMsg("ERROR_PLUS_WRAPPED_MSG", (Object)transformerException.getMessageAndLocation(), (Object)throwable.getMessage()));
        } else {
            System.err.println(new ErrorMsg("ERROR_MSG", transformerException.getMessageAndLocation()));
        }
        throw transformerException;
    }

    public void fatalError(TransformerException transformerException) throws TransformerException {
        Throwable throwable = transformerException.getException();
        if (throwable != null) {
            System.err.println(new ErrorMsg("FATAL_ERR_PLUS_WRAPPED_MSG", (Object)transformerException.getMessageAndLocation(), (Object)throwable.getMessage()));
        } else {
            System.err.println(new ErrorMsg("FATAL_ERR_MSG", transformerException.getMessageAndLocation()));
        }
        throw transformerException;
    }

    public void warning(TransformerException transformerException) throws TransformerException {
        Throwable throwable = transformerException.getException();
        if (throwable != null) {
            System.err.println(new ErrorMsg("WARNING_PLUS_WRAPPED_MSG", (Object)transformerException.getMessageAndLocation(), (Object)throwable.getMessage()));
        } else {
            System.err.println(new ErrorMsg("WARNING_MSG", transformerException.getMessageAndLocation()));
        }
    }

    public void reset() {
        this._method = null;
        this._encoding = null;
        this._sourceSystemId = null;
        this._errorListener = this;
        this._uriResolver = null;
        this._dom = null;
        this._parameters = null;
        this._indentNumber = 0;
        this.setOutputProperties(null);
    }

    static class MessageHandler
    extends org.apache.xalan.xsltc.runtime.MessageHandler {
        private ErrorListener _errorListener;

        public MessageHandler(ErrorListener errorListener) {
            this._errorListener = errorListener;
        }

        public void displayMessage(String string) {
            if (this._errorListener == null) {
                System.err.println(string);
            } else {
                try {
                    this._errorListener.warning(new TransformerException(string));
                }
                catch (TransformerException transformerException) {
                    // empty catch block
                }
            }
        }
    }

}

