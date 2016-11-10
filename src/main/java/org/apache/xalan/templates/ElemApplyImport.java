/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;

public class ElemApplyImport
extends ElemTemplateElement {
    static final long serialVersionUID = 3764728663373024038L;

    public int getXSLToken() {
        return 72;
    }

    public String getNodeName() {
        return "apply-imports";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        int n2;
        if (transformerImpl.currentTemplateRuleIsNull()) {
            transformerImpl.getMsgMgr().error(this, "ER_NO_APPLY_IMPORT_IN_FOR_EACH");
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        if (-1 != (n2 = transformerImpl.getXPathContext().getCurrentNode())) {
            ElemTemplate elemTemplate = transformerImpl.getMatchedTemplate();
            transformerImpl.applyTemplateToNode(this, elemTemplate, n2);
        } else {
            transformerImpl.getMsgMgr().error(this, "ER_NULL_SOURCENODE_APPLYIMPORTS");
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        this.error("ER_CANNOT_ADD", new Object[]{elemTemplateElement.getNodeName(), this.getNodeName()});
        return null;
    }
}

