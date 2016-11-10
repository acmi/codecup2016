/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExecutionList {
    static final Logger log = Logger.getLogger(ExecutionList.class.getName());
    private RunnableExecutorPair runnables;
    private boolean executed;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void add(Runnable runnable, Executor executor) {
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        ExecutionList executionList = this;
        synchronized (executionList) {
            if (!this.executed) {
                this.runnables = new RunnableExecutorPair(runnable, executor, this.runnables);
                return;
            }
        }
        ExecutionList.executeListener(runnable, executor);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void execute() {
        RunnableExecutorPair runnableExecutorPair;
        Object object = this;
        synchronized (object) {
            if (this.executed) {
                return;
            }
            this.executed = true;
            runnableExecutorPair = this.runnables;
            this.runnables = null;
        }
        object = null;
        while (runnableExecutorPair != null) {
            RunnableExecutorPair runnableExecutorPair2 = runnableExecutorPair;
            runnableExecutorPair = runnableExecutorPair.next;
            runnableExecutorPair2.next = object;
            object = runnableExecutorPair2;
        }
        while (object != null) {
            ExecutionList.executeListener(object.runnable, object.executor);
            object = object.next;
        }
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        }
        catch (RuntimeException runtimeException) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, runtimeException);
        }
    }

    private static final class RunnableExecutorPair {
        final Runnable runnable;
        final Executor executor;
        RunnableExecutorPair next;

        RunnableExecutorPair(Runnable runnable, Executor executor, RunnableExecutorPair runnableExecutorPair) {
            this.runnable = runnable;
            this.executor = executor;
            this.next = runnableExecutorPair;
        }
    }

}

