/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.io.PrintStream;
import java.util.ArrayList;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xalan.processor.XSLTAttributeDef;
import org.apache.xalan.processor.XSLTElementDef;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xml.utils.IntStack;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XSLTElementProcessor
extends ElemTemplateElement {
    static final long serialVersionUID = 5597421564955304421L;
    private IntStack m_savedLastOrder;
    private XSLTElementDef m_elemDef;

    XSLTElementProcessor() {
    }

    XSLTElementDef getElemDef() {
        return this.m_elemDef;
    }

    void setElemDef(XSLTElementDef xSLTElementDef) {
        this.m_elemDef = xSLTElementDef;
    }

    public InputSource resolveEntity(StylesheetHandler stylesheetHandler, String string, String string2) throws SAXException {
        return null;
    }

    public void notationDecl(StylesheetHandler stylesheetHandler, String string, String string2, String string3) {
    }

    public void unparsedEntityDecl(StylesheetHandler stylesheetHandler, String string, String string2, String string3, String string4) {
    }

    public void startNonText(StylesheetHandler stylesheetHandler) throws SAXException {
    }

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        if (this.m_savedLastOrder == null) {
            this.m_savedLastOrder = new IntStack();
        }
        this.m_savedLastOrder.push(this.getElemDef().getLastOrder());
        this.getElemDef().setLastOrder(-1);
    }

    public void endElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3) throws SAXException {
        if (this.m_savedLastOrder != null && !this.m_savedLastOrder.empty()) {
            this.getElemDef().setLastOrder(this.m_savedLastOrder.pop());
        }
        if (!this.getElemDef().getRequiredFound()) {
            stylesheetHandler.error("ER_REQUIRED_ELEM_NOT_FOUND", new Object[]{this.getElemDef().getRequiredElem()}, null);
        }
    }

    public void characters(StylesheetHandler stylesheetHandler, char[] arrc, int n2, int n3) throws SAXException {
        stylesheetHandler.error("ER_CHARS_NOT_ALLOWED", null, null);
    }

    public void ignorableWhitespace(StylesheetHandler stylesheetHandler, char[] arrc, int n2, int n3) throws SAXException {
    }

    public void processingInstruction(StylesheetHandler stylesheetHandler, String string, String string2) throws SAXException {
    }

    public void skippedEntity(StylesheetHandler stylesheetHandler, String string) throws SAXException {
    }

    void setPropertiesFromAttributes(StylesheetHandler stylesheetHandler, String string, Attributes attributes, ElemTemplateElement elemTemplateElement) throws SAXException {
        this.setPropertiesFromAttributes(stylesheetHandler, string, attributes, elemTemplateElement, true);
    }

    Attributes setPropertiesFromAttributes(StylesheetHandler stylesheetHandler, String string, Attributes attributes, ElemTemplateElement elemTemplateElement, boolean bl) throws SAXException {
        boolean bl2;
        XSLTAttributeDef xSLTAttributeDef;
        XSLTElementDef xSLTElementDef = this.getElemDef();
        AttributesImpl attributesImpl = null;
        boolean bl3 = bl2 = null != stylesheetHandler.getStylesheet() && stylesheetHandler.getStylesheet().getCompatibleMode() || !bl;
        if (bl2) {
            attributesImpl = new AttributesImpl();
        }
        ArrayList<XSLTAttributeDef> arrayList = new ArrayList<XSLTAttributeDef>();
        ArrayList<XSLTAttributeDef> arrayList2 = new ArrayList<XSLTAttributeDef>();
        int n2 = attributes.getLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string2;
            String string3 = attributes.getURI(i2);
            if (null != string3 && string3.length() == 0 && (attributes.getQName(i2).startsWith("xmlns:") || attributes.getQName(i2).equals("xmlns"))) {
                string3 = "http://www.w3.org/XML/1998/namespace";
            }
            if (null == (xSLTAttributeDef = xSLTElementDef.getAttributeDef(string3, string2 = attributes.getLocalName(i2)))) {
                if (!bl2) {
                    stylesheetHandler.error("ER_ATTR_NOT_ALLOWED", new Object[]{attributes.getQName(i2), string}, null);
                    continue;
                }
                attributesImpl.addAttribute(string3, string2, attributes.getQName(i2), attributes.getType(i2), attributes.getValue(i2));
                continue;
            }
            if (stylesheetHandler.getStylesheetProcessor() == null) {
                System.out.println("stylesheet processor null");
            }
            if (xSLTAttributeDef.getName().compareTo("*") == 0 && stylesheetHandler.getStylesheetProcessor().isSecureProcessing()) {
                stylesheetHandler.error("ER_ATTR_NOT_ALLOWED", new Object[]{attributes.getQName(i2), string}, null);
                continue;
            }
            boolean bl4 = xSLTAttributeDef.setAttrValue(stylesheetHandler, string3, string2, attributes.getQName(i2), attributes.getValue(i2), elemTemplateElement);
            if (bl4) {
                arrayList.add(xSLTAttributeDef);
                continue;
            }
            arrayList2.add(xSLTAttributeDef);
        }
        XSLTAttributeDef[] arrxSLTAttributeDef = xSLTElementDef.getAttributes();
        int n3 = arrxSLTAttributeDef.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            xSLTAttributeDef = arrxSLTAttributeDef[i3];
            String string4 = xSLTAttributeDef.getDefault();
            if (null != string4 && !arrayList.contains(xSLTAttributeDef)) {
                xSLTAttributeDef.setDefAttrValue(stylesheetHandler, elemTemplateElement);
            }
            if (!xSLTAttributeDef.getRequired() || arrayList.contains(xSLTAttributeDef) || arrayList2.contains(xSLTAttributeDef)) continue;
            stylesheetHandler.error(XSLMessages.createMessage("ER_REQUIRES_ATTRIB", new Object[]{string, xSLTAttributeDef.getName()}), null);
        }
        return attributesImpl;
    }
}

