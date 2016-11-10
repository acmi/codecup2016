/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.trace;

import java.lang.reflect.Method;
import java.util.TooManyListenersException;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.trace.EndSelectionEvent;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.trace.GenerateEvent;
import org.apache.xalan.trace.SelectionEvent;
import org.apache.xalan.trace.TraceListener;
import org.apache.xalan.trace.TraceListenerEx;
import org.apache.xalan.trace.TraceListenerEx2;
import org.apache.xalan.trace.TraceListenerEx3;
import org.apache.xalan.trace.TracerEvent;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class TraceManager {
    private TransformerImpl m_transformer;
    private Vector m_traceListeners = null;

    public TraceManager(TransformerImpl transformerImpl) {
        this.m_transformer = transformerImpl;
    }

    public void addTraceListener(TraceListener traceListener) throws TooManyListenersException {
        this.m_transformer.setDebug(true);
        if (null == this.m_traceListeners) {
            this.m_traceListeners = new Vector();
        }
        this.m_traceListeners.addElement(traceListener);
    }

    public void removeTraceListener(TraceListener traceListener) {
        if (null != this.m_traceListeners) {
            this.m_traceListeners.removeElement(traceListener);
            if (0 == this.m_traceListeners.size()) {
                this.m_traceListeners = null;
            }
        }
    }

    public void fireGenerateEvent(GenerateEvent generateEvent) {
        if (null != this.m_traceListeners) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                traceListener.generated(generateEvent);
            }
        }
    }

    public boolean hasTraceListeners() {
        return null != this.m_traceListeners;
    }

    public void fireTraceEvent(ElemTemplateElement elemTemplateElement) {
        if (this.hasTraceListeners()) {
            int n2 = this.m_transformer.getXPathContext().getCurrentNode();
            Node node = this.getDOMNodeFromDTM(n2);
            this.fireTraceEvent(new TracerEvent(this.m_transformer, node, this.m_transformer.getMode(), elemTemplateElement));
        }
    }

    public void fireTraceEndEvent(ElemTemplateElement elemTemplateElement) {
        if (this.hasTraceListeners()) {
            int n2 = this.m_transformer.getXPathContext().getCurrentNode();
            Node node = this.getDOMNodeFromDTM(n2);
            this.fireTraceEndEvent(new TracerEvent(this.m_transformer, node, this.m_transformer.getMode(), elemTemplateElement));
        }
    }

    public void fireTraceEndEvent(TracerEvent tracerEvent) {
        if (this.hasTraceListeners()) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                if (!(traceListener instanceof TraceListenerEx2)) continue;
                ((TraceListenerEx2)traceListener).traceEnd(tracerEvent);
            }
        }
    }

    public void fireTraceEvent(TracerEvent tracerEvent) {
        if (this.hasTraceListeners()) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                traceListener.trace(tracerEvent);
            }
        }
    }

    public void fireSelectedEvent(int n2, ElemTemplateElement elemTemplateElement, String string, XPath xPath, XObject xObject) throws TransformerException {
        if (this.hasTraceListeners()) {
            Node node = this.getDOMNodeFromDTM(n2);
            this.fireSelectedEvent(new SelectionEvent(this.m_transformer, node, elemTemplateElement, string, xPath, xObject));
        }
    }

    public void fireSelectedEndEvent(int n2, ElemTemplateElement elemTemplateElement, String string, XPath xPath, XObject xObject) throws TransformerException {
        if (this.hasTraceListeners()) {
            Node node = this.getDOMNodeFromDTM(n2);
            this.fireSelectedEndEvent(new EndSelectionEvent(this.m_transformer, node, elemTemplateElement, string, xPath, xObject));
        }
    }

    public void fireSelectedEndEvent(EndSelectionEvent endSelectionEvent) throws TransformerException {
        if (this.hasTraceListeners()) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                if (!(traceListener instanceof TraceListenerEx)) continue;
                ((TraceListenerEx)traceListener).selectEnd(endSelectionEvent);
            }
        }
    }

    public void fireSelectedEvent(SelectionEvent selectionEvent) throws TransformerException {
        if (this.hasTraceListeners()) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                traceListener.selected(selectionEvent);
            }
        }
    }

    public void fireExtensionEndEvent(Method method, Object object, Object[] arrobject) {
        ExtensionEvent extensionEvent = new ExtensionEvent(this.m_transformer, method, object, arrobject);
        if (this.hasTraceListeners()) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                if (!(traceListener instanceof TraceListenerEx3)) continue;
                ((TraceListenerEx3)traceListener).extensionEnd(extensionEvent);
            }
        }
    }

    public void fireExtensionEvent(Method method, Object object, Object[] arrobject) {
        ExtensionEvent extensionEvent = new ExtensionEvent(this.m_transformer, method, object, arrobject);
        if (this.hasTraceListeners()) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                if (!(traceListener instanceof TraceListenerEx3)) continue;
                ((TraceListenerEx3)traceListener).extension(extensionEvent);
            }
        }
    }

    public void fireExtensionEndEvent(ExtensionEvent extensionEvent) {
        if (this.hasTraceListeners()) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                if (!(traceListener instanceof TraceListenerEx3)) continue;
                ((TraceListenerEx3)traceListener).extensionEnd(extensionEvent);
            }
        }
    }

    public void fireExtensionEvent(ExtensionEvent extensionEvent) {
        if (this.hasTraceListeners()) {
            int n2 = this.m_traceListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                TraceListener traceListener = (TraceListener)this.m_traceListeners.elementAt(i2);
                if (!(traceListener instanceof TraceListenerEx3)) continue;
                ((TraceListenerEx3)traceListener).extension(extensionEvent);
            }
        }
    }

    private Node getDOMNodeFromDTM(int n2) {
        DTM dTM = this.m_transformer.getXPathContext().getDTM(n2);
        Node node = dTM == null ? null : dTM.getNode(n2);
        return node;
    }
}

