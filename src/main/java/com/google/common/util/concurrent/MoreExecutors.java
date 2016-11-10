/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.util.concurrent;

import java.util.concurrent.Executor;

public final class MoreExecutors {
    public static Executor directExecutor() {
        return DirectExecutor.INSTANCE;
    }

    private static final class DirectExecutor
    extends Enum<DirectExecutor>
    implements Executor {
        public static final /* enum */ DirectExecutor INSTANCE = new DirectExecutor();
        private static final /* synthetic */ DirectExecutor[] $VALUES;

        public static DirectExecutor[] values() {
            return (DirectExecutor[])$VALUES.clone();
        }

        private DirectExecutor() {
            super(string, n2);
        }

        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }

        public String toString() {
            return "MoreExecutors.directExecutor()";
        }

        static {
            $VALUES = new DirectExecutor[]{INSTANCE};
        }
    }

}

