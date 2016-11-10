/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.templates.ElemAttributeSet;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xml.utils.SAXSourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

class ProcessorAttributeSet
extends XSLTElementProcessor {
    static final long serialVersionUID = -6473739251316787552L;

    ProcessorAttributeSet() {
    }

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        ElemAttributeSet elemAttributeSet = new ElemAttributeSet();
        elemAttributeSet.setLocaterInfo(stylesheetHandler.getLocator());
        try {
            elemAttributeSet.setPrefixes(stylesheetHandler.getNamespaceSupport());
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
        elemAttributeSet.setDOMBackPointer(stylesheetHandler.getOriginatingNode());
        this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, elemAttributeSet);
        stylesheetHandler.getStylesheet().setAttributeSet(elemAttributeSet);
        ElemTemplateElement elemTemplateElement = stylesheetHandler.getElemTemplateElement();
        elemTemplateElement.appendChild(elemAttributeSet);
        stylesheetHandler.pushElemTemplateElement(elemAttributeSet);
    }

    public void endElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3) throws SAXException {
        stylesheetHandler.popElemTemplateElement();
    }
}

