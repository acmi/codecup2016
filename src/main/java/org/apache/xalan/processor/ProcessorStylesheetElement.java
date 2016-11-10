/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.processor.XSLTSchema;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.SAXSourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

public class ProcessorStylesheetElement
extends XSLTElementProcessor {
    static final long serialVersionUID = -877798927447840792L;

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        super.startElement(stylesheetHandler, string, string2, string3, attributes);
        try {
            Stylesheet stylesheet;
            int n2 = stylesheetHandler.getStylesheetType();
            if (n2 == 1) {
                try {
                    stylesheet = this.getStylesheetRoot(stylesheetHandler);
                }
                catch (TransformerConfigurationException transformerConfigurationException) {
                    throw new TransformerException(transformerConfigurationException);
                }
            } else {
                Stylesheet stylesheet2 = stylesheetHandler.getStylesheet();
                if (n2 == 3) {
                    StylesheetComposed stylesheetComposed = new StylesheetComposed(stylesheet2);
                    stylesheet2.setImport(stylesheetComposed);
                    stylesheet = stylesheetComposed;
                } else {
                    stylesheet = new Stylesheet(stylesheet2);
                    stylesheet2.setInclude(stylesheet);
                }
            }
            stylesheet.setDOMBackPointer(stylesheetHandler.getOriginatingNode());
            stylesheet.setLocaterInfo(stylesheetHandler.getLocator());
            stylesheet.setPrefixes(stylesheetHandler.getNamespaceSupport());
            stylesheetHandler.pushStylesheet(stylesheet);
            this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, stylesheetHandler.getStylesheet());
            stylesheetHandler.pushElemTemplateElement(stylesheetHandler.getStylesheet());
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
        super.endElement(stylesheetHandler, string, string2, string3);
        stylesheetHandler.popElemTemplateElement();
        stylesheetHandler.popStylesheet();
    }
}

