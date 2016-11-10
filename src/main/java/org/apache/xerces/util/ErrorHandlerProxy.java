/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class ErrorHandlerProxy
implements ErrorHandler {
    public void error(SAXParseException sAXParseException) throws SAXException {
        XMLErrorHandler xMLErrorHandler = this.getErrorHandler();
        if (xMLErrorHandler instanceof ErrorHandlerWrapper) {
            ((ErrorHandlerWrapper)xMLErrorHandler).fErrorHandler.error(sAXParseException);
        } else {
            xMLErrorHandler.error("", "", ErrorHandlerWrapper.createXMLParseException(sAXParseException));
        }
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        XMLErrorHandler xMLErrorHandler = this.getErrorHandler();
        if (xMLErrorHandler instanceof ErrorHandlerWrapper) {
            ((ErrorHandlerWrapper)xMLErrorHandler).fErrorHandler.fatalError(sAXParseException);
        } else {
            xMLErrorHandler.fatalError("", "", ErrorHandlerWrapper.createXMLParseException(sAXParseException));
        }
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
        XMLErrorHandler xMLErrorHandler = this.getErrorHandler();
        if (xMLErrorHandler instanceof ErrorHandlerWrapper) {
            ((ErrorHandlerWrapper)xMLErrorHandler).fErrorHandler.warning(sAXParseException);
        } else {
            xMLErrorHandler.warning("", "", ErrorHandlerWrapper.createXMLParseException(sAXParseException));
        }
    }

    protected abstract XMLErrorHandler getErrorHandler();
}
