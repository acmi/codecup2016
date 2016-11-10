/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.trace;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.trace.SelectionEvent;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class EndSelectionEvent
extends SelectionEvent {
    public EndSelectionEvent(TransformerImpl transformerImpl, Node node, ElemTemplateElement elemTemplateElement, String string, XPath xPath, XObject xObject) {
        super(transformerImpl, node, elemTemplateElement, string, xPath, xObject);
    }
}

