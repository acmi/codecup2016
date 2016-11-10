/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.collection;

import java.util.Random;
import java.util.function.Consumer;

public class ArrayUtil {
    public static <T> void shuffle(T[] arrT, Random random) {
        int n2;
        if (arrT != null && (n2 = arrT.length) > 1) {
            int n3 = n2;
            while (--n3 > 0) {
                int n4 = random.nextInt(n3 + 1);
                T t2 = arrT[n3];
                arrT[n3] = arrT[n4];
                arrT[n4] = t2;
            }
        }
    }

    @SafeVarargs
    public static /* varargs */ <T> void forEach(Consumer<? super T> consumer, T[] ... arrT) {
        for (T[] arrT2 : arrT) {
            int n2 = arrT2.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                consumer.accept(arrT2[i2]);
            }
        }
    }
}

