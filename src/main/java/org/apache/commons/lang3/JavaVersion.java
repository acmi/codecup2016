/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3;

public final class JavaVersion
extends Enum<JavaVersion> {
    public static final /* enum */ JavaVersion JAVA_0_9 = new JavaVersion(1.5f, "0.9");
    public static final /* enum */ JavaVersion JAVA_1_1 = new JavaVersion(1.1f, "1.1");
    public static final /* enum */ JavaVersion JAVA_1_2 = new JavaVersion(1.2f, "1.2");
    public static final /* enum */ JavaVersion JAVA_1_3 = new JavaVersion(1.3f, "1.3");
    public static final /* enum */ JavaVersion JAVA_1_4 = new JavaVersion(1.4f, "1.4");
    public static final /* enum */ JavaVersion JAVA_1_5 = new JavaVersion(1.5f, "1.5");
    public static final /* enum */ JavaVersion JAVA_1_6 = new JavaVersion(1.6f, "1.6");
    public static final /* enum */ JavaVersion JAVA_1_7 = new JavaVersion(1.7f, "1.7");
    public static final /* enum */ JavaVersion JAVA_1_8 = new JavaVersion(1.8f, "1.8");
    public static final /* enum */ JavaVersion JAVA_1_9 = new JavaVersion(1.9f, "1.9");
    public static final /* enum */ JavaVersion JAVA_RECENT = new JavaVersion(JavaVersion.maxVersion(), Float.toString(JavaVersion.maxVersion()));
    private final float value;
    private final String name;
    private static final /* synthetic */ JavaVersion[] $VALUES;

    public static JavaVersion[] values() {
        return (JavaVersion[])$VALUES.clone();
    }

    private JavaVersion(float f2, String string2) {
        super(string, n2);
        this.value = f2;
        this.name = string2;
    }

    static JavaVersion get(String string) {
        int n2;
        int n3;
        if ("0.9".equals(string)) {
            return JAVA_0_9;
        }
        if ("1.1".equals(string)) {
            return JAVA_1_1;
        }
        if ("1.2".equals(string)) {
            return JAVA_1_2;
        }
        if ("1.3".equals(string)) {
            return JAVA_1_3;
        }
        if ("1.4".equals(string)) {
            return JAVA_1_4;
        }
        if ("1.5".equals(string)) {
            return JAVA_1_5;
        }
        if ("1.6".equals(string)) {
            return JAVA_1_6;
        }
        if ("1.7".equals(string)) {
            return JAVA_1_7;
        }
        if ("1.8".equals(string)) {
            return JAVA_1_8;
        }
        if ("1.9".equals(string)) {
            return JAVA_1_9;
        }
        if (string == null) {
            return null;
        }
        float f2 = JavaVersion.toFloatVersion(string);
        if ((double)f2 - 1.0 < 1.0 && Float.parseFloat(string.substring((n2 = Math.max(string.indexOf(46), string.indexOf(44))) + 1, n3 = Math.max(string.length(), string.indexOf(44, n2)))) > 0.9f) {
            return JAVA_RECENT;
        }
        return null;
    }

    public String toString() {
        return this.name;
    }

    private static float maxVersion() {
        float f2 = JavaVersion.toFloatVersion(System.getProperty("java.version", "2.0"));
        if (f2 > 0.0f) {
            return f2;
        }
        return 2.0f;
    }

    private static float toFloatVersion(String string) {
        String[] arrstring = string.split("\\.");
        if (arrstring.length >= 2) {
            try {
                return Float.parseFloat(arrstring[0] + '.' + arrstring[1]);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return -1.0f;
    }

    static {
        $VALUES = new JavaVersion[]{JAVA_0_9, JAVA_1_1, JAVA_1_2, JAVA_1_3, JAVA_1_4, JAVA_1_5, JAVA_1_6, JAVA_1_7, JAVA_1_8, JAVA_1_9, JAVA_RECENT};
    }
}

