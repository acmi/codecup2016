/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXResult;
import org.apache.xalan.xsltc.trax.TransformerHandlerImpl;
import org.apache.xalan.xsltc.trax.TransformerImpl;
import org.apache.xml.utils.XMLReaderManager;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

public class TrAXFilter
extends XMLFilterImpl {
    private Templates _templates;
    private TransformerImpl _transformer;
    private TransformerHandlerImpl _transformerHandler;

    public TrAXFilter(Templates templates) throws TransformerConfigurationException {
        this._templates = templates;
        this._transformer = (TransformerImpl)templates.newTransformer();
        this._transformerHandler = new TransformerHandlerImpl(this._transformer);
    }

    public Transformer getTransformer() {
        return this._transformer;
    }

    private void createParent() throws SAXException {
        XMLReader xMLReader = null;
        try {
            SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
            sAXParserFactory.setNamespaceAware(true);
            if (this._transformer.isSecureProcessing()) {
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
        if (xMLReader == null) {
            xMLReader = XMLReaderFactory.createXMLReader();
        }
        this.setParent(xMLReader);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void parse(InputSource inputSource) throws SAXException, IOException {
        XMLReader xMLReader = null;
        try {
            if (this.getParent() == null) {
                try {
                    xMLReader = XMLReaderManager.getInstance().getXMLReader();
                    this.setParent(xMLReader);
                }
                catch (SAXException sAXException) {
                    throw new SAXException(sAXException.toString());
                }
            }
            this.getParent().parse(inputSource);
        }
        finally {
            if (xMLReader != null) {
                XMLReaderManager.getInstance().releaseXMLReader(xMLReader);
            }
        }
    }

    public void parse(String string) throws SAXException, IOException {
        this.parse(new InputSource(string));
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this._transformerHandler.setResult(new SAXResult(contentHandler));
        if (this.getParent() == null) {
            try {
                this.createParent();
            }
            catch (SAXException sAXException) {
                return;
            }
        }
        this.getParent().setContentHandler(this._transformerHandler);
    }

    public void setErrorListener(ErrorListener errorListener) {
    }
}

