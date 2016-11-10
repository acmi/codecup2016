/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ErrorHandlerWrapper
implements XMLErrorHandler {
    protected ErrorHandler fErrorHandler;

    public ErrorHandlerWrapper() {
    }

    public ErrorHandlerWrapper(ErrorHandler errorHandler) {
        this.setErrorHandler(errorHandler);
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.fErrorHandler = errorHandler;
    }

    public ErrorHandler getErrorHandler() {
        return this.fErrorHandler;
    }

    public void warning(String string, String string2, XMLParseException xMLParseException) throws XNIException {
        if (this.fErrorHandler != null) {
            SAXParseException sAXParseException = ErrorHandlerWrapper.createSAXParseException(xMLParseException);
            try {
                this.fErrorHandler.warning(sAXParseException);
            }
            catch (SAXParseException sAXParseException2) {
                throw ErrorHandlerWrapper.createXMLParseException(sAXParseException2);
            }
            catch (SAXException sAXException) {
                throw ErrorHandlerWrapper.createXNIException(sAXException);
            }
        }
    }

    public void error(String string, String string2, XMLParseException xMLParseException) throws XNIException {
        if (this.fErrorHandler != null) {
            SAXParseException sAXParseException = ErrorHandlerWrapper.createSAXParseException(xMLParseException);
            try {
                this.fErrorHandler.error(sAXParseException);
            }
            catch (SAXParseException sAXParseException2) {
                throw ErrorHandlerWrapper.createXMLParseException(sAXParseException2);
            }
            catch (SAXException sAXException) {
                throw ErrorHandlerWrapper.createXNIException(sAXException);
            }
        }
    }

    public void fatalError(String string, String string2, XMLParseException xMLParseException) throws XNIException {
        if (this.fErrorHandler != null) {
            SAXParseException sAXParseException = ErrorHandlerWrapper.createSAXParseException(xMLParseException);
            try {
                this.fErrorHandler.fatalError(sAXParseException);
            }
            catch (SAXParseException sAXParseException2) {
                throw ErrorHandlerWrapper.createXMLParseException(sAXParseException2);
            }
            catch (SAXException sAXException) {
                throw ErrorHandlerWrapper.createXNIException(sAXException);
            }
        }
    }

    protected static SAXParseException createSAXParseException(XMLParseException xMLParseException) {
        return new SAXParseException(xMLParseException.getMessage(), xMLParseException.getPublicId(), xMLParseException.getExpandedSystemId(), xMLParseException.getLineNumber(), xMLParseException.getColumnNumber(), xMLParseException.getException());
    }

    protected static XMLParseException createXMLParseException(SAXParseException sAXParseException) {
        String string = sAXParseException.getPublicId();
        String string2 = sAXParseException.getSystemId();
        int n2 = sAXParseException.getLineNumber();
        int n3 = sAXParseException.getColumnNumber();
        XMLLocator xMLLocator = new XMLLocator(string, string2, n3, n2){
            private final String val$fPublicId;
            private final String val$fExpandedSystemId;
            private final int val$fColumnNumber;
            private final int val$fLineNumber;

            public String getPublicId() {
                return this.val$fPublicId;
            }

            public String getExpandedSystemId() {
                return this.val$fExpandedSystemId;
            }

            public String getBaseSystemId() {
                return null;
            }

            public String getLiteralSystemId() {
                return null;
            }

            public int getColumnNumber() {
                return this.val$fColumnNumber;
            }

            public int getLineNumber() {
                return this.val$fLineNumber;
            }

            public int getCharacterOffset() {
                return -1;
            }

            public String getEncoding() {
                return null;
            }

            public String getXMLVersion() {
                return null;
            }
        };
        return new XMLParseException(xMLLocator, sAXParseException.getMessage(), sAXParseException);
    }

    protected static XNIException createXNIException(SAXException sAXException) {
        return new XNIException(sAXException.getMessage(), sAXException);
    }

}

