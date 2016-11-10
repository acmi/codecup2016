/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import org.apache.xalan.processor.ProcessorTemplateElem;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.templates.ElemExsltFuncResult;
import org.apache.xalan.templates.ElemExsltFunction;
import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ProcessorExsltFuncResult
extends ProcessorTemplateElem {
    static final long serialVersionUID = 6451230911473482423L;

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        ElemTemplateElement elemTemplateElement;
        String string4 = "";
        super.startElement(stylesheetHandler, string, string2, string3, attributes);
        for (elemTemplateElement = stylesheetHandler.getElemTemplateElement().getParentElem(); elemTemplateElement != null && !(elemTemplateElement instanceof ElemExsltFunction); elemTemplateElement = elemTemplateElement.getParentElem()) {
            if (!(elemTemplateElement instanceof ElemVariable) && !(elemTemplateElement instanceof ElemParam) && !(elemTemplateElement instanceof ElemExsltFuncResult)) continue;
            string4 = "func:result cannot appear within a variable, parameter, or another func:result.";
            stylesheetHandler.error(string4, new SAXException(string4));
        }
        if (elemTemplateElement == null) {
            string4 = "func:result must appear in a func:function element";
            stylesheetHandler.error(string4, new SAXException(string4));
        }
    }
}

