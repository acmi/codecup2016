/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import org.apache.xml.dtm.ref.CoroutineManager;
import org.apache.xml.dtm.ref.IncrementalSAXSource;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.ThreadControllerWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

public class IncrementalSAXSource_Filter
implements Runnable,
IncrementalSAXSource,
ContentHandler,
DTDHandler,
ErrorHandler,
LexicalHandler {
    boolean DEBUG = false;
    private CoroutineManager fCoroutineManager = null;
    private int fControllerCoroutineID = -1;
    private int fSourceCoroutineID = -1;
    private ContentHandler clientContentHandler = null;
    private LexicalHandler clientLexicalHandler = null;
    private DTDHandler clientDTDHandler = null;
    private ErrorHandler clientErrorHandler = null;
    private int eventcounter;
    private int frequency = 5;
    private boolean fNoMoreEvents = false;
    private XMLReader fXMLReader = null;
    private InputSource fXMLReaderInputSource = null;

    public IncrementalSAXSource_Filter() {
        this.init(new CoroutineManager(), -1, -1);
    }

    public void init(CoroutineManager coroutineManager, int n2, int n3) {
        if (coroutineManager == null) {
            coroutineManager = new CoroutineManager();
        }
        this.fCoroutineManager = coroutineManager;
        this.fControllerCoroutineID = coroutineManager.co_joinCoroutineSet(n2);
        this.fSourceCoroutineID = coroutineManager.co_joinCoroutineSet(n3);
        if (this.fControllerCoroutineID == -1 || this.fSourceCoroutineID == -1) {
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_COJOINROUTINESET_FAILED", null));
        }
        this.fNoMoreEvents = false;
        this.eventcounter = this.frequency;
    }

    public void setXMLReader(XMLReader xMLReader) {
        this.fXMLReader = xMLReader;
        xMLReader.setContentHandler(this);
        xMLReader.setDTDHandler(this);
        xMLReader.setErrorHandler(this);
        try {
            xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
        }
        catch (SAXNotRecognizedException sAXNotRecognizedException) {
        }
        catch (SAXNotSupportedException sAXNotSupportedException) {
            // empty catch block
        }
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.clientContentHandler = contentHandler;
    }

    public void setDTDHandler(DTDHandler dTDHandler) {
        this.clientDTDHandler = dTDHandler;
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        this.clientLexicalHandler = lexicalHandler;
    }

    public void characters(char[] arrc, int n2, int n3) throws SAXException {
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.characters(arrc, n2, n3);
        }
    }

    public void endDocument() throws SAXException {
        if (this.clientContentHandler != null) {
            this.clientContentHandler.endDocument();
        }
        this.eventcounter = 0;
        this.co_yield(false);
    }

    public void endElement(String string, String string2, String string3) throws SAXException {
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.endElement(string, string2, string3);
        }
    }

    public void endPrefixMapping(String string) throws SAXException {
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.endPrefixMapping(string);
        }
    }

    public void ignorableWhitespace(char[] arrc, int n2, int n3) throws SAXException {
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.ignorableWhitespace(arrc, n2, n3);
        }
    }

    public void processingInstruction(String string, String string2) throws SAXException {
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.processingInstruction(string, string2);
        }
    }

    public void setDocumentLocator(Locator locator) {
        if (--this.eventcounter <= 0) {
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.setDocumentLocator(locator);
        }
    }

    public void skippedEntity(String string) throws SAXException {
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.skippedEntity(string);
        }
    }

    public void startDocument() throws SAXException {
        this.co_entry_pause();
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.startDocument();
        }
    }

    public void startElement(String string, String string2, String string3, Attributes attributes) throws SAXException {
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.startElement(string, string2, string3, attributes);
        }
    }

    public void startPrefixMapping(String string, String string2) throws SAXException {
        if (--this.eventcounter <= 0) {
            this.co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.startPrefixMapping(string, string2);
        }
    }

    public void comment(char[] arrc, int n2, int n3) throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.comment(arrc, n2, n3);
        }
    }

    public void endCDATA() throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.endCDATA();
        }
    }

    public void endDTD() throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.endDTD();
        }
    }

    public void endEntity(String string) throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.endEntity(string);
        }
    }

    public void startCDATA() throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.startCDATA();
        }
    }

    public void startDTD(String string, String string2, String string3) throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.startDTD(string, string2, string3);
        }
    }

    public void startEntity(String string) throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.startEntity(string);
        }
    }

    public void notationDecl(String string, String string2, String string3) throws SAXException {
        if (null != this.clientDTDHandler) {
            this.clientDTDHandler.notationDecl(string, string2, string3);
        }
    }

    public void unparsedEntityDecl(String string, String string2, String string3, String string4) throws SAXException {
        if (null != this.clientDTDHandler) {
            this.clientDTDHandler.unparsedEntityDecl(string, string2, string3, string4);
        }
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
        if (null != this.clientErrorHandler) {
            this.clientErrorHandler.error(sAXParseException);
        }
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        if (null != this.clientErrorHandler) {
            this.clientErrorHandler.error(sAXParseException);
        }
        this.eventcounter = 0;
        this.co_yield(false);
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
        if (null != this.clientErrorHandler) {
            this.clientErrorHandler.error(sAXParseException);
        }
    }

    private void co_entry_pause() throws SAXException {
        if (this.fCoroutineManager == null) {
            this.init(null, -1, -1);
        }
        try {
            Object object = this.fCoroutineManager.co_entry_pause(this.fSourceCoroutineID);
            if (object == Boolean.FALSE) {
                this.co_yield(false);
            }
        }
        catch (NoSuchMethodException noSuchMethodException) {
            if (this.DEBUG) {
                noSuchMethodException.printStackTrace();
            }
            throw new SAXException(noSuchMethodException);
        }
    }

    private void co_yield(boolean bl) throws SAXException {
        if (this.fNoMoreEvents) {
            return;
        }
        try {
            Object object = Boolean.FALSE;
            if (bl) {
                object = this.fCoroutineManager.co_resume(Boolean.TRUE, this.fSourceCoroutineID, this.fControllerCoroutineID);
            }
            if (object == Boolean.FALSE) {
                this.fNoMoreEvents = true;
                if (this.fXMLReader != null) {
                    throw new StopException();
                }
                this.fCoroutineManager.co_exit_to(Boolean.FALSE, this.fSourceCoroutineID, this.fControllerCoroutineID);
            }
        }
        catch (NoSuchMethodException noSuchMethodException) {
            this.fNoMoreEvents = true;
            this.fCoroutineManager.co_exit(this.fSourceCoroutineID);
            throw new SAXException(noSuchMethodException);
        }
    }

    public void startParse(InputSource inputSource) throws SAXException {
        if (this.fNoMoreEvents) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", null));
        }
        if (this.fXMLReader == null) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_XMLRDR_NOT_BEFORE_STARTPARSE", null));
        }
        this.fXMLReaderInputSource = inputSource;
        ThreadControllerWrapper.runThread(this, -1);
    }

    public void run() {
        if (this.fXMLReader == null) {
            return;
        }
        if (this.DEBUG) {
            System.out.println("IncrementalSAXSource_Filter parse thread launched");
        }
        Serializable serializable = Boolean.FALSE;
        try {
            this.fXMLReader.parse(this.fXMLReaderInputSource);
        }
        catch (IOException iOException) {
            serializable = iOException;
        }
        catch (StopException stopException) {
            if (this.DEBUG) {
                System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
            }
        }
        catch (SAXException sAXException) {
            Exception exception = sAXException.getException();
            if (exception instanceof StopException) {
                if (this.DEBUG) {
                    System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
                }
            }
            if (this.DEBUG) {
                System.out.println("Active IncrementalSAXSource_Filter UNEXPECTED SAX exception: " + exception);
                exception.printStackTrace();
            }
            serializable = sAXException;
        }
        this.fXMLReader = null;
        try {
            this.fNoMoreEvents = true;
            this.fCoroutineManager.co_exit_to(serializable, this.fSourceCoroutineID, this.fControllerCoroutineID);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace(System.err);
            this.fCoroutineManager.co_exit(this.fSourceCoroutineID);
        }
    }

    public Object deliverMoreNodes(boolean bl) {
        if (this.fNoMoreEvents) {
            return Boolean.FALSE;
        }
        try {
            Object object = this.fCoroutineManager.co_resume(bl ? Boolean.TRUE : Boolean.FALSE, this.fControllerCoroutineID, this.fSourceCoroutineID);
            if (object == Boolean.FALSE) {
                this.fCoroutineManager.co_exit(this.fControllerCoroutineID);
            }
            return object;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return noSuchMethodException;
        }
    }

    static class StopException
    extends RuntimeException {
        StopException() {
        }
    }

}

