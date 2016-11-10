/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xpath.XPath;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class ElemSort
extends ElemTemplateElement {
    static final long serialVersionUID = -4991510257335851938L;
    private XPath m_selectExpression = null;
    private AVT m_lang_avt = null;
    private AVT m_dataType_avt = null;
    private AVT m_order_avt = null;
    private AVT m_caseorder_avt = null;

    public void setSelect(XPath xPath) {
        if (xPath.getPatternString().indexOf("{") < 0) {
            this.m_selectExpression = xPath;
        } else {
            this.error("ER_NO_CURLYBRACE", null);
        }
    }

    public XPath getSelect() {
        return this.m_selectExpression;
    }

    public void setLang(AVT aVT) {
        this.m_lang_avt = aVT;
    }

    public AVT getLang() {
        return this.m_lang_avt;
    }

    public void setDataType(AVT aVT) {
        this.m_dataType_avt = aVT;
    }

    public AVT getDataType() {
        return this.m_dataType_avt;
    }

    public void setOrder(AVT aVT) {
        this.m_order_avt = aVT;
    }

    public AVT getOrder() {
        return this.m_order_avt;
    }

    public void setCaseOrder(AVT aVT) {
        this.m_caseorder_avt = aVT;
    }

    public AVT getCaseOrder() {
        return this.m_caseorder_avt;
    }

    public int getXSLToken() {
        return 64;
    }

    public String getNodeName() {
        return "sort";
    }

    public Node appendChild(Node node) throws DOMException {
        this.error("ER_CANNOT_ADD", new Object[]{node.getNodeName(), this.getNodeName()});
        return null;
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
        StylesheetRoot.ComposeState composeState = stylesheetRoot.getComposeState();
        Vector vector = composeState.getVariableNames();
        if (null != this.m_caseorder_avt) {
            this.m_caseorder_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_dataType_avt) {
            this.m_dataType_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_lang_avt) {
            this.m_lang_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_order_avt) {
            this.m_order_avt.fixupVariables(vector, composeState.getGlobalsSize());
        }
        if (null != this.m_selectExpression) {
            this.m_selectExpression.fixupVariables(vector, composeState.getGlobalsSize());
        }
    }
}

