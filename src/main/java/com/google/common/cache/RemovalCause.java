/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

public abstract class RemovalCause
extends Enum<RemovalCause> {
    public static final /* enum */ RemovalCause EXPLICIT = new RemovalCause("EXPLICIT", 0){

        @Override
        boolean wasEvicted() {
            return false;
        }
    };
    public static final /* enum */ RemovalCause REPLACED = new RemovalCause("REPLACED", 1){

        @Override
        boolean wasEvicted() {
            return false;
        }
    };
    public static final /* enum */ RemovalCause COLLECTED = new RemovalCause("COLLECTED", 2){

        @Override
        boolean wasEvicted() {
            return true;
        }
    };
    public static final /* enum */ RemovalCause EXPIRED = new RemovalCause("EXPIRED", 3){

        @Override
        boolean wasEvicted() {
            return true;
        }
    };
    public static final /* enum */ RemovalCause SIZE = new RemovalCause("SIZE", 4){

        @Override
        boolean wasEvicted() {
            return true;
        }
    };
    private static final /* synthetic */ RemovalCause[] $VALUES;

    public static RemovalCause[] values() {
        return (RemovalCause[])$VALUES.clone();
    }

    private RemovalCause() {
        super(string, n2);
    }

    abstract boolean wasEvicted();

    static {
        $VALUES = new RemovalCause[]{EXPLICIT, REPLACED, COLLECTED, EXPIRED, SIZE};
    }

}

