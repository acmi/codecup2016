/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DefaultErrorHandler
implements ErrorListener,
ErrorHandler {
    PrintWriter m_pw;
    boolean m_throwExceptionOnError = true;

    public DefaultErrorHandler() {
        this(true);
    }

    public DefaultErrorHandler(boolean bl) {
        this.m_throwExceptionOnError = bl;
    }

    public PrintWriter getErrorWriter() {
        if (this.m_pw == null) {
            this.m_pw = new PrintWriter(System.err, true);
        }
        return this.m_pw;
    }

    public void warning(SAXParseException sAXParseException) throws SAXException {
        PrintWriter printWriter = this.getErrorWriter();
        DefaultErrorHandler.printLocation(printWriter, sAXParseException);
        printWriter.println("Parser warning: " + sAXParseException.getMessage());
    }

    public void error(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    public void warning(TransformerException transformerException) throws TransformerException {
        PrintWriter printWriter = this.getErrorWriter();
        DefaultErrorHandler.printLocation(printWriter, transformerException);
        printWriter.println(transformerException.getMessage());
    }

    public void error(TransformerException transformerException) throws TransformerException {
        if (this.m_throwExceptionOnError) {
            throw transformerException;
        }
        PrintWriter printWriter = this.getErrorWriter();
        DefaultErrorHandler.printLocation(printWriter, transformerException);
        printWriter.println(transformerException.getMessage());
    }

    public void fatalError(TransformerException transformerException) throws TransformerException {
        if (this.m_throwExceptionOnError) {
            throw transformerException;
        }
        PrintWriter printWriter = this.getErrorWriter();
        DefaultErrorHandler.printLocation(printWriter, transformerException);
        printWriter.println(transformerException.getMessage());
    }

    public static void printLocation(PrintWriter printWriter, Throwable throwable) {
        Object object;
        SourceLocator sourceLocator = null;
        Throwable throwable2 = throwable;
        do {
            if (throwable2 instanceof SAXParseException) {
                sourceLocator = new SAXSourceLocator((SAXParseException)throwable2);
            } else if (throwable2 instanceof TransformerException && null != (object = ((TransformerException)throwable2).getLocator())) {
                sourceLocator = object;
            }
            throwable2 = throwable2 instanceof TransformerException ? ((TransformerException)throwable2).getCause() : (throwable2 instanceof WrappedRuntimeException ? ((WrappedRuntimeException)throwable2).getException() : (throwable2 instanceof SAXException ? ((SAXException)throwable2).getException() : null));
        } while (null != throwable2);
        if (null != sourceLocator) {
            object = null != sourceLocator.getPublicId() ? sourceLocator.getPublicId() : (null != sourceLocator.getSystemId() ? sourceLocator.getSystemId() : XMLMessages.createXMLMessage("ER_SYSTEMID_UNKNOWN", null));
            printWriter.print((String)object + "; " + XMLMessages.createXMLMessage("line", null) + sourceLocator.getLineNumber() + "; " + XMLMessages.createXMLMessage("column", null) + sourceLocator.getColumnNumber() + "; ");
        } else {
            printWriter.print("(" + XMLMessages.createXMLMessage("ER_LOCATION_UNKNOWN", null) + ")");
        }
    }
}

