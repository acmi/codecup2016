/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemWhen;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class ElemChoose
extends ElemTemplateElement {
    static final long serialVersionUID = -3070117361903102033L;

    public int getXSLToken() {
        return 37;
    }

    public String getNodeName() {
        return "choose";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        boolean bl = false;
        for (ElemTemplateElement elemTemplateElement = this.getFirstChildElem(); elemTemplateElement != null; elemTemplateElement = elemTemplateElement.getNextSiblingElem()) {
            int n2 = elemTemplateElement.getXSLToken();
            if (38 == n2) {
                bl = true;
                ElemWhen elemWhen = (ElemWhen)elemTemplateElement;
                XPathContext xPathContext = transformerImpl.getXPathContext();
                int n3 = xPathContext.getCurrentNode();
                if (transformerImpl.getDebug()) {
                    XObject xObject = elemWhen.getTest().execute(xPathContext, n3, (PrefixResolver)elemWhen);
                    if (transformerImpl.getDebug()) {
                        transformerImpl.getTraceManager().fireSelectedEvent(n3, elemWhen, "test", elemWhen.getTest(), xObject);
                    }
                    if (!xObject.bool()) continue;
                    transformerImpl.getTraceManager().fireTraceEvent(elemWhen);
                    transformerImpl.executeChildTemplates((ElemTemplateElement)elemWhen, true);
                    transformerImpl.getTraceManager().fireTraceEndEvent(elemWhen);
                    return;
                }
                if (!elemWhen.getTest().bool(xPathContext, n3, elemWhen)) continue;
                transformerImpl.executeChildTemplates((ElemTemplateElement)elemWhen, true);
                return;
            }
            if (39 != n2) continue;
            bl = true;
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEvent(elemTemplateElement);
            }
            transformerImpl.executeChildTemplates(elemTemplateElement, true);
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(elemTemplateElement);
            }
            return;
        }
        if (!bl) {
            transformerImpl.getMsgMgr().error(this, "ER_CHOOSE_REQUIRES_WHEN");
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        switch (n2) {
            case 38: 
            case 39: {
                break;
            }
            default: {
                this.error("ER_CANNOT_ADD", new Object[]{elemTemplateElement.getNodeName(), this.getNodeName()});
            }
        }
        return super.appendChild(elemTemplateElement);
    }

    public boolean canAcceptVariables() {
        return false;
    }
}

