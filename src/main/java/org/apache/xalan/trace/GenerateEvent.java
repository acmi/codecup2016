/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.trace;

import java.util.EventListener;
import org.apache.xalan.transformer.TransformerImpl;
import org.xml.sax.Attributes;

public class GenerateEvent
implements EventListener {
    public TransformerImpl m_processor;
    public int m_eventtype;
    public char[] m_characters;
    public int m_start;
    public int m_length;
    public String m_name;
    public String m_data;
    public Attributes m_atts;

    public GenerateEvent(TransformerImpl transformerImpl, int n2) {
        this.m_processor = transformerImpl;
        this.m_eventtype = n2;
    }

    public GenerateEvent(TransformerImpl transformerImpl, int n2, String string, Attributes attributes) {
        this.m_name = string;
        this.m_atts = attributes;
        this.m_processor = transformerImpl;
        this.m_eventtype = n2;
    }

    public GenerateEvent(TransformerImpl transformerImpl, int n2, char[] arrc, int n3, int n4) {
        this.m_characters = arrc;
        this.m_start = n3;
        this.m_length = n4;
        this.m_processor = transformerImpl;
        this.m_eventtype = n2;
    }

    public GenerateEvent(TransformerImpl transformerImpl, int n2, String string, String string2) {
        this.m_name = string;
        this.m_data = string2;
        this.m_processor = transformerImpl;
        this.m_eventtype = n2;
    }

    public GenerateEvent(TransformerImpl transformerImpl, int n2, String string) {
        this.m_data = string;
        this.m_processor = transformerImpl;
        this.m_eventtype = n2;
    }
}

