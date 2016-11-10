/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.trace;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.xalan.transformer.TransformerImpl;

public class ExtensionEvent {
    public static final int DEFAULT_CONSTRUCTOR = 0;
    public static final int METHOD = 1;
    public static final int CONSTRUCTOR = 2;
    public final int m_callType;
    public final TransformerImpl m_transformer;
    public final Object m_method;
    public final Object m_instance;
    public final Object[] m_arguments;

    public ExtensionEvent(TransformerImpl transformerImpl, Method method, Object object, Object[] arrobject) {
        this.m_transformer = transformerImpl;
        this.m_method = method;
        this.m_instance = object;
        this.m_arguments = arrobject;
        this.m_callType = 1;
    }

    public ExtensionEvent(TransformerImpl transformerImpl, Constructor constructor, Object[] arrobject) {
        this.m_transformer = transformerImpl;
        this.m_instance = null;
        this.m_arguments = arrobject;
        this.m_method = constructor;
        this.m_callType = 2;
    }

    public ExtensionEvent(TransformerImpl transformerImpl, Class class_) {
        this.m_transformer = transformerImpl;
        this.m_instance = null;
        this.m_arguments = null;
        this.m_method = class_;
        this.m_callType = 0;
    }
}

