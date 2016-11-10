/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import org.apache.xalan.templates.ElemTemplateElement;

public class ElemExtensionScript
extends ElemTemplateElement {
    static final long serialVersionUID = -6995978265966057744L;
    private String m_lang = null;
    private String m_src = null;

    public void setLang(String string) {
        this.m_lang = string;
    }

    public String getLang() {
        return this.m_lang;
    }

    public void setSrc(String string) {
        this.m_src = string;
    }

    public String getSrc() {
        return this.m_src;
    }

    public int getXSLToken() {
        return 86;
    }
}

