/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import javax.xml.transform.SourceLocator;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.templates.DecimalFormatProperties;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xml.utils.SAXSourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class ProcessorDecimalFormat
extends XSLTElementProcessor {
    static final long serialVersionUID = -5052904382662921627L;

    ProcessorDecimalFormat() {
    }

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        DecimalFormatProperties decimalFormatProperties = new DecimalFormatProperties(stylesheetHandler.nextUid());
        decimalFormatProperties.setDOMBackPointer(stylesheetHandler.getOriginatingNode());
        decimalFormatProperties.setLocaterInfo(stylesheetHandler.getLocator());
        this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, decimalFormatProperties);
        stylesheetHandler.getStylesheet().setDecimalFormat(decimalFormatProperties);
        stylesheetHandler.getStylesheet().appendChild(decimalFormatProperties);
    }
}

