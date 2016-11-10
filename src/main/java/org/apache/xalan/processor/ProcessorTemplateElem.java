/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.XSLTElementDef;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.utils.SAXSourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

public class ProcessorTemplateElem
extends XSLTElementProcessor {
    static final long serialVersionUID = 8344994001943407235L;

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        super.startElement(stylesheetHandler, string, string2, string3, attributes);
        try {
            XSLTElementDef xSLTElementDef = this.getElemDef();
            Class class_ = xSLTElementDef.getClassObject();
            ElemTemplateElement elemTemplateElement = null;
            try {
                elemTemplateElement = (ElemTemplateElement)class_.newInstance();
                elemTemplateElement.setDOMBackPointer(stylesheetHandler.getOriginatingNode());
                elemTemplateElement.setLocaterInfo(stylesheetHandler.getLocator());
                elemTemplateElement.setPrefixes(stylesheetHandler.getNamespaceSupport());
            }
            catch (InstantiationException instantiationException) {
                stylesheetHandler.error("ER_FAILED_CREATING_ELEMTMPL", null, instantiationException);
            }
            catch (IllegalAccessException illegalAccessException) {
                stylesheetHandler.error("ER_FAILED_CREATING_ELEMTMPL", null, illegalAccessException);
            }
            this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, elemTemplateElement);
            this.appendAndPush(stylesheetHandler, elemTemplateElement);
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    protected void appendAndPush(StylesheetHandler stylesheetHandler, ElemTemplateElement elemTemplateElement) throws SAXException {
        ElemTemplateElement elemTemplateElement2 = stylesheetHandler.getElemTemplateElement();
        if (null != elemTemplateElement2) {
            elemTemplateElement2.appendChild(elemTemplateElement);
            stylesheetHandler.pushElemTemplateElement(elemTemplateElement);
        }
    }

    public void endElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3) throws SAXException {
        super.endElement(stylesheetHandler, string, string2, string3);
        stylesheetHandler.popElemTemplateElement().setEndLocaterInfo(stylesheetHandler.getLocator());
    }
}

