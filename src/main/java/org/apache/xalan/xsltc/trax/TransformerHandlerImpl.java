/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMBuilder;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xalan.xsltc.trax.TransformerFactoryImpl;
import org.apache.xalan.xsltc.trax.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

public class TransformerHandlerImpl
implements TransformerHandler,
DeclHandler {
    private TransformerImpl _transformer;
    private AbstractTranslet _translet = null;
    private String _systemId;
    private SAXImpl _dom = null;
    private ContentHandler _handler = null;
    private LexicalHandler _lexHandler = null;
    private DTDHandler _dtdHandler = null;
    private DeclHandler _declHandler = null;
    private Result _result = null;
    private Locator _locator = null;
    private boolean _done = false;
    private boolean _isIdentity = false;

    public TransformerHandlerImpl(TransformerImpl transformerImpl) {
        this._transformer = transformerImpl;
        if (transformerImpl.isIdentity()) {
            this._handler = new DefaultHandler();
            this._isIdentity = true;
        } else {
            this._translet = this._transformer.getTranslet();
        }
    }

    public String getSystemId() {
        return this._systemId;
    }

    public void setSystemId(String string) {
        this._systemId = string;
    }

    public Transformer getTransformer() {
        return this._transformer;
    }

    public void setResult(Result result) throws IllegalArgumentException {
        this._result = result;
        if (null == result) {
            ErrorMsg errorMsg = new ErrorMsg("ER_RESULT_NULL");
            throw new IllegalArgumentException(errorMsg.toString());
        }
        if (this._isIdentity) {
            try {
                SerializationHandler serializationHandler = this._transformer.getOutputHandler(result);
                this._transformer.transferOutputProperties(serializationHandler);
                this._handler = serializationHandler;
                this._lexHandler = serializationHandler;
            }
            catch (TransformerException transformerException) {
                this._result = null;
            }
        } else if (this._done) {
            try {
                this._transformer.setDOM(this._dom);
                this._transformer.transform(null, this._result);
            }
            catch (TransformerException transformerException) {
                throw new IllegalArgumentException(transformerException.getMessage());
            }
        }
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        this._handler.characters(arrc, n2, n3);
    }

    public void startDocument() throws SAXException {
        if (this._result == null) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_SET_RESULT_ERR");
            throw new SAXException(errorMsg.toString());
        }
        if (!this._isIdentity) {
            boolean bl = this._translet != null ? this._translet.hasIdCall() : false;
            XSLTCDTMManager xSLTCDTMManager = null;
            try {
                xSLTCDTMManager = (XSLTCDTMManager)this._transformer.getTransformerFactory().getDTMManagerClass().newInstance();
            }
            catch (Exception exception) {
                throw new SAXException(exception);
            }
            DOMWSFilter dOMWSFilter = this._translet != null && this._translet instanceof StripFilter ? new DOMWSFilter(this._translet) : null;
            this._dom = (SAXImpl)xSLTCDTMManager.getDTM(null, false, dOMWSFilter, true, false, bl);
            this._handler = this._dom.getBuilder();
            this._lexHandler = (LexicalHandler)((Object)this._handler);
            this._dtdHandler = (DTDHandler)((Object)this._handler);
            this._declHandler = (DeclHandler)((Object)this._handler);
            this._dom.setDocumentURI(this._systemId);
            if (this._locator != null) {
                this._handler.setDocumentLocator(this._locator);
            }
        }
        this._handler.startDocument();
    }

    public void endDocument() throws SAXException {
        this._handler.endDocument();
        if (!this._isIdentity) {
            if (this._result != null) {
                try {
                    this._transformer.setDOM(this._dom);
                    this._transformer.transform(null, this._result);
                }
                catch (TransformerException transformerException) {
                    throw new SAXException(transformerException);
                }
            }
            this._done = true;
            this._transformer.setDOM(this._dom);
        }
        if (this._isIdentity && this._result instanceof DOMResult) {
            ((DOMResult)this._result).setNode(this._transformer.getTransletOutputHandlerFactory().getNode());
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        this._handler.startElement(string, string2, string3, attributes);
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        this._handler.endElement(string, string2, string3);
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        this._handler.processingInstruction(string, string2);
    }

    public void startCDATA() throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.startCDATA();
        }
    }

    public void endCDATA() throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.endCDATA();
        }
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.comment(arrc, n2, n3);
        }
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        this._handler.ignorableWhitespace(arrc, n2, n3);
    }

    public void setDocumentLocator(Locator locator) {
        this._locator = locator;
        if (this._handler != null) {
            this._handler.setDocumentLocator(locator);
        }
    }

    public void skippedEntity(String string) throws SAXException {
        this._handler.skippedEntity(string);
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        this._handler.startPrefixMapping(string, string2);
    }

    public void endPrefixMapping(String string) throws SAXException {
        this._handler.endPrefixMapping(string);
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.startDTD(string, string2, string3);
        }
    }

    public void endDTD() throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.endDTD();
        }
    }

    public void startEntity(String string) throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.startEntity(string);
        }
    }

    public void endEntity(String string) throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.endEntity(string);
        }
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) throws SAXException {
        if (this._dtdHandler != null) {
            this._dtdHandler.unparsedEntityDecl(string, string2, string3, string4);
        }
    }

    public void notationDecl(String string, String string2, String string3) throws SAXException {
        if (this._dtdHandler != null) {
            this._dtdHandler.notationDecl(string, string2, string3);
        }
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
        if (this._declHandler != null) {
            this._declHandler.attributeDecl(string, string2, string3, string4, string5);
        }
    }

    public void elementDecl(String string, String string2) throws SAXException {
        if (this._declHandler != null) {
            this._declHandler.elementDecl(string, string2);
        }
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
        if (this._declHandler != null) {
            this._declHandler.externalEntityDecl(string, string2, string3);
        }
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
        if (this._declHandler != null) {
            this._declHandler.internalEntityDecl(string, string2);
        }
    }
}

