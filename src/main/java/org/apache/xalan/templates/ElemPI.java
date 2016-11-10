/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.XML11Char;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;

public class ElemPI
extends ElemTemplateElement {
    static final long serialVersionUID = 5621976448020889825L;
    private AVT m_name_atv = null;

    public void setName(AVT aVT) {
        this.m_name_atv = aVT;
    }

    public AVT getName() {
        return this.m_name_atv;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        Vector vector = stylesheetRoot.getComposeState().getVariableNames();
        if (null != this.m_name_atv) {
            this.m_name_atv.fixupVariables(vector, stylesheetRoot.getComposeState().getGlobalsSize());
        }
    }

    public int getXSLToken() {
        return 58;
    }

    public String getNodeName() {
        return "processing-instruction";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        String string;
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        XPathContext xPathContext = transformerImpl.getXPathContext();
        int n2 = xPathContext.getCurrentNode();
        String string2 = string = this.m_name_atv == null ? null : this.m_name_atv.evaluate(xPathContext, n2, this);
        if (string == null) {
            return;
        }
        if (string.equalsIgnoreCase("xml")) {
            transformerImpl.getMsgMgr().warn(this, "WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML", new Object[]{"name", string});
            return;
        }
        if (!this.m_name_atv.isSimple() && !XML11Char.isXML11ValidNCName(string)) {
            transformerImpl.getMsgMgr().warn(this, "WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME", new Object[]{"name", string});
            return;
        }
        String string3 = transformerImpl.transformToString(this);
        try {
            transformerImpl.getResultTreeHandler().processingInstruction(string, string3);
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    public ElemTemplateElement appendChild(ElemTemplateElement elemTemplateElement) {
        int n2 = elemTemplateElement.getXSLToken();
        switch (n2) {
            case 9: 
            case 17: 
            case 28: 
            case 30: 
            case 35: 
            case 36: 
            case 37: 
            case 42: 
            case 50: 
            case 72: 
            case 73: 
            case 74: 
            case 75: 
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

