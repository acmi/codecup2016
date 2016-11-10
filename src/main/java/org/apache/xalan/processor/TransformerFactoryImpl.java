/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TrAXFilter;
import org.apache.xalan.transformer.TransformerIdentityImpl;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.DOM2Helper;
import org.apache.xml.utils.DOMHelper;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.StopParseException;
import org.apache.xml.utils.StylesheetPIHandler;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.TreeWalker;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class TransformerFactoryImpl
extends SAXTransformerFactory {
    public static final String XSLT_PROPERTIES = "org/apache/xalan/res/XSLTInfo.properties";
    private boolean m_isSecureProcessing = false;
    public static final String FEATURE_INCREMENTAL = "http://xml.apache.org/xalan/features/incremental";
    public static final String FEATURE_OPTIMIZE = "http://xml.apache.org/xalan/features/optimize";
    public static final String FEATURE_SOURCE_LOCATION = "http://xml.apache.org/xalan/properties/source-location";
    private String m_DOMsystemID = null;
    private boolean m_optimize = true;
    private boolean m_source_location = false;
    private boolean m_incremental = false;
    URIResolver m_uriResolver;
    private ErrorListener m_errorListener = new DefaultErrorHandler(false);

    public Templates processFromNode(Node node) throws TransformerConfigurationException {
        try {
            TemplatesHandler templatesHandler = this.newTemplatesHandler();
            TreeWalker treeWalker = new TreeWalker(templatesHandler, new DOM2Helper(), templatesHandler.getSystemId());
            treeWalker.traverse(node);
            return templatesHandler.getTemplates();
        }
        catch (SAXException sAXException) {
            if (this.m_errorListener != null) {
                try {
                    this.m_errorListener.fatalError(new TransformerException(sAXException));
                }
                catch (TransformerConfigurationException transformerConfigurationException) {
                    throw transformerConfigurationException;
                }
                catch (TransformerException transformerException) {
                    throw new TransformerConfigurationException(transformerException);
                }
                return null;
            }
            throw new TransformerConfigurationException(XSLMessages.createMessage("ER_PROCESSFROMNODE_FAILED", null), sAXException);
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            throw transformerConfigurationException;
        }
        catch (Exception exception) {
            if (this.m_errorListener != null) {
                try {
                    this.m_errorListener.fatalError(new TransformerException(exception));
                }
                catch (TransformerConfigurationException transformerConfigurationException) {
                    throw transformerConfigurationException;
                }
                catch (TransformerException transformerException) {
                    throw new TransformerConfigurationException(transformerException);
                }
                return null;
            }
            throw new TransformerConfigurationException(XSLMessages.createMessage("ER_PROCESSFROMNODE_FAILED", null), exception);
        }
    }

    String getDOMsystemID() {
        return this.m_DOMsystemID;
    }

    Templates processFromNode(Node node, String string) throws TransformerConfigurationException {
        this.m_DOMsystemID = string;
        return this.processFromNode(node);
    }

    public Source getAssociatedStylesheet(Source source, String string, String string2, String string3) throws TransformerConfigurationException {
        Object object;
        block18 : {
            String string4;
            InputSource inputSource = null;
            Node node = null;
            XMLReader xMLReader = null;
            if (source instanceof DOMSource) {
                object = (DOMSource)source;
                node = object.getNode();
                string4 = object.getSystemId();
            } else {
                inputSource = SAXSource.sourceToInputSource(source);
                string4 = inputSource.getSystemId();
            }
            object = new StylesheetPIHandler(string4, string, string2, string3);
            if (this.m_uriResolver != null) {
                object.setURIResolver(this.m_uriResolver);
            }
            try {
                if (null != node) {
                    TreeWalker treeWalker = new TreeWalker((ContentHandler)object, new DOM2Helper(), string4);
                    treeWalker.traverse(node);
                    break block18;
                }
                try {
                    SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
                    sAXParserFactory.setNamespaceAware(true);
                    if (this.m_isSecureProcessing) {
                        try {
                            sAXParserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                        }
                        catch (SAXException sAXException) {
                            // empty catch block
                        }
                    }
                    SAXParser sAXParser = sAXParserFactory.newSAXParser();
                    xMLReader = sAXParser.getXMLReader();
                }
                catch (ParserConfigurationException parserConfigurationException) {
                    throw new SAXException(parserConfigurationException);
                }
                catch (FactoryConfigurationError factoryConfigurationError) {
                    throw new SAXException(factoryConfigurationError.toString());
                }
                catch (NoSuchMethodError noSuchMethodError) {
                }
                catch (AbstractMethodError abstractMethodError) {
                    // empty catch block
                }
                if (null == xMLReader) {
                    xMLReader = XMLReaderFactory.createXMLReader();
                }
                if (this.m_isSecureProcessing) {
                    xMLReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
                }
                xMLReader.setContentHandler((ContentHandler)object);
                xMLReader.parse(inputSource);
            }
            catch (StopParseException stopParseException) {
            }
            catch (SAXException sAXException) {
                throw new TransformerConfigurationException("getAssociatedStylesheets failed", sAXException);
            }
            catch (IOException iOException) {
                throw new TransformerConfigurationException("getAssociatedStylesheets failed", iOException);
            }
        }
        return object.getAssociatedStylesheet();
    }

    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
        return new StylesheetHandler(this);
    }

    public void setFeature(String string, boolean bl) throws TransformerConfigurationException {
        if (string == null) {
            throw new NullPointerException(XSLMessages.createMessage("ER_SET_FEATURE_NULL_NAME", null));
        }
        if (!string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            throw new TransformerConfigurationException(XSLMessages.createMessage("ER_UNSUPPORTED_FEATURE", new Object[]{string}));
        }
        this.m_isSecureProcessing = bl;
    }

    public boolean getFeature(String string) {
        if (string == null) {
            throw new NullPointerException(XSLMessages.createMessage("ER_GET_FEATURE_NULL_NAME", null));
        }
        if ("http://javax.xml.transform.dom.DOMResult/feature" == string || "http://javax.xml.transform.dom.DOMSource/feature" == string || "http://javax.xml.transform.sax.SAXResult/feature" == string || "http://javax.xml.transform.sax.SAXSource/feature" == string || "http://javax.xml.transform.stream.StreamResult/feature" == string || "http://javax.xml.transform.stream.StreamSource/feature" == string || "http://javax.xml.transform.sax.SAXTransformerFactory/feature" == string || "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter" == string) {
            return true;
        }
        if ("http://javax.xml.transform.dom.DOMResult/feature".equals(string) || "http://javax.xml.transform.dom.DOMSource/feature".equals(string) || "http://javax.xml.transform.sax.SAXResult/feature".equals(string) || "http://javax.xml.transform.sax.SAXSource/feature".equals(string) || "http://javax.xml.transform.stream.StreamResult/feature".equals(string) || "http://javax.xml.transform.stream.StreamSource/feature".equals(string) || "http://javax.xml.transform.sax.SAXTransformerFactory/feature".equals(string) || "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter".equals(string)) {
            return true;
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this.m_isSecureProcessing;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setAttribute(String string, Object object) throws IllegalArgumentException {
        if (string.equals("http://xml.apache.org/xalan/features/incremental")) {
            if (object instanceof Boolean) {
                this.m_incremental = (Boolean)object;
                return;
            } else {
                if (!(object instanceof String)) throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{string, object}));
                this.m_incremental = new Boolean((String)object);
            }
            return;
        } else if (string.equals("http://xml.apache.org/xalan/features/optimize")) {
            if (object instanceof Boolean) {
                this.m_optimize = (Boolean)object;
                return;
            } else {
                if (!(object instanceof String)) throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{string, object}));
                this.m_optimize = new Boolean((String)object);
            }
            return;
        } else {
            if (!string.equals("http://xml.apache.org/xalan/properties/source-location")) throw new IllegalArgumentException(XSLMessages.createMessage("ER_NOT_SUPPORTED", new Object[]{string}));
            if (object instanceof Boolean) {
                this.m_source_location = (Boolean)object;
                return;
            } else {
                if (!(object instanceof String)) throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{string, object}));
                this.m_source_location = new Boolean((String)object);
            }
        }
    }

    public Object getAttribute(String string) throws IllegalArgumentException {
        if (string.equals("http://xml.apache.org/xalan/features/incremental")) {
            return this.m_incremental ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equals("http://xml.apache.org/xalan/features/optimize")) {
            return this.m_optimize ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equals("http://xml.apache.org/xalan/properties/source-location")) {
            return this.m_source_location ? Boolean.TRUE : Boolean.FALSE;
        }
        throw new IllegalArgumentException(XSLMessages.createMessage("ER_ATTRIB_VALUE_NOT_RECOGNIZED", new Object[]{string}));
    }

    public XMLFilter newXMLFilter(Source source) throws TransformerConfigurationException {
        Templates templates = this.newTemplates(source);
        if (templates == null) {
            return null;
        }
        return this.newXMLFilter(templates);
    }

    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
        try {
            return new TrAXFilter(templates);
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            if (this.m_errorListener != null) {
                try {
                    this.m_errorListener.fatalError(transformerConfigurationException);
                    return null;
                }
                catch (TransformerConfigurationException transformerConfigurationException2) {
                    throw transformerConfigurationException2;
                }
                catch (TransformerException transformerException) {
                    throw new TransformerConfigurationException(transformerException);
                }
            }
            throw transformerConfigurationException;
        }
    }

    public TransformerHandler newTransformerHandler(Source source) throws TransformerConfigurationException {
        Templates templates = this.newTemplates(source);
        if (templates == null) {
            return null;
        }
        return this.newTransformerHandler(templates);
    }

    public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
        try {
            TransformerImpl transformerImpl = (TransformerImpl)templates.newTransformer();
            transformerImpl.setURIResolver(this.m_uriResolver);
            TransformerHandler transformerHandler = (TransformerHandler)transformerImpl.getInputContentHandler(true);
            return transformerHandler;
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            if (this.m_errorListener != null) {
                try {
                    this.m_errorListener.fatalError(transformerConfigurationException);
                    return null;
                }
                catch (TransformerConfigurationException transformerConfigurationException2) {
                    throw transformerConfigurationException2;
                }
                catch (TransformerException transformerException) {
                    throw new TransformerConfigurationException(transformerException);
                }
            }
            throw transformerConfigurationException;
        }
    }

    public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
        return new TransformerIdentityImpl(this.m_isSecureProcessing);
    }

    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        try {
            Templates templates = this.newTemplates(source);
            if (templates == null) {
                return null;
            }
            Transformer transformer = templates.newTransformer();
            transformer.setURIResolver(this.m_uriResolver);
            return transformer;
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            if (this.m_errorListener != null) {
                try {
                    this.m_errorListener.fatalError(transformerConfigurationException);
                    return null;
                }
                catch (TransformerConfigurationException transformerConfigurationException2) {
                    throw transformerConfigurationException2;
                }
                catch (TransformerException transformerException) {
                    throw new TransformerConfigurationException(transformerException);
                }
            }
            throw transformerConfigurationException;
        }
    }

    public Transformer newTransformer() throws TransformerConfigurationException {
        return new TransformerIdentityImpl(this.m_isSecureProcessing);
    }

    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        String string = source.getSystemId();
        if (null != string) {
            string = SystemIDResolver.getAbsoluteURI(string);
        }
        if (source instanceof DOMSource) {
            DOMSource dOMSource = (DOMSource)source;
            Node node = dOMSource.getNode();
            if (null != node) {
                return this.processFromNode(node, string);
            }
            String string2 = XSLMessages.createMessage("ER_ILLEGAL_DOMSOURCE_INPUT", null);
            throw new IllegalArgumentException(string2);
        }
        TemplatesHandler templatesHandler = this.newTemplatesHandler();
        templatesHandler.setSystemId(string);
        try {
            InputSource inputSource = SAXSource.sourceToInputSource(source);
            inputSource.setSystemId(string);
            XMLReader xMLReader = null;
            if (source instanceof SAXSource) {
                xMLReader = ((SAXSource)source).getXMLReader();
            }
            if (null == xMLReader) {
                try {
                    SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
                    sAXParserFactory.setNamespaceAware(true);
                    if (this.m_isSecureProcessing) {
                        try {
                            sAXParserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                        }
                        catch (SAXException sAXException) {
                            // empty catch block
                        }
                    }
                    SAXParser sAXParser = sAXParserFactory.newSAXParser();
                    xMLReader = sAXParser.getXMLReader();
                }
                catch (ParserConfigurationException parserConfigurationException) {
                    throw new SAXException(parserConfigurationException);
                }
                catch (FactoryConfigurationError factoryConfigurationError) {
                    throw new SAXException(factoryConfigurationError.toString());
                }
                catch (NoSuchMethodError noSuchMethodError) {
                }
                catch (AbstractMethodError abstractMethodError) {
                    // empty catch block
                }
            }
            if (null == xMLReader) {
                xMLReader = XMLReaderFactory.createXMLReader();
            }
            xMLReader.setContentHandler(templatesHandler);
            xMLReader.parse(inputSource);
        }
        catch (SAXException sAXException) {
            if (this.m_errorListener != null) {
                try {
                    this.m_errorListener.fatalError(new TransformerException(sAXException));
                }
                catch (TransformerConfigurationException transformerConfigurationException) {
                    throw transformerConfigurationException;
                }
                catch (TransformerException transformerException) {
                    throw new TransformerConfigurationException(transformerException);
                }
            }
            throw new TransformerConfigurationException(sAXException.getMessage(), sAXException);
        }
        catch (Exception exception) {
            if (this.m_errorListener != null) {
                try {
                    this.m_errorListener.fatalError(new TransformerException(exception));
                    return null;
                }
                catch (TransformerConfigurationException transformerConfigurationException) {
                    throw transformerConfigurationException;
                }
                catch (TransformerException transformerException) {
                    throw new TransformerConfigurationException(transformerException);
                }
            }
            throw new TransformerConfigurationException(exception.getMessage(), exception);
        }
        return templatesHandler.getTemplates();
    }

    public void setURIResolver(URIResolver uRIResolver) {
        this.m_uriResolver = uRIResolver;
    }

    public URIResolver getURIResolver() {
        return this.m_uriResolver;
    }

    public ErrorListener getErrorListener() {
        return this.m_errorListener;
    }

    public void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException {
        if (null == errorListener) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_ERRORLISTENER", null));
        }
        this.m_errorListener = errorListener;
    }

    public boolean isSecureProcessing() {
        return this.m_isSecureProcessing;
    }
}

