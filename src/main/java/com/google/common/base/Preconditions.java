/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

public final class Preconditions {
    public static void checkArgument(boolean bl) {
        if (!bl) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean bl, Object object) {
        if (!bl) {
            throw new IllegalArgumentException(String.valueOf(object));
        }
    }

    public static /* varargs */ void checkArgument(boolean bl, String string, Object ... arrobject) {
        if (!bl) {
            throw new IllegalArgumentException(Preconditions.format(string, arrobject));
        }
    }

    public static void checkState(boolean bl) {
        if (!bl) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean bl, Object object) {
        if (!bl) {
            throw new IllegalStateException(String.valueOf(object));
        }
    }

    public static /* varargs */ void checkState(boolean bl, String string, Object ... arrobject) {
        if (!bl) {
            throw new IllegalStateException(Preconditions.format(string, arrobject));
        }
    }

    public static <T> T checkNotNull(T t2) {
        if (t2 == null) {
            throw new NullPointerException();
        }
        return t2;
    }

    public static <T> T checkNotNull(T t2, Object object) {
        if (t2 == null) {
            throw new NullPointerException(String.valueOf(object));
        }
        return t2;
    }

    public static /* varargs */ <T> T checkNotNull(T t2, String string, Object ... arrobject) {
        if (t2 == null) {
            throw new NullPointerException(Preconditions.format(string, arrobject));
        }
        return t2;
    }

    public static int checkElementIndex(int n2, int n3) {
        return Preconditions.checkElementIndex(n2, n3, "index");
    }

    public static int checkElementIndex(int n2, int n3, String string) {
        if (n2 < 0 || n2 >= n3) {
            throw new IndexOutOfBoundsException(Preconditions.badElementIndex(n2, n3, string));
        }
        return n2;
    }

    private static String badElementIndex(int n2, int n3, String string) {
        if (n2 < 0) {
            return Preconditions.format("%s (%s) must not be negative", string, n2);
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("negative size: " + n3);
        }
        return Preconditions.format("%s (%s) must be less than size (%s)", string, n2, n3);
    }

    public static int checkPositionIndex(int n2, int n3) {
        return Preconditions.checkPositionIndex(n2, n3, "index");
    }

    public static int checkPositionIndex(int n2, int n3, String string) {
        if (n2 < 0 || n2 > n3) {
            throw new IndexOutOfBoundsException(Preconditions.badPositionIndex(n2, n3, string));
        }
        return n2;
    }

    private static String badPositionIndex(int n2, int n3, String string) {
        if (n2 < 0) {
            return Preconditions.format("%s (%s) must not be negative", string, n2);
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("negative size: " + n3);
        }
        return Preconditions.format("%s (%s) must not be greater than size (%s)", string, n2, n3);
    }

    public static void checkPositionIndexes(int n2, int n3, int n4) {
        if (n2 < 0 || n3 < n2 || n3 > n4) {
            throw new IndexOutOfBoundsException(Preconditions.badPositionIndexes(n2, n3, n4));
        }
    }

    private static String badPositionIndexes(int n2, int n3, int n4) {
        if (n2 < 0 || n2 > n4) {
            return Preconditions.badPositionIndex(n2, n4, "start index");
        }
        if (n3 < 0 || n3 > n4) {
            return Preconditions.badPositionIndex(n3, n4, "end index");
        }
        return Preconditions.format("end index (%s) must not be less than start index (%s)", n3, n2);
    }

    static /* varargs */ String format(String string, Object ... arrobject) {
        int n2;
        string = String.valueOf(string);
        StringBuilder stringBuilder = new StringBuilder(string.length() + 16 * arrobject.length);
        int n3 = 0;
        int n4 = 0;
        while (n4 < arrobject.length && (n2 = string.indexOf("%s", n3)) != -1) {
            stringBuilder.append(string.substring(n3, n2));
            stringBuilder.append(arrobject[n4++]);
            n3 = n2 + 2;
        }
        stringBuilder.append(string.substring(n3));
        if (n4 < arrobject.length) {
            stringBuilder.append(" [");
            stringBuilder.append(arrobject[n4++]);
            while (n4 < arrobject.length) {
                stringBuilder.append(", ");
                stringBuilder.append(arrobject[n4++]);
            }
            stringBuilder.append(']');
        }
        return stringBuilder.toString();
    }
}

