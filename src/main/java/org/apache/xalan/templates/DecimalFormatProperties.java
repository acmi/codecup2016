/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.text.DecimalFormatSymbols;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.QName;

public class DecimalFormatProperties
extends ElemTemplateElement {
    static final long serialVersionUID = -6559409339256269446L;
    DecimalFormatSymbols m_dfs = new DecimalFormatSymbols();
    private QName m_qname = null;

    public DecimalFormatProperties(int n2) {
        this.m_dfs.setInfinity("Infinity");
        this.m_dfs.setNaN("NaN");
        this.m_docOrderNumber = n2;
    }

    public DecimalFormatSymbols getDecimalFormatSymbols() {
        return this.m_dfs;
    }

    public void setName(QName qName) {
        this.m_qname = qName;
    }

    public QName getName() {
        if (this.m_qname == null) {
            return new QName("");
        }
        return this.m_qname;
    }

    public void setDecimalSeparator(char c2) {
        this.m_dfs.setDecimalSeparator(c2);
    }

    public char getDecimalSeparator() {
        return this.m_dfs.getDecimalSeparator();
    }

    public void setGroupingSeparator(char c2) {
        this.m_dfs.setGroupingSeparator(c2);
    }

    public char getGroupingSeparator() {
        return this.m_dfs.getGroupingSeparator();
    }

    public void setInfinity(String string) {
        this.m_dfs.setInfinity(string);
    }

    public String getInfinity() {
        return this.m_dfs.getInfinity();
    }

    public void setMinusSign(char c2) {
        this.m_dfs.setMinusSign(c2);
    }

    public char getMinusSign() {
        return this.m_dfs.getMinusSign();
    }

    public void setNaN(String string) {
        this.m_dfs.setNaN(string);
    }

    public String getNaN() {
        return this.m_dfs.getNaN();
    }

    public String getNodeName() {
        return "decimal-format";
    }

    public void setPercent(char c2) {
        this.m_dfs.setPercent(c2);
    }

    public char getPercent() {
        return this.m_dfs.getPercent();
    }

    public void setPerMille(char c2) {
        this.m_dfs.setPerMill(c2);
    }

    public char getPerMille() {
        return this.m_dfs.getPerMill();
    }

    public int getXSLToken() {
        return 83;
    }

    public void setZeroDigit(char c2) {
        this.m_dfs.setZeroDigit(c2);
    }

    public char getZeroDigit() {
        return this.m_dfs.getZeroDigit();
    }

    public void setDigit(char c2) {
        this.m_dfs.setDigit(c2);
    }

    public char getDigit() {
        return this.m_dfs.getDigit();
    }

    public void setPatternSeparator(char c2) {
        this.m_dfs.setPatternSeparator(c2);
    }

    public char getPatternSeparator() {
        return this.m_dfs.getPatternSeparator();
    }

    public void recompose(StylesheetRoot stylesheetRoot) {
        stylesheetRoot.recomposeDecimalFormats(this);
    }
}

