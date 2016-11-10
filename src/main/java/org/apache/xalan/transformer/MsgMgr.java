/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.io.PrintStream;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.w3c.dom.Node;

public class MsgMgr {
    private TransformerImpl m_transformer;

    public MsgMgr(TransformerImpl transformerImpl) {
        this.m_transformer = transformerImpl;
    }

    public void message(SourceLocator sourceLocator, String string, boolean bl) throws TransformerException {
        ErrorListener errorListener = this.m_transformer.getErrorListener();
        if (null != errorListener) {
            errorListener.warning(new TransformerException(string, sourceLocator));
        } else {
            if (bl) {
                throw new TransformerException(string, sourceLocator);
            }
            System.out.println(string);
        }
    }

    public void warn(SourceLocator sourceLocator, String string) throws TransformerException {
        this.warn(sourceLocator, null, null, string, null);
    }

    public void warn(SourceLocator sourceLocator, String string, Object[] arrobject) throws TransformerException {
        this.warn(sourceLocator, null, null, string, arrobject);
    }

    public void warn(SourceLocator sourceLocator, Node node, Node node2, String string) throws TransformerException {
        this.warn(sourceLocator, node, node2, string, null);
    }

    public void warn(SourceLocator sourceLocator, Node node, Node node2, String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createWarning(string, arrobject);
        ErrorListener errorListener = this.m_transformer.getErrorListener();
        if (null != errorListener) {
            errorListener.warning(new TransformerException(string2, sourceLocator));
        } else {
            System.out.println(string2);
        }
    }

    public void error(SourceLocator sourceLocator, String string) throws TransformerException {
        this.error(sourceLocator, null, null, string, null);
    }

    public void error(SourceLocator sourceLocator, String string, Object[] arrobject) throws TransformerException {
        this.error(sourceLocator, null, null, string, arrobject);
    }

    public void error(SourceLocator sourceLocator, String string, Exception exception) throws TransformerException {
        this.error(sourceLocator, string, null, exception);
    }

    public void error(SourceLocator sourceLocator, String string, Object[] arrobject, Exception exception) throws TransformerException {
        String string2 = XSLMessages.createMessage(string, arrobject);
        ErrorListener errorListener = this.m_transformer.getErrorListener();
        if (null == errorListener) {
            throw new TransformerException(string2, sourceLocator);
        }
        errorListener.fatalError(new TransformerException(string2, sourceLocator));
    }

    public void error(SourceLocator sourceLocator, Node node, Node node2, String string) throws TransformerException {
        this.error(sourceLocator, node, node2, string, null);
    }

    public void error(SourceLocator sourceLocator, Node node, Node node2, String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createMessage(string, arrobject);
        ErrorListener errorListener = this.m_transformer.getErrorListener();
        if (null == errorListener) {
            throw new TransformerException(string2, sourceLocator);
        }
        errorListener.fatalError(new TransformerException(string2, sourceLocator));
    }
}

