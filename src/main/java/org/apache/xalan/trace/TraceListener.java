/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.trace;

import java.util.EventListener;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.GenerateEvent;
import org.apache.xalan.trace.SelectionEvent;
import org.apache.xalan.trace.TracerEvent;

public interface TraceListener
extends EventListener {
    public void trace(TracerEvent var1);

    public void selected(SelectionEvent var1) throws TransformerException;

    public void generated(GenerateEvent var1);
}

