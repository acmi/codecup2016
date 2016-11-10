/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.util.List;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.ProcessorTemplateElem;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xalan.processor.XSLTElementDef;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.processor.XSLTSchema;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XMLNSDecl;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xpath.XPath;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

public class ProcessorLRE
extends ProcessorTemplateElem {
    static final long serialVersionUID = -1490218021772101404L;

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        try {
            Object object;
            Class class_;
            Object object2;
            ElemTemplateElement elemTemplateElement = stylesheetHandler.getElemTemplateElement();
            boolean bl = false;
            boolean bl2 = false;
            if (null == elemTemplateElement) {
                Object object3;
                Stylesheet stylesheet;
                object2 = stylesheetHandler.popProcessor();
                class_ = stylesheetHandler.getProcessorFor("http://www.w3.org/1999/XSL/Transform", "stylesheet", "xsl:stylesheet");
                stylesheetHandler.pushProcessor((XSLTElementProcessor)object2);
                try {
                    stylesheet = this.getStylesheetRoot(stylesheetHandler);
                }
                catch (TransformerConfigurationException transformerConfigurationException) {
                    throw new TransformerException(transformerConfigurationException);
                }
                SAXSourceLocator sAXSourceLocator = new SAXSourceLocator();
                SAXSourceLocator sAXSourceLocator2 = stylesheetHandler.getLocator();
                if (null != sAXSourceLocator2) {
                    sAXSourceLocator.setLineNumber(sAXSourceLocator2.getLineNumber());
                    sAXSourceLocator.setColumnNumber(sAXSourceLocator2.getColumnNumber());
                    sAXSourceLocator.setPublicId(sAXSourceLocator2.getPublicId());
                    sAXSourceLocator.setSystemId(sAXSourceLocator2.getSystemId());
                }
                stylesheet.setLocaterInfo(sAXSourceLocator);
                stylesheet.setPrefixes(stylesheetHandler.getNamespaceSupport());
                stylesheetHandler.pushStylesheet(stylesheet);
                bl2 = true;
                object = new AttributesImpl();
                AttributesImpl attributesImpl = new AttributesImpl();
                int n2 = attributes.getLength();
                for (int i2 = 0; i2 < n2; ++i2) {
                    object3 = attributes.getLocalName(i2);
                    String string4 = attributes.getURI(i2);
                    String string5 = attributes.getValue(i2);
                    if (null != string4 && string4.equals("http://www.w3.org/1999/XSL/Transform")) {
                        object.addAttribute(null, (String)object3, (String)object3, attributes.getType(i2), attributes.getValue(i2));
                        continue;
                    }
                    if ((object3.startsWith("xmlns:") || object3.equals("xmlns")) && string5.equals("http://www.w3.org/1999/XSL/Transform")) continue;
                    attributesImpl.addAttribute(string4, (String)object3, attributes.getQName(i2), attributes.getType(i2), attributes.getValue(i2));
                }
                attributes = attributesImpl;
                try {
                    class_.setPropertiesFromAttributes(stylesheetHandler, "stylesheet", (Attributes)object, stylesheet);
                }
                catch (Exception exception) {
                    if (stylesheet.getDeclaredPrefixes() == null || !this.declaredXSLNS(stylesheet)) {
                        throw new SAXException(XSLMessages.createWarning("WG_OLD_XSLT_NS", null));
                    }
                    throw new SAXException(exception);
                }
                stylesheetHandler.pushElemTemplateElement(stylesheet);
                ElemTemplate elemTemplate = new ElemTemplate();
                if (sAXSourceLocator != null) {
                    elemTemplate.setLocaterInfo(sAXSourceLocator);
                }
                this.appendAndPush(stylesheetHandler, elemTemplate);
                object3 = new XPath("/", stylesheet, stylesheet, 1, stylesheetHandler.getStylesheetProcessor().getErrorListener());
                elemTemplate.setMatch((XPath)object3);
                stylesheet.setTemplate(elemTemplate);
                elemTemplateElement = stylesheetHandler.getElemTemplateElement();
                bl = true;
            }
            object2 = this.getElemDef();
            class_ = object2.getClassObject();
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            while (null != elemTemplateElement) {
                if (elemTemplateElement instanceof ElemLiteralResult) {
                    object = (ElemLiteralResult)elemTemplateElement;
                    bl3 = object.containsExtensionElementURI(string);
                } else if (elemTemplateElement instanceof Stylesheet) {
                    object = (Stylesheet)elemTemplateElement;
                    bl3 = object.containsExtensionElementURI(string);
                    if (!bl3 && null != string && (string.equals("http://xml.apache.org/xalan") || string.equals("http://xml.apache.org/xslt"))) {
                        bl4 = true;
                    } else {
                        bl5 = true;
                    }
                }
                if (bl3) break;
                elemTemplateElement = elemTemplateElement.getParentElem();
            }
            object = null;
            try {
                object = bl3 ? new ElemExtensionCall() : (bl4 ? (ElemTemplateElement)class_.newInstance() : (bl5 ? (ElemTemplateElement)class_.newInstance() : (ElemTemplateElement)class_.newInstance()));
                object.setDOMBackPointer(stylesheetHandler.getOriginatingNode());
                object.setLocaterInfo(stylesheetHandler.getLocator());
                object.setPrefixes(stylesheetHandler.getNamespaceSupport(), bl);
                if (object instanceof ElemLiteralResult) {
                    ((ElemLiteralResult)object).setNamespace(string);
                    ((ElemLiteralResult)object).setLocalName(string2);
                    ((ElemLiteralResult)object).setRawName(string3);
                    ((ElemLiteralResult)object).setIsLiteralResultAsStylesheet(bl2);
                }
            }
            catch (InstantiationException instantiationException) {
                stylesheetHandler.error("ER_FAILED_CREATING_ELEMLITRSLT", null, instantiationException);
            }
            catch (IllegalAccessException illegalAccessException) {
                stylesheetHandler.error("ER_FAILED_CREATING_ELEMLITRSLT", null, illegalAccessException);
            }
            this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, (ElemTemplateElement)object);
            if (!bl3 && object instanceof ElemLiteralResult && (bl3 = ((ElemLiteralResult)object).containsExtensionElementURI(string))) {
                object = new ElemExtensionCall();
                object.setLocaterInfo(stylesheetHandler.getLocator());
                object.setPrefixes(stylesheetHandler.getNamespaceSupport());
                ((ElemLiteralResult)object).setNamespace(string);
                ((ElemLiteralResult)object).setLocalName(string2);
                ((ElemLiteralResult)object).setRawName(string3);
                this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, (ElemTemplateElement)object);
            }
            this.appendAndPush(stylesheetHandler, (ElemTemplateElement)object);
        }
        catch (TransformerException transformerException) {
            throw new SAXException(transformerException);
        }
    }

    protected Stylesheet getStylesheetRoot(StylesheetHandler stylesheetHandler) throws TransformerConfigurationException {
        StylesheetRoot stylesheetRoot = new StylesheetRoot(stylesheetHandler.getSchema(), stylesheetHandler.getStylesheetProcessor().getErrorListener());
        if (stylesheetHandler.getStylesheetProcessor().isSecureProcessing()) {
            stylesheetRoot.setSecureProcessing(true);
        }
        return stylesheetRoot;
    }

    public void endElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3) throws SAXException {
        ElemTemplateElement elemTemplateElement = stylesheetHandler.getElemTemplateElement();
        if (elemTemplateElement instanceof ElemLiteralResult && ((ElemLiteralResult)elemTemplateElement).getIsLiteralResultAsStylesheet()) {
            stylesheetHandler.popStylesheet();
        }
        super.endElement(stylesheetHandler, string, string2, string3);
    }

    private boolean declaredXSLNS(Stylesheet stylesheet) {
        List list = stylesheet.getDeclaredPrefixes();
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            XMLNSDecl xMLNSDecl = (XMLNSDecl)list.get(i2);
            if (!xMLNSDecl.getURI().equals("http://www.w3.org/1999/XSL/Transform")) continue;
            return true;
        }
        return false;
    }
}

