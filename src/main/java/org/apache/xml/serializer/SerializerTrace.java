/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import org.xml.sax.Attributes;

public interface SerializerTrace {
    public boolean hasTraceListeners();

    public void fireGenerateEvent(int var1);

    public void fireGenerateEvent(int var1, String var2, Attributes var3);

    public void fireGenerateEvent(int var1, char[] var2, int var3, int var4);

    public void fireGenerateEvent(int var1, String var2, String var3);

    public void fireGenerateEvent(int var1, String var2);
}

