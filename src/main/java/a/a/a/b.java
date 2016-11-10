/*
 * Decompiled with CFR 0_119.
 */
package a.a.a;

public final class b {
    public static int a(int n2) {
        return n2;
    }

    public static int a(long l2) {
        return (int)(l2 ^ l2 >>> 32);
    }

    public static int a(Object object) {
        return object == null ? 0 : object.hashCode();
    }

    public static int a(float f2) {
        int n2 = (int)f2;
        if (f2 - (float)n2 > 0.0f) {
            ++n2;
        }
        return n2;
    }
}

