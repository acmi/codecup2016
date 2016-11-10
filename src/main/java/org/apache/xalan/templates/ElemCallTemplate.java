/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemForEach;
import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemWithParam;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class ElemCallTemplate
extends ElemForEach {
    static final long serialVersionUID = 5009634612916030591L;
    public QName m_templateName = null;
    private ElemTemplate m_template = null;
    protected ElemWithParam[] m_paramElems = null;

    public void setName(QName qName) {
        this.m_templateName = qName;
    }

    public QName getName() {
        return this.m_templateName;
    }

    public int getXSLToken() {
        return 17;
    }

    public String getNodeName() {
        return "call-template";
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        ElemWithParam elemWithParam;
        int n2;
        super.compose(stylesheetRoot);
        int n3 = this.getParamElemCount();
        for (n2 = 0; n2 < n3; ++n2) {
            elemWithParam = this.getParamElem(n2);
            elemWithParam.compose(stylesheetRoot);
        }
        if (null != this.m_templateName && null == this.m_template) {
            this.m_template = this.getStylesheetRoot().getTemplateComposed(this.m_templateName);
            if (null == this.m_template) {
                String string = XSLMessages.createMessage("ER_ELEMTEMPLATEELEM_ERR", new Object[]{this.m_templateName});
                throw new TransformerException(string, this);
            }
            n3 = this.getParamElemCount();
            for (n2 = 0; n2 < n3; ++n2) {
                elemWithParam = this.getParamElem(n2);
                elemWithParam.m_index = -1;
                int n4 = 0;
                for (ElemTemplateElement elemTemplateElement = this.m_template.getFirstChildElem(); null != elemTemplateElement && elemTemplateElement.getXSLToken() == 41; elemTemplateElement = elemTemplateElement.getNextSiblingElem()) {
                    ElemParam elemParam = (ElemParam)elemTemplateElement;
                    if (elemParam.getName().equals(elemWithParam.getName())) {
                        elemWithParam.m_index = n4;
                    }
                    ++n4;
                }
            }
        }
    }

    public void endCompose(StylesheetRoot stylesheetRoot) throws TransformerException {
        int n2 = this.getParamElemCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            ElemWithParam elemWithParam = this.getParamElem(i2);
            elemWithParam.endCompose(stylesheetRoot);
        }
        super.endCompose(stylesheetRoot);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        if (null != this.m_template) {
            XPathContext xPathContext = transformerImpl.getXPathContext();
            VariableStack variableStack = xPathContext.getVarStack();
            int n2 = variableStack.getStackFrame();
            int n3 = variableStack.link(this.m_template.m_frameSize);
            if (this.m_template.m_inArgsSize > 0) {
                variableStack.clearLocalSlots(0, this.m_template.m_inArgsSize);
                if (null != this.m_paramElems) {
                    int n4 = xPathContext.getCurrentNode();
                    variableStack.setStackFrame(n2);
                    int n5 = this.m_paramElems.length;
                    for (int i2 = 0; i2 < n5; ++i2) {
                        ElemWithParam elemWithParam = this.m_paramElems[i2];
                        if (elemWithParam.m_index < 0) continue;
                        if (transformerImpl.getDebug()) {
                            transformerImpl.getTraceManager().fireTraceEvent(elemWithParam);
                        }
                        XObject xObject = elemWithParam.getValue(transformerImpl, n4);
                        if (transformerImpl.getDebug()) {
                            transformerImpl.getTraceManager().fireTraceEndEvent(elemWithParam);
                        }
                        variableStack.setLocalVariable(elemWithParam.m_index, xObject, n3);
                    }
                    variableStack.setStackFrame(n3);
                }
            }
            SourceLocator sourceLocator = xPathContext.getSAXLocator();
            try {
                xPathContext.setSAXLocator(this.m_template);
                transformerImpl.pushElemTemplateElement(this.m_template);
                this.m_template.execute(transformerImpl);
                Object var12_12 = null;
                transformerImpl.popElemTemplateElement();
                xPathContext.setSAXLocator(sourceLocator);
                variableStack.unlink(n2);
            }
            catch (Throwable throwable) {
                Object var12_13 = null;
                transformerImpl.popElemTemplateElement();
                xPathContext.setSAXLocator(sourceLocator);
                variableStack.unlink(n2);
                throw throwable;
            }
        } else {
            transformerImpl.getMsgMgr().error((SourceLocator)this, "ER_TEMPLATE_NOT_FOUND", new Object[]{this.m_templateName});
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    public int getParamElemCount() {
        return this.m_paramElems == null ? 0 : this.m_paramElems.length;
    }

    public ElemWithParam getParamElem(int n2) {
        return this.m_paramElems[n2];
    }

    public void setParamElem(ElemWithParam elemWithParam) {
        if (null == this.m_paramElems) {
            this.m_paramElems = new ElemWithParam[1];
            this.m_paramElems[0] = elemWithParam;
        } else {
            int n2 = this.m_paramElems.length;
            ElemWithParam[] arrelemWithParam = new ElemWithParam[n2 + 1];
            System.arraycopy(this.m_paramElems, 0, arrelemWithParam, 0, n2);
            this.m_paramElems = arrelemWithParam;
            arrelemWithParam[n2] = elemWithParam;
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        if (2 == n2) {
            this.setParamElem((ElemWithParam)elemTemplateElement);
        }
        return super.appendChild(elemTemplateElement);
    }

    public void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        super.callChildVisitors(xSLTVisitor, bl);
    }
}

