/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.util.Vector;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.WhitespaceInfoPaths;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.WhiteSpaceInfo;
import org.apache.xpath.XPath;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class ProcessorPreserveSpace
extends XSLTElementProcessor {
    static final long serialVersionUID = -5552836470051177302L;

    ProcessorPreserveSpace() {
    }

    public void startElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3, Attributes attributes) throws SAXException {
        Stylesheet stylesheet = stylesheetHandler.getStylesheet();
        WhitespaceInfoPaths whitespaceInfoPaths = new WhitespaceInfoPaths(stylesheet);
        this.setPropertiesFromAttributes(stylesheetHandler, string3, attributes, whitespaceInfoPaths);
        Vector vector = whitespaceInfoPaths.getElements();
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            WhiteSpaceInfo whiteSpaceInfo = new WhiteSpaceInfo((XPath)vector.elementAt(i2), false, stylesheet);
            whiteSpaceInfo.setUid(stylesheetHandler.nextUid());
            stylesheet.setPreserveSpaces(whiteSpaceInfo);
        }
        whitespaceInfoPaths.clearElements();
    }
}

