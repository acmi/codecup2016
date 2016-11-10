/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.trace;

import java.util.EventListener;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class SelectionEvent
implements EventListener {
    public final ElemTemplateElement m_styleNode;
    public final TransformerImpl m_processor;
    public final Node m_sourceNode;
    public final String m_attributeName;
    public final XPath m_xpath;
    public final XObject m_selection;

    public SelectionEvent(TransformerImpl transformerImpl, Node node, ElemTemplateElement elemTemplateElement, String string, XPath xPath, XObject xObject) {
        this.m_processor = transformerImpl;
        this.m_sourceNode = node;
        this.m_styleNode = elemTemplateElement;
        this.m_attributeName = string;
        this.m_xpath = xPath;
        this.m_selection = xObject;
    }
}

