/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public class WrappedRuntimeException
extends RuntimeException {
    private Exception m_exception;

    public WrappedRuntimeException(Exception exception) {
        super(exception.getMessage());
        this.m_exception = exception;
    }

    public Exception getException() {
        return this.m_exception;
    }
}

