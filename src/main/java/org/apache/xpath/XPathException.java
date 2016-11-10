/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.w3c.dom.Node;

public class XPathException
extends TransformerException {
    Object m_styleNode = null;
    protected Exception m_exception;
    static Class class$java$lang$Throwable;

    public void setStylesheetNode(Object object) {
        this.m_styleNode = object;
    }

    public XPathException(String string, ExpressionNode expressionNode) {
        super(string);
        this.setLocator(expressionNode);
        this.setStylesheetNode(this.getStylesheetNode(expressionNode));
    }

    public XPathException(String string) {
        super(string);
    }

    public Node getStylesheetNode(ExpressionNode expressionNode) {
        ExpressionNode expressionNode2 = this.getExpressionOwner(expressionNode);
        if (null != expressionNode2 && expressionNode2 instanceof Node) {
            return (Node)((Object)expressionNode2);
        }
        return null;
    }

    protected ExpressionNode getExpressionOwner(ExpressionNode expressionNode) {
        ExpressionNode expressionNode2;
        for (expressionNode2 = expressionNode.exprGetParent(); null != expressionNode2 && expressionNode2 instanceof Expression; expressionNode2 = expressionNode2.exprGetParent()) {
        }
        return expressionNode2;
    }

    public void printStackTrace(PrintStream printStream) {
        if (printStream == null) {
            printStream = System.err;
        }
        try {
            super.printStackTrace(printStream);
        }
        catch (Exception exception) {
            // empty catch block
        }
        Throwable throwable = this.m_exception;
        for (int i2 = 0; i2 < 10 && null != throwable; ++i2) {
            printStream.println("---------");
            throwable.printStackTrace(printStream);
            if (throwable instanceof TransformerException) {
                TransformerException transformerException = (TransformerException)throwable;
                Exception exception = throwable;
                throwable = transformerException.getException();
                if (exception != throwable) continue;
                break;
            }
            throwable = null;
        }
    }

    public String getMessage() {
        String string = super.getMessage();
        Throwable throwable = this.m_exception;
        while (null != throwable) {
            String string2 = throwable.getMessage();
            if (null != string2) {
                string = string2;
            }
            if (throwable instanceof TransformerException) {
                TransformerException transformerException = (TransformerException)throwable;
                Exception exception = throwable;
                throwable = transformerException.getException();
                if (exception != throwable) continue;
                break;
            }
            throwable = null;
        }
        return null != string ? string : "";
    }

    public void printStackTrace(PrintWriter printWriter) {
        if (printWriter == null) {
            printWriter = new PrintWriter(System.err);
        }
        try {
            super.printStackTrace(printWriter);
        }
        catch (Exception exception) {
            // empty catch block
        }
        boolean bl = false;
        try {
            Class class_ = class$java$lang$Throwable == null ? (XPathException.class$java$lang$Throwable = XPathException.class$("java.lang.Throwable")) : class$java$lang$Throwable;
            class_.getMethod("getCause", null);
            bl = true;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        if (!bl) {
            Throwable throwable = this.m_exception;
            for (int i2 = 0; i2 < 10 && null != throwable; ++i2) {
                printWriter.println("---------");
                try {
                    throwable.printStackTrace(printWriter);
                }
                catch (Exception exception) {
                    printWriter.println("Could not print stack trace...");
                }
                if (throwable instanceof TransformerException) {
                    TransformerException transformerException = (TransformerException)throwable;
                    Exception exception = throwable;
                    throwable = transformerException.getException();
                    if (exception != throwable) continue;
                    throwable = null;
                    break;
                }
                throwable = null;
            }
        }
    }

    public Throwable getException() {
        return this.m_exception;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

