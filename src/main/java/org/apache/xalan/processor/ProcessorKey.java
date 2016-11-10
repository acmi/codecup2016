/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.util.ArrayList;
import javax.xml.transform.SourceLocator;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.XSLTAttributeDef;
import org.apache.xalan.processor.XSLTElementDef;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xml.utils.SAXSourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class ProcessorKey
extends XSLTElementProcessor {
    static final long serialVersionUID = 4285205417566822979L;

    ProcessorKey() {
    }

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        KeyDeclaration keyDeclaration = new KeyDeclaration(stylesheetHandler.getStylesheet(), stylesheetHandler.nextUid());
        keyDeclaration.setDOMBackPointer(stylesheetHandler.getOriginatingNode());
        keyDeclaration.setLocaterInfo(stylesheetHandler.getLocator());
        this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, keyDeclaration);
        stylesheetHandler.getStylesheet().setKey(keyDeclaration);
    }

    void setPropertiesFromAttributes(StylesheetHandler stylesheetHandler, String string, Attributes attributes, ElemTemplateElement elemTemplateElement) throws SAXException {
        String string2;
        XSLTAttributeDef xSLTAttributeDef;
        XSLTElementDef xSLTElementDef = this.getElemDef();
        ArrayList<XSLTAttributeDef> arrayList = new ArrayList<XSLTAttributeDef>();
        int n2 = attributes.getLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string3;
            String string4 = attributes.getURI(i2);
            xSLTAttributeDef = xSLTElementDef.getAttributeDef(string4, string3 = attributes.getLocalName(i2));
            if (null == xSLTAttributeDef) {
                stylesheetHandler.error(attributes.getQName(i2) + "attribute is not allowed on the " + string + " element!", null);
                continue;
            }
            string2 = attributes.getValue(i2);
            if (string2.indexOf("key(") >= 0) {
                stylesheetHandler.error(XSLMessages.createMessage("ER_INVALID_KEY_CALL", null), null);
            }
            arrayList.add(xSLTAttributeDef);
            xSLTAttributeDef.setAttrValue(stylesheetHandler, string4, string3, attributes.getQName(i2), attributes.getValue(i2), elemTemplateElement);
        }
        XSLTAttributeDef[] arrxSLTAttributeDef = xSLTElementDef.getAttributes();
        int n3 = arrxSLTAttributeDef.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            xSLTAttributeDef = arrxSLTAttributeDef[i3];
            string2 = xSLTAttributeDef.getDefault();
            if (null != string2 && !arrayList.contains(xSLTAttributeDef)) {
                xSLTAttributeDef.setDefAttrValue(stylesheetHandler, elemTemplateElement);
            }
            if (!xSLTAttributeDef.getRequired() || arrayList.contains(xSLTAttributeDef)) continue;
            stylesheetHandler.error(XSLMessages.createMessage("ER_REQUIRES_ATTRIB", new Object[]{string, xSLTAttributeDef.getName()}), null);
        }
    }
}

