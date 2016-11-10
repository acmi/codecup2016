/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.xml.sax.SAXException;

public class ElemComment
extends ElemTemplateElement {
    static final long serialVersionUID = -8813199122875770142L;

    public int getXSLToken() {
        return 59;
    }

    public String getNodeName() {
        return "comment";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        try {
            try {
                String string = transformerImpl.transformToString(this);
                transformerImpl.getResultTreeHandler().comment(string);
            }
            catch (SAXException sAXException) {
                throw new TransformerException(sAXException);
            }
            Object var4_4 = null;
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
        }
        catch (Throwable throwable) {
            Object var4_5 = null;
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
            throw throwable;
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        switch (n2) {
            case 9: 
            case 17: 
            case 28: 
            case 30: 
            case 35: 
            case 36: 
            case 37: 
            case 42: 
            case 50: 
            case 72: 
            case 73: 
            case 74: 
            case 75: 
            case 78: {
                break;
            }
            default: {
                this.error("ER_CANNOT_ADD", new Object[]{elemTemplateElement.getNodeName(), this.getNodeName()});
            }
        }
        return super.appendChild(elemTemplateElement);
    }
}

