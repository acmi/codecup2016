/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import org.apache.xalan.processor.ProcessorTemplateElem;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.templates.ElemApplyImport;
import org.apache.xalan.templates.ElemApplyTemplates;
import org.apache.xalan.templates.ElemAttribute;
import org.apache.xalan.templates.ElemCallTemplate;
import org.apache.xalan.templates.ElemComment;
import org.apache.xalan.templates.ElemCopy;
import org.apache.xalan.templates.ElemCopyOf;
import org.apache.xalan.templates.ElemElement;
import org.apache.xalan.templates.ElemExsltFuncResult;
import org.apache.xalan.templates.ElemExsltFunction;
import org.apache.xalan.templates.ElemFallback;
import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.templates.ElemMessage;
import org.apache.xalan.templates.ElemNumber;
import org.apache.xalan.templates.ElemPI;
import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemText;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xalan.templates.ElemValueOf;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.Stylesheet;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ProcessorExsltFunction
extends ProcessorTemplateElem {
    static final long serialVersionUID = 2411427965578315332L;

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        String string4 = "";
        if (!(stylesheetHandler.getElemTemplateElement() instanceof Stylesheet)) {
            string4 = "func:function element must be top level.";
            stylesheetHandler.error(string4, new SAXException(string4));
        }
        super.startElement(stylesheetHandler, string, string2, string3, attributes);
        String string5 = attributes.getValue("name");
        int n2 = string5.indexOf(":");
        if (n2 <= 0) {
            string4 = "func:function name must have namespace";
            stylesheetHandler.error(string4, new SAXException(string4));
        }
    }

    protected void appendAndPush(StylesheetHandler stylesheetHandler, ElemTemplateElement elemTemplateElement) throws SAXException {
        super.appendAndPush(stylesheetHandler, elemTemplateElement);
        elemTemplateElement.setDOMBackPointer(stylesheetHandler.getOriginatingNode());
        stylesheetHandler.getStylesheet().setTemplate((ElemTemplate)elemTemplateElement);
    }

    public void endElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3) throws SAXException {
        ElemTemplateElement elemTemplateElement = stylesheetHandler.getElemTemplateElement();
        this.validate(elemTemplateElement, stylesheetHandler);
        super.endElement(stylesheetHandler, string, string2, string3);
    }

    public void validate(ElemTemplateElement elemTemplateElement, StylesheetHandler stylesheetHandler) throws SAXException {
        String string = "";
        while (elemTemplateElement != null) {
            if (elemTemplateElement instanceof ElemExsltFuncResult && elemTemplateElement.getNextSiblingElem() != null && !(elemTemplateElement.getNextSiblingElem() instanceof ElemFallback)) {
                string = "func:result has an illegal following sibling (only xsl:fallback allowed)";
                stylesheetHandler.error(string, new SAXException(string));
            }
            if ((elemTemplateElement instanceof ElemApplyImport || elemTemplateElement instanceof ElemApplyTemplates || elemTemplateElement instanceof ElemAttribute || elemTemplateElement instanceof ElemCallTemplate || elemTemplateElement instanceof ElemComment || elemTemplateElement instanceof ElemCopy || elemTemplateElement instanceof ElemCopyOf || elemTemplateElement instanceof ElemElement || elemTemplateElement instanceof ElemLiteralResult || elemTemplateElement instanceof ElemNumber || elemTemplateElement instanceof ElemPI || elemTemplateElement instanceof ElemText || elemTemplateElement instanceof ElemTextLiteral || elemTemplateElement instanceof ElemValueOf) && !this.ancestorIsOk(elemTemplateElement)) {
                string = "misplaced literal result in a func:function container.";
                stylesheetHandler.error(string, new SAXException(string));
            }
            ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getFirstChildElem();
            while (elemTemplateElement2 == null) {
                elemTemplateElement2 = elemTemplateElement.getNextSiblingElem();
                if (elemTemplateElement2 == null) {
                    elemTemplateElement = elemTemplateElement.getParentElem();
                }
                if (elemTemplateElement != null && !(elemTemplateElement instanceof ElemExsltFunction)) continue;
                return;
            }
            elemTemplateElement = elemTemplateElement2;
        }
    }

    boolean ancestorIsOk(ElemTemplateElement elemTemplateElement) {
        while (elemTemplateElement.getParentElem() != null && !(elemTemplateElement.getParentElem() instanceof ElemExsltFunction)) {
            ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getParentElem();
            if (elemTemplateElement2 instanceof ElemExsltFuncResult || elemTemplateElement2 instanceof ElemVariable || elemTemplateElement2 instanceof ElemParam || elemTemplateElement2 instanceof ElemMessage) {
                return true;
            }
            elemTemplateElement = elemTemplateElement2;
        }
        return false;
    }
}

