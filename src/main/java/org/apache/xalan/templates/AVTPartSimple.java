/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import org.apache.xalan.templates.AVTPart;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathContext;

public class AVTPartSimple
extends AVTPart {
    static final long serialVersionUID = -3744957690598727913L;
    private String m_val;

    public AVTPartSimple(String string) {
        this.m_val = string;
    }

    public String getSimpleString() {
        return this.m_val;
    }

    public void fixupVariables(Vector vector, int n2) {
    }

    public void evaluate(XPathContext xPathContext, FastStringBuffer fastStringBuffer, int n2, PrefixResolver prefixResolver) {
        fastStringBuffer.append(this.m_val);
    }

    public void callVisitors(XSLTVisitor xSLTVisitor) {
    }
}

