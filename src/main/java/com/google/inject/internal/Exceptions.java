/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

class Exceptions {
    Exceptions() {
    }

    public static RuntimeException rethrowCause(Throwable throwable) {
        Throwable throwable2 = throwable;
        if (throwable2.getCause() != null) {
            throwable2 = throwable2.getCause();
        }
        return Exceptions.rethrow(throwable2);
    }

    public static RuntimeException rethrow(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException)throwable;
        }
        if (throwable instanceof Error) {
            throw (Error)throwable;
        }
        throw new UnhandledCheckedUserException(throwable);
    }

    static class UnhandledCheckedUserException
    extends RuntimeException {
        public UnhandledCheckedUserException(Throwable throwable) {
            super(throwable);
        }
    }

}

