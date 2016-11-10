/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.NamespaceAlias;
import org.apache.xalan.templates.Stylesheet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class ProcessorNamespaceAlias
extends XSLTElementProcessor {
    static final long serialVersionUID = -6309867839007018964L;

    ProcessorNamespaceAlias() {
    }

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        String string4;
        NamespaceAlias namespaceAlias = new NamespaceAlias(stylesheetHandler.nextUid());
        this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, namespaceAlias);
        String string5 = namespaceAlias.getStylesheetPrefix();
        if (string5.equals("#default")) {
            string5 = "";
            namespaceAlias.setStylesheetPrefix(string5);
        }
        String string6 = stylesheetHandler.getNamespaceForPrefix(string5);
        namespaceAlias.setStylesheetNamespace(string6);
        string5 = namespaceAlias.getResultPrefix();
        if (string5.equals("#default")) {
            string5 = "";
            namespaceAlias.setResultPrefix(string5);
            string4 = stylesheetHandler.getNamespaceForPrefix(string5);
            if (null == string4) {
                stylesheetHandler.error("ER_INVALID_NAMESPACE_URI_VALUE_FOR_RESULT_PREFIX_FOR_DEFAULT", null, null);
            }
        } else {
            string4 = stylesheetHandler.getNamespaceForPrefix(string5);
            if (null == string4) {
                stylesheetHandler.error("ER_INVALID_SET_NAMESPACE_URI_VALUE_FOR_RESULT_PREFIX", new Object[]{string5}, null);
            }
        }
        namespaceAlias.setResultNamespace(string4);
        stylesheetHandler.getStylesheet().setNamespaceAlias(namespaceAlias);
        stylesheetHandler.getStylesheet().appendChild(namespaceAlias);
    }
}

