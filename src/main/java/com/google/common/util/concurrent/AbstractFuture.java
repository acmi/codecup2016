/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Uninterruptibles;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

public abstract class AbstractFuture<V>
implements ListenableFuture<V> {
    private static final boolean GENERATE_CANCELLATION_CAUSES;
    private static final Logger log;
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Object NULL;
    private volatile Object value;
    private volatile Listener listeners;
    private volatile Waiter waiters;

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void removeWaiter(Waiter var1_1) {
        var1_1.thread = null;
        block0 : do {
            var2_2 = null;
            var3_3 = this.waiters;
            if (var3_3 == Waiter.TOMBSTONE) {
                return;
            }
            while (var3_3 != null) {
                var4_4 = var3_3.next;
                if (var3_3.thread != null) {
                    var2_2 = var3_3;
                } else if (var2_2 != null) {
                    var2_2.next = var4_4;
                    if (var2_2.thread == null) {
                        continue block0;
                    }
                } else {
                    if (AbstractFuture.ATOMIC_HELPER.casWaiters(this, var3_3, var4_4)) ** break;
                    continue block0;
                }
                var3_3 = var4_4;
            }
            return;
            break;
        } while (true);
    }

    protected AbstractFuture() {
    }

    @Override
    public V get(long l2, TimeUnit timeUnit) throws InterruptedException, TimeoutException, ExecutionException {
        long l3;
        long l4;
        Object object;
        block10 : {
            l3 = timeUnit.toNanos(l2);
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            object = this.value;
            if (object != null & !(object instanceof SetFuture)) {
                return this.getDoneValue(object);
            }
            long l5 = l4 = l3 > 0 ? System.nanoTime() + l3 : 0;
            if (l3 >= 1000) {
                Waiter waiter = this.waiters;
                if (waiter != Waiter.TOMBSTONE) {
                    Waiter waiter2 = new Waiter();
                    do {
                        waiter2.setNext(waiter);
                        if (!ATOMIC_HELPER.casWaiters(this, waiter, waiter2)) continue;
                        do {
                            LockSupport.parkNanos(this, l3);
                            if (Thread.interrupted()) {
                                this.removeWaiter(waiter2);
                                throw new InterruptedException();
                            }
                            object = this.value;
                            if (!(object != null & !(object instanceof SetFuture))) continue;
                            return this.getDoneValue(object);
                        } while ((l3 = l4 - System.nanoTime()) >= 1000);
                        this.removeWaiter(waiter2);
                        break block10;
                    } while ((waiter = this.waiters) != Waiter.TOMBSTONE);
                }
                return this.getDoneValue(this.value);
            }
        }
        while (l3 > 0) {
            object = this.value;
            if (object != null & !(object instanceof SetFuture)) {
                return this.getDoneValue(object);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            l3 = l4 - System.nanoTime();
        }
        throw new TimeoutException();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object object = this.value;
        if (object != null & !(object instanceof SetFuture)) {
            return this.getDoneValue(object);
        }
        Waiter waiter = this.waiters;
        if (waiter != Waiter.TOMBSTONE) {
            Waiter waiter2 = new Waiter();
            do {
                waiter2.setNext(waiter);
                if (!ATOMIC_HELPER.casWaiters(this, waiter, waiter2)) continue;
                do {
                    LockSupport.park(this);
                    if (!Thread.interrupted()) continue;
                    this.removeWaiter(waiter2);
                    throw new InterruptedException();
                } while (!((object = this.value) != null & !(object instanceof SetFuture)));
                return this.getDoneValue(object);
            } while ((waiter = this.waiters) != Waiter.TOMBSTONE);
        }
        return this.getDoneValue(this.value);
    }

    private V getDoneValue(Object object) throws ExecutionException {
        if (object instanceof Cancellation) {
            throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)object).cause);
        }
        if (object instanceof Failure) {
            throw new ExecutionException(((Failure)object).exception);
        }
        if (object == NULL) {
            return null;
        }
        Object object2 = object;
        return (V)object2;
    }

    @Override
    public boolean isDone() {
        Object object = this.value;
        return object != null & !(object instanceof SetFuture);
    }

    @Override
    public boolean isCancelled() {
        Object object = this.value;
        return object instanceof Cancellation;
    }

    @Override
    public boolean cancel(boolean bl) {
        Object object = this.value;
        if (object == null | object instanceof SetFuture) {
            Throwable throwable = GENERATE_CANCELLATION_CAUSES ? this.newCancellationCause() : null;
            Cancellation cancellation = new Cancellation(bl, throwable);
            do {
                if (!ATOMIC_HELPER.casValue(this, object, cancellation)) continue;
                if (bl) {
                    this.interruptTask();
                }
                this.complete();
                if (object instanceof SetFuture) {
                    ((SetFuture)object).future.cancel(bl);
                }
                return true;
            } while ((object = this.value) instanceof SetFuture);
        }
        return false;
    }

    private Throwable newCancellationCause() {
        return new CancellationException("Future.cancel() was called.");
    }

    protected void interruptTask() {
    }

    protected final boolean wasInterrupted() {
        Object object = this.value;
        return object instanceof Cancellation && ((Cancellation)object).wasInterrupted;
    }

    @Override
    public void addListener(Runnable runnable, Executor executor) {
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        Listener listener = this.listeners;
        if (listener != Listener.TOMBSTONE) {
            Listener listener2 = new Listener(runnable, executor);
            do {
                listener2.next = listener;
                if (!ATOMIC_HELPER.casListeners(this, listener, listener2)) continue;
                return;
            } while ((listener = this.listeners) != Listener.TOMBSTONE);
        }
        AbstractFuture.executeListener(runnable, executor);
    }

    protected boolean set(V object) {
        Object object2;
        Object object3 = object2 = object == null ? NULL : object;
        if (ATOMIC_HELPER.casValue(this, null, object2)) {
            this.complete();
            return true;
        }
        return false;
    }

    protected boolean setException(Throwable throwable) {
        Failure failure = new Failure(Preconditions.checkNotNull(throwable));
        if (ATOMIC_HELPER.casValue(this, null, failure)) {
            this.complete();
            return true;
        }
        return false;
    }

    private boolean completeWithFuture(ListenableFuture<? extends V> listenableFuture, Object object) {
        Object object2;
        if (listenableFuture instanceof TrustedFuture) {
            object2 = ((AbstractFuture)listenableFuture).value;
        } else {
            try {
                V v2 = Uninterruptibles.getUninterruptibly(listenableFuture);
                object2 = v2 == null ? NULL : v2;
            }
            catch (ExecutionException executionException) {
                object2 = new Failure(executionException.getCause());
            }
            catch (CancellationException cancellationException) {
                object2 = new Cancellation(false, cancellationException);
            }
            catch (Throwable throwable) {
                object2 = new Failure(throwable);
            }
        }
        if (ATOMIC_HELPER.casValue(this, object, object2)) {
            this.complete();
            return true;
        }
        return false;
    }

    private void complete() {
        Object object = this.clearWaiters();
        while (object != null) {
            object.unpark();
            object = object.next;
        }
        object = this.clearListeners();
        Object object2 = null;
        while (object != null) {
            Object object3 = object;
            object = object.next;
            object3.next = object2;
            object2 = object3;
        }
        while (object2 != null) {
            AbstractFuture.executeListener(object2.task, object2.executor);
            object2 = object2.next;
        }
        this.done();
    }

    void done() {
    }

    final void maybePropagateCancellation(Future<?> future) {
        if (future != null & this.isCancelled()) {
            future.cancel(this.wasInterrupted());
        }
    }

    private Waiter clearWaiters() {
        Waiter waiter;
        while (!ATOMIC_HELPER.casWaiters(this, waiter = this.waiters, Waiter.TOMBSTONE)) {
        }
        return waiter;
    }

    private Listener clearListeners() {
        Listener listener;
        while (!ATOMIC_HELPER.casListeners(this, listener = this.listeners, Listener.TOMBSTONE)) {
        }
        return listener;
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        }
        catch (RuntimeException runtimeException) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, runtimeException);
        }
    }

    static final CancellationException cancellationExceptionWithCause(String string, Throwable throwable) {
        CancellationException cancellationException = new CancellationException(string);
        cancellationException.initCause(throwable);
        return cancellationException;
    }

    static {
        AtomicHelper atomicHelper2;
        AtomicHelper atomicHelper2;
        GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        log = Logger.getLogger(AbstractFuture.class.getName());
        try {
            atomicHelper2 = new UnsafeAtomicHelper();
        }
        catch (Throwable throwable) {
            try {
                atomicHelper2 = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
            }
            catch (Throwable throwable2) {
                log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", throwable);
                log.log(Level.SEVERE, "SafeAtomicHelper is broken!", throwable2);
                atomicHelper2 = new SynchronizedHelper();
            }
        }
        ATOMIC_HELPER = atomicHelper2;
        Class<LockSupport> class_ = LockSupport.class;
        NULL = new Object();
    }

    private static final class SynchronizedHelper
    extends AtomicHelper {
        private SynchronizedHelper() {
            super();
        }

        @Override
        void putThread(Waiter waiter, Thread thread) {
            waiter.thread = thread;
        }

        @Override
        void putNext(Waiter waiter, Waiter waiter2) {
            waiter.next = waiter2;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            AbstractFuture abstractFuture2 = abstractFuture;
            synchronized (abstractFuture2) {
                if (abstractFuture.waiters == waiter) {
                    abstractFuture.waiters = waiter2;
                    return true;
                }
                return false;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            AbstractFuture abstractFuture2 = abstractFuture;
            synchronized (abstractFuture2) {
                if (abstractFuture.listeners == listener) {
                    abstractFuture.listeners = listener2;
                    return true;
                }
                return false;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        boolean casValue(AbstractFuture<?> abstractFuture, Object object, Object object2) {
            AbstractFuture abstractFuture2 = abstractFuture;
            synchronized (abstractFuture2) {
                if (abstractFuture.value == object) {
                    abstractFuture.value = object2;
                    return true;
                }
                return false;
            }
        }
    }

    private static final class SafeAtomicHelper
    extends AtomicHelper {
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> atomicReferenceFieldUpdater, AtomicReferenceFieldUpdater<Waiter, Waiter> atomicReferenceFieldUpdater2, AtomicReferenceFieldUpdater<AbstractFuture, Waiter> atomicReferenceFieldUpdater3, AtomicReferenceFieldUpdater<AbstractFuture, Listener> atomicReferenceFieldUpdater4, AtomicReferenceFieldUpdater<AbstractFuture, Object> atomicReferenceFieldUpdater5) {
            super();
            this.waiterThreadUpdater = atomicReferenceFieldUpdater;
            this.waiterNextUpdater = atomicReferenceFieldUpdater2;
            this.waitersUpdater = atomicReferenceFieldUpdater3;
            this.listenersUpdater = atomicReferenceFieldUpdater4;
            this.valueUpdater = atomicReferenceFieldUpdater5;
        }

        @Override
        void putThread(Waiter waiter, Thread thread) {
            this.waiterThreadUpdater.lazySet(waiter, thread);
        }

        @Override
        void putNext(Waiter waiter, Waiter waiter2) {
            this.waiterNextUpdater.lazySet(waiter, waiter2);
        }

        @Override
        boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            return this.waitersUpdater.compareAndSet(abstractFuture, waiter, waiter2);
        }

        @Override
        boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            return this.listenersUpdater.compareAndSet(abstractFuture, listener, listener2);
        }

        @Override
        boolean casValue(AbstractFuture<?> abstractFuture, Object object, Object object2) {
            return this.valueUpdater.compareAndSet(abstractFuture, object, object2);
        }
    }

    private static final class UnsafeAtomicHelper
    extends AtomicHelper {
        static final Unsafe UNSAFE;
        static final long LISTENERS_OFFSET;
        static final long WAITERS_OFFSET;
        static final long VALUE_OFFSET;
        static final long WAITER_THREAD_OFFSET;
        static final long WAITER_NEXT_OFFSET;

        private UnsafeAtomicHelper() {
            super();
        }

        @Override
        void putThread(Waiter waiter, Thread thread) {
            UNSAFE.putObject((Object)waiter, WAITER_THREAD_OFFSET, (Object)thread);
        }

        @Override
        void putNext(Waiter waiter, Waiter waiter2) {
            UNSAFE.putObject((Object)waiter, WAITER_NEXT_OFFSET, (Object)waiter2);
        }

        @Override
        boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, WAITERS_OFFSET, waiter, waiter2);
        }

        @Override
        boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, LISTENERS_OFFSET, listener, listener2);
        }

        @Override
        boolean casValue(AbstractFuture<?> abstractFuture, Object object, Object object2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, VALUE_OFFSET, object, object2);
        }

        static {
            Unsafe unsafe = null;
            try {
                unsafe = Unsafe.getUnsafe();
            }
            catch (SecurityException securityException) {
                try {
                    unsafe = (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>(){

                        @Override
                        public Unsafe run() throws Exception {
                            Class<Unsafe> class_ = Unsafe.class;
                            for (Field field : class_.getDeclaredFields()) {
                                field.setAccessible(true);
                                Object object = field.get(null);
                                if (!class_.isInstance(object)) continue;
                                return class_.cast(object);
                            }
                            throw new NoSuchFieldError("the Unsafe");
                        }
                    });
                }
                catch (PrivilegedActionException privilegedActionException) {
                    throw new RuntimeException("Could not initialize intrinsics", privilegedActionException.getCause());
                }
            }
            try {
                Class<AbstractFuture> class_ = AbstractFuture.class;
                WAITERS_OFFSET = unsafe.objectFieldOffset(class_.getDeclaredField("waiters"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(class_.getDeclaredField("listeners"));
                VALUE_OFFSET = unsafe.objectFieldOffset(class_.getDeclaredField("value"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            }
            catch (Exception exception) {
                throw Throwables.propagate(exception);
            }
        }

    }

    private static abstract class AtomicHelper {
        private AtomicHelper() {
        }

        abstract void putThread(Waiter var1, Thread var2);

        abstract void putNext(Waiter var1, Waiter var2);

        abstract boolean casWaiters(AbstractFuture<?> var1, Waiter var2, Waiter var3);

        abstract boolean casListeners(AbstractFuture<?> var1, Listener var2, Listener var3);

        abstract boolean casValue(AbstractFuture<?> var1, Object var2, Object var3);
    }

    private final class SetFuture
    implements Runnable {
        final ListenableFuture<? extends V> future;
        final /* synthetic */ AbstractFuture this$0;

        @Override
        public void run() {
            if (this.this$0.value != this) {
                return;
            }
            this.this$0.completeWithFuture(this.future, this);
        }
    }

    private static final class Cancellation {
        final boolean wasInterrupted;
        final Throwable cause;

        Cancellation(boolean bl, Throwable throwable) {
            this.wasInterrupted = bl;
            this.cause = throwable;
        }
    }

    private static final class Failure {
        static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future."){

            @Override
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        });
        final Throwable exception;

        Failure(Throwable throwable) {
            this.exception = Preconditions.checkNotNull(throwable);
        }

    }

    private static final class Listener {
        static final Listener TOMBSTONE = new Listener(null, null);
        final Runnable task;
        final Executor executor;
        Listener next;

        Listener(Runnable runnable, Executor executor) {
            this.task = runnable;
            this.executor = executor;
        }
    }

    private static final class Waiter {
        static final Waiter TOMBSTONE = new Waiter(false);
        volatile Thread thread;
        volatile Waiter next;

        Waiter(boolean bl) {
        }

        Waiter() {
            ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }

        void setNext(Waiter waiter) {
            ATOMIC_HELPER.putNext(this, waiter);
        }

        void unpark() {
            Thread thread = this.thread;
            if (thread != null) {
                this.thread = null;
                LockSupport.unpark(thread);
            }
        }
    }

    static abstract class TrustedFuture<V>
    extends AbstractFuture<V> {
        TrustedFuture() {
        }

        @Override
        public final V get() throws InterruptedException, ExecutionException {
            return super.get();
        }

        @Override
        public final V get(long l2, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return super.get(l2, timeUnit);
        }

        @Override
        public final boolean isDone() {
            return super.isDone();
        }

        @Override
        public final boolean isCancelled() {
            return super.isCancelled();
        }

        @Override
        public final void addListener(Runnable runnable, Executor executor) {
            super.addListener(runnable, executor);
        }
    }

}

