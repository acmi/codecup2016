/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class ElemExsltFuncResult
extends ElemVariable {
    static final long serialVersionUID = -3478311949388304563L;
    private boolean m_isResultSet = false;
    private XObject m_result = null;
    private int m_callerFrameSize = 0;

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        XPathContext xPathContext = transformerImpl.getXPathContext();
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        if (transformerImpl.currentFuncResultSeen()) {
            throw new TransformerException("An EXSLT function cannot set more than one result!");
        }
        int n2 = xPathContext.getCurrentNode();
        XObject xObject = this.getValue(transformerImpl, n2);
        transformerImpl.popCurrentFuncResult();
        transformerImpl.pushCurrentFuncResult(xObject);
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    public int getXSLToken() {
        return 89;
    }

    public String getNodeName() {
        return "result";
    }
}

