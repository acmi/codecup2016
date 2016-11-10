/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class ElemValueOf
extends ElemTemplateElement {
    static final long serialVersionUID = 3490728458007586786L;
    private XPath m_selectExpression = null;
    private boolean m_isDot = false;
    private boolean m_disableOutputEscaping = false;

    public void setSelect(XPath xPath) {
        if (null != xPath) {
            String string = xPath.getPatternString();
            this.m_isDot = null != string && string.equals(".");
        }
        this.m_selectExpression = xPath;
    }

    public XPath getSelect() {
        return this.m_selectExpression;
    }

    public void setDisableOutputEscaping(boolean bl) {
        this.m_disableOutputEscaping = bl;
    }

    public boolean getDisableOutputEscaping() {
        return this.m_disableOutputEscaping;
    }

    public int getXSLToken() {
        return 30;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        Vector vector = stylesheetRoot.getComposeState().getVariableNames();
        if (null != this.m_selectExpression) {
            this.m_selectExpression.fixupVariables(vector, stylesheetRoot.getComposeState().getGlobalsSize());
        }
    }

    public String getNodeName() {
        return "value-of";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        XPathContext xPathContext = transformerImpl.getXPathContext();
        SerializationHandler serializationHandler = transformerImpl.getResultTreeHandler();
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        try {
            try {
                block14 : {
                    xPathContext.pushNamespaceContext(this);
                    int n2 = xPathContext.getCurrentNode();
                    xPathContext.pushCurrentNodeAndExpression(n2, n2);
                    if (this.m_disableOutputEscaping) {
                        serializationHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
                    }
                    try {
                        Expression expression = this.m_selectExpression.getExpression();
                        if (transformerImpl.getDebug()) {
                            XObject xObject = expression.execute(xPathContext);
                            transformerImpl.getTraceManager().fireSelectedEvent(n2, this, "select", this.m_selectExpression, xObject);
                            xObject.dispatchCharactersEvents(serializationHandler);
                        } else {
                            expression.executeCharsToContentHandler(xPathContext, serializationHandler);
                        }
                        Object var8_10 = null;
                        if (!this.m_disableOutputEscaping) break block14;
                    }
                    catch (Throwable throwable) {
                        Object var8_11 = null;
                        if (this.m_disableOutputEscaping) {
                            serializationHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
                        }
                        xPathContext.popNamespaceContext();
                        xPathContext.popCurrentNodeAndExpression();
                        throw throwable;
                    }
                    serializationHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
                }
                xPathContext.popNamespaceContext();
                xPathContext.popCurrentNodeAndExpression();
                {
                }
                Object var10_13 = null;
            }
            catch (SAXException sAXException) {
                throw new TransformerException(sAXException);
            }
            catch (RuntimeException runtimeException) {
                TransformerException transformerException = new TransformerException(runtimeException);
                transformerException.setLocator(this);
                throw transformerException;
            }
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
        }
        catch (Throwable throwable) {
            Object var10_14 = null;
            if (transformerImpl.getDebug()) {
                transformerImpl.getTraceManager().fireTraceEndEvent(this);
            }
            throw throwable;
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        this.error("ER_CANNOT_ADD", new Object[]{elemTemplateElement.getNodeName(), this.getNodeName()});
        return null;
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (bl) {
            this.m_selectExpression.getExpression().callVisitors(this.m_selectExpression, xSLTVisitor);
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }
}

