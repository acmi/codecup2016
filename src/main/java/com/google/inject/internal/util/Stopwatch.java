/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.util;

import java.util.logging.Logger;

public final class Stopwatch {
    private static final Logger logger = Logger.getLogger(Stopwatch.class.getName());
    private long start = System.currentTimeMillis();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long reset() {
        long l2 = System.currentTimeMillis();
        try {
            long l3 = l2 - this.start;
            return l3;
        }
        finally {
            this.start = l2;
        }
    }

    public void resetAndLog(String string) {
        logger.fine(string + ": " + this.reset() + "ms");
    }
}

