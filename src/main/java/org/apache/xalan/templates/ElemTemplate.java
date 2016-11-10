/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.StackGuard;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;

public class ElemTemplate
extends ElemTemplateElement {
    static final long serialVersionUID = -5283056789965384058L;
    private String m_publicId;
    private String m_systemId;
    private Stylesheet m_stylesheet;
    private XPath m_matchPattern = null;
    private QName m_name = null;
    private QName m_mode;
    private double m_priority = Double.NEGATIVE_INFINITY;
    public int m_frameSize;
    int m_inArgsSize;
    private int[] m_argsQNameIDs;

    public String getPublicId() {
        return this.m_publicId;
    }

    public String getSystemId() {
        return this.m_systemId;
    }

    public void setLocaterInfo(SourceLocator sourceLocator) {
        this.m_publicId = sourceLocator.getPublicId();
        this.m_systemId = sourceLocator.getSystemId();
        super.setLocaterInfo(sourceLocator);
    }

    public StylesheetComposed getStylesheetComposed() {
        return this.m_stylesheet.getStylesheetComposed();
    }

    public Stylesheet getStylesheet() {
        return this.m_stylesheet;
    }

    public void setStylesheet(Stylesheet stylesheet) {
        this.m_stylesheet = stylesheet;
    }

    public StylesheetRoot getStylesheetRoot() {
        return this.m_stylesheet.getStylesheetRoot();
    }

    public void setMatch(XPath xPath) {
        this.m_matchPattern = xPath;
    }

    public XPath getMatch() {
        return this.m_matchPattern;
    }

    public void setName(QName qName) {
        this.m_name = qName;
    }

    public QName getName() {
        return this.m_name;
    }

    public void setMode(QName qName) {
        this.m_mode = qName;
    }

    public QName getMode() {
        return this.m_mode;
    }

    public void setPriority(double d2) {
        this.m_priority = d2;
    }

    public double getPriority() {
        return this.m_priority;
    }

    public int getXSLToken() {
        return 19;
    }

    public String getNodeName() {
        return "template";
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
        Vector vector = composeState.getVariableNames();
        if (null != this.m_matchPattern) {
            this.m_matchPattern.fixupVariables(vector, stylesheetRoot.getComposeState().getGlobalsSize());
        }
        composeState.resetStackFrameSize();
        this.m_inArgsSize = 0;
    }

    public void endCompose(StylesheetRoot stylesheetRoot) throws TransformerException {
        StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
        super.endCompose(stylesheetRoot);
        this.m_frameSize = composeState.getFrameSize();
        composeState.resetStackFrameSize();
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        XPathContext xPathContext = transformerImpl.getXPathContext();
        transformerImpl.getStackGuard().checkForInfinateLoop();
        xPathContext.pushRTFContext();
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        transformerImpl.executeChildTemplates((ElemTemplateElement)this, true);
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
        xPathContext.popRTFContext();
    }

    public void recompose(StylesheetRoot stylesheetRoot) {
        stylesheetRoot.recomposeTemplates(this);
    }
}

