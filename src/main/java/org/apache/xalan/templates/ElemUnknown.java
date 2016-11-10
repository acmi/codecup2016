/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemFallback;
import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;

public class ElemUnknown
extends ElemLiteralResult {
    static final long serialVersionUID = -4573981712648730168L;

    public int getXSLToken() {
        return -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void executeFallbacks(TransformerImpl transformerImpl) throws TransformerException {
        ElemTemplateElement elemTemplateElement = this.m_firstChild;
        while (elemTemplateElement != null) {
            if (elemTemplateElement.getXSLToken() == 57) {
                java.lang.Object var4_3;
                try {
                    transformerImpl.pushElemTemplateElement(elemTemplateElement);
                    ((ElemFallback)elemTemplateElement).executeFallback(transformerImpl);
                    var4_3 = null;
                    transformerImpl.popElemTemplateElement();
                }
                catch (Throwable throwable) {
                    var4_3 = null;
                    transformerImpl.popElemTemplateElement();
                    throw throwable;
                }
            }
            elemTemplateElement = elemTemplateElement.m_nextSibling;
        }
    }

    private boolean hasFallbackChildren() {
        ElemTemplateElement elemTemplateElement = this.m_firstChild;
        while (elemTemplateElement != null) {
            if (elemTemplateElement.getXSLToken() == 57) {
                return true;
            }
            elemTemplateElement = elemTemplateElement.m_nextSibling;
        }
        return false;
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        try {
            if (this.hasFallbackChildren()) {
                this.executeFallbacks(transformerImpl);
            }
        }
        catch (TransformerException transformerException) {
            transformerImpl.getErrorListener().fatalError(transformerException);
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }
}

