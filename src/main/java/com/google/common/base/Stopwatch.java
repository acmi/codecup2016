/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class Stopwatch {
    private final Ticker ticker = Ticker.systemTicker();
    private boolean isRunning;
    private long elapsedNanos;
    private long startTick;

    public static Stopwatch createUnstarted() {
        return new Stopwatch();
    }

    Stopwatch() {
    }

    public Stopwatch start() {
        Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
        this.isRunning = true;
        this.startTick = this.ticker.read();
        return this;
    }

    private long elapsedNanos() {
        return this.isRunning ? this.ticker.read() - this.startTick + this.elapsedNanos : this.elapsedNanos;
    }

    public long elapsed(TimeUnit timeUnit) {
        return timeUnit.convert(this.elapsedNanos(), TimeUnit.NANOSECONDS);
    }

    public String toString() {
        long l2 = this.elapsedNanos();
        TimeUnit timeUnit = Stopwatch.chooseUnit(l2);
        double d2 = (double)l2 / (double)TimeUnit.NANOSECONDS.convert(1, timeUnit);
        return String.format(Locale.ROOT, "%.4g %s", d2, Stopwatch.abbreviate(timeUnit));
    }

    private static TimeUnit chooseUnit(long l2) {
        if (TimeUnit.DAYS.convert(l2, TimeUnit.NANOSECONDS) > 0) {
            return TimeUnit.DAYS;
        }
        if (TimeUnit.HOURS.convert(l2, TimeUnit.NANOSECONDS) > 0) {
            return TimeUnit.HOURS;
        }
        if (TimeUnit.MINUTES.convert(l2, TimeUnit.NANOSECONDS) > 0) {
            return TimeUnit.MINUTES;
        }
        if (TimeUnit.SECONDS.convert(l2, TimeUnit.NANOSECONDS) > 0) {
            return TimeUnit.SECONDS;
        }
        if (TimeUnit.MILLISECONDS.convert(l2, TimeUnit.NANOSECONDS) > 0) {
            return TimeUnit.MILLISECONDS;
        }
        if (TimeUnit.MICROSECONDS.convert(l2, TimeUnit.NANOSECONDS) > 0) {
            return TimeUnit.MICROSECONDS;
        }
        return TimeUnit.NANOSECONDS;
    }

    private static String abbreviate(TimeUnit timeUnit) {
        switch (timeUnit) {
            case NANOSECONDS: {
                return "ns";
            }
            case MICROSECONDS: {
                return "\u03bcs";
            }
            case MILLISECONDS: {
                return "ms";
            }
            case SECONDS: {
                return "s";
            }
            case MINUTES: {
                return "min";
            }
            case HOURS: {
                return "h";
            }
            case DAYS: {
                return "d";
            }
        }
        throw new AssertionError();
    }

}

