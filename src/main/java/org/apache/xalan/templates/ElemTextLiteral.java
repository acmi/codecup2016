/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.xml.sax.SAXException;

public class ElemTextLiteral
extends ElemTemplateElement {
    static final long serialVersionUID = -7872620006767660088L;
    private boolean m_preserveSpace;
    private char[] m_ch;
    private String m_str;
    private boolean m_disableOutputEscaping = false;

    public void setPreserveSpace(boolean bl) {
        this.m_preserveSpace = bl;
    }

    public boolean getPreserveSpace() {
        return this.m_preserveSpace;
    }

    public void setChars(char[] arrc) {
        this.m_ch = arrc;
    }

    public char[] getChars() {
        return this.m_ch;
    }

    public synchronized String getNodeValue() {
        if (null == this.m_str) {
            this.m_str = new String(this.m_ch);
        }
        return this.m_str;
    }

    public void setDisableOutputEscaping(boolean bl) {
        this.m_disableOutputEscaping = bl;
    }

    public boolean getDisableOutputEscaping() {
        return this.m_disableOutputEscaping;
    }

    public int getXSLToken() {
        return 78;
    }

    public String getNodeName() {
        return "#Text";
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        try {
            SerializationHandler serializationHandler = transformerImpl.getResultTreeHandler();
            if (transformerImpl.getDebug()) {
                serializationHandler.flushPending();
                transformerImpl.getTraceManager().fireTraceEvent(this);
            }
            if (this.m_disableOutputEscaping) {
                serializationHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
            }
            serializationHandler.characters(this.m_ch, 0, this.m_ch.length);
            if (this.m_disableOutputEscaping) {
                serializationHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
            }
        }
        catch (SAXException sAXException) {
            throw new TransformerException(sAXException);
        }
        finally {
            if (transformerImpl.getDebug()) {
                try {
                    transformerImpl.getResultTreeHandler().flushPending();
                    transformerImpl.getTraceManager().fireTraceEndEvent(this);
                }
                catch (SAXException sAXException) {
                    throw new TransformerException(sAXException);
                }
            }
        }
    }
}

