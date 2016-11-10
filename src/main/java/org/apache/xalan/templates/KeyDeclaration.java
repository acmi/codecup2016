/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;

public class KeyDeclaration
extends ElemTemplateElement {
    static final long serialVersionUID = 7724030248631137918L;
    private QName m_name;
    private XPath m_matchPattern = null;
    private XPath m_use;

    public KeyDeclaration(Stylesheet stylesheet, int n2) {
        this.m_parentNode = stylesheet;
        this.setUid(n2);
    }

    public void setName(QName qName) {
        this.m_name = qName;
    }

    public QName getName() {
        return this.m_name;
    }

    public String getNodeName() {
        return "key";
    }

    public void setMatch(XPath xPath) {
        this.m_matchPattern = xPath;
    }

    public XPath getMatch() {
        return this.m_matchPattern;
    }

    public void setUse(XPath xPath) {
        this.m_use = xPath;
    }

    public XPath getUse() {
        return this.m_use;
    }

    public int getXSLToken() {
        return 31;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        Vector vector = stylesheetRoot.getComposeState().getVariableNames();
        if (null != this.m_matchPattern) {
            this.m_matchPattern.fixupVariables(vector, stylesheetRoot.getComposeState().getGlobalsSize());
        }
        if (null != this.m_use) {
            this.m_use.fixupVariables(vector, stylesheetRoot.getComposeState().getGlobalsSize());
        }
    }

    public void recompose(StylesheetRoot stylesheetRoot) {
        stylesheetRoot.recomposeKeys(this);
    }
}

