/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import com.google.common.base.Platform;

public abstract class Ticker {
    private static final Ticker SYSTEM_TICKER = new Ticker(){

        @Override
        public long read() {
            return Platform.systemNanoTime();
        }
    };

    protected Ticker() {
    }

    public abstract long read();

    public static Ticker systemTicker() {
        return SYSTEM_TICKER;
    }

}

