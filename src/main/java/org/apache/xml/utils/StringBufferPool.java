/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.ObjectPool;

public class StringBufferPool {
    private static ObjectPool m_stringBufPool;
    static Class class$org$apache$xml$utils$FastStringBuffer;

    public static synchronized FastStringBuffer get() {
        return (FastStringBuffer)m_stringBufPool.getInstance();
    }

    public static synchronized void free(FastStringBuffer fastStringBuffer) {
        fastStringBuffer.setLength(0);
        m_stringBufPool.freeInstance(fastStringBuffer);
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static {
        Class class_ = class$org$apache$xml$utils$FastStringBuffer == null ? (StringBufferPool.class$org$apache$xml$utils$FastStringBuffer = StringBufferPool.class$("org.apache.xml.utils.FastStringBuffer")) : class$org$apache$xml$utils$FastStringBuffer;
        m_stringBufPool = new ObjectPool(class_);
    }
}

