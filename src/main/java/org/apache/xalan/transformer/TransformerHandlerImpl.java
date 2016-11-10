/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.io.IOException;
import java.io.PrintStream;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.IncrementalSAXSource_Filter;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.XPathContext;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class TransformerHandlerImpl
implements TransformerHandler,
ContentHandler,
DTDHandler,
EntityResolver,
ErrorHandler,
DeclHandler,
LexicalHandler {
    private final boolean m_optimizer;
    private final boolean m_incremental;
    private final boolean m_source_location;
    private boolean m_insideParse = false;
    private static boolean DEBUG = false;
    private TransformerImpl m_transformer;
    private String m_baseSystemID;
    private Result m_result = null;
    private Locator m_locator = null;
    private EntityResolver m_entityResolver = null;
    private DTDHandler m_dtdHandler = null;
    private ContentHandler m_contentHandler = null;
    private ErrorHandler m_errorHandler = null;
    private LexicalHandler m_lexicalHandler = null;
    private DeclHandler m_declHandler = null;
    DTM m_dtm;

    public TransformerHandlerImpl(TransformerImpl transformerImpl, boolean bl, String string) {
        DTM dTM;
        this.m_transformer = transformerImpl;
        this.m_baseSystemID = string;
        XPathContext xPathContext = transformerImpl.getXPathContext();
        this.m_dtm = dTM = xPathContext.getDTM(null, true, transformerImpl, true, true);
        dTM.setDocumentBaseURI(string);
        this.m_contentHandler = dTM.getContentHandler();
        this.m_dtdHandler = dTM.getDTDHandler();
        this.m_entityResolver = dTM.getEntityResolver();
        this.m_errorHandler = dTM.getErrorHandler();
        this.m_lexicalHandler = dTM.getLexicalHandler();
        this.m_incremental = transformerImpl.getIncremental();
        this.m_optimizer = transformerImpl.getOptimize();
        this.m_source_location = transformerImpl.getSource_location();
    }

    protected void clearCoRoutine() {
        this.clearCoRoutine(null);
    }

    protected void clearCoRoutine(SAXException sAXException) {
        if (null != sAXException) {
            this.m_transformer.setExceptionThrown(sAXException);
        }
        if (this.m_dtm instanceof SAX2DTM) {
            if (DEBUG) {
                System.err.println("In clearCoRoutine...");
            }
            try {
                SAX2DTM sAX2DTM = (SAX2DTM)this.m_dtm;
                if (null != this.m_contentHandler && this.m_contentHandler instanceof IncrementalSAXSource_Filter) {
                    IncrementalSAXSource_Filter incrementalSAXSource_Filter = (IncrementalSAXSource_Filter)this.m_contentHandler;
                    incrementalSAXSource_Filter.deliverMoreNodes(false);
                }
                sAX2DTM.clearCoRoutine(true);
                this.m_contentHandler = null;
                this.m_dtdHandler = null;
                this.m_entityResolver = null;
                this.m_errorHandler = null;
                this.m_lexicalHandler = null;
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if (DEBUG) {
                System.err.println("...exiting clearCoRoutine");
            }
        }
    }

    public void setResult(Result result) throws IllegalArgumentException {
        if (null == result) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_RESULT_NULL", null));
        }
        try {
            SerializationHandler serializationHandler = this.m_transformer.createSerializationHandler(result);
            this.m_transformer.setSerializationHandler(serializationHandler);
        }
        catch (TransformerException transformerException) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_RESULT_COULD_NOT_BE_SET", null));
        }
        this.m_result = result;
    }

    public void setSystemId(String string) {
        this.m_baseSystemID = string;
        this.m_dtm.setDocumentBaseURI(string);
    }

    public String getSystemId() {
        return this.m_baseSystemID;
    }

    public Transformer getTransformer() {
        return this.m_transformer;
    }

    public InputSource resolveEntity(String string, String string2) throws SAXException, IOException {
        if (this.m_entityResolver != null) {
            return this.m_entityResolver.resolveEntity(string, string2);
        }
        return null;
    }

    public void notationDecl(String string, String string2, String string3) throws SAXException {
        if (this.m_dtdHandler != null) {
            this.m_dtdHandler.notationDecl(string, string2, string3);
        }
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) throws SAXException {
        if (this.m_dtdHandler != null) {
            this.m_dtdHandler.unparsedEntityDecl(string, string2, string3, string4);
        }
    }

    public void setDocumentLocator(Locator locator) {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#setDocumentLocator: " + locator.getSystemId());
        }
        this.m_locator = locator;
        if (null == this.m_baseSystemID) {
            this.setSystemId(locator.getSystemId());
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.setDocumentLocator(locator);
        }
    }

    public void startDocument() throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#startDocument");
        }
        this.m_insideParse = true;
        if (this.m_contentHandler != null) {
            if (this.m_incremental) {
                this.m_transformer.setSourceTreeDocForThread(this.m_dtm.getDocument());
                int n2 = Thread.currentThread().getPriority();
                this.m_transformer.runTransformThread(n2);
            }
            this.m_contentHandler.startDocument();
        }
    }

    public void endDocument() throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#endDocument");
        }
        this.m_insideParse = false;
        if (this.m_contentHandler != null) {
            this.m_contentHandler.endDocument();
        }
        if (this.m_incremental) {
            this.m_transformer.waitTransformThread();
        } else {
            this.m_transformer.setSourceTreeDocForThread(this.m_dtm.getDocument());
            this.m_transformer.run();
        }
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#startPrefixMapping: " + string + ", " + string2);
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.startPrefixMapping(string, string2);
        }
    }

    public void endPrefixMapping(String string) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#endPrefixMapping: " + string);
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.endPrefixMapping(string);
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#startElement: " + string3);
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.startElement(string, string2, string3, attributes);
        }
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#endElement: " + string3);
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.endElement(string, string2, string3);
        }
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#characters: " + n2 + ", " + n3);
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.characters(arrc, n2, n3);
        }
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#ignorableWhitespace: " + n2 + ", " + n3);
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.ignorableWhitespace(arrc, n2, n3);
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#processingInstruction: " + string + ", " + string2);
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.processingInstruction(string, string2);
        }
    }

    public void skippedEntity(String string) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#skippedEntity: " + string);
        }
        if (this.m_contentHandler != null) {
            this.m_contentHandler.skippedEntity(string);
        }
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
        ErrorListener errorListener = this.m_transformer.getErrorListener();
        if (errorListener instanceof ErrorHandler) {
            ((ErrorHandler)((Object)errorListener)).warning(sAXParseException);
        } else {
            try {
                errorListener.warning(new TransformerException(sAXParseException));
            }
            catch (TransformerException transformerException) {
                throw sAXParseException;
            }
        }
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
        ErrorListener errorListener = this.m_transformer.getErrorListener();
        if (errorListener instanceof ErrorHandler) {
            ((ErrorHandler)((Object)errorListener)).error(sAXParseException);
            if (null != this.m_errorHandler) {
                this.m_errorHandler.error(sAXParseException);
            }
        } else {
            try {
                errorListener.error(new TransformerException(sAXParseException));
                if (null != this.m_errorHandler) {
                    this.m_errorHandler.error(sAXParseException);
                }
            }
            catch (TransformerException transformerException) {
                throw sAXParseException;
            }
        }
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        ErrorListener errorListener;
        if (null != this.m_errorHandler) {
            try {
                this.m_errorHandler.fatalError(sAXParseException);
            }
            catch (SAXParseException sAXParseException2) {
                // empty catch block
            }
        }
        if ((errorListener = this.m_transformer.getErrorListener()) instanceof ErrorHandler) {
            ((ErrorHandler)((Object)errorListener)).fatalError(sAXParseException);
            if (null != this.m_errorHandler) {
                this.m_errorHandler.fatalError(sAXParseException);
            }
        } else {
            try {
                errorListener.fatalError(new TransformerException(sAXParseException));
                if (null != this.m_errorHandler) {
                    this.m_errorHandler.fatalError(sAXParseException);
                }
            }
            catch (TransformerException transformerException) {
                throw sAXParseException;
            }
        }
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#startDTD: " + string + ", " + string2 + ", " + string3);
        }
        if (null != this.m_lexicalHandler) {
            this.m_lexicalHandler.startDTD(string, string2, string3);
        }
    }

    public void endDTD() throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#endDTD");
        }
        if (null != this.m_lexicalHandler) {
            this.m_lexicalHandler.endDTD();
        }
    }

    public void startEntity(String string) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#startEntity: " + string);
        }
        if (null != this.m_lexicalHandler) {
            this.m_lexicalHandler.startEntity(string);
        }
    }

    public void endEntity(String string) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#endEntity: " + string);
        }
        if (null != this.m_lexicalHandler) {
            this.m_lexicalHandler.endEntity(string);
        }
    }

    public void startCDATA() throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#startCDATA");
        }
        if (null != this.m_lexicalHandler) {
            this.m_lexicalHandler.startCDATA();
        }
    }

    public void endCDATA() throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#endCDATA");
        }
        if (null != this.m_lexicalHandler) {
            this.m_lexicalHandler.endCDATA();
        }
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#comment: " + n2 + ", " + n3);
        }
        if (null != this.m_lexicalHandler) {
            this.m_lexicalHandler.comment(arrc, n2, n3);
        }
    }

    public void elementDecl(String string, String string2) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#elementDecl: " + string + ", " + string2);
        }
        if (null != this.m_declHandler) {
            this.m_declHandler.elementDecl(string, string2);
        }
    }

    public void attributeDecl(String string, String string2, String string3, String string4, String string5) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#attributeDecl: " + string + ", " + string2 + ", etc...");
        }
        if (null != this.m_declHandler) {
            this.m_declHandler.attributeDecl(string, string2, string3, string4, string5);
        }
    }

    public void internalEntityDecl(String string, String string2) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#internalEntityDecl: " + string + ", " + string2);
        }
        if (null != this.m_declHandler) {
            this.m_declHandler.internalEntityDecl(string, string2);
        }
    }

    public void externalEntityDecl(String string, String string2, String string3) throws SAXException {
        if (DEBUG) {
            System.out.println("TransformerHandlerImpl#externalEntityDecl: " + string + ", " + string2 + ", " + string3);
        }
        if (null != this.m_declHandler) {
            this.m_declHandler.externalEntityDecl(string, string2, string3);
        }
    }
}

