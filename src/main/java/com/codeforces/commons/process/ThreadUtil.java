/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.process;

import com.codeforces.commons.exception.ExceptionUtil;
import java.io.PrintStream;
import java.util.Date;
import org.apache.log4j.Logger;

public class ThreadUtil {
    private static final Logger logger = Logger.getLogger(ThreadUtil.class);

    private ThreadUtil() {
        throw new UnsupportedOperationException();
    }

    public static Thread newThread(String string, Runnable runnable, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, long l2, boolean bl) {
        Thread thread2;
        if (string == null) {
            string = String.format("Unnamed thread by %s running %s at %s.", Thread.currentThread(), runnable.getClass().getName(), new Date());
        }
        Thread thread3 = thread2 = l2 > 0 ? new Thread(null, runnable, string, l2) : new Thread(null, runnable, string);
        if (bl) {
            thread2.setDaemon(true);
        }
        if (uncaughtExceptionHandler != null || Thread.getDefaultUncaughtExceptionHandler() == null) {
            if (uncaughtExceptionHandler == null) {
                uncaughtExceptionHandler = (thread, throwable) -> {
                    System.out.printf("Unexpected exception %s (%s) in %s:%n%s%n", throwable.getClass(), throwable.getMessage(), thread.getName(), ExceptionUtil.toString(throwable));
                    logger.error(String.format("Unexpected exception %s (%s) in %s.", throwable.getClass(), throwable.getMessage(), thread.getName()), throwable);
                };
            }
            thread2.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        return thread2;
    }

    public static Thread newThread(String string, Runnable runnable, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, boolean bl) {
        return ThreadUtil.newThread(string, runnable, uncaughtExceptionHandler, 0, bl);
    }

    public static void sleep(long l2) {
        try {
            Thread.sleep(l2);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    public static <T> T execute(Operation<T> operation, int n2, ExecutionStrategy executionStrategy) throws Throwable {
        ThreadUtil.ensureArguments(operation, n2, executionStrategy);
        for (int i2 = 1; i2 <= n2; ++i2) {
            try {
                return operation.run();
            }
            catch (Throwable throwable) {
                if (executionStrategy.getUnsuccessHandler() != null) {
                    executionStrategy.getUnsuccessHandler().handle(i2, throwable);
                }
                if (i2 < n2) {
                    if (i2 == 1) {
                        logger.info("Iteration #1 has been failed: " + throwable.getMessage(), throwable);
                    } else {
                        logger.warn("Iteration #" + i2 + " has been failed: " + throwable.getMessage(), throwable);
                    }
                } else {
                    logger.error("Iteration #" + i2 + " has been failed: " + throwable.getMessage(), throwable);
                    throw throwable;
                }
                ThreadUtil.sleep(executionStrategy.getDelayTimeMillis(i2));
                continue;
            }
        }
        throw new RuntimeException("This line shouldn't be executed.");
    }

    private static <T> void ensureArguments(Operation<T> operation, int n2, ExecutionStrategy executionStrategy) {
        if (operation == null) {
            throw new IllegalArgumentException("Argument 'operation' can't be 'null'.");
        }
        if (n2 < 1) {
            throw new IllegalArgumentException("Argument 'attemptCount' should be positive.");
        }
        if (executionStrategy == null) {
            throw new IllegalArgumentException("Argument 'strategy' can't be 'null'.");
        }
    }

    public static class ExecutionStrategy {
        private final long delayTimeMillis;
        private final Type type;
        private final UnsuccessHandler unsuccessHandler;

        public ExecutionStrategy(long l2, Type type) {
            this(l2, type, null);
        }

        public ExecutionStrategy(long l2, Type type, UnsuccessHandler unsuccessHandler) {
            ExecutionStrategy.ensureArguments(l2, type);
            this.delayTimeMillis = l2;
            this.type = type;
            this.unsuccessHandler = unsuccessHandler;
        }

        private static void ensureArguments(long l2, Type type) {
            if (l2 < 1) {
                throw new IllegalArgumentException("Argument 'delayTimeMillis' should be positive.");
            }
            if (type == null) {
                throw new IllegalArgumentException("Argument 'type' can't be 'null'.");
            }
        }

        public long getDelayTimeMillis(int n2) {
            if (n2 < 1) {
                throw new IllegalArgumentException("Argument 'attemptNumber' should be positive.");
            }
            switch (this.type) {
                case CONSTANT: {
                    return this.delayTimeMillis;
                }
                case LINEAR: {
                    return this.delayTimeMillis * (long)n2;
                }
                case SQUARE: {
                    return this.delayTimeMillis * (long)n2 * (long)n2;
                }
            }
            throw new IllegalArgumentException("Unknown strategy type '" + (Object)((Object)this.type) + "'.");
        }

        public UnsuccessHandler getUnsuccessHandler() {
            return this.unsuccessHandler;
        }

        public static final class Type
        extends Enum<Type> {
            public static final /* enum */ Type CONSTANT = new Type();
            public static final /* enum */ Type LINEAR = new Type();
            public static final /* enum */ Type SQUARE = new Type();
            private static final /* synthetic */ Type[] $VALUES;

            public static Type[] values() {
                return (Type[])$VALUES.clone();
            }

            private Type() {
                super(string, n2);
            }

            static {
                $VALUES = new Type[]{CONSTANT, LINEAR, SQUARE};
            }
        }

    }

    public static interface UnsuccessHandler {
        public void handle(int var1, Throwable var2);
    }

    public static interface Operation<T> {
        public T run() throws Throwable;
    }

}

