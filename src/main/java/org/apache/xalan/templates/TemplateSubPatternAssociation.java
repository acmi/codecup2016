/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.Serializable;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPathContext;
import org.apache.xpath.patterns.StepPattern;

class TemplateSubPatternAssociation
implements Serializable,
Cloneable {
    static final long serialVersionUID = -8902606755229903350L;
    StepPattern m_stepPattern;
    private String m_pattern;
    private ElemTemplate m_template;
    private TemplateSubPatternAssociation m_next = null;
    private boolean m_wild;
    private String m_targetString;

    TemplateSubPatternAssociation(ElemTemplate elemTemplate, StepPattern stepPattern, String string) {
        this.m_pattern = string;
        this.m_template = elemTemplate;
        this.m_stepPattern = stepPattern;
        this.m_targetString = this.m_stepPattern.getTargetString();
        this.m_wild = this.m_targetString.equals("*");
    }

    public Object clone() throws CloneNotSupportedException {
        TemplateSubPatternAssociation templateSubPatternAssociation = (TemplateSubPatternAssociation)super.clone();
        templateSubPatternAssociation.m_next = null;
        return templateSubPatternAssociation;
    }

    public final String getTargetString() {
        return this.m_targetString;
    }

    public void setTargetString(String string) {
        this.m_targetString = string;
    }

    boolean matchMode(QName qName) {
        return this.matchModes(qName, this.m_template.getMode());
    }

    private boolean matchModes(QName qName, QName qName2) {
        return null == qName && null == qName2 || null != qName && null != qName2 && qName.equals(qName2);
    }

    public boolean matches(XPathContext xPathContext, int n2, QName qName) throws TransformerException {
        double d2 = this.m_stepPattern.getMatchScore(xPathContext, n2);
        return Double.NEGATIVE_INFINITY != d2 && this.matchModes(qName, this.m_template.getMode());
    }

    public final boolean isWild() {
        return this.m_wild;
    }

    public final StepPattern getStepPattern() {
        return this.m_stepPattern;
    }

    public final String getPattern() {
        return this.m_pattern;
    }

    public int getDocOrderPos() {
        return this.m_template.getUid();
    }

    public final int getImportLevel() {
        return this.m_template.getStylesheetComposed().getImportCountComposed();
    }

    public final ElemTemplate getTemplate() {
        return this.m_template;
    }

    public final TemplateSubPatternAssociation getNext() {
        return this.m_next;
    }

    public void setNext(TemplateSubPatternAssociation templateSubPatternAssociation) {
        this.m_next = templateSubPatternAssociation;
    }
}

