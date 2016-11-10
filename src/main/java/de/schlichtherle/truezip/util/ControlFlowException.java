/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

public abstract class ControlFlowException
extends Error {
    private static final String TRACEABLE_PROPERTY_KEY = ControlFlowException.class.getName() + ".traceable";

    public ControlFlowException() {
        this(null);
    }

    public ControlFlowException(Throwable throwable) {
        super(throwable);
    }

    public static boolean isTraceable() {
        return Boolean.getBoolean(TRACEABLE_PROPERTY_KEY);
    }

    @Override
    public Throwable fillInStackTrace() {
        return ControlFlowException.isTraceable() ? super.fillInStackTrace() : this /* !! */ ;
    }
}

