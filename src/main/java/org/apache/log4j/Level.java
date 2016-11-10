/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import java.io.Serializable;
import org.apache.log4j.Priority;

public class Level
extends Priority
implements Serializable {
    public static final Level OFF = new Level(Integer.MAX_VALUE, "OFF", 0);
    public static final Level FATAL = new Level(50000, "FATAL", 0);
    public static final Level ERROR = new Level(40000, "ERROR", 3);
    public static final Level WARN = new Level(30000, "WARN", 4);
    public static final Level INFO = new Level(20000, "INFO", 6);
    public static final Level DEBUG = new Level(10000, "DEBUG", 7);
    public static final Level TRACE = new Level(5000, "TRACE", 7);
    public static final Level ALL = new Level(Integer.MIN_VALUE, "ALL", 7);

    protected Level(int n2, String string, int n3) {
        super(n2, string, n3);
    }

    public static Level toLevel(String string, Level level) {
        if (string == null) {
            return level;
        }
        String string2 = string.toUpperCase();
        if (string2.equals("ALL")) {
            return ALL;
        }
        if (string2.equals("DEBUG")) {
            return DEBUG;
        }
        if (string2.equals("INFO")) {
            return INFO;
        }
        if (string2.equals("WARN")) {
            return WARN;
        }
        if (string2.equals("ERROR")) {
            return ERROR;
        }
        if (string2.equals("FATAL")) {
            return FATAL;
        }
        if (string2.equals("OFF")) {
            return OFF;
        }
        if (string2.equals("TRACE")) {
            return TRACE;
        }
        if (string2.equals("\u0130NFO")) {
            return INFO;
        }
        return level;
    }
}

