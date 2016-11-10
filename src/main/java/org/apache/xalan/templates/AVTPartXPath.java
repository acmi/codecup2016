/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.AVTPart;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathFactory;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.XPathParser;
import org.apache.xpath.objects.XObject;

public class AVTPartXPath
extends AVTPart {
    static final long serialVersionUID = -4460373807550527675L;
    private XPath m_xpath;

    public void fixupVariables(Vector vector, int n2) {
        this.m_xpath.fixupVariables(vector, n2);
    }

    public boolean canTraverseOutsideSubtree() {
        return this.m_xpath.getExpression().canTraverseOutsideSubtree();
    }

    public AVTPartXPath(XPath xPath) {
        this.m_xpath = xPath;
    }

    public AVTPartXPath(String string, PrefixResolver prefixResolver, XPathParser xPathParser, XPathFactory xPathFactory, XPathContext xPathContext) throws TransformerException {
        this.m_xpath = new XPath(string, null, prefixResolver, 0, xPathContext.getErrorListener());
    }

    public String getSimpleString() {
        return "{" + this.m_xpath.getPatternString() + "}";
    }

    public void evaluate(XPathContext xPathContext, FastStringBuffer fastStringBuffer, int n2, PrefixResolver prefixResolver) throws TransformerException {
        XObject xObject = this.m_xpath.execute(xPathContext, n2, prefixResolver);
        if (null != xObject) {
            xObject.appendToFsb(fastStringBuffer);
        }
    }

    public void callVisitors(XSLTVisitor xSLTVisitor) {
        this.m_xpath.getExpression().callVisitors(this.m_xpath, xSLTVisitor);
    }
}

