/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemUse;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XML11Char;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;

public class ElemElement
extends ElemUse {
    static final long serialVersionUID = -324619535592435183L;
    protected AVT m_name_avt = null;
    protected AVT m_namespace_avt = null;

    public void setName(AVT aVT) {
        this.m_name_avt = aVT;
    }

    public AVT getName() {
        return this.m_name_avt;
    }

    public void setNamespace(AVT aVT) {
        this.m_namespace_avt = aVT;
    }

    public AVT getNamespace() {
        return this.m_namespace_avt;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
        Vector vector = composeState.getVariableNames();
        if (null != this.m_name_avt) {
            this.m_name_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_namespace_avt) {
            this.m_namespace_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
    }

    public int getXSLToken() {
        return 46;
    }

    public String getNodeName() {
        return "element";
    }

    protected String resolvePrefix(SerializationHandler serializationHandler, String string, String string2) throws TransformerException {
        return string;
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEvent(this);
        }
        SerializationHandler serializationHandler = transformerImpl.getSerializationHandler();
        XPathContext xPathContext = transformerImpl.getXPathContext();
        int n2 = xPathContext.getCurrentNode();
        String string = this.m_name_avt == null ? null : this.m_name_avt.evaluate(xPathContext, n2, this);
        String string2 = null;
        String string3 = "";
        if (string != null && !this.m_name_avt.isSimple() && !XML11Char.isXML11ValidQName(string)) {
            transformerImpl.getMsgMgr().warn(this, "WG_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"name", string});
            string = null;
        } else if (string != null) {
            string2 = QName.getPrefixPart(string);
            if (null != this.m_namespace_avt) {
                string3 = this.m_namespace_avt.evaluate(xPathContext, n2, this);
                if (null == string3 || string2 != null && string2.length() > 0 && string3.length() == 0) {
                    transformerImpl.getMsgMgr().error(this, "ER_NULL_URI_NAMESPACE");
                } else {
                    if (null == (string2 = this.resolvePrefix(serializationHandler, string2, string3))) {
                        string2 = "";
                    }
                    string = string2.length() > 0 ? string2 + ":" + QName.getLocalPart(string) : QName.getLocalPart(string);
                }
            } else {
                try {
                    string3 = this.getNamespaceForPrefix(string2);
                    if (null == string3 && string2.length() == 0) {
                        string3 = "";
                    } else if (null == string3) {
                        transformerImpl.getMsgMgr().warn(this, "WG_COULD_NOT_RESOLVE_PREFIX", new Object[]{string2});
                        string = null;
                    }
                }
                catch (Exception exception) {
                    transformerImpl.getMsgMgr().warn(this, "WG_COULD_NOT_RESOLVE_PREFIX", new Object[]{string2});
                    string = null;
                }
            }
        }
        this.constructNode(string, string2, string3, transformerImpl);
        if (transformerImpl.getDebug()) {
            transformerImpl.getTraceManager().fireTraceEndEvent(this);
        }
    }

    void constructNode(String string, String string2, String string3, TransformerImpl transformerImpl) throws TransformerException {
        try {
            boolean bl;
            SerializationHandler serializationHandler = transformerImpl.getResultTreeHandler();
            if (null == string) {
                bl = false;
            } else {
                if (null != string2) {
                    serializationHandler.startPrefixMapping(string2, string3, true);
                }
                serializationHandler.startElement(string3, QName.getLocalPart(string), string);
                super.execute(transformerImpl);
                bl = true;
            }
            transformerImpl.executeChildTemplates((ElemTemplateElement)this, bl);
            if (null != string) {
                serializationHandler.endElement(string3, QName.getLocalPart(string), string);
                if (null != string2) {
                    serializationHandler.endPrefixMapping(string2);
                }
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        if (bl) {
            if (null != this.m_name_avt) {
                this.m_name_avt.callVisitors(xSLTVisitor);
            }
            if (null != this.m_namespace_avt) {
                this.m_namespace_avt.callVisitors(xSLTVisitor);
            }
        }
        super.callChildVisitors(xSLTVisitor, bl);
    }
}

