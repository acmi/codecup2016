/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

public final class JSE7 {
    public static final boolean AVAILABLE;

    static {
        boolean bl = true;
        try {
            Class.forName("java.nio.file.Path");
        }
        catch (ClassNotFoundException classNotFoundException) {
            bl = false;
        }
        AVAILABLE = bl;
    }
}

