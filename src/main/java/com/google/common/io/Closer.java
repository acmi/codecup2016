/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.io.Closeables;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Closer
implements Closeable {
    private static final Suppressor SUPPRESSOR = SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE;
    final Suppressor suppressor;
    private final Deque<Closeable> stack = new ArrayDeque<Closeable>(4);
    private Throwable thrown;

    public static Closer create() {
        return new Closer(SUPPRESSOR);
    }

    Closer(Suppressor suppressor) {
        this.suppressor = Preconditions.checkNotNull(suppressor);
    }

    public <C extends Closeable> C register(C c2) {
        if (c2 != null) {
            this.stack.addFirst((Closeable)c2);
        }
        return c2;
    }

    public RuntimeException rethrow(Throwable throwable) throws IOException {
        Preconditions.checkNotNull(throwable);
        this.thrown = throwable;
        Throwables.propagateIfPossible(throwable, IOException.class);
        throw new RuntimeException(throwable);
    }

    @Override
    public void close() throws IOException {
        Throwable throwable = this.thrown;
        while (!this.stack.isEmpty()) {
            Closeable closeable = this.stack.removeFirst();
            try {
                closeable.close();
            }
            catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = throwable2;
                    continue;
                }
                this.suppressor.suppress(closeable, throwable, throwable2);
            }
        }
        if (this.thrown == null && throwable != null) {
            Throwables.propagateIfPossible(throwable, IOException.class);
            throw new AssertionError(throwable);
        }
    }

    static final class SuppressingSuppressor
    implements Suppressor {
        static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
        static final Method addSuppressed = SuppressingSuppressor.getAddSuppressed();

        SuppressingSuppressor() {
        }

        static boolean isAvailable() {
            return addSuppressed != null;
        }

        private static Method getAddSuppressed() {
            try {
                return Throwable.class.getMethod("addSuppressed", Throwable.class);
            }
            catch (Throwable throwable) {
                return null;
            }
        }

        @Override
        public void suppress(Closeable closeable, Throwable throwable, Throwable throwable2) {
            if (throwable == throwable2) {
                return;
            }
            try {
                addSuppressed.invoke(throwable, throwable2);
            }
            catch (Throwable throwable3) {
                LoggingSuppressor.INSTANCE.suppress(closeable, throwable, throwable2);
            }
        }
    }

    static final class LoggingSuppressor
    implements Suppressor {
        static final LoggingSuppressor INSTANCE = new LoggingSuppressor();

        LoggingSuppressor() {
        }

        @Override
        public void suppress(Closeable closeable, Throwable throwable, Throwable throwable2) {
            Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, throwable2);
        }
    }

    static interface Suppressor {
        public void suppress(Closeable var1, Throwable var2, Throwable var3);
    }

}

