/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.utils.DOM2Helper;
import org.apache.xml.utils.DOMHelper;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.TreeWalker;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ProcessorInclude
extends XSLTElementProcessor {
    static final long serialVersionUID = -4570078731972673481L;
    private String m_href = null;

    public String getHref() {
        return this.m_href;
    }

    public void setHref(String string) {
        this.m_href = string;
    }

    protected int getStylesheetType() {
        return 2;
    }

    protected String getStylesheetInclErr() {
        return "ER_STYLESHEET_INCLUDES_ITSELF";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, this);
        try {
            Source source = this.getSourceFromUriResolver(stylesheetHandler);
            String string4 = this.getBaseURIOfIncludedStylesheet(stylesheetHandler, source);
            if (stylesheetHandler.importStackContains(string4)) {
                throw new SAXException(XSLMessages.createMessage(this.getStylesheetInclErr(), new Object[]{string4}));
            }
            stylesheetHandler.pushImportURL(string4);
            stylesheetHandler.pushImportSource(source);
            int n2 = stylesheetHandler.getStylesheetType();
            stylesheetHandler.setStylesheetType(this.getStylesheetType());
            stylesheetHandler.pushNewNamespaceSupport();
            try {
                this.parse(stylesheetHandler, string, string2, string3, attributes);
                Object var10_10 = null;
                stylesheetHandler.setStylesheetType(n2);
                stylesheetHandler.popImportURL();
                stylesheetHandler.popImportSource();
                stylesheetHandler.popNamespaceSupport();
            }
            catch (Throwable throwable) {
                Object var10_11 = null;
                stylesheetHandler.setStylesheetType(n2);
                stylesheetHandler.popImportURL();
                stylesheetHandler.popImportSource();
                stylesheetHandler.popNamespaceSupport();
                throw throwable;
            }
        }
        catch (TransformerException transformerException) {
            stylesheetHandler.error(transformerException.getMessage(), transformerException);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void parse(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        block22 : {
            TransformerFactoryImpl transformerFactoryImpl = stylesheetHandler.getStylesheetProcessor();
            URIResolver uRIResolver = transformerFactoryImpl.getURIResolver();
            try {
                Object object;
                Object object2;
                Source source = null;
                if (null != uRIResolver && null != (source = stylesheetHandler.peekSourceFromURIResolver()) && source instanceof DOMSource) {
                    Node node = ((DOMSource)source).getNode();
                    String string4 = stylesheetHandler.peekImportURL();
                    if (string4 != null) {
                        stylesheetHandler.pushBaseIndentifier(string4);
                    }
                    TreeWalker treeWalker = new TreeWalker(stylesheetHandler, new DOM2Helper(), string4);
                    try {
                        treeWalker.traverse(node);
                    }
                    catch (SAXException sAXException) {
                        throw new TransformerException(sAXException);
                    }
                    if (string4 != null) {
                        stylesheetHandler.popBaseIndentifier();
                    }
                    return;
                }
                if (null == source) {
                    object2 = SystemIDResolver.getAbsoluteURI(this.getHref(), stylesheetHandler.getBaseIdentifier());
                    source = new StreamSource((String)object2);
                }
                source = this.processSource(stylesheetHandler, source);
                object2 = null;
                if (source instanceof SAXSource) {
                    object = (SAXSource)source;
                    object2 = object.getXMLReader();
                }
                object = SAXSource.sourceToInputSource(source);
                if (null == object2) {
                    try {
                        SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
                        sAXParserFactory.setNamespaceAware(true);
                        if (stylesheetHandler.getStylesheetProcessor().isSecureProcessing()) {
                            try {
                                sAXParserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                            }
                            catch (SAXException sAXException) {
                                // empty catch block
                            }
                        }
                        SAXParser sAXParser = sAXParserFactory.newSAXParser();
                        object2 = sAXParser.getXMLReader();
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
                if (null == object2) {
                    object2 = XMLReaderFactory.createXMLReader();
                }
                if (null == object2) break block22;
                object2.setContentHandler(stylesheetHandler);
                stylesheetHandler.pushBaseIndentifier(object.getSystemId());
                try {
                    object2.parse((InputSource)object);
                    Object var14_24 = null;
                    stylesheetHandler.popBaseIndentifier();
                }
                catch (Throwable throwable) {
                    Object var14_25 = null;
                    stylesheetHandler.popBaseIndentifier();
                    throw throwable;
                }
            }
            catch (IOException iOException) {
                stylesheetHandler.error("ER_IOEXCEPTION", new Object[]{this.getHref()}, iOException);
            }
            catch (TransformerException transformerException) {
                stylesheetHandler.error(transformerException.getMessage(), transformerException);
            }
        }
    }

    protected Source processSource(StylesheetHandler stylesheetHandler, Source source) {
        return source;
    }

    private Source getSourceFromUriResolver(StylesheetHandler stylesheetHandler) throws TransformerException {
        Source source = null;
        TransformerFactoryImpl transformerFactoryImpl = stylesheetHandler.getStylesheetProcessor();
        URIResolver uRIResolver = transformerFactoryImpl.getURIResolver();
        if (uRIResolver != null) {
            String string = this.getHref();
            String string2 = stylesheetHandler.getBaseIdentifier();
            source = uRIResolver.resolve(string, string2);
        }
        return source;
    }

    private String getBaseURIOfIncludedStylesheet(StylesheetHandler stylesheetHandler, Source source) throws TransformerException {
        String string;
        String string2 = source != null && (string = source.getSystemId()) != null ? string : SystemIDResolver.getAbsoluteURI(this.getHref(), stylesheetHandler.getBaseIdentifier());
        return string2;
    }
}

