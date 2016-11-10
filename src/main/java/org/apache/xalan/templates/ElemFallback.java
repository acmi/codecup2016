/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.PrintStream;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;

public class ElemFallback
extends ElemTemplateElement {
    static final long serialVersionUID = 1782962139867340703L;

    public int getXSLToken() {
        return 57;
    }

    public String getNodeName() {
        return "fallback";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
    }

    public void executeFallback(TransformerImpl transformerImpl) throws TransformerException {
        int n2 = this.m_parentNode.getXSLToken();
        if (79 == n2 || -1 == n2) {
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEvent(this);
            }
            transformerImpl.executeChildTemplates((ElemTemplateElement)this, true);
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
        } else {
            System.out.println("Error!  parent of xsl:fallback must be an extension or unknown element!");
        }
    }
}

