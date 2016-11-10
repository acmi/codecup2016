/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.trace;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.trace.EndSelectionEvent;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.trace.GenerateEvent;
import org.apache.xalan.trace.SelectionEvent;
import org.apache.xalan.trace.TraceListenerEx3;
import org.apache.xalan.trace.TracerEvent;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class PrintTraceListener
implements TraceListenerEx3 {
    PrintWriter m_pw;
    public boolean m_traceTemplates = false;
    public boolean m_traceElements = false;
    public boolean m_traceGeneration = false;
    public boolean m_traceSelection = false;
    public boolean m_traceExtension = false;
    int m_indent = 0;

    public PrintTraceListener(PrintWriter printWriter) {
        this.m_pw = printWriter;
    }

    public void _trace(TracerEvent tracerEvent) {
        switch (tracerEvent.m_styleNode.getXSLToken()) {
            case 78: {
                if (!this.m_traceElements) break;
                this.m_pw.print(tracerEvent.m_styleNode.getSystemId() + " Line #" + tracerEvent.m_styleNode.getLineNumber() + ", " + "Column #" + tracerEvent.m_styleNode.getColumnNumber() + " -- " + tracerEvent.m_styleNode.getNodeName() + ": ");
                ElemTextLiteral elemTextLiteral = (ElemTextLiteral)tracerEvent.m_styleNode;
                String string = new String(elemTextLiteral.getChars(), 0, elemTextLiteral.getChars().length);
                this.m_pw.println("    " + string.trim());
                break;
            }
            case 19: {
                if (!this.m_traceTemplates && !this.m_traceElements) break;
                ElemTemplate elemTemplate = (ElemTemplate)tracerEvent.m_styleNode;
                this.m_pw.print(elemTemplate.getSystemId() + " Line #" + elemTemplate.getLineNumber() + ", " + "Column #" + elemTemplate.getColumnNumber() + ": " + elemTemplate.getNodeName() + " ");
                if (null != elemTemplate.getMatch()) {
                    this.m_pw.print("match='" + elemTemplate.getMatch().getPatternString() + "' ");
                }
                if (null != elemTemplate.getName()) {
                    this.m_pw.print("name='" + elemTemplate.getName() + "' ");
                }
                this.m_pw.println();
                break;
            }
            default: {
                if (!this.m_traceElements) break;
                this.m_pw.println(tracerEvent.m_styleNode.getSystemId() + " Line #" + tracerEvent.m_styleNode.getLineNumber() + ", " + "Column #" + tracerEvent.m_styleNode.getColumnNumber() + ": " + tracerEvent.m_styleNode.getNodeName());
            }
        }
    }

    public void trace(TracerEvent tracerEvent) {
        this._trace(tracerEvent);
    }

    public void traceEnd(TracerEvent tracerEvent) {
    }

    public void selected(SelectionEvent selectionEvent) throws TransformerException {
        if (this.m_traceSelection) {
            Object object;
            ElemTemplateElement elemTemplateElement = selectionEvent.m_styleNode;
            Node node = selectionEvent.m_sourceNode;
            SourceLocator sourceLocator = null;
            if (node instanceof DTMNodeProxy) {
                object = ((DTMNodeProxy)node).getDTMNodeNumber();
                sourceLocator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor((int)object);
            }
            if (sourceLocator != null) {
                this.m_pw.println("Selected source node '" + node.getNodeName() + "', at " + sourceLocator);
            } else {
                this.m_pw.println("Selected source node '" + node.getNodeName() + "'");
            }
            if (selectionEvent.m_styleNode.getLineNumber() == 0) {
                ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getParentElem();
                if (elemTemplateElement2 == elemTemplateElement.getStylesheetRoot().getDefaultRootRule()) {
                    this.m_pw.print("(default root rule) ");
                } else if (elemTemplateElement2 == elemTemplateElement.getStylesheetRoot().getDefaultTextRule()) {
                    this.m_pw.print("(default text rule) ");
                } else if (elemTemplateElement2 == elemTemplateElement.getStylesheetRoot().getDefaultRule()) {
                    this.m_pw.print("(default rule) ");
                }
                this.m_pw.print(elemTemplateElement.getNodeName() + ", " + selectionEvent.m_attributeName + "='" + selectionEvent.m_xpath.getPatternString() + "': ");
            } else {
                this.m_pw.print(selectionEvent.m_styleNode.getSystemId() + " Line #" + selectionEvent.m_styleNode.getLineNumber() + ", " + "Column #" + selectionEvent.m_styleNode.getColumnNumber() + ": " + elemTemplateElement.getNodeName() + ", " + selectionEvent.m_attributeName + "='" + selectionEvent.m_xpath.getPatternString() + "': ");
            }
            if (selectionEvent.m_selection.getType() == 4) {
                this.m_pw.println();
                object = selectionEvent.m_selection.iter();
                int n2 = -1;
                n2 = object.getCurrentPos();
                object.setShouldCacheNodes(true);
                DTMIterator dTMIterator = null;
                try {
                    dTMIterator = object.cloneWithReset();
                }
                catch (CloneNotSupportedException cloneNotSupportedException) {
                    this.m_pw.println("     [Can't trace nodelist because it it threw a CloneNotSupportedException]");
                    return;
                }
                int n3 = dTMIterator.nextNode();
                if (-1 == n3) {
                    this.m_pw.println("     [empty node list]");
                } else {
                    while (-1 != n3) {
                        DTM dTM = selectionEvent.m_processor.getXPathContext().getDTM(n3);
                        this.m_pw.print("     ");
                        this.m_pw.print(Integer.toHexString(n3));
                        this.m_pw.print(": ");
                        this.m_pw.println(dTM.getNodeName(n3));
                        n3 = dTMIterator.nextNode();
                    }
                }
                object.runTo(-1);
                object.setCurrentPos(n2);
            } else {
                this.m_pw.println(selectionEvent.m_selection.str());
            }
        }
    }

    public void selectEnd(EndSelectionEvent endSelectionEvent) throws TransformerException {
    }

    public void generated(GenerateEvent generateEvent) {
        if (this.m_traceGeneration) {
            switch (generateEvent.m_eventtype) {
                case 1: {
                    this.m_pw.println("STARTDOCUMENT");
                    break;
                }
                case 2: {
                    this.m_pw.println("ENDDOCUMENT");
                    break;
                }
                case 3: {
                    this.m_pw.println("STARTELEMENT: " + generateEvent.m_name);
                    break;
                }
                case 4: {
                    this.m_pw.println("ENDELEMENT: " + generateEvent.m_name);
                    break;
                }
                case 5: {
                    String string = new String(generateEvent.m_characters, generateEvent.m_start, generateEvent.m_length);
                    this.m_pw.println("CHARACTERS: " + string);
                    break;
                }
                case 10: {
                    String string = new String(generateEvent.m_characters, generateEvent.m_start, generateEvent.m_length);
                    this.m_pw.println("CDATA: " + string);
                    break;
                }
                case 8: {
                    this.m_pw.println("COMMENT: " + generateEvent.m_data);
                    break;
                }
                case 7: {
                    this.m_pw.println("PI: " + generateEvent.m_name + ", " + generateEvent.m_data);
                    break;
                }
                case 9: {
                    this.m_pw.println("ENTITYREF: " + generateEvent.m_name);
                    break;
                }
                case 6: {
                    this.m_pw.println("IGNORABLEWHITESPACE");
                }
            }
        }
    }

    public void extension(ExtensionEvent extensionEvent) {
        if (this.m_traceExtension) {
            switch (extensionEvent.m_callType) {
                case 0: {
                    this.m_pw.println("EXTENSION: " + ((Class)extensionEvent.m_method).getName() + "#<init>");
                    break;
                }
                case 1: {
                    this.m_pw.println("EXTENSION: " + ((Method)extensionEvent.m_method).getDeclaringClass().getName() + "#" + ((Method)extensionEvent.m_method).getName());
                    break;
                }
                case 2: {
                    this.m_pw.println("EXTENSION: " + ((Constructor)extensionEvent.m_method).getDeclaringClass().getName() + "#<init>");
                }
            }
        }
    }

    public void extensionEnd(ExtensionEvent extensionEvent) {
    }
}

