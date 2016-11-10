/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;

public class NamespaceAlias
extends ElemTemplateElement {
    static final long serialVersionUID = 456173966637810718L;
    private String m_StylesheetPrefix;
    private String m_StylesheetNamespace;
    private String m_ResultPrefix;
    private String m_ResultNamespace;

    public NamespaceAlias(int n2) {
        this.m_docOrderNumber = n2;
    }

    public void setStylesheetPrefix(String string) {
        this.m_StylesheetPrefix = string;
    }

    public String getStylesheetPrefix() {
        return this.m_StylesheetPrefix;
    }

    public void setStylesheetNamespace(String string) {
        this.m_StylesheetNamespace = string;
    }

    public String getStylesheetNamespace() {
        return this.m_StylesheetNamespace;
    }

    public void setResultPrefix(String string) {
        this.m_ResultPrefix = string;
    }

    public String getResultPrefix() {
        return this.m_ResultPrefix;
    }

    public void setResultNamespace(String string) {
        this.m_ResultNamespace = string;
    }

    public String getResultNamespace() {
        return this.m_ResultNamespace;
    }

    public void recompose(StylesheetRoot stylesheetRoot) {
        stylesheetRoot.recomposeNamespaceAliases(this);
    }
}

