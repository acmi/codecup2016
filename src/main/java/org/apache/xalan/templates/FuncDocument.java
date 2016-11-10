/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.SourceTreeManager;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function2Args;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class FuncDocument
extends Function2Args {
    static final long serialVersionUID = 2483304325971281424L;

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        Object object;
        XObject xObject;
        int n2 = xPathContext.getCurrentNode();
        DTM dTM = xPathContext.getDTM(n2);
        int n3 = dTM.getDocumentRoot(n2);
        XObject xObject2 = this.getArg0().execute(xPathContext);
        String string = "";
        Expression expression = this.getArg1();
        if (null != expression) {
            xObject = expression.execute(xPathContext);
            if (4 == xObject.getType()) {
                int n4 = xObject.iter().nextNode();
                if (n4 == -1) {
                    this.warn(xPathContext, "WG_EMPTY_SECOND_ARG", null);
                    XNodeSet xNodeSet = new XNodeSet(xPathContext.getDTMManager());
                    return xNodeSet;
                }
                object = xPathContext.getDTM(n4);
                string = object.getDocumentBaseURI();
            } else {
                xObject.iter();
            }
        } else {
            this.assertion(null != xPathContext.getNamespaceContext(), "Namespace context can not be null!");
            string = xPathContext.getNamespaceContext().getBaseIdentifier();
        }
        xObject = new XNodeSet(xPathContext.getDTMManager());
        NodeSetDTM nodeSetDTM = xObject.mutableNodeset();
        object = 4 == xObject2.getType() ? xObject2.iter() : null;
        int n5 = -1;
        while (null == object || -1 != (n5 = object.nextNode())) {
            int n6;
            XMLString xMLString;
            XMLString xMLString2 = xMLString = null != object ? xPathContext.getDTM(n5).getStringValue(n5) : xObject2.xstr();
            if (null == expression && -1 != n5) {
                DTM dTM2 = xPathContext.getDTM(n5);
                string = dTM2.getDocumentBaseURI();
            }
            if (null == xMLString) continue;
            if (-1 == n3) {
                this.error(xPathContext, "ER_NO_CONTEXT_OWNERDOC", null);
            }
            int n7 = xMLString.indexOf(58);
            int n8 = xMLString.indexOf(47);
            if (n7 != -1 && n8 != -1 && n7 < n8) {
                string = null;
            }
            if (-1 != (n6 = this.getDoc(xPathContext, n2, xMLString.toString(), string)) && !nodeSetDTM.contains(n6)) {
                nodeSetDTM.addElement(n6);
            }
            if (null != object && n6 != -1) continue;
            break;
        }
        return xObject;
    }

    int getDoc(XPathContext xPathContext, int n2, String string, String string2) throws TransformerException {
        int n3;
        Source source;
        SourceTreeManager sourceTreeManager = xPathContext.getSourceTreeManager();
        try {
            source = sourceTreeManager.resolveURI(string2, string, xPathContext.getSAXLocator());
            n3 = sourceTreeManager.getNode(source);
        }
        catch (IOException iOException) {
            throw new TransformerException(iOException.getMessage(), xPathContext.getSAXLocator(), iOException);
        }
        catch (TransformerException transformerException) {
            throw new TransformerException(transformerException);
        }
        if (-1 != n3) {
            return n3;
        }
        if (string.length() == 0) {
            string = xPathContext.getNamespaceContext().getBaseIdentifier();
            try {
                source = sourceTreeManager.resolveURI(string2, string, xPathContext.getSAXLocator());
            }
            catch (IOException iOException) {
                throw new TransformerException(iOException.getMessage(), xPathContext.getSAXLocator(), iOException);
            }
        }
        String string3 = null;
        try {
            if (null != string && string.length() > 0) {
                n3 = sourceTreeManager.getSourceTree(source, xPathContext.getSAXLocator(), xPathContext);
            } else {
                Object[] arrobject = new Object[1];
                arrobject[0] = (string2 == null ? "" : string2) + string;
                this.warn(xPathContext, "WG_CANNOT_MAKE_URL_FROM", arrobject);
            }
        }
        catch (Throwable throwable) {
            Exception exception;
            n3 = -1;
            while (exception instanceof WrappedRuntimeException) {
                exception = ((WrappedRuntimeException)exception).getException();
            }
            if (exception instanceof NullPointerException || exception instanceof ClassCastException) {
                throw new WrappedRuntimeException(exception);
            }
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            if (exception instanceof TransformerException) {
                TransformerException transformerException;
                Throwable throwable2 = transformerException = (TransformerException)exception;
                while (null != throwable2) {
                    if (null != throwable2.getMessage()) {
                        printWriter.println(" (" + throwable2.getClass().getName() + "): " + throwable2.getMessage());
                    }
                    if (throwable2 instanceof TransformerException) {
                        TransformerException transformerException2 = throwable2;
                        SourceLocator sourceLocator = transformerException2.getLocator();
                        if (null != sourceLocator && null != sourceLocator.getSystemId()) {
                            printWriter.println("   ID: " + sourceLocator.getSystemId() + " Line #" + sourceLocator.getLineNumber() + " Column #" + sourceLocator.getColumnNumber());
                        }
                        if (!((throwable2 = transformerException2.getException()) instanceof WrappedRuntimeException)) continue;
                        throwable2 = ((WrappedRuntimeException)throwable2).getException();
                        continue;
                    }
                    throwable2 = null;
                }
            } else {
                printWriter.println(" (" + exception.getClass().getName() + "): " + exception.getMessage());
            }
            string3 = exception.getMessage();
        }
        if (-1 == n3) {
            if (null != string3) {
                this.warn(xPathContext, "WG_CANNOT_LOAD_REQUESTED_DOC", new Object[]{string3});
            } else {
                Object[] arrobject = new Object[1];
                arrobject[0] = string == null ? (string2 == null ? "" : string2) + string : string.toString();
                this.warn(xPathContext, "WG_CANNOT_LOAD_REQUESTED_DOC", arrobject);
            }
        }
        return n3;
    }

    public void error(XPathContext xPathContext, String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createMessage(string, arrobject);
        ErrorListener errorListener = xPathContext.getErrorListener();
        TransformerException transformerException = new TransformerException(string2, xPathContext.getSAXLocator());
        if (null != errorListener) {
            errorListener.error(transformerException);
        } else {
            System.out.println(string2);
        }
    }

    public void warn(XPathContext xPathContext, String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createWarning(string, arrobject);
        ErrorListener errorListener = xPathContext.getErrorListener();
        TransformerException transformerException = new TransformerException(string2, xPathContext.getSAXLocator());
        if (null != errorListener) {
            errorListener.warning(transformerException);
        } else {
            System.out.println(string2);
        }
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
        if (n2 < 1 || n2 > 2) {
            this.reportWrongNumberArgs();
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createMessage("ER_ONE_OR_TWO", null));
    }

    public boolean isNodesetExpr() {
        return true;
    }
}

