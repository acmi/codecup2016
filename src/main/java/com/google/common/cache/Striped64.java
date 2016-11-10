/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import sun.misc.Unsafe;

abstract class Striped64
extends Number {
    static final ThreadLocal<int[]> threadHashCode = new ThreadLocal<T>();
    static final Random rng = new Random();
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    volatile transient Cell[] cells;
    volatile transient long base;
    volatile transient int busy;
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;

    Striped64() {
    }

    final boolean casBase(long l2, long l3) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, l2, l3);
    }

    final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    abstract long fn(long var1, long var3);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    final void retryUpdate(long l2, int[] arrn, boolean bl) {
        int n2;
        int n3;
        if (arrn == null) {
            arrn = new int[1];
            threadHashCode.set(arrn);
            n2 = rng.nextInt();
            arrn[0] = n2 == 0 ? 1 : n2;
            n3 = arrn[0];
        } else {
            n3 = arrn[0];
        }
        n2 = 0;
        do {
            int n4;
            long l3;
            Cell[] arrcell;
            if ((arrcell = this.cells) != null && (n4 = arrcell.length) > 0) {
                Cell[] arrcell2;
                int n5;
                Cell cell = arrcell[n4 - 1 & n3];
                if (cell == null) {
                    if (this.busy == 0) {
                        arrcell2 = new Cell[](l2);
                        if (this.busy == 0 && this.casBusy()) {
                            n5 = 0;
                            try {
                                int n6;
                                int n7;
                                Cell[] arrcell3 = this.cells;
                                if (arrcell3 != null && (n6 = arrcell3.length) > 0 && arrcell3[n7 = n6 - 1 & n3] == null) {
                                    arrcell3[n7] = arrcell2;
                                    n5 = 1;
                                }
                            }
                            finally {
                                this.busy = 0;
                            }
                            if (n5 == 0) continue;
                            return;
                        }
                    }
                    n2 = 0;
                } else if (!bl) {
                    bl = true;
                } else {
                    l3 = cell.value;
                    if (cell.cas(l3, this.fn(l3, l2))) return;
                    if (n4 >= NCPU || this.cells != arrcell) {
                        n2 = 0;
                    } else if (n2 == 0) {
                        n2 = 1;
                    } else if (this.busy == 0 && this.casBusy()) {
                        try {
                            if (this.cells == arrcell) {
                                arrcell2 = new Cell[n4 << 1];
                                for (n5 = 0; n5 < n4; ++n5) {
                                    arrcell2[n5] = arrcell[n5];
                                }
                                this.cells = arrcell2;
                            }
                        }
                        finally {
                            this.busy = 0;
                        }
                        n2 = 0;
                        continue;
                    }
                }
                n3 ^= n3 << 13;
                n3 ^= n3 >>> 17;
                n3 ^= n3 << 5;
                arrn[0] = n3;
                continue;
            }
            if (this.busy == 0 && this.cells == arrcell && this.casBusy()) {
                boolean bl2;
                bl2 = false;
                try {
                    if (this.cells == arrcell) {
                        Cell[] arrcell4 = new Cell[2];
                        arrcell4[n3 & 1] = new Cell(l2);
                        this.cells = arrcell4;
                        bl2 = true;
                    }
                }
                finally {
                    this.busy = 0;
                }
                if (!bl2) continue;
                return;
            }
            l3 = this.base;
            if (this.casBase(l3, this.fn(l3, l2))) return;
        } while (true);
    }

    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException securityException) {
            try {
                return (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>(){

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
    }

    static {
        try {
            UNSAFE = Striped64.getUnsafe();
            Class<Striped64> class_ = Striped64.class;
            baseOffset = UNSAFE.objectFieldOffset(class_.getDeclaredField("base"));
            busyOffset = UNSAFE.objectFieldOffset(class_.getDeclaredField("busy"));
        }
        catch (Exception exception) {
            throw new Error(exception);
        }
    }

    static final class Cell {
        volatile long value;
        private static final Unsafe UNSAFE;
        private static final long valueOffset;

        Cell(long l2) {
            this.value = l2;
        }

        final boolean cas(long l2, long l3) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, l2, l3);
        }

        static {
            try {
                UNSAFE = Striped64.getUnsafe();
                Class<Cell> class_ = Cell.class;
                valueOffset = UNSAFE.objectFieldOffset(class_.getDeclaredField("value"));
            }
            catch (Exception exception) {
                throw new Error(exception);
            }
        }
    }

}

