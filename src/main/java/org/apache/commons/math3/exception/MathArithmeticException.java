/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class MathArithmeticException
extends ArithmeticException {
    private final ExceptionContext context;

    public MathArithmeticException() {
        this.context = new ExceptionContext(this);
        this.context.addMessage(LocalizedFormats.ARITHMETIC_EXCEPTION, new Object[0]);
    }

    public /* varargs */ MathArithmeticException(Localizable localizable, Object ... arrobject) {
        this.context = new ExceptionContext(this);
        this.context.addMessage(localizable, arrobject);
    }

    public String getMessage() {
        return this.context.getMessage();
    }

    public String getLocalizedMessage() {
        return this.context.getLocalizedMessage();
    }
}

