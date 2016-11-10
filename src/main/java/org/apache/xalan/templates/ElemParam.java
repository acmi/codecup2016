/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class ElemParam
extends ElemVariable {
    static final long serialVersionUID = -1131781475589006431L;
    int m_qnameID;

    public ElemParam() {
    }

    public int getXSLToken() {
        return 41;
    }

    public String getNodeName() {
        return "param";
    }

    public ElemParam(ElemParam elemParam) throws TransformerException {
        super(elemParam);
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        this.m_qnameID = stylesheetRoot.getComposeState().getQNameID(this.m_qname);
        int n2 = this.m_parentNode.getXSLToken();
        if (n2 == 19 || n2 == 88) {
            ++((org.apache.xalan.templates.ElemTemplate)this.m_parentNode).m_inArgsSize;
        }
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        VariableStack variableStack;
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        if (!(variableStack = transformerImpl.getXPathContext().getVarStack()).isLocalSet(this.m_index)) {
            int n2 = transformerImpl.getXPathContext().getCurrentNode();
            XObject xObject = this.getValue(transformerImpl, n2);
            transformerImpl.getXPathContext().getVarStack().setLocalVariable(this.m_index, xObject);
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }
}

