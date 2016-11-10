/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.a.a;

import com.codeforces.commons.collection.ArrayUtil;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class c {
    private static final Logger a = LoggerFactory.getLogger(c.class);
    private static final Lock b = new ReentrantLock();
    private static Random c = new Random(c.a());

    private c() {
        throw new UnsupportedOperationException();
    }

    private static SecureRandom g() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            a.error(String.format("Can't create 'SHA1PRNG' instance of %s. Switching to use a default instance.", SecureRandom.class.getSimpleName()), noSuchAlgorithmException);
            return new SecureRandom();
        }
    }

    public static long a() {
        return System.nanoTime() ^ Thread.currentThread().getId() + Runtime.getRuntime().maxMemory() * Runtime.getRuntime().freeMemory() & Runtime.getRuntime().totalMemory();
    }

    public static void a(boolean bl, long l2) {
        b.lock();
        try {
            c = bl ? c.g() : new Random();
            c.setSeed(l2);
        }
        finally {
            b.unlock();
        }
    }

    public static int a(int n2, int n3) {
        b.lock();
        try {
            int n4 = n2 + c.nextInt(n3 - n2 + 1);
            return n4;
        }
        finally {
            b.unlock();
        }
    }

    public static long b() {
        b.lock();
        try {
            long l2 = c.nextLong();
            return l2;
        }
        finally {
            b.unlock();
        }
    }

    public static boolean c() {
        b.lock();
        try {
            boolean bl = c.nextBoolean();
            return bl;
        }
        finally {
            b.unlock();
        }
    }

    public static double d() {
        b.lock();
        try {
            double d2 = c.nextDouble();
            return d2;
        }
        finally {
            b.unlock();
        }
    }

    public static String e() {
        return Hex.encodeHexString(c.a(16));
    }

    public static byte[] a(int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Argument 'length' must be a non-negative integer.");
        }
        if (n2 == 0) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        byte[] arrby = new byte[n2];
        b.lock();
        try {
            c.nextBytes(arrby);
        }
        finally {
            b.unlock();
        }
        return arrby;
    }

    public static double f() {
        return (c.d() - 0.5) * 6.283185307179586;
    }

    public static void a(List<?> list) {
        b.lock();
        try {
            Collections.shuffle(list, c);
        }
        finally {
            b.unlock();
        }
    }

    public static <T> void a(T[] arrT) {
        b.lock();
        try {
            ArrayUtil.shuffle(arrT, c);
        }
        finally {
            b.unlock();
        }
    }
}

