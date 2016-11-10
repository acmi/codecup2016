/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemText;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

public class ProcessorCharacters
extends XSLTElementProcessor {
    static final long serialVersionUID = 8632900007814162650L;
    protected Node m_firstBackPointer = null;
    private StringBuffer m_accumulator = new StringBuffer();
    private ElemText m_xslTextElement;

    public void startNonText(StylesheetHandler stylesheetHandler) throws SAXException {
        int n2;
        if (this == stylesheetHandler.getCurrentProcessor()) {
            stylesheetHandler.popProcessor();
        }
        if ((n2 = this.m_accumulator.length()) > 0 && (null != this.m_xslTextElement || !XMLCharacterRecognizer.isWhiteSpace(this.m_accumulator)) || stylesheetHandler.isSpacePreserve()) {
            ElemTextLiteral elemTextLiteral = new ElemTextLiteral();
            elemTextLiteral.setDOMBackPointer(this.m_firstBackPointer);
            elemTextLiteral.setLocaterInfo(stylesheetHandler.getLocator());
            try {
                elemTextLiteral.setPrefixes(stylesheetHandler.getNamespaceSupport());
            }
            catch (TransformerException transformerException) {
                throw new SAXException(transformerException);
            }
            boolean bl = null != this.m_xslTextElement ? this.m_xslTextElement.getDisableOutputEscaping() : false;
            elemTextLiteral.setDisableOutputEscaping(bl);
            elemTextLiteral.setPreserveSpace(true);
            char[] arrc = new char[n2];
            this.m_accumulator.getChars(0, n2, arrc, 0);
            elemTextLiteral.setChars(arrc);
            ElemTemplateElement elemTemplateElement = stylesheetHandler.getElemTemplateElement();
            elemTemplateElement.appendChild(elemTextLiteral);
        }
        this.m_accumulator.setLength(0);
        this.m_firstBackPointer = null;
    }

    public void characters(StylesheetHandler stylesheetHandler, char[] arrc, int n2, int n3) throws SAXException {
        this.m_accumulator.append(arrc, n2, n3);
        if (null == this.m_firstBackPointer) {
            this.m_firstBackPointer = stylesheetHandler.getOriginatingNode();
        }
        if (this != stylesheetHandler.getCurrentProcessor()) {
            stylesheetHandler.pushProcessor(this);
        }
    }

    public void endElement(StylesheetHandler stylesheetHandler, String string, String string2, String string3) throws SAXException {
        this.startNonText(stylesheetHandler);
        stylesheetHandler.getCurrentProcessor().endElement(stylesheetHandler, string, string2, string3);
        stylesheetHandler.popProcessor();
    }

    void setXslTextElement(ElemText elemText) {
        this.m_xslTextElement = elemText;
    }
}

