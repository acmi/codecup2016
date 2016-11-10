/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import org.apache.xalan.templates.ElemTemplateElement;

public class ElemText
extends ElemTemplateElement {
    static final long serialVersionUID = 1383140876182316711L;
    private boolean m_disableOutputEscaping = false;

    public void setDisableOutputEscaping(boolean bl) {
        this.m_disableOutputEscaping = bl;
    }

    public boolean getDisableOutputEscaping() {
        return this.m_disableOutputEscaping;
    }

    public int getXSLToken() {
        return 42;
    }

    public String getNodeName() {
        return "text";
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        switch (n2) {
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

