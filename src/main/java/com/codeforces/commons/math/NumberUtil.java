/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.math;

import com.codeforces.commons.math.Math;
import com.codeforces.commons.text.StringUtil;

public final class NumberUtil {
    public static Integer toInt(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Byte) {
            return ((Byte)object).byteValue();
        }
        if (object instanceof Short) {
            return (int)((Short)object);
        }
        if (object instanceof Integer) {
            return (Integer)object;
        }
        if (object instanceof Long) {
            return NumberUtil.toInt((Long)object);
        }
        if (object instanceof Float) {
            return NumberUtil.toInt(((Float)object).floatValue());
        }
        if (object instanceof Double) {
            return NumberUtil.toInt((Double)object);
        }
        if (object instanceof Number) {
            return NumberUtil.toInt(((Number)object).doubleValue());
        }
        return NumberUtil.toInt(Double.parseDouble(StringUtil.trim(object.toString())));
    }

    public static int toInt(long l2) {
        int n2 = (int)l2;
        if ((long)n2 == l2) {
            return n2;
        }
        throw new IllegalArgumentException("Can't convert long " + l2 + " to int.");
    }

    public static int toInt(float f2) {
        int n2 = (int)f2;
        if (Math.abs((float)n2 - f2) < 1.0f) {
            return n2;
        }
        throw new IllegalArgumentException("Can't convert float " + f2 + " to int.");
    }

    public static int toInt(double d2) {
        int n2 = (int)d2;
        if (Math.abs((double)n2 - d2) < 1.0) {
            return n2;
        }
        throw new IllegalArgumentException("Can't convert double " + d2 + " to int.");
    }

    public static Long toLong(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Byte) {
            return ((Byte)object).byteValue();
        }
        if (object instanceof Short) {
            return (long)((Short)object);
        }
        if (object instanceof Integer) {
            return (long)((Integer)object);
        }
        if (object instanceof Long) {
            return (Long)object;
        }
        if (object instanceof Float) {
            return NumberUtil.toLong(((Float)object).floatValue());
        }
        if (object instanceof Double) {
            return NumberUtil.toLong((Double)object);
        }
        if (object instanceof Number) {
            return NumberUtil.toLong(((Number)object).doubleValue());
        }
        return NumberUtil.toLong(Double.parseDouble(StringUtil.trim(object.toString())));
    }

    public static long toLong(float f2) {
        long l2 = (long)f2;
        if (Math.abs((float)l2 - f2) < 1.0f) {
            return l2;
        }
        throw new IllegalArgumentException("Can't convert float " + f2 + " to long.");
    }

    public static long toLong(double d2) {
        long l2 = (long)d2;
        if (Math.abs((double)l2 - d2) < 1.0) {
            return l2;
        }
        throw new IllegalArgumentException("Can't convert double " + d2 + " to long.");
    }

    public static Double toDouble(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Byte) {
            return ((Byte)object).byteValue();
        }
        if (object instanceof Short) {
            return (double)((Short)object);
        }
        if (object instanceof Integer) {
            return (double)((Integer)object);
        }
        if (object instanceof Long) {
            return (double)((Long)object);
        }
        if (object instanceof Float) {
            return ((Float)object).floatValue();
        }
        if (object instanceof Double) {
            return (Double)object;
        }
        if (object instanceof Number) {
            return ((Number)object).doubleValue();
        }
        return Double.parseDouble(StringUtil.trim(object.toString()));
    }

    public static boolean equals(Integer n2, Integer n3) {
        return n2 == null ? n3 == null : n2.equals(n3);
    }

    public static boolean equals(Long l2, Long l3) {
        return l2 == null ? l3 == null : l2.equals(l3);
    }

    public static boolean equals(Double d2, Double d3) {
        return d2 == null ? d3 == null : d2.equals(d3);
    }

    public static boolean nearlyEquals(Double d2, Double d3, double d4) {
        if (d2 == null) {
            return d3 == null;
        }
        if (d3 == null) {
            return false;
        }
        if (d2.equals(d3)) {
            return true;
        }
        if (Double.isInfinite(d2) || Double.isNaN(d2) || Double.isInfinite(d3) || Double.isNaN(d3)) {
            return false;
        }
        return Math.abs(d2 - d3) <= d4;
    }
}

