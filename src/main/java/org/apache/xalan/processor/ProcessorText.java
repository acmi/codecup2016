/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import org.apache.xalan.processor.ProcessorCharacters;
import org.apache.xalan.processor.ProcessorTemplateElem;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemText;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ProcessorText
extends ProcessorTemplateElem {
    static final long serialVersionUID = 5170229307201307523L;

    protected void appendAndPush(StylesheetHandler stylesheetHandler, ElemTemplateElement elemTemplateElement) throws SAXException {
        ProcessorCharacters processorCharacters = (ProcessorCharacters)stylesheetHandler.getProcessorFor(null, "text()", "text");
        processorCharacters.setXslTextElement((ElemText)elemTemplateElement);
        ElemTemplateElement elemTemplateElement2 = stylesheetHandler.getElemTemplateElement();
        elemTemplateElement2.appendChild(elemTemplateElement);
        elemTemplateElement.setDOMBackPointer(stylesheetHandler.getOriginatingNode());
    }

    public void endElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3) throws SAXException {
        ProcessorCharacters processorCharacters = (ProcessorCharacters)stylesheetHandler.getProcessorFor(null, "text()", "text");
        processorCharacters.setXslTextElement(null);
    }
}

