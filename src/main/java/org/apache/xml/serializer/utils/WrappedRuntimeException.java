/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer.utils;

public final class WrappedRuntimeException
extends RuntimeException {
    private Exception m_exception;

    public WrappedRuntimeException(Exception exception) {
        super(exception.getMessage());
        this.m_exception = exception;
    }

    public WrappedRuntimeException(String string, Exception exception) {
        super(string);
        this.m_exception = exception;
    }
}

