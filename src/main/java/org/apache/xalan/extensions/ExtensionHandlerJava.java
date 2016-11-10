/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.util.Hashtable;
import org.apache.xalan.extensions.ExtensionHandler;

public abstract class ExtensionHandlerJava
extends ExtensionHandler {
    protected String m_className = "";
    private Hashtable m_cachedMethods = new Hashtable();

    protected ExtensionHandlerJava(String string, String string2, String string3) {
        super(string, string2);
        this.m_className = string3;
    }

    public Object getFromCache(Object object, Object object2, Object[] arrobject) {
        return this.m_cachedMethods.get(object);
    }

    public Object putToCache(Object object, Object object2, Object[] arrobject, Object object3) {
        return this.m_cachedMethods.put(object, object3);
    }
}

