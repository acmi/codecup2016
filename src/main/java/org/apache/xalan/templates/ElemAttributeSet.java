/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemAttribute;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemUse;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;

public class ElemAttributeSet
extends ElemUse {
    static final long serialVersionUID = -426740318278164496L;
    public QName m_qname = null;

    public void setName(QName qName) {
        this.m_qname = qName;
    }

    public QName getName() {
        return this.m_qname;
    }

    public int getXSLToken() {
        return 40;
    }

    public String getNodeName() {
        return "attribute-set";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        if (transformerImpl.isRecursiveAttrSet(this)) {
            throw new TransformerException(XSLMessages.createMessage("ER_XSLATTRSET_USED_ITSELF", new Object[]{this.m_qname.getLocalPart()}));
        }
        transformerImpl.pushElemAttributeSet(this);
        super.execute(transformerImpl);
        for (ElemAttribute elemAttribute = (ElemAttribute)this.getFirstChildElem(); null != elemAttribute; elemAttribute = (ElemAttribute)elemAttribute.getNextSiblingElem()) {
            elemAttribute.execute(transformerImpl);
        }
        transformerImpl.popElemAttributeSet();
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    public ElemTemplateElement appendChildElem(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        switch (n2) {
            case 48: {
                break;
            }
            default: {
                this.error("ER_CANNOT_ADD", new Object[]{elemTemplateElement.getNodeName(), this.getNodeName()});
            }
        }
        return super.appendChild(elemTemplateElement);
    }

    public void recompose(StylesheetRoot stylesheetRoot) {
        stylesheetRoot.recomposeAttributeSets(this);
    }
}

