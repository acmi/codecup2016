/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.transformer.TransformerImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

public class TrAXFilter
extends XMLFilterImpl {
    private Templates m_templates;
    private TransformerImpl m_transformer;

    public TrAXFilter(Templates templates) throws TransformerConfigurationException {
        this.m_templates = templates;
        this.m_transformer = (TransformerImpl)templates.newTransformer();
    }

    public TransformerImpl getTransformer() {
        return this.m_transformer;
    }

    public void setParent(XMLReader xMLReader) {
        super.setParent(xMLReader);
        if (null != xMLReader.getContentHandler()) {
            this.setContentHandler(xMLReader.getContentHandler());
        }
        this.setupParse();
    }

    public void parse(InputSource inputSource) throws SAXException, IOException {
        Object object;
        if (null == this.getParent()) {
            Object object2;
            object = null;
            try {
                object2 = SAXParserFactory.newInstance();
                object2.setNamespaceAware(true);
                if (this.m_transformer.getStylesheet().isSecureProcessing()) {
                    try {
                        object2.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                    }
                    catch (SAXException sAXException) {
                        // empty catch block
                    }
                }
                SAXParser sAXParser = object2.newSAXParser();
                object = sAXParser.getXMLReader();
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
            object2 = object == null ? XMLReaderFactory.createXMLReader() : object;
            try {
                object2.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            }
            catch (SAXException sAXException) {
                // empty catch block
            }
            this.setParent((XMLReader)object2);
        } else {
            this.setupParse();
        }
        if (null == this.m_transformer.getContentHandler()) {
            throw new SAXException(XSLMessages.createMessage("ER_CANNOT_CALL_PARSE", null));
        }
        this.getParent().parse(inputSource);
        object = this.m_transformer.getExceptionThrown();
        if (null != object) {
            if (object instanceof SAXException) {
                throw (SAXException)object;
            }
            throw new SAXException((Exception)object);
        }
    }

    public void parse(String string) throws SAXException, IOException {
        this.parse(new InputSource(string));
    }

    private void setupParse() {
        XMLReader xMLReader = this.getParent();
        if (xMLReader == null) {
            throw new NullPointerException(XSLMessages.createMessage("ER_NO_PARENT_FOR_FILTER", null));
        }
        ContentHandler contentHandler = this.m_transformer.getInputContentHandler();
        xMLReader.setContentHandler(contentHandler);
        xMLReader.setEntityResolver(this);
        xMLReader.setDTDHandler(this);
        xMLReader.setErrorHandler(this);
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.m_transformer.setContentHandler(contentHandler);
    }

    public void setErrorListener(ErrorListener errorListener) {
        this.m_transformer.setErrorListener(errorListener);
    }
}

