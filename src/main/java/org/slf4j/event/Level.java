/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.event;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class Level
extends Enum<Level> {
    public static final /* enum */ Level ERROR = new Level(40, "ERROR");
    public static final /* enum */ Level WARN = new Level(30, "WARN");
    public static final /* enum */ Level INFO = new Level(20, "INFO");
    public static final /* enum */ Level DEBUG = new Level(10, "DEBUG");
    public static final /* enum */ Level TRACE = new Level(0, "TRACE");
    private int levelInt;
    private String levelStr;
    private static final /* synthetic */ Level[] $VALUES;

    public static Level[] values() {
        return (Level[])$VALUES.clone();
    }

    private Level(int n3, String string2) {
        super(string, n2);
        this.levelInt = n3;
        this.levelStr = string2;
    }

    public String toString() {
        return this.levelStr;
    }

    static {
        $VALUES = new Level[]{ERROR, WARN, INFO, DEBUG, TRACE};
    }
}

