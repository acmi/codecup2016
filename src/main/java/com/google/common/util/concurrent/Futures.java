/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.GwtFuturesCatchingSpecialization;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Uninterruptibles;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Futures
extends GwtFuturesCatchingSpecialization {
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>(){};

    public static <V> ListenableFuture<V> immediateFuture(V v2) {
        if (v2 == null) {
            ImmediateSuccessfulFuture<Object> immediateSuccessfulFuture = ImmediateSuccessfulFuture.NULL;
            return immediateSuccessfulFuture;
        }
        return new ImmediateSuccessfulFuture<V>(v2);
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFailedFuture(throwable);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(function);
        ChainingFuture<? super I, ? extends O> chainingFuture = new ChainingFuture<I, O>(listenableFuture, function);
        listenableFuture.addListener(chainingFuture, MoreExecutors.directExecutor());
        return chainingFuture;
    }

    private static final class ChainingFuture<I, O>
    extends AbstractChainingFuture<I, O, Function<? super I, ? extends O>> {
        ChainingFuture(ListenableFuture<? extends I> listenableFuture, Function<? super I, ? extends O> function) {
            super(listenableFuture, function);
        }

        @Override
        void doTransform(Function<? super I, ? extends O> function, I i2) {
            this.set(function.apply(i2));
        }
    }

    private static abstract class AbstractChainingFuture<I, O, F>
    extends AbstractFuture.TrustedFuture<O>
    implements Runnable {
        ListenableFuture<? extends I> inputFuture;
        F function;

        AbstractChainingFuture(ListenableFuture<? extends I> listenableFuture, F f2) {
            this.inputFuture = Preconditions.checkNotNull(listenableFuture);
            this.function = Preconditions.checkNotNull(f2);
        }

        @Override
        public final void run() {
            try {
                I i2;
                ListenableFuture<? extends I> listenableFuture = this.inputFuture;
                F f2 = this.function;
                if (this.isCancelled() | listenableFuture == null | f2 == null) {
                    return;
                }
                this.inputFuture = null;
                this.function = null;
                try {
                    i2 = Uninterruptibles.getUninterruptibly(listenableFuture);
                }
                catch (CancellationException cancellationException) {
                    this.cancel(false);
                    return;
                }
                catch (ExecutionException executionException) {
                    this.setException(executionException.getCause());
                    return;
                }
                this.doTransform(f2, i2);
            }
            catch (UndeclaredThrowableException undeclaredThrowableException) {
                this.setException(undeclaredThrowableException.getCause());
            }
            catch (Throwable throwable) {
                this.setException(throwable);
            }
        }

        abstract void doTransform(F var1, I var2) throws Exception;

        @Override
        final void done() {
            this.maybePropagateCancellation(this.inputFuture);
            this.inputFuture = null;
            this.function = null;
        }
    }

    private static class ImmediateFailedFuture<V>
    extends ImmediateFuture<V> {
        private final Throwable thrown;

        ImmediateFailedFuture(Throwable throwable) {
            super();
            this.thrown = throwable;
        }

        @Override
        public V get() throws ExecutionException {
            throw new ExecutionException(this.thrown);
        }
    }

    private static class ImmediateSuccessfulFuture<V>
    extends ImmediateFuture<V> {
        static final ImmediateSuccessfulFuture<Object> NULL = new ImmediateSuccessfulFuture<Object>(null);
        private final V value;

        ImmediateSuccessfulFuture(V v2) {
            super();
            this.value = v2;
        }

        @Override
        public V get() {
            return this.value;
        }
    }

    private static abstract class ImmediateFuture<V>
    implements ListenableFuture<V> {
        private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());

        private ImmediateFuture() {
        }

        @Override
        public void addListener(Runnable runnable, Executor executor) {
            Preconditions.checkNotNull(runnable, "Runnable was null.");
            Preconditions.checkNotNull(executor, "Executor was null.");
            try {
                executor.execute(runnable);
            }
            catch (RuntimeException runtimeException) {
                log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, runtimeException);
            }
        }

        @Override
        public boolean cancel(boolean bl) {
            return false;
        }

        @Override
        public abstract V get() throws ExecutionException;

        @Override
        public V get(long l2, TimeUnit timeUnit) throws ExecutionException {
            Preconditions.checkNotNull(timeUnit);
            return this.get();
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return true;
        }
    }

}

