/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;

public class ElemMessage
extends ElemTemplateElement {
    static final long serialVersionUID = 1530472462155060023L;
    private boolean m_terminate = false;

    public void setTerminate(boolean bl) {
        this.m_terminate = bl;
    }

    public boolean getTerminate() {
        return this.m_terminate;
    }

    public int getXSLToken() {
        return 75;
    }

    public String getNodeName() {
        return "message";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        String string = transformerImpl.transformToString(this);
        transformerImpl.getMsgMgr().message(this, string, this.m_terminate);
        if (this.m_terminate) {
            transformerImpl.getErrorListener().fatalError(new TransformerException(XSLMessages.createMessage("ER_STYLESHEET_DIRECTED_TERMINATION", null)));
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }
}

