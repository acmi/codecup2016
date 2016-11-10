/*
 * Decompiled with CFR 0_119.
 */
package a.a.a;

import java.io.PrintStream;

public class a {
    private static final boolean h;
    public static final byte a;
    public static final short b;
    public static final char c;
    public static final int d;
    public static final long e;
    public static final float f;
    public static final double g;

    static {
        String string;
        h = System.getProperty("gnu.trove.verbose", null) != null;
        String string2 = System.getProperty("gnu.trove.no_entry.byte", "0");
        int n2 = "MAX_VALUE".equalsIgnoreCase(string2) ? 127 : ("MIN_VALUE".equalsIgnoreCase(string2) ? -128 : (int)Byte.valueOf(string2).byteValue());
        if (n2 > 127) {
            n2 = 127;
        } else if (n2 < -128) {
            n2 = -128;
        }
        a = n2;
        if (h) {
            System.out.println("DEFAULT_BYTE_NO_ENTRY_VALUE: " + a);
        }
        n2 = "MAX_VALUE".equalsIgnoreCase(string2 = System.getProperty("gnu.trove.no_entry.short", "0")) ? 32767 : ("MIN_VALUE".equalsIgnoreCase(string2) ? -32768 : (int)Short.valueOf(string2).shortValue());
        if (n2 > 32767) {
            n2 = 32767;
        } else if (n2 < -32768) {
            n2 = -32768;
        }
        b = n2;
        if (h) {
            System.out.println("DEFAULT_SHORT_NO_ENTRY_VALUE: " + b);
        }
        n2 = "MAX_VALUE".equalsIgnoreCase(string2 = System.getProperty("gnu.trove.no_entry.char", "\u0000")) ? 65535 : ("MIN_VALUE".equalsIgnoreCase(string2) ? 0 : string2.toCharArray()[0]);
        if (n2 > 65535) {
            n2 = 65535;
        } else if (n2 < 0) {
            n2 = 0;
        }
        c = n2;
        if (h) {
            System.out.println("DEFAULT_CHAR_NO_ENTRY_VALUE: " + Integer.valueOf(n2));
        }
        n2 = "MAX_VALUE".equalsIgnoreCase(string2 = System.getProperty("gnu.trove.no_entry.int", "0")) ? Integer.MAX_VALUE : ("MIN_VALUE".equalsIgnoreCase(string2) ? Integer.MIN_VALUE : Integer.valueOf(string2));
        d = n2;
        if (h) {
            System.out.println("DEFAULT_INT_NO_ENTRY_VALUE: " + d);
        }
        long l2 = "MAX_VALUE".equalsIgnoreCase(string = System.getProperty("gnu.trove.no_entry.long", "0")) ? Long.MAX_VALUE : ("MIN_VALUE".equalsIgnoreCase(string) ? Long.MIN_VALUE : Long.valueOf(string));
        e = l2;
        if (h) {
            System.out.println("DEFAULT_LONG_NO_ENTRY_VALUE: " + e);
        }
        float f2 = "MAX_VALUE".equalsIgnoreCase(string2 = System.getProperty("gnu.trove.no_entry.float", "0")) ? Float.MAX_VALUE : ("MIN_VALUE".equalsIgnoreCase(string2) ? Float.MIN_VALUE : ("MIN_NORMAL".equalsIgnoreCase(string2) ? Float.MIN_NORMAL : ("NEGATIVE_INFINITY".equalsIgnoreCase(string2) ? Float.NEGATIVE_INFINITY : ("POSITIVE_INFINITY".equalsIgnoreCase(string2) ? Float.POSITIVE_INFINITY : Float.valueOf(string2).floatValue()))));
        f = f2;
        if (h) {
            System.out.println("DEFAULT_FLOAT_NO_ENTRY_VALUE: " + f);
        }
        double d2 = "MAX_VALUE".equalsIgnoreCase(string = System.getProperty("gnu.trove.no_entry.double", "0")) ? Double.MAX_VALUE : ("MIN_VALUE".equalsIgnoreCase(string) ? Double.MIN_VALUE : ("MIN_NORMAL".equalsIgnoreCase(string) ? Double.MIN_NORMAL : ("NEGATIVE_INFINITY".equalsIgnoreCase(string) ? Double.NEGATIVE_INFINITY : ("POSITIVE_INFINITY".equalsIgnoreCase(string) ? Double.POSITIVE_INFINITY : Double.valueOf(string)))));
        g = d2;
        if (h) {
            System.out.println("DEFAULT_DOUBLE_NO_ENTRY_VALUE: " + g);
        }
    }
}

